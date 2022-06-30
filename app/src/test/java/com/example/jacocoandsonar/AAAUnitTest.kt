package com.example.jacocoandsonar

import org.junit.Assert
import org.junit.Test

class AAAUnitTest {

    @Test
    fun addOneUnitTest() {
        val vm = AAAViewModel()
        Assert.assertEquals(1, vm.addOne(0))
        Assert.assertEquals(5, vm.addOne(4))
    }
}