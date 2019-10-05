package ru.starksoft.commons;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.collection.SimpleArrayMap;

public final class CacheIds {

	private final SimpleArrayMap<String, Integer> map = new SimpleArrayMap<>();

	private CacheIds() {
		throw new UnsupportedOperationException();
	}

	public final int getValue(@NonNull String key) {
		Integer value;

		if (map.containsKey(key)) {
			value = map.get(key);
		} else {
			value = key.hashCode();
			map.put(key, value);
		}

		return value == null ? 0 : value;
	}

	@VisibleForTesting
	int getCacheSize() {
		return map.size();
	}
}
