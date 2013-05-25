package org.herac.tuxguitar.jack;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.system.plugins.TGPlugin;
import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.system.plugins.TGPluginSetup;
import org.herac.tuxguitar.app.system.plugins.base.TGPluginList;
import org.herac.tuxguitar.jack.sequencer.JackSequencerProviderPlugin;
import org.herac.tuxguitar.jack.settings.JackSettings;
import org.herac.tuxguitar.jack.settings.JackSettingsDialog;
import org.herac.tuxguitar.jack.synthesizer.JackOutputPortProviderPlugin;

public class JackPluginImpl extends TGPluginList implements TGPluginSetup {

	private final JackClient jackClient;
	private final JackSettings jackSettings;

	public JackPluginImpl(){
		this.jackClient = new JackClient();
		this.jackSettings = new JackSettings();
	}

	@Override
	protected List<TGPlugin> getPlugins() throws TGPluginException {
		List<TGPlugin> plugins = new ArrayList<TGPlugin>();
		plugins.add( new JackOutputPortProviderPlugin(this.jackClient , this.jackSettings) );
		plugins.add( new JackSequencerProviderPlugin(this.jackClient) );
		return plugins;
	}

	public void closeAll(){
		if(this.jackClient.isOpen()){
			this.jackClient.close();
			this.jackClient.finalize();
		}
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}

	@Override
	public String getDescription() {
		return "Jack Audio Connection Kit plugin support";
	}

	@Override
	public String getName() {
		return "Jack Audio Connection Kit plugin support";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public void setupDialog(Shell parent){
		JackSettingsDialog jackSettingsDialog = new JackSettingsDialog( this.jackSettings );
		jackSettingsDialog.open( parent );
	}
}
