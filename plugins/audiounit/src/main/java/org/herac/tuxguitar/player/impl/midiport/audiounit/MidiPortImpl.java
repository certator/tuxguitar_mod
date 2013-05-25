package org.herac.tuxguitar.player.impl.midiport.audiounit;

import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiReceiver;

public class MidiPortImpl implements MidiOutputPort{

	private String key;
	private String name;
	private MidiReceiverImpl receiver;

	public MidiPortImpl(MidiReceiverImpl midiOut,String name,String key){
        this.key = key;
        this.name = name;
		this.receiver = midiOut;
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

	@Override
	public String getKey(){
		return this.key;
	}

	@Override
	public String getName(){
		return this.name;
	}
}