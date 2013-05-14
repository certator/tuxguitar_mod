/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.transport;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;

/**
 * @author julian
 */
public class TransportStopAction extends Action{

	public static final String NAME = "action.transport.stop";

	public TransportStopAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		TuxGuitar.instance().getTransport().stop();
		return 0;
	}
}
