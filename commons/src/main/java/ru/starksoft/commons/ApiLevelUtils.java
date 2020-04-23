package ru.starksoft.commons;

import android.os.Build;

public final class ApiLevelUtils {

	private ApiLevelUtils() {
		throw new UnsupportedOperationException();
	}

	public static boolean isN() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
	}

	public static boolean isQ() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
	}

	public static boolean isO() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
	}

	public static boolean isM() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	public static boolean isP() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
	}
}
