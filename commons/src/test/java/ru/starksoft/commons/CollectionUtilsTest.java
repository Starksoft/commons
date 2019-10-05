package ru.starksoft.commons;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CollectionUtilsTest {

	private final ArrayList<String> arrayList = new ArrayList<>();

	{
		arrayList.add("1");
		arrayList.add("2");
		arrayList.add("3");
		arrayList.add("4");
		arrayList.add("5");
	}

	@Test
	public void contains() throws Exception {
		boolean ret = CollectionUtils.contains("4", arrayList.toArray());
		assertTrue(ret);

		ret = CollectionUtils.contains("0", arrayList.toArray());
		assertFalse(ret);
	}

	@Test
	public void indexOf() throws Exception {
		int ret = CollectionUtils.indexOf("4", arrayList.toArray());
		assertEquals(3, ret);

		ret = CollectionUtils.indexOf("0", arrayList.toArray());
		assertEquals(-1, ret);
	}

	@Test
	public void getLast() throws Exception {
		String ret = CollectionUtils.getLast(arrayList);
		assertEquals("5", ret);

		ret = CollectionUtils.getLast(null);
		assertNull(ret);
	}

	@Test
	public void cropBeginning() throws Exception {
		ArrayList<String> list = new ArrayList<>(arrayList);
		CollectionUtils.cropBeginning(list, 2);

		assertEquals(2, list.size());
		assertEquals("4", list.get(0));
		assertEquals("5", list.get(1));
	}

	@Test
	public void getItem() throws Exception {
		String ret = CollectionUtils.getItem(arrayList, 0);
		assertEquals("1", ret);

		ret = CollectionUtils.getItem(arrayList, 4);
		assertEquals("5", ret);

		ret = CollectionUtils.getItem(arrayList, -1);
		assertNull(ret);

		ret = CollectionUtils.getItem(arrayList, Integer.MAX_VALUE);
		assertNull(ret);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isEmpty() {
		boolean ret = CollectionUtils.isEmpty(arrayList);
		assertFalse(ret);

		ret = CollectionUtils.isEmpty(Collections.EMPTY_LIST);
		assertTrue(ret);

		ret = CollectionUtils.isEmpty(null);
		assertTrue(ret);
	}
}
