package org.herac.tuxguitar.player.impl.midiport.winmm;

import org.herac.tuxguitar.app.system.plugins.base.TGMidiOutputPortProviderPlugin;
import org.herac.tuxguitar.player.base.MidiOutputPortProvider;

public class MidiOutputPortProviderPlugin extends TGMidiOutputPortProviderPlugin{
	
	private MidiOutputPortProviderImpl portReader;
	
	@Override
	protected MidiOutputPortProvider getProvider() {
		if(this.portReader == null){
			this.portReader = new MidiOutputPortProviderImpl();
		}
		return this.portReader;
	}
	
	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	@Override
	public String getDescription() {
		return "WinMM output plugin";
	}
	
	@Override
	public String getName() {
		return "WinMM output plugin";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
