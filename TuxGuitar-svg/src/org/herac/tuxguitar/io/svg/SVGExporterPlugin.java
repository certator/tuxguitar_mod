package org.herac.tuxguitar.io.svg;

import org.herac.tuxguitar.io.base.TGRawExporter;
import org.herac.tuxguitar.io.plugin.TGExporterPlugin;
import org.herac.tuxguitar.util.plugin.TGPluginException;

public class SVGExporterPlugin extends TGExporterPlugin {
	
	public static final String MODULE_ID = "tuxguitar-svg";
	
	protected TGRawExporter getExporter() throws TGPluginException {
		return new SVGExporter(new SVGExporterStylesDialog());
	}
	
	public String getModuleId(){
		return MODULE_ID;
	}
}
