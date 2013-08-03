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
public class GoFirstTrackAction extends Action{

	public static final String NAME = "action.track.go-first";

	public GoFirstTrackAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		Caret caret = getEditor().getTablature().getCaret();
		TGTrack track = getSongManager().getFirstTrack();
		if(track != null){
			caret.update(track.getNumber());
		}
		return 0;
	}
}
