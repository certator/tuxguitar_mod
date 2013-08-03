/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.marker.AddMarkerAction;
import org.herac.tuxguitar.app.actions.marker.GoFirstMarkerAction;
import org.herac.tuxguitar.app.actions.marker.GoLastMarkerAction;
import org.herac.tuxguitar.app.actions.marker.GoNextMarkerAction;
import org.herac.tuxguitar.app.actions.marker.GoPreviousMarkerAction;
import org.herac.tuxguitar.app.actions.marker.ListMarkersAction;
import org.herac.tuxguitar.app.items.MenuItems;

/**
 * @author julian
 */
public class MarkerMenuItem extends MenuItems{
	private MenuItem markerMenuItem;
	private Menu menu;
	private MenuItem add;
	private MenuItem list;
	private MenuItem first;
	private MenuItem last;
	private MenuItem next;
	private MenuItem previous;

	public MarkerMenuItem(Shell shell,Menu parent, int style) {
		this.markerMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}

	@Override
	public void showItems(){
		//--ADD--
		this.add = new MenuItem(this.menu, SWT.PUSH);
		this.add.addSelectionListener(TuxGuitar.instance().getAction(AddMarkerAction.NAME));

		//--LIST--
		this.list = new MenuItem(this.menu, SWT.PUSH);
		this.list.addSelectionListener(TuxGuitar.instance().getAction(ListMarkersAction.NAME));

		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);

		//--FIRST--
		this.first = new MenuItem(this.menu, SWT.PUSH);
		this.first.addSelectionListener(TuxGuitar.instance().getAction(GoFirstMarkerAction.NAME));

		//--PREVIOUS--
		this.previous = new MenuItem(this.menu, SWT.PUSH);
		this.previous.addSelectionListener(TuxGuitar.instance().getAction(GoPreviousMarkerAction.NAME));

		//--PREVIOUS--
		this.next = new MenuItem(this.menu, SWT.PUSH);
		this.next.addSelectionListener(TuxGuitar.instance().getAction(GoNextMarkerAction.NAME));

		//--LAST--
		this.last = new MenuItem(this.menu, SWT.PUSH);
		this.last.addSelectionListener(TuxGuitar.instance().getAction(GoLastMarkerAction.NAME));

		this.markerMenuItem.setMenu(this.menu);

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void update(){
		//Nothing to do
	}

	@Override
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.markerMenuItem, "marker", null);
		setMenuItemTextAndAccelerator(this.add, "marker.add", AddMarkerAction.NAME);
		setMenuItemTextAndAccelerator(this.list, "marker.list", ListMarkersAction.NAME);
		setMenuItemTextAndAccelerator(this.first, "marker.first", GoFirstMarkerAction.NAME);
		setMenuItemTextAndAccelerator(this.last, "marker.last", GoLastMarkerAction.NAME);
		setMenuItemTextAndAccelerator(this.previous, "marker.previous", GoPreviousMarkerAction.NAME);
		setMenuItemTextAndAccelerator(this.next, "marker.next", GoNextMarkerAction.NAME);
	}

	public void loadIcons(){
		this.add.setImage(TuxGuitar.instance().getIconManager().getMarkerAdd());
		this.list.setImage(TuxGuitar.instance().getIconManager().getMarkerList());
		this.first.setImage(TuxGuitar.instance().getIconManager().getMarkerFirst());
		this.previous.setImage(TuxGuitar.instance().getIconManager().getMarkerPrevious());
		this.next.setImage(TuxGuitar.instance().getIconManager().getMarkerNext());
		this.last.setImage(TuxGuitar.instance().getIconManager().getMarkerLast());
	}
}
