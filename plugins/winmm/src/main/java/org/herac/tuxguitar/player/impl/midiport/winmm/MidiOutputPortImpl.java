package org.herac.tuxguitar.player.impl.midiport.winmm;

import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiReceiver;

public class MidiOutputPortImpl implements MidiOutputPort{

	private int device;
	private String name;
	private MidiReceiverImpl receiver;

	public MidiOutputPortImpl(MidiSystem midiSystem,String name,int device){
		this.name = name;
		this.device = device;
		this.receiver = new MidiReceiverImpl(this,midiSystem);
	}

	@Override
	public void open(){
		if(!this.receiver.isConnected()){
			this.receiver.connect();
		}
	}

	@Override
	public void close(){
		this.receiver.disconnect();
	}

	@Override
	public MidiReceiver getReceiver(){
		this.open();
		return this.receiver;
	}

	@Override
	public void check(){
		// Not implemented
	}

	public int getDevice() {
		return this.device;
	}

	public static String toString(int device){
		return (Integer.toString(device));
	}

	@Override
	public String getKey() {
		return (Integer.toString(this.device));
	}

	@Override
	public String getName() {
		return this.name;
	}
}