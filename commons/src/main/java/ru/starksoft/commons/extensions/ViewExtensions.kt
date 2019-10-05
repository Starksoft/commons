package ru.obed.client.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import ru.starksoft.commons.extensions.resetError

fun TextView?.addTextChangedListener(onTextChanged: ((text: CharSequence) -> Unit)) {
	this?.addTextChangedListener(object : TextWatcher {
		override fun afterTextChanged(s: Editable?) {
		}

		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
		}

		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			s?.let { onTextChanged.invoke(s) }
		}
	})
}

fun TextInputLayout?.addTextChangedListener(onTextChanged: ((text: CharSequence) -> Unit)) {
	this?.editText?.addTextChangedListener(onTextChanged)
}

fun Array<View>.setEnabled(enabled: Boolean, resetError: Boolean = false) {
	for (view in this) {
		view.isEnabled = enabled

		if (view is TextInputLayout) {
			view.editText?.isEnabled = enabled

			if (resetError) {
				view.resetError()
			}
		}
	}
}

fun Array<View>.setVisibility(visible: Boolean) {
	for (view in this) {
		view.visibility = if (visible) View.VISIBLE else GONE
	}
}

fun View?.setVisibility(visible: Boolean) {
	this?.visibility = if (visible) View.VISIBLE else GONE
}
