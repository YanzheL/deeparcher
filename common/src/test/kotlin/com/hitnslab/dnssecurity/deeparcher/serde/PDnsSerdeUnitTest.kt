package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PDnsSerdeUnitTest {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.nnn")

    @Test
    fun testSerde() {
        val correct = PDnsData(
                queryTime = ZonedDateTime.of(LocalDateTime.parse("01-Nov-2019 23:56:39.256", dateTimeFormatter), ZoneId.systemDefault()).toInstant().truncatedTo(ChronoUnit.MICROS),
                domain = "m.qpic.cn",
                queryType = "A",
                replyCode = "NOERROR"
        )
        correct.ips.addAll(
                arrayOf(
                        "223.99.231.176",
                        "223.99.231.180",
                        "223.99.231.169",
                        "223.99.231.168",
                        "223.99.231.140",
                        "223.99.231.165",
                        "223.99.231.141",
                        "223.99.231.170",
                        "223.99.231.143",
                        "223.99.231.148",
                        "223.99.231.139",
                        "223.99.231.167",
                        "223.99.231.152",
                        "223.99.231.154",
                        "223.99.231.183",
                        "223.99.231.189",
                        "2403:18c0:2:6a5::1"
                )
        )
        correct.clientIp = "10.241.144.75"
        correct.cnames.add("photo.qpic.cn")
        val serde = PDnsSerde()
        val serializer = serde.serializer()
        val deserializer = serde.deserializer()
        val serialized = serializer.serialize("key", correct)
        val deserialized = deserializer.deserialize("key", serialized)
        Assertions.assertEquals(deserialized, correct)
    }
}