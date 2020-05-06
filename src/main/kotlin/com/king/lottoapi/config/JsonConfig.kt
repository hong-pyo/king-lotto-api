package com.king.lottoapi.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.io.IOException
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/**
 *   Created by hong-pyo on 07/05/2020
 *   Time : 12:42 오전
 */
@Configuration
class JsonConfig {
    @Bean
    fun objectMapper() : ObjectMapper = Jackson2ObjectMapperBuilder
            .json()
            .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .modules(createJavaTimeModule(FORMATTER),
                    createJavaDateModule(DATE_FORMATTER),
                    KotlinModule(),
                    createBigDecimalModule()
                    )
            .build()

    companion object {
        private val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        private val DATE_FORMATTER = DateTimeFormatter.ISO_DATE

        fun createJavaTimeModule(formatter: DateTimeFormatter) : JavaTimeModule = JavaTimeModule().apply {
            addSerializer(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime>() {
                @Throws(IOException::class)
                override fun serialize(value: LocalDateTime, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeString(value.truncatedTo(ChronoUnit.SECONDS).atZone(ZoneId.systemDefault()).format(formatter))
                }
            })
        }

        fun createJavaDateModule(formatter: DateTimeFormatter) : JavaTimeModule = JavaTimeModule().apply {
            addSerializer(LocalDate::class.java, object : JsonSerializer<LocalDate>() {
                @Throws(IOException::class)
                override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeString(value.format(formatter))
                }
            })

            addDeserializer(LocalDate::class.java, object :JsonDeserializer<LocalDate>() {
                @Throws(IOException::class)
                override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate {
                    return LocalDate.parse(p.valueAsString, formatter)
                }
            })
        }

        private fun createBigDecimalModule() : SimpleModule = SimpleModule().apply {
            addSerializer(BigDecimal::class.java, object : JsonSerializer<BigDecimal> () {
                @Throws(IOException::class)
                override fun serialize(value: BigDecimal, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeString(value.stripTrailingZeros().toPlainString())
                }
            })
        }
    }
}