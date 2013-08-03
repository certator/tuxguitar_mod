/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.note;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGVoice;

/**
 * @author julian
 */
public class DeleteNoteOrRestAction extends Action{

	public static final String NAME = "action.beat.general.delete-note-or-rest";

	public DeleteNoteOrRestAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		//comienza el undoable
		UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();
		TuxGuitar.instance().getFileHistory().setUnsavedFile();

		Caret caret = getEditor().getTablature().getCaret();
		TGBeat beat = caret.getSelectedBeat();
		TGVoice voice = beat.getVoice( caret.getVoice() );
		if( beat.isTextBeat() && beat.isRestBeat() ){
			getSongManager().getMeasureManager().removeText(beat);
		}else if(voice.isRestVoice()){
			getSongManager().getMeasureManager().removeVoice(voice ,true);
		}else{
			int string = caret.getSelectedString().getNumber();
			getSongManager().getMeasureManager().removeNote(caret.getMeasure(),beat.getStart(), caret.getVoice(), string);
		}

		//termia el undoable
		addUndoableEdit(undoable.endUndo());
		updateTablature();

		return 0;
	}

	@Override
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
}
