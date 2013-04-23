package org.herac.tuxguitar.io.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.io.tg.TGInputStream;
import org.herac.tuxguitar.io.tg.TGOutputStream;
import org.herac.tuxguitar.io.tg.TGStream;

public class TGFileFormatManager {
	
	public static final String DEFAULT_EXTENSION = TGStream.TG_FORMAT_EXTENSION;
	
	private static TGFileFormatManager instance;
	
	private final TGSongLoader loader;
	private final TGSongWriter writer;
	private final List<TGInputStreamBase> inputStreams;
	private final List<TGOutputStreamBase> outputStreams;
	private final List<TGRawExporter> exporters;
	private final List<TGRawImporter> importers;
	
	private TGFileFormatManager(){
		this.loader = new TGSongLoader();
		this.writer = new TGSongWriter();
		this.inputStreams = new ArrayList<TGInputStreamBase>();
		this.outputStreams = new ArrayList<TGOutputStreamBase>();
		this.exporters = new ArrayList<TGRawExporter>();
		this.importers = new ArrayList<TGRawImporter>();
		this.addDefaultStreams();
	}
	
	public static TGFileFormatManager instance(){
		if(instance == null){
			instance = new TGFileFormatManager();
		}
		return instance;
	}
	
	public TGSongLoader getLoader(){
		return this.loader;
	}
	
	public TGSongWriter getWriter(){
		return this.writer;
	}
	
	public void addInputStream(TGInputStreamBase stream){
		this.inputStreams.add(stream);
	}
	
	public void removeInputStream(TGInputStreamBase stream){
		this.inputStreams.remove(stream);
	}
	
	public int countInputStreams(){
		return this.inputStreams.size();
	}
	
	public void addOutputStream(TGOutputStreamBase stream){
		this.outputStreams.add(stream);
	}
	
	public void removeOutputStream(TGOutputStreamBase stream){
		this.outputStreams.remove(stream);
	}
	
	public int countOutputStreams(){
		return this.outputStreams.size();
	}
	
	public void addImporter(TGRawImporter importer){
		this.importers.add(importer);
	}
	
	public void removeImporter(TGRawImporter importer){
		this.importers.remove(importer);
	}
	
	public int countImporters(){
		return this.importers.size();
	}
	
	public void addExporter(TGRawExporter exporter){
		this.exporters.add(exporter);
	}
	
	public void removeExporter(TGRawExporter exporter){
		this.exporters.remove(exporter);
	}
	
	public int countExporters(){
		return this.exporters.size();
	}
	
	public Iterator<TGInputStreamBase> getInputStreams(){
		return this.inputStreams.iterator();
	}
	
	public Iterator<TGOutputStreamBase> getOutputStreams(){
		return this.outputStreams.iterator();
	}
	
	public Iterator<TGRawImporter> getImporters(){
		return this.importers.iterator();
	}
	
	public Iterator<TGRawExporter> getExporters(){
		return this.exporters.iterator();
	}
	
	public List<TGFileFormat> getInputFormats(){
		List<TGFileFormat> formats = new ArrayList<TGFileFormat>();
		Iterator<TGInputStreamBase> it = getInputStreams();
		while(it.hasNext()){
			TGInputStreamBase stream = it.next();
			TGFileFormat format = stream.getFileFormat();
			if(!existsFormat(format, formats)){
				formats.add(format);
			}
		}
		return formats;
	}
	
	public List<TGFileFormat> getOutputFormats(){
		List<TGFileFormat> formats = new ArrayList<TGFileFormat>();
		Iterator<TGOutputStreamBase> it = getOutputStreams();
		while(it.hasNext()){
			TGOutputStreamBase stream = it.next();
			TGFileFormat format = stream.getFileFormat();
			if(!existsFormat(format, formats)){
				formats.add(format);
			}
		}
		return formats;
	}
	
	private boolean existsFormat(TGFileFormat format,List<TGFileFormat> formats){
		Iterator<TGFileFormat> it = formats.iterator();
		while(it.hasNext()){
			TGFileFormat comparator = it.next();
			if(comparator.getName().equals(format.getName()) || comparator.getSupportedFormats().equals(format.getSupportedFormats())){
				return true;
			}
		}
		return false;
	}
	
	private void addDefaultStreams(){
		this.addInputStream(new TGInputStream());
		this.addOutputStream(new TGOutputStream());
	}
}
