package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.hitnslab.dnssecurity.pdnsdataloader.error.PDNSParseException
import com.hitnslab.dnssecurity.pdnsdataloader.io.PDNSPreparedStatementSetter
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsDataDAO
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.ByCauseSkipPolicy
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.DAOConverterProcessor
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.HITPDNSLogFieldSetMapper
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.PDNSLogFieldSetMapper
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.partition.PartitionHandler
import org.springframework.batch.core.partition.support.MultiResourcePartitioner
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.springframework.batch.item.support.CompositeItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.task.TaskExecutor
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
            partitionHandler: PartitionHandler
    ): Step {
        return this.stepBuilderFactory.get("masterStep")
                .partitioner("slaveStep", partitioner)
                .step(step)
                .partitionHandler(partitionHandler)
                .build()
    }

    @Bean
    fun slaveStep(
            transactionManager: PlatformTransactionManager,
            itemReader: ItemReader<PDnsData>,
            itemWriter: ItemWriter<PDnsDataDAO>,
            itemProcessor: ItemProcessor<PDnsData, PDnsDataDAO?>
    ): Step {
        return this.stepBuilderFactory.get("slaveStep")
                .transactionManager(transactionManager)
                .chunk<PDnsData, PDnsDataDAO>(120000)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .faultTolerant()
                .skipPolicy(ByCauseSkipPolicy(PDNSParseException::class))
                .retryLimit(10)
                .retry(CannotCreateTransactionException::class.java)
                .build()
    }

    @StepScope
    @Bean
    fun itemProcessor(): ItemProcessor<PDnsData, PDnsDataDAO?> {
        val compositeItemProcessor = CompositeItemProcessor<PDnsData, PDnsDataDAO?>()
        val processors = mutableListOf<ItemProcessor<*, *>>()
        processors.add(DAOConverterProcessor())
        compositeItemProcessor.setDelegates(processors)
        return compositeItemProcessor
    }

    @StepScope
    @Bean
    fun itemReader(
            @Value("#{stepExecutionContext['fileName']}") filename: String,
            fieldSetMapper: PDNSLogFieldSetMapper
    ): FlatFileItemReader<PDnsData> {
        logger.info { "Reading file <$filename> ..." }
        return FlatFileItemReaderBuilder<PDnsData>()
                .name("reader")
                .resource(UrlResource(filename))
                .fieldSetMapper(fieldSetMapper)
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
            @Qualifier("appDataSource") dataSource: DataSource,
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
        return partitioner
    }

    @JobScope
    @Bean
    fun partitionHandler(
            @Qualifier("threadPoolTaskExecutor") taskExecutor: TaskExecutor,
            @Qualifier("slaveStep") step: Step
    ): PartitionHandler {
        val retVal = TaskExecutorPartitionHandler()
        retVal.setTaskExecutor(taskExecutor)
        retVal.step = step
        retVal.gridSize = 24
        retVal.afterPropertiesSet()
        return retVal
    }
}