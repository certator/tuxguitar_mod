package org.herac.tuxguitar.app.actions;

import java.util.HashMap;
import java.util.Map;

public class ActionData {
	
	private Map<String, Object> data;
	
	public ActionData(){
		this.data = new HashMap<String, Object>();
	}
	
	public void put(String key, Object value){
		this.data.put(key, value);
	}
	
	public Object get(String key){
		return this.data.get(key);
	}
}
