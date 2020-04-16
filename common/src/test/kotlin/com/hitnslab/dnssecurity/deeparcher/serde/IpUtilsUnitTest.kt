package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.util.packIpFromBytes
import io.netty.buffer.PooledByteBufAllocator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.InetAddress

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IpUtilsUnitTest {
    private val allocator = PooledByteBufAllocator.DEFAULT

    @Test
    fun testIpv4Seq() {
        val result = mutableListOf<Int>()
        val bytes = allocator.directBuffer()
        bytes.writeBytes(InetAddress.getByName("192.168.178.111").address)
        bytes.writeBytes(InetAddress.getByName("192.168.236.23").address)
        packIpFromBytes(bytes, 4, result)
        val correct = listOf(3232281199u, 3232295959u)
        Assertions.assertEquals(correct, result.map { it.toUInt() })
    }

    @Test
    fun testIpv6Seq() {
        val result = mutableListOf<Long>()
        packIpFromBytes(InetAddress.getByName("2001:0db8:0000:0000:1234:0ace:6006:001e").address, 16, result)
        val correct = listOf(2306139568115548160L, 1311685272962203678L)
        Assertions.assertEquals(correct, result)
    }
}