/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.track;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 * @author julian
 */
public class GoNextTrackAction extends Action{

	public static final String NAME = "action.track.go-next";

	public GoNextTrackAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		Caret caret = getEditor().getTablature().getCaret();
		TGTrack track = getSongManager().getTrack(caret.getTrack().getNumber() + 1);
		if(track != null){
			caret.update(track.getNumber());
		}
		return 0;
	}
}
