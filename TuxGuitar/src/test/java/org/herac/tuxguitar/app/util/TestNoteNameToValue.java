package org.herac.tuxguitar.app.util;

import org.junit.Assert;
import org.junit.Test;

public class TestNoteNameToValue {

	@Test
	public void validNote() {
		Assert.assertEquals(33, TGMusicKeyUtils.getMidiValueFromName("A0"));
		Assert.assertEquals(24, TGMusicKeyUtils.getMidiValueFromName("C0"));
		Assert.assertEquals(31, TGMusicKeyUtils.getMidiValueFromName("G0"));
		Assert.assertEquals(52, TGMusicKeyUtils.getMidiValueFromName("E2"));
		Assert.assertEquals(53, TGMusicKeyUtils.getMidiValueFromName("F2"));
	}

	@Test
	public void validOctave() {
		Assert.assertEquals(59, TGMusicKeyUtils.getMidiValueFromName("B2"));
		Assert.assertEquals(12, TGMusicKeyUtils.getMidiValueFromName("C-1"));
		Assert.assertEquals(91, TGMusicKeyUtils.getMidiValueFromName("G5"));
	}

	@Test
	public void validAlteration() {
		Assert.assertEquals(56, TGMusicKeyUtils.getMidiValueFromName("Ab2"));
		Assert.assertEquals(13, TGMusicKeyUtils.getMidiValueFromName("C#-1"));
		Assert.assertEquals(92, TGMusicKeyUtils.getMidiValueFromName("G#5"));
	}

	@Test
	public void wrongBigSize() throws IllegalArgumentException {
		try {
			TGMusicKeyUtils.getMidiValueFromName("C#-2m2");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void wrongSmallSize() throws IllegalArgumentException {
		try {
			TGMusicKeyUtils.getMidiValueFromName("C");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void wrongValue() throws IllegalArgumentException {
		try {
			// this note is valid but out of range 0..127
			TGMusicKeyUtils.getMidiValueFromName("B-3");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void wrongValue2() throws IllegalArgumentException {
		try {
			// this note is valid but out of range 0..127
			TGMusicKeyUtils.getMidiValueFromName("A8");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void wrongNote() throws IllegalArgumentException {
		try {
			TGMusicKeyUtils.getMidiValueFromName("H0");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void wrongOctave() throws IllegalArgumentException {
		try {
			TGMusicKeyUtils.getMidiValueFromName("Am");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void wrongAlteration() throws IllegalArgumentException {
		try {
			TGMusicKeyUtils.getMidiValueFromName("C@-1");
			Assert.fail();
		} catch (IllegalArgumentException e) {
		}
	}

}
