package org.herac.tuxguitar.app.undo.undoables.track;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.undo.CannotRedoException;
import org.herac.tuxguitar.app.undo.CannotUndoException;
import org.herac.tuxguitar.app.undo.UndoableEdit;
import org.herac.tuxguitar.app.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGTrack;

public class UndoableTrackSoloMute implements UndoableEdit{
	private int doAction;
	private int track;
	private UndoableCaretHelper undoCaret;
	private UndoableCaretHelper redoCaret;
	private boolean undoSolo;
	private boolean undoMute;
	private boolean redoSolo;
	private boolean redoMute;

	private UndoableTrackSoloMute(){
		super();
	}

	@Override
	public void redo() throws CannotRedoException {
		if(!canRedo()){
			throw new CannotRedoException();
		}
		TGSongManager manager = TuxGuitar.instance().getSongManager();
		TGTrack track = manager.getTrack( this.track );
		if( track != null ){
			manager.getTrackManager().changeSolo(track, this.redoSolo );
			manager.getTrackManager().changeMute(track, this.redoMute );
		}
		TuxGuitar.instance().getTable().fireUpdate(false);
		TuxGuitar.instance().updateCache(true);
		if (TuxGuitar.instance().getPlayer().isRunning()) {
			TuxGuitar.instance().getPlayer().updateControllers();
		}

		this.redoCaret.update();
		this.doAction = UNDO_ACTION;
	}

	@Override
	public void undo() throws CannotUndoException {
		if(!canUndo()){
			throw new CannotUndoException();
		}
		TGSongManager manager = TuxGuitar.instance().getSongManager();
		TGTrack track = manager.getTrack( this.track );
		if( track != null ){
			manager.getTrackManager().changeSolo(track, this.undoSolo );
			manager.getTrackManager().changeMute(track, this.undoMute );
		}
		TuxGuitar.instance().getTable().fireUpdate(false);
		TuxGuitar.instance().updateCache(true);
		if (TuxGuitar.instance().getPlayer().isRunning()) {
			TuxGuitar.instance().getPlayer().updateControllers();
		}

		this.undoCaret.update();
		this.doAction = REDO_ACTION;
	}

	@Override
	public boolean canRedo() {
		return (this.doAction == REDO_ACTION);
	}

	@Override
	public boolean canUndo() {
		return (this.doAction == UNDO_ACTION);
	}

	public static UndoableTrackSoloMute startUndo(TGTrack track){
		UndoableTrackSoloMute undoable = new UndoableTrackSoloMute();
		undoable.doAction = UNDO_ACTION;
		undoable.undoCaret = new UndoableCaretHelper();
		undoable.track = track.getNumber();
		undoable.undoSolo = track.isSolo();
		undoable.undoMute = track.isMute();

		return undoable;
	}

	public UndoableTrackSoloMute endUndo(TGTrack track){
		this.redoCaret = new UndoableCaretHelper();
		this.redoSolo = track.isSolo();
		this.redoMute = track.isMute();

		return this;
	}

}
