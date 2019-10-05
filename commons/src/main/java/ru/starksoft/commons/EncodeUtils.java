package ru.starksoft.commons;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncodeUtils {

	public static String md5(@NonNull String input) {
		final String MD5 = "MD5";
		try {
			MessageDigest digest = MessageDigest.getInstance(MD5);
			digest.update(input.getBytes());
			byte[] messageDigest = digest.digest();

			StringBuilder hexString = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				StringBuilder stringBuilder = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
				while (stringBuilder.length() < 2) {
					stringBuilder.insert(0, "0");
				}
				hexString.append(stringBuilder);
			}
			return hexString.toString().toUpperCase();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
