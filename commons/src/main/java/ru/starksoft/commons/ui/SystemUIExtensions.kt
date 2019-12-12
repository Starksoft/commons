package ru.starksoft.commons.ui

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.*
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import dev.chrisbanes.insetter.ViewState
import dev.chrisbanes.insetter.doOnApplyWindowInsets

private var hasNavBar: Boolean? = null

fun Activity.setupFullscreen() {
	if (Build.VERSION.SDK_INT in 19..20) {
		window.apply {
			addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
			addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
		}
	}
	if (Build.VERSION.SDK_INT >= 19) {
		window.decorView.systemUiVisibility = getUIShowedFlags()
	}
	if (Build.VERSION.SDK_INT >= 21) {
		window.apply {
			clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
			clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
		}
	}
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
		window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
	}
}

fun Window.hideSystemUI() {
	decorView.systemUiVisibility =
		(View.SYSTEM_UI_FLAG_IMMERSIVE
				or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				or View.SYSTEM_UI_FLAG_FULLSCREEN)
}

fun Window.showSystemUI() {
	decorView.systemUiVisibility = getUIShowedFlags()
}

private fun getUIShowedFlags(): Int {
	return View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
}

fun getStatusBarHeight(context: Context) = getDimen(context, "status_bar_height")

fun getNavBarHeight(context: Context) = getDimen(context, "navigation_bar_height")

fun getDimen(context: Context, resourceString: String): Int {
	return with(context.resources) {
		val id = getIdentifier(resourceString, "dimen", "android")
		if (id > 0) {
			getDimensionPixelSize(id)
		} else {
			0
		}
	}
}

fun hasNavBar(context: Context): Boolean {
	if (hasNavBar != null) {
		return hasNavBar!!
	}
	val res = context.resources
	val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
	if (resourceId != 0) {
		hasNavBar = res.getBoolean(resourceId)
	} else {
		val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
		val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
		val hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)
		hasNavBar = !(hasMenuKey || hasBackKey && hasHomeKey)
	}
	return hasNavBar!!
}

fun View.setOnSystemUiVisibilityChangeListenerEx(callback: (show: Boolean) -> Unit) {
	setOnSystemUiVisibilityChangeListener { visibility ->
		// Note that system bars will only be "visible" if none of the
		// LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
		if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
			// TODO: The system bars are visible. Make any desired
			// adjustments to your UI, such as showing the action bar or
			// other navigational controls.
			callback(true)

		} else {
			// TODO: The system bars are NOT visible. Make any desired
			// adjustments to your UI, such as hiding the action bar or
			// other navigational controls.
			callback(false)
		}
	}
}

fun View.doOnApplyWindowInsetsCompat(action: (view: View, insets: SystemInsetsWrapper, initialState: ViewState) -> Unit) {
	if (Build.VERSION.SDK_INT in 19..20) {
		val insetsWrapper = when (getScreenOrientation(context)) {
			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
				SystemInsetsWrapper(top = getStatusBarHeight(context), bottom = getNavBarHeight(context))
			}

			ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE -> {
				SystemInsetsWrapper(top = getStatusBarHeight(context), right = getNavBarHeight(context))
			}
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> {
				SystemInsetsWrapper(top = getStatusBarHeight(context), right = getNavBarHeight(context))
			}
			else -> SystemInsetsWrapper(0, 0, 0, 0)
		}

		action(this, insetsWrapper, ViewState(this))

	} else {
		doOnApplyWindowInsets { view, insets, initialState -> action(view, insets.mapToSystemInsetsWrapper(), initialState) }
	}
}

@ScreenOrientation
fun getScreenOrientation(context: Context): Int {
	val windowManager = ContextCompat.getSystemService(context, WindowManager::class.java)

	return when (windowManager?.defaultDisplay?.rotation) {
		Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
		Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
		Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
		Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
		else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
	}
}

@IntDef(
	ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
	ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
	ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
	ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
)
@Retention(AnnotationRetention.SOURCE)
annotation class ScreenOrientation

data class SystemInsetsWrapper(
	val left: Int = 0,
	val top: Int = 0,
	val right: Int = 0,
	val bottom: Int = 0
)

fun WindowInsetsCompat.mapToSystemInsetsWrapper(): SystemInsetsWrapper {
	return SystemInsetsWrapper(systemWindowInsetLeft, systemWindowInsetTop, systemWindowInsetRight, systemWindowInsetBottom)
}
