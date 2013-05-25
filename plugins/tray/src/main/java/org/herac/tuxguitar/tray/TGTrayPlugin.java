package org.herac.tuxguitar.tray;

import org.herac.tuxguitar.app.system.plugins.base.TGPluginAdapter;

public class TGTrayPlugin extends TGPluginAdapter {
	
	private boolean loaded;
	private TGTray tray;
	
	public TGTrayPlugin(){
		super();
	}
	
	@Override
	public void init() {
		this.tray = new TGTray();
	}
	
	@Override
	public void close() {
		this.removePlugin();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if(enabled){
			this.addPlugin();
		}else{
			this.removePlugin();
		}
	}
	
	protected void addPlugin(){
		if(!this.loaded){
			this.tray.addTray();
			this.loaded = true;
		}
	}
	
	protected void removePlugin(){
		if(this.loaded){
			this.tray.removeTray();
			this.loaded = false;
		}
	}
	
	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	@Override
	public String getName() {
		return "System Tray plugin";
	}
	
	@Override
	public String getDescription() {
		return "System Tray plugin for tuxguitar";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
