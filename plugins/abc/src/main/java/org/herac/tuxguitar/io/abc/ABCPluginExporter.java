package org.herac.tuxguitar.io.abc;

import org.herac.tuxguitar.app.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class ABCPluginExporter extends TGExporterPlugin{

	@Override
	protected TGRawExporter getExporter() {
		return new ABCSongExporter();
	}

	@Override
	public String getAuthor() {
		return "Peter Grootswagers <pgrootswagers@planet.nl>";
	}

	@Override
	public String getDescription() {
		return "ABC 2.10.5 exporter plugin";
	}

	@Override
	public String getName() {
		return "ABC exporter";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
}
