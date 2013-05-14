/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.edit;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 */
public class SetNaturalKeyAction extends Action{

	public static final String NAME = "action.edit.set-natural-key";

	public SetNaturalKeyAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | DISABLE_ON_PLAYING | AUTO_UPDATE);
	}

	@Override
	protected int execute(ActionData actionData){
		getEditor().getTablature().getEditorKit().setNatural(!getEditor().getTablature().getEditorKit().isNatural());
		return 0;
	}
}
