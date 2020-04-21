package com.hitnslab.dnssecurity.deeparcher.stream

import com.google.protobuf.Descriptors
import com.google.protobuf.Message
import org.apache.kafka.streams.kstream.Predicate
import java.security.InvalidParameterException

class ProtobufMessagePrefilter<T : Message>(
    val field: String,
    val regex: Regex,
    val allow: Boolean
) : Predicate<String, T> {

    lateinit var fieldDescriptor: Descriptors.FieldDescriptor

    override fun test(key: String, value: T): Boolean {
        if (!::fieldDescriptor.isInitialized) {
            fieldDescriptor = value.descriptorForType.findFieldByName(field)
                ?: throw InvalidParameterException("Invalid PDnsDataProto field <$field>")
        }
        val res = regex.containsMatchIn(value.getField(fieldDescriptor) as String)
        return if (allow) res else !res
    }
}