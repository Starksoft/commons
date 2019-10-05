package ru.starksoft.commons;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public final class JoinStringBuilder {

	private final List<Element> elementsList = new ArrayList<>();

	@NonNull
	public static JoinStringBuilder newInstance() {
		return new JoinStringBuilder();
	}

	@NonNull
	public JoinStringBuilder addWithComma(@Nullable String element) {
		elementsList.add(new Element(element, ", "));
		return this;
	}

	public JoinStringBuilder add(@Nullable String element, @NonNull String delimiter) {
		elementsList.add(new Element(element, delimiter));
		return this;
	}

	@NonNull
	public JoinStringBuilder add(@Nullable String element) {
		return add(element, "");
	}

	@NonNull
	public String build() {
		StringBuilder result = new StringBuilder();

		int index = -1;
		for (int i = 0; i < elementsList.size(); i++) {
			Element element = elementsList.get(i);
			String elementText = element.getText();

			if (!StringUtils.isEmptyResponse(elementText)) {
				if (index >= 0) {
					Element elementNew = elementsList.get(index);
					result.append(elementNew.join());
					result.append(getResult(i, i));
				} else if (elementsList.size() == 1) {
					result.append(elementText);
				} else if (result.length() == 0 && i == elementsList.size() - 1) {
					result.append(elementText);
				}
				index = i;

			} else if (index >= 0) {
				result.append(getResult(i, index));
			}
		}
		return result.toString();
	}

	@NonNull
	private String getResult(int i, int index) {
		String result = "";
		if (i == elementsList.size() - 1) {
			result += elementsList.get(index).getText();
		}
		return result;
	}

	private static class Element {
		@Nullable private final String text;
		@NonNull private final String delimiter;

		private Element(@Nullable String text, @NonNull String delimiter) {
			this.text = text;
			this.delimiter = delimiter;
		}

		@NonNull
		public String join() {
			return text + delimiter;
		}

		@Override
		public String toString() {
			return "Element{" + "text='" + text + '\'' + ", delimiter='" + delimiter + '\'' + '}';
		}

		@Nullable
		public String getText() {
			return text;
		}

		@NonNull
		public String getDelimiter() {
			return delimiter;
		}
	}
}
