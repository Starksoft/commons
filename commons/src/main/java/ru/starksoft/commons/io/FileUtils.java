package ru.starksoft.commons.io;

import android.util.Log;

import java.io.File;

import androidx.annotation.NonNull;

import static ru.starksoft.commons.CommonUtils.checkNotNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class FileUtils {

	private static final String TAG = "FileUtils";

	public static boolean deleteContentsAndDir(@NonNull File dir) {
		if (deleteContents(checkNotNull(dir))) {
			return dir.delete();
		} else {
			return false;
		}
	}

	public static boolean deleteContents(@NonNull File dir) {
		File[] files = checkNotNull(dir).listFiles();
		boolean success = true;
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					success &= deleteContents(file);
				}
				if (!file.delete()) {
					Log.w(TAG, "Failed to delete " + file);
					success = false;
				}
			}
		}
		return success;
	}
}
