package ca.nomosnow.cmpt370_9mare

import org.junit.Test
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.runner.RunWith


/**
 * This tests to see if the color of group selected matches. If it matches
 * then the test passes otherwise it fails.
 *
 * */

@RunWith(AndroidJUnit4::class)
@LargeTest
class ColorTest : BaseTest() {

    companion object {
        const val PERSONAL = "Personal"
        const val WORK = "Work"
        const val SCHOOL = "School"

    }

    @Test
            /**
             * Checking to see if the Personal group exist in the list of events
             * */
    fun dashboardEventList() {
        val title = "Doctor's appointment"
        val group = PERSONAL

        createEvent(title, eventGroup = group)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withText(title)).perform(click())
        onView(withText(group)).check(matches(isDisplayed()))
        onView(isRoot()).perform(pressBack())
        deleteEvent(title)

    }


    @Test
            /**
             * Checking to see if the Work group exist in the list of events
             * */
    fun dashboardEventList_2() {
        val title = "Group meeting"
        val group = WORK

        createEvent(title, eventGroup = group)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withText(title)).perform(click())
        onView(withText(group)).check(matches(isDisplayed()))
        onView(isRoot()).perform(pressBack())
        deleteEvent(title)
    }

    @Test
            /**
             * Checking to see if the School group exist in the list of events
             * */
    fun dashboardEventList_3() {
        val title = "CMPT paper due"
        val group = SCHOOL

        createEvent(title, eventGroup = group)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withText(title)).perform(click())
        onView(withText(group)).check(matches(isDisplayed()))
        onView(isRoot()).perform(pressBack())
        deleteEvent(title)
    }
}