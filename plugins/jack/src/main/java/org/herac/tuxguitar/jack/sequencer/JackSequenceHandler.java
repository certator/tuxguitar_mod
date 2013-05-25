package org.herac.tuxguitar.jack.sequencer;

import org.herac.tuxguitar.player.base.MidiSequenceHandler;
import org.herac.tuxguitar.song.models.TGTimeSignature;

public class JackSequenceHandler extends MidiSequenceHandler{
	
	private JackSequencer seq;
	
	public JackSequenceHandler(JackSequencer seq,int tracks) {
		super(tracks);
		this.seq = seq;
		this.seq.getJackTrackController().init(getTracks());
	}
	
	@Override
	public void addControlChange(long tick,int track,int channel, int controller, int value) {
		this.seq.addEvent(JackEvent.controlChange(tick, track, channel, controller, value));
	}
	
	@Override
	public void addNoteOff(long tick,int track,int channel, int note, int velocity) {
		this.seq.addEvent(JackEvent.noteOff(tick, track, channel, note, velocity));
	}
	
	@Override
	public void addNoteOn(long tick,int track,int channel, int note, int velocity) {
		this.seq.addEvent(JackEvent.noteOn(tick, track, channel, note, velocity));
	}
	
	@Override
	public void addPitchBend(long tick,int track,int channel, int value) {
		this.seq.addEvent(JackEvent.pitchBend(tick, track, channel, value));
	}
	
	@Override
	public void addProgramChange(long tick,int track,int channel, int instrument) {
		this.seq.addEvent(JackEvent.programChange(tick, track, channel, instrument));
	}
	
	@Override
	public void addTempoInUSQ(long tick,int track,int usq) {
		this.seq.addEvent(JackEvent.tempoInUSQ(tick, usq));
	}
	
	@Override
	public void addTimeSignature(long tick,int track,TGTimeSignature ts) {
		//not implemented
	}
	
	@Override
	public void notifyFinish(){
		//not implemented
	}
}
