package ru.starksoft.commons;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public final class ColorUtils {

	private ColorUtils() {
		throw new UnsupportedOperationException();
	}

	@NonNull
	public static String hexColorFromInt(@ColorInt int color) {
		return String.format("#%06X", (0xFFFFFF & color));
	}
}
