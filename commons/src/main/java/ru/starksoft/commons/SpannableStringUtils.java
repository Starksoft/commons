package ru.starksoft.commons;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import java.text.Normalizer;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class SpannableStringUtils {

	/**
	 * Highlights found case insensitive text in originalText
	 *
	 * @param search       - query string
	 * @param originalText - where to find
	 * @return CharSequence - highlighted SpannableString
	 */
	public static CharSequence highlight(@Nullable String search, String originalText) {
		if (!StringUtils.isEmpty(search) && !StringUtils.isEmpty(search.trim())) {
			// ignore case and accents the same thing should have been done for the search text
			String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD)
					.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
					.toLowerCase();
			String searchLocal = search.toLowerCase(Locale.getDefault());

			int start = normalizedText.indexOf(searchLocal);
			if (start < 0) {
				// not found, nothing to do
				return originalText;
			} else {
				// highlight each appearance in the original text
				// while searching in normalized text
				Spannable highlighted = new SpannableString(originalText);
				while (start >= 0) {
					int spanStart = Math.min(start, originalText.length());
					int spanEnd = Math.min(start + searchLocal.length(), originalText.length());
					highlighted.setSpan(new StyleSpan(Typeface.BOLD), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					start = normalizedText.indexOf(searchLocal, spanEnd);
				}
				return highlighted;
			}
		} else {
			return originalText;
		}
	}

	@NonNull
	public static CharSequence highlightFirst(@Nullable String search, @NonNull String originalText) {
		if (StringUtils.isEmpty(search) || StringUtils.isEmpty(search.trim())) {
			return originalText;
		}

		// ignore case and accents the same thing should have been done for the search text
		String normalizedText =
				Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
		String searchLocal = search.toLowerCase(Locale.getDefault());

		int start = normalizedText.indexOf(searchLocal);
		if (start < 0) {
			return originalText;
		} else {
			Spannable highlighted = new SpannableString(originalText);
			int spanStart = Math.min(start, originalText.length());
			int spanEnd = Math.min(start + searchLocal.length(), originalText.length());
			highlighted.setSpan(new StyleSpan(Typeface.BOLD), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return highlighted;
		}
	}

	public static Spannable revertSpanned(Spanned spanned) {
		Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);
		Spannable ret = Spannable.Factory.getInstance().newSpannable(spanned.toString());
		if (spans != null && spans.length > 0) {
			for (int i = spans.length - 1; i >= 0; --i) {
				ret.setSpan(spans[i], spanned.getSpanStart(spans[i]), spanned.getSpanEnd(spans[i]), spanned.getSpanFlags(spans[i]));
			}
		}
		return ret;
	}

	@NonNull
	public static SpannableString getHighlightedTitle(@Nullable String keyword, String title) {
		CharSequence highlight = SpannableStringUtils.highlightFirst(keyword, title);

		if (!highlight.getClass().isAssignableFrom(SpannableString.class)) {
			highlight = new SpannableString(highlight);
		}
		return (SpannableString) highlight;
	}
}
