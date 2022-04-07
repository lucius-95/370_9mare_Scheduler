package ca.nomosnow.cmpt370_9mare

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * NOTE: TEST SUITE DOES NOT REFLECT ModifyTest AS MOST TESTING IS CHECKING VALIDATION
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class ModifyTest : BaseTest() {
    /*
        This test ensures that all data when attempting to modify an event
        is binded to the inputs as its initial value.
     */
    @Test
    fun TC1_text_copies_to_update_fragment() {
        val date = "${(2023..9999).random()}-04-02"
        val testTxt = "Testing"
        val testTitle = "TC1 First event"

        // Create event with all txt inputs with values
        onView(withId(R.id.navigation_calendar)).perform(click())
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.input_title)).perform(
            typeText(testTitle),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.inputDate)).perform(SetButtonText(date))
        onView(withId(R.id.inputTimeFrom)).perform(SetButtonText("04:02"))
        onView(withId(R.id.inputTimeTo)).perform(SetButtonText("12:16"))
        onView(withId(R.id.input_location)).perform(
            typeText("$testTxt Location"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.event_url)).perform(
            scrollTo(),
            typeText("$testTxt URL"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.event_notes)).perform(
            scrollTo(),
            typeText("$testTxt Notes"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.layout_create_event_fragment)).perform(swipeDown())
        onView(withId(R.id.submit_create_event)).perform(click())

        // Attempt to modify and ensure that all data was pushed to modify
        // event parameters
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testTitle)).perform(click())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.input_title)).check(matches(withText(testTitle)))
        onView(withId(R.id.input_location)).check(matches(withText("$testTxt Location")))
        onView(withId(R.id.inputDate)).check(matches(withText(date)))
        onView(withId(R.id.inputTimeFrom)).check(matches(withText("04:02")))
        onView(withId(R.id.inputTimeTo)).check(matches(withText("12:16")))
        onView(withId(R.id.event_url)).perform(scrollTo()).check(matches(withText("$testTxt URL")))
        onView(withId(R.id.event_notes)).perform(scrollTo())
            .check(matches(withText("$testTxt Notes")))

        // Delete event
        onView(withId(R.id.delete_event)).perform(scrollTo(), click())
        onView(withText("Confirm")).perform(click())
    }

    /*
        This test ensures that modifying a parameter changes the
        resulting event.
     */
    @Test
    fun TC2_modifying_text_updates_event() {
        val oldDate = "${(2023..9999).random()}-04-02"
        val newDate = "${(2023..9999).random()}-04-02"
        val testTitle = "TC2 First event"
        val testNewTitle = "TC2 First event modified"

        // Create event
        createEvent(testTitle, oldDate)

        // Modify the title and date of existing event
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testTitle)).perform(click())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.input_title)).perform(
            clearText(),
            typeText(testNewTitle),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.inputDate)).perform(SetButtonText(newDate))
        onView(withId(R.id.submit_create_event)).perform(click())

        // Check to see if event was infact modified
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testNewTitle)).perform(click())
        onView(withText(newDate)).check(matches(isDisplayed()))
        onView(withText("Edit")).perform(click())

        // Delete event
        onView(withId(R.id.delete_event)).perform(scrollTo(), click())
        onView(withText("Confirm")).perform(click())
    }

    /*
        This test ensures that if it modifies itself it will not
        cause conflicts.
     */
    @Test
    fun TC3_no_conflicts_on_modifying_own_event() {
        val testTitle = "TC3 First event"

        // Create and enter event
        createEvent(testTitle)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testTitle)).perform(click())
        onView(withText("Edit")).perform(click())

        // Modify with conflict to and attempt to delete
        onView(withId(R.id.conflict_check)).perform(click())
        onView(withId(R.id.submit_create_event)).perform(click())
        onView(withId(R.id.search_event)).check(matches(isDisplayed()))
        deleteEvent(testTitle)
    }

    /*
        This test ensures that a user can modify an event so that it is
        all-day.
     */
    @Test
    fun TC4_change_time_to_allday() {
        val testTitle = "TC4 First event"

        // Create and enter event
        createEvent(testTitle)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())

        // Modify event with all-day selected
        onView(withText(testTitle)).perform(click())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.all_day)).perform(scrollTo(), click())
        onView(withId(R.id.layout_create_event_fragment)).perform(swipeDown())
        onView(withId(R.id.submit_create_event)).perform(click())

        // Find modified event and check time to ensure it is all-day
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testTitle)).perform(click())
        onView(withText("All-Day")).check(matches(isDisplayed()))
        onView(withText("Edit")).perform(click())

        // Delete event
        onView(withId(R.id.delete_event)).perform(scrollTo(), click())
        onView(withText("Confirm")).perform(click())
    }

    /*
        This test ensures that if an invalid input is entered when modifying an
        event we cannot update the event.
     */
    @Test
    fun TC5_invalid_input_with_modify() {
        val testTitle = "TC5 First event"

        // Create and enter event
        createEvent(testTitle)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())

        // Modify event with a incorrect timing given
        onView(withText(testTitle)).perform(click())
        onView(withText("Edit")).perform(click())
        onView(withId(R.id.inputTimeTo)).perform(SetButtonText("00:00"))
        // Try to submit an invalid event, catch error if thrown and delete event
        try {
            onView(withId(R.id.submit_create_event)).perform(click())
        }
        catch (e: Throwable) {
            onView(withId(R.id.delete_event)).perform(scrollTo(), click())
            onView(withText("Confirm")).perform(click())
        }
    }
}