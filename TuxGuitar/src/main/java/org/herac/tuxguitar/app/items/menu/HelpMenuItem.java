/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.help.ShowAboutAction;
import org.herac.tuxguitar.app.actions.help.ShowDocAction;
import org.herac.tuxguitar.app.items.MenuItems;

/**
 * @author julian
 */
public class HelpMenuItem extends MenuItems{
	private MenuItem helpMenuItem;
	private Menu menu;
	private MenuItem doc;
	private MenuItem about;

	public HelpMenuItem(Shell shell,Menu parent, int style) {
		this.helpMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}

	@Override
	public void showItems(){
		//--Doc
		this.doc = new MenuItem(this.menu, SWT.PUSH);
		this.doc.addSelectionListener(TuxGuitar.instance().getAction(ShowDocAction.NAME));

		//--ABOUT
		this.about = new MenuItem(this.menu, SWT.PUSH);
		this.about.addSelectionListener(TuxGuitar.instance().getAction(ShowAboutAction.NAME));

		this.helpMenuItem.setMenu(this.menu);

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.helpMenuItem, "help", null);
		setMenuItemTextAndAccelerator(this.doc, "help.doc", ShowDocAction.NAME);
		setMenuItemTextAndAccelerator(this.about, "help.about", ShowAboutAction.NAME);
	}

	public void loadIcons(){
		//Nothing to do
	}

	@Override
	public void update(){
		//Nothing to do
	}
}
