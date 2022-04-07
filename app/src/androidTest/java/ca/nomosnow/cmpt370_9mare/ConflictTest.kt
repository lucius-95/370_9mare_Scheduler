package ca.nomosnow.cmpt370_9mare

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ConflictTest : BaseTest() {
    /*
        Checks if creating an event in conflict with the first event
        causes a pop-up and prevents the creation of the second event
     */
    @Test
    fun TC1_create_event_causes_conflict() {
        val conflictDate = "${(2023..9999).random()}-04-02"
        val testTitle = "TC1 First event"
        val conflictTitle = "TC1 Conflict event"

        // Create both events
        createEvent(testTitle, conflictDate)
        createEvent(conflictTitle, conflictDate, true, "06:02")

        // Check if the pop-up occurred, if so everything worked and we need to
        // delete the first event
        onView(withText("Conflict Found")).check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())
        onView(withId(R.id.cancel_create_event)).perform(click())
        deleteEvent(testTitle)
    }

    /*
        Checks if modifying an event with conflicts enabled correctly identifies
        and conflicts with the conflicting event.
     */
    @Test
    fun TC2_modify_event_causes_conflict() {
        val conflictDate = "${(2023..9999).random()}-04-02"
        val testTitle = "TC2 First event"
        val conflictTitle = "TC2 Conflict event"

        // Create both events first without conflicts
        createEvent(testTitle, conflictDate)
        createEvent(conflictTitle, conflictDate, false, "12:16", "14:16")

        // Modify second event to conflict with first event
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.show_future_events)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(conflictTitle)).perform(click())
        onView(withText(R.string.edit)).perform(click())
        onView(withId(R.id.inputTimeFrom)).perform(SetButtonText("06:00"))
        onView(withId(R.id.conflict_check)).perform(click())
        onView(withId(R.id.submit_create_event)).perform(click())

        // Check for conflicts, if so, delete both events
        onView(withText("Conflict Found")).check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())
        onView(withId(R.id.cancel_create_event)).perform(click())
        deleteEvent(testTitle)
        deleteEvent(conflictTitle)
    }

    /*
        Checks to see if a deleted event can still cause conflicts
     */
    @Test
    fun TC3_delete_event_then_create_event_causes_no_conflict() {
        val testTitle = "TC3 Test event"

        // Create an event and then delete it
        createEvent(testTitle)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        deleteEvent(testTitle)

        // Check to see if creating the same event as a new event causes a conflict
        // If not, delete the new event
        createEvent(testTitle, conflictCheck = true)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(ScrollToBottom())
        onView(withText(testTitle)).check(matches(isDisplayed()))
        deleteEvent(testTitle)
    }

    /*
        Checks to see if all conflicting events show in the alert pop-up
     */
    @Test
    fun TC4_conflicting_events_show_in_dialog() {
        val conflictDate = "${(2023..9999).random()}-04-02"
        val testTitle = "TC4 First event"
        val conflictTitle = "TC4 Conflict event"

        // Create both events and see if the first event shows up in pop-up text
        createEvent(testTitle, conflictDate)
        createEvent(conflictTitle, conflictDate, true, "06:02")
        onView(withText("Conflict Found")).check(matches(isDisplayed()))
        onView(withText("TC4 First event: 04:02 - 12:16\n")).check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())

        // If so delete the first event
        onView(withId(R.id.cancel_create_event)).perform(click())
        deleteEvent(testTitle)
    }

    /*
        Check to see if multiple conflicts appear in pop-up dialog text
     */
    @Test
    fun TC5_multiple_conflicting_events() {
        val conflictDate = "${(2023..9999).random()}-04-02"
        val testTitle1 = "TC5 First event"
        val testTitle2 = "TC5 Second event"
        val conflictTitle = "TC5 Conflict event"

        // Create 3 events, 1 of which will conflict then make sure both events are within the dialog
        createEvent(testTitle1, conflictDate)
        createEvent(testTitle2, conflictDate, false, "12:45", "13:45")
        createEvent(conflictTitle, conflictDate, true, "06:02", "13:00")
        onView(withText("Conflict Found")).check(matches(isDisplayed()))
        onView(withText("TC5 First event: 04:02 - 12:16\nTC5 Second event: 12:45 - 13:45\n"))
            .check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())

        // If both are within the dialog, delete the first 2 events
        onView(withId(R.id.cancel_create_event)).perform(click())
        deleteEvent(testTitle1)
        deleteEvent(testTitle2)
    }

    /*
        Checks to make sure an event can start on the end of another event
     */
    @Test
    fun TC6_conflict_timing_not_exclusive() {
        val conflictDate = "${(2023..9999).random()}-04-02"
        val testTitle1 = "TC6 First event"
        val testTitle2 = "TC6 Second event"

        // Create an event where the timeFrom is the first events timeTo
        createEvent(testTitle1, conflictDate)
        createEvent(testTitle2, conflictDate, true, "12:16", "13:16")

        // Trying to delete if a pop-up occurs would cause an error, therefore only works
        // if no conflict occurs which is what we desire
        deleteEvent(testTitle1)
        deleteEvent(testTitle2)
    }
}