package ru.starksoft.commons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class CommonUtils {

	private CommonUtils() {
		throw new UnsupportedOperationException();
	}

	public static boolean contains(int key, @NonNull int... array) {
		Arrays.sort(array);
		return Arrays.binarySearch(array, key) >= 0;
	}

	@Nullable
	public static String firstUpperCase(@Nullable String string) {
		if (string == null || string.equals("")) {
			return null;
		}
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	@NonNull
	public static <T> T checkNotNull(@Nullable T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}

	@NonNull
	public static <T> T checkArguments(@Nullable T obj) {
		if (obj == null) {
			throw new IllegalArgumentException();
		}
		return obj;
	}

	public static void checkArgument(boolean condition) {
		if (!condition) {
			throw new IllegalArgumentException();
		}
	}

	@SuppressLint("MissingPermission")
	public static boolean isNetworkAvailable(@NonNull Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		try {
			networkInfo = connMgr.getActiveNetworkInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (networkInfo != null && networkInfo.isConnected());
	}
}
