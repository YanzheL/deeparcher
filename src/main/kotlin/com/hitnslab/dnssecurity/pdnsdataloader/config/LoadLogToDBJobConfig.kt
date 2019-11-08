package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.hitnslab.dnssecurity.pdnsdataloader.io.PDNSPreparedStatementSetter
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsDataDAO
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.HITPDNSLogFieldSetMapper
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
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
class LoadLogToDBJobConfig {
    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun loadLogFromFileToDB(jobRepository: JobRepository, step: Step): Job {
        return jobBuilderFactory.get("LoadLogToDB")
                .repository(jobRepository)
                .start(step)
                .build()
    }

    @Bean
    fun step1(
            transactionManager: PlatformTransactionManager,
            itemReader: ItemReader<PDnsData>,
            itemWriter: ItemWriter<PDnsDataDAO>
    ): Step {
        return this.stepBuilderFactory.get("step1")
                .transactionManager(transactionManager)
                .chunk<PDnsData, PDnsDataDAO>(20000)
                .reader(itemReader)
                .processor(ItemProcessor<PDnsData, PDnsDataDAO> { PDnsDataDAO(it) })
                .writer(itemWriter)
                .build()
    }

    @StepScope
    @Bean
    fun itemReader(@Value("#{jobParameters[input]}") filename: String): FlatFileItemReader<PDnsData> {
        return FlatFileItemReaderBuilder<PDnsData>()
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

    @StepScope
    @Bean
    fun itemWriter(
            @Qualifier("AppDataSource") dataSource: DataSource,
            @Value("#{jobParameters[table]}") table: String
    ): ItemWriter<PDnsDataDAO> {
        return JdbcBatchItemWriterBuilder<PDnsDataDAO>()
                .dataSource(dataSource)
                .sql("INSERT INTO $table ( client_ip, domain, q_time, q_type, r_cnames, r_code, r_ips, top_priv_domain ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                .itemPreparedStatementSetter(PDNSPreparedStatementSetter())
                .build()
    }

}