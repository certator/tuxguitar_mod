/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.track;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.graphics.control.TGTrackImpl;

/**
 * @author julian
 */
public class GoToTrackAction extends Action{

	public static final String NAME = "action.track.goto";

	public static final String PROPERTY_TRACK = "track";

	public GoToTrackAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE);
	}

	@Override
	protected int execute(ActionData actionData){
		Object data = actionData.get(PROPERTY_TRACK);
		if(data instanceof TGTrackImpl){
			TGTrackImpl track = (TGTrackImpl)data;
			getEditor().getTablature().getCaret().update(track.getNumber());
		}
		return 0;
	}
}
