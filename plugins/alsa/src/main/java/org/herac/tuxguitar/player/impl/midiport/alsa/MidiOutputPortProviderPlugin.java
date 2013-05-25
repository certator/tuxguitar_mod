package org.herac.tuxguitar.player.impl.midiport.alsa;

import org.herac.tuxguitar.app.system.plugins.base.TGMidiOutputPortProviderPlugin;
import org.herac.tuxguitar.player.base.MidiOutputPortProvider;

public class MidiOutputPortProviderPlugin extends TGMidiOutputPortProviderPlugin{

	@Override
	protected MidiOutputPortProvider getProvider() {
		return new MidiOutputPortProviderImpl();
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}

	@Override
	public String getDescription() {
		return "ALSA output plugin";
	}

	@Override
	public String getName() {
		return "ALSA output plugin";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

}
