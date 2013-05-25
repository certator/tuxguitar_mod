package org.herac.tuxguitar.io.svg;

import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class SVGExporterPlugin extends TGExporterPlugin {
	
	@Override
	protected TGRawExporter getExporter() throws TGPluginException {
		return new SVGExporter(new SVGExporterStylesDialog());
	}
	
	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	@Override
	public String getDescription() {
		return "SVG exporter plugin";
	}
	
	@Override
	public String getName() {
		return "SVG exporter";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
