/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.items.MenuItems;

/**
 * @author julian
 */
public class TrackMenuItem extends MenuItems{
	private MenuItem trackMenuItem;
	private TrackMenu menu;

	public TrackMenuItem(Shell shell,Menu parent, int style) {
		this.trackMenuItem = new MenuItem(parent, style);
		this.menu = new TrackMenu(shell, SWT.DROP_DOWN);
	}

	@Override
	public void showItems(){
		this.menu.showItems();
		this.trackMenuItem.setMenu(this.menu.getMenu());
		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void loadProperties(){
		this.setMenuItemTextAndAccelerator(this.trackMenuItem, "track", null);
		this.menu.loadProperties();
	}

	@Override
	public void update(){
		this.menu.update();
	}

	public void loadIcons(){
		//Nothing to do
	}
}
