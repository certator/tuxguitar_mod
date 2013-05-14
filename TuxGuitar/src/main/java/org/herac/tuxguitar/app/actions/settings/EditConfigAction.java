/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.settings;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.system.config.TGConfigEditor;

/**
 * @author julian
 */
public class EditConfigAction extends Action{

	public static final String NAME = "action.settings.configure";

	public EditConfigAction() {
		super(NAME, AUTO_LOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE );
	}

	@Override
	protected int execute(ActionData actionData){
		new TGConfigEditor().showDialog(TuxGuitar.instance().getShell());
		return 0;
	}
}
