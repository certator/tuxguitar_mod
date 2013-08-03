/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.note;

import java.util.Iterator;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGVoice;

/**
 * @author julian
 */
public class ChangeTiedNoteAction extends Action{

	public static final String NAME = "action.note.general.tied";

	public ChangeTiedNoteAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		Caret caret = getEditor().getTablature().getCaret();
		if(caret.getSelectedNote() != null){
			//comienza el undoable
			UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

			getSongManager().getMeasureManager().changeTieNote(caret.getSelectedNote());

			//termia el undoable
			addUndoableEdit(undoable.endUndo());
		}else{
			TGNote note = getSongManager().getFactory().newNote();
			note.setValue(0);
			note.setVelocity(caret.getVelocity());
			note.setString(caret.getSelectedString().getNumber());
			note.setTiedNote(true);

			TGDuration duration = getSongManager().getFactory().newDuration();
			caret.getDuration().copy(duration);

			setTiedNoteValue(note,caret);

			//comienza el undoable
			UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

			getSongManager().getMeasureManager().addNote(caret.getSelectedBeat(),note,duration, caret.getVoice());

			//termia el undoable
			addUndoableEdit(undoable.endUndo());
		}
		TuxGuitar.instance().getFileHistory().setUnsavedFile();
		updateTablature();
		return 0;
	}

	private void setTiedNoteValue(TGNote note,Caret caret){
		TGMeasure measure = caret.getMeasure();
		TGVoice voice = getSongManager().getMeasureManager().getPreviousVoice( measure.getBeats(), caret.getSelectedBeat(), caret.getVoice());
		while( measure != null){
			while( voice != null ){
				if(voice.isRestVoice()){
					note.setValue(0);
					return;
				}
				// Check if is there any note at same string.
				Iterator<TGNote> it = voice.getNotes().iterator();
				while( it.hasNext() ){
					TGNote current = it.next();
					if(current.getString() == note.getString()){
						note.setValue( current.getValue() );
						return;
					}
				}
				voice = getSongManager().getMeasureManager().getPreviousVoice( measure.getBeats(), voice.getBeat(), caret.getVoice());
			}
			measure = getSongManager().getTrackManager().getPrevMeasure(measure);
			if( measure != null ){
				voice = getSongManager().getMeasureManager().getLastVoice( measure.getBeats() , caret.getVoice());
			}
		}
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
