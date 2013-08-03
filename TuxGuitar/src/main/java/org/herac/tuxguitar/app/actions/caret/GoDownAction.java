/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.caret;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 */
public class GoDownAction extends Action{

	public static final String NAME = "action.caret.go-down";

	public GoDownAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | DISABLE_ON_PLAYING | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		getEditor().getTablature().getCaret().moveDown();
		return 0;
	}
}
