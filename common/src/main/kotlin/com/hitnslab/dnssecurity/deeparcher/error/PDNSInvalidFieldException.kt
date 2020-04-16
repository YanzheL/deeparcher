package com.hitnslab.dnssecurity.deeparcher.error


class PDNSInvalidFieldException(message: String?, cause: Throwable?) : IllegalArgumentException(message, cause) {
    constructor () : this(null, null)
    constructor (s: String) : this(s, null)
    constructor (cause: Throwable) : this(null, cause)
}