/*
 * Created on 26-dic-2005
 */
package org.herac.tuxguitar.song.models.effects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.song.factory.TGFactory;

/**
 * @author julian
 */
public class TGEffectTremoloBar implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAX_POSITION_LENGTH = 12;
	public static final int MAX_VALUE_LENGTH = 12;

	private final List<TremoloBarPoint> points;

	public TGEffectTremoloBar(){
		this.points = new ArrayList<TremoloBarPoint>();
	}

	public void addPoint(int position,int value){
		this.points.add(new TremoloBarPoint(position,value));
	}

	public List<TremoloBarPoint> getPoints(){
		return this.points;
	}

	public TGEffectTremoloBar clone(TGFactory factory){
		TGEffectTremoloBar effect = factory.newEffectTremoloBar();
		Iterator<TremoloBarPoint> it = getPoints().iterator();
		while(it.hasNext()){
			TremoloBarPoint point = it.next();
			effect.addPoint(point.getPosition(),point.getValue());
		}

		return effect;
	}

	public class TremoloBarPoint{
		private final int position;
		private final int value;

		public TremoloBarPoint(int position,int value){
			this.position = position;
			this.value = value;
		}

		public int getPosition() {
			return this.position;
		}

		public int getValue() {
			return this.value;
		}

		public long getTime(long duration){
			return (duration * getPosition() / MAX_POSITION_LENGTH);
		}

		@Override
		public Object clone(){
			return new TremoloBarPoint(getPosition(),getValue());
		}
	}

}
