package org.herac.tuxguitar.app.system.plugins;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;

public class TGPluginSettingsManager {
	
	private static TGPluginSettingsManager instance;
	
	private Map handlers;
	
	public TGPluginSettingsManager(){
		this.handlers = new HashMap();
	}
	
	public static TGPluginSettingsManager getInstance(){
		synchronized (TGPluginSettingsManager.class) {
			if( instance == null ){
				instance = new TGPluginSettingsManager();
			}
			return instance;
		}
	}
	
	public void openPluginSettingsDialog(String moduleId, Shell parent){
		if( this.containsPluginSettingsHandler(moduleId) ){
			TGPluginSettingsHandler tgPluginSettingsHandler = (TGPluginSettingsHandler) this.handlers.get(moduleId);
			tgPluginSettingsHandler.openSettingsDialog(parent);
		}
	}
	
	public void addPluginSettingsHandler(String moduleId, TGPluginSettingsHandler pluginSettingsHandler){
		if(!this.containsPluginSettingsHandler(moduleId) ){
			this.handlers.put(moduleId, pluginSettingsHandler);
		}
	}
	
	public void removePluginSettingsHandler(String moduleId){
		if( this.containsPluginSettingsHandler(moduleId) ){
			this.handlers.remove(moduleId);
		}
	}
	
	public boolean containsPluginSettingsHandler(String moduleId){
		return this.handlers.containsKey(moduleId);
	}
}
