package ru.starksoft.commons.callback

interface Func2<R, T1, T2> : Function {
	fun call(param: T1, param2: T2): R
}
