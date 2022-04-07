package ca.nomosnow.cmpt370_9mare

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test suite ensures the functionality of "List of Events" (Dashboard) fragment
 * which shows past/future events and gives user an option to search for an event using its name
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class DashboardTest : BaseTest() {

    /**
     * Check default view which is "future events"
     */
    @Test
    fun d1_dashboard_event_list() {
        val testTitle = "D1 - Default List"

        createEvent(testTitle)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testTitle)).check(matches(isDisplayed()))
        deleteEvent(testTitle)
    }

    /**
     * Check view when picking "future events" option
     */
    @Test
    fun d2_dashboard_future_events() {
        val testTitle = "D2 - Future Event"

        createEvent(testTitle)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.show_future_events)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testTitle)).check(matches(isDisplayed()))
        deleteEvent(testTitle)
    }

    /**
     * Check view when picking "past events" option
     */
    @Test
    fun d3_dashboard_past_events() {
        val testTitle = "D3 - Past Event"

        createEvent(testTitle, PAST)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.show_past_events)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testTitle)).check(matches(isDisplayed()))
        deleteEvent(testTitle, true)
    }

    /**
     * Check "search event" functionality
     */
    @Test
    fun d4_dashboard_search_event() {
        val testTitle = "D4 - Search Event"

        createEvent(testTitle)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.search_event)).perform(click())
        onView(withHint("Enter Event Name")).perform(typeText(testTitle))
        onView(withText("SEARCH")).perform(click())
        onView(withText(testTitle)).check(matches(isDisplayed()))
        deleteEvent(testTitle)
    }

    /**
     * Edge case of "search event": input empty string
     */
    @Test
    fun d5_dashboard_search_empty_name() {
        val testTitle = "D5 - Search with Empty Name"

        createEvent(testTitle)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.search_event)).perform(click())
        onView(withText("SEARCH")).perform(click())
        try {
            onView(withText(testTitle)).check(matches(isDisplayed()))
        } catch (e: NoMatchingViewException) {
            deleteEvent(testTitle)
        }
    }
}