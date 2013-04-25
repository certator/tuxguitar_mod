package org.herac.tuxguitar.app.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.system.tuning.TuningTemplate;
import org.herac.tuxguitar.app.system.tuning.TuningTemplateManager;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.util.MapComparator;
import org.herac.tuxguitar.util.StringUtil;

public class TuningTemplateDialog extends Shell {

	private static final int MINIMUM_BUTTON_WIDTH = 80;
	private static final int MINIMUM_BUTTON_HEIGHT = 25;

	final ComboValue<TGString[]> comboTuning;
	final Composite stringsZone;
	private TGString[] strings;

	private static class Key {
		String instrument;
		int stringCount;
	}

	public TuningTemplateDialog(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		this.setLayout(new GridLayout(1,false));
		this.setText(TuxGuitar.getProperty("tuning.dialog.properties"));

		Composite body = new Composite(this,SWT.NONE);
		body.setLayout(new GridLayout());
		body.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));

		Composite bottom = new Composite(this, SWT.NONE);
		bottom.setLayout(new GridLayout(2,false));
		bottom.setLayoutData(new GridData(SWT.END,SWT.FILL,true,true,2,1));

		initButtons(bottom);

		ComboValue<Key> comboFilter = createComboFilter(body);

		comboTuning = new ComboValue<TGString[]>(body, SWT.DROP_DOWN);
		comboTuning.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateStrings(comboTuning.getSelectionValue());
			}
		});

		stringsZone = new Composite(body, 0);
		stringsZone.setLayout(new RowLayout(SWT.HORIZONTAL));

		// initialize default selected elements
		comboFilter.select(0);
		updateTuningList(comboFilter.getSelectionValue());
		comboTuning.select(0);
		updateStrings(comboTuning.getSelectionValue());
	}

	private ComboValue<Key> createComboFilter(Composite body) {
		// compute list of tuning filters
		TuningTemplateManager manager = TuxGuitar.instance().getTuningTemplateManager();
		Map<Key, String> filters = new HashMap<TuningTemplateDialog.Key, String>();
		final ComboValue<Key> comboFilter = new ComboValue<Key>(body, SWT.DROP_DOWN);
		for (String instrument: manager.getAvailableInstruments()) {
			for (Integer stringCount: manager.getAvailableNumberOfStringsFromInstrument(instrument)) {
				String label = TuxGuitar.getProperty("tuning.string-instrument", new String[]{instrument, "" + stringCount});
				label = StringUtil.toUpperFirstChar(label);
				Key key = new Key();
				key.instrument = instrument;
				key.stringCount = stringCount;
				filters.put(key, label);
			}
		}

		// sort labels
		List<Key> keys = new ArrayList<Key>(filters.keySet());
		Collections.sort(keys, new MapComparator<Key,String>(keys, filters));
		for (Key key: keys) {
			comboFilter.add(filters.get(key), key);
		}

		comboFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = comboFilter.getSelectionIndex();
				if (i != -1) {
					Key key = comboFilter.getSelectionValue();
					updateTuningList(key);
					// update default selected elements
					comboTuning.select(0);
					updateStrings(comboTuning.getSelectionValue());
				} else {
					updateTuningList("", 0);
				}
			}
		});

		return comboFilter;
	}

	/**
	 * Update preview of the strings
	 */
	private void updateStrings(TGString[] strings) {
		this.strings = strings;

		// clean up
		for (Control control : stringsZone.getChildren()) {
			control.dispose();
		}
		if (strings == null || strings.length == 0) {
			return;
		}

		int min = 500;
		int max = -500;
		for (TGString string: strings) {
			int v = string.getValue();
			if (v < min) {
				min = v;
			}
			if (v > max) {
				max = v;
			}
		}

		for (TGString string: strings) {
			StringLabel label = new StringLabel(stringsZone);
			label.setMinMax(min, max);
			label.setString(string);
		}
		stringsZone.layout(true);
		stringsZone.pack();
		pack(true);
	}

	/**
	 * Update the combo displaying tuning names
	 */
	private void updateTuningList(Key key) {
		if (key == null) {
			return;
		}
		updateTuningList(key.instrument, key.stringCount);
	}

	/**
	 * Update the combo displaying tuning names
	 */
	private void updateTuningList(String instrument, int stringCount) {
		comboTuning.removeAll();
		Iterator<TuningTemplate> i = TuxGuitar.instance().getTuningTemplateManager().getTuningTemplates();
		List<TuningTemplate> list = new ArrayList<TuningTemplate>();
		while (i.hasNext()) {
			TuningTemplate template = i.next();
			if (template.getInstrument().equals(instrument) && template.getStringCount() == stringCount) {
				list.add(template);
			}
		}
		Collections.sort(list, new Comparator<TuningTemplate>() {
			@Override
			public int compare(TuningTemplate t1, TuningTemplate t2) {
				return t1.getName().compareTo(t2.getName());
			}
		});
		for (TuningTemplate template: list) {
			if (template.getInstrument().equals(instrument) && template.getStringCount() == stringCount) {
				String label = StringUtil.toUpperFirstChar(template.getName());
				comboTuning.add(label, template.getStrings());
			}
		}
	}

	private void initButtons(Composite parent) {
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
		data.minimumWidth = MINIMUM_BUTTON_WIDTH;
		data.minimumHeight = MINIMUM_BUTTON_HEIGHT;

		Button buttonOK = new Button(parent, SWT.PUSH);
		buttonOK.setText(TuxGuitar.getProperty("ok"));
		buttonOK.setLayoutData(data);
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TuningTemplateDialog.this.dispose();
			}
		});

		Button buttonCancel = new Button(parent, SWT.PUSH);
		buttonCancel.setText(TuxGuitar.getProperty("cancel"));
		buttonCancel.setLayoutData(data);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				strings = null;
				TuningTemplateDialog.this.dispose();
			}
		});

		this.setDefaultButton( buttonOK );
		this.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				strings = null;
			}
		});
	}

	/**
	 * Get strings (i.e. loudest string in classical guitar is on last)
	 */
	public TGString[] getSelectedStrings() {
		if (strings == null) {
			return null;
		}
		// TuxGuitar use strings in reverse order;
		TGString[] reverse = new TGString[strings.length];
		for (int i = 0; i < strings.length; i++) {
			reverse[strings.length - 1 - i] = strings[i];
		}
		return reverse;
	}
	
	@Override
	protected void checkSubclass() {
		// Allow to inherit SWT components... I would like to cook OOP thanks
	}

}