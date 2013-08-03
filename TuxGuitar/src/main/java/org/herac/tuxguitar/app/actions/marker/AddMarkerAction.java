/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.marker;

import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.marker.MarkerEditor;
import org.herac.tuxguitar.app.marker.MarkerList;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.song.models.TGMarker;

/**
 * @author julian
 */
public class AddMarkerAction extends Action{

	public static final String NAME = "action.marker.add";

	public AddMarkerAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		if(new MarkerEditor(getMarker()).open(getEditor().getTablature().getShell())){
			MarkerList.instance().update(true);
		}
		return 0;
	}

	private TGMarker getMarker(){
		TGMeasureImpl measure = getEditor().getTablature().getCaret().getMeasure();
		if (measure != null) {
			TGMarker marker = getSongManager().getMarker(measure.getNumber());
			if(marker == null){
				marker = getSongManager().getFactory().newMarker();
				marker.setMeasure(measure.getNumber());
			}
			return marker;
		}
		return null;
	}
}
