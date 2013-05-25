package org.herac.tuxguitar.player.impl.midiport.coreaudio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.herac.tuxguitar.player.base.MidiControllers;
import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiReceiver;

public class MidiReceiverImpl extends MidiReceiverJNI implements MidiReceiver{
	private boolean open; // unncessary
    private boolean connected;	
	private final List<MidiOutputPort> ports;
	
	public MidiReceiverImpl(){
		this.ports = new ArrayList<MidiOutputPort>();	
        this.connected = false;
	}

	@Override
	public void open(){
		super.open();
		this.open = true;
	}

	@Override
	public void close(){
		if(this.isOpen()){
			this.disconnect();
			super.close();
			this.open = false;
		}
	}	
		
	public boolean isOpen(){
		return (this.open);
	}	
			
	public boolean isConnected(){
		return (this.isOpen() && this.connected);
	}	
			
    public void connect(){
        if(isOpen()){
            if(!isConnected()){             
                this.connected = true;
                this.openDevice();
            }
        }
    }

	public void disconnect() {
		if(isConnected()){
			this.closeDevice();
			this.connected = false;
		}
	}		
		
	public List<MidiOutputPort> listPorts(){
		if(isOpen()){
			this.ports.clear();
			this.ports.add(new MidiPortImpl(this, "Core Audio midi playback" , "coreaudio" ));
			return this.ports;
		}
		return Collections.emptyList();
	}		

	public void sendSystemReset() {
		if(isOpen()){
			//not implemented
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
		if(isOpen()){
			super.controlChange(channel, controller, value);
		}
	}

	@Override
	public void sendNoteOff(int channel, int key, int velocity) {
		if(isOpen()){
			super.noteOff(channel, key, velocity);
		}
	}

	@Override
	public void sendNoteOn(int channel, int key, int velocity) {
		if(isOpen()){
			super.noteOn(channel, key, velocity);
		}
	}

	@Override
	public void sendPitchBend(int channel, int value) {
		if(isOpen()){
			super.pitchBend(channel, value);
		}
	}

	@Override
	public void sendProgramChange(int channel, int value) {
		if(isOpen()){
			super.programChange(channel, value);
		}
	}
}
