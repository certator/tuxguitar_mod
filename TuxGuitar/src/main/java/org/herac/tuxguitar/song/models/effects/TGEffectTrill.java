package org.herac.tuxguitar.song.models.effects;

import java.io.Serializable;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGDuration;

public class TGEffectTrill implements Serializable {
	private static final long serialVersionUID = 1L;

	private int fret;
	private TGDuration duration;

	public TGEffectTrill(TGFactory factory) {
		this.fret = 0;
		this.duration = factory.newDuration();
	}

	public int getFret() {
		return this.fret;
	}

	public void setFret(int fret) {
		this.fret = fret;
	}

	public TGDuration getDuration() {
		return this.duration;
	}

	public void setDuration(TGDuration duration) {
		this.duration = duration;
	}

	public TGEffectTrill clone(TGFactory factory){
		TGEffectTrill effect = factory.newEffectTrill();
		effect.setFret(getFret());
		effect.getDuration().setValue(getDuration().getValue());
		effect.getDuration().setDotted(getDuration().isDotted());
		effect.getDuration().setDoubleDotted(getDuration().isDoubleDotted());
		effect.getDuration().getDivision().setEnters(getDuration().getDivision().getEnters());
		effect.getDuration().getDivision().setTimes(getDuration().getDivision().getTimes());

		return effect;
	}

}
