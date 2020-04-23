package ru.starksoft.commons.callback;

import androidx.annotation.NonNull;

public interface Action1 <T> extends Action {
	void call(@NonNull T param);
}
