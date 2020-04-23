package ru.starksoft.commons

import java.util.concurrent.TimeUnit

object DelayUtils {

	private val lastUpdateTimeMap: MutableMap<String, Long> by lazy { HashMap() }
	private val UPDATE_DELAY = TimeUnit.HOURS.toMillis(1)

	private fun isDelayReached(currentTime: Long, lastUpdate: Long): Boolean {
		return currentTime - lastUpdate >= UPDATE_DELAY
	}

	@JvmOverloads
	fun checkDelay(key: String, forced: Boolean = false, callback: (time: Long) -> Unit) {
		val currentTimeMillis = System.currentTimeMillis()
		val lastUpdateTime = lastUpdateTimeMap.getOrElse(key) { 0 }

		if (forced || isDelayReached(currentTimeMillis, lastUpdateTime)) {
			callback.invoke(currentTimeMillis)
			lastUpdateTimeMap[key] = currentTimeMillis
		}
	}
}
