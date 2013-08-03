package org.herac.tuxguitar.io.lilypond;

import org.herac.tuxguitar.app.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class LilypondPluginExporter extends TGExporterPlugin{

	@Override
	protected TGRawExporter getExporter() {
		return new LilypondSongExporter();
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>, updates by b4dc0d3r";
	}

	@Override
	public String getDescription() {
		return "Lilypond exporter plugin";
	}

	@Override
	public String getName() {
		return "Lilypond exporter";
	}

	@Override
	public String getVersion() {
		return "1.1";
	}
}
