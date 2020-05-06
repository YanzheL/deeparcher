package com.hitnslab.dnssecurity.deeparcher.util

/**
 * Returns the intersection size of two collections. This function assumes elements in these collections are unique.
 */
fun <E> Collection<E>.intersectionSize(other: Collection<E>): Int {
    var count = 0
    this.forEach { if (other.contains(it)) count++ }
    return count
}

/**
 * Returns the intersection size of two byte arrays.
 * This function assumes bytes in each window of these byte arrays are unique.
 *
 * @param [wsize] size of compare window
 * @return the intersection size if no error occurred, `-1` if the size of two arrays are not multiple of `wsize`.
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