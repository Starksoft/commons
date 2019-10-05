package ru.starksoft.commons

import android.os.Looper

object ThreadUtil {

	@JvmStatic
	fun checkNotOnMainThread() {
		check(Looper.getMainLooper() != Looper.myLooper()) { "You are not allowed to run this method on main thread" }
	}

	@JvmStatic
	fun checkOnMainThread() {
		check(Looper.getMainLooper() == Looper.myLooper()) { "You are not allowed to run this method not on main thread" }
	}

	@JvmStatic
	fun isOnMainThread(): Boolean {
		return Looper.myLooper() == Looper.getMainLooper()
	}
}
