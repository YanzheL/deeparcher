package com.hitnslab.dnssecurity.pdnsdataloader.config.job

import com.hitnslab.dnssecurity.pdnsdataloader.config.PDnsJobProperties
import com.hitnslab.dnssecurity.pdnsdataloader.error.PDNSParseException
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.PDNSLogFieldSetMapper
import com.hitnslab.dnssecurity.pdnsdataloader.parsing.PDnsDataValidator
import com.hitnslab.dnssecurity.pdnsdataloader.processing.ByCauseSkipPolicy
import com.hitnslab.dnssecurity.pdnsdataloader.processing.PDNSKafkaItemWriter
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
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
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.core.io.UrlResource
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.transaction.CannotCreateTransactionException

class LoadPDNSDataJobConfig {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Autowired
    lateinit var jobRepository: JobRepository

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var properties: PDnsJobProperties

    @Bean
    fun job(step: Step): Job {
        return jobBuilderFactory.get("LOAD_PDNS_DATA")
                .repository(jobRepository)
                .start(step)
                .build()
    }

    @Bean
    fun step(
            itemReader: ItemReader<PDnsData>,
            itemWriter: ItemWriter<PDnsData>,
            itemProcessor: ItemProcessor<PDnsData, PDnsData?>
    ): Step {
        val config = properties.step
        val builder = stepBuilderFactory.get("LOAD_PDNS_DATA_STEP0")
                .chunk<PDnsData, PDnsData>(properties.step.chunkSize)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .faultTolerant()
                .skipPolicy(ByCauseSkipPolicy(PDNSParseException::class))
                .retryLimit(properties.step.retryLimit)
                .retry(CannotCreateTransactionException::class.java)
        val manager = config.transaction.manager
        if (manager != null) {
            val transactionManager = applicationContext.getBean(manager)
            builder.transactionManager(transactionManager)
        }
        return builder.build()
    }

    @Bean
    fun itemWriter(): ItemWriter<PDnsData> {
        val name = properties.step.itemWriter.name
        if (name == "kafka") {
            val kafkaTemplate = applicationContext.getBean(KafkaTemplate::class.java)
            return PDNSKafkaItemWriter(kafkaTemplate as KafkaTemplate<String, PDnsData>)
        }
        throw NotImplementedError("Unknown ItemWriter <$name>")
    }

    @Bean
    fun itemProcessor(): ItemProcessor<PDnsData, PDnsData?> {
        val compositeItemProcessor = CompositeItemProcessor<PDnsData, PDnsData?>()
        val processors = mutableListOf<ItemProcessor<*, *>>()
        processors.add(PDnsDataValidator())
        compositeItemProcessor.setDelegates(processors)
        return compositeItemProcessor
    }

    @StepScope
    @Bean
    fun itemReader(
            @Value("#{jobParameters['file']}") filename: String,
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
        return properties.step.itemReader.fieldSetMapper.getConstructor().newInstance()
    }
}