package ru.starksoft.commons.numeric;

import androidx.annotation.Nullable;

@SuppressWarnings("SpellCheckingInspection")
public final class UnboxingUtils {

    private UnboxingUtils() {
        throw new UnsupportedOperationException();
    }

    public static int safeUnbox(@Nullable Integer boxed) {
        return boxed == null ? 0 : (int) boxed;
    }

    public static long safeUnbox(@Nullable Long boxed) {
        return boxed == null ? 0L : (long) boxed;
    }

    public static short safeUnbox(@Nullable Short boxed) {
        return boxed == null ? 0 : (short) boxed;
    }

    public static byte safeUnbox(@Nullable Byte boxed) {
        return boxed == null ? 0 : (byte) boxed;
    }

    public static char safeUnbox(@Nullable Character boxed) {
        return boxed == null ? '\u0000' : boxed;
    }

    public static double safeUnbox(@Nullable Double boxed) {
        return boxed == null ? 0.0 : boxed;
    }

    public static float safeUnbox(@Nullable Float boxed) {
        return boxed == null ? 0f : boxed;
    }

    public static boolean safeUnbox(@Nullable Boolean boxed) {
        return boxed != null && boxed;
    }
}
