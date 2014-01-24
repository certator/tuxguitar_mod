package org.herac.tuxguitar.cocoa.modifiedmarker;

import org.herac.tuxguitar.cocoa.TGCocoaIntegrationPlugin;
import org.herac.tuxguitar.util.plugin.TGPlugin;
import org.herac.tuxguitar.util.plugin.TGPluginException;

public class ModifiedMarkerPlugin implements TGPlugin {
	
	private ModifiedMarker modifiedMarker;
	
	public void init() throws TGPluginException {
		// Nothing todo
	}
	
	public void close() throws TGPluginException {
		// Nothing todo
	}
	
	public void setEnabled(boolean enabled) throws TGPluginException {
		try {
			if( this.modifiedMarker != null ){
				this.modifiedMarker.setEnabled(enabled);
			}else if(enabled){
				this.modifiedMarker = new ModifiedMarker();
				this.modifiedMarker.setEnabled(true);
				this.modifiedMarker.init();
			}
		} catch( Throwable throwable ){
			throw new TGPluginException( throwable );
		}
	}
	
	public String getModuleId() {
		return TGCocoaIntegrationPlugin.MODULE_ID;
	}
}
