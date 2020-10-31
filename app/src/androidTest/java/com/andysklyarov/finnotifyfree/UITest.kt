package com.andysklyarov.finnotifyfree

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andysklyarov.finnotifyfree.ui.MainActivity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isCurrencyCodeVisible() {
        Thread.sleep(1000)
        onView(withId(R.id.currency_textview)).check(matches(isDisplayed()))
    }

    @Test
    fun isCurrencyCodeUSD() {
        Thread.sleep(1000)
        onView(withId(R.id.refresher)).perform(swipeDown())
        onView(withId(R.id.currency_textview)).check(matches(withText("USD")))
    }
}