package com.hitnslab.dnssecurity.pdnsdataloader.error


class PDNSInvalidFieldException(message: String?, cause: Throwable?) : PDNSParseException(message, cause) {
    constructor () : this(null, null)
    constructor (s: String) : this(s, null)
    constructor (cause: Throwable) : this(null, cause)
}