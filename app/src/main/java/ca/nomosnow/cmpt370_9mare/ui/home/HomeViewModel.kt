package ca.nomosnow.cmpt370_9mare.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel : ViewModel() {
    // Initialize variables for current day timings
    private var _currentDay = MutableLiveData<String>()
    private var today: LocalDate = LocalDate.now()
    init {
        _currentDay.value = stringDisplayToday(today)
    }

    /**
     * Formatting and Get functionalities for date time / toString() conversions
     */
    private fun stringFormatToday(today: LocalDate): String {
        return today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    private fun stringDisplayToday(today: LocalDate): String {
        return today.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
    }

    // Get current date formatted correctly
    fun getToday(): String {
        return stringFormatToday(today)
    }
}
