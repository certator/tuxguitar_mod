package org.herac.tuxguitar.player.impl.jsa.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class MidiProgramChangeMessage extends MidiShortMessage{
	
	public MidiProgramChangeMessage(int channel,int instrument) throws InvalidMidiDataException{
		this.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument);
		this.setVoice(MidiShortMessage.DEFAULT_VOICE);
		this.setBendMode(MidiShortMessage.DEFAULT_BEND_MODE);
	}
}
