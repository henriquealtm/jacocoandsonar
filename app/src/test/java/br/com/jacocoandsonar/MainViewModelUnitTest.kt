package br.com.jacocoandsonar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var mainVm: MainViewModel
    private val showToastMessageWithIdObserver = Observer<Int> {}


    @Before
    fun prepare() {
        mainVm = MainViewModel()
        mainVm.showToastMessageWithId.observeForever(showToastMessageWithIdObserver)
    }

    @After
    fun cleanUp() {
        mainVm.showToastMessageWithId.removeObserver(showToastMessageWithIdObserver)
    }

    @Test
    fun `GIVEN the initial state of MainViewModel THEN showToastMessageWithId_value is null`() {
        assertNull(mainVm.showToastMessageWithId.value)
    }

    @Test
    fun `WHEN calling buttonClick() THEN buttonClick_value is equals to R_string_toast_message id`() {
        mainVm.run {
            buttonClick()
            assertEquals(R.string.toast_message, mainVm.showToastMessageWithId.value)
        }
    }

}