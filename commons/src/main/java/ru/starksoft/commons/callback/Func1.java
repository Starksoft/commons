package ru.starksoft.commons.callback;

public interface Func1<R, T> extends Function {

    R call(T param);
}
