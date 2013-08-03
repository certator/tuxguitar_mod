/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.view.ShowFretBoardAction;
import org.herac.tuxguitar.app.actions.view.ShowInstrumentsAction;
import org.herac.tuxguitar.app.actions.view.ShowTransportAction;
import org.herac.tuxguitar.app.items.ToolItems;

/**
 * @author julian
 */
public class ViewToolItems extends ToolItems{
	public static final String NAME = "view.items";

	private ToolItem showFretBoard;
	private ToolItem showInstruments;
	private ToolItem showTransport;

	public ViewToolItems(){
		super(NAME);
	}

	@Override
	public void showItems(ToolBar toolBar){
		//--FRETBOARD--
		this.showFretBoard = new ToolItem(toolBar, SWT.CHECK);
		this.showFretBoard.addSelectionListener(TuxGuitar.instance().getAction(ShowFretBoardAction.NAME));

		//--INSTRUMENTS--
		this.showInstruments = new ToolItem(toolBar, SWT.CHECK);
		this.showInstruments.addSelectionListener(TuxGuitar.instance().getAction(ShowInstrumentsAction.NAME));

		//--TRANSPORT--
		this.showTransport = new ToolItem(toolBar, SWT.CHECK);
		this.showTransport.addSelectionListener(TuxGuitar.instance().getAction(ShowTransportAction.NAME));

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void update(){
		this.showFretBoard.setSelection(TuxGuitar.instance().getFretBoardEditor().isVisible());
		this.showInstruments.setSelection(!TuxGuitar.instance().getChannelManager().isDisposed());
		this.showTransport.setSelection(!TuxGuitar.instance().getTransport().isDisposed());
	}

	@Override
	public void loadProperties(){
		this.showFretBoard.setToolTipText(TuxGuitar.getProperty("view.show-fretboard"));
		this.showInstruments.setToolTipText(TuxGuitar.getProperty("view.show-instruments"));
		this.showTransport.setToolTipText(TuxGuitar.getProperty("view.show-transport"));
	}

	public void loadIcons(){
		this.showFretBoard.setImage(TuxGuitar.instance().getIconManager().getFretboard());
		this.showInstruments.setImage(TuxGuitar.instance().getIconManager().getInstruments());
		this.showTransport.setImage(TuxGuitar.instance().getIconManager().getTransport());
	}
}
