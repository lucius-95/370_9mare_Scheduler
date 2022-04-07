package ca.nomosnow.cmpt370_9mare

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import ca.nomosnow.cmpt370_9mare.data.schedule_event.EventRoomDatabase
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEvent
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEventDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RunWith(AndroidJUnit4::class)
@MediumTest
class ScheduleEventDaoTest {
    private lateinit var scheduleEventDao: ScheduleEventDao
    private lateinit var db: EventRoomDatabase
    private val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)

    private fun convertDate(someDate: String): LocalDate? {
        return LocalDate.parse(someDate, formatter)
    }

    // create database
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, EventRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        scheduleEventDao = db.scheduleEventDao()
    }

    //close database
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * create first Event with full input
     */
    @Test
    @Throws(IOException::class)
    fun insertAndGetScheduleEventTest1(): Unit = runBlocking {
        val scheduleEvent = ScheduleEvent(
            1,
            "event1",
            "Saskatoon",
            "Personal",
            "July 25, 2017",
            "10:40 AM",
            "10:50 AM",
            "",
            "event1 testing notes"
        )
        scheduleEventDao.insertEvent(scheduleEvent)
        val events: List<ScheduleEvent> = scheduleEventDao.getScheduleEvents().first()
        assertEquals(events[0].id, scheduleEvent.id)
        assertEquals(events[0].date, scheduleEvent.date)
        assertEquals(events[0].date, scheduleEvent.date)
        assertEquals(events[0].time_from, scheduleEvent.time_from)
        assertEquals(events[0].time_to, scheduleEvent.time_to)
        assertEquals(events[0].location, scheduleEvent.location)
        assertEquals(events[0].url, scheduleEvent.url)
        assertEquals(events[0].notes, scheduleEvent.notes)
    }

    /**
     * create first Event with only title
     */
    @Test
    @Throws(IOException::class)
    fun insertAndGetScheduleEventTest2(): Unit = runBlocking {
        val scheduleEvent = ScheduleEvent(2, "event2", "", "", "", "", "", "", "")
        scheduleEventDao.insertEvent(scheduleEvent)
        val events: List<ScheduleEvent> = scheduleEventDao.getScheduleEvents().first()
        assertEquals(2, scheduleEvent.id)
        assertEquals(events[0].title, scheduleEvent.title)
        assertEquals("", scheduleEvent.date)
        assertEquals("", scheduleEvent.time_from)
        assertEquals("", scheduleEvent.time_to)
        assertEquals("", scheduleEvent.location)
        assertEquals("", scheduleEvent.url)
        assertEquals("", scheduleEvent.notes)
        assertEquals("", scheduleEvent.group)
    }


    /**
     * Test size of the even after created 3 events
     */
    @Test
    @Throws(IOException::class)
    fun getScheduleEventsTest(): Unit = runBlocking {
        val scheduleEvent1 = ScheduleEvent(1, "event1", "", "", "", "", "", "", "")
        val scheduleEvent2 = ScheduleEvent(2, "event2", "", "", "", "", "", "", "")
        val scheduleEvent3 = ScheduleEvent(3, "event3", "", "", "", "", "", "", "")
        scheduleEventDao.insertEvent(scheduleEvent1)
        scheduleEventDao.insertEvent(scheduleEvent2)
        scheduleEventDao.insertEvent(scheduleEvent3)
        assertEquals(3, scheduleEventDao.getScheduleEvents().first().size)
        assertEquals(scheduleEvent2.title, scheduleEventDao.getScheduleEvent(2).first().title)
    }


    /**
     * Test getScheduleEvent
     */
    @Test
    @Throws(IOException::class)
    fun getScheduleEventTest1(): Unit = runBlocking {
        val scheduleEvent1 = ScheduleEvent(1, "event1", "", "", "", "", "", "", "")
        scheduleEventDao.insertEvent(scheduleEvent1)
        assertEquals(scheduleEvent1.title, scheduleEventDao.getScheduleEvent(1).first().title)
    }


    /**
     * Test getScheduleEvent
     * create 2 events and check 2nd event
     */
    @Test
    @Throws(IOException::class)
    fun getScheduleEventTest2(): Unit = runBlocking {
        val scheduleEvent1 = ScheduleEvent(1, "event1", "", "", "", "", "", "", "")
        val scheduleEvent2 = ScheduleEvent(2, "event2", "", "", "", "", "10:20 AM", "", "")
        scheduleEventDao.insertEvent(scheduleEvent1)
        scheduleEventDao.insertEvent(scheduleEvent2)
        assertEquals(
            scheduleEvent2.time_from,
            scheduleEventDao.getScheduleEvent(2).first().time_from
        )
    }

    /**
     * getDailyEventByTimeAndDate test
     * create 3 events and insert them into database,check only 2 of them were query
     */
    @Test
    @Throws(IOException::class)
    fun getDailyEventByTimeAndDateTest(): Unit = runBlocking {
        val scheduleEvent1 =
            ScheduleEvent(1, "event1", "", "", "2022-06-06", "10:15:31", "", "", "")
        val scheduleEvent2 =
            ScheduleEvent(2, "event2", "", "", "2022-06-06", "14:15:14", "15:15:14", "", "")
        val scheduleEvent3 =
            ScheduleEvent(3, "event3", "", "", "2022-06-06", "15:15:14", "16:15:14", "", "")
        scheduleEventDao.insertEvent(scheduleEvent2)
        scheduleEventDao.insertEvent(scheduleEvent1)
        scheduleEventDao.insertEvent(scheduleEvent3)
        val currentTime = "11:15:15"
        val currentDate = "2022-06-06"
        assertEquals(
            scheduleEvent1.id,
            scheduleEventDao.getEventByDate(currentDate).first()[0].id
        )
        assertEquals(
            scheduleEvent2.id,
            scheduleEventDao.getDailyEventByTimeAndDate(currentTime, currentDate).first()[0].id
        )
        assertEquals(
            scheduleEvent3.id,
            scheduleEventDao.getDailyEventByTimeAndDate(currentTime, currentDate).first()[1].id
        )

    }


    /**
     * Testing searchDatesByEventName from Dao Object
     */
    @Test
    @Throws(IOException::class)
    fun searchDatesByEventNameTest(): Unit = runBlocking {
        val scheduleEvent1 =
            ScheduleEvent(
                1,
                "event1",
                "Saskatoon",
                "",
                "2022-05-05",
                "10:15",
                "15:15",
                "https://nomosnow.ca",
                "Just a Notes"
            )
        scheduleEventDao.insertEvent(scheduleEvent1)
        assertEquals(
            scheduleEvent1.date,
            scheduleEventDao.searchDatesByEventName("event1").first()[0]
        )
    }


    /**
     * testing getEventFromDateAndName from ScheduleEventDao Object
     */
    @Test
    @Throws(IOException::class)
    fun getEventFromDatesAndNameTest(): Unit = runBlocking {
        val scheduleEvent1 =
            ScheduleEvent(
                1,
                "event1",
                "Saskatoon",
                "",
                "2022-05-05",
                "10:15",
                "15:15",
                "https://nomosnow.ca",
                "Just a Notes"
            )
        scheduleEventDao.insertEvent(scheduleEvent1)
        assertEquals(
            scheduleEvent1.time_to,
            scheduleEventDao.getEventFromDateAndName("2022-05-05", "event1").first()[0].time_to
        )
    }


    /**
     * function test getFutureDates from ScheduleEventDao Object
     */
    @Test
    @Throws(IOException::class)
    fun getFutureDatesTest(): Unit = runBlocking {
        val scheduleEvent1 =
            ScheduleEvent(
                1,
                "event1",
                "Saskatoon",
                "",
                "2022-05-05",
                "10:15",
                "15:15",
                "https://nomosnow.ca",
                "Just a Notes"
            )

        val currentDate = "2022-05-04"
        scheduleEventDao.insertEvent(scheduleEvent1)
        assertEquals(
            scheduleEvent1.date,
            scheduleEventDao.getFutureDates(currentDate).first()[0]
        )
    }


    /**
     * function test getPastDates from ScheduleEventDao
     */
    @Test
    @Throws(IOException::class)
    fun getPastDatesTest(): Unit = runBlocking {
        val scheduleEvent1 =
            ScheduleEvent(
                1,
                "event1",
                "Saskatoon",
                "",
                "2022-05-03",
                "10:15",
                "15:15",
                "https://nomosnow.ca",
                "Just a Notes"
            )

        val currentDate = "2022-05-04"
        scheduleEventDao.insertEvent(scheduleEvent1)
        assertEquals(
            scheduleEvent1.date,
            scheduleEventDao.getPastDates(currentDate).first()[0]
        )
    }


    /**
     * function test getTodayAndFutureEvent from ScheduleEventDao
     */
    @Test
    @Throws(IOException::class)
    fun getTodayAndFutureEventTest(): Unit = runBlocking {
        val scheduleEvent1 =
            ScheduleEvent(
                1,
                "event1",
                "Saskatoon",
                "",
                "2022-05-04",
                "10:15",
                "15:15",
                "https://nomosnow.ca",
                "Just a Notes"
            )
        val scheduleEvent2 =
            ScheduleEvent(
                2,
                "event2",
                "Saskatoon",
                "",
                "2022-05-03",
                "16:15",
                "18:15",
                "https://nomosnow.ca",
                "Just a Notes"
            )

        val currentDate = "2022-05-03"
        scheduleEventDao.insertEvent(scheduleEvent1)
        scheduleEventDao.insertEvent(scheduleEvent2)
        assertEquals(
            scheduleEvent2.date,
            scheduleEventDao.getTodayAndFutureEvent(currentDate).first()[0].date
        )
        assertEquals(
            scheduleEvent1.date,
            scheduleEventDao.getTodayAndFutureEvent(currentDate).first()[1].date
        )
    }


}