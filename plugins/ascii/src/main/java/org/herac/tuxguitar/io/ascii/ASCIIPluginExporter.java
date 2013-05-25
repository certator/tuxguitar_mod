package org.herac.tuxguitar.io.ascii;

import org.herac.tuxguitar.app.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class ASCIIPluginExporter extends TGExporterPlugin{
	
	@Override
	protected TGRawExporter getExporter() {
		return new ASCIISongExporter();
	}
	
	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	@Override
	public String getDescription() {
		return "ASCII tab exporter";
	}
	
	@Override
	public String getName() {
		return "ASCII tab exporter";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
