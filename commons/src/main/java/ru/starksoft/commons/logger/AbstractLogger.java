package ru.starksoft.commons.logger;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.starksoft.commons.BuildConfig;
import ru.starksoft.commons.callback.Func0;

public abstract class AbstractLogger {

	private static final boolean LOG_ENABLED = true;
	private static final String LOG_DELIMITER = ": ";
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final SimpleDateFormat logDateFormat;
	private final File logDir;
	private File logFile;

	public AbstractLogger(@NonNull File logDir) {
		this.logDir = logDir;
		logDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US);
	}

	@NonNull
	static String replaceLogPlaceholders(@NonNull String input, @Nullable Func0<Object> args) {
		int indexOf = input.indexOf("{}");

		if (indexOf >= 0 && args != null) {
			@Nullable Object call = args.call();

			String argsString;
			if (call instanceof List) {
				List collection = (List) call;

				int argSize = collection.size();
				int placeHoldersSize = findWord(input, "{}").size();
				if (argSize != placeHoldersSize) {
					throw new IllegalArgumentException(
							"{} placeholders (" + placeHoldersSize + ") and arguments (" + argSize + ") mismatch");
				}

				for (int i = 0; i < placeHoldersSize; i++) {
					Object o = collection.get(i);
					String s = o == null ? "null" : o.toString();
					input = input.replaceFirst(Pattern.quote("{}"), Matcher.quoteReplacement(s));
				}

			} else {
				if (call == null) {
					argsString = "null";
				} else {
					argsString = call.toString();
				}

				return input.replace("{}", argsString);
			}
		}

		return input;
	}

	@NonNull
	static List<Integer> findWord(@NonNull String textString, @NonNull String word) {
		List<Integer> indexes = new ArrayList<>();
		String lowerCaseTextString = textString.toLowerCase();
		String lowerCaseWord = word.toLowerCase();
		int wordLength = 0;

		int index = 0;
		while (index != -1) {
			index = lowerCaseTextString.indexOf(lowerCaseWord, index + wordLength);
			if (index != -1) {
				indexes.add(index);
			}
			wordLength = word.length();
		}
		return indexes;
	}

	public final void log(@NonNull LogType logType, @NonNull final String message) {
		log(logType, message, null);
	}

	public final void log(@NonNull LogType logType, @NonNull final String message, @Nullable Func0<Object> args) {
		if (!LOG_ENABLED) {
			return;
		}

		executorService.submit(() -> {
			try {
				printMessage(logType, replaceLogPlaceholders(message, args));
			} catch (Exception e) {
				e.printStackTrace();
				printMessage(logType, "Exception in Logger! e=" + Log.getStackTraceString(e));
			}
		});
	}

	private void printMessage(@NonNull LogType logType, String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(logType.name(), msg);
		}

		writeLogToFile(logType.name() + LOG_DELIMITER + msg);
	}

	@NonNull
	private File getLogFile() {
		if (logFile == null) {
			File logFolder = new File(logDir + File.separator + "logs");
			if (!logFolder.exists()) {
				if (!logFolder.mkdirs()) {
					throw new IllegalStateException("Cant create log folder");
				}
			}

			logFile = new File(logFolder, "log.txt");
		}
		return logFile;
	}

	private void writeLogToFile(@NonNull String text) {
		try (FileWriter fWriter = new FileWriter(getLogFile(), true)) {

			fWriter.write(logDateFormat.format(new Date()));
			fWriter.write(LOG_DELIMITER);
			fWriter.write(text);
			fWriter.write("\n");
			fWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public interface LogType {

		@NonNull
		String name();
	}
}
