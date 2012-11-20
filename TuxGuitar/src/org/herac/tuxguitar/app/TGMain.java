package org.herac.tuxguitar.app;

import java.util.Map.Entry;

public class TGMain {
	
	public static void main(String[] args){
		
		
//		for (Entry<Object, Object> entry : System.getProperties().entrySet())
//		{
//			
//			//System.out.println("property: " + entry.getKey().toString() + " = " + entry.getValue().toString());
//		}
		
		TuxGuitar.instance().displayGUI(args);
		System.exit(0);
	}
	
}
