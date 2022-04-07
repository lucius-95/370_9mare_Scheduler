package ca.nomosnow.cmpt370_9mare

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import java.time.LocalDate

class HomeTest : BaseTest() {
    @Test
    fun TC1_Test_Today_Event_with_No_Event() {
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.expandableListView)).perform(click())
    }

    @Test
    fun TC2_Test_Today_Event_with_1_Event() {
        val date = LocalDate.now().toString()
        val eventName1 = "TC2 Event 1"
        createEvent(eventName1, date)
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.expandableListView)).perform(click())
        deleteEvent(eventName1)
    }

    @Test
    fun TC3_Test_Today_Event_with_2_Events() {
        val date = LocalDate.now().toString()
        val eventName2 = "TC3 Event 2"
        val eventName3 = "TC3 Event 3"
        createEvent(eventName2, date)
        onView(withId(R.id.navigation_calendar)).perform(click())
        createEvent(eventName3, date)
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.expandableListView)).perform(click())
        deleteEvent(eventName2)
        deleteEvent(eventName3)
    }

    @Test
    fun TC4_Test_Next_Event_with_No_Event() {
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.expandableListView)).perform(click())
    }

    @Test
    fun TC5_Test_Next_Event_with_1_Event() {
        val eventName4 = "TC5 Event 1"
        createEvent(eventName4, timeFrom = "01:00", timeTo = "02:00")
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.expandableListView)).perform(click())
        deleteEvent(eventName4)
    }

    @Test
    fun TC6_Test_Next_Event_with_2_Events() {
        val eventName5 = "TC6 Event 1"
        val eventName6 = "TC6 Event 2"
        createEvent(eventName5, timeFrom = "01:00", timeTo = "02:00")
        createEvent(eventName6, timeFrom = "03:00", timeTo = "04:00")
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.expandableListView)).perform(click())
        deleteEvent(eventName5)
        deleteEvent(eventName6)
    }

    @Test
    fun TC7_Test_Today_display() {
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.expandableListView)).perform(click())
    }


}