package com.hitnslab.dnssecurity.deeparcher.util

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.PooledByteBufAllocator
import io.netty.buffer.Unpooled
import io.netty.util.ReferenceCounted

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class ByteBufSet : MutableSet<ByteBuf>, ReferenceCounted {
    // From Set
    override val size: Int
        get() = buffer.readableBytes()

    var dirty = false

    val buffer: ByteBuf

    val allocator: ByteBufAllocator

    constructor() {
        allocator = PooledByteBufAllocator.DEFAULT
        buffer = allocator.heapBuffer()
    }

    constructor(initBuffer: ByteArray) {
        allocator = PooledByteBufAllocator.DEFAULT
        val compositeByteBuf = allocator.compositeBuffer()
        compositeByteBuf.addComponent(true, Unpooled.wrappedBuffer(initBuffer).asReadOnly())
        compositeByteBuf.addComponent(true, allocator.heapBuffer())
        buffer = compositeByteBuf
    }

    constructor(initBuffer: ByteBuf) {
        allocator = PooledByteBufAllocator.DEFAULT
        val compositeByteBuf = allocator.compositeBuffer()
        compositeByteBuf.addComponent(true, initBuffer.asReadOnly())
        compositeByteBuf.addComponent(true, allocator.heapBuffer())
        buffer = compositeByteBuf
    }


    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun contains(element: ByteBuf): Boolean {
        val target = element.slice()
        val chunkWidth = target.readableBytes()
        if (chunkWidth == 0 || size % chunkWidth != 0) {
            return false
        }
        val view = buffer.slice()
        val chunks: Int = size / chunkWidth
        for (i in 0 until chunks) {
            val slice = view.readSlice(chunkWidth)
            if (slice.compareTo(target) == 0) {
                return true
            }
        }
        return false
    }

    override fun containsAll(elements: Collection<ByteBuf>): Boolean {
        return elements.all { contains(it) }
    }

    // From MutableSet

    override fun iterator(): MutableIterator<ByteBuf> {
        TODO("Not yet implemented")
    }

    override fun add(element: ByteBuf): Boolean {
        val slice = element.slice()
        if (!contains(slice)) {
            buffer.writeBytes(slice)
            dirty = true
            return true
        }
        return false
    }

    fun addAll(element: ByteBuf, width: Int): Boolean {
        val slice = element.slice()
        val bytes = slice.readableBytes()
        if (bytes == 0 || bytes % width != 0) {
            return false
        }
        for (i in 0 until bytes / width) {
            add(slice.readSlice(width))
        }
        return true
    }

    override fun remove(element: ByteBuf): Boolean {
        TODO("Not yet implemented")
    }

    override fun addAll(elements: Collection<ByteBuf>): Boolean {
        return elements.map { add(it) }.any()
    }

    override fun removeAll(elements: Collection<ByteBuf>): Boolean {
        TODO("Not yet implemented")
    }

    override fun retainAll(elements: Collection<ByteBuf>): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        buffer.clear()
    }

    override fun touch(): ReferenceCounted = buffer.touch()
    override fun touch(hint: Any?): ReferenceCounted = buffer.touch(hint)
    override fun refCnt(): Int = buffer.refCnt()
    override fun release(): Boolean = buffer.release()
    override fun release(decrement: Int): Boolean = buffer.release(decrement)
    override fun retain(): ReferenceCounted = buffer.retain()
    override fun retain(increment: Int): ReferenceCounted = buffer.retain(increment)
}