/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.edit;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.edit.EditorKit;

/**
 * @author julian
 */
public class SetMouseModeEditionAction extends Action{

	public static final String NAME = "action.edit.set-mouse-mode-edition";

	public SetMouseModeEditionAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | DISABLE_ON_PLAYING | AUTO_UPDATE);
	}

	@Override
	protected int execute(ActionData actionData){
		getEditor().getTablature().getEditorKit().setMouseMode(EditorKit.MOUSE_MODE_EDITION);
		return 0;
	}
}
