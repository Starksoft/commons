package ru.starksoft.commons;

import java.util.Arrays;

import androidx.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public final class HashCodeUtils {

    public static final int NONE_HASHCODE = 0;

    private HashCodeUtils() {
        throw new UnsupportedOperationException();
    }

    public static int hashCode(@Nullable Object... list) {
        return hashCode(null, list);
    }

    public static int hashCode(@Nullable Class clazz, @Nullable Object... list) {
        if (list == null && clazz == null) {
            return NONE_HASHCODE;
        }

        int result = clazz == null ? 1 : 31 + clazz.getName().hashCode();
        if (list != null) {
            result = 31 * result + Arrays.deepHashCode(list);
        }
        return result;
    }
}
