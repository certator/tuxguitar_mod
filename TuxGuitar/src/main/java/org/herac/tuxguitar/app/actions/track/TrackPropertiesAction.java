/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.actions.track;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.util.DialogUtils;
import org.herac.tuxguitar.app.widgets.TrackPropertiesDialog;
import org.herac.tuxguitar.graphics.control.TGTrackImpl;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class TrackPropertiesAction extends Action {

	public static final String NAME = "action.track.properties";

	public TrackPropertiesAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		showDialog(getEditor().getTablature().getShell());
		return 0;
	}

	private void showDialog(Shell shell) {
		TGTrackImpl track = getEditor().getTablature().getCaret().getTrack();
		if (track != null) {
			Shell dialog = new TrackPropertiesDialog(shell, track);
			DialogUtils.openDialog(dialog, DialogUtils.OPEN_STYLE_CENTER | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);
		}
	}
}