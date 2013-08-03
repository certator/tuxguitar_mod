/*
 * Created on 29-nov-2005
 */
package org.herac.tuxguitar.song.models;

import java.io.Serializable;

import org.herac.tuxguitar.song.factory.TGFactory;

/**
 * @author julian
 */
public class TGTimeSignature implements Serializable {
	private static final long serialVersionUID = 1L;

	private TGDuration denominator;
	private int numerator;

	public TGTimeSignature(TGFactory factory){
		this.numerator = 4;
		this.denominator = factory.newDuration();
	}

	public int getNumerator() {
		return this.numerator;
	}

	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}

	public TGDuration getDenominator() {
		return this.denominator;
	}

	public void setDenominator(TGDuration denominator) {
		this.denominator = denominator;
	}

	public TGTimeSignature clone(TGFactory factory){
		TGTimeSignature timeSignature = factory.newTimeSignature();
		copy(timeSignature);
		return timeSignature;
	}

	public void copy(TGTimeSignature timeSignature){
		timeSignature.setNumerator(getNumerator());
		getDenominator().copy(timeSignature.getDenominator());
	}

	public boolean isEqual(TGTimeSignature ts){
		return (getNumerator() == ts.getNumerator() && getDenominator().isEqual(ts.getDenominator()));
	}
}
