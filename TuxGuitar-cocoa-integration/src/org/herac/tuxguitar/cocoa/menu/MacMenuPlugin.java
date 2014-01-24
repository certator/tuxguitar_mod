package org.herac.tuxguitar.cocoa.menu;

import org.herac.tuxguitar.cocoa.TGCocoaIntegrationPlugin;
import org.herac.tuxguitar.util.plugin.TGPlugin;
import org.herac.tuxguitar.util.plugin.TGPluginException;

public class MacMenuPlugin implements TGPlugin {
	
	private MacMenu macMenu;
	
	public void init() throws TGPluginException {
		// Nothing todo
	}
	
	public void close() throws TGPluginException {
		// Nothing todo
	}
	
	public void setEnabled(boolean enabled) throws TGPluginException {
		try {
			if( this.macMenu != null ){
				this.macMenu.setEnabled(enabled);
			}else if(enabled){
				this.macMenu = new MacMenu();
				this.macMenu.setEnabled(true);
				this.macMenu.init();
			}
		} catch( Throwable throwable ){
			throw new TGPluginException( throwable );
		}
	}
	
	public String getModuleId() {
		return TGCocoaIntegrationPlugin.MODULE_ID;
	}
}
