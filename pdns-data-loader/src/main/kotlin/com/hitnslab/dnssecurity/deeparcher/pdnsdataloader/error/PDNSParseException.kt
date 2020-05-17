package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.error


/**
 * Base class for PDNS parse exceptions.
 *
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
open class PDNSParseException(message: String?, cause: Throwable?) : IllegalArgumentException(message, cause) {
    constructor () : this(null, null)
    constructor (s: String) : this(s, null)
    constructor (cause: Throwable) : this(null, cause)
}