/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.track.AddTrackAction;
import org.herac.tuxguitar.app.actions.track.RemoveTrackAction;
import org.herac.tuxguitar.app.items.ToolItems;

/**
 * @author julian
 */
public class TrackToolItems extends ToolItems{
	public static final String NAME = "track.items";

	private ToolItem add;
	private ToolItem remove;

	public TrackToolItems(){
		super(NAME);
	}

	@Override
	public void showItems(ToolBar toolBar){
		this.add = new ToolItem(toolBar, SWT.PUSH);
		this.add.addSelectionListener(TuxGuitar.instance().getAction(AddTrackAction.NAME));

		this.remove = new ToolItem(toolBar, SWT.PUSH);
		this.remove.addSelectionListener(TuxGuitar.instance().getAction(RemoveTrackAction.NAME));

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void loadProperties(){
		this.add.setToolTipText(TuxGuitar.getProperty("track.add"));
		this.remove.setToolTipText(TuxGuitar.getProperty("track.remove"));
	}

	public void loadIcons(){
		this.add.setImage(TuxGuitar.instance().getIconManager().getTrackAdd());
		this.remove.setImage(TuxGuitar.instance().getIconManager().getTrackRemove());
	}

	@Override
	public void update(){
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.add.setEnabled(!running);
		this.remove.setEnabled(!running);
	}
}
