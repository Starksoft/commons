package ru.starksoft.commons.converters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrafficUtilsTest {

	@Test
	public void convert() {
		String convert = TrafficUtils.format(1024);
		assertEquals("1 kb", convert);

		convert = TrafficUtils.format(10);
		assertEquals("1 kb", convert);

		convert = TrafficUtils.format(1024 * 3);
		assertEquals("3 kb", convert);

		convert = TrafficUtils.format(1048576);
		assertEquals("1 mb", convert);

		convert = TrafficUtils.format(2097152);
		assertEquals("2 mb", convert);

		convert = TrafficUtils.format(1073741824L);
		assertEquals("1 gb", convert);

		convert = TrafficUtils.format(2147483648L);
		assertEquals("2 gb", convert);

		convert = TrafficUtils.format(1073741824L * 5);
		assertEquals("5 gb", convert);
	}
}
