package ca.nomosnow.cmpt370_9mare.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ca.nomosnow.cmpt370_9mare.ScheduleApplication
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModel
import ca.nomosnow.cmpt370_9mare.ScheduleEventViewModelFactory
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent
import ca.nomosnow.cmpt370_9mare.databinding.FragmentHomeBinding
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment() {

    // Initialize Bindings and ViewModels
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: ScheduleEventViewModel by activityViewModels {
        ScheduleEventViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleEventDao()
        )
    }

    // Initialize adapter
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null


    /**
     * Bind layout and view to binding
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Apply necessary inputs based on current date and time
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set current date and time to binding
        val today = LocalDate.now().dayOfMonth
        val month = LocalDate.now().month
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
            homeFragment = this@HomeFragment
            dayNumber.text = today.toString()
            thisMonth.text = month.toString()
        }

        // Set up expandable "Next Events" and "Todays Event" functionality
        val expandableListView = binding.expandableListView
        viewModel.todayAndFutureEvents.observe(this.viewLifecycleOwner) { items ->
            items.let { // Put all children (Schedule Events) into expandable view
                val listData = getData(it)
                titleList = ArrayList(listData.keys)
                adapter = HomeExpandableAdapter(
                    requireContext(),
                    titleList as ArrayList<String>,
                    listData
                )
                expandableListView.setAdapter(adapter)
            }
        }
    }

    /**
     * Getting all data and creating correct expandable containers
     */
    private fun getData(listSchedule: List<ScheduleEvent>): HashMap<String, List<String>> {
        val listData = HashMap<String, List<String>>()
        val todayEvents = ArrayList<String>()
        val nextEvent = ArrayList<String>()
        var lastEvent = 0
        // get today event
        for (event in listSchedule) {
            if (event.date == homeViewModel.getToday()) {
                todayEvents.add(event.time_from + " to " + event.time_to + "      " + event.title)
                lastEvent = event.id
            }
        }
        // No events on current day
        if (todayEvents.isEmpty()) {
            todayEvents.add("No events for Today")
            if (listSchedule.isNotEmpty()) {
                nextEvent.add(listSchedule[0].date + "            " + listSchedule[0].title)
            } else {
                nextEvent.add("No event for future")
            }
        } else { // 1+ events on current day
            for (event in listSchedule) {
                if (event.id == lastEvent + 1) {
                    nextEvent.add(event.date + "            " + event.title)
                }
            }
        }
        // No events in the future
        if (nextEvent.isEmpty()) {
            nextEvent.add("No event for the Future")
        }

        // get future event
        listData["Today Event"] = todayEvents
        listData["Next Event"] = nextEvent

        return listData
    }

    /**
     * Destroy bindings if done with fragment
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}