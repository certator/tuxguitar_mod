package org.herac.tuxguitar.app.system.plugins.base;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.io.base.TGRawExporter;

public abstract class TGExporterPlugin extends TGPluginAdapter{

	private boolean loaded;
	private TGRawExporter exporter;

	protected abstract TGRawExporter getExporter() throws TGPluginException;

	@Override
	public void init() throws TGPluginException {
		this.exporter = getExporter();
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
		if(!this.loaded && this.exporter != null){
			TGFileFormatManager.instance().addExporter(this.exporter);
			TuxGuitar.instance().getItemManager().createMenu();
			this.loaded = true;
		}
	}

	protected void removePlugin() throws TGPluginException {
		if(this.loaded && this.exporter != null){
			TGFileFormatManager.instance().removeExporter(this.exporter);
			TuxGuitar.instance().getItemManager().createMenu();
			this.loaded = false;
		}
	}
}
