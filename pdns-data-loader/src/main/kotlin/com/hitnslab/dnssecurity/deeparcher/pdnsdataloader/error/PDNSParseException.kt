package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.error


open class PDNSParseException(message: String?, cause: Throwable?) : IllegalArgumentException(message, cause) {
    constructor () : this(null, null)
    constructor (s: String) : this(s, null)
    constructor (cause: Throwable) : this(null, cause)
}