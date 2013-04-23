package org.herac.tuxguitar.app.system.keybindings;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.system.keybindings.xml.KeyBindingReader;
import org.herac.tuxguitar.app.system.keybindings.xml.KeyBindingWriter;
import org.herac.tuxguitar.app.util.TGFileUtils;

public class KeyBindingActionManager {
	
	private final List<KeyBindingAction> keyBindingsActions;
	private KeyBindingListener listener;
	
	public KeyBindingActionManager(){
		this.keyBindingsActions = new ArrayList<KeyBindingAction>();
		this.init();
	}
	
	public void init(){
		List<KeyBindingAction> enabled = KeyBindingReader.getKeyBindings(getUserFileName());
		this.keyBindingsActions.addAll( (enabled != null ? enabled : KeyBindingActionDefaults.getDefaultKeyBindings()) );
		this.listener = new KeyBindingListener(this);
	}
	
	private String getUserFileName(){
		return TGFileUtils.PATH_USER_CONFIG + File.separator + "shortcuts.xml";
	}
	
	public Action getActionForKeyBinding(KeyBinding kb){
		Iterator<KeyBindingAction> it = this.keyBindingsActions.iterator();
		while(it.hasNext()){
			KeyBindingAction keyBindingAction = it.next();
			if(keyBindingAction.getKeyBinding() != null && kb.isSameAs( keyBindingAction.getKeyBinding() )){
				return TuxGuitar.instance().getAction(keyBindingAction.getAction());
			}
		}
		return null;
	}
	
	public KeyBinding getKeyBindingForAction(String action){
		Iterator<KeyBindingAction> it = this.keyBindingsActions.iterator();
		while(it.hasNext()){
			KeyBindingAction keyBindingAction = it.next();
			if(action.equals( keyBindingAction.getAction() )){
				return keyBindingAction.getKeyBinding();
			}
		}
		return null;
	}
	
	public void reset(List<KeyBindingAction> keyBindings){
		this.keyBindingsActions.clear();
		this.keyBindingsActions.addAll(keyBindings);
	}
	
	public List<KeyBindingAction> getKeyBindingActions(){
		return this.keyBindingsActions;
	}
	
	public void saveKeyBindings(){
		KeyBindingWriter.setBindings(getKeyBindingActions(),getUserFileName());
	}
	
	public void appendListenersTo(Control control){
		control.addKeyListener(this.listener);
	}
	
	public void processKeyBinding(KeyBinding kb){
		Action action = getActionForKeyBinding(kb);
		if (action != null){
			action.process(new ActionData());
		}
	}
}
