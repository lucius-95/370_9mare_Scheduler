package ca.nomosnow.cmpt370_9mare


import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ca.nomosnow.cmpt370_9mare.ui.calendar.CalendarFragment
import ca.nomosnow.cmpt370_9mare.ui.calendar.CalendarFragmentDirections
import ca.nomosnow.cmpt370_9mare.ui.event.CreateEventFragment
import ca.nomosnow.cmpt370_9mare.ui.event.CreateEventFragmentDirections
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*


/**
 * Tests for all navigation flows
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationTests : BaseTest() {

    /**
     * Testing action fragment from calendar fragment to create event fragment
     */
    @Test
    fun calendar_fragment_navigate_to_create_event_fragment() {
        val mockNavController = mock(NavController::class.java)
        val scenario = launchFragmentInContainer<CalendarFragment>(
            bundleOf("eventID" to -1),
            themeResId = R.style.Theme_9mare
        )
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        // Click start order
        onView(withId(R.id.floatingActionButton)).perform(click())
        verify(mockNavController).navigate(CalendarFragmentDirections.actionNavigationCalendarToCreateEventFragment())
    }


    /**
     * Testing action fragment from create event fragment to calendar using cancel button
     */
    @Test
    fun create_event_fragment_navigate_to_calendar_fragment() {
        val mockNavController = mock(NavController::class.java)
        val scenario =
            launchFragmentInContainer<CreateEventFragment>(
                bundleOf("eventID" to -1),
                themeResId = R.style.Theme_9mare
            )
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        // Click start order
        onView(withId(R.id.cancel_create_event)).perform(click())
        verify(mockNavController).navigateUp()
    }


    /**
     * Testing action fragment from create event fragment to calendar using add button
     */
    @Test
    fun create_event_fragment_navigate_to_calendar_fragment_add() {
        val mockNavController = mock(NavController::class.java)
        val scenario =
            launchFragmentInContainer<CreateEventFragment>(
                bundleOf("eventID" to -1),
                themeResId = R.style.Theme_9mare
            )
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        // perform input
        onView(withId(R.id.input_title)).perform(typeText("Test Event"))
        onView(withId(R.id.inputDate)).perform(SetButtonText("2024-04-02"))
        onView(withId(R.id.inputTimeFrom)).perform(SetButtonText("04:02"))
        onView(withId(R.id.inputTimeTo)).perform(SetButtonText("12:16"))

        // Click start order
        onView(withId(R.id.submit_create_event)).perform(click())
        verify(
            mockNavController,
            atLeastOnce()
        ).navigate(CreateEventFragmentDirections.actionCreateEventFragmentToNavigationCalendar())

        // Remove Test Event
        launchActivity<ca.nomosnow.cmpt370_9mare.MainActivity>()
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.show_future_events)).perform(click())
        deleteEvent("Test Event")
    }
}