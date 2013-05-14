/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.edit;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 */
public class SetVoice2Action extends Action{

	public static final String NAME = "action.edit.voice-2";

	public SetVoice2Action() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		getEditor().getTablature().getCaret().setVoice(1);
		return 0;
	}
}
