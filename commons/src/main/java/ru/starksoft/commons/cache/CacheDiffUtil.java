package ru.starksoft.commons.cache;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public final class CacheDiffUtil {

	private static final String TAG = "CacheDiffUtil";

	private CacheDiffUtil() {
		throw new UnsupportedOperationException();
	}

	public static <T extends CacheEntry> DiffResult<T> calculateDiff(@NonNull List<T> oldList, @NonNull List<T> newList) {
		long startTime = System.currentTimeMillis();

		Log.d(TAG, "calculateDiff: started");

		Map<String, T> fastFindMap = new HashMap<>();
		List<T> toAdd = new ArrayList<>();
		List<T> toUpdate = new ArrayList<>();
		List<String> toRemove = new ArrayList<>();
		List<String> exists = new ArrayList<>();

		for (T t : oldList) {
			String entryId = t.getEntryId();
			fastFindMap.put(entryId, t);
			toRemove.add(entryId);
		}

		for (CacheEntry originEntry : newList) {
			String entryId = originEntry.getEntryId();

			// Удаление
			exists.add(entryId);

			// Добавим, если нет
			if (!toRemove.contains(entryId)) {
				Log.d(TAG, "calculateDiff: adding");
				//noinspection unchecked
				toAdd.add((T) originEntry);

			} else {
				// Изменение
				T item = fastFindMap.get(entryId);

				int itemContentHashCode = item != null ? item.getContentHashCode() : 0;
				int originEntryContentHashCode = originEntry.getContentHashCode();

				if (itemContentHashCode != originEntryContentHashCode) {
					Log.d(TAG, "calculateDiff: changing");
					//noinspection unchecked
					toUpdate.add((T) originEntry);
				}
			}
		}

		// Удаляем все существующие значения
		toRemove.removeAll(exists);

		Log.d(TAG, "calculateDiff: toAdd=" + toAdd.size());
		Log.d(TAG, "calculateDiff: toUpdate=" + toUpdate.size());
		Log.d(TAG, "calculateDiff: toRemove=" + toRemove.size());

		Log.d(TAG, "calculateDiff: done in " + (System.currentTimeMillis() - startTime) + " ms");

		return new DiffResult<>(toAdd, toUpdate, toRemove);
	}

	private static <T extends CacheEntry> Map<String, T> createFindMap(@NonNull List<T> oldList) {
		Map<String, T> map = new HashMap<>();

		for (T t : oldList) {
			map.put(t.getEntryId(), t);
		}
		return map;
	}

	@NonNull
	private static <T extends CacheEntry> List<String> getAllIds(@NonNull List<T> oldList) {
		List<String> result = new ArrayList<>();

		for (T cacheEntry : oldList) {
			result.add(cacheEntry.getEntryId());
		}

		return result;
	}
}
