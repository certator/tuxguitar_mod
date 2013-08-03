/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.track;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.track.UndoableCloneTrack;

/**
 * @author julian
 */
public class CloneTrackAction extends Action{

	public static final String NAME = "action.track.clone";

	public CloneTrackAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		//comienza el undoable
		UndoableCloneTrack undoable = UndoableCloneTrack.startUndo();
		TuxGuitar.instance().getFileHistory().setUnsavedFile();

		Caret caret = getEditor().getTablature().getCaret();

		getSongManager().cloneTrack(caret.getTrack());
		updateTablature();

		//termia el undoable
		addUndoableEdit(undoable.endUndo());

		return 0;
	}
}
