/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.help;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.help.doc.TGDocumentation;
import org.herac.tuxguitar.app.util.MessageDialog;

/**
 * @author julian
 */
public class ShowDocAction extends Action {

	public static final String NAME = "action.help.doc";

	public ShowDocAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | KEY_BINDING_AVAILABLE );
	}

	@Override
	protected int execute(ActionData actionData){
		try {
			new TGDocumentation().display();
		} catch (Throwable throwable) {
			MessageDialog.errorMessage(throwable);
		}
		return 0;
	}
}
