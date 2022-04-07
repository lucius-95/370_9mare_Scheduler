package ca.nomosnow.cmpt370_9mare

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class CreateEventTest : BaseTest() {
    /**
     * CreateEventWithAllDayButtonTest
     */
    @Test
    fun createEventWithAllDayButtonTest() {
        onView(withId(R.id.navigation_calendar)).perform(click())
        onView(withId(R.id.next_month_calendar)).perform(click())
        onView(withId(R.id.month_calendar_grid)).check(ViewAssertions.matches(CoreMatchers.notNullValue()))
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.input_title)).perform(
            typeText("RepeatEvent"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.inputDate)).perform(SetButtonText("2022-04-05"))
        onView(withId(R.id.inputTimeFrom)).perform(SetButtonText("04:02"))
        onView(withId(R.id.inputTimeTo)).perform(SetButtonText("12:16"))
        onView(withId(R.id.all_day)).perform(click())
        onView(withId(R.id.input_location)).perform(
            typeText("Saskatoon Location"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.event_url)).perform(
            scrollTo(),
            typeText("https://nomosnow.ca"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.event_notes)).perform(
            scrollTo(),
            typeText("some Notes"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.layout_create_event_fragment)).perform(swipeDown())
        onView(withId(R.id.submit_create_event)).perform(click())
    }


    /**
     * CreateEventWithAllDayButtonTest
     */
    @Test
    fun createEventWithRepeatButtonTest() {
        onView(withId(R.id.navigation_calendar)).perform(click())
        onView(withId(R.id.next_month_calendar)).perform(click())
        onView(withId(R.id.month_calendar_grid)).check(ViewAssertions.matches(CoreMatchers.notNullValue()))
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.input_title)).perform(
            ViewActions.typeText("RepeatEvent"),
            ViewActions.pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.inputDate)).perform(SetButtonText("2022-03-30"))
        onView(withId(R.id.inputTimeFrom)).perform(SetButtonText("00:02"))
        onView(withId(R.id.inputTimeTo)).perform(SetButtonText("01:16"))
        onView(withId(R.id.repeat_button)).perform(click())
        onView(withId(R.id.spRepeatEvery_autocomplete)).perform(click())
        onView(withText("4")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.input_location)).perform(
            typeText("Saskatoon Location"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.event_url)).perform(
            scrollTo(),
            typeText("https://nomosnow.ca"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.event_notes)).perform(
            scrollTo(),
            typeText("some Notes"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.layout_create_event_fragment)).perform(ViewActions.swipeDown())
        onView(withId(R.id.submit_create_event)).perform(click())
    }

    /**
     * CreateEventWithAllDayButtonAndWeekRepeatsTest
     */
    @Test
    fun createEventWithWeeksRepeatButtonTest() {
        onView(withId(R.id.navigation_calendar)).perform(click())
        onView(withId(R.id.next_month_calendar)).perform(click())
        onView(withId(R.id.month_calendar_grid)).check(ViewAssertions.matches(CoreMatchers.notNullValue()))
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.input_title)).perform(
            typeText("RepeatWeekEvent"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.inputDate)).perform(SetButtonText("2022-03-29"))
        onView(withId(R.id.repeat_button)).perform(click())
        onView(withId(R.id.spRepeatEvery_autocomplete)).perform(click())
        onView(withText("2")).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.spRepetitionInterval_autocomplete)).perform(click())
        onView(withText("Week(s)")).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.input_location)).perform(
            typeText("Saskatoon Location"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.event_url)).perform(
            scrollTo(),
            typeText("https://nomosnow.ca"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.event_notes)).perform(
            scrollTo(),
            typeText("some Notes"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.layout_create_event_fragment)).perform(swipeDown())
        onView(withId(R.id.submit_create_event)).perform(click())
    }


}