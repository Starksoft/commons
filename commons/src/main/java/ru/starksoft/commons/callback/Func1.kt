package ru.starksoft.commons.callback

interface Func1<R, T> : Function {
	fun call(param: T): R
}
