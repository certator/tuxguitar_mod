package org.herac.tuxguitar.player.impl.midiport.oss;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.system.plugins.TGPluginSetup;
import org.herac.tuxguitar.app.system.plugins.base.TGMidiOutputPortProviderPlugin;
import org.herac.tuxguitar.player.base.MidiOutputPortProvider;

public class MidiOutputPortProviderPlugin extends TGMidiOutputPortProviderPlugin implements TGPluginSetup{
	
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
		return "OSS output plugin";
	}
	
	@Override
	public String getName() {
		return "OSS output plugin";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
	
	@Override
	public void setupDialog(Shell parent) {
		MidiConfigUtils.setupDialog(parent,(MidiOutputPortProviderImpl)getProvider());
	}
}
