package com.hitnslab.dnssecurity.deeparcher.util

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import java.net.InetAddress
import java.nio.ByteBuffer


fun parseIpFromBytes(bytesIn: ByteBuf, width: Int, ipOut: MutableCollection<InetAddress>) {
    val view = bytesIn.slice()
    val size = view.readableBytes()
    if (size == 0) {
        return
    }
    if (size % width != 0) {
        throw IllegalArgumentException("IP Address Bytes <$bytesIn> has incorrect length <${size}>.")
    }
    while (view.readableBytes() != 0) {
        val address = view.readSlice(width)
        ipOut.add(InetAddress.getByAddress(ByteBufUtil.getBytes(address)))
    }
}

fun parseIpFromBytes(bytesIn: ByteArray, width: Int, ipOut: MutableCollection<InetAddress>) {
    return parseIpFromBytes(Unpooled.wrappedBuffer(bytesIn), width, ipOut)
}

fun parseIpFromBytes(bytesIn: ByteBuffer, width: Int, ipOut: MutableCollection<InetAddress>) {
    return parseIpFromBytes(Unpooled.wrappedBuffer(bytesIn), width, ipOut)
}

fun <R : Number> packIpFromBytes(bytesIn: ByteArray, width: Int, ipOut: MutableList<R>) {
    return packIpFromBytes(Unpooled.wrappedBuffer(bytesIn), width, ipOut)
}

fun <R : Number> packIpFromBytes(bytesIn: ByteBuffer, width: Int, ipOut: MutableList<R>) {
    return packIpFromBytes(Unpooled.wrappedBuffer(bytesIn), width, ipOut)
}

fun <R : Number> packIpFromBytes(bytesIn: ByteBuf, width: Int, ipOut: MutableList<R>) {
    val view = bytesIn.slice()
    val size = view.readableBytes()
    if (size == 0) {
        return
    }
    if (size % width != 0) {
        throw IllegalArgumentException("IP Address Bytes <$bytesIn> has incorrect length <${size}>.")
    }
    while (view.readableBytes() != 0) {
        if (width == 4) {
            ipOut.add(view.readInt() as R)
        } else {
            ipOut.add(view.readLong() as R)
        }
    }
}