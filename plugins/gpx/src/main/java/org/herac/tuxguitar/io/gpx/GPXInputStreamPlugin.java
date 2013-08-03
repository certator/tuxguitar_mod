package org.herac.tuxguitar.io.gpx;

import org.herac.tuxguitar.app.system.plugins.base.TGInputStreamPlugin;
import org.herac.tuxguitar.io.base.TGInputStreamBase;

public class GPXInputStreamPlugin extends TGInputStreamPlugin{

	@Override
	protected TGInputStreamBase getInputStream() {
		return new GPXInputStream();
	}

	@Override
	public String getAuthor() {
		return "J.Jørgen von Bargen, Julian Casadesus <julian@casadesus.com.ar>, b4dc0d3r";
	}

	@Override
	public String getDescription() {
		return "GPX File Format plugin based on code contributed by J.Jørgen von Bargen, updates by b4dc0d3r";
	}

	@Override
	public String getName() {
		return "GPX File Format plugin";
	}

	@Override
	public String getVersion() {
		return "0.2";
	}
}
