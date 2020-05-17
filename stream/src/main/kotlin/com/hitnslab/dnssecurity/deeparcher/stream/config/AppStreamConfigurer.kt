package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.github.benmanes.caffeine.cache.LoadingCache
import com.google.common.hash.Funnels
import com.hitnslab.dnssecurity.deeparcher.stream.BloomFilterKStreamPredicate
import org.apache.kafka.streams.kstream.KStream
import org.springframework.beans.factory.annotation.Autowired
import java.io.PrintWriter
import java.nio.charset.Charset

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
open class AppStreamConfigurer {

    @Autowired
    lateinit var fileWriterCache: LoadingCache<String, PrintWriter>

    fun configSinks(
        src: KStream<String, *>,
        type: String, path: String,
        options: Map<String, String>? = null
    ) {
        var sinkSrc = src
        options?.let {
            val unique = it.getOrDefault("unique", "false").toBoolean()
            if (unique) {
                sinkSrc = sinkSrc.filterNot(
                    BloomFilterKStreamPredicate(
                        Funnels.stringFunnel(Charset.defaultCharset()),
                        it.getOrDefault("expectedInsertions", "2000000000").toLong(),
                        it.getOrDefault("fpp", "0.01").toDouble()
                    )
                )
            }
        }
        when (type) {
            "topic" -> sinkSrc.to(path)
            "file" -> sinkSrc.foreach { _, v ->
                fileWriterCache.get(path)!!.println(v)
            }
            else -> throw Exception("Invalid sink type <$type>")
        }
    }
}