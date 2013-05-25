package org.herac.tuxguitar.io.midi;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.app.system.plugins.TGPlugin;
import org.herac.tuxguitar.app.system.plugins.base.TGPluginList;

public class MidiPluginList extends TGPluginList{

	@Override
	protected List<TGPlugin> getPlugins() {
		List<TGPlugin> plugins = new ArrayList<TGPlugin>();
		plugins.add(new MidiPluginImporter());
		plugins.add(new MidiPluginExporter());
		return plugins;
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}

	@Override
	public String getDescription() {
		return "Midi file format import-export plugin";
	}

	@Override
	public String getName() {
		return "Midi file format import-export plugin";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
}
