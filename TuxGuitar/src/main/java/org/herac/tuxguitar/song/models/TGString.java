/*
 * Created on 30-nov-2005
 */
package org.herac.tuxguitar.song.models;

import java.io.Serializable;

import org.herac.tuxguitar.song.factory.TGFactory;

/**
 * @author julian
 */
public class TGString implements Serializable {
	private static final long serialVersionUID = 1L;

	private int number;
	private int value;

	public TGString(){
		this.number = 0;
		this.value = 0;
	}

	public int getNumber() {
		return this.number;
	}

	public int getValue() {
		return this.value;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isEqual(TGString string){
		return (this.getNumber() == string.getNumber() && this.getValue() == string.getValue());
	}

	public TGString clone(TGFactory factory){
		TGString string = factory.newString();
		copy(string);
		return string;
	}

	public void copy(TGString string){
		string.setNumber(getNumber());
		string.setValue(getValue());
	}

}
