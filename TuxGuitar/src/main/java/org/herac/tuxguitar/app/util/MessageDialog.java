/*
 * Created on 20-dic-2005
 */
package org.herac.tuxguitar.app.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.ActionLock;
import org.herac.tuxguitar.app.helper.SyncThread;

/**
 * @author julian
 */
public class MessageDialog {

	private int style;
	private String name;
	private String message;

	protected MessageDialog(String name,String message,int style){
		this.name = name;
		this.message = message;
		this.style = style;
	}

	protected void show(Shell parent){
		MessageBox messageBox = new MessageBox(parent, this.style);
		messageBox.setText(this.name);
		messageBox.setMessage(this.message);
		messageBox.open();
	}

	public static void infoMessage(final String title,final String message){
		MessageDialog.infoMessage(TuxGuitar.instance().getShell(), title, message);
	}

	public static void infoMessage(final Shell shell,final String title,final String message){
		new SyncThread(new Runnable() {
			@Override
			public void run() {
				if(!shell.isDisposed()){
					new MessageDialog(title,message,SWT.ICON_INFORMATION).show(shell);
				}
			}
		}).start();
	}

	public static void errorMessage(final Throwable throwable){
		MessageDialog.errorMessage(TuxGuitar.instance().getShell(),throwable);
	}

	public static void errorMessage(final Shell shell,final Throwable throwable){
		MessageDialog.errorMessage(shell, (throwable.getMessage() != null ? throwable.getMessage() : throwable.getClass().getName() ));
		new Thread(new Runnable() {
			@Override
			public void run() {
				throwable.printStackTrace();
			}
		}).start();
	}

	public static void errorMessage(final Shell shell,final String message){
		if(!shell.isDisposed()){
			new SyncThread(new Runnable() {
				@Override
				public void run() {
					if(!shell.isDisposed()){
						ActionLock.unlock();
						TuxGuitar.instance().unlock();
						shell.setCursor(shell.getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
						new MessageDialog(TuxGuitar.getProperty("error"),message,SWT.ICON_ERROR).show(shell);
					}
				}
			}).start();
		}
	}

}
