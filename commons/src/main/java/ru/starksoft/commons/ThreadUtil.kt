package ru.starksoft.commons

import android.os.Looper
import android.util.Log

object ThreadUtil {

	private const val TAG = "ThreadUtil"

	@JvmStatic
	fun checkNotOnMainThread() {
		if (isOnMainThread()) {
			Log.d(TAG, "${Thread.currentThread().name} ### checkOnMainThread: ")
			throw IllegalStateException("You are not allowed to run this method on main thread")
		}
	}

	@JvmStatic
	fun checkOnMainThread() {
		if (!isOnMainThread()) {
			Log.d(TAG, "${Thread.currentThread().name} ### checkOnMainThread: ")
			throw IllegalStateException("Called from wrong thread, expected main, but was called from ${Thread.currentThread().name}")
		}
	}

	@JvmStatic
	fun isOnMainThread(): Boolean {
		return Looper.myLooper() == Looper.getMainLooper()
	}
}
