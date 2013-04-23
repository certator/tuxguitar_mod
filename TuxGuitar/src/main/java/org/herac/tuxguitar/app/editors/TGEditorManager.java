package org.herac.tuxguitar.app.editors;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.models.TGBeat;

public class TGEditorManager {
	
	private List<TGRedrawListener> redrawListeners;
	private List<TGUpdateListener> updateListeners;
	private List<TGExternalBeatViewerListener> beatViewerListeners;
	
	public TGEditorManager(){
		this.redrawListeners = new ArrayList<TGRedrawListener>();
		this.updateListeners = new ArrayList<TGUpdateListener>();
		this.beatViewerListeners = new ArrayList<TGExternalBeatViewerListener>();
	}
	
	public void doRedraw( int type ){
		for(int i = 0; i < this.redrawListeners.size(); i ++){
			TGRedrawListener listener = this.redrawListeners.get( i );
			listener.doRedraw( type );
		}
	}
	
	public void doUpdate( int type ){
		for(int i = 0; i < this.updateListeners.size(); i ++){
			TGUpdateListener listener = this.updateListeners.get( i );
			listener.doUpdate( type );
		}
	}
	
	public void showExternalBeat( TGBeat beat ){
		for(int i = 0; i < this.beatViewerListeners.size(); i ++){
			TGExternalBeatViewerListener listener = this.beatViewerListeners.get( i );
			listener.showExternalBeat(beat);
		}
	}
	
	public void hideExternalBeat(){
		for(int i = 0; i < this.beatViewerListeners.size(); i ++){
			TGExternalBeatViewerListener listener = this.beatViewerListeners.get( i );
			listener.hideExternalBeat();
		}
	}
	
	public void addRedrawListener( TGRedrawListener listener){
		if(!this.redrawListeners.contains( listener )){
			this.redrawListeners.add( listener );
		}
	}
	
	public void removeRedrawListener( TGRedrawListener listener){
		if(this.redrawListeners.contains( listener )){
			this.redrawListeners.remove( listener );
		}
	}
	
	public void addUpdateListener( TGUpdateListener listener){
		if(!this.updateListeners.contains( listener )){
			this.updateListeners.add( listener );
		}
	}
	
	public void removeUpdateListener( TGUpdateListener listener){
		if(this.updateListeners.contains( listener )){
			this.updateListeners.remove( listener );
		}
	}
	
	public void addBeatViewerListener( TGExternalBeatViewerListener listener){
		if(!this.beatViewerListeners.contains( listener )){
			this.beatViewerListeners.add( listener );
		}
	}
	
	public void removeBeatViewerListener( TGExternalBeatViewerListener listener){
		if(this.beatViewerListeners.contains( listener )){
			this.beatViewerListeners.remove( listener );
		}
	}
}