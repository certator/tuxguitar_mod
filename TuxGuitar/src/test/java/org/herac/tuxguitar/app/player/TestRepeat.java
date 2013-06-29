package org.herac.tuxguitar.app.player;

import junit.framework.Assert;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.player.base.MidiRepeatController;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGSong;
import org.junit.Test;

public class TestRepeat {

	private final TGFactory factory = new TGFactoryImpl();

	private void createMeasureHeader(TGSong song, String name, boolean repeatOpen, int repeatClose, int repeatAlternative) {
		TGMeasureHeader result = new TGMeasureHeader(this.factory);
		result.setRepeatOpen(repeatOpen);
		result.setRepeatClose(repeatClose);
		result.setRepeatAlternative(repeatAlternative);
		result.setMarker(this.factory.newMarker());
		result.getMarker().setTitle(name);

		result.setNumber(song.countMeasureHeaders() + 1);
		song.addMeasureHeader(result);
	}

	private String playAsString(MidiRepeatController player) {
		StringBuilder result = new StringBuilder();

		for (int i = 1; i < 50; i++) {
			if (player.finished()) {
				break;
			}

			TGMeasureHeader header = player.getTGSong().getMeasureHeader(player.getIndex());
			player.process();
			if (player.shouldPlay()) {
				result.append(header.getMarker().getTitle());
			}
		}

		if (!player.finished()) {
			result.append("[truncated]");
		}

		return result.toString();
	}

	@Test
	public void noRepeat() {
		TGSong song = new TGSong();
		createMeasureHeader(song, "A", false, 0, 0);
		createMeasureHeader(song, "B", false, 0, 0);
		createMeasureHeader(song, "C", false, 0, 0);

		MidiRepeatController player = new MidiRepeatController(song, -1, -1);
		String sequence = playAsString(player);
		Assert.assertEquals("ABC", sequence);
	}

	@Test
	public void simpleRepeat() {
		TGSong song = new TGSong();
		createMeasureHeader(song, "A", true, 2, 0);

		MidiRepeatController player = new MidiRepeatController(song, -1, -1);
		String sequence = playAsString(player);
		Assert.assertEquals("AAA", sequence);
	}

	@Test
	public void oneFullRepeat() {
		TGSong song = new TGSong();
		createMeasureHeader(song, "A", true, 0, 0);
		createMeasureHeader(song, "B", false, 0, 0);
		createMeasureHeader(song, "C", false, 1, 0);

		MidiRepeatController player = new MidiRepeatController(song, -1, -1);
		String sequence = playAsString(player);
		Assert.assertEquals("ABCABC", sequence);
	}

	@Test
	public void multiRepeat() {
		TGSong song = new TGSong();
		createMeasureHeader(song, "A", false, 0, 0);
		createMeasureHeader(song, "B", true, 0, 0);
		createMeasureHeader(song, "C", false, 2, 0);
		createMeasureHeader(song, "D", false, 0, 0);

		MidiRepeatController player = new MidiRepeatController(song, -1, -1);
		String sequence = playAsString(player);
		Assert.assertEquals("ABCBCBCD", sequence);
	}

	@Test
	public void eachMeasureRepeat() {
		TGSong song = new TGSong();
		createMeasureHeader(song, "A", true, 1, 0);
		createMeasureHeader(song, "B", true, 1, 0);
		createMeasureHeader(song, "C", true, 1, 0);

		MidiRepeatController player = new MidiRepeatController(song, -1, -1);
		String sequence = playAsString(player);
		Assert.assertEquals("AABBCC", sequence);
	}

	@Test
	public void alternateEndRepeat() {
		TGSong song = new TGSong();
		createMeasureHeader(song, "A", false, 0, 0);
		createMeasureHeader(song, "B", true, 0, 0);
		createMeasureHeader(song, "C", false, 1, 1 << 0);
		createMeasureHeader(song, "D", false, 0, 1 << 1);
		createMeasureHeader(song, "E", false, 0, 0);

		MidiRepeatController player = new MidiRepeatController(song, -1, -1);
		String sequence = playAsString(player);
		Assert.assertEquals("ABCBDE", sequence);
	}

	@Test
	public void manyAlternateEndRepeat() {
		TGSong song = new TGSong();
		createMeasureHeader(song, "A", false, 0, 0);
		createMeasureHeader(song, "B", true, 0, 0);
		createMeasureHeader(song, "C", false, 2, 1 << 0);
		createMeasureHeader(song, "D", false, 0, 1 << 1);
		createMeasureHeader(song, "E", false, 0, 1 << 2);
		createMeasureHeader(song, "F", false, 0, 0);

		MidiRepeatController player = new MidiRepeatController(song, -1, -1);
		String sequence = playAsString(player);
		Assert.assertEquals("ABCBDBEF", sequence);
	}

}
