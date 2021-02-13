package ru.starksoft.commons.example;

import android.content.Context;

import androidx.annotation.NonNull;
import ru.starksoft.commons.BuildConfig;
import ru.starksoft.commons.callback.Func0;
import ru.starksoft.commons.logger.AbstractLogger;

public final class Logger extends AbstractLogger {

    public Logger(@NonNull Context context) {
        super(context.getFilesDir(), true, BuildConfig.DEBUG);
    }

    public void logMisc(@NonNull String message) {
        log(LogType.MISC, message, null);
    }

    public void logMisc(@NonNull String message, @NonNull Func0<Object> args) {
        log(LogType.MISC, message, args);
    }

    public void logDisabled(@NonNull String message, @NonNull Func0<Object> args) {
        log(LogType.DISABLED, message, args);
    }

    public enum LogType implements AbstractLogger.LogType {
        MISC(true), DISABLED(false);

        private final boolean enabled;

        LogType(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }

}
