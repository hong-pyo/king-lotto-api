package com.king.lottoapi.config

import com.king.lottoapi.config.ReplicationRoutingDataSource.Companion.masterDbKey
import com.king.lottoapi.config.ReplicationRoutingDataSource.Companion.slaveDbKey
import com.king.lottoapi.helper.logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.*
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


/**
 *   Created by hong-pyo on 06/05/2020
 *   Time : 1:42 오전
 */
@Configuration
@EnableJpaRepositories(basePackages = ["com.king.lottoapi"])
@EnableTransactionManagement
@EnableJpaAuditing
@EnableConfigurationProperties(MasterDataSourceConfig::class, SlaveDataSourceConfig::class)
@Profile("!test")
class DataSourceConfig {

    @Bean
    @Primary
    fun dataSource(masterDataSourceConfig: MasterDataSourceConfig, slaveDataSourceConfig: SlaveDataSourceConfig) : DataSource {
        val masterDataSource = HikariDataSource(masterDataSourceConfig)
        val slaveDataSource = HikariDataSource(slaveDataSourceConfig)

        return LazyConnectionDataSourceProxy(
                ReplicationRoutingDataSource().apply {
                    this.setTargetDataSources(mapOf(slaveDbKey to slaveDataSource, masterDbKey to masterDataSource))
                    this.setDefaultTargetDataSource(masterDataSource)
                    this.afterPropertiesSet()
                })
    }

    @Bean
    fun entityManagerFactory(datasource : DataSource, jpaProperties: JpaProperties) : LocalContainerEntityManagerFactoryBean =
            LocalContainerEntityManagerFactoryBean().apply {
                this.jpaVendorAdapter = HibernateJpaVendorAdapter().apply { setShowSql(jpaProperties.isShowSql) }
                this.dataSource = datasource
                this.setPackagesToScan("com.king.lottoapi")
                this.persistenceUnitName = "king"

                val properties = Properties()
                val hibernateSettings = HibernateSettings().ddlAuto { "none"}
                properties.putAll(HibernateProperties().determineHibernateProperties(jpaProperties.properties, hibernateSettings))
                this.setJpaProperties(properties)
            }

    @Bean
    fun transactionManager(entityManagerFactory : EntityManagerFactory) : PlatformTransactionManager =
            JpaTransactionManager(entityManagerFactory)
}

@Configuration
@PropertySource("classpath:/db/db-\${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "datasource.master")
@Profile("!test")
class MasterDataSourceConfig : HikariConfig()


@Configuration
@PropertySource("classpath:/db/db-\${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "datasource.slave")
@Profile("!test")
class SlaveDataSourceConfig : HikariConfig()

class ReplicationRoutingDataSource : AbstractRoutingDataSource() {

    private val log = logger()

    override fun determineCurrentLookupKey(): Any {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            log.info("slave is selected")
            return slaveDbKey
        }

        log.info("master is selected")
        return masterDbKey
    }
    companion object {
        const val masterDbKey = "master"
        const val slaveDbKey = "slave"
    }
}