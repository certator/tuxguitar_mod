package org.herac.tuxguitar.song.factory;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGColor;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGLyric;
import org.herac.tuxguitar.song.models.TGMarker;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGScale;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGStroke;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGText;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;
import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloBar;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloPicking;
import org.herac.tuxguitar.song.models.effects.TGEffectTrill;

public class TGFactory {
	
	public TGSong newSong(){
		return new TGSong();
	}
	
	public TGLyric newLyric(){
		return new TGLyric();
	}
	
	public TGMarker newMarker(){
		return new TGMarker(this);
	}
	
	public TGChord newChord(int length){
		return new TGChord(length);
	}
	
	public TGScale newScale(){
		return new TGScale();
	}
	
	public TGColor newColor(){
		return new TGColor();
	}
	
	public TGDuration newDuration(){
		return new TGDuration(this);
	}
	
	public TGDivisionType newDivisionType(){
		return new TGDivisionType();
	}
	
	public TGTimeSignature newTimeSignature(){
		return new TGTimeSignature(this);
	}
	
	public TGTempo newTempo(){
		return new TGTempo();
	}
	
	public TGChannel newChannel(){
		return new TGChannel();
	}
	
	public TGTrack newTrack(){
		return new TGTrack(this);
	}
	
	public TGMeasureHeader newHeader(){
		return new TGMeasureHeader(this);
	}
	
	public TGMeasure newMeasure(TGMeasureHeader header){
		return new TGMeasure(header);
	}
	
	public TGBeat newBeat(){
		return new TGBeat(this);
	}
	
	public TGVoice newVoice(int index){
		return new TGVoice(this, index);
	}
	
	public TGNote newNote(){
		return new TGNote(this);
	}
	
	public TGString newString(){
		return new TGString();
	}
	
	public TGStroke newStroke(){
		return new TGStroke();
	}
	
	public TGText newText(){
		return new TGText();
	}
	
	public TGNoteEffect newEffect(){
		return new TGNoteEffect();
	}
	
	public TGEffectBend newEffectBend(){
		return new TGEffectBend();
	}
	
	public TGEffectTremoloBar newEffectTremoloBar(){
		return new TGEffectTremoloBar();
	}
	
	public TGEffectGrace newEffectGrace(){
		return new TGEffectGrace();
	}
	
	public TGEffectHarmonic newEffectHarmonic(){
		return new TGEffectHarmonic();
	}
	
	public TGEffectTrill newEffectTrill(){
		return new TGEffectTrill(this);
	}
	
	public TGEffectTremoloPicking newEffectTremoloPicking(){
		return new TGEffectTremoloPicking(this);
	}
}
