package ca.nomosnow.cmpt370_9mare

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * This test suite ensures that after an event is created, it is shown in the
 * Calendar Fragment in the date it belongs to
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class CalendarViewTest : BaseTest() {

    /**
     * The event is created on the current date
     */
    @Test
    fun c1_calendar_current_date() {
        val date = LocalDate.now().toString()
        val testTitle = "C1 - Current Date"

        createEvent(testTitle, date)
        onView(withText(testTitle)).check(matches(isDisplayed()))
        deleteEvent(testTitle)
    }

    /**
     * The event is created in the next month
     */
    @Test
    fun c2_calendar_next_month() {
        val date = LocalDate.now().plusMonths(1).toString()
        val testTitle = "C2 - Next Month"

        createEvent(testTitle, date)
        onView(withId(R.id.next_month_calendar)).perform(click())
        onView(withText(testTitle)).check(matches(isDisplayed()))
        deleteEvent(testTitle)
    }

    /**
     * The event is created in the last month
     */
    @Test
    fun c3_calendar_last_month() {
        val date = LocalDate.now().minusMonths(1).toString()
        val testTitle = "C3 - Last Month"

        createEvent(testTitle, date)
        onView(withId(R.id.button_last_month)).perform(click())
        onView(withText(testTitle)).check(matches(isDisplayed()))
        deleteEvent(testTitle, true)
    }
}