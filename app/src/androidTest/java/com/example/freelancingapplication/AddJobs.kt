package com.example.freelancingapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
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
class AddJobs {
        @get:Rule
        val testrule = ActivityScenarioRule(PostJob::class.java)

    @Test
    fun addJobsUItest() {
        runBlocking {
            var userRepos = UserRepository()
            ServiceBuilder.token = "Bearer " + userRepos.checkUser("abijeet", "333333").token
            ServiceBuilder.currentuser = userRepos.checkUser("abijeet", "333333").data
            println(ServiceBuilder.token)
        }
        Espresso.onView(ViewMatchers.withId(R.id.titleJ))
                .perform(ViewActions.typeText("Website Develop"))
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.descJ))
                .perform(ViewActions.typeText("Need a full stack website for college assignment"))
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.skillsJ))
                .perform(ViewActions.typeText("Backend, Front End Development"))
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.budgetJ))
                .perform(ViewActions.typeText("Rs 20000 // 20 days"))
        Thread.sleep(1000)
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.postbtnJ))
                .perform(ViewActions.click())

        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.btmNAV))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}