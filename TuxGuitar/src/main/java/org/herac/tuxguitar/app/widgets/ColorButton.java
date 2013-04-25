package org.herac.tuxguitar.app.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.herac.tuxguitar.app.TuxGuitar;

/**
 * Button to display and select a color. 
 * 
 * The code override the SelectionListeners to allow to catch changes
 * of color from UI. Maybe we should use an own event for that.
 * 
 * @author bayo
 */
public class ColorButton extends Button {

	private final static int ICON_SIZE = 16;

	/**
	 * Icon used to display the current color
	 */
	private Image icon;

	/**
	 * Current selected color
	 */
	private RGB color;

	private final List<SelectionListener> selectionListeners;

	public ColorButton(Composite parent) {
		super(parent, SWT.PUSH);
		this.selectionListeners = new ArrayList<SelectionListener>();
		super.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				ColorDialog dialog = new ColorDialog(ColorButton.this.getParent().getShell());
				dialog.setRGB(getColor());
				dialog.setText(TuxGuitar.getProperty("choose-color"));
				RGB rgb = dialog.open();
				if (rgb != null) {
					setColor(rgb);
					for (SelectionListener listener: selectionListeners) {
						listener.widgetSelected(event);
					}
				}
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Allow to inherit SWT components... I would like to cook OOP thanks
	}

	@Override
	public void addSelectionListener(SelectionListener listener) {
		this.selectionListeners.add(listener);
	}

	@Override
	public void removeSelectionListener(SelectionListener listener) {
		this.selectionListeners.remove(listener);
	}

	public void setColor(RGB color){
		if (color == null) {
			throw new IllegalArgumentException("Initialized color expected");
		}
		if (color.equals(this.color)) {
			return;
		}

		if (icon == null) {
			icon = new Image(TuxGuitar.instance().getDisplay(), ICON_SIZE, ICON_SIZE);
		}

		this.color = color;
		Color c = new Color(this.getDisplay(), color.red, color.green, color.blue);
		GC gc = new GC(icon);
		gc.setBackground(c);
		gc.fillRectangle(0, 0, ICON_SIZE, ICON_SIZE);
		gc.dispose();
		c.dispose();

		setImage(icon);
	}

	public RGB getColor() {
		return color;
	}

	@Override
	public void dispose(){
		if(icon != null && !icon.isDisposed()){
			icon.dispose();
		}
		super.dispose();
	}

}
