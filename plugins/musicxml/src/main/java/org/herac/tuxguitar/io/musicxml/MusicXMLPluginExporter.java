package org.herac.tuxguitar.io.musicxml;

import org.herac.tuxguitar.app.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class MusicXMLPluginExporter extends TGExporterPlugin{

	@Override
	protected TGRawExporter getExporter() {
		return new MusicXMLSongExporter();
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}

	@Override
	public String getDescription() {
		return "MusicXML exporter plugin";
	}

	@Override
	public String getName() {
		return "MusicXML exporter";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
}
