package com.hitnslab.dnssecurity.deeparcher.util

import io.netty.buffer.ByteBuf

/**
 * Returns the intersection size of two collections. This function assumes elements in these collections are unique.
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
fun <E> Collection<E>.intersectionSize(other: Collection<E>): Int {
    var count = 0
    this.forEach { if (other.contains(it)) count++ }
    return count
}

/**
 * Returns the intersection size of two byte arrays. Comparison is performed on each window.
 * This function assumes each window of these byte arrays are unique.
 * No temporary internal arrays/collections will be created, which saves memory.
 *
 * @param [wsize] size of comparison window, default is `1`.
 * @return the intersection size if no error occurred, `-1` if the sizes of two arrays are not multiple of `wsize`.
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
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
 * Returns the difference set of two byte arrays. Comparison is performed on each window.
 * This function assumes each window of these byte arrays are unique.
 *
 * @param [wsize] size of comparison window, default is `1`.
 * @return the difference set (this - other)
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
fun ByteArray.diff(other: ByteArray, wsize: Int = 1): ByteArray {
    val size1 = this.size
    val size2 = other.size
    if (size1 % wsize != 0 || size2 % wsize != 0) {
        return byteArrayOf()
    }
    val result = mutableListOf<Byte>()
    val currentWindow = ByteArray(wsize)
    for (wstart1 in 0 until size1 step wsize) {
        var seen = false
        for (i in 0 until wsize) {
            currentWindow[i] = this[wstart1 + i]
        }
        for (wstart2 in 0 until size2 step wsize) {
            var same = true
            for (i in 0 until wsize) {
                currentWindow[i] = this[wstart1 + i]
                if (currentWindow[i] != other[wstart2 + i]) {
                    same = false
                }
            }
            if (same) {
                seen = true
            }
        }
        if (!seen) {
            currentWindow.forEach { result.add(it) }
        }
    }
    return result.toByteArray()
}

/**
 * Returns the size of difference set of two byte arrays. Comparison is performed on each window.
 * This function assumes each window of these byte arrays are unique.
 * No temporary internal arrays/collections will be created, which saves memory.
 *
 * @param [wsize] size of comparison window, default is `1`.
 * @return the size of difference set (this - other) if no error occurred, `-1` if the sizes of two arrays are not multiple of `wsize`.
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
fun ByteArray.diffSize(other: ByteArray, wsize: Int = 1): Int {
    val size1 = this.size
    val size2 = other.size
    if (size1 % wsize != 0 || size2 % wsize != 0) {
        return -1
    }
    var count = 0
    for (wstart1 in 0 until size1 step wsize) {
        var seen = false
        for (wstart2 in 0 until size2 step wsize) {
            var same = true
            for (i in 0 until wsize) {
                if (this[wstart1 + i] != other[wstart2 + i]) {
                    same = false
                }
            }
            if (same) {
                seen = true
            }
        }
        if (!seen) {
            count++
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
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
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
