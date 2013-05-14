/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.view;

import org.eclipse.swt.SWT;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 */
public class ShowFretBoardAction extends Action{

	public static final String NAME = "action.view.show-fretboard";

	public ShowFretBoardAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);
		if(TuxGuitar.instance().getFretBoardEditor().isVisible()){
			TuxGuitar.instance().getFretBoardEditor().hideFretBoard();
		}else{
			TuxGuitar.instance().getFretBoardEditor().showFretBoard();
		}
		TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);
		return 0;
	}
}
