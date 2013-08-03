package org.herac.tuxguitar.app.tracktable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.editors.TGColorImpl;
import org.herac.tuxguitar.app.editors.TGPainterImpl;
import org.herac.tuxguitar.graphics.TGColor;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGResourceFactory;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

class TrackCanvas extends Composite implements PaintListener{

	/**
	 * Coefficient to compute the width of one measure according to the height of the component.
	 */
	private float widthMeasureCoef = 1.0f;

	/**
	 * Displayed track
	 */
	private TGTrack track;

	/**
	 * Horizontal scroll. Skip pixels from the left side of the component.
	 */
	private int hScroll;

	public TrackCanvas(Composite parent, TGTrack track){
		super(parent, SWT.DOUBLE_BUFFERED);
		this.track = track;
		this.addPaintListener(this);
	}

	@Override
	public void paintControl(PaintEvent e) {
		TGPainterImpl painter = new TGPainterImpl(e.gc);
		paintTrack(painter);
	}

	protected void paintTrack(TGPainterImpl painter){
		if (this.track == null)
			return;

		if(!TuxGuitar.instance().isLocked()){
			TuxGuitar.instance().lock();

			int x = -this.hScroll;
			int y = 0;
			int height = this.getSize().y;
			int width = painter.getGC().getDevice().getBounds().width;
			int measureWidth = (int)(height * this.widthMeasureCoef);
			boolean playing = TuxGuitar.instance().getPlayer().isRunning();

			painter.setBackground(new TGColorImpl(painter.getGC().getDevice().getSystemColor(SWT.COLOR_GRAY)));
			painter.initPath(TGPainter.PATH_FILL);
			painter.setAntialias(false);
			painter.addRectangle(0,y,width,height);
			painter.closePath();

			TGColor trackColor = painter.createColor(this.track.getColor().getR(),this.track.getColor().getG(),this.track.getColor().getB());
			TGColor trackColor2 = createAlternativeColor(painter, this.track.getColor());
			TGColor selectedColor = createSelectionColor(painter, this.track.getColor());

			boolean normal = true;
			boolean hasChange = false;
			int count = this.track.countMeasures();
			for(int j = 0;j < count;j++){
				TGMeasureImpl measure = (TGMeasureImpl)this.track.getMeasure(j);

				// swap the color to show a structural change
				if (measure.isRepeatOpen() && !hasChange) {
					normal = !normal;
				}

				TGColor color = normal ? trackColor : trackColor2;
				painter.setBackground(color);
				painter.setForeground(color);

				if(isRestMeasure(measure)){
					painter.initPath();
					painter.setAntialias(false);
					painter.addRectangle(x,y,measureWidth - 2,height - 1);
					painter.closePath();
				}else{
					painter.initPath(TGPainter.PATH_FILL);
					painter.setAntialias(false);
					painter.addRectangle(x,y,measureWidth - 1,height );
					painter.closePath();
				}

				boolean hasCaret = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getMeasure().equals(measure);
				if((playing && measure.isPlaying(TuxGuitar.instance().getTablatureEditor().getTablature().getViewLayout())) || (!playing && hasCaret)){
					painter.setBackground(selectedColor);
					painter.initPath(TGPainter.PATH_FILL);
					painter.setAntialias(false);
					painter.addRectangle(x + 4,y + 4,measureWidth - 9,height - 8);
					painter.closePath();
				}
				x += measureWidth;

				// swap the color to show a structural change
				if (measure.hasDoubleBar() || measure.getRepeatClose() != 0) {
					hasChange = true;
					normal = !normal;
				} else {
					hasChange = false;
				}
			}
			trackColor.dispose();
			trackColor2.dispose();
			selectedColor.dispose();

			TuxGuitar.instance().unlock();
		}
	}

	/**
	 * Create selected color according to dark/light of the trackColor
	 */
	private TGColor createSelectionColor(TGResourceFactory factory,
			org.herac.tuxguitar.song.models.TGColor color) {
		if (color.getR() + color.getG() + color.getB() < (255 * 3 / 2)) {
			return factory.createColor(255,255,255);
		} else {
			return factory.createColor(0,0,0);
		}
	}

	/**
	 * Create an alternative color to highlight structural changes of the tracks
	 */
	private TGColor createAlternativeColor(TGResourceFactory factory,
			org.herac.tuxguitar.song.models.TGColor color) {
		RGB rgb = new RGB(color.getR(), color.getG(), color.getB());
		float[] hsv = rgb.getHSB();
		final float delta = 0.15f;
		if (hsv[2] > 0.5) {
			hsv[2] -= delta;
		} else {
			hsv[2] += delta;
		}

		rgb = new RGB(hsv[0], hsv[1], hsv[2]);
		return factory.createColor(rgb.red, rgb.green, rgb.blue);
	}

	private boolean isRestMeasure(TGMeasureImpl measure){
		int beatCount = measure.countBeats();
		for(int i = 0; i < beatCount; i++){
			if( !measure.getBeat(i).isRestBeat() ){
				return false;
			}
		}
		return true;
	}

	public void setTrack(TGTrack track) {
		this.track = track;
		redraw();
	}

	/**
	 * Set the location of the client horizontal scroll
	 */
	public void setHScroll(int scroll) {
		this.hScroll = scroll;
		redraw();
	}

	/**
	 * Size taken by the draw of the full track inside the component
	 */
	public int getRealWidth() {
		if (this.track == null)
			return 0;
		int width = this.track.countMeasures() * (int)(this.getSize().y * this.widthMeasureCoef);
		return width;
	}

	public float getWidthMeasureCoef() {
		return this.widthMeasureCoef;
	}

	public void setWidthMeasureCoef(float widthMeasureCoef) {
		// clamp
		if (widthMeasureCoef > 1) {
			widthMeasureCoef = 1;
		} else if (this.getSize().y != 0){
			int measureWidth = (int)(this.getSize().y * widthMeasureCoef);
			if (measureWidth < 12){
				widthMeasureCoef = 12.0f / this.getSize().y;
			}
		}
		if (this.widthMeasureCoef != widthMeasureCoef) {
			this.widthMeasureCoef = widthMeasureCoef;
			redraw();
		}
	}

	/**
	 * Return the measure of the displayed track at the requested position.
	 * @param x Position of the visible component
	 * @return
	 */
	public TGMeasure getMeasureAtPosition(int x) {
		if (this.track == null) {
			return null;
		}
		int measureWidth = (int)(this.getSize().y * this.widthMeasureCoef);
		int index = (x + this.hScroll) / measureWidth;
		if (index < 0 || index >= this.track.countMeasures()) {
			return null;
		}
		return this.track.getMeasure(index);
	}

}
