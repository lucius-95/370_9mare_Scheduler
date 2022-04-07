package ca.nomosnow.cmpt370_9mare.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.nomosnow.cmpt370_9mare.R
import ca.nomosnow.cmpt370_9mare.ScheduleApplication
import ca.nomosnow.cmpt370_9mare.databinding.FragmentDashboardBinding

private const val TAG = "dashboard"
private const val FUTURE = "future"
private const val PAST = "past"
private const val SEARCH = "search"


// DashboardFragment
class DashboardFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: DashboardViewModel by activityViewModels {
        DashboardViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleEventDao()
        )
    }

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView


    /**
     * crete view for fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * binding data to View
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.eventListRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        initializeDashboardAdapter(viewModel.futureEvents)
    }

    /**
     * create option menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Determines how to handle interactions with the selected [MenuItem]
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.search_event -> {
                Log.d(TAG, "searchEvent button clicked")
                showSearchDialog()
                return true
            }
            R.id.show_future_events -> {
                Log.d(TAG, "showFutureEvents button clicked")
                showEvents(FUTURE)
            }
            R.id.show_past_events -> {
                Log.d(TAG, "showPastEvents button clicked")
                showEvents(PAST)
            }
            //  Otherwise, do nothing and use the core event handling
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * destroy view and binding data
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * initialize adapter for dashboard
     */
    private fun initializeDashboardAdapter(events: LiveData<List<DashboardGroupEvents>>) {
        val dashboardAdapter = DashboardAdapter(childFragmentManager)

        recyclerView.adapter = dashboardAdapter
        // Attach an observer on the event list to update the UI automatically when the data changes.
        events.observe(this.viewLifecycleOwner) { items ->
            items.let {
                dashboardAdapter.submitList(it)
            }
        }
    }

    /**
     * setting up showEvents for future events and past events
     */
    private fun showEvents(type: String): Boolean {
        when (type) {
            FUTURE -> initializeDashboardAdapter(viewModel.futureEvents)
            PAST -> initializeDashboardAdapter(viewModel.pastEvents)
            else -> initializeDashboardAdapter(viewModel.searchedEvents)
        }

        return true
    }

    /**
     * invoke shw search dialog
     */
    private fun showSearchDialog() {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Search for Events by Name")
        // Set up the input
        val input = EditText(this.context)
        // Specify the type of input expected
        input.hint = "Enter Event Name"
        input.inputType = InputType.TYPE_CLASS_TEXT

        // Set up the buttons
        builder.setView(input)
            .setPositiveButton(R.string.search) { _, _ -> searchEventAndSetAdapter(input.text) }
            .setNeutralButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            .setOnKeyListener { dialog, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchEventAndSetAdapter(input.text)
                    dialog.dismiss()
                    true
                } else false
            }
            .show()
    }

    /**
     * searchEventAndSetAdapter function take Editable data and display event
     */
    private fun searchEventAndSetAdapter(name: Editable) {
        val eventName = String.format("%%$name%%")
        if (eventName != "%%") {
            viewModel.searchEvent(eventName)
            showEvents(SEARCH)
        } else {
            initializeDashboardAdapter(MutableLiveData())
        }
    }
}