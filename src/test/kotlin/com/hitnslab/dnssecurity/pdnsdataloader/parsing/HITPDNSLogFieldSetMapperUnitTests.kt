package com.hitnslab.dnssecurity.pdnsdataloader.parsing

import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.batch.item.file.transform.DefaultFieldSet
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HITPDNSLogFieldSetMapperUnitTests {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.nnn")

    @Test
    fun normal() {
        val correct = PDnsData(
                queryTime = ZonedDateTime.of(LocalDateTime.parse("01-Nov-2019 23:56:39.256", dateTimeFormatter), ZoneId.systemDefault()).toInstant(),
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
                        "223.99.231.189"
                )
        )
        correct.clientIp = "10.241.144.75"
        correct.cnames.add("photo.qpic.cn")
        val line = "2019-11-02T00:00:00.109296+08:00 10.235.1.2  01-Nov-2019 23:56:39.256 client 10.241.144.75 5019: view WH_Student: m.qpic.cn IN A NOERROR + NS NE NT ND NC H 14 Response: m.qpic.cn 214 IN CNAME photo.qpic.cn.;photo.qpic.cn 60 IN A 223.99.231.176;photo.qpic.cn 60 IN A 223.99.231.180;photo.qpic.cn 60 IN A 223.99.231.169;photo.qpic.cn 60 IN A 223.99.231.168;photo.qpic.cn 60 IN A 223.99.231.140;photo.qpic.cn 60 IN A 223.99.231.165;photo.qpic.cn 60 IN A 223.99.231.141;photo.qpic.cn 60 IN A 223.99.231.170;photo.qpic.cn 60 IN A 223.99.231.143;photo.qpic.cn 60 IN A 223.99.231.148;photo.qpic.cn 60 IN A 223.99.231.139;photo.qpic.cn 60 IN A 223.99.231.167;photo.qpic.cn 60 IN A 223.99.231.152;photo.qpic.cn 60 IN A 223.99.231.154;photo.qpic.cn 60 IN A 223.99.231.183;photo.qpic.cn 60 IN A 223.99.231.189;"
        val fieldSet = DefaultFieldSet(line.split("\\s+".toRegex()).toTypedArray())
        val fieldSetMapper = HITPDNSLogFieldSetMapper()
        val result = fieldSetMapper.mapFieldSet(fieldSet)
        assertEquals(result, correct)
        println(result)
        println(result.ips)
        println(result.cnames)
    }
}