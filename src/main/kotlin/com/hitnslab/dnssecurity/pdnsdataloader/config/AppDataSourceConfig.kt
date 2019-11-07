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
class AppDataSourceConfig {

    @Bean(name = ["AppDataSourceProperties"])
    @ConfigurationProperties("app.datasource")
    fun appDataSourceProperties() = DataSourceProperties()

    @Bean(name = ["AppDataSource"])
    @ConfigurationProperties("app.datasource.configuration")
    fun appDataSource(@Qualifier("AppDataSourceProperties") dataSourceProperties: DataSourceProperties): DataSource {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource::class.java)
                .build()
    }

    @Primary
    @Bean(name = ["InternalDataSourceProperties"])
    @ConfigurationProperties("spring.datasource")
    fun internalDataSourceProperties() = DataSourceProperties()

    @Primary
    @Bean(name = ["InternalDataSource"])
    @ConfigurationProperties("spring.datasource.configuration")
    fun dataSource(@Qualifier("InternalDataSourceProperties") dataSourceProperties: DataSourceProperties): DataSource {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource::class.java)
                .build()
    }

}