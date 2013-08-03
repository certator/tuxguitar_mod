/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.file;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
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
import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.util.TGSynchronizer;

/**
 * @author julian
 */
public class PrintAction extends Action{

	public static final String NAME = "action.file.print";

	public PrintAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		try{
			final PrintStyles data = PrintStylesDialog.open(TuxGuitar.instance().getShell());
			if(data != null){
				PrintDialog dialog = new PrintDialog(TuxGuitar.instance().getShell(), SWT.NULL);
				PrinterData printerData = dialog.open();

				if (printerData != null) {
					TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);

					this.print(printerData, data);
				}
			}
		}catch(Throwable throwable ){
			MessageDialog.errorMessage(throwable);
		}
		return 0;
	}

	public void print(final PrinterData printerData ,final PrintStyles data){
		try{
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						final TGSongManager manager = new TGSongManager();
						manager.setFactory(new TGFactoryImpl());
						manager.setSong(getSongManager().getSong().clone(manager.getFactory()));

						new SyncThread(new Runnable() {
							@Override
							public void run() {
								try{
									Printer printer = new Printer(printerData);
									PrintController controller = new PrintController(manager, new TGResourceFactoryImpl(printer));
									PrintLayout layout = new PrintLayout(controller,data);

									print(printer, printerData, layout , getPrinterArea(printer,0.5), getPrinterScale(printer) );
								}catch(Throwable throwable ){
									MessageDialog.errorMessage(throwable);
								}
							}
						}).start();
					}catch(Throwable throwable ){
						MessageDialog.errorMessage(throwable);
					}
				}
			}).start();
		}catch(Throwable throwable ){
			MessageDialog.errorMessage(throwable);
		}
	}

	protected void print(final Printer printer,final PrinterData printerData ,final PrintLayout layout, final TGRectangle bounds, final float scale){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					layout.loadStyles(scale);
					layout.updateSong();
					layout.makeDocument(new PrintDocumentImpl(layout,printer, printerData, bounds));
				}catch(Throwable throwable ){
					MessageDialog.errorMessage(throwable);
				}
			}
		}).start();
	}

	protected TGRectangle getPrinterArea(Printer printer,double margin) {
		Rectangle clientArea = printer.getClientArea();
		Rectangle trim = printer.computeTrim(0, 0, 0, 0);
		Point dpi = printer.getDPI();

		int x = (int) (margin * dpi.x) - trim.x;
		int y = (int) (margin * dpi.y) - trim.y;
		int width = clientArea.width + trim.width - (int) (margin * dpi.x) - trim.x;
		int height = clientArea.height + trim.height - (int) (margin * dpi.y) - trim.y;

		return new TGRectangle(x,y,width,height);
	}

	protected float getPrinterScale(Printer printer) {
		Point dpi = printer.getDPI();
		if( dpi != null ){
			return ( dpi.x / 100.0f );
		}
		return 1.0f;
	}

	private class PrintDocumentImpl implements PrintDocument{

		private Printer printer;
		private PrinterData printerData;
		private PrintLayout layout;
		private TGPainterImpl painter;
		private TGRectangle bounds;
		private boolean started;

		public PrintDocumentImpl(PrintLayout layout, Printer printer,PrinterData printerData, TGRectangle bounds){
			this.layout = layout;
			this.printer = printer;
			this.printerData = printerData;
			this.bounds = bounds;
			this.painter = new TGPainterImpl();
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
			if(this.started){
				this.printer.startPage();
				this.painter.init(new GC(this.printer));
			}
		}

		@Override
		public void pageFinish() {
			if(this.started){
				this.painter.dispose();
				this.printer.endPage();
			}
		}

		@Override
		public void start() {
			this.started = this.printer.startJob(getJobName());
		}

		@Override
		public void finish() {
			if(this.started){
				this.printer.endJob();
				this.started = false;
				try {
					TGSynchronizer.instance().addRunnable(new TGSynchronizer.TGRunnable(){
						@Override
						public void run() {
							dispose();
						}
					});
				} catch (Throwable e) {
					e.printStackTrace();
				}
				TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);
			}
		}

		@Override
		public boolean isPaintable(int page){
			if(this.printerData.scope == PrinterData.PAGE_RANGE){
				if(this.printerData.startPage > 0 && this.printerData.startPage > page){
					return false;
				}
				if(this.printerData.endPage > 0 && this.printerData.endPage < page){
					return false;
				}
			}
			return true;
		}

		public String getJobName(){
			String prefix = TuxGuitar.APPLICATION_NAME;
			String song = this.layout.getSongManager().getSong().getName();
			return ( song != null && song.length() > 0 ? (prefix + "-" + song) : prefix );
		}

		public void dispose(){
			if(!this.printer.isDisposed()){
				this.printer.dispose();
			}
		}
	}
}