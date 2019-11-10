package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Bean("appDataSourceProperties")
    @ConfigurationProperties("app.datasource")
    fun appDataSourceProperties() = DataSourceProperties()

    @Bean("appDataSource")
    @ConfigurationProperties("app.datasource.hikari")
    fun appDataSource(@Qualifier("appDataSourceProperties") dataSourceProperties: DataSourceProperties): DataSource {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource::class.java)
                .build()
    }

    @Primary
    @Bean("internalDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    fun internalDataSourceProperties() = DataSourceProperties()

    @Primary
    @Bean("internalDataSource")
    @ConfigurationProperties("spring.datasource.hikari")
    fun dataSource(@Qualifier("internalDataSourceProperties") dataSourceProperties: DataSourceProperties): DataSource {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource::class.java)
                .build()
    }

}