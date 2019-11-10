package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.hitnslab.dnssecurity.pdnsdataloader.error.DomainValidationException
import com.hitnslab.dnssecurity.pdnsdataloader.io.PDNSPreparedStatementSetter
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsDataDAO
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.ByCauseSkipPolicy
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.HITPDNSLogFieldSetMapper
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.PDNSLogFieldSetMapper
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.partition.support.MultiResourcePartitioner
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.CannotCreateTransactionException
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
@EnableBatchProcessing
class LoadLogToDBJobConfig {

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    private val logger = KotlinLogging.logger {}

    @Bean
    fun job(jobRepository: JobRepository, @Qualifier("masterStep") step: Step): Job {
        return jobBuilderFactory.get("LoadLogToDB")
                .repository(jobRepository)
                .start(step)
                .build()
    }

    @Bean
    fun masterStep(
            @Qualifier("slaveStep") step: Step,
            @Qualifier("partitioner") partitioner: Partitioner,
            @Qualifier("taskExecutor") taskExecutor: TaskExecutor
    ): Step {
        return this.stepBuilderFactory.get("masterStep")
                .partitioner("slaveStep", partitioner)
                .step(step)
                .taskExecutor(taskExecutor)
                .build()
    }

    @Bean
    fun slaveStep(
            transactionManager: PlatformTransactionManager,
            itemReader: ItemReader<PDnsData>,
            itemWriter: ItemWriter<PDnsDataDAO>
    ): Step {
        return this.stepBuilderFactory.get("slaveStep")
                .transactionManager(transactionManager)
                .chunk<PDnsData, PDnsDataDAO>(100000)
                .reader(itemReader)
                .processor(ItemProcessor<PDnsData, PDnsDataDAO> { PDnsDataDAO(it) })
                .writer(itemWriter)
                .faultTolerant()
                .skipPolicy(ByCauseSkipPolicy(DomainValidationException::class))
                .retryLimit(3)
                .retry(CannotCreateTransactionException::class.java)
                .build()
    }

    @StepScope
    @Bean
    fun itemReader(
            @Value("#{stepExecutionContext['fileName']}") filename: String
    ): FlatFileItemReader<PDnsData> {
        logger.info { "Reading file <$filename> ..." }
        return FlatFileItemReaderBuilder<PDnsData>()
                .name("reader")
                .resource(UrlResource(filename))
                .fieldSetMapper(fieldSetMapper())
                .lineTokenizer { line: String? ->
                    if (line == null)
                        DefaultFieldSet(arrayOf())
                    else
                        DefaultFieldSet(line.split("\\s+".toRegex()).toTypedArray())
                }
                .build()
    }

    @Bean
    fun fieldSetMapper(): PDNSLogFieldSetMapper {
        return HITPDNSLogFieldSetMapper()
    }

    @StepScope
    @Bean
    fun itemWriter(
            @Qualifier("AppDataSource") dataSource: DataSource,
            @Value("#{jobParameters['table']}") table: String,
            pdnsPreparedStatementSetter: PDNSPreparedStatementSetter
    ): JdbcBatchItemWriter<PDnsDataDAO> {
        return JdbcBatchItemWriterBuilder<PDnsDataDAO>()
                .dataSource(dataSource)
                .sql("INSERT INTO $table ( client_ip, domain, q_time, q_type, r_cnames, r_code, r_ips, top_priv_domain ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                .itemPreparedStatementSetter(pdnsPreparedStatementSetter)
                .build()
    }

    @StepScope
    @Bean
    fun partitioner(
            @Value("#{jobParameters['pattern']}") pattern: String
    ): Partitioner {
//        PartitionStepBuilder()
        val partitioner = MultiResourcePartitioner()
        val resolver = PathMatchingResourcePatternResolver()
        val resources = resolver.getResources(pattern)
        resources.sortBy(Resource::getFilename)
        partitioner.setResources(resources)
        partitioner.partition(24)
        return partitioner
    }

    @JobScope
    @Bean
    fun taskExecutor(
            @Value("#{jobParameters['min-pool-size'] ?: 24}") minPoolSize: Int,
            @Value("#{jobParameters['max-pool-size'] ?: 24}") maxPoolSize: Int,
            @Value("#{jobParameters['queue-capability'] ?: 0}") queueCapability: Int
    ): ThreadPoolTaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.maxPoolSize = maxPoolSize
        taskExecutor.corePoolSize = minPoolSize
        taskExecutor.setQueueCapacity(queueCapability)
        taskExecutor.afterPropertiesSet()
        return taskExecutor
    }
}