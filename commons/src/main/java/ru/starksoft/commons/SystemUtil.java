package ru.starksoft.commons;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

public final class SystemUtil {

	private SystemUtil() {
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
}
