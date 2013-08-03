/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.duration;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGDuration;

/**
 * @author julian
 */
public class DecrementDurationAction extends Action{

	public static final String NAME = "action.note.duration.decrement-duration";

	public DecrementDurationAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TGDuration duration = getEditor().getTablature().getCaret().getDuration();
		if(duration.getValue() > TGDuration.WHOLE){
			//comienza el undoable
			UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

			this.changeDuration(duration.getValue() / 2);

			TuxGuitar.instance().getFileHistory().setUnsavedFile();
			this.updateTablature();

			//termia el undoable
			addUndoableEdit(undoable.endUndo());
		}

		return 0;
	}

	private void changeDuration(int value) {
		Caret caret = getEditor().getTablature().getCaret();
		caret.getDuration().setValue(value);
		caret.getDuration().setDotted(false);
		caret.getDuration().setDoubleDotted(false);
		caret.changeDuration(caret.getDuration().clone(getSongManager().getFactory()));
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
