package ca.nomosnow.cmpt370_9mare.ui.event

import android.content.Context
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import ca.nomosnow.cmpt370_9mare.R
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent
import ca.nomosnow.cmpt370_9mare.databinding.FragmentCreateEventBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class CreateEventFragmentInit(
    private val fragment: CreateEventFragment,
    private val binding: FragmentCreateEventBinding,
) {
    /**
     * Key listener for hiding the keyboard when the "Enter" button is tapped.
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                fragment.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            true
        } else {
            false
        }
    }

    /**
     * setInputBinding function to call handleKeyEvent if Enter button is clicked, it close down keyboard
     * better for user experience
     */
    fun setInputBinding() = binding.apply {
        inputTitle.setOnKeyListener { view, key, _ -> handleKeyEvent(view, key) }
        inputLocation.setOnKeyListener { view, key, _ -> handleKeyEvent(view, key) }
        eventUrl.setOnKeyListener { view, key, _ -> handleKeyEvent(view, key) }
        eventNotes.setOnKeyListener { view, key, _ -> handleKeyEvent(view, key) }
    }

    /**
     * Set adapter for a dropdown menu
     */
    fun setDropdownAdapter(view: AutoCompleteTextView, objects: List<Any>) {
        view.setAdapter(ArrayAdapter(fragment.requireContext(), R.layout.dropdown_item, objects))
    }

    /**
     * Handler for setting up button, switch, and parameter listeners
     */
    fun setupListeners() {
        binding.apply {

            // Listener for "All-Day" Toggle to show/hide time_picker
            allDay.setOnCheckedChangeListener { _, isCheck ->
                pickTime.isVisible = !isCheck
                // Do not check for conflict if "All-Day" is selected
                if (isCheck) conflictCheck.isChecked = false
                preloadTime()
            }
            // Listener for "Repeat" Toggle to repeat events
            conflictCheck.setOnCheckedChangeListener { _, isCheck ->
                if (isCheck && fragment.eventId <= 0) repeatButton.isChecked = false
            }
            // Listener for "Conflict" Toggle to check for conflicting events
            repeatButton.setOnCheckedChangeListener { _, isCheck ->
                if (isCheck) conflictCheck.isChecked = false
                repeatSpinners.isVisible = isCheck
            }

            // Group Selector Listener to change dot colour
            groupMenuAutocomplete.addTextChangedListener {
                groupColour.background = ContextCompat.getDrawable(
                    fragment.requireContext(),
                    when (groupMenuAutocomplete.text.toString()) {
                        "Personal" -> R.drawable.group_personal
                        "Work" -> R.drawable.group_work
                        else -> R.drawable.group_school
                    }
                )
            }

            // Repeat Listener for changing text field based on parameter input
            spRepetitionIntervalAutocomplete.addTextChangedListener {
                repeatDescription.text =
                    when (binding.spRepetitionIntervalAutocomplete.text.toString()) {
                        "Day(s)" -> fragment.getString(R.string.repeat_description, "Day")
                        "Week(s)" -> fragment.getString(R.string.repeat_description, "Week")
                        else -> fragment.getString(R.string.repeat_description, "Month")
                    }
            }
        }
    }

    /**
     * Function to preload the timeTo input with a set time of 23:59 on opening
     */
    fun preloadTime() {
        fragment.scheduleEventShareViewModel.pickTimeFrom(
            LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).substring(0, 5)
        )
        fragment.scheduleEventShareViewModel.pickTimeTo("23:59")
    }

    /**
     * Binds views with the passed in item data.
     */
    fun bind(event: ScheduleEvent) {
        binding.apply {
            inputTitle.setText(event.title, TextView.BufferType.SPANNABLE)
            inputLocation.setText(event.location, TextView.BufferType.SPANNABLE)
            groupMenuAutocomplete.setText(event.group, false)
            inputDate.text = event.date
            eventUrl.setText(event.url, TextView.BufferType.SPANNABLE)
            eventNotes.setText(event.notes, TextView.BufferType.SPANNABLE)

            if (event.time_from == fragment.getString(R.string.all_day)) {
                allDay.isChecked = true
                preloadTime()
            } else {
                allDay.isChecked = false
                inputTimeFrom.text = event.time_from
                inputTimeTo.text = event.time_to
            }

            pickTime.isVisible = !allDay.isChecked

            // No repeat option while updating events
            repeatButton.isChecked = false
            repeatButton.isVisible = false
        }
    }
}