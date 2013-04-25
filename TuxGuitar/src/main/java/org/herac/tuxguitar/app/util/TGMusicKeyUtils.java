package org.herac.tuxguitar.app.util;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.system.language.LanguageLoader;

public class TGMusicKeyUtils {

	public static final String PREFIX_CHORD = "chord";

	public static final String PREFIX_SCALE = "scale";

	public static final String PREFIX_TUNING = "tuning";

	public static final String PREFIX_FRETBOARD = "fretboard";

	public static final String PREFIX_MATRIX = "matrix";

	public static final int MAX_NOTES = 12;

	private static final String[][] DEFAULT_KEY_NAMES = new String[][]{
		{"C","C#","Cb"},
		{"D","D#","Db"},
		{"E","E#","Eb"},
		{"F","F#","Fb"},
		{"G","G#","Gb"},
		{"A","A#","Ab"},
		{"B","B#","Bb"}
	};

	private static final String[] NOTE_NAMES = TGMusicKeyUtils.getSharpKeyNames(PREFIX_TUNING);

	public static String[] getSharpKeyNames(String prefix){
		return new TGMusicKeyNames(true,prefix).getNames();
	}

	public static String[] getFlatKeyNames(String prefix){
		return new TGMusicKeyNames(false,prefix).getNames();
	}

	protected static void loadKeyNames(String[] names,String prefix,boolean sharp){
		if(sharp){
			loadSharpKeyNames(names, prefix);
		}else{
			loadFlatKeyNames(names, prefix);
		}
	}

	private static void loadSharpKeyNames(String[] names,String prefix){
		names[0] = getName(prefix,0,0);
		names[1] = getName(prefix,0,1);
		names[2] = getName(prefix,1,0);
		names[3] = getName(prefix,1,1);
		names[4] = getName(prefix,2,0);
		names[5] = getName(prefix,3,0);
		names[6] = getName(prefix,3,1);
		names[7] = getName(prefix,4,0);
		names[8] = getName(prefix,4,1);
		names[9] = getName(prefix,5,0);
		names[10] = getName(prefix,5,1);
		names[11] = getName(prefix,6,0);
	}

	private static void loadFlatKeyNames(String[] names,String prefix){
		names[0] = getName(prefix,0,0);
		names[1] = getName(prefix,1,2);
		names[2] = getName(prefix,1,0);
		names[3] = getName(prefix,2,2);
		names[4] = getName(prefix,2,0);
		names[5] = getName(prefix,3,0);
		names[6] = getName(prefix,4,2);
		names[7] = getName(prefix,4,0);
		names[8] = getName(prefix,5,2);
		names[9] = getName(prefix,5,0);
		names[10] = getName(prefix,6,2);
		names[11] = getName(prefix,6,0);
	}

	/**
	 * Convert an English notation note into TG value. For example "C0" became 0
	 * @return Value of the note
	 * @throws IllegalArgumentException
	 */
	public static int getTgValueFromName(String name) {
		return getMidiValueFromName(name) - 24;
	}

	/**
	 * Convert an English notation note into MIDI value. For example "C4" became 72, or "A0" became 33. 
	 * @param name An English notation name ("C4", "Bb4"...)
	 * @return Value of the note
	 * @throws IllegalArgumentException
	 */
	public static int getMidiValueFromName(String name) {
		if (name == null || name.length() <= 1 || name.length() > 4) {
			throw new IllegalArgumentException("Note '" + name + "' is not valid");
		}

		char note = name.charAt(0);
		if (note >= 'a') {
			// upper char
			note -= ('a' - 'A');
		}
		
		if (note < 'A' || note > 'G') {
			throw new IllegalArgumentException("Note '" + name + "' is not valid");
		}
		
		byte noteValue = (byte) ((note - 'A') * 2);
		// half-tone between B-C and E-F
		if (noteValue >= 10) noteValue -= 2;
		else if (noteValue >= 4) noteValue -= 1;
		// C is the first note of the octave
		noteValue = (byte) ((noteValue + 9) % 12);

		char octave = name.charAt(name.length() - 1);
		if (octave < '0' || octave > '9') {
			throw new IllegalArgumentException("Note '" + name + "' is not valid");
		}
		byte octaveValue = (byte) (octave - '0');

		if (name.length() >= 3) {
			char alteration = name.charAt(1);
			if (alteration == 'b') {
				noteValue -= 1;
			} else if (alteration == '#') {
				noteValue += 1;
			} else if (alteration == '-' && name.length() == 3) {
				// negative sign of the octave
				octaveValue = (byte) -octaveValue;
			} else {
				throw new IllegalArgumentException("Note '" + name + "' is not valid");
			}
		}
		if (name.length() == 4) {
			char neg = name.charAt(2);
			if (neg != '-') {
				throw new IllegalArgumentException("Note '" + name + "' is not valid");
			}
			octaveValue = (byte) -octaveValue;
		}

		int value = 24 + noteValue + octaveValue * 12;
		if (value < 0 || value >= 128) {
			throw new IllegalArgumentException("Note value '" + name + "' is out of range. Found '" + value + "'");
		}
		return value;
	}

	private static String getName(String prefix,int key,int signature){
		String resource = ("key." + prefix + "." + key + "." + signature);
		return TuxGuitar.instance().getLanguageManager().getProperty(resource,DEFAULT_KEY_NAMES[key][signature]);
	}

	public static String getNoteName(int value) {
		return NOTE_NAMES[ value % MAX_NOTES ] + (value / MAX_NOTES);
	}
}

class TGMusicKeyNames implements LanguageLoader{

	private final boolean sharp;
	private final String prefix;
	private final String[] names;

	public TGMusicKeyNames(boolean sharp,String prefix){
		this.sharp = sharp;
		this.prefix = prefix;
		this.names = new String[12];
		this.loadProperties();

		TuxGuitar.instance().getLanguageManager().addLoader(this);
	}

	public String[] getNames(){
		return this.names;
	}

	@Override
	public void loadProperties() {
		TGMusicKeyUtils.loadKeyNames(this.names, this.prefix, this.sharp);
	}
}
