package org.herac.tuxguitar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.herac.tuxguitar.app.system.plugins.TGPlugin;

public class TGServiceReader {

	private static final String SERVICE_PATH = new String("META-INF/services/");

	public static Iterator<TGPlugin> lookupProviders(Class<?> spi){
		return lookupProviders(spi,TGClassLoader.instance().getClassLoader());
	}

	public static Iterator<TGPlugin> lookupProviders(Class<?> spi,ClassLoader loader){
		try{
			if (spi == null || loader == null){
				throw new IllegalArgumentException();
			}
			return new IteratorImpl(spi,loader,loader.getResources(SERVICE_PATH + spi.getName()));
		}catch (IOException ioex){
			return Collections.emptyIterator();
		}
	}

	private static final class IteratorImpl implements Iterator<TGPlugin> {
		private final Class<?> spi;
		private final ClassLoader loader;
		private final Enumeration<URL> urls;
		private Iterator<String> iterator;

		public IteratorImpl(Class<?> spi,ClassLoader loader,Enumeration<URL> urls){
			this.spi = spi;
			this.loader = loader;
			this.urls = urls;
			this.initialize();
		}

		private void initialize(){
			List<String> providers = new ArrayList<String>();
			while (this.urls.hasMoreElements()) {
				URL url = this.urls.nextElement();
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
					String line = null;
					while((line = reader.readLine()) != null){
						String provider = uncommentLine(line).trim();
						if(provider != null && provider.length() > 0){
							providers.add(provider);
						}
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			this.iterator = providers.iterator();
		}

		private String uncommentLine(String line){
			int index = line.indexOf('#');
			if(index >= 0){
				return (line.substring(0,index));
			}
			return line;
		}

		@Override
		public boolean hasNext() {
			return (this.iterator != null && this.iterator.hasNext());
		}

		@Override
		public TGPlugin next() {
			if (!hasNext()){
				throw new NoSuchElementException();
			}
			try {
				Object provider = this.loader.loadClass( this.iterator.next() ).newInstance();
				if(this.spi.isInstance(provider)){
					return (TGPlugin) provider;
				}
			} catch (Throwable throwable) {
				throwable.printStackTrace();
				throw new RuntimeException(throwable);
			}
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
