package ru.starksoft.commons.converters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeIntervalUtilsTest {

	@Test
	public void format() {
		String result = TimeIntervalUtils.format(500);
		assertEquals("500 ms", result);

		result = TimeIntervalUtils.format(1700);
		assertEquals("1 s", result);

		result = TimeIntervalUtils.format(2000);
		assertEquals("2 s", result);

		result = TimeIntervalUtils.format(59_000);
		assertEquals("59 s", result);

		result = TimeIntervalUtils.format(60_000);
		assertEquals("1 m", result);

		result = TimeIntervalUtils.format(60_000 * 5);
		assertEquals("5 m", result);

		result = TimeIntervalUtils.format(60_000 * 59);
		assertEquals("59 m", result);

		result = TimeIntervalUtils.format(60_000 * 60);
		assertEquals("1 h", result);

		result = TimeIntervalUtils.format(60_000 * 60 * 5);
		assertEquals("5 h", result);

		result = TimeIntervalUtils.format(60_000 * 60 * 23);
		assertEquals("23 h", result);

		result = TimeIntervalUtils.format(60_000 * 60 * 24);
		assertEquals("1 d", result);

		result = TimeIntervalUtils.format(60_000 * 60 * 24 * 5);
		assertEquals("5 d", result);
	}

	@Test
	public void formatInterval() {
		String result = TimeIntervalUtils.formatInterval(500);
		assertEquals("0:00:00.500", result);

		result = TimeIntervalUtils.formatInterval(1500);
		assertEquals("0:00:01.500", result);

		result = TimeIntervalUtils.formatInterval(11500);
		assertEquals("0:00:11.500", result);

		result = TimeIntervalUtils.formatInterval(60 * 1000);
		assertEquals("0:01:00.000", result);

		result = TimeIntervalUtils.formatInterval(60 * 1000 * 5);
		assertEquals("0:05:00.000", result);

		result = TimeIntervalUtils.formatInterval(60 * 1000 * 59);
		assertEquals("0:59:00.000", result);

		result = TimeIntervalUtils.formatInterval(60 * 60 * 1000 * 2);
		assertEquals("2:00:00.000", result);

	}
}
