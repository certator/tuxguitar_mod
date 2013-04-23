package org.herac.tuxguitar.community;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.app.system.plugins.TGPlugin;
import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.system.plugins.base.TGPluginList;
import org.herac.tuxguitar.community.browser.TGBrowserPluginImpl;
import org.herac.tuxguitar.community.io.TGShareSongPlugin;
import org.herac.tuxguitar.community.startup.TGCommunityStartupPlugin;

public class TGCommunityPluginImpl extends TGPluginList {

	@Override
	protected List<TGPlugin> getPlugins() throws TGPluginException {
		List<TGPlugin> plugins = new ArrayList<TGPlugin>();
		
		plugins.add(new TGShareSongPlugin());
		plugins.add(new TGBrowserPluginImpl());
		plugins.add(new TGCommunityStartupPlugin());
		
		return plugins;
	}
	
	@Override
	public void init() throws TGPluginException{
		TGCommunitySingleton.getInstance().loadSettings();
		super.init();
	}
	
	@Override
	public void close() throws TGPluginException{
		TGCommunitySingleton.getInstance().saveSettings();
		super.close();
	}
	
	@Override
	public String getName() {
		return "TuxGuitar Community Integration";
	}
	
	@Override
	public String getDescription() {
		return "TuxGuitar Community Integration";
	}
	
	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	@Override
	public String getVersion() {
		return "1.2";
	}
}
