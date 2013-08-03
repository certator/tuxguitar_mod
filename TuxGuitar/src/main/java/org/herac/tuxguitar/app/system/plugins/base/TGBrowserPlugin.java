package org.herac.tuxguitar.app.system.plugins.base;

import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.tools.browser.TGBrowserManager;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserFactory;

public abstract class TGBrowserPlugin extends TGPluginAdapter{
	private boolean loaded;
	private TGBrowserFactory factory;

	protected abstract TGBrowserFactory getFactory() throws TGPluginException;

	@Override
	public void init() throws TGPluginException {
		this.factory = getFactory();
		this.loaded = false;
	}

	@Override
	public void close() throws TGPluginException {
		this.loaded = false;
	}

	@Override
	public void setEnabled(boolean enabled) throws TGPluginException {
		if(enabled){
			addPlugin();
		}else{
			removePlugin();
		}
	}

	protected void addPlugin() throws TGPluginException {
		if(!this.loaded){
			TGBrowserManager.instance().addFactory(this.factory);
			this.loaded = true;
		}
	}

	protected void removePlugin() throws TGPluginException {
		if(this.loaded){
			TGBrowserManager.instance().removeFactory(this.factory);
			this.loaded = false;
		}
	}
}
