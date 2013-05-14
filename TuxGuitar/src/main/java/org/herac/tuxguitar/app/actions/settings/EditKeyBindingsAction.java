/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.settings;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.system.keybindings.editor.KeyBindingEditor;

/**
 * @author julian
 */
public class EditKeyBindingsAction extends Action{

	public static final String NAME = "action.settings.key-bindings";

	public EditKeyBindingsAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE);
	}

	@Override
	protected int execute(ActionData actionData){
		new KeyBindingEditor().show(TuxGuitar.instance().getShell());
		return 0;
	}
}
