package com.hitnslab.dnssecurity.pdnsdataloader.model

import java.util.*

data class MalDomainInfo(
        val domain: String
) {
    val last_check: Date? = null
    val type: String? = null
}