package org.herac.tuxguitar.app.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.RGB;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.editors.TGColorImpl;
import org.herac.tuxguitar.app.editors.TGPainterImpl;
import org.herac.tuxguitar.graphics.TGColor;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGResourceFactory;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.song.models.TGTrack;

public class TGTableCanvasPainter implements PaintListener{

	private final TGTableViewer viewer;
	private final TGTrack track;

	public TGTableCanvasPainter(TGTableViewer viewer,TGTrack track){
		this.viewer = viewer;
		this.track = track;
	}

	@Override
	public void paintControl(PaintEvent e) {
		TGPainterImpl painter = new TGPainterImpl(e.gc);
		paintTrack(painter);
	}

	protected void paintTrack(TGPainterImpl painter){
		if(!TuxGuitar.instance().isLocked()){
			TuxGuitar.instance().lock();

			int x = -this.viewer.getHScrollSelection();
			int y = 0;
			int size = this.viewer.getTable().getRowHeight();
			int width = painter.getGC().getDevice().getBounds().width;
			boolean playing = TuxGuitar.instance().getPlayer().isRunning();

			painter.setBackground(new TGColorImpl(painter.getGC().getDevice().getSystemColor(SWT.COLOR_GRAY)));
			painter.initPath(TGPainter.PATH_FILL);
			painter.setAntialias(false);
			painter.addRectangle(0,y,width,size);
			painter.closePath();

			TGColor trackColor = painter.createColor(this.track.getColor().getR(),this.track.getColor().getG(),this.track.getColor().getB());
			TGColor trackColor2 = createAlternativeColor(painter, this.track.getColor());
			TGColor selectedColor = createSelectionColor(painter, this.track.getColor());

			boolean normal = true;
			int count = this.track.countMeasures();
			for(int j = 0;j < count;j++){
				TGMeasureImpl measure = (TGMeasureImpl)this.track.getMeasure(j);

				TGColor color = normal ? trackColor : trackColor2;
				painter.setBackground(color);
				painter.setForeground(color);

				if(isRestMeasure(measure)){
					painter.initPath();
					painter.setAntialias(false);
					painter.addRectangle(x,y,size - 2,size - 1);
					painter.closePath();
				}else{
					painter.initPath(TGPainter.PATH_FILL);
					painter.setAntialias(false);
					painter.addRectangle(x,y,size - 1,size );
					painter.closePath();
				}

				boolean hasCaret = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getMeasure().equals(measure);
				if((playing && measure.isPlaying(this.viewer.getEditor().getTablature().getViewLayout())) || (!playing && hasCaret)){
					painter.setBackground(selectedColor);
					painter.initPath(TGPainter.PATH_FILL);
					painter.setAntialias(false);
					painter.addRectangle(x + 4,y + 4,size - 9,size - 8);
					painter.closePath();
				}
				x += size;

				// swap the color to show a structural change
				if (measure.hasDoubleBar() || measure.getRepeatClose() != 0) {
					normal = !normal;
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

}
