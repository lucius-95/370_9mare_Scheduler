package ca.nomosnow.cmpt370_9mare.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.nomosnow.cmpt370_9mare.R
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent
import ca.nomosnow.cmpt370_9mare.databinding.DailyEventViewBinding

/**
 * DailyEventCalendarAdapter
 */
class DailyEventCalendarAdapter(private val onItemClicked: (ScheduleEvent) -> Unit) :
    ListAdapter<ScheduleEvent, DailyEventCalendarAdapter.DailyEventViewHolder>(DiffCallback) {


    /**
     * DailyEventViewHolder create view holder and bind data into it
     */
    class DailyEventViewHolder(private var binding: DailyEventViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(scheduleEvent: ScheduleEvent) {
            binding.apply {
                dailyEventTitle.text = scheduleEvent.title
                dailyEventTimeStart.text = scheduleEvent.time_from
                dailyEventTimeEnd.text = scheduleEvent.time_to
                dailyEventColor.setBackgroundColor(
                    ContextCompat.getColor(
                        dailyEventColor.context,
                        when (scheduleEvent.group) {
                            "Personal" -> R.color.blue
                            "Work" -> R.color.orange
                            else -> R.color.green
                        }
                    )
                )
            }
        }
    }

    /**
     * create layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyEventViewHolder {
        return DailyEventViewHolder(
            DailyEventViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    /**
     * bind item with onClick()
     */
    override fun onBindViewHolder(holder: DailyEventViewHolder, position: Int) {
        val scheduleEvent = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(scheduleEvent)
        }
        holder.bind(scheduleEvent)
    }

    /**
     * Singleton Object to compare if the same object is called
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ScheduleEvent>() {
        override fun areItemsTheSame(oldItem: ScheduleEvent, newItem: ScheduleEvent): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ScheduleEvent, newItem: ScheduleEvent): Boolean {
            return oldItem == newItem
        }
    }
}