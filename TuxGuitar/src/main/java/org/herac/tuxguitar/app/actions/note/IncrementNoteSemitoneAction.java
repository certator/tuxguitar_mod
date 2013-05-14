/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.note;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGNote;

/**
 * @author julian
 */
public class IncrementNoteSemitoneAction extends Action{

	public static final String NAME = "action.note.general.increment-semitone";

	public IncrementNoteSemitoneAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TGNote note = getEditor().getTablature().getCaret().getSelectedNote();
		if(note != null){
			//comienza el undoable
			UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

			if(getSongManager().getMeasureManager().moveSemitoneUp(getEditor().getTablature().getCaret().getMeasure(),note.getVoice().getBeat().getStart(),note.getString())){
				//termia el undoable
				addUndoableEdit(undoable.endUndo());
				TuxGuitar.instance().getFileHistory().setUnsavedFile();
			}
			updateTablature();
		}
		return 0;
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
