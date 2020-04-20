package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.converter.PDnsDataToProtoConverter
import com.hitnslab.dnssecurity.deeparcher.converter.ProtoToPDnsDataConverter
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.format.DateTimeFormatter

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PDnsSerdeUnitTests {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.nnn")

    @Test
    fun testSerde() {
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
        val correct = builder.build()!!
        val serde = GenericSerde(PDnsProtoSerializer::class, PDnsProtoDeserializer::class)
        val i2p = PDnsDataToProtoConverter()
        val p2i = ProtoToPDnsDataConverter()
        val serializer = serde.serializer()
        val deserializer = serde.deserializer()
        val serialized = serializer.serialize("key", i2p.convert(correct))
        val deserialized = p2i.convert(deserializer.deserialize("key", serialized))
        Assertions.assertEquals(deserialized, correct)
    }
}