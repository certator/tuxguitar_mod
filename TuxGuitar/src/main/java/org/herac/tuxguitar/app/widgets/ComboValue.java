package org.herac.tuxguitar.app.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ComboValue<V> extends Combo {

	private List<V> values;

	public ComboValue(Composite parent, int style) {
		super(parent, style);
		values = new ArrayList<V>();
	}

//	iv got calling from custom "add" which create problems
//
//	@Override
//	public void add(String string) {
//		values.add(null);
//		super.add(string);
//	}
//	@Override
//	public void add(String string, int index) {
//		values.add(index, null);
//		super.add(string, index);
//	}
//
//	@Override
//	public void remove(int index) {
//		values.remove(index);
//		super.remove(index);
//	}
//
//	@Override
//	public void remove(String string) {
//		int i = indexOf(string);
//		if (i != -1) {
//			values.remove(i);
//			super.remove(string);
//		}
//	}
//
//	@Override
//	public void remove(int start, int end) {
//		for (int i = start; i < end; i++) {
//			values.remove(start);
//		}
//		super.remove(start, end);
//	}

	@Override
	public void removeAll() {
		values.clear();
		super.removeAll();
	}

	public void add(String string, V value) {
		values.add(value);
		super.add(string);
	}

	public V getSelectionValue() {
		int i = getSelectionIndex();
		if (i == -1) {
			return null;
		}
		return values.get(i);
	}

	@Override
	protected void checkSubclass() {
		// Allow to inherit SWT components... I would like to cook OOP thanks
	}

	@Override
	public void dispose() {
		values.clear();
		values = null;
		super.dispose();
	}
}
