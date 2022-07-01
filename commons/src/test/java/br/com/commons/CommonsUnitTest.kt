package br.com.commons

import org.junit.Assert.assertEquals
import org.junit.Test

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

    @Test
    fun test2() {
        val commons = Commons()
        assertEquals(3, commons.subtractOne(4))
        assertEquals(1, commons.subtractOne(2))
    }
}