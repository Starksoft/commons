@file:Suppress("unused")

package ru.starksoft.commons.extensions

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "TextInputLayoutExtensions"

@SuppressLint("LongLogTag")
fun TextInputLayout.getTextSafe(): String {
	val editText = editText
	if (editText !is TextInputEditText) {
		Log.d(
			TAG,
			"getTextSafe: Edit text is not a valid TextInputEditText class, consider to use TextInputEditText as a child of TextInputLayout"
		)
	}

	return if (editText == null || editText.text == null) {
		""
	} else editText.text.trim().toString()
}

fun TextInputLayout?.resetError() {
	val textInputLayout = this

	//textInputLayout?.error?.isNotEmpty() == true
	if (textInputLayout != null && !TextUtils.isEmpty(textInputLayout.error)) {
		textInputLayout.error = null
		// Хак, чтобы убрать отступы у TextInputLayout
		textInputLayout.isErrorEnabled = false
	}
}

fun TextInputLayout.setTextWithTrim(input: String?): String {
	return input?.trim()?.let {
		editText?.setText(it)
		editText?.setSelection(it.length)
		it
	} ?: ""
}

fun TextInputLayout.validateEmpty(resetError: Boolean): Boolean {
	if (resetError) {
		resetError()
	}

	return if (editText?.text.isNullOrBlank()) {
		this.error = "Поле не должно быть пустым или содержать только пробелы"
		false
	} else {
		true
	}
}

fun TextInputLayout.validateRegex(regex: Regex): Boolean {
	return if (!regex.matches(editText?.text.toString())) {
		this.error = "Недопустимый ввод"
		false
	} else {
		true
	}
}
