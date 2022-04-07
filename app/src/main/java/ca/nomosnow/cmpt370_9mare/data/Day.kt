package ca.nomosnow.cmpt370_9mare.data

import androidx.annotation.Nullable
import java.time.LocalDate

/**
 * Day data Object
 */
data class Day(
    @Nullable
    val day: String?,
    val date: LocalDate?
)