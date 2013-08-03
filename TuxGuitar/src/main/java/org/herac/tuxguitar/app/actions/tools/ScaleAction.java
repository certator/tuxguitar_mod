/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.tools;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.tools.scale.ScaleEditor;

/**
 * @author julian
 */
public class ScaleAction extends Action{

	public static final String NAME = "action.tools.scale";

	public ScaleAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE);
	}

	@Override
	protected int execute(ActionData actionData){
		new ScaleEditor().show();
		return 0;
	}
}
