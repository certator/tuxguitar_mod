/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.effects;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;

/**
 * @author julian
 */
public class ChangeFadeInAction extends Action{

	public static final String NAME = "action.note.effect.change-fade-in";

	public ChangeFadeInAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		//comienza el undoable
		UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

		Caret caret = getEditor().getTablature().getCaret();
		getSongManager().getMeasureManager().changeFadeIn(caret.getMeasure(),caret.getPosition(),caret.getSelectedString().getNumber());
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
