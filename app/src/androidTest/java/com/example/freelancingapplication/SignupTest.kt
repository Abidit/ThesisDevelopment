package com.example.freelancingapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class SignupTest {

        @get:Rule
        val testrule = ActivityScenarioRule(SignupForm::class.java)

        @Test
        fun signupTestUI() {

            Espresso.onView(ViewMatchers.withId(R.id.etFullname))
                    .perform(ViewActions.typeText("Basanta Thapa"))
            Thread.sleep(2000)
            Espresso.closeSoftKeyboard()

            Espresso.onView(ViewMatchers.withId(R.id.etEmail))
                    .perform(ViewActions.typeText("basanta@mail.com"))
            Thread.sleep(2000)
            Espresso.closeSoftKeyboard()

            Espresso.onView(ViewMatchers.withId(R.id.spinnertype))
                .perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Applicant"))
                .perform(ViewActions.click())
            Thread.sleep(2000)

            Espresso.onView(ViewMatchers.withId(R.id.etUsernameS))
                    .perform(ViewActions.typeText("basanta"))
            Thread.sleep(2000)
            Espresso.closeSoftKeyboard()

            Espresso.onView(ViewMatchers.withId(R.id.etPasswordS))
                    .perform(ViewActions.typeText("basanta"))
            Thread.sleep(2000)
            Espresso.closeSoftKeyboard()

            Espresso.onView(ViewMatchers.withId(R.id.etCpassword))
                    .perform(ViewActions.typeText("basanta"))
            Thread.sleep(2000)
            Espresso.closeSoftKeyboard()

            Espresso.onView(ViewMatchers.withId(R.id.btnSignup))
                    .perform(ViewActions.click())

            Thread.sleep(5000)

            Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }


