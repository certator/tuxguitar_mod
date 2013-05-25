package org.herac.tuxguitar.player.impl.midiport.winmm;

import org.herac.tuxguitar.player.base.MidiControllers;
import org.herac.tuxguitar.player.base.MidiReceiver;

public class MidiReceiverImpl implements MidiReceiver{
	
	private boolean connected;
	private MidiOutputPortImpl midiPort;
	private MidiSystem midiSystem;
	
	public MidiReceiverImpl(MidiOutputPortImpl midiPort, MidiSystem midiSystem){
		this.midiPort = midiPort;
		this.midiSystem = midiSystem;
		this.connected = false;
	}
	
	public boolean isConnected(){
		return this.connected;
	}
	
	public void connect(){
		if(!isConnected()){
			this.midiSystem.openPort(this.midiPort);
			this.connected = true;
		}
	}
	
	public void disconnect() {
		if(isConnected()){
			this.midiSystem.closePort();
			this.connected = false;
		}
	}
	
	@Override
	public void sendAllNotesOff() {
		for(int i = 0; i < 16; i ++){
			sendControlChange(i,MidiControllers.ALL_NOTES_OFF,0);
		}
	}
	
	@Override
	public void sendControlChange(int channel, int controller, int value) {
		if(isConnected()){
			this.midiSystem.controlChange(channel, controller, value);
		}
	}
	
	@Override
	public void sendNoteOff(int channel, int key, int velocity) {
		if(isConnected()){
			this.midiSystem.noteOff(channel, key, velocity);
		}
	}
	
	@Override
	public void sendNoteOn(int channel, int key, int velocity) {
		if(isConnected()){
			this.midiSystem.noteOn(channel, key, velocity);
		}
	}
	
	@Override
	public void sendPitchBend(int channel, int value) {
		if(isConnected()){
			this.midiSystem.pitchBend(channel, value);
		}
	}
	
	@Override
	public void sendProgramChange(int channel, int value) {
		if(isConnected()){
			this.midiSystem.programChange(channel, value);
		}
	}
	
	public void sendSystemReset() {
		//not implemented
	}
}
