package com.hitnslab.dnssecurity.deeparcher.util

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import java.net.InetAddress

/**
 * Extract multiple `InetAddress` from bytes
 *
 * @param [bytesIn] all bytes that represents multiple IP addresses, big endian.
 * @param [width] `4` for IPv4, `16` for IPv6
 * @param [ipOut] output collection
 */
fun parseIpFromBytes(bytesIn: ByteBuf, width: Int, ipOut: MutableCollection<InetAddress>) {
    val size = bytesIn.readableBytes()
    if (size == 0) {
        return
    }
    if (size % width != 0) {
        throw IllegalArgumentException("IP Address Bytes <$bytesIn> has incorrect length <${size}>.")
    }
    while (bytesIn.readableBytes() != 0) {
        val address = bytesIn.readSlice(width)
        ipOut.add(InetAddress.getByAddress(ByteBufUtil.getBytes(address)))
    }
    bytesIn.release()
}

/**
 * Pack bytes to integer types
 *
 * @param [bytesIn] input bytes
 * @param [width] pack width, should be 1, 2, 4 or multiple of 8
 * @param [ipOut] output collection
 */
fun <R : Number> packIntegerFromBytes(bytesIn: ByteBuf, width: Int, ipOut: MutableList<R>) {
    val size = bytesIn.readableBytes()
    if (size == 0) {
        return
    }
    if (size % width != 0) {
        throw IllegalArgumentException("IP Address Bytes <$bytesIn> has incorrect length <${size}>.")
    }
    while (bytesIn.readableBytes() != 0) {
        when {
            width == 1 -> ipOut.add(bytesIn.readByte() as R)
            width == 2 -> ipOut.add(bytesIn.readShort() as R)
            width == 4 -> ipOut.add(bytesIn.readInt() as R)
            width % 8 == 0 -> ipOut.add(bytesIn.readLong() as R)
        }
    }
    bytesIn.release()
}

/**
 * Union two sets of bytes with specified window width
 *
 * @return `null` if set1 contains set2 else the union of two sets
 */
fun bytesSetUnion(set1: ByteBuf, set2: ByteBuf, width: Int): ByteBuf? {
    val bufSet = ByteBufSet(set1)
    bufSet.addAll(set2, width)
    var ret: ByteBuf? = null
    if (bufSet.dirty) {
        ret = bufSet.buffer.retainedSlice()
    }
    bufSet.release()
    return ret
}