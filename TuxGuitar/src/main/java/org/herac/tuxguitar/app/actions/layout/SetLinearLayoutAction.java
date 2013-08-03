/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.layout;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Tablature;
import org.herac.tuxguitar.graphics.control.TGLayoutHorizontal;

/**
 * @author julian
 */
public class SetLinearLayoutAction extends Action{

	public static final String NAME = "action.view.layout-set-linear";

	public SetLinearLayoutAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		Tablature tablature = getEditor().getTablature();
		tablature.setViewLayout(new TGLayoutHorizontal(tablature,tablature.getViewLayout().getStyle()));
		updateTablature();
		return 0;
	}
}
