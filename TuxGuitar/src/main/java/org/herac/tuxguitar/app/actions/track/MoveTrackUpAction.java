/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.track;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.track.UndoableMoveTrackUp;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 * @author julian
 */
public class MoveTrackUpAction extends Action{

	public static final String NAME = "action.track.move-up";

	public MoveTrackUpAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		//comienza el undoable
		UndoableMoveTrackUp undoable = UndoableMoveTrackUp.startUndo();

		Caret caret = getEditor().getTablature().getCaret();
		TGTrack track = caret.getTrack();

		if(getSongManager().moveTrackUp(track)){
			updateTablature();

			//termia el undoable
			addUndoableEdit(undoable.endUndo(track));
			TuxGuitar.instance().getFileHistory().setUnsavedFile();
		}
		return 0;
	}
}
