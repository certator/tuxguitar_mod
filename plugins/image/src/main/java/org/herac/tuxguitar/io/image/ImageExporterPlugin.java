package org.herac.tuxguitar.io.image;

import org.herac.tuxguitar.app.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class ImageExporterPlugin extends TGExporterPlugin{

	@Override
	protected TGRawExporter getExporter() {
		return new ImageExporterDialog();
	}

	@Override
	public String getVersion() {
		return "0.1";
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}

	@Override
	public String getName() {
		return "Image exporter";
	}

	@Override
	public String getDescription() {
		return "Image exporter";
	}
}
