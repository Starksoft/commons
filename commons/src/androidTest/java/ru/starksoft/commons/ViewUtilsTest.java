package ru.starksoft.commons;

import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ViewUtilsTest {

	@Test
	public void hideViews() {
		View v = new View(ApplicationProvider.getApplicationContext());
		v.setVisibility(View.VISIBLE);
		ViewUtils.hideViews(v);
		assertEquals(v.getVisibility(), View.GONE);
	}

	@Test
	public void showViews() {
		View v = new View(ApplicationProvider.getApplicationContext());
		v.setVisibility(View.GONE);
		ViewUtils.showViews(v);
		assertEquals(v.getVisibility(), View.VISIBLE);
	}

	@Test
	public void setVisibilityType() {
		View v = new View(ApplicationProvider.getApplicationContext());
		v.setVisibility(View.GONE);

		ViewUtils.setVisibility(true, View.INVISIBLE, v);
		assertEquals(v.getVisibility(), View.VISIBLE);

		ViewUtils.setVisibility(false, View.GONE, v);
		assertEquals(v.getVisibility(), View.GONE);

		boolean wasException = false;
		try {
			ViewUtils.setVisibility(false, 0, v);
		} catch (Exception e) {
			wasException = true;
		}
		assertTrue(wasException);
	}

	@Test
	public void setVisibility() {
		View v = new View(ApplicationProvider.getApplicationContext());
		v.setVisibility(View.GONE);
		ViewUtils.setVisibility(true, v);
		assertEquals(v.getVisibility(), View.VISIBLE);
		ViewUtils.setVisibility(false, v);
		assertEquals(v.getVisibility(), View.GONE);
	}

	@Test
	public void setVisibilityKeepingSpace() {
		View v = new View(ApplicationProvider.getApplicationContext());
		v.setVisibility(View.GONE);
		ViewUtils.setVisibilityKeepingSpace(true, v);
		assertEquals(v.getVisibility(), View.VISIBLE);
		ViewUtils.setVisibilityKeepingSpace(false, v);
		assertEquals(v.getVisibility(), View.INVISIBLE);
	}

	@Test
	public void setEnabled() {
		View v = new View(ApplicationProvider.getApplicationContext());
		v.setEnabled(false);

		ViewUtils.setEnabled(true, v);
		assertTrue(v.isEnabled());

		ViewUtils.setEnabled(false, v);
		assertFalse(v.isEnabled());
	}

	@Test
	public void dpToPx() {
//		int ret = ViewUtils.dpToPx(1);
//		assertEquals(4, ret);
	}

	@Test
	public void pxToDp() {
		//		int ret = ViewUtils.pxToDp(ApplicationProvider.getApplicationContext(), 4);
		//		assertEquals(1, ret);
	}

	@Test
	public void isSingleClicked() {
		boolean ret = ViewUtils.isSingleClicked();
		assertTrue(ret);

		ret = ViewUtils.isSingleClicked();
		assertFalse(ret);
	}
}
