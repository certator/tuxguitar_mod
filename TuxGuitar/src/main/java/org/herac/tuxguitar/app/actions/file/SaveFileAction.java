/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.file;

import org.eclipse.swt.SWT;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.actions.ActionLock;

/**
 * @author julian
 */
public class SaveFileAction extends Action{

	public static final String NAME = "action.file.save";

	public SaveFileAction() {
		super(NAME, AUTO_LOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE );
	}

	@Override
	protected int execute(ActionData actionData){
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
					TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);
					ActionLock.unlock();
				}
			}
		}).start();

		return 0;
	}
}
