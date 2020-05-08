package com.hitnslab.dnssecurity.deeparcher.util

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExtensionUnitTests {
    private val logger = KotlinLogging.logger {}

    @Test
    fun test0() {
        val bs0 = byteArrayOf(56, 78, 90, 1)
        val bs1 = byteArrayOf(12, 34, 56, 78)
        val bs2 = byteArrayOf(13, 35, 57, 79)

        Assertions.assertEquals(2, bs0.intersectionSize(bs1, 1))
        Assertions.assertEquals(1, bs0.intersectionSize(bs1, 2))
        Assertions.assertEquals(-1, bs0.intersectionSize(bs1, 3))
        Assertions.assertEquals(0, bs0.intersectionSize(bs1, 4))

        Assertions.assertEquals(0, bs0.intersectionSize(bs2, 1))
        Assertions.assertEquals(0, bs0.intersectionSize(bs2, 2))
        Assertions.assertEquals(-1, bs0.intersectionSize(bs2, 3))
        Assertions.assertEquals(0, bs0.intersectionSize(bs2, 4))
    }
}