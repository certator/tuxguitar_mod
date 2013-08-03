/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.duration;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGDuration;

/**
 * @author julian
 */
public class ChangeDivisionTypeAction extends Action{

	public static final String NAME = "action.note.duration.change-division-type";

	public static final String PROPERTY_DIVISION_TYPE = "divisionType";

	public ChangeDivisionTypeAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		Object propertyDivisionType = actionData.get(PROPERTY_DIVISION_TYPE);

		//comienza el undoable
		UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

		TGDivisionType divisionType = defaultDivisionType();
		if( propertyDivisionType instanceof TGDivisionType){
			divisionType = (TGDivisionType)propertyDivisionType;
		}


		TGDivisionType newDivisionType = null;
		TGDivisionType oldDivisionType = getSelectedDuration().getDivision();
		if( oldDivisionType.isEqual(TGDivisionType.NORMAL)){
			newDivisionType = divisionType;
		}
		else{
			newDivisionType = noTuplet();
			if(!oldDivisionType.isEqual(divisionType) && propertyDivisionType instanceof TGDivisionType ){
				newDivisionType = divisionType;
			}
		}

		setDivisionType(newDivisionType);
		setDurations();

		//termia el undoable
		addUndoableEdit(undoable.endUndo());

		return 0;
	}

	private TGDivisionType noTuplet(){
		TGDivisionType divisionType = getSongManager().getFactory().newDivisionType();
		TGDivisionType.NORMAL.copy(divisionType);
		return divisionType;
	}

	private TGDivisionType defaultDivisionType(){
		TGDivisionType divisionType = getSongManager().getFactory().newDivisionType();
		TGDivisionType.TRIPLET.copy(divisionType);
		return divisionType;
	}

	private void setDivisionType(TGDivisionType divisionType){
		getSelectedDuration().getDivision().setEnters(divisionType.getEnters());
		getSelectedDuration().getDivision().setTimes(divisionType.getTimes());
	}

	private void setDurations() {
		Caret caret = getEditor().getTablature().getCaret();
		caret.changeDuration(getSelectedDuration().clone(getSongManager().getFactory()));
		TuxGuitar.instance().getFileHistory().setUnsavedFile();
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}

	public TGDuration getSelectedDuration(){
		return getEditor().getTablature().getCaret().getDuration();
	}
}
