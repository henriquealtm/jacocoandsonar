package br.com.jacocoandsonar.commons

import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

fun ViewInteraction.doesViewExist() = try {
    check(matches(isDisplayed()))
    true
} catch (e: NoMatchingViewException) {
    false
}