/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.marker;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.marker.MarkerNavigator;

/**
 * @author julian
 */
public class GoFirstMarkerAction extends Action{

	public static final String NAME = "action.marker.go-first";

	public GoFirstMarkerAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE);
	}

	@Override
	protected int execute(ActionData actionData){
		new MarkerNavigator().goToSelectedMarker(getSongManager().getFirstMarker());

		return 0;
	}
}
