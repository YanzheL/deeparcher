package com.hitnslab.dnssecurity.pdnsdataloader.error


class DomainValidationException(message: String?, cause: Throwable?) : IllegalArgumentException(message, cause) {
    constructor () : this(null, null)
    constructor (s: String) : this(s, null)
    constructor (cause: Throwable) : this(null, cause)
}