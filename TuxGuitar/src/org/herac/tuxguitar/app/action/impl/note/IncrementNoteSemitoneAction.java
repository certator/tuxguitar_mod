/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.action.impl.note;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.action.TGActionBase;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGNote;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IncrementNoteSemitoneAction extends TGActionBase{
	
	public static final String NAME = "action.note.general.increment-semitone";
	
	public IncrementNoteSemitoneAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}
	
	protected void processAction(TGActionContext context){
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
	}
	
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
