package ru.starksoft.commons;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionUtils {
	/**
	 * Tell if an array contains specified value
	 *
	 * @param value The value
	 * @param array The array
	 * @return True if the specified array contains the value, false otherwise
	 */
	public static <T> boolean contains(T value, T[] array) {
		return indexOf(value, array) != -1;
	}

	/**
	 * Searches an array for the specified object and returns the index of the first occurrence.
	 *
	 * @param value The value
	 * @param array The array
	 * @return Index of value in array
	 */
	public static <T> int indexOf(T value, T[] array) {
		int i = array.length;
		for (; i-- > 0; ) {
			if (array[i].equals(value)) {
				break;
			}
		}
		return i;
	}

	@Nullable
	public static <T> T getLast(List<T> list) {
		return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
	}

	public static <T> void cropBeginning(Collection<T> collection, int maxCount) {
		Iterator<T> iterator = collection.iterator();
		while (collection.size() > maxCount) {
			iterator.next();
			iterator.remove();
		}
	}

	@Nullable
	public static <T> T getItem(List<T> list, int index) {
		return list != null && index >= 0 && index < list.size() ? list.get(index) : null;
	}

	public static <T> T[] concatenate(T[] a, T[] b) {
		int aLen = a.length;
		int bLen = b.length;

		@SuppressWarnings("unchecked") T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}
}
