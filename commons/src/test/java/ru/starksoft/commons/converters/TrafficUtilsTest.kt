package ru.starksoft.commons.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.starksoft.commons.converters.TrafficUtils.format

class TrafficUtilsTest {

    @Test
    fun convert() {
        var convert = format(1024)
        assertEquals("1 kb", convert)

        convert = format(10)
        assertEquals("1 kb", convert)

        convert = format((1024 * 3).toLong())
        assertEquals("3 kb", convert)

        convert = format(1048576)
        assertEquals("1 mb", convert)

        convert = format(2097152)
        assertEquals("2 mb", convert)

        convert = format(1073741824L)
        assertEquals("1 gb", convert)

        convert = format(2147483648L)
        assertEquals("2 gb", convert)

        convert = format(1073741824L * 5)
        assertEquals("5 gb", convert)
    }
}
