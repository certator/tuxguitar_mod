/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.actions.view;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowPianoAction extends Action{

	public static final String NAME = "action.view.show-piano";

	public ShowPianoAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		if(TuxGuitar.instance().getPianoEditor().isDisposed()){
			TuxGuitar.instance().getPianoEditor().show();
		}else{
			TuxGuitar.instance().getPianoEditor().dispose();
		}
		return 0;
	}
}
