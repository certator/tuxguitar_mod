/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.marker;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.marker.MarkerNavigator;

/**
 * @author julian
 */
public class GoNextMarkerAction extends Action{

	public static final String NAME = "action.marker.go-next";

	public GoNextMarkerAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE );
	}

	@Override
	protected int execute(ActionData actionData){
		Caret caret = getEditor().getTablature().getCaret();

		new MarkerNavigator().goToSelectedMarker(getSongManager().getNextMarker(caret.getMeasure().getNumber()));

		return 0;
	}
}
