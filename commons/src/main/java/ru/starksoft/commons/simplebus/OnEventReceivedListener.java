package ru.starksoft.commons.simplebus;

import android.os.Bundle;

import androidx.annotation.NonNull;

public interface OnEventReceivedListener {
	void onReceived(@NonNull String event, Bundle extras);
}
