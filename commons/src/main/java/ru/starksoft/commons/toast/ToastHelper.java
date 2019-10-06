package ru.starksoft.commons.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

public final class ToastHelper {

	private ToastHelper() {
		throw new UnsupportedOperationException();
	}

	public static void showToast(@NonNull Context context, int message, boolean longLength) {
		Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}

	public static void showToast(@NonNull Context context, String message, boolean longLength) {
		Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}

	public static void showToastInBottom(@NonNull Context context, String message, boolean longLength) {
		Toast toast = Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}

	public static void showToastInCenter(@NonNull Context context, String message, boolean longLength) {
		Toast toast = Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
