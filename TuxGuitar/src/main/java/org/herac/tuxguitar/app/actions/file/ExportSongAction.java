/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.file;

import org.eclipse.swt.SWT;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.actions.ActionLock;
import org.herac.tuxguitar.io.base.TGLocalFileExporter;
import org.herac.tuxguitar.io.base.TGRawExporter;

/**
 * @author julian
 */
public class ExportSongAction extends Action {

	public static final String NAME = "action.file.export";

	public static final String PROPERTY_EXPORTER = "exporter";

	public ExportSongAction() {
		super(NAME, AUTO_LOCK | AUTO_UPDATE );
	}

	@Override
	protected int execute(ActionData actionData){
		Object propertyExporter = actionData.get(PROPERTY_EXPORTER);
		if(!(propertyExporter instanceof TGRawExporter) ){
			return AUTO_UNLOCK;
		}

		final TGRawExporter exporter = (TGRawExporter)propertyExporter;
		if( exporter instanceof TGLocalFileExporter ){
			return this.processLocalFileExporter( (TGLocalFileExporter)exporter );
		}
		return this.processRawExporter( exporter );
	}

	private int processLocalFileExporter( final TGLocalFileExporter exporter ){
		if(!exporter.configure(false)){
			return AUTO_UNLOCK;
		}

		final String fileName = FileActionUtils.chooseFileName(exporter.getFileFormat());
		if(fileName == null){
			return AUTO_UNLOCK;
		}

		TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(!TuxGuitar.isDisposed()){
					FileActionUtils.exportSong(exporter, fileName);
					TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);
					ActionLock.unlock();
				}
			}
		}).start();

		return 0;
	}

	private int processRawExporter( final TGRawExporter exporter ){
		TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(!TuxGuitar.isDisposed()){
					FileActionUtils.exportSong(exporter);
					TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);
					ActionLock.unlock();
				}
			}
		}).start();

		return 0;
	}
}
