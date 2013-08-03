package org.herac.tuxguitar.io.tg;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.app.system.plugins.TGPlugin;
import org.herac.tuxguitar.app.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.app.system.plugins.base.TGInputStreamPlugin;
import org.herac.tuxguitar.app.system.plugins.base.TGPluginList;
import org.herac.tuxguitar.io.base.TGInputStreamBase;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class TGPluginListImpl extends TGPluginList{

	@Override
	protected List<TGPlugin> getPlugins() {
		List<TGPlugin> plugins = new ArrayList<TGPlugin>();
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new org.herac.tuxguitar.io.tg.v12.TGInputStream();
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new org.herac.tuxguitar.io.tg.v11.TGInputStream();
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new org.herac.tuxguitar.io.tg.v10.TGInputStream();
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new org.herac.tuxguitar.io.tg.v09.TGInputStream();
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new org.herac.tuxguitar.io.tg.v08.TGInputStream();
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new org.herac.tuxguitar.io.tg.v07.TGInputStream();
			}
		});
		plugins.add(new TGExporterPlugin() {
			@Override
			protected TGRawExporter getExporter() {
				return new org.herac.tuxguitar.io.tg.v12.TGOutputStream();
			}
		});
		plugins.add(new TGExporterPlugin() {
			@Override
			protected TGRawExporter getExporter() {
				return new org.herac.tuxguitar.io.tg.v11.TGOutputStream();
			}
		});
		plugins.add(new TGExporterPlugin() {
			@Override
			protected TGRawExporter getExporter() {
				return new org.herac.tuxguitar.io.tg.v10.TGOutputStream();
			}
		});
		return plugins;
	}

	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}

	@Override
	public String getName() {
		return "TuxGuitar file format compatibility";
	}

	@Override
	public String getDescription() {
		return "This plugin, provides support for other tuxguitar file format versions.";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
}
