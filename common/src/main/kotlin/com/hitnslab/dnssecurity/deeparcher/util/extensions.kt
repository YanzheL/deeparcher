package com.hitnslab.dnssecurity.deeparcher.util

/**
 * Returns the intersection size of two collections. This function assumes elements in these collections are unique.
 */
fun <E> Collection<E>.intersectionSize(other: Collection<E>): Int {
    var count = 0
    this.forEach { if (other.contains(it)) count++ }
    return count
}