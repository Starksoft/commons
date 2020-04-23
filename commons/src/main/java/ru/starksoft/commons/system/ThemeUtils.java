package ru.starksoft.commons.system;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public final class ThemeUtils {

	private ThemeUtils() {
		throw new UnsupportedOperationException();
	}

	public static boolean isNightTheme(@NonNull Context context) {
		switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
			case Configuration.UI_MODE_NIGHT_YES:
				return true;

			case Configuration.UI_MODE_NIGHT_NO:
			default:
				return false;
		}
	}
}
