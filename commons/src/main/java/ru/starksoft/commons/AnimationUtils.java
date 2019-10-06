package ru.starksoft.commons;

import android.animation.Animator;
import android.view.View;

import androidx.annotation.NonNull;

public final class AnimationUtils {

	private static final int ANIMATION_DURATION = 300;

	private AnimationUtils() {
		throw new UnsupportedOperationException();
	}

	public static void setVisibility(boolean show, @NonNull View... views) {
		for (View view : views) {
			animateView(show, View.GONE, ANIMATION_DURATION, view);
		}
	}

	public static void setVisibility(boolean show, int duration, @NonNull View... views) {
		for (View view : views) {
			animateView(show, View.GONE, duration, view);
		}
	}

	public static void toggleVisibility(@NonNull View... views) {
		for (View view : views) {
			int visibility = view.getVisibility();

			switch (visibility) {
				case View.GONE:
					animateView(true, View.GONE, ANIMATION_DURATION, view);
					break;
				case View.VISIBLE:
					animateView(false, View.GONE, ANIMATION_DURATION, view);
					break;
			}
		}
	}

	public static void animateViews(final boolean show, @ViewUtils.Visibility final int visibility, View... views) {
		for (View view : views) {
			animateView(show, visibility, ANIMATION_DURATION, view);
		}
	}

	private static void animateView(final boolean show, @ViewUtils.Visibility final int visibility, int duration,
									final @NonNull View view) {
		float viewAlpha = view.getAlpha();
		float alpha = show ? 1.0f : 0f;

		if (show && viewAlpha > 0f) {
			view.setAlpha(0f);
		}

		view.animate().alpha(alpha).setDuration(duration).setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (show) {
					view.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (!show) {
					view.setVisibility(visibility);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
	}
}
