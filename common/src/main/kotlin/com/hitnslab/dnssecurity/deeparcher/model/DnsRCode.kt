package com.hitnslab.dnssecurity.deeparcher.model

/**
 * @see [DNS Parameters](https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml)
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
enum class DnsRCode(val value: Int) {
    NOERROR(0),
    FORMERR(1),
    SERVFAIL(2),
    NXDOMAIN(3),
    NOTIMP(4),
    REFUSED(5),
    YXDOMAIN(6),
    YXRRSET(7),
    NXRRSET(8),
    NOTAUTH(9),
    NOTZONE(10),
    DSOTYPENI(11);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}