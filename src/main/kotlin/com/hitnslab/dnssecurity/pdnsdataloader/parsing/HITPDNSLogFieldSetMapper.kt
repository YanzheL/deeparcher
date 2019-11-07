package com.hitnslab.dnssecurity.pdnsdataloader.parsing

import com.hitnslab.dnssecurity.pdnsdataloader.model.PDNSInfo
import mu.KotlinLogging
import org.springframework.batch.item.file.transform.FieldSet
import java.text.SimpleDateFormat

/**
 *
 * Example:
 * 0        2019-11-02T00:00:00.010625+08:00
 * 1        10.235.1.2
 * 2        01-Nov-2019
 * 3        23:56:39.157
 * 4        client
 * 5        10.240.60.123
 * 6        49470:
 * 7        view
 * 8        WH_Student:
 * 9        tile-service.weather.microsoft.com
 * 10       IN
 * 11       A
 * 12       NOERROR
 * 13       +
 * 14       NS
 * 15       NE
 * 16       NT
 * 17       ND
 * 18       NC
 * 19       H
 * 20       26
 * 21       Response:
 * 22       tile-service.weather.microsoft.com 499 IN CNAME wildcard.weather.microsoft.com.edgekey.net.;wildcard.weather.microsoft.com.edgekey.net 780 IN CNAME e15275.g.akamaiedge.net.;e15275.g.akamaiedge.net 60 IN A 184.85.125.248;
 */
open class HITPDNSLogFieldSetMapper : PDNSLogFieldSetMapper {

    private val dataStringBuilder = StringBuilder(3)

    companion object {
        private val timeFmt = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss.SSS")
    }

    private val logger = KotlinLogging.logger {}

    override fun mapFieldSet(fieldSet: FieldSet): PDNSInfo {
        dataStringBuilder
                .clear()
                .append(fieldSet.readString(2))
                .append(" ")
                .append(fieldSet.readString(3))
        val pdnsInfo = PDNSInfo(
                queryTime = timeFmt.parse(dataStringBuilder.toString()),
                domain = fieldSet.readString(9),
                queryType = fieldSet.readString(11),
                replyCode = fieldSet.readString(12)
        )
        pdnsInfo.clientIpString = fieldSet.readString(5)
        var hasResponseBody = false
        var bodyBaseIdx = -1
        val size = fieldSet.values.size
        for (i in 13 until size) {
            if (fieldSet.readString(i) == "Response:") {
                hasResponseBody = true
                bodyBaseIdx = i + 1
                break
            }
        }
        if (hasResponseBody) {
            val replies: List<String> = fieldSet.values
                    .slice(bodyBaseIdx until size)
                    .joinToString(separator = " ")
                    .split(";")
            for (reply in replies) {
                if (reply.isEmpty())
                    continue
                val parsedReply = reply.split("\\s+".toRegex())
                if (parsedReply.size != 5) {
                    logger.warn { "Malformed DNS reply entry: <$reply>" }
                    continue
                }
                val replyType = parsedReply[3]
                val replyData: String = parsedReply[4].trimEnd('.')
                when (replyType) {
                    "CNAME" -> pdnsInfo.cnames.add(replyData)
                    "A" -> pdnsInfo.addIpStrings(replyData)
                }
            }
        }
        return pdnsInfo
    }
}