package ru.starksoft.commons.snackbar;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import ru.starksoft.commons.R;
import ru.starksoft.commons.ui.ViewUtils;

public final class ColoredSnackBar {

	private ColoredSnackBar() {
		throw new UnsupportedOperationException();
	}

	@NonNull
	public static Snackbar info(@NonNull Snackbar snackbar) {
		return colorSnackBar(snackbar, getColorFromRes(snackbar, R.color.snackbar_info));
	}

	@NonNull
	public static Snackbar warning(@NonNull Snackbar snackbar) {
		return colorSnackBar(snackbar, getColorFromRes(snackbar, R.color.snackbar_warning));
	}

	@NonNull
	public static Snackbar alert(@NonNull Snackbar snackbar) {
		return colorSnackBar(snackbar, getColorFromRes(snackbar, R.color.snackbar_alert));
	}

	@NonNull
	public static Snackbar confirm(@NonNull Snackbar snackbar) {
		return colorSnackBar(snackbar, getColorFromRes(snackbar, R.color.snackbar_confirm));
	}

	@NonNull
	private static View getSnackBarLayout(@NonNull Snackbar snackbar) {
		return snackbar.getView();
	}

	@ColorInt
	private static int getColorFromRes(@NonNull Snackbar snackbar, @ColorRes int resId) {
		return ContextCompat.getColor(snackbar.getView().getContext(), resId);
	}

	@NonNull
	private static Snackbar colorSnackBar(@NonNull Snackbar snackbar, int colorId) {
		View snackBarView = getSnackBarLayout(snackbar);
		snackBarView.setBackgroundColor(colorId);

		TextView textView = snackBarView.findViewById(R.id.snackbar_text);
		textView.setMinimumHeight(ViewUtils.dpToPx(56));
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setTextColor(Color.WHITE);
		textView.setMaxLines(4);

		return snackbar;
	}
}
