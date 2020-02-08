package com.hitnslab.dnssecurity.pdnsdataloader.batch

import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import com.google.common.io.Files
import mu.KotlinLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.core.io.UrlResource


class FileHashTasklet : Tasklet {

    private val logger = KotlinLogging.logger {}

    val hashFunction: HashFunction = Hashing.murmur3_128()

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val resource = UrlResource(chunkContext.stepContext.jobParameters["file"] as String)
        if (!resource.exists() || !resource.isFile) {
            logger.warn { "Skip invalid file <${resource.filename}>" }
            return RepeatStatus.FINISHED
        }
        val hash = Files.asByteSource(resource.file)
                .hash(hashFunction).toString().toUpperCase()
        logger.debug { "File <${resource.filename}> Hash <$hash>" }
        chunkContext.stepContext.stepExecution.executionContext.putString("hash", hash)
        return RepeatStatus.FINISHED
    }
}