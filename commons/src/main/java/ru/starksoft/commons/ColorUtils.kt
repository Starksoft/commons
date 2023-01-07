package ru.starksoft.commons

import androidx.annotation.ColorInt

object ColorUtils {

    @JvmStatic
    fun hexColorFromInt(
        @ColorInt
        color: Int
    ): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }
}
