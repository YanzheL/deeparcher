package com.hitnslab.dnssecurity.pdnsdataloader.processing

import mu.KotlinLogging
import org.springframework.batch.core.step.skip.SkipPolicy
import kotlin.reflect.KClass

class ByCauseSkipPolicy<T : Throwable>(private val clazz: KClass<T>) : SkipPolicy {

    private val logger = KotlinLogging.logger {}

    override fun shouldSkip(t: Throwable, skipCount: Int): Boolean {
        val ret = clazz.isInstance(t.cause)
        if (ret) {
            logger.debug { "Skipped exception <${t.cause}>" }
        }
        return ret
    }
}