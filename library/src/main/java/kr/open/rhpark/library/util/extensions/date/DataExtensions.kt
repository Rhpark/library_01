package kr.open.rhpark.library.util.extensions.date

import java.text.SimpleDateFormat
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
 */
public fun String.timeDateToLong(format: String, locale: Locale = Locale.US): Long =
    SimpleDateFormat(format, locale).parse(this)?.time
        ?: throw IllegalArgumentException("Invalid time string")

/**
 * ex)
 * format = "HH:mm:ss dd/MM/yyyy"
 * locale = Locale.US
 */
public fun Date.formattedToString(format: String, locale: Locale = Locale.US):String =
    SimpleDateFormat(format, locale).format(this)