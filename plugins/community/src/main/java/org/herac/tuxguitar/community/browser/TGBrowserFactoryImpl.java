package org.herac.tuxguitar.community.browser;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowser;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserData;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserFactory;

public class TGBrowserFactoryImpl implements TGBrowserFactory {

	private TGBrowserDataImpl data;

	public TGBrowserFactoryImpl(){
		this.data = new TGBrowserDataImpl();
	}

	@Override
	public String getName() {
		return "Community Files";
	}

	@Override
	public String getType() {
		return "community";
	}

	@Override
	public TGBrowser newTGBrowser(TGBrowserData data) {
		return new TGBrowserImpl( (TGBrowserDataImpl)data );
	}

	@Override
	public TGBrowserData parseData(String string) {
		return this.data;
	}

	@Override
	public TGBrowserData dataDialog(Shell parent) {
		TGBrowserAuthDialog authDialog = new TGBrowserAuthDialog();
		authDialog.open( parent );
		if( authDialog.isAccepted() ){
			return this.data;
		}
		return null;
	}

}
