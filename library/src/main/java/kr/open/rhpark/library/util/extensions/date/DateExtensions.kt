package kr.open.rhpark.library.util.extensions.date

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

/**
 * ex)
 * format = "HH:mm:ss dd/MM/yyyy"
 * locale = Locale.US
 */
public fun Long.timeDateToString(format: String, locale: Locale = Locale.US): String =
    SimpleDateFormat(format, locale).format(Date(this))

/**
 * ex)
 * format = "HH:mm:ss dd/MM/yyyy"
 * locale = Locale.US
 * return milliseconds
 */
public fun String.timeDateToLong(format: String, locale: Locale = Locale.US): Long =
    SimpleDateFormat(format, locale).parse(this)?.time
        ?: throw IllegalArgumentException("Invalid time string")

public fun String.timeDateToDate(format: String, locale: Locale = Locale.US): Date? {
    val dateFormat = SimpleDateFormat(format, locale)
    return dateFormat.parse(this)
}

/**
 * ex)
 * format = "HH:mm:ss dd/MM/yyyy"
 * locale = Locale.US
 */
public fun Date.formattedToString(format: String, locale: Locale = Locale.US):String =
    SimpleDateFormat(format, locale).format(this)

/**
 * @return n Seconds (Long)
 */
public fun Date.timeDifferenceInSeconds(other: Date): Long =
    Math.abs(this.time - other.time) / 1000

/**
 * @return n Minutes (Long)
 */
public fun Date.timeDifferenceInMinutes(other: Date): Long =
    Math.abs(this.time - other.time) / (1000 * 60)

/**
 * @return n Hours (Long)
 */
public fun Date.timeDifferenceInHours(other: Date): Long =
    Math.abs(this.time - other.time) / (1000 * 3600)

/**
 * @return n Hours (Long)
 */
public fun LocalDateTime.hoursBetween(other: LocalDateTime): Long = ChronoUnit.HOURS.between(this, other)

/**
 * @return n Minutes (Long)
 */
public fun LocalDateTime.minutesBetween(other: LocalDateTime): Long = ChronoUnit.MINUTES.between(this, other)

/**
 * @return n DAYS (Long)
 */
public fun LocalDate.daysDifference(other: LocalDate): Long = ChronoUnit.DAYS.between(this, other)

/**
 * @return n MONTHS (Long)
 */
public fun LocalDate.monthsDifference(other: LocalDate): Long = ChronoUnit.MONTHS.between(this, other)

/**
 * @return n YEARS (Long)
 */
public fun LocalDate.yearsDifference(other: LocalDate): Long = ChronoUnit.YEARS.between(this, other)

public fun Long.secondsToMinutes(): Long = this / 60
public fun Long.secondsToHours(): Long = this / 3600
public fun Long.secondsToDays(): Long = this / 86400

public fun Long.millisecondsToSeconds(): Long = this / 1000
public fun Long.millisecondsToMinutes(): Long = this / (1000 * 60)
public fun Long.millisecondsToHours(): Long = this / (1000 * 3600)
public fun Long.millisecondsToDays(): Long = this / (1000 * 86400)

public fun Int.secondsToMinutes(): Int = this / 60
public fun Int.secondsToHours(): Int = this / 3600
public fun Int.secondsToDays(): Int = this / 86400