package ru.starksoft.commons.converters;

import androidx.annotation.NonNull;

public final class TrafficUtils {

	private TrafficUtils() {
		throw new UnsupportedOperationException();
	}

	@NonNull
	public static String format(long bytes) {

		if (bytes <= 1024) {
			return "1 kb";

		} else if (bytes < 1024 * 1024) {
			int i1 = (int) (bytes / 1024);
			return i1 + " kb";

		} else if (bytes < 1024 * 1024 * 1024) {
			int i1 = (int) (bytes / 1024 / 1024);
			return i1 + " mb";

		} else {
			int i1 = (int) (bytes / 1024 / 1024 / 1024);
			return i1 + " gb";
		}
	}
}
