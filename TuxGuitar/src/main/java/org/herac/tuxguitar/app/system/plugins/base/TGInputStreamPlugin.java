package org.herac.tuxguitar.app.system.plugins.base;

import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.io.base.TGInputStreamBase;

public abstract class TGInputStreamPlugin extends TGPluginAdapter{

	private boolean loaded;
	private TGInputStreamBase stream;

	protected abstract TGInputStreamBase getInputStream() throws TGPluginException ;

	@Override
	public void init() throws TGPluginException {
		this.stream = getInputStream();
	}

	@Override
	public void close() throws TGPluginException {
		this.removePlugin();
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
			TGFileFormatManager.instance().addInputStream(this.stream);
			this.loaded = true;
		}
	}

	protected void removePlugin() throws TGPluginException {
		if(this.loaded){
			TGFileFormatManager.instance().removeInputStream(this.stream);
			this.loaded = false;
		}
	}
}
