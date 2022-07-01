package br.com.jacocoandsonar

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.jacocoandsonar.commons.BaseRoboeletricTest
import br.com.jacocoandsonar.commons.doesViewExist
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUnitTest : BaseRoboeletricTest() {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Test
    fun `Starts without crashing`() {
        scenario = launchActivity()
    }

    @Test
    fun `Verify if the binding property is loaded with MainActivity`() {
        scenario = launchActivity()
        scenario.onActivity {
            assertNotNull(it.binding)
            assertNotNull(it.binding.vm)
            assertTrue(it.binding.vm is MainViewModel)
        }
    }

    // TODO - This test doesn't seem a unit test. There is a click, a LiveData update, an state observing and a showing snack bar
    // TODO - But mock the ViewModel for this example would take too much :|
    // TODO - MAYBE this should be broken in:
    // TODO -
    // TODO - ONE: Perform the click and verify if the mocked ViewModel buttonClick() was called
    // TODO - TWO: Verify if the snackBar is hidden when just creating the screen
    // TODO - THREE: Force showToast state to true and verify if snackBar is shown with the right message
    // TODO - FOUR: Force showToast state to false and verify if the snackBar is hidden when just creating the screen
    @Test
    fun `Click in the button and show the Snackbar`() {
        scenario = launchActivity()

        onView(withId(R.id.button)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Message to show on Toast - Now in the AAB distributed with Firebase")))
    }

    @Test
    fun `Verify if the SnackBar is not being shown when MainActivity is launched`() {
        scenario = launchActivity()
        scenario.onActivity {
            val snackBarExists = onView(
                withId(com.google.android.material.R.id.snackbar_text)
            ).doesViewExist()

            assertFalse(snackBarExists)
        }
    }

}