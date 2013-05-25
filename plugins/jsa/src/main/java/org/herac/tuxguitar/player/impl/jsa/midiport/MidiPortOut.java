package org.herac.tuxguitar.player.impl.jsa.midiport;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;

import org.herac.tuxguitar.player.base.MidiControllers;
import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiReceiver;
import org.herac.tuxguitar.player.impl.jsa.utils.MidiMessageUtils;
import org.herac.tuxguitar.util.TGSynchronizer;

public class MidiPortOut implements MidiOutputPort {
	
	private String key;
	private String name;
	private MidiReceiverImpl receiver;
	
	public MidiPortOut(MidiDevice device){
		this.key = device.getDeviceInfo().getName();
		this.name = device.getDeviceInfo().getName();
		this.receiver = new MidiReceiverImpl(device);
	}
	
	@Override
	public MidiReceiver getReceiver(){
		return this.receiver;
	}
	
	@Override
	public void open() throws MidiPlayerException{
		try {
			this.receiver.open();
		} catch (Throwable throwable) {
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}
	}
	
	@Override
	public void close() throws MidiPlayerException{
		try {
			this.receiver.close();
		} catch (Throwable throwable) {
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}
	}
	
	@Override
	public void check() throws MidiPlayerException{
		try {
			this.receiver.open();
		} catch (Throwable throwable) {
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}
	}
	
	@Override
	public String getKey() {
		return this.key;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
}

class MidiReceiverImpl implements MidiReceiver{
	
	private MidiDevice device;
	private Receiver receiver;
	
	public MidiReceiverImpl(MidiDevice device){
		this.device = device;
	}
	
	protected synchronized void open() throws Throwable{
		if(!this.device.isOpen()){
			final MidiDevice device = this.device;
			TGSynchronizer.instance().addRunnable(new TGSynchronizer.TGRunnable() {
				@Override
				public void run() throws Throwable {
					device.open();
				}
			});
		}
		if(this.receiver == null){
			final MidiDevice device = this.device;
			TGSynchronizer.instance().addRunnable(new TGSynchronizer.TGRunnable() {
				@Override
				public void run() throws Throwable {
					setReceiver(device.getReceiver());
				}
			});
		}
	}
	
	protected synchronized void close() throws Throwable{
		if(this.receiver != null){
			final Receiver receiver = this.receiver;
			TGSynchronizer.instance().addRunnable(new TGSynchronizer.TGRunnable() {
				@Override
				public void run() throws Throwable {
					receiver.close();
					setReceiver(null);
				}
			});
		}
		if(this.device.isOpen()){
			final MidiDevice device = this.device;
			TGSynchronizer.instance().addRunnable(new TGSynchronizer.TGRunnable() {
				@Override
				public void run() throws Throwable {
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
			getReceiver().send(MidiMessageUtils.systemReset(),-1);
		}
	}
	
	@Override
	public void sendAllNotesOff(){
		if(getReceiver() != null){
			for(int channel = 0; channel < 16; channel ++){
				getReceiver().send(MidiMessageUtils.controlChange(channel, MidiControllers.ALL_NOTES_OFF,0),-1);
			}
		}
	}
	
	@Override
	public void sendNoteOn(int channel, int key, int velocity) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageUtils.noteOn(channel, key, velocity),-1);
		}
	}
	
	@Override
	public void sendNoteOff(int channel, int key, int velocity) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageUtils.noteOff(channel, key, velocity),-1);
		}
	}
	
	@Override
	public void sendControlChange(int channel, int controller, int value) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageUtils.controlChange(channel,controller, value),-1);
		}
	}
	
	@Override
	public void sendProgramChange(int channel, int value) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageUtils.programChange(channel, value),-1);
		}
	}
	
	@Override
	public void sendPitchBend(int channel, int value) {
		if(getReceiver() != null){
			getReceiver().send(MidiMessageUtils.pitchBend(channel, value),-1);
		}
	}
}