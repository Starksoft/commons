package ru.starksoft.commons;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class JoinStringBuilderTest {

    private final JoinStringBuilder joinStringBuilder = new JoinStringBuilder();

    @Test
    public void allAraValid() throws Exception {
        joinStringBuilder.add("one", ",");
        joinStringBuilder.add("two", "\n");

        String result = joinStringBuilder.build();

        assertEquals("one,two", result);
    }

    @Test
    public void lastIsInvalid() throws Exception {
        joinStringBuilder.add("one", ",");
        joinStringBuilder.add("two", "\n");
        joinStringBuilder.add("null", "|");

        String result = joinStringBuilder.build();

        assertEquals("one,two", result);
    }

    @Test
    public void lastTwoAreInvalid() throws Exception {
        joinStringBuilder.add("one", ",");
        joinStringBuilder.add("two", "\n");
        joinStringBuilder.add("null", "|");
        joinStringBuilder.add(null, "!");

        String result = joinStringBuilder.build();

        assertEquals("one,two", result);
    }

    @Test
    public void allAreInvalid() throws Exception {
        joinStringBuilder.add("null", "|");
        joinStringBuilder.add(null, "!");
        joinStringBuilder.add("", "$");

        String result = joinStringBuilder.build();

        assertEquals("", result);
    }

    @Test
    public void invalidInTheMiddle() throws Exception {
        joinStringBuilder.add("one", ",");
        joinStringBuilder.add("null", "|");
        joinStringBuilder.add("two", "\n");

        String result = joinStringBuilder.build();

        assertEquals("one,two", result);
    }

    @Test
    public void validInTheMiddle() throws Exception {
        joinStringBuilder.add("null", "|");
        joinStringBuilder.add("one", ",");
        joinStringBuilder.add(null, "!");

        String result = joinStringBuilder.build();

        assertEquals("one", result);
    }

    @Test
    public void validInvalid() throws Exception {
        joinStringBuilder.add("one", ",");
        joinStringBuilder.add("null", "|");
        joinStringBuilder.add("two", "\n");
        joinStringBuilder.add(null, "!");

        String result = joinStringBuilder.build();

        assertEquals("one,two", result);
    }

    @Test
    public void oneItem() throws Exception {
        joinStringBuilder.add("one", ",");
        String result = joinStringBuilder.build();

        assertEquals("one", result);
    }

    @Test
    public void twoEmptyOneValid() throws Exception {
        joinStringBuilder.add("", ",");
        joinStringBuilder.add("", ",");
        joinStringBuilder.add("123", ",");

        String result = joinStringBuilder.build();

        assertEquals("123", result);
    }

    @Test
    public void twoEmptyOneValidAndOneEmpty() throws Exception {
        joinStringBuilder.add("", ",");
        joinStringBuilder.add("", ",");
        joinStringBuilder.add("123", ",");
        joinStringBuilder.add("", ",");

        String result = joinStringBuilder.build();

        assertEquals("123", result);
    }
}