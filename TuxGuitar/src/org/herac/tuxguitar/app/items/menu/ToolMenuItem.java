/*
 * Created on 02-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.items.menu;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.action.TGActionProcessor;
import org.herac.tuxguitar.app.action.impl.settings.EditConfigAction;
import org.herac.tuxguitar.app.action.impl.settings.EditKeyBindingsAction;
import org.herac.tuxguitar.app.action.impl.settings.EditPluginsAction;
import org.herac.tuxguitar.app.action.impl.tools.ScaleAction;
import org.herac.tuxguitar.app.action.impl.tools.TGBrowserAction;
import org.herac.tuxguitar.app.action.impl.tools.TransposeAction;
import org.herac.tuxguitar.app.items.MenuItems;
import org.herac.tuxguitar.app.tools.custom.TGCustomTool;
import org.herac.tuxguitar.app.tools.custom.TGCustomToolManager;

/**
 * @author julian
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class ToolMenuItem extends MenuItems {
	private MenuItem settingsMenuItem;
	private Menu menu;
	private MenuItem scale;
	private MenuItem browser;
	private MenuItem transpose;
	private MenuItem plugins;
	private MenuItem config;
	private MenuItem keyBindings;
	
	public ToolMenuItem(Shell shell,Menu parent, int style) {
		this.settingsMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}
	
	public void showItems(){
		this.transpose = new MenuItem(this.menu, SWT.PUSH);
		this.transpose.addSelectionListener(new TGActionProcessor(TransposeAction.NAME));
		
		this.scale = new MenuItem(this.menu, SWT.PUSH);
		this.scale.addSelectionListener(new TGActionProcessor(ScaleAction.NAME));
		
		this.browser = new MenuItem(this.menu, SWT.PUSH);
		this.browser.addSelectionListener(new TGActionProcessor(TGBrowserAction.NAME));
		
		Iterator it = TGCustomToolManager.instance().getCustomTools();
		while(it.hasNext()){
			TGCustomTool tool = (TGCustomTool)it.next();
			MenuItem menuItem = new MenuItem(this.menu, SWT.PUSH);
			menuItem.setText(tool.getName());
			menuItem.addSelectionListener(new TGActionProcessor(tool.getAction()));
		}
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--PLUGINS--
		this.plugins = new MenuItem(this.menu, SWT.PUSH);
		this.plugins.addSelectionListener(new TGActionProcessor(EditPluginsAction.NAME));
		
		//--KEY BINDINGS--
		this.keyBindings = new MenuItem(this.menu, SWT.PUSH);
		this.keyBindings.addSelectionListener(new TGActionProcessor(EditKeyBindingsAction.NAME));
		
		//--CONFIG--
		this.config = new MenuItem(this.menu, SWT.PUSH);
		this.config.addSelectionListener(new TGActionProcessor(EditConfigAction.NAME));
		
		this.settingsMenuItem.setMenu(this.menu);
		
		this.loadIcons();
		this.loadProperties();
	}
	
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.settingsMenuItem, "tools", null);
		setMenuItemTextAndAccelerator(this.transpose, "tools.transpose", TransposeAction.NAME);
		setMenuItemTextAndAccelerator(this.scale, "tools.scale", ScaleAction.NAME);
		setMenuItemTextAndAccelerator(this.browser, "tools.browser", TGBrowserAction.NAME);
		setMenuItemTextAndAccelerator(this.plugins, "tools.plugins", EditPluginsAction.NAME);
		setMenuItemTextAndAccelerator(this.keyBindings, "tools.shortcuts", EditKeyBindingsAction.NAME);
		setMenuItemTextAndAccelerator(this.config, "tools.settings", EditConfigAction.NAME);
	}
	
	public void loadIcons(){
		//Nothing to do
	}
	
	public void update(){
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.transpose.setEnabled( !running );
	}
}