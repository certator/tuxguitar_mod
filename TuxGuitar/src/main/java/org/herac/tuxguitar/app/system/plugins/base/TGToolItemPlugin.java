package org.herac.tuxguitar.app.system.plugins.base;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.tools.custom.TGCustomTool;
import org.herac.tuxguitar.app.tools.custom.TGCustomToolManager;

public abstract class TGToolItemPlugin extends TGPluginAdapter{

	private boolean loaded;
	private TGCustomTool tool;
	private TGCustomToolAction toolAction;

	protected abstract void doAction();

	protected abstract String getItemName() throws TGPluginException ;

	@Override
	public void init() throws TGPluginException {
		String name = getItemName();
		this.tool = new TGCustomTool(name,name);
		this.toolAction = new TGCustomToolAction(this.tool.getName());
	}

	@Override
	public void close() throws TGPluginException {
		this.removePlugin();
	}

	@Override
	public void setEnabled(boolean enabled) throws TGPluginException {
		if(enabled){
			addPlugin();
		}else{
			removePlugin();
		}
	}

	protected void addPlugin() throws TGPluginException {
		if(!this.loaded){
			TuxGuitar.instance().getActionManager().addAction(this.toolAction);
			TGCustomToolManager.instance().addCustomTool(this.tool);
			TuxGuitar.instance().getItemManager().createMenu();
			this.loaded = true;
		}
	}

	protected void removePlugin() throws TGPluginException {
		if(this.loaded){
			TGCustomToolManager.instance().removeCustomTool(this.tool);
			TuxGuitar.instance().getActionManager().removeAction(this.tool.getAction());
			TuxGuitar.instance().getItemManager().createMenu();
			this.loaded = false;
		}
	}

	protected class TGCustomToolAction extends Action{

		public TGCustomToolAction(String name) {
			super(name, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
		}

		@Override
		protected int execute(ActionData actionData) {
			doAction();
			return 0;
		}
	}
}