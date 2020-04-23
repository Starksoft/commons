package ru.starksoft.commons.system;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class OpenHelper {

	private static final String TAG = "OpenHelper";

	private OpenHelper() {
		throw new UnsupportedOperationException();
	}

	public static void openGooglePlay(@NonNull Fragment fragment, @NonNull String targetPackageName) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + targetPackageName));
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
			fragment.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void openGooglePlay(@NotNull Context context) {
		openGooglePlay(context, context.getPackageName());
	}

	public static void openGooglePlay(@NotNull Context context, @NonNull String targetPackageName) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + targetPackageName));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void openPackageOrGooglePlay(@NotNull Context context, @NonNull String targetPackageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(targetPackageName);
		if (intent != null) {
			context.startActivity(intent);
		} else {
			openGooglePlay(context, targetPackageName);
		}
	}

	public static void openPackageDetails(@NonNull Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.fromParts("package", context.getPackageName(), null));
		context.startActivity(intent);
	}

	public static void openPackageDetails(@NonNull Fragment fragment, @NonNull String targetPackageName) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.fromParts("package", targetPackageName, null));
		fragment.startActivity(intent);
	}

	public static void openUrl(@Nullable Context context, @NonNull String url) {
		if (context == null) {
			return;
		}

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(browserIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	public static void openUrlInChromeCustomTab(@NonNull Context context, @NonNull String url) {
	//		CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
	//		customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	//		customTabsIntent.launchUrl(context, Uri.parse(url));
	//	}

	public static void openMap(@NonNull Context context, @NonNull String address) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openDialer(@NotNull Context context, @NotNull String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
