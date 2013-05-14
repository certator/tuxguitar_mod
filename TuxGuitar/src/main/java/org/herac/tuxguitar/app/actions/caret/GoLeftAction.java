/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.caret;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 */
public class GoLeftAction extends Action{

	public static final String NAME = "action.caret.go-left";

	public GoLeftAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		if(TuxGuitar.instance().getPlayer().isRunning()){
			TuxGuitar.instance().getTransport().gotoPrevious();
		}
		else{
			getEditor().getTablature().getCaret().moveLeft();
		}
		return 0;
	}
}
