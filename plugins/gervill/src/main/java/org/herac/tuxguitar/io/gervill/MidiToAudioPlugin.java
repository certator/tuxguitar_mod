package org.herac.tuxguitar.io.gervill;

import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class MidiToAudioPlugin extends TGExporterPlugin{
	
	private boolean available;
	
	public MidiToAudioPlugin(){
		this.available = MidiToAudioSynth.instance().isAvailable();
	}
	
	@Override
	public void init() throws TGPluginException {
		if( this.available ){
			super.init();
		}
	}
	
	@Override
	public void close() throws TGPluginException {
		if( this.available ){
			super.close();
		}
	}
	
	@Override
	public void setEnabled( boolean enabled ) throws TGPluginException {
		if( this.available ){
			super.setEnabled( enabled );
		}
	}
	
	@Override
	protected TGRawExporter getExporter() throws TGPluginException {
		if( this.available ){
			return new MidiToAudioExporter();
		}
		return null;
	}
	
	@Override
	public String getVersion() {
		return "1.1";
	}
	
	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	@Override
	public String getName() {
		return "Gervill Plugin";
	}
	
	@Override
	public String getDescription() {
		String description = new String();
		description += ("The purpose of this plugin is to add gervill support to tuxguitar.\n");
		description += ("The current version of this plugin includes \"Export to Audio\" feature.\n");
		description += ("See more about Gervill: https://gervill.dev.java.net/\n\n");
		description += ("This plugin will only work if gervill synthesizer is installed in your JVM");
		return description;
	}
}
