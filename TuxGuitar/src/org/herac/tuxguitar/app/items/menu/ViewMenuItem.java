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
import org.herac.tuxguitar.app.action.impl.layout.SetChordDiagramEnabledAction;
import org.herac.tuxguitar.app.action.impl.layout.SetChordNameEnabledAction;
import org.herac.tuxguitar.app.action.impl.layout.SetCompactViewAction;
import org.herac.tuxguitar.app.action.impl.layout.SetLinearLayoutAction;
import org.herac.tuxguitar.app.action.impl.layout.SetMultitrackViewAction;
import org.herac.tuxguitar.app.action.impl.layout.SetPageLayoutAction;
import org.herac.tuxguitar.app.action.impl.layout.SetScoreEnabledAction;
import org.herac.tuxguitar.app.action.impl.layout.SetTablatureEnabledAction;
import org.herac.tuxguitar.app.action.impl.view.ShowFretBoardAction;
import org.herac.tuxguitar.app.action.impl.view.ShowInstrumentsAction;
import org.herac.tuxguitar.app.action.impl.view.ShowMatrixAction;
import org.herac.tuxguitar.app.action.impl.view.ShowPianoAction;
import org.herac.tuxguitar.app.action.impl.view.ShowToolbarsAction;
import org.herac.tuxguitar.app.action.impl.view.ShowTransportAction;
import org.herac.tuxguitar.app.items.MenuItems;
import org.herac.tuxguitar.graphics.control.TGLayout;
import org.herac.tuxguitar.graphics.control.TGLayoutHorizontal;
import org.herac.tuxguitar.graphics.control.TGLayoutVertical;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ViewMenuItem extends MenuItems{
	
	private Menu menu;
	private Menu chordMenu;
	private MenuItem layoutMenuItem;
	private MenuItem showToolbars;
	private MenuItem showInstruments;
	private MenuItem showTransport;
	private MenuItem showFretBoard;
	private MenuItem showPiano;
	private MenuItem showMatrix;
	private MenuItem pageLayout;
	private MenuItem linearLayout;
	private MenuItem multitrack;
	private MenuItem scoreEnabled;
	private MenuItem tablatureEnabled;
	private MenuItem compact;
	
	private MenuItem chordMenuItem;
	private MenuItem chordName;
	private MenuItem chordDiagram;
	
	public ViewMenuItem(Shell shell,Menu parent, int style) {
		this.layoutMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}
	
	public void showItems(){
		//--TOOLBARS--
		this.showToolbars = new MenuItem(this.menu, SWT.CHECK);
		this.showToolbars.addSelectionListener(new TGActionProcessor(ShowToolbarsAction.NAME));
		
		//--INSTRUMENTS--
		this.showInstruments = new MenuItem(this.menu, SWT.CHECK);
		this.showInstruments.addSelectionListener(new TGActionProcessor(ShowInstrumentsAction.NAME));
		
		//--TRANSPORT--
		this.showTransport = new MenuItem(this.menu, SWT.CHECK);
		this.showTransport.addSelectionListener(new TGActionProcessor(ShowTransportAction.NAME));
		
		//--FRETBOARD--
		this.showFretBoard = new MenuItem(this.menu, SWT.CHECK);
		this.showFretBoard.addSelectionListener(new TGActionProcessor(ShowFretBoardAction.NAME));
		
		//--PIANO--
		this.showPiano = new MenuItem(this.menu, SWT.CHECK);
		this.showPiano.addSelectionListener(new TGActionProcessor(ShowPianoAction.NAME));
		
		//--MATRIX--
		this.showMatrix = new MenuItem(this.menu, SWT.CHECK);
		this.showMatrix.addSelectionListener(new TGActionProcessor(ShowMatrixAction.NAME));
		
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--PAGE LAYOUT--
		this.pageLayout = new MenuItem(this.menu, SWT.RADIO);
		this.pageLayout.addSelectionListener(new TGActionProcessor(SetPageLayoutAction.NAME));
		
		//--LINEAR LAYOUT--
		this.linearLayout = new MenuItem(this.menu, SWT.RADIO);
		this.linearLayout.addSelectionListener(new TGActionProcessor(SetLinearLayoutAction.NAME));
		
		//--MULTITRACK--
		this.multitrack = new MenuItem(this.menu, SWT.CHECK);
		this.multitrack.addSelectionListener(new TGActionProcessor(SetMultitrackViewAction.NAME));
		
		//--SCORE
		this.scoreEnabled = new MenuItem(this.menu, SWT.CHECK);
		this.scoreEnabled.addSelectionListener(new TGActionProcessor(SetScoreEnabledAction.NAME));
		
		//--SCORE
		this.tablatureEnabled = new MenuItem(this.menu, SWT.CHECK);
		this.tablatureEnabled.addSelectionListener(new TGActionProcessor(SetTablatureEnabledAction.NAME));
		
		//--COMPACT
		this.compact = new MenuItem(this.menu, SWT.CHECK);
		this.compact.addSelectionListener(new TGActionProcessor(SetCompactViewAction.NAME));
		
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--CHORD STYLE
		this.chordMenuItem = new MenuItem(this.menu,SWT.CASCADE);
		this.chordMenu = new Menu(this.menu.getShell(), SWT.DROP_DOWN);
		
		this.chordName = new MenuItem(this.chordMenu, SWT.CHECK);
		this.chordName.addSelectionListener(new TGActionProcessor(SetChordNameEnabledAction.NAME));
		
		this.chordDiagram = new MenuItem(this.chordMenu, SWT.CHECK);
		this.chordDiagram.addSelectionListener(new TGActionProcessor(SetChordDiagramEnabledAction.NAME));
		
		this.chordMenuItem.setMenu(this.chordMenu);
		this.layoutMenuItem.setMenu(this.menu);
		
		this.loadIcons();
		this.loadProperties();
	}
	
	public void update(){
		TGLayout layout = TuxGuitar.instance().getTablatureEditor().getTablature().getViewLayout();
		int style = layout.getStyle();
		this.showToolbars.setSelection(TuxGuitar.instance().getItemManager().isCoolbarVisible());
		this.showInstruments.setSelection(!TuxGuitar.instance().getChannelManager().isDisposed());
		this.showTransport.setSelection(!TuxGuitar.instance().getTransport().isDisposed());
		this.showFretBoard.setSelection(TuxGuitar.instance().getFretBoardEditor().isVisible());
		this.showPiano.setSelection(!TuxGuitar.instance().getPianoEditor().isDisposed());
		this.showMatrix.setSelection(!TuxGuitar.instance().getMatrixEditor().isDisposed());
		this.pageLayout.setSelection(layout instanceof TGLayoutVertical);
		this.linearLayout.setSelection(layout instanceof TGLayoutHorizontal);
		this.multitrack.setSelection( (style & TGLayout.DISPLAY_MULTITRACK) != 0 );
		this.scoreEnabled.setSelection( (style & TGLayout.DISPLAY_SCORE) != 0 );
		this.tablatureEnabled.setSelection( (style & TGLayout.DISPLAY_TABLATURE) != 0 );
		this.compact.setSelection( (style & TGLayout.DISPLAY_COMPACT) != 0 );
		this.compact.setEnabled((style & TGLayout.DISPLAY_MULTITRACK) == 0 || layout.getSongManager().getSong().countTracks() == 1);
		this.chordName.setSelection( (style & TGLayout.DISPLAY_CHORD_NAME) != 0 );
		this.chordDiagram.setSelection( (style & TGLayout.DISPLAY_CHORD_DIAGRAM) != 0 );
	}
	
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.layoutMenuItem, "view", null);
		setMenuItemTextAndAccelerator(this.showToolbars, "view.show-toolbars", ShowToolbarsAction.NAME);
		setMenuItemTextAndAccelerator(this.showInstruments, "view.show-instruments", ShowInstrumentsAction.NAME);
		setMenuItemTextAndAccelerator(this.showTransport, "view.show-transport", ShowTransportAction.NAME);
		setMenuItemTextAndAccelerator(this.showFretBoard, "view.show-fretboard", ShowFretBoardAction.NAME);
		setMenuItemTextAndAccelerator(this.showPiano, "view.show-piano", ShowPianoAction.NAME);
		setMenuItemTextAndAccelerator(this.showMatrix, "view.show-matrix", ShowMatrixAction.NAME);
		setMenuItemTextAndAccelerator(this.pageLayout, "view.layout.page", SetPageLayoutAction.NAME);
		setMenuItemTextAndAccelerator(this.linearLayout, "view.layout.linear", SetLinearLayoutAction.NAME);
		setMenuItemTextAndAccelerator(this.multitrack, "view.layout.multitrack", SetMultitrackViewAction.NAME);
		setMenuItemTextAndAccelerator(this.scoreEnabled, "view.layout.score-enabled", SetScoreEnabledAction.NAME);
		setMenuItemTextAndAccelerator(this.tablatureEnabled, "view.layout.tablature-enabled", SetTablatureEnabledAction.NAME);
		setMenuItemTextAndAccelerator(this.compact, "view.layout.compact", SetCompactViewAction.NAME);
		setMenuItemTextAndAccelerator(this.chordMenuItem, "view.layout.chord-style", null);
		setMenuItemTextAndAccelerator(this.chordName, "view.layout.chord-name", SetChordNameEnabledAction.NAME);
		setMenuItemTextAndAccelerator(this.chordDiagram, "view.layout.chord-diagram", SetChordDiagramEnabledAction.NAME);
	}
	
	public void loadIcons(){
		//Nothing to do
	}
}
