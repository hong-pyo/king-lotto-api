package com.king.lottoapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.config.SocketConfig
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate


/**
 *   Created by hong-pyo on 05/05/2020
 *   Time : 2:22 오전
 */

@Configuration
@Profile("!test")
class RestTemplateConfig(@Value("\${server.tomcat.max-threads}") private val maxThread: Int) {
    val readTimeout = 5 * 1000
    val connectionTimeout = 1 * 1000
    val socketTimeout = 5 * 1000

    @Bean
    fun httpRequestFactory() =
            HttpComponentsClientHttpRequestFactory().apply {
                httpClient = HttpClientBuilder.create()
                        .setConnectionManager(PoolingHttpClientConnectionManager().apply {
                            defaultMaxPerRoute = maxThread
                            maxTotal = maxThread * 3
                        })
                        .disableCookieManagement()
                        .setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(socketTimeout).build())
                        .build()
                setReadTimeout(readTimeout)
                setConnectTimeout(connectionTimeout)
                setConnectionRequestTimeout(connectionTimeout)
            }

    @Bean
    fun restTemplate(builder: RestTemplateBuilder, objectMapper: ObjectMapper) : RestTemplate =
            builder.additionalMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
                    .requestFactory(this::httpRequestFactory).build()
}