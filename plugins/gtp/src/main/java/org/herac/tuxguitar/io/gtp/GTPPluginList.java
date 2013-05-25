package org.herac.tuxguitar.io.gtp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.system.plugins.TGPlugin;
import org.herac.tuxguitar.app.system.plugins.TGPluginSetup;
import org.herac.tuxguitar.app.system.plugins.base.TGInputStreamPlugin;
import org.herac.tuxguitar.app.system.plugins.base.TGOutputStreamPlugin;
import org.herac.tuxguitar.app.system.plugins.base.TGPluginList;
import org.herac.tuxguitar.io.base.TGInputStreamBase;
import org.herac.tuxguitar.io.base.TGOutputStreamBase;

public class GTPPluginList extends TGPluginList implements TGPluginSetup{
	
	@Override
	protected List<TGPlugin> getPlugins() {
		GTPSettingsUtil.instance().load();
		
		List<TGPlugin> plugins = new ArrayList<TGPlugin>();
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new GP5InputStream(GTPSettingsUtil.instance().getSettings());
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new GP4InputStream(GTPSettingsUtil.instance().getSettings());
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new GP3InputStream(GTPSettingsUtil.instance().getSettings());
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new GP2InputStream(GTPSettingsUtil.instance().getSettings());
			}
		});
		plugins.add(new TGInputStreamPlugin() {
			@Override
			protected TGInputStreamBase getInputStream() {
				return new GP1InputStream(GTPSettingsUtil.instance().getSettings());
			}
		});
		plugins.add(new TGOutputStreamPlugin() {
			@Override
			protected TGOutputStreamBase getOutputStream() {
				return new GP5OutputStream(GTPSettingsUtil.instance().getSettings());
			}
		});
		plugins.add(new TGOutputStreamPlugin() {
			@Override
			protected TGOutputStreamBase getOutputStream() {
				return new GP4OutputStream(GTPSettingsUtil.instance().getSettings());
			}
		});
		plugins.add(new TGOutputStreamPlugin() {
			@Override
			protected TGOutputStreamBase getOutputStream() {
				return new GP3OutputStream(GTPSettingsUtil.instance().getSettings());
			}
		});
		return plugins;
	}
	
	@Override
	public void setupDialog(Shell parent) {
		GTPSettingsUtil.instance().configure(parent);
	}
	
	@Override
	public String getAuthor() {
		return "Julian Casadesus <julian@casadesus.com.ar>";
	}
	
	@Override
	public String getName() {
		return "Guitar Pro File Format plugin";
	}
	
	@Override
	public String getDescription() {
		return "Guitar Pro File Format plugin for TuxGuitar";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}
