package org.herac.tuxguitar.app.tools.browser.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.tools.browser.TGBrowserCollection;

public class TGBrowserCollectionCombo {

	private Combo combo;
	private List<TGBrowserCollection> collections;

	public TGBrowserCollectionCombo(Composite parent, int style){
		this.combo = new Combo(parent,style);
		this.collections = new ArrayList<TGBrowserCollection>();
		this.addFirstElement();
	}

	private void addFirstElement(){
		this.combo.add(TuxGuitar.getProperty("browser.collection.select"));
		this.combo.select(0);
	}

	public void add(TGBrowserCollection collection) {
		this.combo.add(collection.getData().getTitle());
		this.collections.add(collection);
	}

	public TGBrowserCollection getSelection(){
		int index = ( this.combo.getSelectionIndex() - 1);
		if(index >= 0 && index < this.collections.size()){
			return this.collections.get(index);
		}
		return null;
	}

	public void addSelectionListener(SelectionListener listener) {
		this.combo.addSelectionListener(listener);
	}

	public void setLayoutData(Object layoutData) {
		this.combo.setLayoutData(layoutData);
	}

	public void removeAll() {
		this.combo.removeAll();
		this.collections.clear();
		this.addFirstElement();
	}

	public void select(int index) {
		this.combo.select( ( index + 1 ) );
	}

	public void setEnabled(boolean enabled){
		this.combo.setEnabled(enabled);
	}

	public boolean isEmpty(){
		return this.collections.isEmpty();
	}
}
