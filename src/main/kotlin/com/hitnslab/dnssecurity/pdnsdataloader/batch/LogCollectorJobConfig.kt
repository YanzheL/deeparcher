package com.hitnslab.dnssecurity.pdnsdataloader.batch

import com.hitnslab.dnssecurity.pdnsdataloader.config.PDnsJobProperties
import com.hitnslab.dnssecurity.pdnsdataloader.error.PDNSParseException
import com.hitnslab.dnssecurity.pdnsdataloader.io.PDNSKafkaItemWriter
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsDataDAO
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.ByCauseSkipPolicy
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.DAOConverterProcessor
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.PDNSLogFieldSetMapper
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.partition.PartitionHandler
import org.springframework.batch.core.partition.support.MultiResourcePartitioner
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.springframework.batch.item.support.CompositeItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.task.TaskExecutor
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.transaction.CannotCreateTransactionException
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class LogCollectorJobConfig {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var jobRepository: JobRepository

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var properties: PDnsJobProperties

    @Bean("LogCollectorJob")
    fun job(
            masterStep: Step
    ): Job {
        return jobBuilderFactory.get("LogCollector")
                .repository(jobRepository)
                .start(masterStep)
                .build()
    }


    @Bean
    fun masterStep(
            slaveStep: Step,
            partitioner: Partitioner,
            partitionHandler: PartitionHandler
    ): Step {
        return stepBuilderFactory.get("LogCollectorMasterStep")
                .partitioner("LogCollectorSlaveStep", partitioner)
                .step(slaveStep)
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
        return stepBuilderFactory.get("LogCollectorSlaveStep")
                .transactionManager(transactionManager)
                .chunk<PDnsData, PDnsDataDAO>(properties.slaveStep!!.chunkSize)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .faultTolerant()
                .skipPolicy(ByCauseSkipPolicy(PDNSParseException::class))
                .retryLimit(properties.slaveStep!!.retryLimit)
                .retry(CannotCreateTransactionException::class.java)
                .build()
    }

    @Bean
    @ConditionalOnProperty(name = ["app.job.slave-step.item-writer.name"], havingValue = "kafka")
    fun kafkaItemWriter(kafkaTemplate: KafkaTemplate<*, *>): ItemWriter<PDnsDataDAO> {
        return PDNSKafkaItemWriter(kafkaTemplate as KafkaTemplate<String, PDnsDataDAO>)
    }

    @Bean
    fun partitionHandler(
            taskExecutor: TaskExecutor,
            slaveStep: Step
    ): PartitionHandler {
        val retVal = TaskExecutorPartitionHandler()
        retVal.setTaskExecutor(taskExecutor)
        retVal.step = slaveStep
        retVal.gridSize = 24
        retVal.afterPropertiesSet()
        return retVal
    }

    @StepScope
    @Bean
    fun partitioner(
            @Value("#{jobParameters['pattern']}") pattern: String
    ): Partitioner {
        val partitioner = MultiResourcePartitioner()
        val resolver = PathMatchingResourcePatternResolver()
        val resources = resolver.getResources(pattern)
        resources.sortBy(Resource::getFilename)
        partitioner.setResources(resources)
        return partitioner
    }

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
        return properties.slaveStep!!.itemReader!!.fieldSetMapper.getConstructor().newInstance()
    }
}