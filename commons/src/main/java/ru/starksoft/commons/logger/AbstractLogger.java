package ru.starksoft.commons.logger;

import android.content.Context;
import android.net.Uri;
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
import ru.starksoft.commons.callback.Func0;

public abstract class AbstractLogger {

    private static final String LOG_DELIMITER = ": ";
    private static final String LOG_FOLDER = "logs";
    private static final String LOG_FILE = "log.txt";
    private final static String PATTERN = "yyyy.MM.dd HH:mm:ss";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final SimpleDateFormat logDateFormat;
    private final boolean logcatEnabled;
    private final boolean printThreadName;
    private final File logFile;

    public AbstractLogger(@NonNull Context context) {
        this(context, LOG_FILE, false, true);
    }

    public AbstractLogger(@NonNull Context context,
                          @NonNull String fileName,
                          boolean logcatEnabled,
                          boolean printThreadName) {
        File filesDir = context.getFilesDir();
        File logDirectory = new File(filesDir, LOG_FOLDER);

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalStateException("fileName is null");
        }

        if (!logDirectory.exists()) {
            if (!logDirectory.mkdirs()) {
                throw new IllegalStateException("Can`t create log folder");
            }
        }

        this.logFile = new File(logDirectory, fileName);
        this.logcatEnabled = logcatEnabled;
        this.logDateFormat = new SimpleDateFormat(PATTERN, Locale.US);
        this.printThreadName = printThreadName;
    }

    @NonNull
    @SuppressWarnings("rawtypes")
    static String replaceLogPlaceholders(@NonNull String input, @Nullable Func0<Object> args) {
        if (args != null) {
            @Nullable
            Object call = args.call();

            String argsString;
            if (call != null && call.getClass().isArray()) {
                Object[] array = (Object[]) call;

                int argSize = array.length;
                int placeHoldersSize = findWord(input, "{}").size();

                checkArgsAndPlaceholders(input, argSize, placeHoldersSize);

                for (int i = 0; i < placeHoldersSize; i++) {
                    Object o = array[i];
                    input = replaceStringWithObject(input, o);
                }

            } else if (call instanceof List) {
                List collection = (List) call;

                int argSize = collection.size();
                int placeHoldersSize = findWord(input, "{}").size();

                checkArgsAndPlaceholders(input, argSize, placeHoldersSize);

                for (int i = 0; i < placeHoldersSize; i++) {
                    Object o = collection.get(i);
                    input = replaceStringWithObject(input, o);
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
    private static String replaceStringWithObject(@NonNull String input, Object o) {
        String s = o == null ? "null" : o.toString();
        input = input.replaceFirst(Pattern.quote("{}"), Matcher.quoteReplacement(s));
        return input;
    }

    private static void checkArgsAndPlaceholders(@NonNull String input, int argSize, int placeHoldersSize) {
        if (argSize != placeHoldersSize) {
            throw new IllegalArgumentException("{} placeholders (" +
                                               placeHoldersSize +
                                               ") and arguments (" +
                                               argSize +
                                               ") mismatch. Message: \"" +
                                               input +
                                               "\"");
        }
    }

    @NonNull
    @SuppressWarnings("SameParameterValue")
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

    public final void log(@NonNull LogType logType, @NonNull String message) {
        log(logType, message, null);
    }

    public final void logException(@NonNull LogType logType, @Nullable String message, @NonNull Throwable t) {
        boolean empty = message == null || message.isEmpty();
        log(logType, empty ? "{}" : message, () -> Log.getStackTraceString(t));
    }

    public final void log(@NonNull LogType logType, @NonNull final String message, @Nullable Func0<Object> args) {
        if (!logType.isEnabled()) {
            return;
        }

        String callerThreadName = Thread.currentThread().getName();

        executorService.submit(() -> {
            try {
                printMessage(logType, callerThreadName, replaceLogPlaceholders(message, args));
            } catch (Throwable t) {
                t.printStackTrace();
                printMessage(logType, callerThreadName, "Throwable in Logger! t=" + Log.getStackTraceString(t));
            }
        });
    }

    private void printMessage(@NonNull LogType logType, @NonNull String callerThreadName, String msg) {
        if (logcatEnabled) {
            Log.d(logType.name(), msg);
        }

        String threadPrint = "";
        if (printThreadName) {
            threadPrint = LOG_DELIMITER + "### " + callerThreadName;
        }

        writeLogToFile(logType.name() + threadPrint + LOG_DELIMITER + msg);
    }

    @NonNull
    public File getLogFile() {
        synchronized (this) {
            return logFile;
        }
    }

    public long getLogFileSize() {
        synchronized (this) {
            return getLogFile().length();
        }
    }

    @NonNull
    public Uri getFileUri(@NonNull Context context) {
        return Uri.parse("content://" + context.getPackageName() + ".fileprovider/" + LOG_FOLDER + "/" + LOG_FILE);
    }

    public boolean clear() {
        synchronized (this) {
            return getLogFile().delete();
        }
    }

    private void writeLogToFile(@NonNull String text) {
        synchronized (this) {
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
    }

    public interface LogType {

        @NonNull
        String name();

        boolean isEnabled();
    }
}
