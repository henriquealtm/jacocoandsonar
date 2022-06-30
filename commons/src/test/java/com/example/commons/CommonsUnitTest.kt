package com.example.commons

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CommonsUnitTest {

    @Test
    fun test() {
        val commons = Commons()
        assertEquals(4, commons.timesOne(4))
        assertEquals(2, commons.timesOne(2))
    }

}