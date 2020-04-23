package ru.starksoft.commons.simplebus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public final class SimpleBus {

	private static final String TAG = "SimpleBus";
	private final Context context;
	private final Map<String, List<OnEventReceivedListener>> listenersMap = new HashMap<>();

	public SimpleBus(@NonNull Context context, @NonNull List<Event> eventsToRegister) {
		this.context = context;
		IntentFilter intentFilter = new IntentFilter();
		for (Event event : eventsToRegister) {
			intentFilter.addAction(event.name());
		}

		BroadcastReceiver messageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d(TAG, "onReceive: context=" + context + ", intent=" + intent);

				String action = intent.getAction();
				Bundle extras = intent.getExtras();
				List<OnEventReceivedListener> onEventReceivedListeners = listenersMap.get(action);
				if (onEventReceivedListeners != null) {
					for (OnEventReceivedListener l : onEventReceivedListeners) {
						if (action != null) {
							l.onReceived(action, extras);
						}
					}
				}
			}
		};
		LocalBroadcastManager.getInstance(context).registerReceiver(messageReceiver, intentFilter);
	}

	@AnyThread
	public void send(@NonNull Event event) {
		send(event, null);
	}

	@AnyThread
	public void send(@NonNull Event event, @Nullable Bundle extras) {
		Intent intent = new Intent(event.name());
		if (extras != null) {
			intent.putExtras(extras);
		}
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	public void registerEventListener(@NonNull Event event, @NonNull OnEventReceivedListener onEventReceivedListener) {
		List<OnEventReceivedListener> onEventReceivedListeners = listenersMap.get(event.name());
		if (onEventReceivedListeners == null) {
			onEventReceivedListeners = new ArrayList<>();
			listenersMap.put(event.name(), onEventReceivedListeners);
		}

		onEventReceivedListeners.add(onEventReceivedListener);
	}

	public void unregister(@NonNull OnEventReceivedListener onEventReceivedListener) {
		for (Map.Entry<String, List<OnEventReceivedListener>> stringListEntry : listenersMap.entrySet()) {
			List<OnEventReceivedListener> value = stringListEntry.getValue();
			if (value != null) {
				Iterator<OnEventReceivedListener> iterator = value.iterator();
				while (iterator.hasNext()) {
					OnEventReceivedListener next = iterator.next();
					if (next.equals(onEventReceivedListener)) {
						iterator.remove();
					}
				}
			}
		}
	}
}
