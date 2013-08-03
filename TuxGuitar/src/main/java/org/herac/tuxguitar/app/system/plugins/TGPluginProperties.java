/*
 * Created on 09-ene-2006
 */
package org.herac.tuxguitar.app.system.plugins;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.herac.tuxguitar.app.system.config.TGConfigManager;
import org.herac.tuxguitar.app.util.TGFileUtils;

/**
 * @author julian
 */
public class TGPluginProperties  extends TGConfigManager{

	private static final String FILE_NAME = "plugin.properties";

	private static TGPluginProperties instance;

	public static TGPluginProperties instance(){
		if(instance == null){
			instance = new TGPluginProperties();
			instance.init();
		}
		return instance;
	}

	private TGPluginProperties(){
		super();
	}

	@Override
	public String getName() {
		return "TuxGuitar Plugin Properties";
	}

	@Override
	public String getFileName(){
		return TGFileUtils.PATH_USER_CONFIG + File.separator + FILE_NAME;
	}

	@Override
	public Properties getDefaults() {
		Properties properties = new Properties();
		try {
			InputStream is = TGFileUtils.getResourceAsStream(FILE_NAME);
			if(is != null){
				properties.load(is);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return properties;
	}
}