package org.herac.tuxguitar.app.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.herac.tuxguitar.app.util.TGMusicKeyUtils;
import org.herac.tuxguitar.song.models.TGString;

public class StringLabel extends CLabel {

	private final static int ICON_SIZE = 10;

	/**
	 * Icon used to display the string
	 */
	private Image icon;

	private int min = 40;
	private int max = 70;
	private int value;

	public StringLabel(Composite parent) {
		super(parent, 0);
	}

	@Override
	protected void checkSubclass() {
		// Allow to inherit SWT components... I would like to cook OOP thanks
	}

	public void setString(TGString string) {
		String text = TGMusicKeyUtils.getNoteName(string.getValue());
		setText(text);
		value = string.getValue();
		updateIcon();
	}

	public void setMinMax(int min, int max) {
		this.min = min;
		this.max = max;
	}

	private void updateIcon() {
		if (value == 0) {
			return;
		}

		double coef = (value - min) / (double)(max - min);
		if (coef < 0) coef = 0;
		if (coef > 1) coef = 1;
		coef = 1-coef;
		coef = 0.5 + coef * 0.5;

		if (icon == null) {
			icon = new Image(getDisplay(), ICON_SIZE,ICON_SIZE);
		}

		GC gc = new GC(icon);
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(0, 0, ICON_SIZE, ICON_SIZE);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		gc.setAlpha(255);
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		int radius = (int) (ICON_SIZE * coef * 0.5);
		gc.fillOval(ICON_SIZE/2-radius, ICON_SIZE/2-radius, radius*2, radius*2);
		gc.dispose();

		setImage(icon);
	}

	@Override
	public void dispose() {
		if(icon != null && !icon.isDisposed()){
			icon.dispose();
		}
		super.dispose();
	}
}
