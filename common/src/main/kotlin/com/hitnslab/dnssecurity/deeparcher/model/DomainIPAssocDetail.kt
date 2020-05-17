package com.hitnslab.dnssecurity.deeparcher.model

import java.net.InetAddress

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
@Deprecated(
    "This data class is deprecated, " +
            "please use the generated protobuf class <DomainAssocDetailProto.DomainAssocDetail> directly."
)
class DomainAssocDetail(
    val domain: String,
    val topPrivateDomain: String
) {
    val ipv4Addresses by lazy { mutableSetOf<InetAddress>() }
    val ipv6Addresses by lazy { mutableSetOf<InetAddress>() }
    val cnames by lazy { mutableSetOf<String>() }
//    class Builder {
//        private var domain: String? = null
//        private var topPrivateDomain: String? = null
//        private var ipv4Addresses:MutableSet<Inet4Address>? = null
//        private var ipv6Addresses:MutableSet<Inet6Address>? = null
////        private val ipv4AddressesDelegate = lazy { mutableSetOf<Inet4Address>() }
////        private val ipv6AddressesDelegate = lazy { mutableSetOf<Inet6Address>() }
////        private val ipv4Addresses by ipv4AddressesDelegate
////        private val ipv6Addresses by ipv6AddressesDelegate
//
//        var error: Throwable? = null
//            private set
//
//        fun from(value: PDnsData) = apply {
//            domain = value.domain
//            topPrivateDomain = value.topPrivateDomain
//            value.ips?.forEach {
//                when (it) {
//                    is Inet4Address -> {
//                        if (ipv4Addresses == null){
//                            ipv4Addresses = mutableSetOf()
//                        }
//                        ipv4Addresses!!.add(it)
//                    }
//                    is Inet6Address ->{
//                        if (ipv6Addresses == null){
//                            ipv6Addresses = mutableSetOf()
//                        }
//                        ipv6Addresses!!.add(it)
//                    }
//                }
//            }
//        }
//
//        fun from(value: DomainAssocDetail) = apply {
//            domain = value.domain
//            topPrivateDomain = value.topPrivateDomain
//            value.ipv4Addresses?.toMutableSet()
//            ipv4Addresses = value.ipv4Addresses?.toMutableSet()
//            ipv6Addresses = value.ipv6Addresses?.toMutableSet()
//        }
//
//        fun domain(value: String) = apply { domain = value }
//        fun topPrivateDomain(value: String) = apply { topPrivateDomain = value }
//
//        fun addIpv4(value: ByteArray) = apply {
//            Assert.isTrue(value.size % 4 == 0, "Invalid IPv4 bytes <$value>")
//            ipv4Bytes.add(value)
//        }
//
//        fun addIpv6(value: ByteArray) = apply {
//            Assert.isTrue(value.size % 16 == 0, "Invalid IPv6 bytes <$value>")
//            ipv6Bytes.add(value)
//        }
//
//        fun build(): DomainAssocDetail? {
//            if (domain == null) {
//                error = PDNSInvalidFieldException("domain cannot be null")
//                return null
//            }
//            if (topPrivateDomain == null) {
//                error = PDNSInvalidFieldException("topPrivateDomain cannot be null")
//                return null
//            }
//        }
//
//    }
}