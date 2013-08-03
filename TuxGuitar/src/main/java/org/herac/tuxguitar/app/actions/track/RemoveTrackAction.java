/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.track;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.track.UndoableRemoveTrack;
import org.herac.tuxguitar.graphics.control.TGTrackImpl;

/**
 * @author julian
 */
public class RemoveTrackAction extends Action{

	public static final String NAME = "action.track.remove";

	public RemoveTrackAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		Caret caret = getEditor().getTablature().getCaret();

		if(getSongManager().getSong().countTracks() <= 1){
			//TuxGuitar.instance().getAction(NewFileAction.NAME).process(e);
			TuxGuitar.instance().newSong();
			return 0;
		}
		//comienza el undoable
		UndoableRemoveTrack undoable = UndoableRemoveTrack.startUndo();
		TuxGuitar.instance().getFileHistory().setUnsavedFile();

		TGTrackImpl track = caret.getTrack();
		TGTrackImpl nextCaretTrack = (TGTrackImpl)getSongManager().getTrack(track.getNumber() + 1);
		if(nextCaretTrack == null){
			nextCaretTrack =  (TGTrackImpl)getSongManager().getTrack(track.getNumber() - 1);
		}
		getSongManager().removeTrack(track);
		updateTablature();

		//move the caret to the next or previous track
		if(nextCaretTrack != null){
			caret.update(nextCaretTrack.getNumber(),getSongManager().getTrackManager().getMeasureAt(nextCaretTrack, caret.getMeasure().getStart()).getStart(),1);
		}

		//termia el undoable
		addUndoableEdit(undoable.endUndo());

		return 0;
	}
}
