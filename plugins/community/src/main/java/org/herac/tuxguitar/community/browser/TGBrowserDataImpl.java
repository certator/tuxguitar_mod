package org.herac.tuxguitar.community.browser;

import org.herac.tuxguitar.app.tools.browser.base.TGBrowserData;

public class TGBrowserDataImpl implements TGBrowserData {
	
	public TGBrowserDataImpl(){
		super();
	}
	
	@Override
	public String getTitle() {
		return "TuxGuitar Community";
	}
	
	@Override
	public String toString(){
		return getTitle();
	}
}
