package org.herac.tuxguitar.io.abc;

import org.herac.tuxguitar.app.system.plugins.base.TGImporterPlugin;
import org.herac.tuxguitar.io.base.TGRawImporter;

public class ABCPluginImporter extends TGImporterPlugin{
	
	@Override
	protected TGRawImporter getImporter() {
		return new ABCSongImporter();
	}
	
	@Override
	public String getAuthor() {
		return "Peter Grootswagers <pgrootswagers@planet.nl>";
	}
	
	@Override
	public String getName() {
		return "ABCF file format importer";
	}
	
	@Override
	public String getDescription() {
		return "ABCF file format importer";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
