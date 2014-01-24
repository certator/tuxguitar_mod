/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.actions.layout;

import java.awt.AWTEvent;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.graphics.control.TGLayout;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SetScoreEnabledAction extends Action{
	public static final String NAME = "action.view.layout-set-score-enabled";
	
	public SetScoreEnabledAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}
	
	protected int execute(AWTEvent e){
		TGLayout layout = getEditor().getTablature().getViewLayout();
		layout.setStyle( ( layout.getStyle() ^ TGLayout.DISPLAY_SCORE ) );
		if((layout.getStyle() & TGLayout.DISPLAY_TABLATURE) == 0 && (layout.getStyle() & TGLayout.DISPLAY_SCORE) == 0 ){
			layout.setStyle( ( layout.getStyle() ^ TGLayout.DISPLAY_TABLATURE) );
		}
		updateTablature();
		return 0;
	}
}
