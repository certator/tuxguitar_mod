/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.composition.ChangeInfoAction;
import org.herac.tuxguitar.app.items.ToolItems;

/**
 * @author julian
 */
public class PropertiesToolItems extends ToolItems{
	public static final String NAME = "property.items";

	private ToolItem info;

	public PropertiesToolItems(){
		super(NAME);
	}

	@Override
	public void showItems(ToolBar toolBar){
		this.info = new ToolItem(toolBar, SWT.PUSH);
		this.info.addSelectionListener(TuxGuitar.instance().getAction(ChangeInfoAction.NAME));

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void loadProperties(){
		this.info.setToolTipText(TuxGuitar.getProperty("composition.properties"));
	}

	public void loadIcons(){
		this.info.setImage(TuxGuitar.instance().getIconManager().getSongProperties());
	}

	@Override
	public void update(){
		//Nothing to do
	}
}
