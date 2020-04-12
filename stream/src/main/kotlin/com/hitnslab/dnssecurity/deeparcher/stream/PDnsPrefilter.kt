package com.hitnslab.dnssecurity.deeparcher.stream

import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.apache.kafka.streams.kstream.Predicate
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

class PDnsPrefilter(
        field: String,
        pattern: String,
        allow: Boolean
) : Predicate<String, PDnsData> {

    val propertyRef: KProperty1<PDnsData, String>

    val regex: Regex

    val allow: Boolean

    init {
        propertyRef = buildPropertyRef(field)
        regex = Regex(pattern)
        this.allow = allow
    }

    override fun test(key: String, value: PDnsData): Boolean {
        val res = regex.containsMatchIn(propertyRef.get(value))
        return if (allow) res else !res
    }

    private fun buildPropertyRef(fieldName: String): KProperty1<PDnsData, String> {
        for (property in PDnsData::class.memberProperties) {
            if (property.name == fieldName && property.returnType == String::class.createType()) {
                return property as KProperty1<PDnsData, String>
            }
        }
        throw Exception("Invalid PDnsData field <$fieldName>")
    }
}