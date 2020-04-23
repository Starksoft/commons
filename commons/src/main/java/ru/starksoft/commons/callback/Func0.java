package ru.starksoft.commons.callback;

import androidx.annotation.NonNull;

public interface Func0 <R> extends Function {
	@NonNull
	R call();
}
