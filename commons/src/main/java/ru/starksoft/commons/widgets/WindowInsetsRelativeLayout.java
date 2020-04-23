package ru.starksoft.commons.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

public class WindowInsetsRelativeLayout extends RelativeLayout {

	public WindowInsetsRelativeLayout(Context context) {
		super(context);
	}

	public WindowInsetsRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WindowInsetsRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public WindowInsetsRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public WindowInsets onApplyWindowInsets(WindowInsets insets) {
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++) {
			getChildAt(index).dispatchApplyWindowInsets(insets);
		}

		return insets;
	}
}
