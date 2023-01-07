package ru.starksoft.commons.numeric

object NumericUtils {

    @JvmStatic
    fun parseInt(input: String?): Int {
        if (input.isNullOrBlank()) {
            return 0
        }
        return try {
            input.trim().toInt()
        } catch (ignored: NumberFormatException) {
            0
        }
    }

    @JvmStatic
    fun parseFloat(input: String?): Float {
        if (input.isNullOrBlank()) {
            return 0f
        }
        return try {
            input.trim().replace(",".toRegex(), ".").toFloat()
        } catch (ignored: NumberFormatException) {
            0f
        }
    }
}
