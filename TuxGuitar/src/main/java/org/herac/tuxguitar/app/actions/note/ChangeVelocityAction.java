/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.note;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;

/**
 * @author julian
 */
public class ChangeVelocityAction extends Action{

	public static final String NAME = "action.note.general.velocity";

	public static final String PROPERTY_VELOCITY = "velocity";

	public ChangeVelocityAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | DISABLE_ON_PLAYING);
	}

	@Override
	protected int execute(ActionData actionData){
		Object propertyVelocity = actionData.get(PROPERTY_VELOCITY);
		if( propertyVelocity instanceof Integer){
			int velocity = ((Integer)propertyVelocity).intValue();

			//comienza el undoable
			UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();
			TuxGuitar.instance().getFileHistory().setUnsavedFile();

			Caret caret = getEditor().getTablature().getCaret();
			caret.setVelocity(velocity);
			getSongManager().getMeasureManager().changeVelocity(velocity,caret.getMeasure(),caret.getPosition(),caret.getSelectedString().getNumber());
			updateTablature();

			//termia el undoable
			addUndoableEdit(undoable.endUndo());

			return AUTO_UPDATE;
		}
		return 0;
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
