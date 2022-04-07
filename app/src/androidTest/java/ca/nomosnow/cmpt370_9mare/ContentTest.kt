package ca.nomosnow.cmpt370_9mare


import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ca.nomosnow.cmpt370_9mare.ui.calendar.CalendarFragment
import ca.nomosnow.cmpt370_9mare.ui.event.CreateEventFragment
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ContentTest : BaseTest() {

    @Test
    fun schedule_fragment_content_test() {
        launchFragmentInContainer<CalendarFragment>(themeResId = R.style.Theme_9mare)
        onView(withId(R.id.next_month_calendar)).perform(click())
        onView(withId(R.id.button_last_month)).perform(click())

        // check for the content of the calender
        onView(withId(R.id.month_calendar_grid)).check(matches(notNullValue()))
        onView(withId(R.id.month_calendar_grid)).check(matches(isNotClickable()))

        // check for create new event button
        onView(withId(R.id.floatingActionButton)).check(matches(isClickable()))
    }

    /**
     * This function will test for event, description and location inputs
     * */
    @Test
    fun title_description_event_test() {
        // Check for the title event
        launchFragmentInContainer<CreateEventFragment>(
            bundleOf("eventID" to -1),
            themeResId = R.style.Theme_9mare
        )
        onView(withId(R.id.input_title)).check(matches(withHint(R.string.title)))
        onView(withId(R.id.event_title)).check(matches(isNotClickable()))

        // Check for the event location
        onView(withId(R.id.input_location)).check(matches(withHint(R.string.location)))
        onView(withId(R.id.event_location)).check(matches(isNotClickable()))
    }

    @Test
    fun all_day_option_test() {
        launchFragmentInContainer<CreateEventFragment>(
            bundleOf("eventID" to -1),
            themeResId = R.style.Theme_9mare
        )
        onView(withId(R.id.all_day)).check(matches(withText(R.string.all_day)))
        onView(withId(R.id.all_day)).check(matches(isNotChecked()))
        onView(withId(R.id.all_day)).perform(click())
    }

    @Test
    fun start_end_test() {
        launchFragmentInContainer<CreateEventFragment>(
            bundleOf("eventID" to -1),
            themeResId = R.style.Theme_9mare
        )

        // check for date
        onView(withId(R.id.inputDate)).check(matches(isClickable()))

        // check for start time
        onView(withId(R.id.inputTimeFrom)).check(matches(isClickable()))

        // check for end time
        onView(withId(R.id.inputTimeTo)).check(matches(isClickable()))
    }


    @Test
    fun url_notes_test() {
        launchFragmentInContainer<CreateEventFragment>(
            bundleOf("eventID" to -1),
            themeResId = R.style.Theme_9mare
        )
        // Check for the url
        onView(withId(R.id.event_url))
            .check(matches(withHint("URL")))
        onView(withId(R.id.url_layout))
            .check(matches(isNotClickable()))

        // Check for the notes
        onView(withId(R.id.event_notes))
            .check(matches(withHint("Notes")))
        onView(withId(R.id.event_notes_layout))
            .check(matches(isNotClickable()))
    }

    @Test
    fun cancel_create_button_test() {
        launchFragmentInContainer<CreateEventFragment>(
            bundleOf("eventID" to -1),
            themeResId = R.style.Theme_9mare
        )
        onView(withId(R.id.cancel_create_event))
            .check(matches(withText("Cancel")))
        onView(withId(R.id.cancel_create_event))
            .check(matches(isClickable()))

        onView(withId(R.id.submit_create_event))
            .check(matches(withText("Add")))
        onView(withId(R.id.submit_create_event))
            .check(matches(isClickable()))
    }

    @Test
    fun TC1_Test_title_pickDate_pickTime() {
        val testName = "TC1"

        createEvent(testName)
        deleteEvent(testName)
    }

    @Test
    fun TC2_Test_title_pickDate_pickTime() {
        val testName = "TC2"

        createEvent(testName, PAST)
        deleteEvent(testName, true)
    }

    @Test
    fun TC3_Test_title_pickDate_pickTime() {
        val testName = "TC3"

        createEvent(testName, FUTURE, timeFrom = "03:00", timeTo = "05:00")
        onView(withId(R.id.navigation_calendar)).perform(click())
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.show_future_events)).perform(click())
        deleteEvent(testName)
    }

    @Test
    fun TC4_Test_title_pickDate_pickTime() {
        val testName = "TC4"

        createEvent(testName, PAST)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.show_past_events)).perform(click())
        deleteEvent(testName, true)
    }

    @Test
    fun TC5_Test_title_pickDate_pickTime() {
        val testName = "TC5"

        createEvent(testName, PAST, timeFrom = "03:00", timeTo = "05:00")
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.show_past_events)).perform(click())
        deleteEvent(testName, true)
    }
}