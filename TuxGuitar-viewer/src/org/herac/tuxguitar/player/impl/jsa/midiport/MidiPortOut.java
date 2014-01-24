package org.herac.tuxguitar.player.impl.jsa.midiport;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.herac.tuxguitar.gm.port.GMOutputPort;
import org.herac.tuxguitar.gm.port.GMReceiver;
import org.herac.tuxguitar.player.base.MidiControllers;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.impl.jsa.message.MidiMessageFactory;
import org.herac.tuxguitar.util.TGException;
import org.herac.tuxguitar.util.TGSynchronizer;

public class MidiPortOut extends GMOutputPort {
	
	private String key;
	private String name;
	private MidiReceiverImpl receiver;
	
	public MidiPortOut(MidiDevice device){
		this.key = device.getDeviceInfo().getName();
		this.name = device.getDeviceInfo().getName();
		this.receiver = new MidiReceiverImpl(device);
	}
	
	public GMReceiver getReceiver(){
		return this.receiver;
	}
	
	public void open() throws MidiPlayerException{
		try {
			this.receiver.open();
		} catch (Throwable throwable) {
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}
	}
	
	public void close() throws MidiPlayerException{
		try {
			this.receiver.close();
		} catch (Throwable throwable) {
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}
	}
	
	public void check() throws MidiPlayerException{
		try {
			this.receiver.open();
		} catch (Throwable throwable) {
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getName() {
		return this.name;
	}
}

class MidiReceiverImpl implements GMReceiver{
	
	private MidiDevice device;
	private Receiver receiver;
	
	public MidiReceiverImpl(MidiDevice device){
		this.device = device;
	}
	
	protected synchronized void open() throws Throwable{
		if(!this.device.isOpen()){
			final MidiDevice device = this.device;
			TGSynchronizer.instance().execute(new TGSynchronizer.TGRunnable() {
				public void run() throws TGException {
					try{
						device.open();
					} catch(MidiUnavailableException e){
						throw new TGException(e);
					}
				}
			});
		}
		if(this.receiver == null){
			final MidiDevice device = this.device;
			TGSynchronizer.instance().execute(new TGSynchronizer.TGRunnable() {
				public void run() throws TGException {
					try{
						setReceiver(device.getReceiver());
					} catch(MidiUnavailableException e){
						throw new TGException(e);
					}
				}
			});
		}
	}
	
	protected synchronized void close() throws Throwable{
		if(this.receiver != null){
			final Receiver receiver = this.receiver;
			TGSynchronizer.instance().execute(new TGSynchronizer.TGRunnable() {
				public void run() throws TGException {
					receiver.close();
					setReceiver(null);
				}
			});
		}
		if(this.device.isOpen()){
			final MidiDevice device = this.device;
			TGSynchronizer.instance().execute(new TGSynchronizer.TGRunnable() {
				public void run() throws TGException {
					device.close();
				}
			});
		}
	}
	
	protected void setReceiver(Receiver receiver){
		this.receiver = receiver;
	}
	
	protected Receiver getReceiver(){
		return this.receiver;
	}
	
	public void sendSystemReset(){
		if(getReceiver() != null){
			getReceiver().send(MidiMessageFactory.systemReset(),-1);
		}
	}
	
	public void sendAllNotesOff(){
		if(getReceiver() != null){
			for(int channel = 0; channel < 16; channel ++){
				getReceiver().send(MidiMessageFactory.controlChange(channel, MidiControllers.ALL_NOTES_OFF,0),-1);
			}
		}
	}
	
	public void sendNoteOn(int channel, int key, int velocity) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageFactory.noteOn(channel, key, velocity),-1);
		}
	}
	
	public void sendNoteOff(int channel, int key, int velocity) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageFactory.noteOff(channel, key, velocity),-1);
		}
	}
	
	public void sendControlChange(int channel, int controller, int value) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageFactory.controlChange(channel,controller, value),-1);
		}
	}
	
	public void sendProgramChange(int channel, int value) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageFactory.programChange(channel, value),-1);
		}
	}
	
	public void sendPitchBend(int channel, int value) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageFactory.pitchBend(channel, value),-1);
		}
	}
}