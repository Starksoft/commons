package ru.starksoft.commons.text;

import org.junit.Test;

import ru.starksoft.commons.text.JoinStringBuilder;

import static junit.framework.Assert.assertEquals;

public class JoinStringBuilderTest {

	@Test
	public void allAraValid() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();

		joinStringBuilder.add("one", ",");
		joinStringBuilder.add("two", "\n");

		String result = joinStringBuilder.build();

		assertEquals("one,two", result);
	}

	@Test
	public void lastIsInvalid() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("one", ",");
		joinStringBuilder.add("two", "\n");
		joinStringBuilder.add("null", "|");

		String result = joinStringBuilder.build();

		assertEquals("one,two", result);
	}

	@Test
	public void lastTwoAreInvalid() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("one", ",");
		joinStringBuilder.add("two", "\n");
		joinStringBuilder.add("null", "|");
		joinStringBuilder.add(null, "!");

		String result = joinStringBuilder.build();

		assertEquals("one,two", result);
	}

	@Test
	public void allAreInvalid() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("null", "|");
		joinStringBuilder.add(null, "!");
		joinStringBuilder.add("", "$");

		String result = joinStringBuilder.build();

		assertEquals("", result);

		joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add(null, "|");
		joinStringBuilder.add(null, "!");
		joinStringBuilder.add(null, "$");

		result = joinStringBuilder.build();

		assertEquals("", result);

	}

	@Test
	public void invalidInTheMiddle() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("one", ",");
		joinStringBuilder.add("null", "|");
		joinStringBuilder.add("two", "\n");

		String result = joinStringBuilder.build();

		assertEquals("one,two", result);
	}

	@Test
	public void validInTheMiddle() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("null", "|");
		joinStringBuilder.add("one", ",");
		joinStringBuilder.add(null, "!");

		String result = joinStringBuilder.build();

		assertEquals("one", result);
	}

	@Test
	public void validInvalid() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("one", ",");
		joinStringBuilder.add("null", "|");
		joinStringBuilder.add("two", "\n");
		joinStringBuilder.add(null, "!");

		String result = joinStringBuilder.build();

		assertEquals("one,two", result);
	}

	@Test
	public void oneItem() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("one", ",");
		String result = joinStringBuilder.build();

		assertEquals("one", result);
	}

	@Test
	public void twoEmptyOneValid() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("", ",");
		joinStringBuilder.add("", ",");
		joinStringBuilder.add("123", ",");

		String result = joinStringBuilder.build();

		assertEquals("123", result);
	}

	@Test
	public void twoEmptyOneValidAndOneEmpty() {
		JoinStringBuilder joinStringBuilder = new JoinStringBuilder();
		joinStringBuilder.add("", ",");
		joinStringBuilder.add("", ",");
		joinStringBuilder.add("123", ",");
		joinStringBuilder.add("", ",");

		String result = joinStringBuilder.build();

		assertEquals("123", result);
	}
}
