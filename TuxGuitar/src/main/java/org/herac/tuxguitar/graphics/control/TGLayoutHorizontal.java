/*
 * Created on 04-ene-2006
 */
package org.herac.tuxguitar.graphics.control;

import java.util.Iterator;

import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 * @author julian
 */
public class TGLayoutHorizontal extends TGLayout{

	public TGLayoutHorizontal(TGController controller,int style){
		super(controller,style);
	}

	@Override
	public int getMode(){
		return MODE_HORIZONTAL;
	}

	@Override
	public void paintSong(TGPainter painter,TGRectangle clientArea,int fromX,int fromY) {
		this.setWidth(0);
		this.setHeight(0);
		this.clearTrackPositions();

		int style = getStyle();
		int number = getComponent().getTrackSelection();
		int posY = fromY + getFirstTrackSpacing();
		int height = getFirstTrackSpacing();
		int trackHeight;
		Iterator<TGTrack> tracks = getSongManager().getSong().getTracks();
		while(tracks.hasNext()){
			TGTrackImpl track = (TGTrackImpl) tracks.next();
			if(number < 0 || track.getNumber() == number){

				TGTrackSpacing ts = new TGTrackSpacing(this) ;
				ts.setSize(TGTrackSpacing.POSITION_SCORE_MIDDLE_LINES, ((style & DISPLAY_SCORE) != 0 ?( (getScoreLineSpacing() * 5) ):0));
				((TGLyricImpl)track.getLyrics()).start();

				//------AUTO_SPACING---------------------------------------
				int maxY = 0;
				int minY = 0;
				// Need to score extra-lines in edition mode
				if( (style & DISPLAY_TABLATURE) == 0 && (style & DISPLAY_SCORE) != 0 ){
					maxY = ((getScoreLineSpacing() * 4) + (getScoreLineSpacing() * 4));
					minY = -(getScoreLineSpacing() * 3);
				}

				Iterator<TGMeasure> measures = track.getMeasures();
				while(measures.hasNext()){
					TGMeasureImpl measure = (TGMeasureImpl)measures.next();
					maxY = (measure.getMaxY() > maxY)?measure.getMaxY():maxY;
					minY = (measure.getMinY() < minY)?measure.getMinY():minY;
					measure.registerSpacing(this,ts);
				}
				ts.setSize(TGTrackSpacing.POSITION_SCORE_UP_LINES, ( (style & DISPLAY_SCORE) != 0 ?Math.abs(minY):0));
				if((style & DISPLAY_SCORE) != 0 && maxY > track.getScoreHeight()){
					ts.setSize(TGTrackSpacing.POSITION_SCORE_DOWN_LINES, (maxY - track.getScoreHeight()) );
				}
				if((style & DISPLAY_TABLATURE) != 0){
					ts.setSize(TGTrackSpacing.POSITION_TABLATURE_TOP_SEPARATOR, ((style & DISPLAY_SCORE) != 0 ? getMinScoreTabSpacing() : Math.max(Math.abs(minY), getStringSpacing()) ));
					ts.setSize(TGTrackSpacing.POSITION_TABLATURE, ((style & DISPLAY_SCORE) != 0 ?  track.getTabHeight() + getStringSpacing() + 1 : Math.max(maxY, track.getTabHeight() + getStringSpacing() + 1) ));
				}
				ts.setSize(TGTrackSpacing.POSITION_LYRIC,10);
				checkDefaultSpacing(ts);

				//----------------------------------------------------
				paintMeasures(track,painter,fromX,posY,ts,clientArea);
				paintLines(track,ts,painter,fromX + (getWidth() + 2),posY,(clientArea.getWidth() - (fromX + getWidth()) ));

				trackHeight = ts.getSize();
				addTrackPosition(track.getNumber(),posY,trackHeight);

				posY += trackHeight + getTrackSpacing();
				height += trackHeight + getTrackSpacing();
			}
		}
		if(getWidth() > clientArea.getWidth()){
			// solo para dar un espacio.
			this.setWidth( getWidth() + getFirstMeasureSpacing());
		}
		this.setHeight(height);
	}

	public void paintMeasures(TGTrackImpl track,TGPainter painter,int fromX, int fromY,TGTrackSpacing ts,TGRectangle clientArea) {
		int posX = (fromX + getFirstMeasureSpacing());
		int posY = fromY;
		int width = getFirstMeasureSpacing();

		Iterator<TGMeasure> measures = track.getMeasures();
		while(measures.hasNext()){
			TGMeasureImpl measure = (TGMeasureImpl)measures.next();

			//asigno la posicion dentro del compas
			measure.setPosX(posX);
			measure.setPosY(posY);
			measure.setTs(ts);

			((TGLyricImpl)track.getLyrics()).setCurrentMeasure(measure);

			//Solo pinto lo que entre en pantalla
			boolean isAtX = ((posX + measure.getWidth(this)) > clientArea.getX() - 100 && posX < clientArea.getX() + clientArea.getWidth() + measure.getWidth(this) + 100);
			boolean isAtY = (posY + ts.getSize() > clientArea.getY() && posY < clientArea.getY() + clientArea.getHeight() + 80);
			if(isAtX && isAtY){
				paintMeasure(measure,painter,0);
				((TGLyricImpl)track.getLyrics()).paintCurrentNoteBeats(painter,this,measure,posX, posY);
			}else{
				measure.setOutOfBounds(true);
			}

			int measureWidth = measure.getWidth(this);
			posX += measureWidth;
			width += measureWidth;
		}
		this.setWidth(Math.max(getWidth(),width));
	}
}
