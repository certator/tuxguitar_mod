package org.herac.tuxguitar.community.startup;

import org.herac.tuxguitar.app.system.plugins.base.TGPluginAdapter;

public class TGCommunityStartupPlugin extends TGPluginAdapter {

	private boolean done;

	public TGCommunityStartupPlugin(){
		this.done = false;
	}

	@Override
	public void init() {
		// Nothing to init.
	}

	@Override
	public void close() {
		// Nothing to close.
	}

	@Override
	public void setEnabled(boolean enabled) {
		if(!this.done && enabled){
			TGCommunityStartupScreen startup = new TGCommunityStartupScreen();
			if( !startup.isDisabled() ){
				startup.open();
			}
		}
		this.done = true;
	}
}
