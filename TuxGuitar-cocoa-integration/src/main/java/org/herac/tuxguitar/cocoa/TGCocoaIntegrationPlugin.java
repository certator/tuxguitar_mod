package org.herac.tuxguitar.cocoa;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.app.system.plugins.TGPlugin;
import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.system.plugins.base.TGPluginList;
import org.herac.tuxguitar.cocoa.menu.MacMenuPlugin;
import org.herac.tuxguitar.cocoa.modifiedmarker.ModifiedMarkerPlugin;
import org.herac.tuxguitar.cocoa.opendoc.OpenDocPlugin;
import org.herac.tuxguitar.cocoa.toolbar.MacToolbarPlugin;

public class TGCocoaIntegrationPlugin extends TGPluginList {
	
	private List<TGPlugin> plugins; 
	
	@Override
	protected List<TGPlugin> getPlugins() throws TGPluginException {
		if( this.plugins == null ){
			this.plugins = new ArrayList<TGPlugin>();
			
			this.plugins.add(new OpenDocPlugin());
			this.plugins.add(new MacMenuPlugin());
			this.plugins.add(new MacToolbarPlugin());
			this.plugins.add(new ModifiedMarkerPlugin());
		}
		return this.plugins;
	}
	
	@Override
	public String getAuthor() {
		return "Auria & Julian Casadesus";
	}
	
	@Override
	public String getDescription() {
		return "Cocoa Integration Plugin";
	}
	
	@Override
	public String getName() {
		return "Cocoa Integration Plugin";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
