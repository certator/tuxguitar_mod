/*
 * Created on 02-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.items.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.action.TGActionProcessor;
import org.herac.tuxguitar.app.action.impl.insert.InsertTextAction;
import org.herac.tuxguitar.app.action.impl.note.ChangeTiedNoteAction;
import org.herac.tuxguitar.app.action.impl.note.CleanBeatAction;
import org.herac.tuxguitar.app.action.impl.note.DecrementNoteSemitoneAction;
import org.herac.tuxguitar.app.action.impl.note.DeleteNoteOrRestAction;
import org.herac.tuxguitar.app.action.impl.note.IncrementNoteSemitoneAction;
import org.herac.tuxguitar.app.action.impl.note.InsertRestBeatAction;
import org.herac.tuxguitar.app.action.impl.note.MoveBeatsCustomAction;
import org.herac.tuxguitar.app.action.impl.note.MoveBeatsLeftAction;
import org.herac.tuxguitar.app.action.impl.note.MoveBeatsRightAction;
import org.herac.tuxguitar.app.action.impl.note.RemoveUnusedVoiceAction;
import org.herac.tuxguitar.app.action.impl.note.SetStrokeDownAction;
import org.herac.tuxguitar.app.action.impl.note.SetStrokeUpAction;
import org.herac.tuxguitar.app.action.impl.note.SetVoiceAutoAction;
import org.herac.tuxguitar.app.action.impl.note.SetVoiceDownAction;
import org.herac.tuxguitar.app.action.impl.note.SetVoiceUpAction;
import org.herac.tuxguitar.app.action.impl.note.ShiftNoteDownAction;
import org.herac.tuxguitar.app.action.impl.note.ShiftNoteUpAction;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.items.MenuItems;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGStroke;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BeatMenuItem extends MenuItems{
	
	private MenuItem noteMenuItem;
	private Menu menu;
	private MenuItem tiedNote;
	private MenuItem insertRestBeat;
	private MenuItem deleteNoteOrRest;
	private MenuItem cleanBeat;
	private MenuItem removeVoice;
	private MenuItem insertText;
	private MenuItem voiceAuto;
	private MenuItem voiceUp;
	private MenuItem voiceDown;
	private MenuItem strokeUp;
	private MenuItem strokeDown;
	private MenuItem shiftUp;
	private MenuItem shiftDown;
	private MenuItem semitoneUp;
	private MenuItem semitoneDown;
	private MenuItem moveBeatsLeft;
	private MenuItem moveBeatsRight;
	private MenuItem moveBeatsCustom;
	private DurationMenuItem durationMenuItem;
	private ChordMenuItem chordMenuItem;
	private NoteEffectsMenuItem effectMenuItem;
	private DynamicMenuItem dynamicMenuItem;
	
	public BeatMenuItem(Shell shell,Menu parent, int style) {
		this.noteMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}
	
	public void showItems(){
		//--Tied Note
		this.tiedNote = new MenuItem(this.menu, SWT.CHECK);
		this.tiedNote.addSelectionListener(new TGActionProcessor(ChangeTiedNoteAction.NAME));
		
		//--Insert Rest Beat
		this.insertRestBeat = new MenuItem(this.menu, SWT.PUSH);
		this.insertRestBeat.addSelectionListener(new TGActionProcessor(InsertRestBeatAction.NAME));
		
		//--Delete Note or Rest
		this.deleteNoteOrRest = new MenuItem(this.menu, SWT.PUSH);
		this.deleteNoteOrRest.addSelectionListener(new TGActionProcessor(DeleteNoteOrRestAction.NAME));
		
		//--Clean Beat
		this.cleanBeat = new MenuItem(this.menu, SWT.PUSH);
		this.cleanBeat.addSelectionListener(new TGActionProcessor(CleanBeatAction.NAME));
		
		//--Remove Voice
		this.removeVoice = new MenuItem(this.menu, SWT.PUSH);
		this.removeVoice.addSelectionListener(new TGActionProcessor(RemoveUnusedVoiceAction.NAME));
		
		//--Duration--
		this.durationMenuItem = new DurationMenuItem(this.menu.getShell(),this.menu,SWT.CASCADE);
		this.durationMenuItem.showItems();
		
		//--Chord--
		this.chordMenuItem = new ChordMenuItem(this.menu.getShell(),this.menu,SWT.CASCADE);
		this.chordMenuItem.showItems();
		
		//--Effects--
		this.effectMenuItem = new NoteEffectsMenuItem(this.menu.getShell(),this.menu,SWT.CASCADE);
		this.effectMenuItem.showItems();
		
		//--Dynamic--
		this.dynamicMenuItem = new DynamicMenuItem(this.menu.getShell(),this.menu,SWT.CASCADE);
		this.dynamicMenuItem.showItems();
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		this.insertText = new MenuItem(this.menu, SWT.PUSH);
		this.insertText.addSelectionListener(new TGActionProcessor(InsertTextAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--Semitone Down
		this.voiceAuto = new MenuItem(this.menu, SWT.PUSH);
		this.voiceAuto.addSelectionListener(new TGActionProcessor(SetVoiceAutoAction.NAME));
		
		//--Semitone Up
		this.voiceUp = new MenuItem(this.menu, SWT.PUSH);
		this.voiceUp.addSelectionListener(new TGActionProcessor(SetVoiceUpAction.NAME));
		
		//--Semitone Down
		this.voiceDown = new MenuItem(this.menu, SWT.PUSH);
		this.voiceDown.addSelectionListener(new TGActionProcessor(SetVoiceDownAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--Semitone Up
		this.strokeUp = new MenuItem(this.menu, SWT.CHECK);
		this.strokeUp.addSelectionListener(new TGActionProcessor(SetStrokeUpAction.NAME));
		
		//--Semitone Down
		this.strokeDown = new MenuItem(this.menu, SWT.CHECK);
		this.strokeDown.addSelectionListener(new TGActionProcessor(SetStrokeDownAction.NAME));
				
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--Semitone Up
		this.semitoneUp = new MenuItem(this.menu, SWT.PUSH);
		this.semitoneUp.addSelectionListener(new TGActionProcessor(IncrementNoteSemitoneAction.NAME));
		
		//--Semitone Down
		this.semitoneDown = new MenuItem(this.menu, SWT.PUSH);
		this.semitoneDown.addSelectionListener(new TGActionProcessor(DecrementNoteSemitoneAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--Shift Up
		this.shiftUp = new MenuItem(this.menu, SWT.PUSH);
		this.shiftUp.addSelectionListener(new TGActionProcessor(ShiftNoteUpAction.NAME));
		
		//--Shift Down
		this.shiftDown = new MenuItem(this.menu, SWT.PUSH);
		this.shiftDown.addSelectionListener(new TGActionProcessor(ShiftNoteDownAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--Move Beats Left
		this.moveBeatsLeft = new MenuItem(this.menu, SWT.PUSH);
		this.moveBeatsLeft.addSelectionListener(new TGActionProcessor(MoveBeatsLeftAction.NAME));
		
		//--Move Beats Right
		this.moveBeatsRight = new MenuItem(this.menu, SWT.PUSH);
		this.moveBeatsRight.addSelectionListener(new TGActionProcessor(MoveBeatsRightAction.NAME));
		
		//--Move Beats Custom
		this.moveBeatsCustom = new MenuItem(this.menu, SWT.PUSH);
		this.moveBeatsCustom.addSelectionListener(new TGActionProcessor(MoveBeatsCustomAction.NAME));
		
		this.noteMenuItem.setMenu(this.menu);
		
		this.loadIcons();
		this.loadProperties();
	}
	
	public void update(){
		Caret caret = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret();
		TGBeat beat = caret.getSelectedBeat();
		TGNote note = caret.getSelectedNote();
		boolean restBeat = caret.isRestBeatSelected();
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.tiedNote.setEnabled(!running);
		this.tiedNote.setSelection(note != null && note.isTiedNote());
		this.insertRestBeat.setEnabled(!running);
		this.deleteNoteOrRest.setEnabled(!running);
		this.cleanBeat.setEnabled(!running);
		this.removeVoice.setEnabled(!running);
		this.voiceAuto.setEnabled(!running && !restBeat);
		this.voiceUp.setEnabled(!running && !restBeat);
		this.voiceDown.setEnabled(!running && !restBeat);
		this.strokeUp.setEnabled(!running && !restBeat);
		this.strokeUp.setSelection( beat != null && beat.getStroke().getDirection() == TGStroke.STROKE_UP );
		this.strokeDown.setEnabled(!running && !restBeat);
		this.strokeDown.setSelection( beat != null && beat.getStroke().getDirection() == TGStroke.STROKE_DOWN );
		this.semitoneUp.setEnabled(!running && note != null);
		this.semitoneDown.setEnabled(!running && note != null);
		this.shiftUp.setEnabled(!running && note != null);
		this.shiftDown.setEnabled(!running && note != null);
		this.insertText.setEnabled(!running);
		this.moveBeatsLeft.setEnabled(!running);
		this.moveBeatsRight.setEnabled(!running);
		this.moveBeatsCustom.setEnabled(!running);
		this.durationMenuItem.update();
		this.chordMenuItem.update();
		this.effectMenuItem.update();
		this.dynamicMenuItem.update();
	}
	
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.noteMenuItem, "beat", null);
		setMenuItemTextAndAccelerator(this.cleanBeat, "beat.clean", CleanBeatAction.NAME);
		setMenuItemTextAndAccelerator(this.removeVoice, "beat.voice.remove-unused", RemoveUnusedVoiceAction.NAME);
		setMenuItemTextAndAccelerator(this.tiedNote, "note.tiednote", ChangeTiedNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.insertRestBeat, "beat.insert-rest", InsertRestBeatAction.NAME);
		setMenuItemTextAndAccelerator(this.deleteNoteOrRest, "beat.delete-note-or-rest", DeleteNoteOrRestAction.NAME);
		setMenuItemTextAndAccelerator(this.voiceAuto, "beat.voice-auto", SetVoiceAutoAction.NAME);
		setMenuItemTextAndAccelerator(this.voiceUp, "beat.voice-up", SetVoiceUpAction.NAME);
		setMenuItemTextAndAccelerator(this.voiceDown, "beat.voice-down", SetVoiceDownAction.NAME);
		setMenuItemTextAndAccelerator(this.strokeUp, "beat.stroke-up", SetStrokeUpAction.NAME);
		setMenuItemTextAndAccelerator(this.strokeDown, "beat.stroke-down", SetStrokeDownAction.NAME);
		setMenuItemTextAndAccelerator(this.semitoneUp, "note.semitone-up", IncrementNoteSemitoneAction.NAME);
		setMenuItemTextAndAccelerator(this.semitoneDown, "note.semitone-down", DecrementNoteSemitoneAction.NAME);
		setMenuItemTextAndAccelerator(this.shiftUp, "note.shift-up", ShiftNoteUpAction.NAME);
		setMenuItemTextAndAccelerator(this.shiftDown, "note.shift-down", ShiftNoteDownAction.NAME);
		setMenuItemTextAndAccelerator(this.insertText, "text.insert", InsertTextAction.NAME);
		setMenuItemTextAndAccelerator(this.moveBeatsLeft, "beat.move-left", MoveBeatsLeftAction.NAME);
		setMenuItemTextAndAccelerator(this.moveBeatsRight, "beat.move-right", MoveBeatsRightAction.NAME);
		setMenuItemTextAndAccelerator(this.moveBeatsCustom, "beat.move-custom", MoveBeatsCustomAction.NAME);
		
		this.durationMenuItem.loadProperties();
		this.chordMenuItem.loadProperties();
		this.effectMenuItem.loadProperties();
		this.dynamicMenuItem.loadProperties();
	}
	
	public void loadIcons(){
		this.tiedNote.setImage(TuxGuitar.instance().getIconManager().getNoteTied());
	}
}
