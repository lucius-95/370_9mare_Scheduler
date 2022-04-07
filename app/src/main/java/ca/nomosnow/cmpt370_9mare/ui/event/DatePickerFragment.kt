package ca.nomosnow.cmpt370_9mare.ui.event

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ca.nomosnow.cmpt370_9mare.R
import ca.nomosnow.cmpt370_9mare.ScheduleApplication
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModel
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModelFactory

class DatePickerFragment(private val date: String) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    companion object {
        const val DATE_PICKER = "datePicker_tag"
    }

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: ScheduleEventViewModel by activityViewModels {
        ScheduleEventViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleEventDao()
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val year = date.slice(0..3).toInt()
        val month = date.slice(5..6).toInt() - 1
        val day = date.slice(8..9).toInt()

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), R.style.PickerStyle, this, year, month, day)
    }


    // Set the pickedDate in the shared ScheduleEventViewModel
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Format month and day to add a 0
        val dateString = String.format("$year-%02d-%02d", month + 1, day)

        viewModel.pickDate(dateString)
    }
}
