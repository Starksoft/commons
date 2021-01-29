package ru.starksoft.commons;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.starksoft.commons.callback.Action1;
import ru.starksoft.commons.callback.Func0;
import ru.starksoft.commons.callback.Func1;

public final class NullUtils {

    private static final String TAG = "NullUtils";

    private NullUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> void action(@Nullable T param, @NonNull Action1<T> action) {
        Log.d(TAG, "action: param=" + param + ", action=" + action);
        if (param != null) {
            action.call(param);
        } else {
            Log.d(TAG, "action: param is null");
        }
    }

    public static <T1, T2> void action2(@Nullable T1 param1,
                                        @NonNull Func1<T2, T1> action,
                                        @NonNull Action1<T2> action2) {
        if (param1 != null) {
            T2 param2 = action.call(param1);
            if (param2 != null) {
                action2.call(param2);
            } else {
                Log.d(TAG, "action2: param2 is null");
            }
        } else {
            Log.d(TAG, "action2: param1 is null");
        }
    }

    public static <R> R function(@Nullable R param, @NonNull Func0<R> function) {
        return param == null ? function.call() : param;
    }

    public static <R, T> R function(@Nullable T param, @NonNull R result, @NonNull Func1<R, T> function) {
        return param == null ? result : function.call(param);
    }
}
