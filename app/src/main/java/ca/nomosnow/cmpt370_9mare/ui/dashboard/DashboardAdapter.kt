package ca.nomosnow.cmpt370_9mare.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.nomosnow.cmpt370_9mare.databinding.GroupEventsViewBinding
import ca.nomosnow.cmpt370_9mare.ui.calendar.DailyEventCalendarAdapter

/**
 * [ListAdapter] implementation for the recyclerview.
 */
class DashboardAdapter(private val fragmentManager: FragmentManager) :
    ListAdapter<DashboardGroupEvents, DashboardAdapter.DashboardViewHolder>(DiffCallback) {

    /**
     * call back Singleton object function to compare objects
     */
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DashboardGroupEvents>() {
            override fun areItemsTheSame(
                oldItem: DashboardGroupEvents,
                newItem: DashboardGroupEvents
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: DashboardGroupEvents,
                newItem: DashboardGroupEvents
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(
            GroupEventsViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * binding item to view holder
     */
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    /**
     * ViewHolder to hold data of DashboardFragment
     */
    inner class DashboardViewHolder(private var binding: GroupEventsViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind views on DashboardFragment to the events' data
         */
        fun bind(group: DashboardGroupEvents) {
            val dailyEventAdapter = DailyEventCalendarAdapter {
                val action =
                    DashboardFragmentDirections.actionNavigationDashboardToCreateEventFragment(it.id)

                ShowEventDetailsFragment(it, action).show(
                    fragmentManager,
                    ShowEventDetailsFragment.EVENT_DETAILS
                )
            }

            binding.dateOfGroup.text = group.date
            binding.groupList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = dailyEventAdapter
            }

            group.events.observe(itemView.context as LifecycleOwner) {
                dailyEventAdapter.submitList(it)
            }
        }
    }
}