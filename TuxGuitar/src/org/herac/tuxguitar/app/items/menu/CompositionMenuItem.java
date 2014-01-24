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
import org.herac.tuxguitar.app.action.impl.composition.ChangeClefAction;
import org.herac.tuxguitar.app.action.impl.composition.ChangeInfoAction;
import org.herac.tuxguitar.app.action.impl.composition.ChangeKeySignatureAction;
import org.herac.tuxguitar.app.action.impl.composition.ChangeTempoAction;
import org.herac.tuxguitar.app.action.impl.composition.ChangeTimeSignatureAction;
import org.herac.tuxguitar.app.action.impl.composition.ChangeTripletFeelAction;
import org.herac.tuxguitar.app.action.impl.insert.RepeatAlternativeAction;
import org.herac.tuxguitar.app.action.impl.insert.RepeatCloseAction;
import org.herac.tuxguitar.app.action.impl.insert.RepeatOpenAction;
import org.herac.tuxguitar.app.items.MenuItems;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CompositionMenuItem extends MenuItems{
	private MenuItem compositionMenuItem;
	private Menu menu;
	private MenuItem timeSignature;
	private MenuItem tempo;
	private MenuItem clef;
	private MenuItem keySignature;
	private MenuItem repeatOpen;
	private MenuItem repeatClose;
	private MenuItem repeatAlternative;
	private MenuItem tripletFeel;
	
	private MenuItem properties;
	
	public CompositionMenuItem(Shell shell,Menu parent, int style) {
		this.compositionMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}
	
	public void showItems(){
		//--TIME SIGNATURE--
		this.timeSignature = new MenuItem(this.menu, SWT.PUSH);
		this.timeSignature.addSelectionListener(new TGActionProcessor(ChangeTimeSignatureAction.NAME));
		//--TEMPO--
		this.tempo = new MenuItem(this.menu, SWT.PUSH);
		this.tempo.addSelectionListener(new TGActionProcessor(ChangeTempoAction.NAME));
		//--CLEF--
		this.clef = new MenuItem(this.menu, SWT.PUSH);
		this.clef.addSelectionListener(new TGActionProcessor(ChangeClefAction.NAME));
		//--KEY SIGNATURE--
		this.keySignature = new MenuItem(this.menu, SWT.PUSH);
		this.keySignature.addSelectionListener(new TGActionProcessor(ChangeKeySignatureAction.NAME));
		//--TRIPLET FEEL--
		this.tripletFeel = new MenuItem(this.menu, SWT.PUSH);
		this.tripletFeel.addSelectionListener(new TGActionProcessor(ChangeTripletFeelAction.NAME));
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		//--REPEAT OPEN--
		this.repeatOpen = new MenuItem(this.menu, SWT.PUSH);
		this.repeatOpen.addSelectionListener(new TGActionProcessor(RepeatOpenAction.NAME));
		//--REPEAT CLOSE--
		this.repeatClose = new MenuItem(this.menu, SWT.PUSH);
		this.repeatClose.addSelectionListener(new TGActionProcessor(RepeatCloseAction.NAME));
		//--REPEAT ALTERNATIVE--
		this.repeatAlternative = new MenuItem(this.menu, SWT.PUSH);
		this.repeatAlternative.addSelectionListener(new TGActionProcessor(RepeatAlternativeAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		//--INFO--
		this.properties = new MenuItem(this.menu, SWT.PUSH);
		this.properties.addSelectionListener(new TGActionProcessor(ChangeInfoAction.NAME));
		
		this.compositionMenuItem.setMenu(this.menu);
		
		this.loadIcons();
		this.loadProperties();
	}
	
	public void update(){
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.timeSignature.setEnabled(!running);
		this.tempo.setEnabled(!running);
		this.clef.setEnabled(!running);
		this.keySignature.setEnabled(!running);
		this.tripletFeel.setEnabled(!running);
		this.repeatOpen.setEnabled(!running);
		this.repeatClose.setEnabled(!running);
		this.repeatAlternative.setEnabled(!running);
	}
	
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.compositionMenuItem, "composition", null);		
		setMenuItemTextAndAccelerator(this.timeSignature, "composition.timesignature", ChangeTimeSignatureAction.NAME);
		setMenuItemTextAndAccelerator(this.tempo, "composition.tempo", ChangeTempoAction.NAME);
		setMenuItemTextAndAccelerator(this.clef, "composition.clef", ChangeClefAction.NAME);
		setMenuItemTextAndAccelerator(this.keySignature, "composition.keysignature", ChangeKeySignatureAction.NAME);
		setMenuItemTextAndAccelerator(this.tripletFeel, "composition.tripletfeel", ChangeTripletFeelAction.NAME);
		setMenuItemTextAndAccelerator(this.repeatOpen, "repeat.open", RepeatOpenAction.NAME);
		setMenuItemTextAndAccelerator(this.repeatClose, "repeat.close", RepeatCloseAction.NAME);
		setMenuItemTextAndAccelerator(this.repeatAlternative, "repeat.alternative", RepeatAlternativeAction.NAME);
		setMenuItemTextAndAccelerator(this.properties, "composition.properties", ChangeInfoAction.NAME);
	}
	
	public void loadIcons() {
		this.timeSignature.setImage(TuxGuitar.instance().getIconManager().getCompositionTimeSignature());
		this.tempo.setImage(TuxGuitar.instance().getIconManager().getCompositionTempo());
		this.repeatOpen.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatOpen());
		this.repeatClose.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatClose());
		this.repeatAlternative.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatAlternative());
		this.properties.setImage(TuxGuitar.instance().getIconManager().getSongProperties());
	}
}
