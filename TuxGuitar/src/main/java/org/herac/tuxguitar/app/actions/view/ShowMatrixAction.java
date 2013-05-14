/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.view;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 */
public class ShowMatrixAction extends Action{

	public static final String NAME = "action.view.show-matrix";

	public ShowMatrixAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		if(TuxGuitar.instance().getMatrixEditor().isDisposed()){
			TuxGuitar.instance().getMatrixEditor().show();
		}else{
			TuxGuitar.instance().getMatrixEditor().dispose();
		}
		return 0;
	}
}
