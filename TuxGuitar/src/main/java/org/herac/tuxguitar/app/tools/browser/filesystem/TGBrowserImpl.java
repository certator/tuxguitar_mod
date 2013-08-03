package org.herac.tuxguitar.app.tools.browser.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.herac.tuxguitar.app.tools.browser.base.TGBrowser;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserElement;

public class TGBrowserImpl extends TGBrowser{

	private File root;
	private TGBrowserElementImpl element;
	private final TGBrowserDataImpl data;

	public TGBrowserImpl(TGBrowserDataImpl data){
		this.data = data;
	}

	@Override
	public void open(){
		this.root = new File(this.data.getPath());
	}

	@Override
	public void close(){
		this.root = null;
	}

	@Override
	public void cdElement(TGBrowserElement element) {
		this.element = (TGBrowserElementImpl)element;
	}

	@Override
	public void cdRoot() {
		this.element = null;
	}

	@Override
	public void cdUp() {
		if(this.element != null){
			this.element = this.element.getParent();
		}
	}

	@Override
	public List<TGBrowserElement> listElements() {
		List<TGBrowserElement> elements = new ArrayList<TGBrowserElement>();
		File file = ((this.element != null)?this.element.getFile():this.root);
		if(file.exists() && file.isDirectory()){
			File[] files = file.listFiles();
			for(int i = 0; i < files.length;i ++){
				elements.add(new TGBrowserElementImpl(this.element,files[i]));
			}
		}
		if( !elements.isEmpty() ){
			Collections.sort(elements,new TGBrowserElementComparator());
		}
		return elements;
	}

}
