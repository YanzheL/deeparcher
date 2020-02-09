package com.hitnslab.dnssecurity.pdnsdataloader.batch

import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import com.google.common.io.Files
import mu.KotlinLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.core.io.FileSystemResource
import java.io.IOException
import java.nio.file.Path


class FileHashTasklet : Tasklet {

    private val logger = KotlinLogging.logger {}

    val hashFunction: HashFunction = Hashing.murmur3_128()

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val fileLocation = chunkContext.stepContext.jobParameters["file"] as String
        val resource = FileSystemResource(Path.of(fileLocation))
        if (!resource.isReadable) {
            logger.error { "Cannot read file <$fileLocation>" }
            return RepeatStatus.FINISHED
        }
        val hashFileLocation = "$fileLocation.hash"
        val hashFile = FileSystemResource(Path.of(hashFileLocation))
        var hash: String? = null
        if (hashFile.isReadable) {
            logger.info { "Found hash file <$hashFileLocation>" }
            val size = hashFile.contentLength()
            if (size != hashFunction.bits() / 4L) {
                logger.warn { "Hash file has invalid length <$size>" }
            } else {
                val reader = hashFile.inputStream.reader()
                reader.use {
                    hash = reader.readText().toUpperCase()
                }
                logger.info { "Using pre-computed hash <$hash> for file <$fileLocation>" }
            }
        }
        if (hash == null) {
            hash = Files.asByteSource(resource.file)
                    .hash(hashFunction).toString().toUpperCase()
            val writer = hashFile.outputStream.writer()
            try {
                writer.use {
                    writer.write(hash)
                    logger.info { "Wrote hash back to <$hashFileLocation>" }
                }
            } catch (e: IOException) {
                logger.warn { "Failed to write hash back to <$hashFileLocation> with exception <${e.message}>" }
            }
        }
        logger.debug { "File <${resource.filename}> Hash <$hash>" }
        chunkContext.stepContext.stepExecution.executionContext.putString("hash", hash)
        return RepeatStatus.FINISHED
    }
}