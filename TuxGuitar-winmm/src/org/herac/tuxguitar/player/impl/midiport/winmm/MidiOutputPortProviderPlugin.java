package org.herac.tuxguitar.player.impl.midiport.winmm;

import org.herac.tuxguitar.player.base.MidiOutputPortProvider;
import org.herac.tuxguitar.player.plugin.TGMidiOutputPortProviderPlugin;

public class MidiOutputPortProviderPlugin extends TGMidiOutputPortProviderPlugin{
	
	public static final String MODULE_ID = "tuxguitar-winmm";
	
	private MidiOutputPortProviderImpl portReader;
	
	protected MidiOutputPortProvider getProvider() {
		if(this.portReader == null){
			this.portReader = new MidiOutputPortProviderImpl();
		}
		return this.portReader;
	}
	
	public String getModuleId(){
		return MODULE_ID;
	}
}
