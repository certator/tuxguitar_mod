package org.herac.tuxguitar.app.system.tuning;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.herac.tuxguitar.app.util.TGFileUtils;

public class TuningTemplateManager {

	private static final String DEFAULT_SHORTCUT_FILE = "tunings-default.xml";

	private final List<TuningTemplate> templates;

	public TuningTemplateManager() {
		InputStream stream = TGFileUtils.getResourceAsStream(DEFAULT_SHORTCUT_FILE);
		templates = new ArrayList<TuningTemplate>();
		if (stream != null) {
			templates.addAll(TuningTemplateReader.getTuningTemplates(stream));
		}
		
	}
	
	public List<String> getAvailableInstruments() {
		Set<String> instruments = new HashSet<String>();
		for (TuningTemplate tuning: templates) {
			instruments.add(tuning.getInstrument());
		}
		return new ArrayList<String>(instruments);
	}

	public List<Integer> getAvailableNumberOfStringsFromInstrument(String instrument) {
		Set<Integer> strings = new HashSet<Integer>();
		for (TuningTemplate tuning: templates) {
			if (!tuning.getInstrument().equals(instrument)) {
				continue;
			}
			strings.add(tuning.getStrings().length);
		}
		return new ArrayList<Integer>(strings);
	}

	public Iterator<TuningTemplate> getTuningTemplates() {
		return templates.iterator();
	}

}
