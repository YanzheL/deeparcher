package com.hitnslab.dnssecurity.deeparcher.util

import io.netty.buffer.ByteBuf

/**
 * Returns the intersection size of two collections. This function assumes elements in these collections are unique.
 */
fun <E> Collection<E>.intersectionSize(other: Collection<E>): Int {
    var count = 0
    this.forEach { if (other.contains(it)) count++ }
    return count
}

/**
 * Returns the intersection size of two byte arrays. Comparison is performed on each window.
 * This function assumes each window of these byte arrays are unique.
 *
 * @param [wsize] size of comparison window, default is `1`.
 * @return the intersection size if no error occurred, `-1` if the sizes of two arrays are not multiple of `wsize`.
 */
fun ByteArray.intersectionSize(other: ByteArray, wsize: Int = 1): Int {
    val size1 = this.size
    val size2 = other.size
    if (size1 % wsize != 0 || size2 % wsize != 0) {
        return -1
    }
    var count = 0
    for (wstart1 in 0 until size1 step wsize) {
        for (wstart2 in 0 until size2 step wsize) {
            var same = true
            for (i in 0 until wsize) {
                if (this[wstart1 + i] != other[wstart2 + i]) {
                    same = false
                }
            }
            if (same) {
                count++
            }
        }
    }
    return count
}

/**
 * Returns the intersection size of two buffers. Comparison is performed on each window.
 * This function assumes each window of these buffers are unique.
 * The specified buffer will be released after this operation.
 *
 * @param [wsize] size of comparison window, default is `1`.
 * @return the intersection size if no error occurred, `-1` if the sizes of two buffers are not multiple of `wsize`.
 */
fun ByteBuf.intersectionSize(other: ByteBuf, wsize: Int = 1): Int {
    if (this.readableBytes() % wsize != 0 || other.readableBytes() % wsize != 0) {
        other.release()
        return -1
    }
    var count = 0
    val mySlice = this.slice()
    while (mySlice.readableBytes() != 0) {
        val window1 = mySlice.readSlice(wsize)
        val lookup = other.slice()
        while (lookup.readableBytes() != 0) {
            val window2 = lookup.readSlice(wsize)
            if (window1.compareTo(window2) == 0) {
                count += 1
            }
        }
    }
    other.release()
    return count
}