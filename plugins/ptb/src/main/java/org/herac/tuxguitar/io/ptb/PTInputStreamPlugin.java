package org.herac.tuxguitar.io.ptb;

import org.herac.tuxguitar.app.system.plugins.base.TGInputStreamPlugin;
import org.herac.tuxguitar.io.base.TGInputStreamBase;

public class PTInputStreamPlugin extends TGInputStreamPlugin{
	
	@Override
	protected TGInputStreamBase getInputStream() {
		return new PTInputStream();
	}
	
	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	@Override
	public String getDescription() {
		return "PTB File Format plugin";
	}
	
	@Override
	public String getName() {
		return "PTB File Format plugin";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
