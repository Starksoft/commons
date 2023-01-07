package ru.starksoft.commons.converters

import java.util.*
import java.util.concurrent.TimeUnit

object TimeIntervalUtils {

    @JvmStatic
    fun format(time: Long): String {
        return if (time < 1000) {
            "$time ms"
        } else if (time < TimeUnit.MINUTES.toMillis(1)) {
            TimeUnit.MILLISECONDS.toSeconds(time).toString() + " s"
        } else if (time < TimeUnit.HOURS.toMillis(1)) {
            TimeUnit.MILLISECONDS.toMinutes(time).toString() + " m"
        } else if (time < TimeUnit.DAYS.toMillis(1)) {
            TimeUnit.MILLISECONDS.toHours(time).toString() + " h"
        } else {
            TimeUnit.MILLISECONDS.toDays(time).toString() + " d"
        }
    }

    @JvmStatic
    fun formatInterval(time: Long): String {
        val ms = time % 1000
        val sec = (time - ms) % 60000 / 1000
        val hr = TimeUnit.MILLISECONDS.toHours(time)
        val min = TimeUnit.MILLISECONDS.toMinutes(time - TimeUnit.HOURS.toMillis(hr))
        return String.format(Locale.US, "%01d:%02d:%02d.%03d", hr, min, sec, ms)
    }
}
