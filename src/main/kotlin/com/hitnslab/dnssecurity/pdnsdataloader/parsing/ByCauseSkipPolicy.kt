package com.hitnslab.dnssecurity.pdnsdataloader.parsing

import org.springframework.batch.core.step.skip.SkipPolicy
import kotlin.reflect.KClass

class ByCauseSkipPolicy<T : Throwable>(private val clazz: KClass<T>) : SkipPolicy {

    override fun shouldSkip(t: Throwable, skipCount: Int) = clazz.isInstance(t.cause)
}