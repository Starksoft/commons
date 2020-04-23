package ru.starksoft.commons.numeric;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericUtilsTest {

	@Test
	public void parseInt() {
		int result = NumericUtils.parseInt("");
		assertEquals(0, result);

		result = NumericUtils.parseInt("0");
		assertEquals(0, result);

		result = NumericUtils.parseInt("null");
		assertEquals(0, result);

		result = NumericUtils.parseInt(null);
		assertEquals(0, result);

		result = NumericUtils.parseInt(" 1 ");
		assertEquals(1, result);

		result = NumericUtils.parseInt(" -1 ");
		assertEquals(-1, result);

		result = NumericUtils.parseInt("1 -1 ");
		assertEquals(0, result);
	}
}
