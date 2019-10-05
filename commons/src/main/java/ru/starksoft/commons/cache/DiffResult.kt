package ru.starksoft.commons.cache

data class DiffResult<T>(val toAdd: List<T>, val toUpdate: List<T>, val toRemove: List<String>)
