package ru.starksoft.commons.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public final class TextInputLayoutUtils {

    private static final String TAG = "TextInputLayoutUtils";

    private TextInputLayoutUtils() {
        throw new UnsupportedOperationException();
    }

    public static void setEnabled(boolean enabled, boolean resetError, @NonNull View... views) {
        for (View v : views) {
            v.setEnabled(enabled);

            if (v instanceof TextInputLayout) {
                EditText editText = ((TextInputLayout) v).getEditText();
                if (editText != null) {
                    editText.setEnabled(enabled);
                }

                if (resetError) {
                    resetError((TextInputLayout) v);
                }
            }
        }
    }

    public static void resetError(TextInputLayout textInputLayout) {
        if (textInputLayout != null && !TextUtils.isEmpty(textInputLayout.getError())) {
            textInputLayout.setError(null);
            // Хак, чтобы убрать отступы у TextInputLayout
            textInputLayout.setErrorEnabled(false);
        }
    }

    public static boolean setErrorWithValidation(@NonNull TextInputLayout textInputLayout,
                                                 String text,
                                                 String errorText,
                                                 boolean reEnterTextWithTrim) {
        if (reEnterTextWithTrim) {
            text = setTextWithTrim(textInputLayout, text);
        }
        return setErrorWithValidation(textInputLayout, text, errorText, false, null);
    }

    public static boolean setErrorWithValidation(@NonNull TextInputLayout textInputLayout,
                                                 String text,
                                                 String errorText) {
        return setErrorWithValidation(textInputLayout, text, errorText, false, null);
    }

    public static boolean setErrorWithValidation(@NonNull TextInputLayout textInputLayout,
                                                 String text,
                                                 String errorText,
                                                 boolean condition,
                                                 String errorOnConditionText) {
        boolean validated = false;
        boolean isEmpty = TextUtils.isEmpty(text);
        textInputLayout.setErrorEnabled((isEmpty || condition));

        if (TextUtils.isEmpty(text)) {
            textInputLayout.setError(errorText);
        } else if (condition) {
            textInputLayout.setError(errorOnConditionText);
        } else {
            textInputLayout.setError(null);
            validated = true;
        }
        return validated;
    }

    public static String getEditTextTextSafe(@NonNull TextInputLayout textInputLayout) {
        EditText editText = textInputLayout.getEditText();
        if (!(editText instanceof TextInputEditText)) {
            Log.d(
                    TAG,
                    "getEditTextTextSafe: Edit text is not a valid TextInputEditText class, consider to use TextInputEditText as a child of TextInputLayout"
            );
        }

        if (editText == null || editText.getText() == null) {
            return "";
        }

        return editText.getText().toString();
    }

    public static String setTextWithTrim(@NonNull TextInputLayout textInputLayout, String input) {
        if (!TextUtils.isEmpty(input) && textInputLayout.getEditText() != null) {
            input = input.trim();
            textInputLayout.getEditText().setText(input);
            textInputLayout.getEditText().setSelection(input.length());
        }
        return input;
    }

    public static class Validator {

        private String emptyText;
        private String errorMessage;

        public static Validator newValidator() {
            return new Validator();
        }

        public Validator withEmptyText(String emptyText) {
            this.emptyText = emptyText;
            return this;
        }

        public Validator withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        /**
         * Checks the textInputLayout text is not empty
         *
         * @param textInputLayout
         * @return
         */
        public boolean validate(TextInputLayout textInputLayout) {
            return setErrorWithValidation(textInputLayout, getEditTextTextSafe(textInputLayout).trim(), emptyText);
        }

        /**
         * Checks the textInputLayout text is not empty and the condition is false
         *
         * @param textInputLayout - TextInputLayout to play with
         * @param condition       - Condition that must be true to show errorMessage text
         * @return true if
         */
        public boolean validate(TextInputLayout textInputLayout, boolean condition) {
            return setErrorWithValidation(
                    textInputLayout,
                    getEditTextTextSafe(textInputLayout).trim(),
                    emptyText,
                    condition,
                    errorMessage
            );
        }
    }
}
