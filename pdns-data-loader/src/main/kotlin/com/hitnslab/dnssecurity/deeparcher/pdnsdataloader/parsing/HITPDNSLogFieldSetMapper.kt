package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.parsing

import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.error.PDNSInvalidFieldException
import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.error.PDNSInvalidFormatException
import mu.KotlinLogging
import org.springframework.batch.item.file.transform.FieldSet
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
class HITPDNSLogFieldSetMapper : PDNSLogFieldSetMapper {

    private val dataStringBuilder = StringBuffer(3)

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.nnn")

    private val logger = KotlinLogging.logger {}

    override fun mapFieldSet(fieldSet: FieldSet): PDnsData {
        val queryTime: String
        try {
            synchronized(this) {
                dataStringBuilder.setLength(0)
                dataStringBuilder.append(fieldSet.readString(2))
                        .append(" ")
                        .append(fieldSet.readString(3))
                queryTime = dataStringBuilder.toString()
            }
        } catch (e: Exception) {
            throw PDNSInvalidFieldException("PDNS parse failed: Invalid queryTime in fieldSet <$fieldSet>, exception <$e>", e)
        }
        val ret: PDnsData
        val values = fieldSet.values
        try {
            val localDateTime = LocalDateTime.parse(queryTime, dateTimeFormatter)
            ret = PDnsData(
                    queryTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant(),
                    domain = fieldSet.readString(9),
                    queryType = fieldSet.readString(11),
                    replyCode = fieldSet.readString(12)
            )
        } catch (e: Exception) {
            throw PDNSInvalidFieldException("PDNS parse failed: Cannot create PDnsData object with values <$fieldSet>, exception <$e>", e)
        }
        val size = values.size
        if (size <= 14) {
            throw PDNSInvalidFormatException("PDNS parse failed: Insufficient number of fields in <$fieldSet>")
        }
        ret.clientIp = fieldSet.readString(5)
        var hasResponseBody = false
        var bodyBaseIdx = -1
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
                    logger.warn { "Malformed DNS reply entry conjunction data: <$conjunction>" }
                    break
                }
                val replyData: String = conjunction.split(";")[0].trimEnd('.')
                when (replyType) {
                    "CNAME" -> ret.cnames.add(replyData)
                    "A", "AAAA" -> ret.ips.add(replyData)
                }
                entryStart = entryEnd
            } while (entryStart < size - 1)
        }
        return ret
    }
}