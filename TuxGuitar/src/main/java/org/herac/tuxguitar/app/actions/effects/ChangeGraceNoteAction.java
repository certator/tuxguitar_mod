/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.effects;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.effects.GraceEditor;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;

/**
 * @author julian
 */
public class ChangeGraceNoteAction extends Action{

	public static final String NAME = "action.note.effect.change-grace";

	public ChangeGraceNoteAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TGNote note = getEditor().getTablature().getCaret().getSelectedNote();
		if(note != null){
			GraceEditor graceEditor = new GraceEditor();
			graceEditor.show(note);
			if(!graceEditor.isCancelled()){
				changeGrace(graceEditor.getResult());
			}
		}
		return 0;
	}

	private void changeGrace(TGEffectGrace effect){
		//comienza el undoable
		UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

		Caret caret = getEditor().getTablature().getCaret();
		getSongManager().getMeasureManager().changeGraceNote(caret.getMeasure(),caret.getPosition(),caret.getSelectedString().getNumber(),effect);
		TuxGuitar.instance().getFileHistory().setUnsavedFile();
		updateTablature();

		//termia el undoable
		addUndoableEdit(undoable.endUndo());
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
