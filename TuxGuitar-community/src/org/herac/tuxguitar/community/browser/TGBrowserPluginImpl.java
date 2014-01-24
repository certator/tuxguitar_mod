package org.herac.tuxguitar.community.browser;

import org.herac.tuxguitar.app.tools.browser.base.TGBrowserFactory;
import org.herac.tuxguitar.app.tools.browser.plugin.TGBrowserPlugin;
import org.herac.tuxguitar.community.TGCommunityPlugin;
import org.herac.tuxguitar.util.plugin.TGPluginException;

public class TGBrowserPluginImpl extends TGBrowserPlugin {
	
	protected TGBrowserFactory getFactory() throws TGPluginException {
		return new TGBrowserFactoryImpl();
	}
	
	public String getModuleId(){
		return TGCommunityPlugin.MODULE_ID;
	}
}
