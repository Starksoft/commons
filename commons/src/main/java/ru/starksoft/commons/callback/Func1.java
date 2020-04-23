package ru.starksoft.commons.callback;

import androidx.annotation.NonNull;

public interface Func1 <R, T> extends Function {
	R call(@NonNull T param);
}
