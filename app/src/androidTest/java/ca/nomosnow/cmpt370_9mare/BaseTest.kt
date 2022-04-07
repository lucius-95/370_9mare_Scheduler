package ca.nomosnow.cmpt370_9mare

import android.view.KeyEvent
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.Rule

open class BaseTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        const val FUTURE = "future"
        const val PAST = "past"
    }

    fun createEvent(
        title: String,
        date: String = FUTURE,
        conflictCheck: Boolean = false,
        timeFrom: String = "04:02",
        timeTo: String = "12:16",
        eventGroup: String = "Personal"
    ) {
        val randomDate = when (date) {
            FUTURE -> "${(2023..9999).random()}-04-02"
            PAST -> "${(1001..2021).random()}-04-02"
            else -> date
        }

        onView(withId(R.id.navigation_calendar)).perform(click())
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.input_title)).perform(typeText(title))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.inputDate)).perform(
            SetButtonText(
                randomDate
            )
        )
        onView(withId(R.id.inputTimeFrom)).perform(
            SetButtonText(
                timeFrom
            )
        )
        onView(withId(R.id.inputTimeTo)).perform(
            SetButtonText(
                timeTo
            )
        )
        if (conflictCheck) {
            onView(withId(R.id.conflict_check)).perform(click())
        }
        onView(withId(R.id.group_menu_autocomplete)).perform(click())
        onView(withText(eventGroup)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        Thread.sleep(50)
        onView(withId(R.id.submit_create_event)).perform(click())
    }

    fun deleteEvent(title: String, past: Boolean = false) {
        Thread.sleep(50)
        onView(withId(R.id.navigation_dashboard)).perform(click())
        if (past) onView(withId(R.id.show_past_events)).perform(click())
        else onView(withId(R.id.show_future_events)).perform(click())
        onView(withId(R.id.event_list_recycler_view)).perform(
            ScrollToBottom()
        )
        onView(withText(title)).perform(click())
        onView(withId(R.id.edit_fab)).perform(click())
        onView(withId(R.id.delete_event)).perform(scrollTo(), click())
        onView(withText("Confirm")).perform(click())
    }

    class SetButtonText(private val text: String) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), isAssignableFrom(Button::class.java))
        }

        override fun perform(uiController: UiController, view: View) {
            (view as Button).text = text
        }

        override fun getDescription(): String {
            return "Replace button text"
        }
    }

    class ScrollToBottom : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), isAssignableFrom(RecyclerView::class.java))
        }

        override fun perform(uiController: UiController?, view: View?) {
            val recyclerView = view as RecyclerView
            val itemCount = recyclerView.adapter?.itemCount
            val position = itemCount?.minus(1) ?: 0
            recyclerView.scrollToPosition(position)
            uiController?.loopMainThreadUntilIdle()
            /* This is not a mistake.
            Repeat is needed for case RecyclerView inside RecyclerView*/
            recyclerView.scrollToPosition(position)
            uiController?.loopMainThreadUntilIdle()
        }

        override fun getDescription(): String {
            return "Scroll RecyclerView to bottom"
        }
    }
}