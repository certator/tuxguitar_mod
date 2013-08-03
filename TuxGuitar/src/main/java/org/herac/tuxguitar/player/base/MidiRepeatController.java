package org.herac.tuxguitar.player.base;

import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGSong;

public class MidiRepeatController {

	private final TGSong song;
	private TGMeasureHeader header;
	private TGMeasureHeader previous;
	private final MeasureSequencer sequencer;
	private int repeatMove;

	public MidiRepeatController(TGSong song, int startHeader , int endHeader){
		this.song = song;
		this.sequencer = new MeasureSequencer(song, startHeader==-1?null:startHeader, endHeader==-1?null:endHeader);
		if (this.sequencer.hasNext()) {
			this.header = this.sequencer.next();
		}
		this.repeatMove = 0;
	}

	/**
	 * Find next index measure
	 */
	public void process() {
		if (this.sequencer.hasNext()) {
			this.previous = this.header;
			this.header = this.sequencer.next();
			if (this.previous != null && this.header != null && this.previous.getNumber() != this.header.getNumber() - 1) {
				this.repeatMove -= this.header.getStart() - this.header.getLength() - this.previous.getStart();
			}
		} else {
			this.header = null;
		}
	}

	public boolean finished(){
		return this.header == null;
	}

	public boolean shouldPlay(){
		return true;
	}

	public int getIndex(){
		return this.header.getNumber() - 1;
	}

	public long getRepeatMove(){
		return this.repeatMove;
	}

	public TGSong getTGSong() {
		return this.song;
	}

}
