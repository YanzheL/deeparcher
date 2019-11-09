package com.hitnslab.dnssecurity.pdnsdataloader.parsing

import com.hitnslab.dnssecurity.pdnsdataloader.error.DomainValidationException
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import mu.KotlinLogging
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.stereotype.Component
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
@Component
class HITPDNSLogFieldSetMapper : PDNSLogFieldSetMapper {

    private val dataStringBuilder = StringBuilder(3)

    private val timeFmt = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss.SSS")

    private val logger = KotlinLogging.logger {}

    override fun mapFieldSet(fieldSet: FieldSet): PDnsData {
        dataStringBuilder
                .clear()
                .append(fieldSet.readString(2))
                .append(" ")
                .append(fieldSet.readString(3))
        val ret: PDnsData
        try {
            ret = PDnsData(
                    queryTime = timeFmt.parse(dataStringBuilder.toString()),
                    domain = fieldSet.readString(9),
                    queryType = fieldSet.readString(11),
                    replyCode = fieldSet.readString(12)
            )
        } catch (e: Exception) {
            throw DomainValidationException(e)
        }
        ret.clientIp = fieldSet.readString(5)
        var hasResponseBody = false
        var bodyBaseIdx = -1
        val values = fieldSet.values
        val size = values.size
        if (size <= 14) {
            throw DomainValidationException("Invalid pDNS record <$values>")
        }
        for (i in 13 until size) {
            if (values[i].trim() == "Response:") {
                hasResponseBody = true
                bodyBaseIdx = i + 1
                break
            }
        }
        if (hasResponseBody) {
            var entryStart = bodyBaseIdx
            do {
                val entryEnd = entryStart + 4
                if (entryEnd >= size) {
//                    logger.warn { "Malformed DNS reply entry: <${values.slice(entryStart until size)}>" }
                    break
                }
                val replyType = values[entryStart + 3].trim()
                val conjunction = values[entryStart + 4]
                if (';' !in conjunction) {
                    logger.error { "Malformed DNS reply entry conjunction data: <$conjunction>" }
                    break
                }
                val replyData: String = conjunction.split(";")[0].trimEnd('.')
                when (replyType) {
                    "CNAME" -> ret.cnames.add(replyData)
                    "A" -> ret.ips.add(replyData)
                }
                entryStart = entryEnd
            } while (entryStart < size - 1)
        }
        return ret
    }
}