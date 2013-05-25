package org.herac.tuxguitar.jack.sequencer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.jack.JackClient;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;

public class JackSequencerProvider implements MidiSequencerProvider{

	private List<MidiSequencer> jackSequencerProviders;
	private final JackClient jackClient;

	public JackSequencerProvider(JackClient jackClient){
		this.jackClient = jackClient;
	}

	@Override
	public List<MidiSequencer> listSequencers() throws MidiPlayerException {
		if(this.jackSequencerProviders == null){
			this.jackSequencerProviders = new ArrayList<MidiSequencer>();
			this.jackSequencerProviders.add(new JackSequencer(this.jackClient));
		}
		return this.jackSequencerProviders;
	}

	@Override
	public void closeAll() throws MidiPlayerException {
		Iterator<MidiSequencer> it = listSequencers().iterator();
		while(it.hasNext()){
			MidiSequencer sequencer = it.next();
			sequencer.close();
		}
	}

}
