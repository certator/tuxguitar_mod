package org.herac.tuxguitar.player.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGSong;

/**
 * Provide a sequence of measure headers according to the music notation.
 * @author bayo
 */
public class MeasureSequencer implements Iterator<TGMeasureHeader> {

	private final TGSong song;

	private final Integer startMeasure;
	private final Integer endMeasure;
	private TGMeasureHeader next;

	private int index;

	private int repeatStart;
	private int repeatEnd;
	private int repeatCount;
	private int repeatNumber;

	private Map<Integer, Integer> repeatAlternates;


	public MeasureSequencer(TGSong song) {
		this(song, null, null);
	}

	public MeasureSequencer(TGSong song, Integer startMeasure, Integer endMeasure) {
		this.song = song;
		this.startMeasure = startMeasure;
		this.endMeasure = endMeasure;
		this.index = (this.startMeasure != null) ? this.startMeasure : 0;
		if (this.index < 0) {
			throw new IllegalArgumentException("Start measure header index must be positive");
		}
		this.next = first();
	}

	/**
	 * Return the very first element, else null if no element exists
	 * @return A measure header, else null if not found.
	 */
	private TGMeasureHeader first() {
		if (this.song == null) {
			return null;
		}
		if (this.index < 0) {
			return null;
		}
		if (this.index >= this.song.countMeasureHeaders()) {
			return null;
		}
		return this.song.getMeasureHeader(this.index);
	}

	/**
	 * Extract first index of each alternative repeat between 2 measure header index
	 * @param begin
	 * @param end
	 * @return A map from alternative number to index
	 */
	private Map<Integer, Integer> extractRepeatAlternate(int begin, int end) {
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();

		for (int i = begin; i < end; i++) {
			TGMeasureHeader header = this.song.getMeasureHeader(i);
			int alternateFlag = header.getRepeatAlternative();
			if (i == begin && alternateFlag == 0) {
				result.put(0, begin);
			}
			int alternative = 0;
			while (alternateFlag != 0) {
				alternative++;
				if ((alternateFlag & 1) != 0) {
					if (!result.containsKey(alternative)) {
						result.put(alternative, i);
					}
				}
				alternateFlag >>= 1;
			}
		}

		return result;
	}

	private int findRepeatEnd(int index) {
		int current = index;
		while (current < this.song.countMeasureHeaders()) {
			TGMeasureHeader header = this.song.getMeasureHeader(current);
			if (header.getRepeatClose() > 0) {
				break;
			}
			current++;
		}
		return current;
	}

	/**
	 * Find last index measure header containing alternate loop after the end of the loop
	 * @param first index which can contain alternate
	 * @param count number of repeat
	 * @return index after last alternate (if result = index there is no alternates)
	 */
	private int findAlternateEnd(int index, int count) {
		int current = index;
		while (current < this.song.countMeasureHeaders()) {
			TGMeasureHeader header = this.song.getMeasureHeader(current);
			if (header.isRepeatOpen()) {
				break;
			}
			if (header.getRepeatAlternative() == 0) {
				break;
			}
			current++;
		}
		return current;
	}

	private TGMeasureHeader internalNext() {

		// initialize repeat
		TGMeasureHeader header = this.song.getMeasureHeader(this.index);
		if (header.isRepeatOpen() && this.repeatAlternates == null) {
			this.repeatStart = this.index;
			this.repeatNumber = 0;
			this.repeatEnd = findRepeatEnd(this.index);
			if (this.repeatEnd < this.song.countMeasureHeaders()) {
				TGMeasureHeader end = this.song.getMeasureHeader(this.repeatEnd);
				this.repeatCount = end.getRepeatClose();
				this.repeatEnd++;
				this.repeatEnd = findAlternateEnd(this.repeatEnd, this.repeatCount);
				this.repeatAlternates = extractRepeatAlternate(this.repeatStart, this.repeatEnd);
			} else {
				// ignore loop
				this.repeatAlternates = null;
			}
		}

		if (this.repeatAlternates != null) {
			// it is a loop
			Integer index = null;
			if (this.index == this.repeatEnd - 1) {
				// end of this alternate
				this.repeatNumber++;
				index = this.repeatAlternates.get(0);
				if (index == null) {
					index = this.repeatAlternates.get(this.repeatNumber+1);
				}
			} else {
				int currentAlternate = header.getRepeatAlternative();
				TGMeasureHeader next = this.song.getMeasureHeader(this.index + 1);
				if (currentAlternate == next.getRepeatAlternative()) {
					index = this.index + 1;
				} else if (currentAlternate == 0) {
					// begin of the alternate
					index = this.repeatAlternates.get(this.repeatNumber+1);
				} else {
					// end of this alternate
					this.repeatNumber++;
					index = this.repeatAlternates.get(0);
					if (index == null) {
						index = this.repeatAlternates.get(this.repeatNumber+1);
					}
				}
			}

			if (index == null || this.repeatNumber > this.repeatCount) {
				// reset the loop
				this.index = this.repeatEnd;
				this.repeatAlternates = null;
			} else {
				this.index = index;
			}
		} else {
			// it is not a loop
			this.index++;
		}

		if (this.endMeasure != null && this.index >= this.endMeasure) {
			return null;
		}

		if (this.index >= this.song.countMeasureHeaders()) {
			return null;
		} else {
			return this.song.getMeasureHeader(this.index);
		}
	}

	@Override
	public boolean hasNext() {
		return this.next != null;
	}

	@Override
	public TGMeasureHeader next() {
		TGMeasureHeader tmp = this.next;
		this.next = internalNext();
		return tmp;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public TGSong getTGSong() {
		return this.song;
	}

}
