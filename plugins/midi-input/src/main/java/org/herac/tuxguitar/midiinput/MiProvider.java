package org.herac.tuxguitar.midiinput;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;

import javax.sound.midi.ShortMessage;
import javax.swing.Timer;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.ActionLock;
import org.herac.tuxguitar.app.editors.TablatureEditor;
import org.herac.tuxguitar.app.editors.fretboard.FretBoard;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.tools.scale.ScaleManager;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.app.util.MessageDialog;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.graphics.control.TGTrackImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.util.TGSynchronizer;

public class MiProvider
{
private final int	DEVICE_CHANNELS_COUNT	= 6;	// number of MIDI channels supported by the input device

private int			f_Mode			= MiConfig.MODE_FRETBOARD_ECHO;		// current mode
private int			f_ChordMode		= MiConfig.CHORD_MODE_DIAGRAM;		// current chord mode
private int			f_BaseChannel	= 0;								// 0-based MIDI channel corresponding to the first string
private byte		f_MinVelocity	= MiConfig.DEF_VELOCITY_THRESHOLD;	// notes with velocity lower than this threshold are considered unwanted noise
private long		f_MinDuration	= MiConfig.DEF_DURATION_THRESHOLD;	// notes with duration lower than this threshold are considered unwanted noise

private final int[]		f_EchoNotes		= new int[6];					// list of notes for echo
private	Timer		f_EchoTimer		= null;							// timer for echo rendering
private int			f_EchoTimeOut	= MiConfig.DEF_ECHO_TIMEOUT;	// time out for echo rendering [msec]
private boolean		f_EchoLastWasOn	= false;						// indicates if last note message was NOTE_ON
private TGBeat		f_EchoBeat		= null;							// beat for echo rendering

private	Timer		f_InputTimer	= null;							// timer for chord/scale input
private int			f_InputTimeOut	= MiConfig.DEF_INPUT_TIMEOUT;	// time out for chord/scale input [msec]

private final	MiBuffer	f_Buffer		= new MiBuffer();				// input notes buffer

static private	MiProvider	s_Instance;


	private MiProvider()
	{
	echo_ResetNotes();
	}


	static public MiProvider instance()
	{
	if(s_Instance == null)
		s_Instance = new MiProvider();

	return s_Instance;
	}


	public void	setMode(int inValue)
	{
	f_Mode = inValue;
	echo_ResetNotes();
	}


	int	getMode()	{ return f_Mode; }

	public void	setBaseChannel	(int inValue)	{ f_BaseChannel = inValue; }
	public void	setMinVelocity	(byte inValue)	{ f_MinVelocity = inValue; }
	public void	setMinDuration	(long inValue)	{ f_MinDuration = inValue; }
	public void	setEchoTimeOut	(int inValue)	{ f_EchoTimeOut = inValue; }
	public void	setInputTimeOut	(int inValue)	{ f_InputTimeOut = inValue; }
	public void	setChordMode	(int inValue)	{ f_ChordMode = inValue; }


	public void	echo_ResetNotes()
	{
	f_EchoLastWasOn = false;

	for(int s = 0; s < f_EchoNotes.length; s++)
		f_EchoNotes[s] = -1;
	}


	public void	noteReceived(ShortMessage inMessage, long inTimeStamp)
	{
	byte	pitch		= (byte)inMessage.getData1(),
			velocity	= (byte)inMessage.getData2(),
			stringIndex = (byte)getString(inMessage.getChannel());

	if(stringIndex != -1)
		{
		byte	fretIndex = (byte)getFret(pitch, stringIndex);

		if(fretIndex != -1)
			{
			switch(inMessage.getCommand())
				{
				case ShortMessage.NOTE_ON:
					{
					switch(f_Mode)
						{
						case MiConfig.MODE_FRETBOARD_ECHO:
							if(velocity == 0 || velocity > f_MinVelocity)	// questo VA MODIFICATO!!!
								echo(stringIndex, fretIndex, velocity > 0);
							break;

						case MiConfig.MODE_CHORDS_RECORDING:
							if(velocity == 0 || velocity > f_MinVelocity)	// questo VA MODIFICATO!!!
								echo(stringIndex, fretIndex, velocity > 0);

							chord_AddNote(stringIndex, fretIndex, pitch, velocity, inTimeStamp);
							break;

						case MiConfig.MODE_SCALES_RECOGNITION:
							if(velocity == 0 || velocity > f_MinVelocity)	// questo VA MODIFICATO!!!
								echo(stringIndex, fretIndex, velocity > 0);

							scale_AddNote(stringIndex, fretIndex, pitch, velocity, inTimeStamp);
							break;

						case MiConfig.MODE_SONG_RECORDING:
							if(velocity == 0 || velocity > f_MinVelocity)	// questo VA MODIFICATO!!!
								echo(stringIndex, fretIndex, velocity > 0);

							MiRecorder.instance().addNote(stringIndex, fretIndex, pitch, velocity, inTimeStamp);
							break;
						}
					}
					break;

				case ShortMessage.NOTE_OFF:
					switch(f_Mode)
						{
						case MiConfig.MODE_FRETBOARD_ECHO:
							echo(stringIndex, fretIndex, false);
							break;

						case MiConfig.MODE_CHORDS_RECORDING:
							echo(stringIndex, fretIndex, false);
							chord_AddNote(stringIndex, fretIndex, pitch, (byte)0, inTimeStamp);
							break;

						case MiConfig.MODE_SCALES_RECOGNITION:
							echo(stringIndex, fretIndex, false);
							scale_AddNote(stringIndex, fretIndex, pitch, (byte)0, inTimeStamp);
							break;

						case MiConfig.MODE_SONG_RECORDING:
							echo(stringIndex, fretIndex, false);
							MiRecorder.instance().addNote(stringIndex, fretIndex, pitch, (byte)0, inTimeStamp);
							break;
						}
					break;
				}
			}
		}
	}


	private int	getString(int inChannel)
	{
	// returns the 1-based string index corresponding to the 0-based specified MIDI channel
	// or -1 if the current track does not have such string

	TGTrackImpl	track = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getTrack();

	if(track != null)
		{
		int		stringsCount	= track.getStrings().size(),
				stringIndex		= inChannel - (f_BaseChannel + (DEVICE_CHANNELS_COUNT - stringsCount)) + 1;

		if(stringIndex > 0 && stringIndex <= stringsCount)
			return(stringIndex);
		}

	return(-1);
	}


	static int	getStringFirstPitch(int inString)
	{
	// returns the note corresponding to the free vibrating string
	// or -1 if the current track does not have such string

	TGTrackImpl	track = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getTrack();

	if(	track != null &&
		track.getStrings().size() >= inString)
		return(track.getString(inString).getValue());

	return(-1);
	}


	static int	getFret(int inPitch, int inString)
	{
	// returns the 0-based fret index corresponding to the specified note and string
	// or -1 if an error occurred

	int		stringFirstPitch = getStringFirstPitch(inString);

	if(stringFirstPitch != -1)
		{
		int		fret = inPitch - stringFirstPitch;

		if(fret >= 0 && fret < FretBoard.MAX_FRETS)
			return(fret);
		}

	return(-1);
	}


	private void	echo_UpdateExternalBeat(boolean inIsEmpty)
	{
	TGSynchronizer.TGRunnable	task;

	if(inIsEmpty)
		{
		task = new TGSynchronizer.TGRunnable() {
			@Override
			public void run() throws Throwable {
				TuxGuitar.instance().hideExternalBeat();
				}
			};
		}
	else
		{
		task = new TGSynchronizer.TGRunnable() {
			@Override
			public void run() throws Throwable {
				TuxGuitar.instance().showExternalBeat(f_EchoBeat);
				}
			};
		}

	try {
		TGSynchronizer.instance().runLater(task);
		}
	catch(Throwable t)
		{
		MessageDialog.errorMessage(t);
		}
	}


	private void	echo_BuildAndShowBeat(int inString, int inFret, boolean inIsOn)
	{
	if(f_Mode == MiConfig.MODE_SCALES_RECOGNITION && f_InputTimer == null)
		return;

	f_EchoNotes[inString - 1] = (inIsOn ? inFret : -1);
	f_EchoLastWasOn = inIsOn;

	TGSongManager	songMgr	= TuxGuitar.instance().getSongManager();

	f_EchoBeat = songMgr.getFactory().newBeat();

	for(int s = 0; s < f_EchoNotes.length; s++)
		{
		if(f_EchoNotes[s] != -1)
			{
			TGNote	note = songMgr.getFactory().newNote();

			note.setString(s + 1);
			note.setValue(f_EchoNotes[s]);
			f_EchoBeat.getVoice(0).addNote(note);
			}
		}

	echo_UpdateExternalBeat(false);
	}


	private void	echo(int inString, int inFret, boolean inIsNoteOn)
	{
	if(f_EchoTimer == null)
		{
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt)
				{
				if(f_EchoLastWasOn)
					{
					f_EchoTimer.restart();
					}
				else
					{
					f_EchoTimer.stop();
					f_EchoTimer = null;

					if(f_Mode != MiConfig.MODE_SCALES_RECOGNITION)
						echo_UpdateExternalBeat(true);
					}
				}
			};

		echo_ResetNotes();
		echo_BuildAndShowBeat(inString, inFret, inIsNoteOn);
		f_EchoTimer = new Timer(f_EchoTimeOut, taskPerformer);
		f_EchoTimer.start();
		}
	else
		{
		echo_BuildAndShowBeat(inString, inFret, inIsNoteOn);
		f_EchoTimer.restart();
		}
	}


	private void	chord_AddNote(byte inString, byte inFret, byte inPitch, byte inVelocity, long inTimeStamp)
	{
	if(f_InputTimer == null)
		{
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				f_InputTimer.stop();
				f_InputTimer = null;

				f_Buffer.stopRecording(MiPort.getNotesPortTimeStamp());
				//System.out.println("Chord ended");

				if(f_Buffer.finalize(f_MinVelocity, f_MinDuration * 1000) > 0)
					{
					if(!TuxGuitar.instance().getPlayer().isRunning() && !TuxGuitar.instance().isLocked() && !ActionLock.isLocked())
						{
						TablatureEditor	editor	= TuxGuitar.instance().getTablatureEditor();
						Caret			caret	= editor.getTablature().getCaret();
						TGTrackImpl		track	= caret.getTrack();
						TGMeasureImpl	measure	= caret.getMeasure();
						TGBeat			beat	= caret.getSelectedBeat();
						TGSongManager	songMgr	= TuxGuitar.instance().getSongManager();

						TGChord			chord	= f_Buffer.toChord(measure.getTrack().stringCount());
						//TGBeat		_beat		= f_Buffer.toBeat();

						// emulates InsertChordAction
						ActionLock.lock();

						UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

						if(f_ChordMode == MiConfig.CHORD_MODE_ALL)
							{
							songMgr.getMeasureManager().cleanBeat(beat);

							TGVoice		voice	= beat.getVoice(caret.getVoice());
							Iterator<TGString>	it		= track.getStrings().iterator();

							while(it.hasNext())
								{
								TGString	string	= it.next();
								int			value	= chord.getFretValue(string.getNumber() - 1);

								if(value >= 0)
									{
									TGNote note = songMgr.getFactory().newNote();
									note.setValue(value);
									note.setVelocity(editor.getTablature().getCaret().getVelocity());
									note.setString(string.getNumber());

									TGDuration duration = songMgr.getFactory().newDuration();
									voice.getDuration().copy(duration);

									songMgr.getMeasureManager().addNote(beat, note, duration, voice.getIndex());
									}
								}
							}

						songMgr.getMeasureManager().addChord(beat, chord);
						TuxGuitar.instance().getFileHistory().setUnsavedFile();
						editor.getTablature().updateMeasure(measure.getNumber());

						TuxGuitar.instance().getUndoableManager().addEdit(undoable.endUndo());

						ActionLock.unlock();
						TuxGuitar.instance().updateCache(true);
						}
					}
				}
			};

		if(inVelocity > 0)
			{
			//System.out.println("New chord");

			f_Buffer.startRecording(MiPort.getNotesPortTimeStamp());
			f_Buffer.addEvent(inString, inFret, inPitch, inVelocity, inTimeStamp);

			f_InputTimer = new Timer(f_InputTimeOut, taskPerformer);
			f_InputTimer.start();
			}
		}
	else
		{
		f_Buffer.addEvent(inString, inFret, inPitch, inVelocity, inTimeStamp);

		if(inVelocity > 0)
			f_InputTimer.restart();
		}
	}


	private void	scale_AddNote(byte inString, byte inFret, byte inPitch, byte inVelocity, long inTimeStamp)
	{
	if(f_InputTimer == null)
		{
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				f_InputTimer.stop();
				f_InputTimer = null;

				f_Buffer.stopRecording(MiPort.getNotesPortTimeStamp());
				//System.out.println("Scale ended");

				if(f_Buffer.finalize(f_MinVelocity, f_MinDuration * 1000) > 0)
					{
					TGBeat		beat		= f_Buffer.toBeat();
					TreeSet<Byte>	pitches	= f_Buffer.toPitchesSet();

					MiScaleFinder.findMatchingScale(pitches);
					TuxGuitar.instance().showExternalBeat(beat);
					}
				else
					{
					TuxGuitar.instance().hideExternalBeat();
					MiScaleFinder.selectScale(ScaleManager.NONE_SELECTION, 0);
					}

				TuxGuitar.instance().updateCache(true);
				}
			};

		if(inVelocity > 0)
			{
			//System.out.println("New scale");

			f_Buffer.startRecording(MiPort.getNotesPortTimeStamp());
			f_Buffer.addEvent(inString, inFret, inPitch, inVelocity, inTimeStamp);

			f_InputTimer = new Timer(f_InputTimeOut, taskPerformer);
			f_InputTimer.start();
			}
		}
	else
		{
		f_Buffer.addEvent(inString, inFret, inPitch, inVelocity, inTimeStamp);

		if(inVelocity > 0)
			f_InputTimer.restart();
		}
	}
}
