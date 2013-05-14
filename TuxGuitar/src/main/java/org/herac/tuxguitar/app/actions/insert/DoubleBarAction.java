/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.insert;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.undo.undoables.custom.UndoableChangeDoubleBar;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;

public class DoubleBarAction extends Action{

	public static final String NAME = "action.insert.doublebar";

	public DoubleBarAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		UndoableChangeDoubleBar undoable = UndoableChangeDoubleBar.startUndo();
		TuxGuitar.instance().getFileHistory().setUnsavedFile();

		TGMeasureImpl measure = getEditor().getTablature().getCaret().getMeasure();
		measure.getHeader().setDoubleBar(!measure.getHeader().hasDoubleBar());
		updateTablature();

		addUndoableEdit(undoable.endUndo());

		return 0;
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
