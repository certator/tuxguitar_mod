package org.herac.tuxguitar.io.image;

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
import org.herac.tuxguitar.app.util.MessageDialog;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.graphics.TGResourceFactory;
import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.graphics.control.TGLayout;
import org.herac.tuxguitar.io.base.TGRawExporter;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGSong;

public class ImageExporter implements TGRawExporter{

	private static final int PAGE_WIDTH = 550;
	private static final int PAGE_HEIGHT = 800;

	private PrintStyles styles;
	private ImageFormat format;
	private String path;

	@Override
	public String getExportName() {
		return TuxGuitar.getProperty("tuxguitar-image.export-label");
	}

	public PrintStyles getStyles() {
		return this.styles;
	}

	public void setStyles(PrintStyles styles) {
		this.styles = styles;
	}

	public ImageFormat getFormat() {
		return this.format;
	}

	public void setFormat(ImageFormat format) {
		this.format = format;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
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
	public void exportSong(final TGSong song) {
		if( this.path != null ){
			if( this.styles == null ){
				this.styles = getDefaultStyles(song);
			}
			if( this.format == null ){
				this.format = ImageFormat.IMAGE_FORMATS[0];
			}
			export(song);
		}
	}

	public void export(final TGSong song){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					TGSongManager manager = new TGSongManager();
					manager.setFactory(new TGFactoryImpl());
					manager.setSong(song.clone(manager.getFactory()));

					export(manager);
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}

	public void export(final TGSongManager manager){
		new SyncThread(new Runnable() {
			@Override
			public void run() {
				try{
					TGResourceFactory factory = new TGResourceFactoryImpl(TuxGuitar.instance().getDisplay());

					PrintController controller = new PrintController(manager, factory);
					PrintLayout layout = new PrintLayout(controller, getStyles());

					export(layout);
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}

	public void export(final PrintLayout layout){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					layout.loadStyles(1f);
					layout.updateSong();
					layout.makeDocument(new PrintDocumentImpl(new TGRectangle(25,25,PAGE_WIDTH,PAGE_HEIGHT), getFormat(), getPath() ));
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}

	private class PrintDocumentImpl implements PrintDocument{

		private TGPainterImpl painter;
		private TGRectangle bounds;
		private String path;
		private Image buffer;
		private List<ImageData> pages;
		private ImageFormat format;

		public PrintDocumentImpl(TGRectangle bounds, ImageFormat format, String path){
			this.bounds = bounds;
			this.path = path;
			this.painter = new TGPainterImpl();
			this.pages = new ArrayList<ImageData>();
			this.format = format;
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
			this.buffer = new Image(TuxGuitar.instance().getDisplay(),this.bounds.getWidth() + (this.bounds.getX() * 2), this.bounds.getHeight() + (this.bounds.getY() * 2) );
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
			try {
				ImageWriter.write(this.format, this.path, this.pages);
			} catch (Throwable throwable) {
				MessageDialog.errorMessage(throwable);
			}
		}

		@Override
		public boolean isPaintable(int page) {
			return true;
		}
	}
}
