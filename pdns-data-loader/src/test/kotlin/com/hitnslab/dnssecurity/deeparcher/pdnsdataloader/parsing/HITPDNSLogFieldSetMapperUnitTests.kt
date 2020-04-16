package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.parsing

import com.hitnslab.dnssecurity.deeparcher.error.PDNSInvalidFieldException
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.springframework.core.io.DefaultResourceLoader
import java.time.format.DateTimeFormatter

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HITPDNSLogFieldSetMapperUnitTests {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.nnn")

    @Test
    fun testParsingResult() {
        val builder = PDnsData.Builder()
                .queryTime("01-Nov-2019 23:56:39.256", dateTimeFormatter)
                .domain("m.qpic.cn")
                .queryType("A")
                .replyCode("NOERROR")
                .addIp("223.99.231.176")
                .addIp("223.99.231.180")
                .addIp("223.99.231.169")
                .addIp("223.99.231.168")
                .addIp("223.99.231.140")
                .addIp("223.99.231.165")
                .addIp("223.99.231.141")
                .addIp("223.99.231.170")
                .addIp("223.99.231.143")
                .addIp("223.99.231.148")
                .addIp("223.99.231.139")
                .addIp("223.99.231.167")
                .addIp("223.99.231.152")
                .addIp("223.99.231.154")
                .addIp("223.99.231.183")
                .addIp("223.99.231.189")
                .clientIp("10.241.144.75")
                .addCName("photo.qpic.cn")
        val correct = builder.build()
        val line = "2019-11-02T00:00:00.109296+08:00 10.235.1.2  01-Nov-2019 23:56:39.256 client 10.241.144.75 5019: view WH_Student: m.qpic.cn IN A NOERROR + NS NE NT ND NC H 14 Response: m.qpic.cn 214 IN CNAME photo.qpic.cn.;photo.qpic.cn 60 IN A 223.99.231.176;photo.qpic.cn 60 IN A 223.99.231.180;photo.qpic.cn 60 IN A 223.99.231.169;photo.qpic.cn 60 IN A 223.99.231.168;photo.qpic.cn 60 IN A 223.99.231.140;photo.qpic.cn 60 IN A 223.99.231.165;photo.qpic.cn 60 IN A 223.99.231.141;photo.qpic.cn 60 IN A 223.99.231.170;photo.qpic.cn 60 IN A 223.99.231.143;photo.qpic.cn 60 IN A 223.99.231.148;photo.qpic.cn 60 IN A 223.99.231.139;photo.qpic.cn 60 IN A 223.99.231.167;photo.qpic.cn 60 IN A 223.99.231.152;photo.qpic.cn 60 IN A 223.99.231.154;photo.qpic.cn 60 IN A 223.99.231.183;photo.qpic.cn 60 IN A 223.99.231.189;"
        val result = parseOneLine(line)
        assertEquals(result, correct)
    }

    @Test
    fun testParseFile() {
        parseOneFile("classpath:example-pdns.log")
    }

    fun parseOneLine(line: String): PDnsData? {
        val fieldSet = DefaultFieldSet(line.split("\\s+".toRegex()).toTypedArray())
        val fieldSetMapper = HITPDNSLogFieldSetMapper()
        val result = fieldSetMapper.mapFieldSet(fieldSet).build()
        println(result)
        print(result?.ips)
        println(result?.cnames)
        return result
    }

    fun parseOneFile(path: String): List<PDnsData?> {
        val file = DefaultResourceLoader().getResource(path).file
        return file.readLines().map {
            try {
                parseOneLine(it)
            } catch (e: PDNSInvalidFieldException) {
                null
            }
        }

    }
}