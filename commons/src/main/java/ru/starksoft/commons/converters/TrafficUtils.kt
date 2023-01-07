package ru.starksoft.commons.converters

object TrafficUtils {

    @JvmStatic
    fun format(bytes: Long): String {
        return if (bytes <= 1024) {
            "1 kb"
        } else if (bytes < 1024 * 1024) {
            val i1 = (bytes / 1024).toInt()
            "$i1 kb"
        } else if (bytes < 1024 * 1024 * 1024) {
            val i1 = (bytes / 1024 / 1024).toInt()
            "$i1 mb"
        } else {
            val i1 = (bytes / 1024 / 1024 / 1024).toInt()
            "$i1 gb"
        }
    }
}
