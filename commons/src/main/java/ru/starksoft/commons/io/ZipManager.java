package ru.starksoft.commons.io;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipManager {

	private static final String TAG = "ZipManager";

	private ZipManager() {
		throw new UnsupportedOperationException();
	}

	private static final int BUFFER_SIZE = 6 * 1024;

	public static void zip(String[] files, String zipFile) throws IOException {
		BufferedInputStream origin;
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
		try {
			byte data[] = new byte[BUFFER_SIZE];

			for (int i = 0; i < files.length; i++) {
				FileInputStream fi = new FileInputStream(files[i]);
				origin = new BufferedInputStream(fi, BUFFER_SIZE);
				try {
					ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
						out.write(data, 0, count);
					}
				} finally {
					origin.close();
				}
			}
		} finally {
			out.close();
		}
	}

	public static boolean unzip(String zipFile, String location) {
		try {
			File f = new File(location);
			if (!f.isDirectory()) {
				f.mkdirs();
			}
			try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile))) {
				ZipEntry ze;
				while ((ze = zin.getNextEntry()) != null) {
					String path = location + File.separator + ze.getName();

					if (ze.isDirectory()) {
						File unzipFile = new File(path);
						if (!unzipFile.isDirectory()) {
							unzipFile.mkdirs();
						}
					} else {

						try (FileOutputStream fout = new FileOutputStream(path, false)) {
							for (int c = zin.read(); c != -1; c = zin.read()) {
								fout.write(c);
							}
							zin.closeEntry();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return true;
			} catch (Exception e) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Unzip exception", e);
			return false;
		}
	}
}
