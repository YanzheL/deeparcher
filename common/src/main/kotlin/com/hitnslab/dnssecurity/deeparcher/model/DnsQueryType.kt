package com.hitnslab.dnssecurity.deeparcher.model

/**
 * @see [DNS Parameters](https://www.iana.org/assignments/dns-parameters/dns-parameters.xhtml)
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
enum class DnsQueryType(val value: Int) {
    A(1),
    NS(2),
    CNAME(5),
    SOA(6),
    PTR(12),
    MX(15),
    TXT(16),
    KEY(25),
    AAAA(28),
    LOC(29),
    SRV(33),
    NAPTR(35),
    A6(38),
    DNAME(39),
    DS(43),
    DNSKEY(48),
    SPF(99),
    AMTRELAY(260);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}