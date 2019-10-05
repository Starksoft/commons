package ru.starksoft.commons.callback

interface Action2<T, T2> : Action {
	fun call(param: T, param2: T2)
}
