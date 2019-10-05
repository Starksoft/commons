package ru.starksoft.commons;

import android.content.res.Resources;
import android.text.format.DateUtils;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public final class TimeUtils {

	public static final int SIMPLE_DATE_FORMAT = 1;
	public static final int FULL_DATE_FORMAT = 2;
	public static final int FULL_DATE_FORMAT_WITH_TIME = 3;
	public static final int FRIENDS_RESUME_DATE_FORMAT = 4;
	public static final int RESUMES_COMMENTS_DATE_FORMAT = 5;
	public static final int BILLS_DATE_FORMAT = 6;
	public static final int FULL_DATE_FORMAT_WITH_TIME_EX = 7;
	public static final String D_MMMM_YYYY = "d MMMM yyyy";

	private static String dateToday;
	private static String dateIn;
	private static String dateYesterday;

	private static String[] sMonthNames = new String[12];
	private static String[] sMonthGenitiveNames = new String[12];
	private static String[] dayOfWeekInTime = new String[7];
	private static String[] dayOfWeekDeclensionInTime = new String[7];
	private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols() {
		@Override
		public String[] getMonths() {
			return sMonthGenitiveNames;
		}
	};

	private TimeUtils() {
		throw new UnsupportedOperationException();
	}

	private static void checkInit() {
		if (dateYesterday == null || dateToday == null) {
			throw new IllegalStateException("Not initialized, call init() for proper execution");
		}
	}

	public static void init(@NonNull Resources resources) {
//		sMonthNames = resources.getStringArray(R.array.months);
//		sMonthGenitiveNames = resources.getStringArray(R.array.monthsGenitive);
//
//		dateYesterday = resources.getString(R.string.date_yesterday);
//		dateToday = resources.getString(R.string.date_today);
//		dateIn = resources.getString(R.string.date_in);
	}

	/**
	 * @param serverTimeStamp unicode time stamp, in seconds
	 * @param format          SIMPLE_DATE_FORMAT (сегодня, вчера, 20 января, 23 марта 2015, ...),
	 *                        FULL_DATE_FORMAT (30 августа 2013, ...),
	 *                        FULL_DATE_FORMAT_WITH_TIME (12:30, 12.10.2014, ...),
	 *                        FRIENDS_RESUME_DATE_FORMAT (сегодня, вчера, 20 января, 2014, 2013, ...)
	 *                        RESUMES_COMMENTS_DATE_FORMAT (9:00, 10 сентября, 23 марта 2015, ...)
	 *                        BILLS_DATE_FORMAT (сегодня, 10 сентября, 23 марта 2015, ...)
	 */
	public static String formatTimeStamp(long serverTimeStamp, @TimeFormat int format) {
		checkInit();

		final long timeInMs = serverTimeStamp * 1000;

		Locale locale = Locale.getDefault();
		TimeZone timezone = TimeZone.getDefault();

		Calendar objCalendar = Calendar.getInstance(timezone);
		objCalendar.setTimeInMillis(timeInMs);

		Calendar now = Calendar.getInstance();

		SimpleDateFormat objFormatter = new SimpleDateFormat("d MMMM", locale);

		if ((now.get(Calendar.YEAR) - objCalendar.get(Calendar.YEAR) > 0)) {
			objFormatter = new SimpleDateFormat(D_MMMM_YYYY, locale);
		}

		if (format == FULL_DATE_FORMAT) {
			objFormatter = new SimpleDateFormat(D_MMMM_YYYY, locale);

		} else if (format == FULL_DATE_FORMAT_WITH_TIME) {
			objFormatter = new SimpleDateFormat("HH:mm, dd.MM.yyyy", locale);

		} else if (format == FRIENDS_RESUME_DATE_FORMAT && (now.get(Calendar.YEAR) - objCalendar.get(Calendar.YEAR) > 0)) {
			objFormatter = new SimpleDateFormat(dateIn + " yyyy", locale);

		} else if (format == FULL_DATE_FORMAT_WITH_TIME_EX) {
			if ((DateUtils.isToday(timeInMs))) {
				objFormatter = new SimpleDateFormat(dateIn + " HH:mm", locale);
			}

		} else if (format == RESUMES_COMMENTS_DATE_FORMAT) {
			if ((DateUtils.isToday(timeInMs))) {
				objFormatter = new SimpleDateFormat("HH:mm", locale);
			} else if (now.get(Calendar.YEAR) - objCalendar.get(Calendar.YEAR) > 0) {
				objFormatter = new SimpleDateFormat(D_MMMM_YYYY, locale);
			}
		}

		objFormatter.setTimeZone(timezone);

		String result;
		if ((DateUtils.isToday(timeInMs)) && (format == SIMPLE_DATE_FORMAT || format == FRIENDS_RESUME_DATE_FORMAT || format == BILLS_DATE_FORMAT)) {
			result = dateToday;
		} else if ((format == SIMPLE_DATE_FORMAT || format == FRIENDS_RESUME_DATE_FORMAT || format == FULL_DATE_FORMAT_WITH_TIME_EX) &&
				(now.get(Calendar.DATE) - objCalendar.get(Calendar.DATE) == 1) && (now.get(Calendar.MONTH)) == objCalendar.get(Calendar.MONTH)) {

			result = dateYesterday;
		} else {
			objFormatter.setDateFormatSymbols(myDateFormatSymbols);
			result = objFormatter.format(objCalendar.getTime());
		}

		objCalendar.clear();
		return result;
	}

	public static int getAge(int year, int month, int day) throws TimeUtilsException {
		GregorianCalendar cal = new GregorianCalendar();
		int y, m, d, a;

		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH);
		d = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(year, month, day);
		a = y - cal.get(Calendar.YEAR);
		if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
			--a;
		}
		if (a < 0) {
			throw new TimeUtilsException("Age < 0");
		}
		return a;
	}

	public static Calendar parseSimpleDateToCalendar(String date) throws ParseException {
		Date parseDate = parseSimpleDate(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate);
		return calendar;
	}

	@Nullable
	public static Date parseSimpleDate(@Nullable String date) {
		try {
			return parseDate(date, "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Date parseDate(String date, String format) throws ParseException {
		DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		return df.parse(date);
	}

	/**
	 * @param month from zero index
	 * @return month
	 */
	public static String getMonthByNumber(int month) {
		checkInit();

		return month >= 0 && month < sMonthNames.length ? sMonthNames[month] : "";
	}

	public static String getFormatDayOfWeek(Resources resources, long timeStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeStamp);
		int dayInt = cal.get(Calendar.DAY_OF_WEEK);
		if (dayInt == 1) {
			dayInt = 7;
		}
//		dayOfWeekDeclensionInTime = resources.getStringArray(R.array.dayOfWeekDeclensionInTime);
		return dayOfWeekDeclensionInTime[dayInt - 2];
	}

	@Retention(SOURCE)
	@IntDef({
			SIMPLE_DATE_FORMAT,
			FULL_DATE_FORMAT,
			FULL_DATE_FORMAT_WITH_TIME,
			FRIENDS_RESUME_DATE_FORMAT,
			RESUMES_COMMENTS_DATE_FORMAT,
			BILLS_DATE_FORMAT,
			FULL_DATE_FORMAT_WITH_TIME_EX})
	public @interface TimeFormat {
	}

	public static class TimeUtilsException extends RuntimeException {

		public TimeUtilsException(String message) {
			super(message);
		}
	}
}
