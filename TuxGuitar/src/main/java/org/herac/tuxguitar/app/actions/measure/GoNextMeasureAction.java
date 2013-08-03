/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.measure;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableAddMeasure;
import org.herac.tuxguitar.graphics.control.TGTrackImpl;
import org.herac.tuxguitar.song.models.TGMeasure;

/**
 * @author julian
 */
public class GoNextMeasureAction extends Action{

	public static final String NAME = "action.measure.go-next";

	public GoNextMeasureAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		Caret caret = getEditor().getTablature().getCaret();
		//si es el ultimo compas, agrego uno nuevo
		if(getSongManager().getTrackManager().isLastMeasure(caret.getMeasure())){
			int number = (getSongManager().getSong().countMeasureHeaders() + 1);

			//comienza el undoable
			UndoableAddMeasure undoable = UndoableAddMeasure.startUndo(number);

			this.getSongManager().addNewMeasure(number);
			this.fireUpdate(number);
			this.moveToNext();

			//termia el undoable
			this.addUndoableEdit(undoable.endUndo());
		}
		else{
			this.moveToNext();
		}

		return 0;
	}

	private void moveToNext(){
		if(TuxGuitar.instance().getPlayer().isRunning()){
			TuxGuitar.instance().getTransport().gotoNext();
		}
		else{
			Caret caret = getEditor().getTablature().getCaret();
			TGTrackImpl track = caret.getTrack();
			TGMeasure measure = getSongManager().getTrackManager().getNextMeasure(caret.getMeasure());
			if(track != null && measure != null){
				caret.update(track.getNumber(),measure.getStart(),caret.getSelectedString().getNumber());
			}
		}
	}
}
