package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch

import mu.KotlinLogging
import org.springframework.batch.core.step.skip.SkipPolicy
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class ByCauseSkipPolicy<T : Throwable>(private val clazz: KClass<T>) : SkipPolicy {

    private val logger = KotlinLogging.logger {}

    override fun shouldSkip(t: Throwable, skipCount: Int): Boolean {
        val target = if (t.cause == null) t else t.cause
        val ret = clazz.isSuperclassOf(target!!::class)
        if (ret) {
            logger.debug { "Skipped exception <${target}>" }
        }
        return ret
    }
}