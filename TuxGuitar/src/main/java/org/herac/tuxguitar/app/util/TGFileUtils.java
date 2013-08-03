package org.herac.tuxguitar.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.system.config.TGConfigKeys;
import org.herac.tuxguitar.util.TGClassLoader;
import org.herac.tuxguitar.util.TGLibraryLoader;
import org.herac.tuxguitar.util.TGVersion;

public class TGFileUtils {

	private static final String TG_CONFIG_PATH = "tuxguitar.config.path";
	private static final String TG_SHARE_PATH = "tuxguitar.share.path";
	private static final String TG_USER_SHARE_PATH = "tuxguitar.user-share.path";
	private static final String TG_CLASS_PATH = "tuxguitar.class.path";
	private static final String TG_LIBRARY_PATH = "tuxguitar.library.path";
	private static final String TG_LIBRARY_PREFIX = "tuxguitar.library.prefix";
	private static final String TG_LIBRARY_EXTENSION = "tuxguitar.library.extension";

	public static final String PATH_USER_CONFIG = getUserConfigDir();
	public static final String PATH_USER_PLUGINS_CONFIG = getUserPluginsConfigDir();
	public static final String PATH_USER_SHARE_PATH = getUserSharedPath();
	//writable
	public static final String[] TG_STATIC_SHARED_PATHS = getStaticSharedPaths();

	public static InputStream getResourceAsStream(String resource) {
		try {
			if(TG_STATIC_SHARED_PATHS != null){
				for( int i = 0; i < TG_STATIC_SHARED_PATHS.length ; i ++ ){
					String s = TG_STATIC_SHARED_PATHS[i] + File.separator + resource;
					File file = new File(s);
					if( isExistentAndReadable( file ) ){
						return new FileInputStream( file );
					}
				}
			}
			return TGClassLoader.instance().getClassLoader().getResourceAsStream(resource);
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return null;
	}

	public static URL getResourceUrl(String resource) {
		try {
			if(TG_STATIC_SHARED_PATHS != null){
				for( int i = 0; i < TG_STATIC_SHARED_PATHS.length ; i ++ ){
					File file = new File(TG_STATIC_SHARED_PATHS[i] + File.separator + resource);
					if( isExistentAndReadable( file ) ){
						return file.toURI().toURL();
					}
				}
			}
			return TGClassLoader.instance().getClassLoader().getResource(resource);
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return null;
	}

	public static Enumeration<URL> getResourceUrls(String resource) {
		try {
			Vector<URL> vector = new Vector<URL>();
			if(TG_STATIC_SHARED_PATHS != null){
				for( int i = 0; i < TG_STATIC_SHARED_PATHS.length ; i ++ ){
					File file = new File(TG_STATIC_SHARED_PATHS[i] + File.separator + resource);
					if( isExistentAndReadable( file ) ){
						vector.addElement( file.toURI().toURL() );
					}
				}
			}
			Enumeration<URL> resources = TGClassLoader.instance().getClassLoader().getResources(resource);
			while( resources.hasMoreElements() ){
				URL url = resources.nextElement();
				if( !vector.contains(url) ){
					vector.addElement( url );
				}
			}
			return vector.elements();
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return null;
	}

	private static String getResourcePath(String resource) {
		try {
			if(TG_STATIC_SHARED_PATHS != null){
				for( int i = 0; i < TG_STATIC_SHARED_PATHS.length ; i ++ ){
					File file = new File(TG_STATIC_SHARED_PATHS[i] + File.separator + resource);
					if( isExistentAndReadable( file ) ){
						return file.getAbsolutePath() + File.separator;
					}
				}
			}
			URL url = TGClassLoader.instance().getClassLoader().getResource(resource);
			if(url != null){
				return new File(URLDecoder.decode(url.getPath(), "UTF-8")).getAbsolutePath() + File.separator;
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return null;
	}

	public static void loadClasspath(){
		try {
			Enumeration<URL> plugins = getResourceUrls("plugins");
			while( plugins.hasMoreElements() ){
				URL url = plugins.nextElement();
				TGClassLoader.instance().addPaths(new File(url.getFile()));
			}

			String custompath = System.getProperty(TG_CLASS_PATH);
			if(custompath != null){
				String[] paths = custompath.split(File.pathSeparator);
				for(int i = 0; i < paths.length; i++){
					TGClassLoader.instance().addPaths(new File(paths[i]));
				}
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
	}

	public static void loadLibraries(){
		String libraryPath = System.getProperty(TG_LIBRARY_PATH);
		if(libraryPath != null){
			String[] libraryPaths = libraryPath.split(File.pathSeparator);
			String libraryPrefix = System.getProperty(TG_LIBRARY_PREFIX);
			String libraryExtension = System.getProperty(TG_LIBRARY_EXTENSION);
			for(int i = 0; i < libraryPaths.length; i++){
				TGLibraryLoader.instance().loadLibraries(new File(libraryPaths[i]),libraryPrefix,libraryExtension);
			}
		}
	}

	public static String[] getFileNames( String resource ){
		try {
			String path = getResourcePath(resource);
			if( path != null ){
				File file = new File( path );
				if( isExistentAndReadable( file ) && isDirectoryAndReadable( file )){
					return file.list();
				}
			}
			InputStream stream = getResourceAsStream(resource + "/list.properties" );
			if( stream != null ){
				BufferedReader reader = new BufferedReader( new InputStreamReader(stream) );
				List<String> fileNameList = new ArrayList<String>();
				String fileName = null;
				while( (fileName = reader.readLine()) != null ){
					fileNameList.add( fileName );
				}
				String[] fileNames = new String[ fileNameList.size() ];
				for (int i = 0 ; i < fileNames.length ; i ++ ){
					fileNames[ i ] = fileNameList.get( i );
				}
				return fileNames;
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return null;
	}

	public static Image loadImage(String name){
		return loadImage(TuxGuitar.instance().getConfig().getStringConfigValue(TGConfigKeys.SKIN),name);
	}

	public static Image loadImage(String skin,String name){
		try{
			InputStream stream = getResourceAsStream("skins/" + skin + "/" + name);
			if(stream != null){
				return new Image(TuxGuitar.instance().getDisplay(),new ImageData(stream));
			}
			System.err.println(name + ": not found");
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return new Image(TuxGuitar.instance().getDisplay(),16,16);
	}

	public static boolean isLocalFile(URL url){
		try {
			if(url.getProtocol().equals( new File(url.getFile()).toURI().toURL().getProtocol() ) ){
				return true;
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return false;
	}

	private static String getDefaultUserAppDir(){
		return ((System.getProperty("user.home") + File.separator + ".tuxguitar-" + TGVersion.CURRENT.getVersion()));
	}

	private static String getUserConfigDir(){
		// Look for the system property
		String configPath = System.getProperty(TG_CONFIG_PATH);

		// Default System User Home
		if(configPath == null){
			configPath = (getDefaultUserAppDir() + File.separator + "config");
		}

		// Check if the path exists
		File file = new File(configPath);
		if(!isExistentAndReadable( file )){
			tryCreateDirectory( file );
		}
		return configPath;
	}

	private static String getUserPluginsConfigDir(){
		String configPluginsPath = (getUserConfigDir() + File.separator + "plugins");

		//Check if the path exists
		File file = new File(configPluginsPath);
		if(!isExistentAndReadable( file )){
			tryCreateDirectory( file );
		}

		return configPluginsPath;
	}

	private static String getUserSharedPath(){
		// Look for the system property
		String userSharePath = System.getProperty(TG_USER_SHARE_PATH);

		// Use configuration path as default.
		if( userSharePath == null){
			userSharePath = (getDefaultUserAppDir() + File.separator + "cache");
		}

		// Check if the path exists
		File file = new File(userSharePath);
		if(!isExistentAndReadable( file )){
			tryCreateDirectory( file );
		}
		return userSharePath;
	}

	private static String[] getStaticSharedPaths(){
		String staticSharedPaths = new String(PATH_USER_SHARE_PATH);
		String staticSharedPathsProperty = System.getProperty(TG_SHARE_PATH);
		if( staticSharedPathsProperty != null ){
			staticSharedPaths += (File.pathSeparator + staticSharedPathsProperty);
		}
		return staticSharedPaths.split(File.pathSeparator);
	}

	private static boolean isExistentAndReadable( File file ){
		try{
			return file.exists();
		}catch(SecurityException se){
			return false;
		}
	}

	private static boolean isDirectoryAndReadable( File file ){
		try{
			return file.isDirectory();
		}catch(SecurityException se){
			return false;
		}
	}

	private static boolean tryCreateDirectory( File file ){
		try{
			return file.mkdirs();
		}catch(SecurityException se){
			return false;
		}
	}
}
