/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.help;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.help.about.AboutDialog;

/**
 * @author julian
 */
public class ShowAboutAction extends Action {

	public static final String NAME = "action.help.about";

	protected Canvas imageCanvas;
	protected Image tabImage;

	public ShowAboutAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK);
	}

	@Override
	protected int execute(ActionData actionData){
		new AboutDialog().open(TuxGuitar.instance().getShell());
		return 0;
	}
}
