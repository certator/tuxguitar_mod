/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.note;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.effects.StrokeEditor;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGStroke;

/**
 * @author julian
 */
public class SetStrokeUpAction extends Action{

	public static final String NAME = "action.beat.general.set-stroke-up";

	public SetStrokeUpAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TGBeat beat = getEditor().getTablature().getCaret().getSelectedBeat();
		if(beat != null && !beat.isRestBeat()){
			StrokeEditor editor = new StrokeEditor();
			editor.open(beat);
			if( editor.getStatus() != StrokeEditor.STATUS_CANCEL ){
				int direction = ( editor.getStatus() == StrokeEditor.STATUS_CLEAN ? TGStroke.STROKE_NONE : TGStroke.STROKE_UP );
				int value = editor.getValue();

				//comienza el undoable
				UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();
				if(getSongManager().getMeasureManager().setStroke( beat.getMeasure(), beat.getStart(), value, direction ) ){
					//termia el undoable
					addUndoableEdit(undoable.endUndo());
					TuxGuitar.instance().getFileHistory().setUnsavedFile();
				}
				updateTablature();
			}
		}
		return 0;
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
