package ca.nomosnow.cmpt370_9mare.ui.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import ca.nomosnow.cmpt370_9mare.R
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent
import ca.nomosnow.cmpt370_9mare.databinding.FragmentEventDetailsBinding


/**
 * ShowEventDetailsFragment class constructor with ScheduleEvent and NavDirections as parameters
 * show detail of events
 */
class ShowEventDetailsFragment(
    private val event: ScheduleEvent,
    private val action: NavDirections
) : DialogFragment() {

    /**
     * Singleton Object
     */
    companion object {
        const val EVENT_DETAILS = "eventDetails_tag"
    }

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!


    /**
     * createDialog View
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.RoundedCornersDialog)
            _binding = FragmentEventDetailsBinding.inflate(layoutInflater)

            // Inflate and set the layout for the dialog
            builder.setView(binding.root).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /**
     * createView and binding data to layout
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.apply {
            titleValue.text = event.title
            locationValue.text = event.location
            groupValue.text = event.group
            dateValue.text = event.date
            timeValue.text =
                if (event.time_to.isNotEmpty()) "${event.time_from} to ${event.time_to}" else event.time_from
            urlValue.text = event.url
            notesValue.text = event.notes

            editButton.setOnClickListener {
                requireDialog().cancel()
                NavHostFragment.findNavController(requireParentFragment()).navigate(action)
            }

            eventDetailsColor.setBackgroundColor(
                ContextCompat.getColor(
                    eventDetailsColor.context,
                    when (event.group) {
                        "Personal" -> R.color.blue
                        "Work" -> R.color.orange
                        else -> R.color.green
                    }
                )
            )
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}