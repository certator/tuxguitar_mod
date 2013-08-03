package org.herac.tuxguitar.cocoa.modifiedmarker;

import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.system.plugins.base.TGPluginAdapter;

public class ModifiedMarkerPlugin extends TGPluginAdapter {

	private ModifiedMarker modifiedMarker;

	@Override
	public void init() throws TGPluginException {
		// Nothing todo
	}

	@Override
	public void close() throws TGPluginException {
		// Nothing todo
	}

	@Override
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
}
