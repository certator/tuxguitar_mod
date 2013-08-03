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
import org.herac.tuxguitar.graphics.control.TGBeatGroup;
import org.herac.tuxguitar.graphics.control.TGBeatImpl;
import org.herac.tuxguitar.graphics.control.TGVoiceImpl;
import org.herac.tuxguitar.song.models.TGVoice;

/**
 * @author julian
 */
public class SetVoiceUpAction extends Action{

	public static final String NAME = "action.beat.general.voice-up";

	public SetVoiceUpAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		Caret caret = getEditor().getTablature().getCaret();
		TGBeatImpl beat = caret.getSelectedBeat();
		if( beat != null ){
			TGVoiceImpl voice = beat.getVoiceImpl( caret.getVoice() );
			TGBeatGroup group = voice.getBeatGroup();
			if(!voice.isEmpty() && !voice.isRestVoice() && group != null ){
				//comienza el undoable
				UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

				Iterator<TGVoiceImpl> it = group.getVoices().iterator();
				while( it.hasNext() ){
					TGVoice current = it.next();
					getSongManager().getMeasureManager().changeVoiceDirection(current, TGVoice.DIRECTION_UP);
				}

				//termia el undoable
				addUndoableEdit(undoable.endUndo());
				TuxGuitar.instance().getFileHistory().setUnsavedFile();

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
