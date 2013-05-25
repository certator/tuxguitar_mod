package org.herac.tuxguitar.io.tef;

import org.herac.tuxguitar.app.system.plugins.base.TGImporterPlugin;
import org.herac.tuxguitar.io.base.TGRawImporter;

public class TEPluginImporter extends TGImporterPlugin{

	@Override
	protected TGRawImporter getImporter() {
		return new TESongImporter();
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}

	@Override
	public String getName() {
		return "TEF file format importer";
	}

	@Override
	public String getDescription() {
		return "TEF file format importer";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
}
