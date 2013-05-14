/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.layout;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.graphics.control.TGLayout;

/**
 * @author julian
 */
public class SetScoreEnabledAction extends Action{

	public static final String NAME = "action.view.layout-set-score-enabled";

	public SetScoreEnabledAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TGLayout layout = getEditor().getTablature().getViewLayout();
		layout.setStyle( ( layout.getStyle() ^ TGLayout.DISPLAY_SCORE ) );
		if((layout.getStyle() & TGLayout.DISPLAY_TABLATURE) == 0 && (layout.getStyle() & TGLayout.DISPLAY_SCORE) == 0 ){
			layout.setStyle( ( layout.getStyle() ^ TGLayout.DISPLAY_TABLATURE) );
		}
		updateTablature();
		return 0;
	}
}
