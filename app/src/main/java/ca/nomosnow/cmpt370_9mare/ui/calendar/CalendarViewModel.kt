package ca.nomosnow.cmpt370_9mare.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import ca.nomosnow.cmpt370_9mare.data.Day
import ca.nomosnow.cmpt370_9mare.data.schedule_event.ScheduleEventDao
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


/**
 * CalendarViewModel class is a shared Calendar Model across app
 */
@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel(private val scheduleEventDao: ScheduleEventDao) : ViewModel() {
    private val _monthYearText = MutableLiveData<String>()
    private val _daysOfTheMonth = MutableLiveData<ArrayList<Day>?>()
    private val _selectDate = MutableLiveData(LocalDate.now())

    val selectDate: LiveData<LocalDate> = _selectDate
    val monthYearText: LiveData<String> = _monthYearText
    val daysOfTheMonth: MutableLiveData<ArrayList<Day>?> = _daysOfTheMonth

    private var currentMonth = selectDate.value.toString().substring(0..6)
    var datesWithEventInMonth = datesFromMonths(currentMonth)

    /**
     * init data value
     */
    init {
        setMonthYearText()
        _daysOfTheMonth.value = selectDate.value?.let { daysInMonthArray(it) }
    }


    /**
     * setMonthYearText for month year title of calendar
     */
    private fun setMonthYearText() {
        _monthYearText.value = monthYearFromDate(selectDate.value).toString()
    }


    /**
     * monthYearFromDate function take LocalDate and return a format String
     * [date] : LocalDate?
     * return : String?
     */
    private fun monthYearFromDate(date: LocalDate?): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date?.format(formatter)
    }

    /**
     * daysInMonthArray take LocalDate and return List of Day in that month
     * [date]: LocalDate
     * return ArrayList<Day> type
     */
    private fun daysInMonthArray(date: LocalDate): ArrayList<Day> {

        // maximum 31 days in month
        val daysInMonthArray = arrayListOf<Day>()

        // current year month from chosen date
        val yearMonth: YearMonth = YearMonth.from(date)

        //number of days in a month
        val daysInMonth: Int = yearMonth.lengthOfMonth()

        // first date of the month

        val firstOfMonth: LocalDate = date.withDayOfMonth(1)
        // number days of week

        val dayOfWeek: Int = firstOfMonth.dayOfWeek.value
        var y = 0
        for (x: Int in 1..42) {
            when {
                x <= dayOfWeek || x > daysInMonth + dayOfWeek -> daysInMonthArray.add(

                    Day(null, null)

                )
                else -> {
                    daysInMonthArray.add(
                        Day(
                            (x - dayOfWeek).toString(),
                            firstOfMonth.plusDays(y.toLong())
                        )
                    )
                    y++
                }
            }

        }

        if (!clearDaysIfAllNull(daysInMonthArray.subList(0, 7))) {
            clearDaysIfAllNull(daysInMonthArray.subList(35, 42))
        }

        return daysInMonthArray
    }

    /**
     * clearDaysIfAllNull function clear all null day of calendar
     */
    private fun clearDaysIfAllNull(dayList: MutableList<Day>): Boolean {
        return if (dayList.none { day -> day != Day(null, null) }) {
            dayList.clear()
            true
        } else false
    }

    /**
     * nextMonthAction set new value of new month for calendar when nextMonthAction action is trigger
     */
    fun nextMonthAction() {
        _selectDate.value = selectDate.value?.plusMonths(1)

        _daysOfTheMonth.value = selectDate.value?.let { daysInMonthArray(it) }
        setMonthYearText()
    }

    /**
     * previousMonthAction set new value of new month for calendar when previousMonth action is trigger
     */
    fun previousMonthAction() {
        _selectDate.value = selectDate.value?.minusMonths(1)
        _daysOfTheMonth.value = selectDate.value?.let { daysInMonthArray(it) }
        setMonthYearText()
    }

    /**
     * setter selectDate
     */
    fun setSelectDate(date: LocalDate?) {
        _selectDate.value = date
    }

    /**
     * datesFromMonths function take month as argument to return
     */
    fun datesFromMonths(month: String): LiveData<List<String>> =
        scheduleEventDao.getDatesByMonth("$month%%").asLiveData()
}

class CalendarViewModelFactory(private val scheduleEventDao: ScheduleEventDao) :
    ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(scheduleEventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

