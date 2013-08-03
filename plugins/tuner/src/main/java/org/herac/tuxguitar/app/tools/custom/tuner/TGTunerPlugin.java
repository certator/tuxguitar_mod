package org.herac.tuxguitar.app.tools.custom.tuner;

import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.song.models.TGString;

/**
 * @author Nikola Kolarovic <nikola.kolarovic at gmail.com>
 *
 */
public class TGTunerPlugin extends org.herac.tuxguitar.app.system.plugins.base.TGToolItemPlugin {


	@Override
	protected void doAction() {
		List<TGString> strings = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getTrack().getStrings();
		Iterator<TGString> it = strings.iterator();
		int[] tuning = new int[strings.size()];
		int i=0;
		while (it.hasNext()) {
			TGString current = it.next();
			tuning[i] = current.getValue();
			i++;
		}
		TGTunerDialog dialog = new TGTunerDialog(tuning);
		dialog.show();

	}

	@Override
	protected String getItemName() {
		return "Guitar Tuner";
	}

	@Override
	public String getName() {
		return "GuitarTuner";
	}

	@Override
	public String getAuthor() {
		return "Nikola Kolarovic";
	}

	@Override
	public String getDescription() {
		return "Visual tuner that analyses the most dominant frequency from your microphone" +
			   " and displays it on the tuner scale.";
	}

	@Override
	public String getVersion() {
		return "0.01b";
	}


}
