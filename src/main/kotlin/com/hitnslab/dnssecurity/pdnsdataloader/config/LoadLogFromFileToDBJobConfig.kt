package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.hitnslab.dnssecurity.pdnsdataloader.io.PDNSPreparedStatementSetter
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDNSInfo
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.HITPDNSLogFieldSetMapper
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
@EnableBatchProcessing
class LoadLogFromFileToDBJobConfig {
    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun loadLogFromFileToDB(jobRepository: JobRepository, step: Step): Job {
        return jobBuilderFactory.get("loadLogFromFileToDB")
                .repository(jobRepository)
                .start(step)
                .build()
    }

    @Bean
    fun step1(
            transactionManager: PlatformTransactionManager,
            itemReader: ItemReader<PDNSInfo>,
            itemWriter: ItemWriter<PDNSInfo>
    ): Step {
        return this.stepBuilderFactory.get("step1")
                .transactionManager(transactionManager)
                .chunk<PDNSInfo, PDNSInfo>(20000)
                .reader(itemReader)
                .writer(itemWriter)
                .build()
    }

    @StepScope
    @Bean
    fun itemReader(@Value("#{jobParameters[input]}") filename: String): FlatFileItemReader<PDNSInfo> {
        return FlatFileItemReaderBuilder<PDNSInfo>()
                .name("reader")
                .resource(FileSystemResource(filename))
                .fieldSetMapper(HITPDNSLogFieldSetMapper())
                .lineTokenizer { line: String? ->
                    if (line == null)
                        DefaultFieldSet(arrayOf())
                    else
                        DefaultFieldSet(line.split("\\s+".toRegex()).toTypedArray())
                }
                .build()
    }

    @Bean
    fun itemWriter(@Qualifier("AppDataSource") dataSource: DataSource): ItemWriter<PDNSInfo> {
        return JdbcBatchItemWriterBuilder<PDNSInfo>()
                .dataSource(dataSource)
                .sql("INSERT INTO raw_dns_log ( client_ip, domain, q_time, q_type, r_cnames, r_code, r_ips, r_ttl ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                .itemPreparedStatementSetter(PDNSPreparedStatementSetter())
                .build()
    }

}