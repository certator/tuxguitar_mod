package org.herac.tuxguitar.app.system.plugins.base;

import org.herac.tuxguitar.app.system.plugins.TGPlugin;

public abstract class TGPluginAdapter implements TGPlugin{
	
	@Override
	public String getName() {
		return "Untitled Plugin";
	}
	
	@Override
	public String getAuthor() {
		return "Unavailable";
	}
	
	@Override
	public String getDescription() {
		return "Unavailable";
	}
	
	@Override
	public String getVersion() {
		return "Unavailable";
	}
}
