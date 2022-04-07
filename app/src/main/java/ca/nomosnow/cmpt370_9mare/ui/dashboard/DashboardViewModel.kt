package ca.nomosnow.cmpt370_9mare.ui.dashboard

import androidx.lifecycle.*
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEventDao
import ca.nomosnow.cmpt370_9mare.data.schedule_event.getCurrentDate

/**
 * DashboardViewModel is a shared model across entire app
 */
class DashboardViewModel(private val scheduleEventDao: ScheduleEventDao) : ViewModel() {

    private val today = getCurrentDate()

    // Future/Past dates compared to today's date
    private val futureDates: LiveData<List<String>> =
        scheduleEventDao.getFutureDates(today).asLiveData()
    private val pastDates: LiveData<List<String>> =
        scheduleEventDao.getPastDates(today).asLiveData()

    // Future/Past events
    val futureEvents = getEventsFromDates(futureDates)
    val pastEvents = getEventsFromDates(pastDates)

    // Events from search result
    lateinit var searchedEvents: LiveData<List<DashboardGroupEvents>>

    /**
     * Get groups of events from a list of dates
     */
    private fun getEventsFromDates(dates: LiveData<List<String>>): LiveData<List<DashboardGroupEvents>> =
        dates.map {
            it.map { date ->
                DashboardGroupEvents(
                    date,
                    scheduleEventDao.getEventByDate(date).asLiveData()
                )
            }
        }

    /**
     * Search for event using name
     */
    fun searchEvent(name: String) {
        val dates = scheduleEventDao.searchDatesByEventName(name).asLiveData()

        searchedEvents = dates.map {
            it.map { date ->
                DashboardGroupEvents(
                    date,
                    scheduleEventDao.getEventFromDateAndName(date, name).asLiveData()
                )
            }
        }
    }
}

/**
 * Create Singleton DashboardViewModel
 */
class DashboardViewModelFactory(private val scheduleEventDao: ScheduleEventDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(scheduleEventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}