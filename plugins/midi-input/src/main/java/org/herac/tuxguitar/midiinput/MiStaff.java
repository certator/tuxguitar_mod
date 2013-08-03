package org.herac.tuxguitar.midiinput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.managers.TGTrackManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGTrack;


class MiStaff
{
	private TreeMap<Long, MiStaffEvent>	f_Events				= new TreeMap<Long, MiStaffEvent>();	// staff events map
	private TGTrack	f_TgTrack				= null;				// work track
	private final	boolean	f_Dump_Input			= false;			// for debugging...
	private final	boolean	f_Dump_TrackGeneration	= false;			// for debugging...


	MiStaff(
		final ArrayList<MiNote>	inBufferNotes,		// MIDI input notes from buffer [microseconds]
		int				inTempo,			// quarters per minute
		long			inStartTime,		// first MIDI time stamp [microseconds]
		long			inStopTime,			// last MIDI time stamp [microseconds]
		long			inStartPosition,	// recording start position [ticks]
		String			inTrackName)		// name for the TuxGuitar track to be created
	{
	ArrayList<MiNote>	midiNotes = new ArrayList<MiNote>();

	// make a deep copy of input buffer notes
	for(Iterator<MiNote> it = inBufferNotes.iterator(); it.hasNext();)
		midiNotes.add(new MiNote(it.next()));

	// convert time stamps from absolute microseconds to song relative ticks
	for(Iterator<MiNote> it = midiNotes.iterator(); it.hasNext();)
		{
		MiNote	note	= it.next();
		long	timeOn	= note.getTimeOn(),
				timeOff	= note.getTimeOff();

		// absolute to relative time stamps
		timeOn	-= inStartTime;
		timeOff	-= inStartTime;

		// time stamps to ticks
		timeOn	= inStartPosition + timestampToTicks(inTempo, timeOn);
		timeOff	= inStartPosition + timestampToTicks(inTempo, timeOff);

		// update values
		note.setTimeOn(timeOn);
		note.setTimeOff(timeOff);
		}

	if(f_Dump_Input)
		{
		MiBuffer.dump(inBufferNotes, "input buffer MIDI notes");
		MiBuffer.dump(midiNotes, "converted MIDI notes");
		}

	TGSongManager	tgSongMgr		= TuxGuitar.instance().getSongManager();
	long			startTick		= inStartPosition,
					stopTick		= inStartPosition + timestampToTicks(inTempo, inStopTime - inStartTime);
	TGMeasureHeader	mh				= tgSongMgr.getMeasureHeaderAt(startTick);
	long			firstBarTick	= mh.getStart();

	// insert bars into staff
	for(long tick = firstBarTick; tick <= stopTick; tick += 4 * TGDuration.QUARTER_TIME)
		addBar(tick);

	// insert note events into staff
	for(Iterator<MiNote> it = midiNotes.iterator(); it.hasNext();)
		addNote(it.next());

	// generate bars
	createMeasures();

	// generate beats
	insertNotesIntoTrack(inTrackName);
	}


	static	long	timestampToTicks(int inTempo, long inTimeStamp)
	{
	long	ticks = (inTimeStamp * inTempo * TGDuration.QUARTER_TIME) / 60000000L;

	return(ticks);
	}


	static	long	ticksToTimestamp(int inTempo, long inTicks)
	{
	long	timeStamp = (inTicks * 60000000L) / (inTempo * TGDuration.QUARTER_TIME);

	return(timeStamp);
	}


	void	addBar(long inTime)
	{
	MiStaffEvent	se = f_Events.get( new Long(inTime) );

	if(se == null)
		{
		se = new MiStaffEvent(inTime);
		f_Events.put( new Long(inTime) , se);
		}

	se.markAsBar();
	}


	void	addNote(MiNote inNote)
	{
	MiStaffEvent	se;

	se = f_Events.get( new Long(inNote.getTimeOn()) );

	if(se == null)
		{
		se = new MiStaffEvent(inNote.getTimeOn());
		f_Events.put( new Long(inNote.getTimeOn()) , se);
		}

	se.addNoteOn(inNote);
	}


	private void	mergeEvent(TreeMap<Long, MiStaffEvent> inEventsMap, MiStaffEvent inSE)
	{
	MiStaffEvent	se = inEventsMap.get( new Long(inSE.getBeginTime()) );

	if(se == null)
		inEventsMap.put( new Long(inSE.getBeginTime()) , inSE);
	else
		se.merge(inSE);
	}


	private void	addTiedNote(long inTime, MiStaffNote inSN, long inResidualDuration)
	{
	MiStaffEvent	se = f_Events.get( new Long(inTime) );

	if(se == null)
		{
		se = new MiStaffEvent(inTime);
		f_Events.put( new Long(inTime), se);
		}

	MiStaffNote sn = new MiStaffNote(inSN);

	sn.setDuration(inResidualDuration);
	se.addTiedNote(sn);
	}


	private void	dump(String inTitle)
	{
	Iterator<Long>	it = f_Events.keySet().iterator();

	System.out.println();
	System.out.println("MiStaff dump " + inTitle + "...");
	System.out.println();

	while(it.hasNext())
		{
		Long			time	= it.next();
		MiStaffEvent	se		= f_Events.get(time);

		System.out.print(se);
		}
	}


	void	createMeasures()
	{
	TGSongManager	tgSongMgr	= TuxGuitar.instance().getSongManager();
	Iterator<Long>		it			= f_Events.keySet().iterator();

	while(it.hasNext())
		{
		Long			key	= it.next();
		MiStaffEvent	se	= f_Events.get(key);

		if(	se.isBar() &&
			tgSongMgr.getMeasureHeaderAt(key.longValue()) == null)
			{
			tgSongMgr.addNewMeasure(tgSongMgr.getSong().countMeasureHeaders() + 1);
			}
		}
	}


	private TGBeat	getEventBeat(long inTime)
	{
	MiStaffEvent	se		= f_Events.get( new Long(inTime) );
	TGBeat			tgBeat	= se.getBeat();

	// creates a TGBeat if needed
	if(tgBeat == null)
		{
		TGSongManager	tgSongMgr	= TuxGuitar.instance().getSongManager();
		TGTrackManager	tgTrackMgr	= tgSongMgr.getTrackManager();
		TGMeasure		tgMeasure	= tgTrackMgr.getMeasureAt(f_TgTrack, inTime);

		if(tgMeasure != null)
			{
			tgBeat = tgSongMgr.getFactory().newBeat();

			tgBeat.setStart(inTime);
			tgMeasure.addBeat(tgBeat);
			se.setBeat(tgBeat);
			}
		}

	return(tgBeat);
	}


	private void	insertNoteIntoTrack(TGBeat inTgBeat, MiStaffNote inSN)
	{
	TGSongManager	tgSongMgr	= TuxGuitar.instance().getSongManager();
	TGNote			tgNote		= tgSongMgr.getFactory().newNote();
	TGDuration		tgDuration	= tgSongMgr.getFactory().newDuration();

	tgNote.setString	(inSN.getString());
	tgNote.setValue		(inSN.getFret());
	tgNote.setVelocity	(inSN.getVelocity());
	tgNote.setTiedNote	(inSN.isTied());

	int		noteType = MiStaffNote.durationToNoteType(inSN.getNominalDuration());

	tgDuration.setValue(noteType);
	tgDuration.setDotted(inSN.getDotCount() == 1);
	tgDuration.setDoubleDotted(inSN.getDotCount() == 2);

	if(f_Dump_TrackGeneration)
		{
		System.out.println(
				"" + inTgBeat.getMeasure().getNumber() + " " + inTgBeat.getStart() +
				" (" + tgNote.getString() + "," + tgNote.getValue() + "," + tgNote.getVelocity() + ") " +
				"1/"	+ tgDuration.getValue() +
				", d: "	+ tgDuration.getTime() +
				(tgNote.isTiedNote() ? " (tied)" : "") +
				(tgDuration.getTime() != inSN.getOverallDuration() ? " snDur=" + inSN.getOverallDuration() : ""));
		}

	// here we probably should choose the voice
	// it would be nice to have one voice for each string...
	inTgBeat.getVoice(0).setDuration(tgDuration);
	inTgBeat.getVoice(0).addNote(tgNote);
	}


	private void	generateTrack(String inTrackName)
	{
	TGSongManager	tgSongMgr	= TuxGuitar.instance().getSongManager();
	TGTrack			tgTrack		= tgSongMgr.addTrack();
	Iterator<Long>		eventsIt;

	if(f_Dump_TrackGeneration)
		{
		System.out.println();
		System.out.println("generating track: " + inTrackName + "...");
		System.out.println();
		}

	tgTrack.setName(inTrackName);

	f_TgTrack = tgTrack;

	// clears events TGBeats
	for(eventsIt = f_Events.keySet().iterator(); eventsIt.hasNext();)
		{
		Long			time	= eventsIt.next();
		MiStaffEvent	se		= f_Events.get(time);

		se.setBeat(null);
		}

	// generate TuxGuitar track
	for(eventsIt = f_Events.keySet().iterator(); eventsIt.hasNext();)
		{
		Long			time	= eventsIt.next();
		MiStaffEvent	se		= f_Events.get(time);

		if(se.isOnBeat() || se.isTieBeat())
			{
			TGBeat		tgBeat	= getEventBeat(se.getBeginTime());
			Iterator<MiStaffNote>	it		= se.getNotes().iterator();

			while(it.hasNext())
				{
				MiStaffNote	sn = it.next();

				insertNoteIntoTrack(tgBeat, sn);
				}
			}
		}
	}


	void	insertNotesIntoTrack(String inTrackName)
	{
	// normalize beats
	TreeMap<Long, MiStaffEvent>		normalizedEvents = new TreeMap<Long, MiStaffEvent>();

	for(Iterator<Entry<Long, MiStaffEvent>> eventsIt = f_Events.entrySet().iterator(); eventsIt.hasNext();)
		{
		Map.Entry<Long, MiStaffEvent>		me	= eventsIt.next();
		MiStaffEvent	se	= me.getValue();

		se.normalizeBeat(TGDuration.SIXTY_FOURTH);
		mergeEvent(normalizedEvents, se);
		}

	f_Events = normalizedEvents;

	dump("after beat normalization");
	generateTrack("after beat normalization");

	// add tie events due to bar crossing at the beginning of each crossed bar
	boolean	keepGoing = true;

	while(keepGoing)
		{
		long	nextBarBeginTime = 0;

		keepGoing = false;

		for(Iterator<Entry<Long, MiStaffEvent>> eventsIt = f_Events.entrySet().iterator(); eventsIt.hasNext();)
			{
			Map.Entry<Long, MiStaffEvent>		me	= eventsIt.next();
			MiStaffEvent	se	= me.getValue();

			if(se.isBar())
				nextBarBeginTime = se.getBeginTime() + 4 * TGDuration.QUARTER_TIME;	// bar length should be a MiStaff member

			for(Iterator<MiStaffNote> snIt = se.getNotes().iterator(); snIt.hasNext();)
				{
				MiStaffNote	sn = snIt.next();

				if(se.getBeginTime() + sn.getOverallDuration() > nextBarBeginTime)
					{
					long	limitedDuration		= (nextBarBeginTime - se.getBeginTime()),
							residualDuration	= sn.getOverallDuration() - limitedDuration;

					sn.setDuration(limitedDuration);
					addTiedNote(nextBarBeginTime, sn, residualDuration);
					keepGoing = true;
					break;
					}
				}

			if(keepGoing)
				break;
			}
		}

	dump("after tied due to bar crossing");
	generateTrack("after tied due to bar crossing");

	// normalize durations
	for(Iterator<Entry<Long, MiStaffEvent>> eventsIt = f_Events.entrySet().iterator(); eventsIt.hasNext();)
		{
		Map.Entry<Long, MiStaffEvent>		me	= eventsIt.next();
		MiStaffEvent	se	= me.getValue();

		se.normalizeDurations();
		}

	dump("after duration normalization");
	generateTrack("after duration normalization");

	// add tie events due to note crossing
	keepGoing = true;

	while(keepGoing)
		{
		keepGoing = false;

		Iterator<Long> eventsIt2 = f_Events.keySet().iterator();

		if(eventsIt2.hasNext())
			eventsIt2.next();

		for(Iterator<Entry<Long, MiStaffEvent>> eventsIt = f_Events.entrySet().iterator(); eventsIt.hasNext();)
			{
			Map.Entry<Long, MiStaffEvent>		me	= eventsIt.next();
			MiStaffEvent	se	= me.getValue();

			if(eventsIt.hasNext() && eventsIt2.hasNext())
				{
				long	nextTime	= eventsIt2.next().longValue();

				for(Iterator<MiStaffNote> snIt = se.getNotes().iterator(); snIt.hasNext();)
					{
					MiStaffNote	sn = snIt.next();

					if(se.getBeginTime() + sn.getOverallDuration() > nextTime)
						{
						long	limitedDuration		= (nextTime - se.getBeginTime()),
								residualDuration	= sn.getOverallDuration() - limitedDuration;

						sn.setDuration(limitedDuration);
						addTiedNote(nextTime, sn, residualDuration);
						keepGoing = true;
						break;
						}
					}

				if(keepGoing)
					break;
				}
			}
		}

	dump("after tied due to note crossing");
	generateTrack("after tied due to note crossing");

	// normalize durations
	for(Iterator<Entry<Long, MiStaffEvent>> eventsIt = f_Events.entrySet().iterator(); eventsIt.hasNext();)
		{
		Map.Entry<Long, MiStaffEvent>		me	= eventsIt.next();
		MiStaffEvent	se	= me.getValue();

		se.normalizeDurations();
		}

	dump("after duration normalization 2");
	generateTrack("after duration normalization 2");
	}
}
