package org.herac.tuxguitar.player.impl.jsa;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.system.plugins.TGPlugin;
import org.herac.tuxguitar.app.system.plugins.TGPluginSetup;
import org.herac.tuxguitar.app.system.plugins.base.TGMidiOutputPortProviderPlugin;
import org.herac.tuxguitar.app.system.plugins.base.TGMidiSequencerProviderPlugin;
import org.herac.tuxguitar.app.system.plugins.base.TGPluginList;
import org.herac.tuxguitar.player.base.MidiOutputPortProvider;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;
import org.herac.tuxguitar.player.impl.jsa.midiport.MidiPortProviderImpl;
import org.herac.tuxguitar.player.impl.jsa.sequencer.MidiSequencerProviderImpl;
import org.herac.tuxguitar.player.impl.jsa.utils.MidiConfigUtils;

public class MidiPluginList extends TGPluginList implements TGPluginSetup{

	@Override
	protected List<TGPlugin> getPlugins() {
		List<TGPlugin> plugins = new ArrayList<TGPlugin>();
		plugins.add(new TGMidiOutputPortProviderPlugin() {
			@Override
			protected MidiOutputPortProvider getProvider() {
				return new MidiPortProviderImpl();
			}
		});
		plugins.add(new TGMidiSequencerProviderPlugin() {
			@Override
			protected MidiSequencerProvider getProvider() {
				return new MidiSequencerProviderImpl();
			}
		});
		return plugins;
	}

	@Override
	public void setupDialog(Shell parent) {
		MidiConfigUtils.setupDialog(parent);
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}

	@Override
	public String getDescription() {
		return "Java Sound Api plugin";
	}

	@Override
	public String getName() {
		return "Java Sound Api plugin";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
}
