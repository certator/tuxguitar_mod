package org.herac.tuxguitar.app.marker;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.graphics.control.TGTrackImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMarker;

public class MarkerNavigator {

	public MarkerNavigator(){
		super();
	}

	public void goToSelectedMarker(TGMarker marker){
		if(marker != null){
			Caret caret = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret();
			TGSongManager manager = TuxGuitar.instance().getSongManager();
			TGTrackImpl track = caret.getTrack();
			if(track != null){
				TGMeasureImpl measure = (TGMeasureImpl)manager.getTrackManager().getMeasure(track,marker.getMeasure());
				if(measure != null){
					TGBeat beat = manager.getMeasureManager().getFirstBeat(measure.getBeats());
					if(beat != null){
						caret.moveTo(track, measure,beat,caret.getStringNumber());
					}
				}
			}
		}
	}
}
