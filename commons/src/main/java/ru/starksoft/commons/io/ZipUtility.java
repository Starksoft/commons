package ru.starksoft.commons.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import androidx.annotation.NonNull;

public final class ZipUtility {

	private ZipUtility() {
		throw new UnsupportedOperationException();
	}

	public static void zipDirectory(File directory, File zip) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
		zip(directory, directory, zos);
		zos.close();
	}

	private static void zip(File directory, File base, ZipOutputStream zos) throws IOException {
		File[] files = directory.listFiles();
		byte[] buffer = new byte[8192];
		int read = 0;
		for (int i = 0, n = files.length; i < n; i++) {
			if (files[i].isDirectory()) {
				zip(files[i], base, zos);
			} else {
				FileInputStream in = new FileInputStream(files[i]);
				ZipEntry entry = new ZipEntry(files[i].getPath().substring(base.getPath().length() + 1));
				zos.putNextEntry(entry);
				while (-1 != (read = in.read(buffer))) {
					zos.write(buffer, 0, read);
				}
				in.close();
			}
		}
	}

	public static void unzip(File zip, File extractTo) throws IOException {
		ZipFile archive = new ZipFile(zip);
		Enumeration e = archive.entries();
		while (e.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) e.nextElement();
			File file = new File(extractTo, entry.getName());
			if (entry.isDirectory() && !file.exists()) {
				file.mkdirs();
			} else {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				InputStream in = archive.getInputStream(entry);
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

				byte[] buffer = new byte[8192];
				int read;

				while (-1 != (read = in.read(buffer))) {
					out.write(buffer, 0, read);
				}
				in.close();
				out.close();
			}
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void unzip(@NonNull String location, @NonNull String zipFile) throws IOException {
		File f = new File(location);
		if (!isValidZip(new File(zipFile))) {
			throw new IOException("File " + zipFile + " is corrupted!");
		}

		if (!f.isDirectory()) {
			f.mkdirs();
		}

		try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				String path = location + ze.getName();

				if (ze.isDirectory()) {
					File unzipFile = new File(path);
					if (!unzipFile.isDirectory()) {
						unzipFile.mkdirs();
					}
				} else {
					try (FileOutputStream fileOutputStream = new FileOutputStream(path, false)) {
						for (int c = zin.read(); c != -1; c = zin.read()) {
							fileOutputStream.write(c);
						}
						zin.closeEntry();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isValidZip(@NonNull File file) {
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(file);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (zipfile != null) {
					zipfile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
