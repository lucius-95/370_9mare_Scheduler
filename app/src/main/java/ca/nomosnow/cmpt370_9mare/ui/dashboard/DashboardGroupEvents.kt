package ca.nomosnow.cmpt370_9mare.ui.dashboard

import androidx.lifecycle.LiveData
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent

//DashboardGroupEvents Data type
data class DashboardGroupEvents(
    val date: String,
    val events: LiveData<List<ScheduleEvent>>
)