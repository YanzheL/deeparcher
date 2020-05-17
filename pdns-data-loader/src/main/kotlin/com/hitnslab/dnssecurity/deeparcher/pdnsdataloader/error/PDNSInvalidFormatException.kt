package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.error


/**
 * Exception class for invalid PDNS data file format.
 *
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class PDNSInvalidFormatException(message: String?, cause: Throwable?) : PDNSParseException(message, cause) {
    constructor () : this(null, null)
    constructor (s: String) : this(s, null)
    constructor (cause: Throwable) : this(null, cause)
}