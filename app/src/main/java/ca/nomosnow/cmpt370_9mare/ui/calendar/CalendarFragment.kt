package ca.nomosnow.cmpt370_9mare.ui.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import ca.nomosnow.cmpt370_9mare.ScheduleApplication
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModel
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModelFactory
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent
import ca.nomosnow.cmpt370_9mare.databinding.FragmentCalendarBinding
import ca.nomosnow.cmpt370_9mare.ui.dashboard.ShowEventDetailsFragment


/**
 * Calendar Fragment
 */
@RequiresApi(Build.VERSION_CODES.O)
class CalendarFragment : Fragment() {
    // Binding
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    /**
     * shared CalendarViewModel
     */
    private val sharedViewModel: CalendarViewModel by activityViewModels {
        CalendarViewModelFactory((activity?.application as ScheduleApplication).database.scheduleEventDao())
    }

    /**
     * shared ScheduleEventViewModel
     */
    private val sharedScheduleEvent: ScheduleEventViewModel by activityViewModels {
        ScheduleEventViewModelFactory((activity?.application as ScheduleApplication).database.scheduleEventDao())
    }

    /**
     * Create view function for the flagment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        // Giving the binding access to the OverviewViewModel
        return binding.root
    }

    /**
     * onViewCreated bind data from calendar fragment layout
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            calendarFragment = this@CalendarFragment
        }

        // Initialize adapters
        initializeMonthCalendarAdapter()
        initializeDailyEventAdapter(sharedScheduleEvent.eventFromDate(sharedViewModel.selectDate.value.toString()))

        sharedViewModel.selectDate.observe(viewLifecycleOwner) {
            sharedViewModel.datesWithEventInMonth =
                sharedViewModel.datesFromMonths(it.toString().substring(0..6))
        }

        binding.floatingActionButton.setOnClickListener {
            val action = CalendarFragmentDirections.actionNavigationCalendarToCreateEventFragment(
                eventId = -1
            )
            view.findNavController().navigate(action)
        }
    }

    /**
     * destroy all binding data and view when leave fragment
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Initialize MonthCalendarAdapter
     */
    private fun initializeMonthCalendarAdapter() {
        binding.monthCalendarGrid.adapter =
            MonthCalendarAdapter(sharedViewModel, viewLifecycleOwner) {
                sharedViewModel.setSelectDate(it.date)
                initializeDailyEventAdapter(sharedScheduleEvent.eventFromDate(it.date.toString()))
            }
    }


    /**
     * Inlitialize DailyEventAdapter
     */
    private fun initializeDailyEventAdapter(events: LiveData<List<ScheduleEvent>>) {
        val dailyEventAdapter = DailyEventCalendarAdapter {

            val action =
                CalendarFragmentDirections.actionNavigationCalendarToCreateEventFragment(it.id)
            ShowEventDetailsFragment(it, action).show(
                childFragmentManager,
                ShowEventDetailsFragment.EVENT_DETAILS
            )
        }

        binding.dailyEventList.adapter = dailyEventAdapter
        events.observe(this.viewLifecycleOwner) { items ->
            items.let {
                dailyEventAdapter.submitList(it)
            }
        }
    }

    /**
     * goToNextMonth function call next month action which is load next month calendar
     */
    fun goToNextMonth() {
        sharedViewModel.nextMonthAction()
        initializeMonthCalendarAdapter()
        initializeDailyEventAdapter(sharedScheduleEvent.eventFromDate(sharedViewModel.selectDate.value.toString()))
    }

    /**
     * goToPreviousMonth function call previous month action which is load previous month calendar
     */
    fun goToPreviousMonth() {
        sharedViewModel.previousMonthAction()
        initializeMonthCalendarAdapter()
        initializeDailyEventAdapter(sharedScheduleEvent.eventFromDate(sharedViewModel.selectDate.value.toString()))
    }
}
