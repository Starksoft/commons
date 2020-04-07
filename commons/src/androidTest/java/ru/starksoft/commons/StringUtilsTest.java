package ru.starksoft.commons;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class StringUtilsTest extends TestCase {

	private static final String RUBLE_SYMBOL = "\u20BD";

	@Test
	public void formatNumberFromString() throws Exception {
		String result = StringUtils.formatPrice("100000.21");
		assertEquals("100 000,21 " + RUBLE_SYMBOL, result);

		result = StringUtils.formatPrice("");
		assertEquals("0 " + RUBLE_SYMBOL, result);

		result = StringUtils.formatPrice("0.1");
		assertEquals("0,1 " + RUBLE_SYMBOL, result);
	}

	@Test
	public void formatPrice() throws Exception {
		String ret = StringUtils.formatPrice(100000.21);
		assertEquals("100 000,21 \u20BD", ret);

		ret = StringUtils.formatPrice(2000000.123456789);
		assertEquals("2 000 000,12 \u20BD", ret);

		ret = StringUtils.formatPrice(-1);
		assertEquals("-1 \u20BD", ret);

		ret = StringUtils.formatPrice(100.00);
		assertEquals("100 \u20BD", ret);

		ret = StringUtils.formatPrice(83.50);
		assertEquals("83,5 \u20BD", ret);
	}

	@Test
	public void formatPrice2() {
		String ret = StringUtils.formatPrice(100000.21);
		assertEquals("100 000,21 \u20BD", ret);

		ret = StringUtils.formatPrice(2000000.123456789);
		assertEquals("2 000 000,12 \u20BD", ret);

		ret = StringUtils.formatPrice(-1, 2);
		assertEquals("-1,00 \u20BD", ret);

		ret = StringUtils.formatPrice(100.00, 2);
		assertEquals("100,00 \u20BD", ret);

		ret = StringUtils.formatPrice(83.50, 2);
		assertEquals("83,50 \u20BD", ret);

		ret = StringUtils.formatPrice(83.50, 3);
		assertEquals("83,50 \u20BD", ret);
	}

	@Test
	public void cutString() throws Exception {
		String ret = StringUtils.cutString("123", 1);
		assertEquals("1…", ret);

		ret = StringUtils.cutString("123", 0);
		assertEquals("…", ret);
	}

	@Test
	public void stringToJSONObject() throws Exception {
		JSONObject ret = StringUtils.stringToJSONObject("{'a':1,'b':2}");
		assertEquals(new JSONObject("{'a':1,'b':2}").toString(), ret.toString());

		ret = StringUtils.stringToJSONObject(null);
		assertEquals(new JSONObject("{}").toString(), ret.toString());

		ret = StringUtils.stringToJSONObject("null");
		assertEquals(new JSONObject("{}").toString(), ret.toString());
	}

	@Test
	public void pluralForm() throws Exception {
		String one = "штука";
		String two = "штуки";
		String three = "штук";

		String ret = StringUtils.pluralForm(0, one, two, three);
		assertEquals(three, ret);

		ret = StringUtils.pluralForm(1, one, two, three);
		assertEquals(one, ret);

		ret = StringUtils.pluralForm(3, one, two, three);
		assertEquals(two, ret);

		ret = StringUtils.pluralForm(785, one, two, three);
		assertEquals(three, ret);

		ret = StringUtils.pluralForm(-7, one, two, three);
		assertEquals(three, ret);
	}

	@Test
	public void pluralForm1() throws Exception {
		int id1 = 1;
		int id2 = 2;
		int id3 = 3;

		int ret = StringUtils.pluralForm(0, id1, id2, id3);
		assertEquals(id3, ret);

		ret = StringUtils.pluralForm(1, id1, id2, id3);
		assertEquals(id1, ret);

		ret = StringUtils.pluralForm(3, id1, id2, id3);
		assertEquals(id2, ret);

		ret = StringUtils.pluralForm(785, id1, id2, id3);
		assertEquals(id3, ret);

		ret = StringUtils.pluralForm(-7, id1, id2, id3);
		assertEquals(id3, ret);
	}

	@Test
	public void isEmptyResponse() throws Exception {
		boolean ret = StringUtils.isEmptyResponse(null);
		Assert.assertTrue(ret);

		ret = StringUtils.isEmptyResponse("null");
		Assert.assertTrue(ret);

		ret = StringUtils.isEmptyResponse("");
		Assert.assertTrue(ret);
	}

	@Test
	public void capitalizeForced() throws Exception {
		String ret = StringUtils.capitalizeForced("UPPERCASE");
		assertEquals("Uppercase", ret);
	}

	@Test
	public void capitalize() throws Exception {
		String ret = StringUtils.capitalize("dIfFeRrentCase");
		assertEquals("DIfFeRrentCase", ret);

		ret = StringUtils.capitalize("a");
		assertEquals("A", ret);
	}

	@Test
	public void decapitalize() throws Exception {
		String ret = StringUtils.decapitalize("UPPERCASE");
		assertEquals("uPPERCASE", ret);
	}

	@Test
	public void toLowerCase() throws Exception {
		String ret = StringUtils.toLowerCase("UPPERCASE");
		assertEquals("uppercase", ret);
	}

	@Test
	public void isNumeric() throws Exception {
		boolean ret = StringUtils.isNumeric("0");
		assertTrue(ret);

		ret = StringUtils.isNumeric("NaN");
		assertFalse(ret);
	}

	@Test
	public void convertEmptyResponse() throws Exception {
		String ret = StringUtils.convertEmptyResponse("123");
		assertEquals("123", ret);

		ret = StringUtils.convertEmptyResponse("null");
		assertEquals("", ret);

		ret = StringUtils.convertEmptyResponse(null);
		assertEquals("", ret);

		ret = StringUtils.convertEmptyResponse("");
		assertEquals("", ret);
	}

	@Test
	public void containsString() throws Exception {
		boolean ret = StringUtils.containsString("0233453", "233");
		Assert.assertTrue(ret);

		ret = StringUtils.containsString("0233453", "-");
		assertFalse(ret);
	}

	@Test
	public void containsStringStart() throws Exception {
		boolean ret = StringUtils.containsStringStart("0233453", "0233");
		Assert.assertTrue(ret);

		ret = StringUtils.containsStringStart("0233453", "-");
		assertFalse(ret);
	}

	@Test
	public void formatDigit() throws Exception {
		String ret = StringUtils.formatDigit(123);
		assertEquals("123", ret);

		ret = StringUtils.formatDigit(1000);
		assertEquals("1 000", ret);

		ret = StringUtils.formatDigit(-71000);
		assertEquals("-71 000", ret);

		ret = StringUtils.formatDigit(100000);
		assertEquals("100 000", ret);

		ret = StringUtils.formatDigit(1000000);
		assertEquals("1 000 000", ret);
	}

	@Test
	public void highlight() throws Exception {
		CharSequence highlighted = StringUtils.highlight("search", "String where to search");

		assertEquals(SpannableString.class, highlighted.getClass());

		StyleSpan[] styleSpans = ((SpannableString) highlighted).getSpans(16, 22, StyleSpan.class);

		assertNotNull(styleSpans);

		assertThat("Проверяем подсветку искомой строки", "String where to search", is(equalTo(highlighted.toString())));
	}

	@Test
	public void revertSpanned() throws Exception {
		SpannableString spannableString = new SpannableString("String where to revert");
		spannableString.setSpan(new StyleSpan(Typeface.BOLD), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new UnderlineSpan(), 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		Object[] styleSpans = spannableString.getSpans(0, 100, Object.class);
		assertNotNull(styleSpans);

		assertEquals(StyleSpan.class, styleSpans[0].getClass());
		assertEquals(UnderlineSpan.class, styleSpans[1].getClass());

		Spannable highlighted = StringUtils.revertSpanned(spannableString);

		styleSpans = highlighted.getSpans(0, 100, Object.class);
		assertNotNull(styleSpans);

		assertEquals(UnderlineSpan.class, styleSpans[0].getClass());
		assertEquals(StyleSpan.class, styleSpans[1].getClass());
	}

	@Test
	public void removeLastSeparator() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("123");
		sb.append(",");
		sb.append("123");
		sb.append(",");

		StringBuilder sb2 = StringUtils.removeLastSeparator(sb, ",");

		assertEquals("123,123", sb2.toString());
	}

	@Test
	public void addStringBuilderSeparator() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(true);

		StringUtils.addStringBuilderSeparator(sb, ", ");

		sb.append(false);

		assertEquals("true, false", sb.toString());
	}

	@Test
	public void joinIterable() throws Exception {
		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add("a");
		arrayList.add("b");
		arrayList.add(null);

		String out = StringUtils.join(", ", arrayList);
		assertEquals("a, b", out);

		arrayList.clear();
		arrayList.add(null);
		arrayList.add(null);
		arrayList.add(null);
		arrayList.add(null);
		arrayList.add(null);
		arrayList.add("one");
		arrayList.add(null);
		arrayList.add(null);
		arrayList.add(null);
		arrayList.add(null);
		arrayList.add(null);

		out = StringUtils.join(", ", arrayList);
		assertEquals("one", out);
	}

	@Test
	public void isEmpty() throws Exception {
		boolean ret = StringUtils.isEmpty(null);
		Assert.assertTrue(ret);

		ret = StringUtils.isEmpty("");
		Assert.assertTrue(ret);

		ret = StringUtils.isEmpty("123");
		Assert.assertTrue(!ret);
	}

	@Test
	public void isValidEmail() throws Exception {
		boolean ret = StringUtils.isValidEmail("k@m.ru");
		Assert.assertTrue(ret);

		ret = StringUtils.isValidEmail("gsdghdfghjdfghm.ru");
		assertFalse(ret);

		// FIXME Не работает с русскими доменами
		//ret = StringUtils.isValidEmail("админ@домен.рф");
		//assertTrue(ret);

	}

	@Test
	public void getFirstLetter() throws Exception {
		String literal = StringUtils.getFirstLetter("fgwsdhfghd");
		assertEquals(literal, "f");
	}

	@Test
	public void joinStrings() throws Exception {
		String a = "a";
		String b = "b";
		String c = "";

		String out = StringUtils.join(", ", a, b, c, null);
		assertEquals("a, b", out);

		out = StringUtils.join(", ", c, a, null, b);
		assertEquals("a, b", out);

		out = StringUtils.join(", ", c, a, b, null, null, b, a);
		assertEquals("a, b, b, a", out);

		out = StringUtils.join(", ", null, null, null, null, null, null);
		assertEquals(null, out);
	}

	@Test
	public void findWord() {
		List<Integer> words = StringUtils.findWord("{}{}{}{}", "{}");
		assertEquals(4, words.size());

		words = StringUtils.findWord("{}", "{}");
		assertEquals(1, words.size());

		words = StringUtils.findWord("124", "{}");
		assertEquals(0, words.size());
	}
}
