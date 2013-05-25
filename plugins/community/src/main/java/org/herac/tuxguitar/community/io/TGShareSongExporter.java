package org.herac.tuxguitar.community.io;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.base.TGRawExporter;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGSong;

public class TGShareSongExporter implements TGRawExporter { 
	
	public TGShareSongExporter(){
		super();
	}
	
	@Override
	public void exportSong(TGSong srcSong) throws TGFileFormatException {
		final TGSong song = srcSong.clone(new TGFactory());
		new Thread( new Runnable() {
			@Override
			public void run() {
				new TGShareSong().process( song );
			}
		} ).start();
	}
	
	@Override
	public String getExportName() {
		return ("Share with the Community");
	}
}
