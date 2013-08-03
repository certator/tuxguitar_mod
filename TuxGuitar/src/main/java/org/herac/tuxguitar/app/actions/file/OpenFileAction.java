/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.file;

import java.io.File;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.actions.ActionLock;
import org.herac.tuxguitar.app.helper.SyncThread;
import org.herac.tuxguitar.app.util.ConfirmDialog;
import org.herac.tuxguitar.app.util.FileChooser;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.util.TGSynchronizer;

/**
 * @author julian
 */
public class OpenFileAction extends Action {

	public static final String NAME = "action.file.open";

	public static final String PROPERTY_URL = "url";

	public OpenFileAction() {
		super(NAME, AUTO_LOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		final Object propertyUrl = actionData.get(PROPERTY_URL);

		TuxGuitar.instance().getPlayer().reset();

		if(TuxGuitar.instance().getFileHistory().isUnsavedFile()){
			ConfirmDialog confirm = new ConfirmDialog(TuxGuitar.getProperty("file.save-changes-question"));
			confirm.setDefaultStatus( ConfirmDialog.STATUS_CANCEL );
			int status = confirm.confirm(ConfirmDialog.BUTTON_YES | ConfirmDialog.BUTTON_NO | ConfirmDialog.BUTTON_CANCEL, ConfirmDialog.BUTTON_YES);
			if(status == ConfirmDialog.STATUS_CANCEL){
				return AUTO_UNLOCK;
			}
			if(status == ConfirmDialog.STATUS_YES){
				final String fileName = FileActionUtils.getFileName();
				if(fileName == null){
					return AUTO_UNLOCK;
				}
				TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);
				new Thread(new Runnable() {
					@Override
					public void run() {
						if(!TuxGuitar.isDisposed()){
							FileActionUtils.save(fileName);
							new SyncThread(new Runnable() {
								@Override
								public void run() {
									if(!TuxGuitar.isDisposed()){
										TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);
										openFile( propertyUrl );
									}
								}
							}).start();
						}
					}
				}).start();
				return 0;
			}
		}
		openFile( propertyUrl );

		return 0;
	}

	protected void openFile(Object data){
		final URL url = getOpenFileName(data);
		if(url == null){
			ActionLock.unlock();
			return;
		}
		TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);
		try {
			TGSynchronizer.instance().runLater(new TGSynchronizer.TGRunnable() {
				@Override
				public void run() throws Throwable {
					new Thread(new Runnable() {
						@Override
						public void run() {
							if(!TuxGuitar.isDisposed()){
								FileActionUtils.open(url);
								TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);
								ActionLock.unlock();
							}
						}
					}).start();
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected URL getOpenFileName(Object data){
		try{
			if(data instanceof URL){
				TuxGuitar.instance().getFileHistory().setChooserPath( (URL)data );
				return (URL)data;
			}
			String path = FileChooser.instance().open(TuxGuitar.instance().getShell(),TGFileFormatManager.instance().getInputFormats());
			if(path != null){
				File file = new File(path);
				if( file.exists() && file.isFile() ){
					return file.toURI().toURL();
				}
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return null;
	}
}