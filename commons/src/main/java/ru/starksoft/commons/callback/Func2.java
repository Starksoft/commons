package ru.starksoft.commons.callback;

public interface Func2 <R, T1, T2> extends Function {
	R call(T1 param, T2 param2);
}
