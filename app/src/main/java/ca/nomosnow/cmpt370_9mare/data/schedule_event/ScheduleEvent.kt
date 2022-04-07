package ca.nomosnow.cmpt370_9mare.data.schedule_event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


/**
 * Entity data class
 */
@Entity(tableName = "event")
data class ScheduleEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "location")
    var location: String,
    @ColumnInfo(name = "group")
    var group: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "time_from")
    var time_from: String,
    @ColumnInfo(name = "time_to")
    var time_to: String,
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "notes")
    var notes: String
)

/**
 * Get today's date in a customized format
 */
fun getCurrentDate(): String {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH) + 1
    val day = c.get(Calendar.DAY_OF_MONTH)

    return String.format("$year-%02d-%02d", month, day)
}