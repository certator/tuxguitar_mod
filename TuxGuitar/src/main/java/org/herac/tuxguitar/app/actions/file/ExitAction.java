/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.file;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.actions.ActionLock;

/**
 * @author julian
 */
public class ExitAction extends Action {

	public static final String NAME = "action.file.exit";

	public ExitAction() {
		super(NAME, AUTO_LOCK | KEY_BINDING_AVAILABLE );
	}

	@Override
	protected int execute(ActionData actionData){
		ActionLock.unlock();
		TuxGuitar.instance().getShell().close();
		return 0;
	}
}
