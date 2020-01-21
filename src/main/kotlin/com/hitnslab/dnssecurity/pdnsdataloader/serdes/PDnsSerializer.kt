package com.hitnslab.dnssecurity.pdnsdataloader.serdes

import com.hitnslab.dnssecurity.pdnsdataloader.api.proto.PDnsDataProto
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsDataDAO
import org.apache.kafka.common.serialization.Serializer

class PDnsSerializer : Serializer<PDnsDataDAO> {

    override fun serialize(topic: String?, data: PDnsDataDAO?): ByteArray {
        return PDnsDataProto.PDnsData
                .newBuilder()
                .setQueryTime(data!!.queryTime.epochSecond)
                .setDomain(data.domain)
                .setQueryType(data.queryType.value)
                .setReplyCode(data.replyCode.value)
                .setTopPrivateDomain(data.topPrivateDomain)
                .build()
                .toByteArray()
    }
}