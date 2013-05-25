package org.herac.tuxguitar.player.impl.midiport.coreaudio;

import org.herac.tuxguitar.app.system.plugins.base.TGMidiOutputPortProviderPlugin;
import org.herac.tuxguitar.player.base.MidiOutputPortProvider;

public class MidiPortReaderPlugin extends TGMidiOutputPortProviderPlugin{

	@Override
	protected MidiOutputPortProvider getProvider() {
		return new MidiPortReaderCoreAudio();
	}

	@Override
	public String getAuthor() {
		return "Auria";
	}

	@Override
	public String getDescription() {		
		return "Core Audio output plugin";
	}

	@Override
	public String getName() {
		return "Core Audio output plugin";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
	
}
