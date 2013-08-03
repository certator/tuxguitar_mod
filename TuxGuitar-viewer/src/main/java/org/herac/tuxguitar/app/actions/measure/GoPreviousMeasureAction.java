/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.actions.measure;

import java.awt.AWTEvent;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.graphics.control.TGTrackImpl;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GoPreviousMeasureAction extends Action{
	public static final String NAME = "action.measure.go-previous";
	
	public GoPreviousMeasureAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}
	
	protected int execute(AWTEvent e){
		if(TuxGuitar.instance().getPlayer().isRunning()){
			TuxGuitar.instance().getTransport().gotoPrevious();
		}
		else{
			Caret caret = getEditor().getTablature().getCaret();
			TGTrackImpl track = caret.getTrack();
			TGMeasureImpl measure = (TGMeasureImpl)getSongManager().getTrackManager().getPrevMeasure(caret.getMeasure());
			if(track != null && measure != null){
				caret.update(track.getNumber(),measure.getStart(),caret.getSelectedString().getNumber());
			}
		}
		return 0;
	}
}
