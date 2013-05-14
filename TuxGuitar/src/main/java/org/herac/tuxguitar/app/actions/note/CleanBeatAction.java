/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.note;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGBeat;

/**
 * @author julian
 */
public class CleanBeatAction extends Action{

	public static final String NAME = "action.note.general.clean-beat";

	public CleanBeatAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TGBeat beat = getEditor().getTablature().getCaret().getSelectedBeat();
		if( beat != null){
			//comienza el undoable
			UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();
			TuxGuitar.instance().getFileHistory().setUnsavedFile();

			//getSongManager().getMeasureManager().removeAllComponentsAt(caret.getMeasure(),caret.getSelectedComponent().getStart());
			getSongManager().getMeasureManager().cleanBeat(beat);

			//termia el undoable
			addUndoableEdit(undoable.endUndo());
			updateTablature();
		}
		return 0;
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
