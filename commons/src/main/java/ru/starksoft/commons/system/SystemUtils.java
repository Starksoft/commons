package ru.starksoft.commons.system;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.NonNull;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

@SuppressWarnings("unused")
public final class SystemUtils {

	private SystemUtils() {
		throw new UnsupportedOperationException();
	}

	public static void restartApplication(@NonNull Context context) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
		if (intent != null) {
			ComponentName componentName = intent.getComponent();
			Intent mainIntent = Intent.makeRestartActivityTask(componentName);
			context.startActivity(mainIntent);
		}
		System.exit(0);
	}

	@SuppressLint("BatteryLife")
	public static void showDisablePowerManagementDialog(@NonNull Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			String packageName = context.getPackageName();
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
				Intent intent = new Intent();
				intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
				intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("package:" + packageName));
				context.startActivity(intent);
			}
		}
	}

	public static boolean isIgnoringBatteryOptimizations(@NonNull Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			return (pm != null && pm.isIgnoringBatteryOptimizations(context.getPackageName()));
		} else {
			return true;
		}
	}
}
