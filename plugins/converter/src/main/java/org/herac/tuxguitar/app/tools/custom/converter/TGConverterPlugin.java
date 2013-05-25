package org.herac.tuxguitar.app.tools.custom.converter;

public class TGConverterPlugin extends org.herac.tuxguitar.app.system.plugins.base.TGToolItemPlugin {
	
	@Override
	protected void doAction() {
		new TGConverterDialog().show();
	}
	
	@Override
	protected String getItemName() {
		return "File format batch converter";
	}
	
	@Override
	public String getName() {
		return "BatchConverter";
	}
	
	@Override
	public String getAuthor() {
		return "Nikola Kolarovic & Julian Casadesus";
	}
	
	@Override
	public String getDescription() {
		return "Converts folder containing various tab formats into wanted file format.\n" +
		       "Depending on your loaded file format plugins, you can read and write.";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
