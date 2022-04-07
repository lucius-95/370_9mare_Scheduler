package ca.nomosnow.cmpt370_9mare.ui.event

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.nomosnow.cmpt370_9mare.R
import ca.nomosnow.cmpt370_9mare.ScheduleApplication
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModel
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModelFactory
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent
import ca.nomosnow.cmpt370_9mare.databinding.FragmentCreateEventBinding
import ca.nomosnow.cmpt370_9mare.ui.calendar.CalendarViewModel
import ca.nomosnow.cmpt370_9mare.ui.calendar.CalendarViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class CreateEventFragment : Fragment() {
    // Singleton calendarViewModel
    private val calendarViewModel: CalendarViewModel by activityViewModels {
        CalendarViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleEventDao()
        )
    }

    // Singleton scheduleEventViewModel
    val scheduleEventShareViewModel: ScheduleEventViewModel by activityViewModels {
        ScheduleEventViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleEventDao()
        )
    }

    // Binding
    private var _binding: FragmentCreateEventBinding? = null
    val binding get() = _binding!!

    // Navigation related
    private val navigationArgs: CreateEventFragmentArgs by navArgs()
    var eventId = 0

    // Sub-classes for createEventFragment functionalities
    private val initialization get() = CreateEventFragmentInit(this, binding)
    private val validation get() = CreateEventValidation(this, binding)
    private val inputs get() = CreateEventInputHandler(this, binding)

    // Existing event when in Edit mode
    lateinit var currentEvent: ScheduleEvent


    /**
     * create CreateEventFragment instance and accept params argument from another fragment
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get value of eventId argument
        eventId = navigationArgs.eventId
        // Clear the date and time variables in viewModel
        scheduleEventShareViewModel.pickDate(calendarViewModel.selectDate.value.toString())
    }

    /**
     * binding FragmentCreateEventBinding and inflate view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)

        /* Dropdown variables */
        val eventGroups = resources.getStringArray(R.array.event_groups).toList()
        val repeatTypes = resources.getStringArray(R.array.repeat_types).toList()
        val repeatLengths = (1..30).toList()
        /* Set dropdown adapters */
        initialization.setDropdownAdapter(binding.groupMenuAutocomplete, eventGroups)
        initialization.setDropdownAdapter(binding.spRepeatEveryAutocomplete, repeatLengths)
        initialization.setDropdownAdapter(binding.spRepetitionIntervalAutocomplete, repeatTypes)
        initialization.preloadTime()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Bind view models to displayed fragment
        binding.apply {
            viewModel = scheduleEventShareViewModel
            createEventFragment = this@CreateEventFragment
        }

        // Check to see if passing an ID to the fragment (Update Case)
        // If so specialize fragment for modifying events
        if (eventId > 0) {
            binding.apply {
                submitCreateEvent.text = getString(R.string.update_button_text)
                deleteEvent.isVisible = true
            }
            scheduleEventShareViewModel.eventFromId(eventId)
                .observe(this.viewLifecycleOwner) { selectedItem ->
                    if (selectedItem != null) {
                        currentEvent = selectedItem
                        initialization.bind(currentEvent)
                    }
                }
            (activity as AppCompatActivity?)?.supportActionBar?.title =
                getString(R.string.modify_event_title)
        } else {
            binding.repeatDescription.text = getString(R.string.repeat_description, "Day")
        }

        // Init all other inputs/validations
        initialization.setupListeners()
        initialization.setInputBinding()
        validation.setUpValidations()
    }

    override fun onResume() {
        super.onResume()
        // Take current date and time and apply it to the event parameters
        scheduleEventShareViewModel.apply {
            pickedDate.observe(viewLifecycleOwner) { binding.inputDate.text = it }
            pickedTimeFrom.observe(viewLifecycleOwner) { binding.inputTimeFrom.text = it }
            pickedTimeTo.observe(viewLifecycleOwner) { binding.inputTimeTo.text = it }
        }
    }

    /**
     *  Handler for all creation and modifications of events within the
     *  CreateEventFragment.
     */
    fun createModifyEvent() {
        // No Conflict Checks
        if (!binding.conflictCheck.isChecked) {
            if (eventId > 0) { // Update/Modify Event
                inputs.updateEvent()
            } else { // Add Event
                inputs.addNewEvent()
            }
        } else { // Conflict Checks
            inputs.apply {
                // Begin a coroutine to wait on the Query to database to finish
                lifecycle.coroutineScope.launch {
                    scheduleEventShareViewModel.eventConflicts(date, timeFrom, timeTo, eventId)
                        .collect {
                            when {
                                it.isNotEmpty() -> { // Query came back with 1+ conflicting events
                                    validation.showConflictDialog(it)
                                }
                                eventId > 0 -> { // No conflicts + Modify/Update Event
                                    updateEvent()
                                }
                                else -> { // No conflicts + Add Event
                                    addNewEvent()
                                }
                            }
                        }
                }
            }
        }
    }

    /**
     * Handler for deleting event confirmation pop-up
     */
    fun deleteEvent() {
        // Build dialog with confirm and cancel buttons
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage("Delete the current event?")
            .setCancelable(false)
            .setPositiveButton("Confirm") { _, _ ->
                scheduleEventShareViewModel.deleteItem(currentEvent)
                findNavController().navigateUp()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    /**
     * Button Listeners
     */

    // User wants to stop creating/modifying event
    fun cancelEvent() {
        findNavController().navigateUp()
    }

    // User wants to edit date
    fun showDatePicker() = DatePickerFragment(inputs.date).show(
        childFragmentManager,
        DatePickerFragment.DATE_PICKER
    )

    // User wants to edit timeFrom
    fun showTimeFromPicker() = TimePickerFragment(inputs.timeFrom).show(
        childFragmentManager,
        TimePickerFragment.TIME_FROM_PICKER
    )

    // User wants to edit timeTo
    fun showTimeToPicker() = TimePickerFragment(inputs.timeTo).show(
        childFragmentManager,
        TimePickerFragment.TIME_TO_PICKER
    )
}