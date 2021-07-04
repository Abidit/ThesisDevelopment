package com.example.freelancingapplication

import adapter.AppAdapter
import adapter.JobAdapter
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import api.ServiceBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import repository.UserRepository


@LargeTest
@RunWith(JUnit4::class)
class acceptApplication {

    @get:Rule
    val testrule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun acceptapplication() {

        runBlocking {
            var userRepos = UserRepository()
            ServiceBuilder.token = "Bearer " + userRepos.checkUser("abijeet", "333333").token
            ServiceBuilder.currentuser = userRepos.checkUser("abijeet", "333333").data

        }
        Espresso.onView(ViewMatchers.withId(R.id.ic_Jobs)).perform(ViewActions.click())
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.apprecycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AppAdapter.AdapterViewHolder>
                    (1, clickOnViewChild(R.id.acceptbtn)))
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.btmNAV))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = ViewActions.click()
            .perform(uiController, view.findViewById<View>(viewId))
    }

}