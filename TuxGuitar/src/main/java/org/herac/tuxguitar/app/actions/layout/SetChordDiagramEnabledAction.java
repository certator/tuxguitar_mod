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
public class SetChordDiagramEnabledAction extends Action{

	public static final String NAME = "action.view.layout-set-chord-diagram-enabled";

	public SetChordDiagramEnabledAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TGLayout layout = getEditor().getTablature().getViewLayout();
		layout.setStyle( ( layout.getStyle() ^ TGLayout.DISPLAY_CHORD_DIAGRAM) );
		updateTablature();
		return 0;
	}
}
