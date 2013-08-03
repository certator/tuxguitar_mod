/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.TGPainterImpl;
import org.herac.tuxguitar.app.editors.TGResourceFactoryImpl;
import org.herac.tuxguitar.app.helper.SyncThread;
import org.herac.tuxguitar.app.printer.PrintController;
import org.herac.tuxguitar.app.printer.PrintDocument;
import org.herac.tuxguitar.app.printer.PrintLayout;
import org.herac.tuxguitar.app.printer.PrintPreview;
import org.herac.tuxguitar.app.printer.PrintStyles;
import org.herac.tuxguitar.app.printer.PrintStylesDialog;
import org.herac.tuxguitar.app.util.MessageDialog;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.graphics.TGResourceFactory;
import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.util.TGSynchronizer;

/**
 * @author julian
 */
public class PrintPreviewAction extends Action{

	public static final String NAME = "action.file.print-preview";

	public PrintPreviewAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		try{
			final PrintStyles data = PrintStylesDialog.open(TuxGuitar.instance().getShell());
			if(data != null){
				TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);

				this.printPreview(data);
			}
		}catch(Throwable throwable){
			MessageDialog.errorMessage(throwable);
		}
		return 0;
	}

	public void printPreview(final PrintStyles data){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					final TGSongManager manager = new TGSongManager();
					manager.setFactory(new TGFactoryImpl());
					manager.setSong(getSongManager().getSong().clone(manager.getFactory()));

					printPreview(manager,data);
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}

	public void printPreview(final TGSongManager manager, final PrintStyles data){
		new SyncThread(new Runnable() {
			@Override
			public void run() {
				try{
					TGResourceFactory factory = new TGResourceFactoryImpl(TuxGuitar.instance().getDisplay());
					PrintController controller = new PrintController(manager, factory);
					PrintLayout layout = new PrintLayout(controller,data);

					printPreview( layout );
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}

	public void printPreview(final PrintLayout layout){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					layout.loadStyles(1f);
					layout.updateSong();
					layout.makeDocument(new PrintDocumentImpl(new TGRectangle(0,0,850,1050)));
				}catch(Throwable throwable){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}

	private class PrintDocumentImpl implements PrintDocument{

		private final TGPainterImpl painter;
		private final TGRectangle bounds;
		private final List<Image> pages;

		public PrintDocumentImpl(TGRectangle bounds){
			this.bounds = bounds;
			this.painter = new TGPainterImpl();
			this.pages = new ArrayList<Image>();
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
			Image page = new Image(TuxGuitar.instance().getDisplay(),this.bounds.getWidth() - this.bounds.getX(), this.bounds.getHeight() - this.bounds.getY());
			this.painter.init( page );
			this.pages.add( page );
		}

		@Override
		public void pageFinish() {
			this.painter.dispose();
		}

		@Override
		public void start() {
			// Not implemented
		}

		@Override
		public void finish() {
			final TGRectangle bounds = this.bounds;
			final List<Image> pages = this.pages;
			try {
				TGSynchronizer.instance().addRunnable(new TGSynchronizer.TGRunnable(){
					@Override
					public void run() {
						PrintPreview preview = new PrintPreview(pages,bounds);
						preview.showPreview(getEditor().getTablature().getShell());
						Iterator<Image> it = pages.iterator();
						while(it.hasNext()){
							Image image = it.next();
							image.dispose();
						}
					}
				});
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean isPaintable(int page) {
			return true;
		}
	}
}
