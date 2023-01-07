package ru.starksoft.commons.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.starksoft.commons.converters.TimeIntervalUtils.format
import ru.starksoft.commons.converters.TimeIntervalUtils.formatInterval

class TimeIntervalUtilsTest {

    @Test
    fun format() {
        var result = format(500)
        assertEquals("500 ms", result)

        result = format(1700)
        assertEquals("1 s", result)

        result = format(2000)
        assertEquals("2 s", result)

        result = format(59000)
        assertEquals("59 s", result)

        result = format(60000)
        assertEquals("1 m", result)

        result = format((60000 * 5).toLong())
        assertEquals("5 m", result)

        result = format((60000 * 59).toLong())
        assertEquals("59 m", result)

        result = format((60000 * 60).toLong())
        assertEquals("1 h", result)

        result = format((60000 * 60 * 5).toLong())
        assertEquals("5 h", result)

        result = format((60000 * 60 * 23).toLong())
        assertEquals("23 h", result)

        result = format((60000 * 60 * 24).toLong())
        assertEquals("1 d", result)

        result = format((60000 * 60 * 24 * 5).toLong())
        assertEquals("5 d", result)
    }

    @Test
    fun formatInterval() {
        var result = formatInterval(500)
        assertEquals("0:00:00.500", result)

        result = formatInterval(1500)
        assertEquals("0:00:01.500", result)

        result = formatInterval(11500)
        assertEquals("0:00:11.500", result)

        result = formatInterval((60 * 1000).toLong())
        assertEquals("0:01:00.000", result)

        result = formatInterval((60 * 1000 * 5).toLong())
        assertEquals("0:05:00.000", result)

        result = formatInterval((60 * 1000 * 59).toLong())
        assertEquals("0:59:00.000", result)

        result = formatInterval((60 * 60 * 1000 * 2).toLong())
        assertEquals("2:00:00.000", result)
    }
}
