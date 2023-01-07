package ru.starksoft.commons

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object ApiLevelUtils {

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    val isN: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    val isQ: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    val isO: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    val isM: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    val isP: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

}
