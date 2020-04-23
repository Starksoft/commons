package ru.starksoft.commons.numeric;

import androidx.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public final class NumericUtils {

	private NumericUtils() {
		throw new UnsupportedOperationException();
	}

	public static int parseInt(@Nullable String input) {
		int result = 0;

		if (input == null || input.length() == 0) {
			return result;
		}

		try {
			result = Integer.parseInt(input.trim());
		} catch (NumberFormatException ignored) {
		}

		return result;
	}
}
