package com.hitnslab.dnssecurity.deeparcher.util

import com.google.protobuf.ByteString
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ByteBufSetUnitTests {

    private val logger = KotlinLogging.logger {}

    private val allocator = ByteBufAllocator.DEFAULT

    @Test
    fun test1() {
        val bs0 = byteArrayOf(56, 78, 90, 1)
        val bs1 = byteArrayOf(12, 34, 56, 78)
        val bs2 = byteArrayOf(34, 56, 78, 90)
        val bufSet = ByteBufSet(bs0)
        bufSet.add(Unpooled.wrappedBuffer(bs1))
        bufSet.add(Unpooled.wrappedBuffer(bs2))
        Assertions.assertTrue(bufSet.contains(Unpooled.wrappedBuffer(bs1)))
        Assertions.assertTrue(bufSet.contains(Unpooled.wrappedBuffer(bs2)))
        Assertions.assertTrue(bufSet.contains(Unpooled.wrappedBuffer(byteArrayOf(56, 78, 90, 1))))
        Assertions.assertFalse(bufSet.contains(Unpooled.wrappedBuffer(byteArrayOf(12, 34, 56, 78, 34, 56))))
        Assertions.assertFalse(bufSet.contains(Unpooled.wrappedBuffer(byteArrayOf(34, 56, 78, 34))))
        logger.info { ByteBufUtil.getBytes(bufSet.buffer).asList() }
    }

    @Test
    fun test2() {
        val bs0 = allocator.directBuffer()
        bs0.writeBytes(byteArrayOf(56, 78, 90, 1))
        val bs1 = allocator.heapBuffer()
        bs1.writeBytes(byteArrayOf(12, 34, 56, 78))
        val bs2 = ByteString.copyFrom(byteArrayOf(34, 56, 78, 90))

        val bufSet = ByteBufSet(Unpooled.wrappedBuffer(bs0))
        Assertions.assertTrue(bufSet.add(Unpooled.wrappedBuffer(bs1)))
        Assertions.assertTrue(bufSet.add(Unpooled.wrappedBuffer(bs2.asReadOnlyByteBuffer())))
        Assertions.assertFalse(bufSet.add(Unpooled.wrappedBuffer(byteArrayOf(34, 56, 78, 90))))
        Assertions.assertTrue(bufSet.contains(Unpooled.wrappedBuffer(bs1)))
        Assertions.assertTrue(bufSet.contains(Unpooled.wrappedBuffer(bs2.toByteArray())))
        Assertions.assertTrue(bufSet.contains(Unpooled.wrappedBuffer(byteArrayOf(56, 78, 90, 1))))
        Assertions.assertFalse(bufSet.contains(Unpooled.wrappedBuffer(byteArrayOf(12, 34, 56, 78, 34, 56))))
        Assertions.assertFalse(bufSet.contains(Unpooled.wrappedBuffer(byteArrayOf(34, 56, 78, 34))))
        logger.info { ByteBufUtil.getBytes(bufSet.buffer).asList() }
        bs0.release()
    }

    @Test
    fun test3() {
        val bs0 = allocator.directBuffer()
        bs0.writeBytes(byteArrayOf(56, 78, 90, 1))
        val bs1 = allocator.heapBuffer()
        bs1.writeBytes(byteArrayOf(12, 34, 56, 78))
        Assertions.assertEquals(ByteBufSet.intersectionSize(bs0.retainedSlice(), bs1.retainedSlice(), 1), 2)
        Assertions.assertEquals(ByteBufSet.intersectionSize(bs0.retainedSlice(), bs1.retainedSlice(), 2), 1)
        Assertions.assertEquals(ByteBufSet.intersectionSize(bs0.retainedSlice(), bs1.retainedSlice(), 3), -1)
        Assertions.assertEquals(ByteBufSet.intersectionSize(bs0.retainedSlice(), bs1.retainedSlice(), 4), 0)
        bs0.release()
        bs1.release()
    }
}