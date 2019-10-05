package ru.starksoft.commons.cache

interface CacheEntry {

	fun getEntryId(): String

	fun getContentHashCode(): Int
}
