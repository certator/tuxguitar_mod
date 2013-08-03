package org.herac.tuxguitar.io.ascii;

import java.io.OutputStream;

import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGLocalFileExporter;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGSong;

public class ASCIISongExporter implements TGLocalFileExporter{

	private OutputStream stream;

	@Override
	public String getExportName() {
		return "ASCII";
	}

	@Override
	public TGFileFormat getFileFormat() {
		return new TGFileFormat("ASCII","*.tab");
	}

	@Override
	public boolean configure(boolean setDefaults) {
		return true;
	}

	@Override
	public void init(TGFactory factory,OutputStream stream){
		this.stream = stream;
	}

	@Override
	public void exportSong(TGSong song) {
		if( this.stream != null ){
			new ASCIITabOutputStream(this.stream).writeSong(song);
		}
	}

}