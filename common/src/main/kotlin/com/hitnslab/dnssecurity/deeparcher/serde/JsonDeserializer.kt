package com.hitnslab.dnssecurity.deeparcher.serde

import com.alibaba.fastjson.JSON
import org.apache.kafka.common.serialization.Deserializer
import kotlin.reflect.KClass

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class JsonDeserializer<T : Any>(val clz: KClass<T>) : Deserializer<T> {

    override fun deserialize(topic: String?, data: ByteArray?): T {
        return JSON.parseObject(data, clz.java)
    }
}