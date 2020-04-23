package ru.starksoft.commons.converters;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public final class TimeIntervalUtils {

	private TimeIntervalUtils() {
		throw new UnsupportedOperationException();
	}

	@NonNull
	public static String format(long time) {

		if (time < 1000) {
			return time + " ms";

		} else if (time < TimeUnit.MINUTES.toMillis(1)) {
			return TimeUnit.MILLISECONDS.toSeconds(time) + " s";

		} else if (time < TimeUnit.HOURS.toMillis(1)) {
			return TimeUnit.MILLISECONDS.toMinutes(time) + " m";

		} else if (time < TimeUnit.DAYS.toMillis(1)) {
			return TimeUnit.MILLISECONDS.toHours(time) + " h";

		} else {
			return TimeUnit.MILLISECONDS.toDays(time) + " d";
		}
	}

	@NonNull
	public static String formatInterval(long time) {
		long ms = time % 1000;
		long sec = (time - ms) % 60_000 / 1000;

		long hr = TimeUnit.MILLISECONDS.toHours(time);
		long min = TimeUnit.MILLISECONDS.toMinutes(time - TimeUnit.HOURS.toMillis(hr));
		return String.format(Locale.US, "%01d:%02d:%02d.%03d", hr, min, sec, ms);
	}
}
