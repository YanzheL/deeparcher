package com.hitnslab.dnssecurity.deeparcher.stream

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WhitelistPredicateUnitTest {

    @Test
    fun test() {
        val predicate = WhitelistPredicate()
                .fromFile("whitelist.csv")
                .fromRegex("\\.(edu|gov)(\\.[a-z]{2})?$")
        Assertions.assertTrue(predicate.test("www.hitwh.edu.cn"))
        Assertions.assertTrue(predicate.test("abc.hitwh.edu.cn"))
        Assertions.assertTrue(predicate.test("111.abc.efg.hitwh.edu.cn"))
        Assertions.assertTrue(predicate.test("www.gov.cn"))
        Assertions.assertTrue(predicate.test("www.ucr.edu"))
        Assertions.assertTrue(predicate.test("abc.efg.ucr.edu"))
        Assertions.assertTrue(predicate.test("www.ucr.gov"))
        Assertions.assertTrue(predicate.test("www.gov.cn"))

        Assertions.assertFalse(predicate.test("www.hitwh.edu.com"))
        Assertions.assertFalse(predicate.test("abc.hitwh.edu.qwedw"))
        Assertions.assertFalse(predicate.test(""))
        Assertions.assertFalse(predicate.test("qwdde"))
        Assertions.assertFalse(predicate.test("qwed.wqdw"))
        Assertions.assertFalse(predicate.test("abc.efg.ucr.edue"))
        Assertions.assertFalse(predicate.test("www.hitwh.eduu.cn"))
    }
}