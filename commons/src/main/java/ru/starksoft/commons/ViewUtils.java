package ru.starksoft.commons;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

@SuppressWarnings("WeakerAccess")
public final class ViewUtils {

	private static final int MIN_CLICK_INTERVAL = 600;
	private static long lastClickTime;

	private ViewUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Hides all views
	 *
	 * @param views - views to operate on
	 */
	public static void hideViews(@Nullable View... views) {
		setVisibility(false, views);
	}

	/**
	 * Shows all views
	 *
	 * @param views - views to operate on
	 */
	public static void showViews(@Nullable View... views) {
		setVisibility(true, views);
	}

	/**
	 * @param visible - true to setVisibility(View.VISIBLE) and false to setVisibility(type)
	 * @param type    - One of View.GONE or View.INVISIBLE
	 * @param views   - views to operate on
	 */
	public static void setVisibility(boolean visible, @Visibility int type, @Nullable View... views) {
		if (type != View.GONE && type != View.INVISIBLE) {
			throw new RuntimeException("Method supports View.GONE or View.INVISIBLE only");
		}

		if (views != null) {
			for (View view : views) {
				if (view != null) {
					view.setVisibility(visible ? View.VISIBLE : type);
				}
			}
		}
	}

	/**
	 * Sets visibility of given views
	 *
	 * @param visible - true to setVisibility(View.VISIBLE) and false to setVisibility(View.GONE)
	 * @param views   - views to operate on
	 */
	public static void setVisibility(boolean visible, @Nullable View... views) {
		setVisibility(visible, View.GONE, views);
	}

	/**
	 * Sets visibility of given views
	 *
	 * @param visible - true to setVisibility(View.VISIBLE) and false to setVisibility(View.INVISIBLE)
	 * @param views   - views to operate on
	 */
	public static void setVisibilityKeepingSpace(boolean visible, @Nullable View... views) {
		setVisibility(visible, View.INVISIBLE, views);
	}

	/**
	 * Sets enabled of given views
	 *
	 * @param enabled - true to setEnabled(true) and false to setEnabled(false)
	 * @param views   - views to operate on
	 */
	public static void setEnabled(boolean enabled, @Nullable View... views) {
		if (views != null) {
			for (View view : views) {
				if (view != null) {
					view.setEnabled(enabled);
				}
			}
		}
	}

	public static int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static float dpToPxF(int dp) {
		return (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static int pxToDp(@NonNull Context context, int px) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		float logicalDensity = metrics.density;
		return Math.round(px / logicalDensity);

		//DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		//return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	}

	public static boolean isSingleClicked() {
		long currentClickTime = System.currentTimeMillis();
		long elapsedTime = currentClickTime - lastClickTime;
		lastClickTime = currentClickTime;
		return elapsedTime > MIN_CLICK_INTERVAL;
	}

	//	public static boolean isLinearLayoutManagerAtEnd(@NonNull LinearLayoutManager layoutManager) {
	//		int visibleItemCount = layoutManager.getChildCount();
	//		int totalItemCount = layoutManager.getItemCount();
	//		int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
	//		return visibleItemCount + pastVisibleItems >= totalItemCount;
	//	}

	public static void hideSoftKeyboard(@Nullable Activity activity) {
		if (activity == null) {
			return;
		}
		InputMethodManager inputManager = getInputMethodManager(activity);
		View focus = activity.getCurrentFocus();
		if (inputManager != null && focus != null) {
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}

	public static void hideSoftKeyboard(@NonNull View view) {
		InputMethodManager inputManager = getInputMethodManager(view.getContext());
		if (inputManager != null) {
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static void showSoftKeyboard(@NonNull Context context) {
		InputMethodManager inputManager = getInputMethodManager(context);
		if (inputManager != null) {
			inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}

	public static void showToast(@NonNull Context context, int message, boolean longLength) {
		Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}

	public static void showToast(@NonNull Context context, String message, boolean longLength) {
		Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}

	public static void showToastInBottom(@NonNull Context context, String message, boolean longLength) {
		Toast toast = Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}

	public static void showToastInCenter(@NonNull Context context, String message, boolean longLength) {
		Toast toast = Toast.makeText(context, message, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static boolean isTextViewEllipsized(@NonNull TextView textView) {
		Layout layout = textView.getLayout();
		if (layout != null) {
			for (int i = 0; i < layout.getLineCount(); i++) {
				if (layout.getEllipsisCount(i) > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getTextViewVisibleText(@NonNull TextView textView) {
		Layout layout = textView.getLayout();
		if (layout != null) {
			int start = layout.getLineStart(0);
			int end = layout.getLineEnd(textView.getLineCount() - 1);
			return textView.getText().toString().substring(start, end);
		}
		return "";
	}

	public static void setMenuVisibility(@NonNull Menu menu, int id, boolean visible) {
		MenuItem item = menu.findItem(id);
		if (item != null) {
			item.setVisible(visible);
		}
	}

	public static void setTextAndVisibility(@Nullable TextView textView, @Nullable CharSequence text) {
		if (textView == null) {
			return;
		}

		boolean isVisible = !StringUtils.isEmpty(text);
		if (isVisible) {
			textView.setText(text);
		}
		setVisibility(isVisible, textView);
	}

	public static void setTextAndParentVisibility(@Nullable TextView textView, @Nullable CharSequence text) {
		if (textView == null) {
			return;
		}

		View viewParent = (View) textView.getParent();
		if (viewParent == null) {
			return;
		}

		boolean isVisible = !StringUtils.isEmpty(text);
		if (isVisible) {
			textView.setText(text);
		}
		setVisibility(isVisible, viewParent);
	}

	public static void showSoftKeyboard(@Nullable View view) {
		if (view == null) {
			return;
		}

		InputMethodManager inputManager = getInputMethodManager(view.getContext());

		if (inputManager != null) {
			inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
			inputManager.showSoftInput(view, 0);
		}
	}

	public static void showSoftKeyboard(@Nullable EditText inputEditText) {
		if (inputEditText == null) {
			return;
		}

		InputMethodManager inputMethodManager = getInputMethodManager(inputEditText.getContext());
		if (inputMethodManager != null) {
			inputMethodManager.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT);
		}
	}

	@Nullable
	private static InputMethodManager getInputMethodManager(@Nullable Context context) {
		if (context == null) {
			return null;
		}
		return ContextCompat.getSystemService(context, InputMethodManager.class);
	}

	@IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Visibility {
	}
}
