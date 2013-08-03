package org.herac.tuxguitar.player.impl.midiport.alsa;

import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiReceiver;

public class MidiOutputPortImpl implements MidiOutputPort{

	private int port;
	private int client;
	private String clientName;
	private MidiReceiverImpl receiver;

	public MidiOutputPortImpl(MidiSystem midiSystem,String clientName,int client,int port){
		this.port = port;
		this.client = client;
		this.clientName = clientName;
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

	public int getPort() {
		return this.port;
	}

	public int getClient() {
		return this.client;
	}

	@Override
	public String getKey(){
		return ("tuxguitar-alsa_" + this.client + "-" + this.port);
	}

	@Override
	public String getName(){
		return (this.clientName +" [" + this.client + ":" + this.port + "]");
	}
}