package com.hitnslab.dnssecurity.deeparcher.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.core.io.ClassPathResource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DomainWhitelistPredicateUnitTests {

    @Test
    fun test0() {
        val predicates = listOf(
            DomainWhitelistPredicate().fromResource(ClassPathResource("whitelist.txt")),
            DomainWhitelistPredicate().fromResource(ClassPathResource("whitelist.txt.gz")),
            DomainWhitelistPredicate().fromResource(ClassPathResource("whitelist.txt.xz")),
            DomainWhitelistPredicate().fromResource(ClassPathResource("whitelist.txt.zip"))
        )
        var size = -1
        predicates.forEach {
            if (size == -1) {
                size = it.whitelist.size
                Assertions.assertTrue(size > 0)
            } else {
                Assertions.assertTrue(it.whitelist.size == size)
            }
        }
    }

    @Test
    fun test1() {
        val predicate = DomainWhitelistPredicate()
            .fromResource(ClassPathResource("whitelist.txt.xz"))
            .fromRegex("\\.(edu|gov)(\\.[a-z]{2})?$")
        Assertions.assertTrue(predicate.test("www.hitwh.edu.cn"))
        Assertions.assertTrue(predicate.test("abc.hitwh.edu.cn"))
        Assertions.assertTrue(predicate.test("111.abc.efg.hitwh.edu.cn"))
        Assertions.assertTrue(predicate.test("www.gov.cn"))
        Assertions.assertTrue(predicate.test("www.ucr.edu"))
        Assertions.assertTrue(predicate.test("abc.efg.ucr.edu"))
        Assertions.assertTrue(predicate.test("www.ucr.gov"))
        Assertions.assertTrue(predicate.test("www.gov.cn"))
        Assertions.assertTrue(predicate.test("googleapis.com"))
        Assertions.assertTrue(predicate.test("auditrecording-pa.googleapis.com"))

        Assertions.assertFalse(predicate.test("d1uo4w7k31k5mn.cloudfront.net"))
        Assertions.assertFalse(predicate.test("www.hitwh.edu.com"))
        Assertions.assertFalse(predicate.test("abc.hitwh.edu.qwedw"))
        Assertions.assertFalse(predicate.test(""))
        Assertions.assertFalse(predicate.test("qwdde"))
        Assertions.assertFalse(predicate.test("qwed.wqdw"))
        Assertions.assertFalse(predicate.test("abc.efg.ucr.edue"))
        Assertions.assertFalse(predicate.test("www.hitwh.eduu.cn"))
    }
}