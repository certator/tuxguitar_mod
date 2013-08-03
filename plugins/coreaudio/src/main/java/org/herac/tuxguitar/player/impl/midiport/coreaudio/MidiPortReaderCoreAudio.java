package org.herac.tuxguitar.player.impl.midiport.coreaudio;

import java.util.List;

import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiOutputPortProvider;

public class MidiPortReaderCoreAudio implements MidiOutputPortProvider{

	private static final MidiReceiverImpl midiOut = new MidiReceiverImpl();

	public MidiPortReaderCoreAudio(){
		super();
	}

	@Override
	public List<MidiOutputPort> listPorts() {
		if(!midiOut.isOpen()){
			midiOut.open();
		}
		return midiOut.listPorts();
	}

	@Override
	public void closeAll(){
		midiOut.close();
	}

}
