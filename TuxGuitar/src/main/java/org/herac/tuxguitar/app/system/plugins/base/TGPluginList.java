package org.herac.tuxguitar.app.system.plugins.base;

import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.app.system.plugins.TGPlugin;
import org.herac.tuxguitar.app.system.plugins.TGPluginException;

public abstract class TGPluginList extends TGPluginAdapter{

	private List<TGPlugin> plugins;

	public TGPluginList(){
		super();
	}

	@Override
	public void init() throws TGPluginException {
		Iterator<TGPlugin> it = getIterator();
		while(it.hasNext()){
			TGPlugin plugin = it.next();
			plugin.init();
		}
	}

	@Override
	public void close() throws TGPluginException {
		Iterator<TGPlugin> it = getIterator();
		while(it.hasNext()){
			TGPlugin plugin = it.next();
			plugin.close();
		}
	}

	@Override
	public void setEnabled(boolean enabled) throws TGPluginException {
		Iterator<TGPlugin> it = getIterator();
		while(it.hasNext()){
			TGPlugin plugin = it.next();
			plugin.setEnabled( enabled);
		}
	}

	private Iterator<TGPlugin> getIterator() throws TGPluginException {
		if(this.plugins == null){
			this.plugins = getPlugins();
		}
		return this.plugins.iterator();
	}

	protected abstract List<TGPlugin> getPlugins() throws TGPluginException ;
}
