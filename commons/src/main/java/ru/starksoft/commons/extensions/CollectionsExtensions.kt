package ru.starksoft.commons.extensions

fun <E> MutableCollection<E>.addAll(elements: Collection<E>, clear: Boolean) {
	if (clear) {
		clear()
	}
	addAll(elements)
}
