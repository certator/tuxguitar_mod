package org.herac.tuxguitar.app.system.tuning;

import org.herac.tuxguitar.song.models.TGString;

public class TuningTemplate {

	/**
	 * Name of the instrument
	 */
	private final String instrument;

	/**
	 * Name of the tuning
	 */
	private final String name;

	/**
	 * Strings of the tuning
	 */
	private final TGString strings[];

	public TuningTemplate(String instrument, String name, TGString strings[]) {
		this.instrument = instrument;
		this.name = name;
		this.strings = strings;
	}

	public String getInstrument() {
		return instrument;
	}

	public String getName() {
		return name;
	}

	public TGString[] getStrings() {
		return strings;
	}

	public int getStringCount() {
		return strings.length;
	}

}
