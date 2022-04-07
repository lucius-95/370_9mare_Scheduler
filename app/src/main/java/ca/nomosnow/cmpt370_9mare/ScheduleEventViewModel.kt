package ca.nomosnow.cmpt370_9mare

import androidx.lifecycle.*
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEventDao
import ca.nomosnow.cmpt370_9mare.data.schedule_event.getCurrentDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ScheduleEventViewModel(private val scheduleEventDao: ScheduleEventDao) : ViewModel() {
    private val today = getCurrentDate()
    val todayAndFutureEvents: LiveData<List<ScheduleEvent>> =
        scheduleEventDao.getTodayAndFutureEvent(today).asLiveData()


    val pickedDate = MutableLiveData<String>()
    val pickedTimeFrom = MutableLiveData<String>()
    val pickedTimeTo = MutableLiveData<String>()

    /**
     * insertEvent function insert new event into EventRoomDatabase
     */
    private fun insertEvent(scheduleEvent: ScheduleEvent) {
        viewModelScope.launch {
            scheduleEventDao.insertEvent(scheduleEvent)
        }
    }

    /**
     * insertEvent function insert new event into EventRoomDatabase
     */
    private fun updateEvent(scheduleEvent: ScheduleEvent) {
        viewModelScope.launch {
            scheduleEventDao.updateEvent(scheduleEvent)
        }
    }

    /**
     * insertEvent function insert new event into EventRoomDatabase
     */
    private fun deleteEvent(scheduleEvent: ScheduleEvent) {
        viewModelScope.launch {
            scheduleEventDao.deleteEvent(scheduleEvent)
        }
    }

    /**
     * private function getNewItemEntry take variables and return new ScheduleEvent
     */
    private fun getNewItemEntry(
        title: String,
        location: String,
        group: String,
        date: String,
        time_from: String,
        time_to: String,
        url: String,
        notes: String
    ): ScheduleEvent {
        return ScheduleEvent(
            title = title,
            location = location,
            group = group,
            date = date,
            time_from = time_from,
            time_to = time_to,
            url = url,
            notes = notes
        )
    }

    /**
     * public function create new item and insert ScheduleEvent into EventRoomDatabase
     */

    fun addNewItem(
        title: String,
        location: String,
        group: String,
        date: String,
        time_from: String,
        time_to: String,
        url: String,
        notes: String
    ) {
        val newItem =
            getNewItemEntry(title, location, group, date, time_from, time_to, url, notes)
        insertEvent(newItem)
    }


    /**
     * Update event function take ScheduleEvent as an arguments to update event
     * [event]: ScheduleEvent
     */
    fun updateItem(
        event: ScheduleEvent
    ) {
        updateEvent(event)
    }


    /**
     *  deleteItem function take ScheduleEvent as an argument to invoke deleteEvent
     *  [event]: ScheduleEvent
     */
    fun deleteItem(event: ScheduleEvent) {
        deleteEvent(event)
    }

    /**
     * eventFromId function take an Int id and return an event which has the given ID
     * [id]: Int
     * return: LiveData<ScheduleEvent>
     */
    fun eventFromId(id: Int): LiveData<ScheduleEvent> =
        scheduleEventDao.getScheduleEvent(id).asLiveData()

    /**
     * eventFromDate function take a String and return list of events
     * [date]:String
     * return LiveData<List<ScheduleEvent>>
     */
    fun eventFromDate(date: String): LiveData<List<ScheduleEvent>> {
        return scheduleEventDao.getEventByDate(date).asLiveData()
    }

    /**
     * setter pickDate function take date as string and set value of pickedDate
     * [date]:String
     */
    fun pickDate(date: String) {
        pickedDate.value = date
    }

    /**
     * setter pickTimeFrom function take time as String and set PickedTimeFrom value
     * [time]:String
     */
    fun pickTimeFrom(time: String) {
        pickedTimeFrom.value = time
    }

    /**
     * setter pickTimeTo function take time as String and set pickedTimeTo value
     * [time]:String
     */
    fun pickTimeTo(time: String) {
        pickedTimeTo.value = time
    }

    /**
     * eventConflicts function take date, timeFrom,timeTo,EventId and return live list of Schedule Event
     * [date]:String
     * [timeFrom]:String
     * [timeTo]:String
     * [eventId]:Int
     * return Flow<List<ScheduleEvent>>
     */
    fun eventConflicts(
        date: String,
        timeFrom: String,
        timeTo: String,
        eventId: Int
    ): Flow<List<ScheduleEvent>> {
        return scheduleEventDao.getConflictEvent(date, timeFrom, timeTo, eventId)
    }
}

/**
 * Boilerplate code to create Singleton ScheduleEventViewModel
 */
class ScheduleEventViewModelFactory(private val scheduleEventDao: ScheduleEventDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleEventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleEventViewModel(scheduleEventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}