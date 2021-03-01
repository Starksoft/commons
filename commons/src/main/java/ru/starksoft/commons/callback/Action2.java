package ru.starksoft.commons.callback;

public interface Action2<T, T2> extends Action {

    void call(T param, T2 param2);
}
