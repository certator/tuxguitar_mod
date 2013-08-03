/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.effects;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGNote;

/**
 * @author julian
 */
public class ChangeDeadNoteAction extends Action{

	public static final String NAME = "action.note.effect.change-dead";

	public ChangeDeadNoteAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		//comienza el undoable
		UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

		Caret caret = getEditor().getTablature().getCaret();
		TGNote note = caret.getSelectedNote();
		if(note == null){
			note = getSongManager().getFactory().newNote();
			note.setValue(0);
			note.setVelocity(caret.getVelocity());
			note.setString(caret.getSelectedString().getNumber());

			TGDuration duration = getSongManager().getFactory().newDuration();
			caret.getDuration().copy(duration);

			getSongManager().getMeasureManager().addNote(caret.getMeasure(),caret.getPosition(),note,duration,caret.getVoice());
		}
		getSongManager().getMeasureManager().changeDeadNote(note);
		TuxGuitar.instance().getFileHistory().setUnsavedFile();
		updateTablature();

		//termia el undoable
		addUndoableEdit(undoable.endUndo());

		return 0;
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
