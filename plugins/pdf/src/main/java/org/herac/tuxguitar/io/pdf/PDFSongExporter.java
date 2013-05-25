package org.herac.tuxguitar.io.pdf;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.editors.TGPainterImpl;
import org.herac.tuxguitar.app.editors.TGResourceFactoryImpl;
import org.herac.tuxguitar.app.helper.SyncThread;
import org.herac.tuxguitar.app.printer.PrintController;
import org.herac.tuxguitar.app.printer.PrintDocument;
import org.herac.tuxguitar.app.printer.PrintLayout;
import org.herac.tuxguitar.app.printer.PrintStyles;
import org.herac.tuxguitar.app.printer.PrintStylesDialog;
import org.herac.tuxguitar.app.util.MessageDialog;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.graphics.TGResourceFactory;
import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.graphics.control.TGLayout;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGLocalFileExporter;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGSong;

public class PDFSongExporter implements TGLocalFileExporter{
	
	private static final int PAGE_WIDTH = 550;
	private static final int PAGE_HEIGHT = 800;
	
	private PrintStyles styles;
	private OutputStream stream;
	
	@Override
	public String getExportName() {
		return "PDF";
	}
	
	@Override
	public TGFileFormat getFileFormat() {
		return new TGFileFormat("PDF","*.pdf");
	}
	
	public PrintStyles getDefaultStyles(TGSong song){
		PrintStyles styles = new PrintStyles();
		styles.setStyle(TGLayout.DISPLAY_TABLATURE);
		styles.setFromMeasure(1);
		styles.setToMeasure(song.countMeasureHeaders());
		styles.setTrackNumber(1);
		styles.setBlackAndWhite(false);
		return styles;
	}
	
	@Override
	public boolean configure(boolean setDefaults) {
		this.styles = (!setDefaults ? PrintStylesDialog.open(TuxGuitar.instance().getShell()) : null );
		return ( this.styles != null || setDefaults );
	}
	
	@Override
	public void init(TGFactory factory,OutputStream stream){
		this.stream = stream;
	}
	
	@Override
	public void exportSong(TGSong song) {
		try{
			if( this.stream != null ){
				this.export(this.stream,song, (this.styles != null ? this.styles : getDefaultStyles(song)) );
			}
		}catch(Throwable throwable){
			return;
		}
	}
	
	public void export(final OutputStream stream,final TGSong song,final PrintStyles data){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					TGSongManager manager = new TGSongManager();
					manager.setFactory(new TGFactoryImpl());
					manager.setSong(song.clone(manager.getFactory()));
					
					export(stream,manager,data);
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}
	
	public void export(final OutputStream stream,final TGSongManager manager, final PrintStyles data){
		new SyncThread(new Runnable() {
			@Override
			public void run() {
				try{
					TGResourceFactory factory = new TGResourceFactoryImpl(TuxGuitar.instance().getDisplay());
					
					PrintController controller = new PrintController(manager, factory);
					PrintLayout layout = new PrintLayout(controller,data);
					
					export(stream, layout);
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}
	
	public void export(final OutputStream stream, final PrintLayout layout){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					layout.loadStyles(1f);
					layout.updateSong();
					layout.makeDocument(new PrintDocumentImpl(new TGRectangle(0,0,PAGE_WIDTH,PAGE_HEIGHT), stream));
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}
	
	private class PrintDocumentImpl implements PrintDocument{
		
		private TGPainterImpl painter;
		private TGRectangle bounds;
		private OutputStream stream;
		private Image buffer;
		private List<ImageData> pages;
		
		public PrintDocumentImpl(TGRectangle bounds, OutputStream stream){
			this.bounds = bounds;
			this.stream = stream;
			this.painter = new TGPainterImpl();
			this.pages = new ArrayList<ImageData>();
		}
		
		@Override
		public TGPainter getPainter() {
			return this.painter;
		}
		
		@Override
		public TGRectangle getBounds(){
			return this.bounds;
		}
		
		@Override
		public void pageStart() {
			this.buffer = new Image(TuxGuitar.instance().getDisplay(),this.bounds.getWidth() - this.bounds.getX(), this.bounds.getHeight() - this.bounds.getY());
			this.painter.init( this.buffer );
		}
		
		@Override
		public void pageFinish() {
			this.pages.add( this.buffer.getImageData() );
			this.painter.dispose();
			this.buffer.dispose();
		}
		
		@Override
		public void start() {
			// Not implemented
		}
		
		@Override
		public void finish() {
			try{
				PDFWriter.write(this.stream,this.pages);
			}catch(Throwable throwable){
				MessageDialog.errorMessage(throwable);
			}
		}
		
		@Override
		public boolean isPaintable(int page) {
			return true;
		}
	}
}
