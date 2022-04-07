package ca.nomosnow.cmpt370_9mare.ui.event

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ca.nomosnow.cmpt370_9mare.R
import ca.nomosnow.cmpt370_9mare.ScheduleApplication
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModel
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Use the [TimePickerFragment] factory method to
 * create an instance of this fragment.
 */
class TimePickerFragment(private val time: String) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    companion object {
        const val TIME_FROM_PICKER = "timeFromPicker_tag"
        const val TIME_TO_PICKER = "timeToPicker_tag"
    }

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: ScheduleEventViewModel by activityViewModels {
        ScheduleEventViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleEventDao()
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val hour = time.slice(0..1).toInt()
        val minute = time.slice(3..4).toInt()

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(requireActivity(), R.style.PickerStyle, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker, hour: Int, time: Int) {
        // Format month and day to add a 0
        val timeString = String.format("%02d:%02d", hour, time)

        if (this.tag == TIME_FROM_PICKER) {
            viewModel.pickTimeFrom(timeString)
        } else {
            viewModel.pickTimeTo(timeString)
        }
    }

}