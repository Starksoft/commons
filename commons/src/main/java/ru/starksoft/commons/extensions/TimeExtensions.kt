package ru.starksoft.commons.extensions

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

const val BASE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"

const val dateToday = "today"
const val dateYesterday = "yesterday"

fun String?.toTime(): Long {
    if (this.isNullOrEmpty()) {
        return 0
    }
    return try {
        val formatter = SimpleDateFormat(BASE_TIME_FORMAT, Locale.getDefault())

        formatter.timeZone = TimeZone.getTimeZone("GMT");
        formatter.parse(this).time

    } catch (e: Exception) {
        0
    }
}

/**
 * FULL_DATE_FORMAT (30 августа 2013, ...)
 */
fun Long?.toFullDate(): String {
    return try {
        SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(this!!)

    } catch (e: Exception) {
        ""
    }
}

/**
 * TAK_DATE_FORMAT (сегодня, 14:20; вчера, 14:20; 20 января; 23 марта 2015; ...)
 */
fun Long?.toDateWithTime(resourcesProvider: Context): String {
    val timeStamp = this

    return try {
        if (timeStamp!! == 0L) {
            return ""
        }

        val locale = Locale.getDefault()

        val calendarFromTimestamp = Calendar.getInstance()
        val calendarNow = Calendar.getInstance()

        calendarFromTimestamp.timeInMillis = timeStamp

        when {
            isToday(calendarFromTimestamp, calendarNow) -> {
                val formatter = SimpleDateFormat(dateToday + ", " + "HH:mm", locale)
                return formatter.format(timeStamp)
            }
            isYesterday(calendarFromTimestamp, calendarNow) -> {
                val formatter = SimpleDateFormat(
                    dateYesterday + ", " + "HH:mm",
                    locale
                )
                return formatter.format(timeStamp)
            }
            else -> {
                val formatter =
                    if (calendarNow.get(Calendar.YEAR) - calendarFromTimestamp.get(Calendar.YEAR) > 0) {
                        SimpleDateFormat("d MMMM yyyy", locale)
                    } else {
                        SimpleDateFormat("d MMMM", locale)
                    }
                return formatter.format(timeStamp)
            }
        }
    } catch (e: Exception) {
        ""
    }
}


/**
 * TAK_DATE_FORMAT (сегодня; вчера; 20 января 2019; 23 марта 2015; ...)
 */
fun Long?.toDate(resourcesProvider: Context): String {
    val timeStamp = this

    return try {
        if (timeStamp!! == 0L) {
            return ""
        }

        val locale = Locale.getDefault()
        val calendarFromTimestamp = Calendar.getInstance()
        val calendarNow = Calendar.getInstance()

        calendarFromTimestamp.timeInMillis = timeStamp

        return when {
            isToday(calendarFromTimestamp, calendarNow) -> {
                SimpleDateFormat(dateToday, locale).format(timeStamp)
            }
            isYesterday(calendarFromTimestamp, calendarNow) -> {
                SimpleDateFormat(
                    dateYesterday,
                    locale
                ).format(timeStamp)
            }
            else -> {
                SimpleDateFormat("d MMMM yyyy", locale).format(timeStamp)
            }
        }
    } catch (e: Exception) {
        ""
    }
}

private fun isToday(calendarFromTimestamp: Calendar, calendarNow: Calendar): Boolean {
    return calendarNow.get(Calendar.DATE) == calendarFromTimestamp.get(Calendar.DATE) &&
            calendarNow.get(Calendar.MONTH) == calendarFromTimestamp.get(Calendar.MONTH) &&
            calendarNow.get(Calendar.YEAR) == calendarFromTimestamp.get(Calendar.YEAR)
}

private fun isYesterday(calendarFromTimestamp: Calendar, calendarNow: Calendar): Boolean {
    return calendarNow.get(Calendar.DATE) - calendarFromTimestamp.get(Calendar.DATE) == 1 &&
            calendarNow.get(Calendar.MONTH) == calendarFromTimestamp.get(Calendar.MONTH) &&
            calendarNow.get(Calendar.YEAR) == calendarFromTimestamp.get(Calendar.YEAR)
}
