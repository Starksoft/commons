package ru.starksoft.commons.callback

interface Action1<T> : Action {
	fun call(param: T)
}
