package org.herac.tuxguitar.app.undo.undoables.measure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.undo.CannotRedoException;
import org.herac.tuxguitar.app.undo.CannotUndoException;
import org.herac.tuxguitar.app.undo.UndoableEdit;
import org.herac.tuxguitar.app.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.helpers.TGSongSegment;
import org.herac.tuxguitar.song.helpers.TGSongSegmentHelper;
import org.herac.tuxguitar.song.models.TGMarker;

public class UndoableRemoveMeasure implements UndoableEdit{
	private int doAction;
	private final UndoableCaretHelper undoCaret;
	private UndoableCaretHelper redoCaret;
	private final TGSongSegment tracksMeasures;
	private final UndoMarkers undoMarkers;
	private final int n1;
	private final int n2;
	
	public UndoableRemoveMeasure(int n1,int n2){
		this.doAction = UNDO_ACTION;
		this.undoCaret = new UndoableCaretHelper();
		this.n1 = n1;
		this.n2 = n2;
		this.tracksMeasures = new TGSongSegmentHelper(TuxGuitar.instance().getSongManager()).copyMeasures(n1,n2);
		this.undoMarkers = new UndoMarkers();
	}
	
	@Override
	public void redo() throws CannotRedoException {
		if(!canRedo()){
			throw new CannotRedoException();
		}
		TuxGuitar.instance().getSongManager().removeMeasureHeaders(this.n1,this.n2);
		TuxGuitar.instance().fireUpdate();
		
		this.redoCaret.update();
		this.doAction = UNDO_ACTION;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		if(!canUndo()){
			throw new CannotUndoException();
		}
		new TGSongSegmentHelper(TuxGuitar.instance().getSongManager()).insertMeasures(this.tracksMeasures.clone(TuxGuitar.instance().getSongManager().getFactory()),this.n1,0,0);
		
		TuxGuitar.instance().fireUpdate();
		this.undoMarkers.undo();
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
	
	public UndoableRemoveMeasure endUndo(){
		this.redoCaret = new UndoableCaretHelper();
		return this;
	}
	
	private class UndoMarkers{
		private final List<TGMarker> markers;
		
		public UndoMarkers(){
			this.markers = new ArrayList<TGMarker>();
			Iterator<TGMarker> it = TuxGuitar.instance().getSongManager().getMarkers().iterator();
			while(it.hasNext()){
				this.markers.add(it.next().clone(TuxGuitar.instance().getSongManager().getFactory()));
			}
		}
		
		public void undo(){
			TuxGuitar.instance().getSongManager().removeAllMarkers();
			Iterator<TGMarker> it = this.markers.iterator();
			while(it.hasNext()){
				TGMarker marker = it.next();
				TuxGuitar.instance().getSongManager().updateMarker(marker.clone(TuxGuitar.instance().getSongManager().getFactory()));
			}
		}
	}
}
