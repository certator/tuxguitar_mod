/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.edit;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.undo.CannotRedoException;

/**
 * @author julian
 */
public class RedoAction extends Action{

	public static final String NAME = "action.edit.redo";

	public RedoAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		try {
			if(TuxGuitar.instance().getUndoableManager().canRedo()){
				TuxGuitar.instance().getUndoableManager().redo();
			}
		} catch (CannotRedoException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
}
