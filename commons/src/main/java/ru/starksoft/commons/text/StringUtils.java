package ru.starksoft.commons.text;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

@SuppressWarnings("WeakerAccess")
public class StringUtils {
	public static final String STRING_BULLET = "•";
	public static final String STRING_ARROW = "→";
	public static final String STRING_DASH = "—";
	public static final String STRING_NBSP = " ";

	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));

	static {
		final String symbol = VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP ? "\u20BD" : "Р";

		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
		decimalFormatSymbols.setCurrencySymbol(symbol);
		decimalFormatSymbols.setGroupingSeparator(' ');
		decimalFormatSymbols.setMonetaryDecimalSeparator(',');
		((DecimalFormat) NUMBER_FORMAT).setDecimalFormatSymbols(decimalFormatSymbols);

		NUMBER_FORMAT.setMaximumFractionDigits(2);
		NUMBER_FORMAT.setMinimumFractionDigits(0);
	}

	private StringUtils() {
		throw new UnsupportedOperationException();
	}

	@NonNull
	public static String cutString(@NonNull String str, int length) {
		if (str.length() > length) {
			str = str.substring(0, length) + (str.length() > length ? "…" : "");
		}
		return str;
	}

	@NonNull
	public static JSONObject stringToJSONObject(@NonNull String str) throws JSONException {
		return isEmptyResponse(str) ? new JSONObject("{}") : new JSONObject(str);
	}

	public static String pluralForm(int n, String form1, String form2, String form3) {
		n = Math.abs(n) % 100;
		int n1 = n % 10;
		if (n > 10 && n < 20) {
			return form3;
		} else if (n1 > 1 && n1 < 5) {
			return form2;
		} else if (n1 == 1) {
			return form1;
		}
		return form3;
	}

	public static int pluralForm(int n, int form1, int form2, int form3) {
		n = Math.abs(n) % 100;
		int n1 = n % 10;
		if (n > 10 && n < 20) {
			return form3;
		} else if (n1 > 1 && n1 < 5) {
			return form2;
		} else if (n1 == 1) {
			return form1;
		}
		return form3;
	}

	public static boolean isEmptyResponse(@Nullable CharSequence str) {
		return str == null || str.length() == 0 || str.equals("null");
	}

	@NonNull
	public static String capitalizeForced(@NonNull String str) {
		if (isEmpty(str)) {
			return "";
		}
		return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
	}

	@NonNull
	public static String capitalize(@NonNull String str) {
		if (isEmpty(str)) {
			return "";
		}
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	@NonNull
	public static String decapitalize(@NonNull String str) {
		if (isEmpty(str)) {
			return "";
		}
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

	public static boolean isNumeric(@Nullable String str) {
		if (isEmpty(str)) {
			return false;
		}

		ParsePosition pos = new ParsePosition(0);
		NUMBER_FORMAT.parse(str, pos);
		return str.length() == pos.getIndex();
	}

	@NonNull
	public static String convertEmptyResponse(@NonNull String str) {
		return isEmptyResponse(str) ? "" : str;
	}

	public static boolean containsString(@Nullable String stringWhere, @Nullable String stringWhat) {
		return !(stringWhat == null || stringWhere == null) && stringWhere.toLowerCase().contains(stringWhat.toLowerCase());
	}

	public static boolean containsStringStart(@Nullable String stringWhere, @Nullable String stringWhat) {
		return !(stringWhat == null || stringWhere == null) && stringWhere.toLowerCase().startsWith(stringWhat.toLowerCase());
	}

	@NonNull
	public static String formatPrice(@NonNull Number number) {
		return NUMBER_FORMAT.format(number);
	}

	@NonNull
	public static String formatPrice(@NonNull Number number, int minimumFractionDigits) {
		int prevMinimumFractionDigits = NUMBER_FORMAT.getMinimumFractionDigits();
		int prevMaximumFractionDigits = NUMBER_FORMAT.getMaximumFractionDigits();
		if (minimumFractionDigits > prevMaximumFractionDigits) {
			minimumFractionDigits = prevMaximumFractionDigits;
		}

		NUMBER_FORMAT.setMinimumFractionDigits(minimumFractionDigits);
		try {
			return NUMBER_FORMAT.format(number);
		} finally {
			NUMBER_FORMAT.setMinimumFractionDigits(prevMinimumFractionDigits);
		}
	}

	@NonNull
	public static String formatPrice(@Nullable String possibleNumber) {
		double number = 0.0f;
		if (!isEmpty(possibleNumber)) {
			number = Double.valueOf(possibleNumber);
		}
		return NUMBER_FORMAT.format(number);
	}

	@NonNull
	public static CharSequence highlight(@Nullable String search, @NonNull String originalText) {
		if (TextUtils.isEmpty(search) || TextUtils.isEmpty(search.trim())) {
			return originalText;
		}

		// ignore case and accents the same thing should have been done for the search text
		String normalizedText =
				Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
		search = search.toLowerCase();

		int start = normalizedText.indexOf(search);
		if (start < 0) {
			// not found, nothing to to highlight
			return originalText;
		}

		// highlight each appearance in the original text while searching in normalized text
		Spannable highlighted = new SpannableString(originalText);
		while (start >= 0) {
			int spanStart = Math.min(start, originalText.length());
			int spanEnd = Math.min(start + search.length(), originalText.length());
			highlighted.setSpan(new StyleSpan(Typeface.BOLD), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			start = normalizedText.indexOf(search, spanEnd);
		}
		return highlighted;
	}

	public static Spannable revertSpanned(@NonNull Spanned stext) {
		Object[] spans = stext.getSpans(0, stext.length(), Object.class);
		Spannable ret = Spannable.Factory.getInstance().newSpannable(stext.toString());
		if (spans != null && spans.length > 0) {
			for (int i = spans.length - 1; i >= 0; --i) {
				ret.setSpan(spans[i], stext.getSpanStart(spans[i]), stext.getSpanEnd(spans[i]), stext.getSpanFlags(spans[i]));
			}
		}
		return ret;
	}

	@NonNull
	public static StringBuilder removeLastSeparator(@NonNull StringBuilder sb, @NonNull String separator) {
		if (sb.length() > separator.length() && sb.substring(sb.length() - separator.length()).equals(separator)) {
			sb.setLength(sb.length() - separator.length());
		}
		return sb;
	}

	public static void addStringBuilderSeparator(@NonNull StringBuilder sb, @NonNull String separator) {
		final String str = sb.toString();
		if (!str.endsWith(separator) && !str.endsWith("\n") && str.length() > 0) {
			sb.append(separator);
		}
	}

	/**
	 * Returns a string containing the tokens joined by delimiters.
	 * <p>
	 * Ignores null and empty entries
	 *
	 * @param tokens an array object to be joined. Strings will be formed from the objects by calling object.toString().
	 */
	@Nullable
	public static String join(@Nullable CharSequence delimiter, @Nullable Iterable tokens) {
		if (tokens == null || delimiter == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean firstTime = true;
		for (Object token : tokens) {
			if (token == null || isEmpty(token.toString())) {
				continue;
			}

			if (firstTime) {
				firstTime = false;
			} else {
				sb.append(delimiter);
			}
			sb.append(token);
		}

		if (sb.length() == 0) {
			return null;
		}
		return sb.toString();
	}

	@Nullable
	public static String join(CharSequence delimiter, String... tokens) {
		return join(delimiter, Arrays.asList(tokens));
	}

	@NonNull
	public static String getFirstLetter(@NonNull String str) {
		return str.substring(0, 1);
	}

	public static boolean isEmpty(@Nullable CharSequence str) {
		return str == null || str.length() == 0;
	}

	/**
	 * Validates email
	 *
	 * @param email email to check
	 * @return true if email is valid
	 */
	public static boolean isValidEmail(@NonNull CharSequence email) {
		return !isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	@NonNull
	public static String formatDigit(@NonNull Number digit) {
		return String.format(Locale.US, "%,d", digit).replace(',', ' ');
	}

	@NonNull
	public static String toLowerCase(@NonNull String str) {
		if (isEmpty(str)) {
			return str;
		}
		return str.toLowerCase();
	}

	@NonNull
	public static List<Integer> findWord(@NonNull String textString, @NonNull String word) {
		List<Integer> indexes = new ArrayList<>();
		String lowerCaseTextString = textString.toLowerCase();
		String lowerCaseWord = word.toLowerCase();
		int wordLength = 0;

		int index = 0;
		while (index != -1) {
			index = lowerCaseTextString.indexOf(lowerCaseWord, index + wordLength);
			if (index != -1) {
				indexes.add(index);
			}
			wordLength = word.length();
		}
		return indexes;
	}
}
