/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.effects.ChangeAccentuatedNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeBendNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeDeadNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeFadeInAction;
import org.herac.tuxguitar.app.actions.effects.ChangeGhostNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeGraceNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeHammerNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeHarmonicNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeHeavyAccentuatedNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeLetRingAction;
import org.herac.tuxguitar.app.actions.effects.ChangePalmMuteAction;
import org.herac.tuxguitar.app.actions.effects.ChangePoppingAction;
import org.herac.tuxguitar.app.actions.effects.ChangeSlappingAction;
import org.herac.tuxguitar.app.actions.effects.ChangeSlideNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeStaccatoAction;
import org.herac.tuxguitar.app.actions.effects.ChangeTappingAction;
import org.herac.tuxguitar.app.actions.effects.ChangeTremoloBarAction;
import org.herac.tuxguitar.app.actions.effects.ChangeTremoloPickingAction;
import org.herac.tuxguitar.app.actions.effects.ChangeTrillNoteAction;
import org.herac.tuxguitar.app.actions.effects.ChangeVibratoNoteAction;
import org.herac.tuxguitar.app.items.MenuItems;
import org.herac.tuxguitar.song.models.TGNote;

/**
 * @author julian
 */
public class NoteEffectsMenuItem extends MenuItems{

	private MenuItem noteEffectsMenuItem;
	private Menu menu;
	private MenuItem vibrato;
	private MenuItem bend;
	private MenuItem tremoloBar;
	private MenuItem deadNote;
	private MenuItem slide;
	private MenuItem hammer;
	private MenuItem ghostNote;
	private MenuItem accentuatedNote;
	private MenuItem heavyAccentuatedNote;
	private MenuItem letRing;
	private MenuItem harmonicNote;
	private MenuItem graceNote;
	private MenuItem trill;
	private MenuItem tremoloPicking;
	private MenuItem palmMute;
	private MenuItem staccato;
	private MenuItem tapping;
	private MenuItem slapping;
	private MenuItem popping;

	private MenuItem fadeIn;

	public NoteEffectsMenuItem(Shell shell,Menu parent, int style) {
		this.noteEffectsMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}

	@Override
	public void showItems(){
		//--VIBRATO--
		this.vibrato = new MenuItem(this.menu, SWT.CHECK);
		this.vibrato.addSelectionListener(TuxGuitar.instance().getAction(ChangeVibratoNoteAction.NAME));

		//--BEND--
		this.bend = new MenuItem(this.menu, SWT.CHECK);
		this.bend.addSelectionListener(TuxGuitar.instance().getAction(ChangeBendNoteAction.NAME));

		//--BEND--
		this.tremoloBar = new MenuItem(this.menu, SWT.CHECK);
		this.tremoloBar.addSelectionListener(TuxGuitar.instance().getAction(ChangeTremoloBarAction.NAME));

		//--SLIDE--
		this.slide = new MenuItem(this.menu, SWT.CHECK);
		this.slide.addSelectionListener(TuxGuitar.instance().getAction(ChangeSlideNoteAction.NAME));

		//--SLIDE--
		this.deadNote = new MenuItem(this.menu, SWT.CHECK);
		this.deadNote.addSelectionListener(TuxGuitar.instance().getAction(ChangeDeadNoteAction.NAME));

		//--HAMMER--
		this.hammer = new MenuItem(this.menu, SWT.CHECK);
		this.hammer.addSelectionListener(TuxGuitar.instance().getAction(ChangeHammerNoteAction.NAME));

		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);

		//--GHOST NOTE--
		this.ghostNote = new MenuItem(this.menu, SWT.CHECK);
		this.ghostNote.addSelectionListener(TuxGuitar.instance().getAction(ChangeGhostNoteAction.NAME));

		//--ACCENTUATED NOTE--
		this.accentuatedNote = new MenuItem(this.menu, SWT.CHECK);
		this.accentuatedNote.addSelectionListener(TuxGuitar.instance().getAction(ChangeAccentuatedNoteAction.NAME));

		//--HEAVY ACCENTUATED NOTE--
		this.heavyAccentuatedNote = new MenuItem(this.menu, SWT.CHECK);
		this.heavyAccentuatedNote.addSelectionListener(TuxGuitar.instance().getAction(ChangeHeavyAccentuatedNoteAction.NAME));

		//--LET RING--
		this.letRing = new MenuItem(this.menu, SWT.CHECK);
		this.letRing.addSelectionListener(TuxGuitar.instance().getAction(ChangeLetRingAction.NAME));

		//--HARMONIC NOTE--
		this.harmonicNote = new MenuItem(this.menu, SWT.CHECK);
		this.harmonicNote.addSelectionListener(TuxGuitar.instance().getAction(ChangeHarmonicNoteAction.NAME));

		//--GRACE NOTE--
		this.graceNote = new MenuItem(this.menu, SWT.CHECK);
		this.graceNote.addSelectionListener(TuxGuitar.instance().getAction(ChangeGraceNoteAction.NAME));

		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);

		//--TRILL--
		this.trill = new MenuItem(this.menu, SWT.CHECK);
		this.trill.addSelectionListener(TuxGuitar.instance().getAction(ChangeTrillNoteAction.NAME));

		//--TREMOLO PICKING--
		this.tremoloPicking = new MenuItem(this.menu, SWT.CHECK);
		this.tremoloPicking.addSelectionListener(TuxGuitar.instance().getAction(ChangeTremoloPickingAction.NAME));

		//--PALM MUTE--
		this.palmMute = new MenuItem(this.menu, SWT.CHECK);
		this.palmMute.addSelectionListener(TuxGuitar.instance().getAction(ChangePalmMuteAction.NAME));

		//--STACCATO
		this.staccato = new MenuItem(this.menu, SWT.CHECK);
		this.staccato.addSelectionListener(TuxGuitar.instance().getAction(ChangeStaccatoAction.NAME));

		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);

		//--TAPPING
		this.tapping = new MenuItem(this.menu, SWT.CHECK);
		this.tapping.addSelectionListener(TuxGuitar.instance().getAction(ChangeTappingAction.NAME));

		//--SLAPPING
		this.slapping = new MenuItem(this.menu, SWT.CHECK);
		this.slapping.addSelectionListener(TuxGuitar.instance().getAction(ChangeSlappingAction.NAME));

		//--POPPING
		this.popping = new MenuItem(this.menu, SWT.CHECK);
		this.popping.addSelectionListener(TuxGuitar.instance().getAction(ChangePoppingAction.NAME));

		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);

		//--FADE IN
		this.fadeIn = new MenuItem(this.menu, SWT.CHECK);
		this.fadeIn.addSelectionListener(TuxGuitar.instance().getAction(ChangeFadeInAction.NAME));

		this.noteEffectsMenuItem.setMenu(this.menu);

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void update(){
		TGNote note = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getSelectedNote();
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.vibrato.setSelection(note != null && note.getEffect().isVibrato());
		this.vibrato.setEnabled(!running && note != null);
		this.bend.setSelection(note != null && note.getEffect().isBend());
		this.bend.setEnabled(!running && note != null);
		this.tremoloBar.setSelection(note != null && note.getEffect().isTremoloBar());
		this.tremoloBar.setEnabled(!running && note != null);
		this.deadNote.setSelection(note != null && note.getEffect().isDeadNote());
		this.deadNote.setEnabled(!running && note != null);
		this.slide.setSelection(note != null && note.getEffect().isSlide());
		this.slide.setEnabled(!running && note != null);
		this.hammer.setSelection(note != null && note.getEffect().isHammer());
		this.hammer.setEnabled(!running && note != null);
		this.ghostNote.setSelection(note != null && note.getEffect().isGhostNote());
		this.ghostNote.setEnabled(!running && note != null);
		this.accentuatedNote.setSelection(note != null && note.getEffect().isAccentuatedNote());
		this.accentuatedNote.setEnabled(!running && note != null);
		this.heavyAccentuatedNote.setSelection(note != null && note.getEffect().isHeavyAccentuatedNote());
		this.heavyAccentuatedNote.setEnabled(!running && note != null);
		this.letRing.setSelection(note != null && note.getEffect().isLetRing());
		this.letRing.setEnabled(!running && note != null);
		this.harmonicNote.setSelection(note != null && note.getEffect().isHarmonic());
		this.harmonicNote.setEnabled(!running && note != null);
		this.graceNote.setSelection(note != null && note.getEffect().isGrace());
		this.graceNote.setEnabled(!running && note != null);
		this.trill.setSelection(note != null && note.getEffect().isTrill());
		this.trill.setEnabled(!running && note != null);
		this.tremoloPicking.setSelection(note != null && note.getEffect().isTremoloPicking());
		this.tremoloPicking.setEnabled(!running && note != null);
		this.palmMute.setSelection(note != null && note.getEffect().isPalmMute());
		this.palmMute.setEnabled(!running && note != null);
		this.staccato.setSelection(note != null && note.getEffect().isStaccato());
		this.staccato.setEnabled(!running && note != null);
		this.tapping.setSelection(note != null && note.getEffect().isTapping());
		this.tapping.setEnabled(!running && note != null);
		this.slapping.setSelection(note != null && note.getEffect().isSlapping());
		this.slapping.setEnabled(!running && note != null);
		this.popping.setSelection(note != null && note.getEffect().isPopping());
		this.popping.setEnabled(!running && note != null);
		this.fadeIn.setSelection(note != null && note.getEffect().isFadeIn());
		this.fadeIn.setEnabled(!running && note != null);
	}

	@Override
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.noteEffectsMenuItem, "effects", null);
		setMenuItemTextAndAccelerator(this.vibrato, "effects.vibrato", ChangeVibratoNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.bend, "effects.bend", ChangeBendNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.tremoloBar, "effects.tremolo-bar", ChangeTremoloBarAction.NAME);
		setMenuItemTextAndAccelerator(this.deadNote, "effects.deadnote", ChangeDeadNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.slide, "effects.slide", ChangeSlideNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.hammer, "effects.hammer", ChangeHammerNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.ghostNote, "effects.ghostnote", ChangeGhostNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.accentuatedNote, "effects.accentuatednote", ChangeAccentuatedNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.heavyAccentuatedNote, "effects.heavyaccentuatednote", ChangeHeavyAccentuatedNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.letRing, "effects.let-ring", ChangeLetRingAction.NAME);
		setMenuItemTextAndAccelerator(this.harmonicNote, "effects.harmonic", ChangeHarmonicNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.graceNote, "effects.grace", ChangeGraceNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.trill, "effects.trill", ChangeTrillNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.tremoloPicking, "effects.tremolo-picking", ChangeTremoloPickingAction.NAME);
		setMenuItemTextAndAccelerator(this.palmMute, "effects.palm-mute", ChangePalmMuteAction.NAME);
		setMenuItemTextAndAccelerator(this.staccato, "effects.staccato", ChangeStaccatoAction.NAME);
		setMenuItemTextAndAccelerator(this.tapping, "effects.tapping", ChangeTappingAction.NAME);
		setMenuItemTextAndAccelerator(this.slapping, "effects.slapping", ChangeSlappingAction.NAME);
		setMenuItemTextAndAccelerator(this.popping, "effects.popping", ChangePoppingAction.NAME);
		setMenuItemTextAndAccelerator(this.fadeIn, "effects.fade-in", ChangeFadeInAction.NAME);
	}

	public void loadIcons(){
		//Nothing to do
	}
}
