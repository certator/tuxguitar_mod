/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.edit;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 */
public class SetVoice1Action extends Action{

	public static final String NAME = "action.edit.voice-1";

	public SetVoice1Action() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		getEditor().getTablature().getCaret().setVoice(0);
		return 0;
	}
}
