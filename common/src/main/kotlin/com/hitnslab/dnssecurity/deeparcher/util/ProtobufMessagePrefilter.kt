package com.hitnslab.dnssecurity.deeparcher.util

import com.google.protobuf.Descriptors
import com.google.protobuf.Message
import java.security.InvalidParameterException
import java.util.function.Predicate

class ProtobufMessagePrefilter<T : Message>(
    val field: String,
    val regex: Regex,
    val allow: Boolean
) : Predicate<T> {

    lateinit var fieldDescriptor: Descriptors.FieldDescriptor

    override fun test(value: T): Boolean {
        if (!::fieldDescriptor.isInitialized) {
            fieldDescriptor = value.descriptorForType.findFieldByName(field)
                ?: throw InvalidParameterException("Invalid PDnsDataProto field <$field>")
        }
        val res = regex.containsMatchIn(value.getField(fieldDescriptor) as String)
        return if (allow) res else !res
    }
}