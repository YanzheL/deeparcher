package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.config.job

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.converter.PDnsDataToProtoConverter
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch.ByCauseSkipPolicy
import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch.PDNSKafkaItemWriter
import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.config.PDnsJobProperties
import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.error.PDNSParseException
import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.parsing.PDNSLogFieldSetMapper
import com.hitnslab.dnssecurity.deeparcher.util.ProtobufMessagePredicate
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
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.transaction.CannotCreateTransactionException
import java.nio.file.Path

/**
 * Config LOAD_PDNS_DATA job.
 *
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
@Configuration
@EnableConfigurationProperties(PDnsJobProperties::class)
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
        itemReader: ItemReader<PDnsData.Builder>,
        itemWriter: ItemWriter<PDnsDataProto.PDnsData>,
        itemProcessor: ItemProcessor<PDnsData.Builder, PDnsDataProto.PDnsData?>
    ): Step {
        val config = properties.step
        val builder = stepBuilderFactory.get("LOAD_PDNS_DATA_STEP0")
            .chunk<PDnsData.Builder, PDnsDataProto.PDnsData>(properties.step.chunkSize)
            .reader(itemReader)
            .processor(itemProcessor)
            .writer(itemWriter)
            .faultTolerant()
            .skipPolicy(ByCauseSkipPolicy(PDNSParseException::class))
            .retryLimit(properties.step.retryLimit)
            .retry(CannotCreateTransactionException::class.java)
        config.transactionManager?.let {
            builder.transactionManager(
                applicationContext.getBean(it)
            )
        }
        return builder.build()
    }

    @Bean
    fun itemWriter(): ItemWriter<PDnsDataProto.PDnsData> {
        val config = properties.step.itemWriter
        val name = config.name
        if (name == "kafka") {
            val kafkaTemplate = applicationContext.getBean(KafkaTemplate::class.java)
            val itemWriter = PDNSKafkaItemWriter(kafkaTemplate as KafkaTemplate<String, PDnsDataProto.PDnsData>)
            itemWriter.successInterval = config.metrics.successInterval
            itemWriter.failureInterval = config.metrics.failureInterval
            itemWriter.metricsEnabled = config.metrics.enable
            return itemWriter
        }
        throw NotImplementedError("Unknown ItemWriter <$name>")
    }

    @Bean
    fun itemProcessor(): ItemProcessor<PDnsData.Builder, PDnsDataProto.PDnsData?> {
        val processorConfig = properties.step.itemProcessor
        val compositeItemProcessor = CompositeItemProcessor<PDnsData.Builder, PDnsDataProto.PDnsData?>()
        val processors = mutableListOf<ItemProcessor<*, *>>()
        processors.add(ItemProcessor<PDnsData.Builder, PDnsData?> { it.build() })
        val converter = PDnsDataToProtoConverter()
        processors.add(ItemProcessor<PDnsData, PDnsDataProto.PDnsData?> { converter.convert(it) })
        processorConfig.prefilters
            ?.map { filterSpec ->
                val filter = ProtobufMessagePredicate<PDnsDataProto.PDnsData>(
                    filterSpec.field,
                    Regex(filterSpec.pattern),
                    filterSpec.allow
                )
                ItemProcessor<PDnsDataProto.PDnsData, PDnsDataProto.PDnsData?> {
                    return@ItemProcessor if (filter.test(it)) it else null
                }
            }
            ?.forEach { processors.add(it) }
        compositeItemProcessor.setDelegates(processors)
        return compositeItemProcessor
    }

    @StepScope
    @Bean
    fun itemReader(
        @Value("#{jobParameters['file']}") filename: String,
        fieldSetMapper: PDNSLogFieldSetMapper
    ): FlatFileItemReader<PDnsData.Builder> {
        logger.info { "Reading file <$filename> ..." }
        return FlatFileItemReaderBuilder<PDnsData.Builder>()
            .name("reader")
            .resource(FileSystemResource(Path.of(filename)))
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