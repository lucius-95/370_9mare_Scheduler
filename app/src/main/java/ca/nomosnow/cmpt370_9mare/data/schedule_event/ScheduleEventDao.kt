package ca.nomosnow.cmpt370_9mare.data.schedule_event

import androidx.room.*
import kotlinx.coroutines.flow.Flow


/**
 * Dao query for database
 */
@Dao
interface ScheduleEventDao {
    // ignore the conflict for now
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(scheduleEvent: ScheduleEvent)

    @Update
    suspend fun updateEvent(scheduleEvent: ScheduleEvent)

    @Delete
    suspend fun deleteEvent(scheduleEvent: ScheduleEvent)

    @Query("SELECT * FROM event WHERE id = :id")
    fun getScheduleEvent(id: Int): Flow<ScheduleEvent>

    @Query("SELECT * FROM event ORDER BY title ASC")
    fun getScheduleEvents(): Flow<List<ScheduleEvent>>

    @Query("SELECT * FROM event ORDER BY id")
    fun getAllEvents(): Flow<List<ScheduleEvent>>

    @Query("SELECT * FROM event WHERE date = :date ORDER BY time_from")
    fun getEventByDate(date: String): Flow<List<ScheduleEvent>>

    @Query("SELECT DISTINCT date FROM event WHERE date LIKE :month")
    fun getDatesByMonth(month: String): Flow<List<String>>

    @Query(
        "SELECT * FROM event WHERE date = :date AND NOT id = :eventId " +
                "AND NOT (:timeTo <= time_from OR :timeFrom >= time_to) ORDER BY time_from"
    )
    fun getConflictEvent(
        date: String,
        timeFrom: String,
        timeTo: String,
        eventId: Int
    ): Flow<List<ScheduleEvent>>

    @Query("SELECT * FROM event WHERE time_to >= :currentTime AND date = :date ORDER BY time_from")
    fun getDailyEventByTimeAndDate(currentTime: String, date: String): Flow<List<ScheduleEvent>>

    @Query("SELECT DISTINCT date FROM event WHERE title LIKE :name ORDER by date")
    fun searchDatesByEventName(name: String): Flow<List<String>>

    @Query("SELECT * FROM event WHERE date = :date AND title LIKE :name ORDER by time_from")
    fun getEventFromDateAndName(date: String, name: String): Flow<List<ScheduleEvent>>

    @Query("SELECT DISTINCT date FROM event WHERE date >= :currentDate ORDER by date")
    fun getFutureDates(currentDate: String): Flow<List<String>>

    @Query("SELECT DISTINCT date FROM event WHERE date < :currentDate ORDER by date DESC")
    fun getPastDates(currentDate: String): Flow<List<String>>

    @Query("SELECT * FROM event WHERE date >= :currentDate ORDER by date")
    fun getTodayAndFutureEvent(currentDate: String): Flow<List<ScheduleEvent>>
}