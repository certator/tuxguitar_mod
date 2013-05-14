package org.herac.tuxguitar.app.tools.browser.filesystem;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowser;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserData;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserFactory;

public class TGBrowserFactoryImpl implements TGBrowserFactory{
	
	public TGBrowserFactoryImpl() {
		super();
	}
	
	@Override
	public String getType(){
		return "file.system";
	}
	
	@Override
	public String getName(){
		return TuxGuitar.getProperty("browser.factory.fs.name");
	}
	
	@Override
	public TGBrowser newTGBrowser(TGBrowserData data) {
		if(data instanceof TGBrowserDataImpl){
			return new TGBrowserImpl((TGBrowserDataImpl)data);
		}
		return null;
	}
	
	@Override
	public TGBrowserData parseData(String string) {
		return TGBrowserDataImpl.fromString(string);
	}
	
	@Override
	public TGBrowserData dataDialog(Shell parent) {
		TGBrowserDataDialog dialog = new TGBrowserDataDialog();
		return dialog.open(parent);
	}
}
