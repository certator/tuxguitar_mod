package org.herac.tuxguitar.community.browser;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.app.tools.browser.TGBrowserException;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowser;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserElement;

public class TGBrowserImpl extends TGBrowser {

	private final TGBrowserConnection connection;
	private TGBrowserElementImpl element;

	public TGBrowserImpl(TGBrowserDataImpl data){
		this.element = null;
		this.connection = new TGBrowserConnection();
	}

	@Override
	public void open() throws TGBrowserException {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() throws TGBrowserException {
		// TODO Auto-generated method stub
	}

	@Override
	public void cdRoot() throws TGBrowserException {
		this.element = null;
	}

	@Override
	public void cdUp() throws TGBrowserException {
		if( this.element != null ){
			this.element = this.element.getParent();
		}
	}

	@Override
	public void cdElement(TGBrowserElement element) throws TGBrowserException {
		if( element instanceof TGBrowserElementImpl ){
			TGBrowserElementImpl nextElement = (TGBrowserElementImpl)element;
			nextElement.setParent( this.element );
			this.element = nextElement;
		}
	}

	@Override
	public List<TGBrowserElement> listElements() throws TGBrowserException {
		List<TGBrowserElement> elements = new ArrayList<TGBrowserElement>();
		this.connection.getElements(elements , this.element );
		return elements;
	}
}
