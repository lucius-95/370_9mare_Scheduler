package ca.nomosnow.cmpt370_9mare

import android.app.Application
import ca.nomosnow.cmpt370_9mare.data.schedule_event.EventRoomDatabase


/**
 * application create database
 */
class ScheduleApplication : Application() {
    val database: EventRoomDatabase by lazy { EventRoomDatabase.getDatabase(this) }
}