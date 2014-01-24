/*
 * Created on 29-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.editors.tab;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.editors.TGColorImpl;
import org.herac.tuxguitar.app.editors.TGPainterImpl;
import org.herac.tuxguitar.app.editors.TGResourceFactoryImpl;
import org.herac.tuxguitar.app.editors.TGScrollBar;
import org.herac.tuxguitar.app.system.config.TGConfig;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.graphics.TGResourceFactory;
import org.herac.tuxguitar.graphics.control.TGBeatImpl;
import org.herac.tuxguitar.graphics.control.TGController;
import org.herac.tuxguitar.graphics.control.TGLayout;
import org.herac.tuxguitar.graphics.control.TGLayoutStyles;
import org.herac.tuxguitar.graphics.control.TGLayoutVertical;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
/**
 * @author julian
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class Tablature implements TGController {
	
	private Component component;
	private TGScrollBar scroll;
	
	private TGSongManager songManager;
	private Caret caret;
	//private int width;
	private int height;
	private TGLayout viewLayout;
	
	private TGBeatImpl playedBeat;
	private TGMeasureImpl playedMeasure;
	
	//private int scrollX;
	private int scrollY;
	private boolean resetScroll;
	protected long lastVScrollTime;
	protected long lastHScrollTime;
	
	private boolean painting;
	
	public Tablature(Component component,TGScrollBar scroll) {
		this.component = component;
		this.scroll = scroll;
	}
	
	public void initDefaults(){
		this.caret = new Caret(this);
	}
	
	public void updateTablature(){
		this.playedBeat = null;
		this.playedMeasure = null;
		getViewLayout().updateSong();
	}
	
	public void initCaret(){
		this.caret.update(1,TGDuration.QUARTER_TIME,1);
	}
	
	public synchronized void paintTablature(TGPainterImpl painter){
		TuxGuitar.instance().lock();
		this.setPainting(true);
		try{
			this.checkScroll();
			
			Rectangle area = this.component.getBounds();
			
			this.scrollY = this.scroll.getValue();
			
			this.getViewLayout().paint(painter, createRectangle(area) , 0, -this.scrollY );
			
			//this.width = this.viewLayout.getWidth();
			this.height = this.viewLayout.getHeight();
			
			this.updateScroll();
			
			if(TuxGuitar.instance().getPlayer().isRunning()){
				redrawPlayingMode(painter,true);
			}
			// Si no estoy reproduciendo y hay cambios
			// muevo el scroll al compas que tiene el caret
			else if(getCaret().hasChanges()){
				// Mover el scroll puede necesitar redibujar
				// por eso es importante desmarcar los cambios antes de hacer el moveScrollTo
				getCaret().setChanges(false);
					
				moveScrollTo(getCaret().getMeasure() , area);
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		this.setPainting(false);
		TuxGuitar.instance().unlock();
	}
	
	public void resetScroll(){
		this.resetScroll = true;
	}
	
	public void checkScroll(){
		if(this.resetScroll){
			this.scroll.setValue(0);
			this.resetScroll = false;
		}
	}
	
	public void updateScroll(){
		this.scroll.setMaximum((int)Math.max(0, (this.height - this.component.getSize().getHeight()) ) );
	}
	
	public boolean moveScrollTo(TGMeasureImpl measure){
		return this.moveScrollTo(measure, this.component.getBounds());
	}
	
	public boolean moveScrollTo(TGMeasureImpl measure, Rectangle area){
		boolean success = false;
		if(measure != null && measure.getTs() != null){
			int mX = measure.getPosY();
			int mWidth = measure.getTs().getSize();
			int marginWidth = getViewLayout().getFirstTrackSpacing();
			boolean forceRedraw = false;
			
			//Solo se ajusta si es necesario
			//si el largo del compas es mayor al de la pantalla. nunca se puede ajustar a la medida.
			if( mX < 0 || ( (mX + mWidth ) > area.height && (area.height >= mWidth + marginWidth || mX > marginWidth) )  ){
				this.scroll.setValue((this.scrollY + mX) - marginWidth );
				success = true;
			}
			
			if(!success){
				// Si la seleccion "real" del scroll es distinta a la anterior, se fuerza el redraw
				forceRedraw = (this.scrollY != this.scroll.getValue() );
			}
			
			if(forceRedraw || success){
				this.component.repaint();
			}
		}
		return success;
	}
	
	public void beforeRedraw(){
		TuxGuitar.instance().lock();
		this.playedBeat = null;
		this.playedMeasure = null;
		this.setPainting(true);
		TuxGuitar.instance().unlock();
	}
	
	public void redrawPlayingMode(){
		if(!isPainting() && !TuxGuitar.instance().isLocked()){
			TuxGuitar.instance().lock();
			if(TuxGuitar.instance().getPlayer().isRunning()){
				this.setPainting(true);
				
				TGPainterImpl painter = new TGPainterImpl((Graphics2D)this.component.getGraphics());
				redrawPlayingMode(painter,false);
				painter.dispose();
				
				this.setPainting(false);
			}
			TuxGuitar.instance().unlock();
		}
	}
	
	private void redrawPlayingMode(TGPainterImpl painter,boolean force){
		if(!TuxGuitar.instance().isLocked()){
			try{
				org.herac.tuxguitar.graphics.control.TGMeasureImpl measure = TuxGuitar.instance().getEditorCache().getPlayMeasure();
				TGBeatImpl beat = TuxGuitar.instance().getEditorCache().getPlayBeat();
				if(measure != null && beat != null && measure.hasTrack(getCaret().getTrack().getNumber())){
					if(!moveScrollTo(measure) || force){
						boolean paintMeasure = (force || this.playedMeasure == null || !this.playedMeasure.equals(measure));
						if(this.playedMeasure != null && this.playedBeat != null && !this.playedMeasure.isOutOfBounds() && this.playedMeasure.hasTrack(getCaret().getTrack().getNumber())){
							getViewLayout().paintPlayMode(painter, this.playedMeasure, this.playedBeat,paintMeasure);
						}
						if(!measure.isOutOfBounds()){
							getViewLayout().paintPlayMode(painter, measure, beat,paintMeasure);
						}
						this.playedBeat = beat;
						this.playedMeasure =  measure;
					}
				}
			}catch(Throwable throwable){
				throwable.printStackTrace();
			}
		}
	}
	
	public boolean isPainting() {
		return this.painting;
	}
	
	public void setPainting(boolean painting) {
		this.painting = painting;
	}
	
	public Caret getCaret(){
		return this.caret;
	}
	
	public TGSongManager getSongManager() {
		return this.songManager;
	}
	
	public void setSongManager(TGSongManager songManager) {
		this.songManager = songManager;
	}
	
	public TGLayout getViewLayout(){
		return this.viewLayout;
	}
	
	public void setViewLayout(TGLayout viewLayout){
		if(getViewLayout() != null){
			getViewLayout().disposeLayout();
		}
		this.viewLayout = viewLayout;
		/*
		if(this.getHorizontalBar() != null){
			this.getHorizontalBar().setSelection(0);
		}
		if(this.getVerticalBar() != null){
			this.getVerticalBar().setSelection(0);
		}*/
		this.reloadStyles();
	}
	
	public void reloadStyles(){
		if(this.getViewLayout() != null){
			this.getViewLayout().loadStyles(1.0f);
			this.component.setBackground(((TGColorImpl)getViewLayout().getResources().getBackgroundColor()).getHandle() );
		}
	}
	
	public TGRectangle createRectangle( Rectangle rectangle ){
		return new TGRectangle(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
	}
	
	public void reloadViewLayout(){
		setViewLayout(new TGLayoutVertical(this,TGConfig.LAYOUT_STYLE));
	}
	
	public void dispose(){
		this.getViewLayout().disposeLayout();
	}

	public TGResourceFactory getResourceFactory() {
		return new TGResourceFactoryImpl();
	}
	
	public void configureStyles(TGLayoutStyles styles) {
		styles.setBufferEnabled(true);
		styles.setStringSpacing( TGConfig.TAB_LINE_SPACING );
		styles.setScoreLineSpacing( TGConfig.SCORE_LINE_SPACING );
		styles.setFirstMeasureSpacing(0);
		styles.setMinBufferSeparator(20);
		styles.setMinTopSpacing(30);
		styles.setMinScoreTabSpacing(TGConfig.MIN_SCORE_TABLATURE_SPACING);
		styles.setFirstTrackSpacing(TGConfig.FIRST_TRACK_SPACING);
		styles.setTrackSpacing(TGConfig.TRACK_SPACING);
		
		styles.setChordFretIndexSpacing(8);
		styles.setChordStringSpacing(5);
		styles.setChordFretSpacing(6);
		styles.setChordNoteSize(4);
		styles.setRepeatEndingSpacing(20);
		styles.setTextSpacing(15);
		styles.setMarkerSpacing(15);
		styles.setDivisionTypeSpacing(10);
		styles.setEffectSpacing(8);
		
		styles.setDefaultFont(TGConfig.FONT_DEFAULT);
		styles.setNoteFont(TGConfig.FONT_NOTE);
		styles.setTimeSignatureFont(TGConfig.FONT_TIME_SIGNATURE);
		styles.setLyricFont(TGConfig.FONT_LYRIC);
		styles.setTextFont(TGConfig.FONT_TEXT);
		styles.setMarkerFont(TGConfig.FONT_MARKER);
		styles.setGraceFont(TGConfig.FONT_GRACE);
		styles.setChordFont(TGConfig.FONT_CHORD);
		styles.setChordFretFont(TGConfig.FONT_CHORD_FRET);
		styles.setBackgroundColor(TGConfig.COLOR_BACKGROUND);
		styles.setLineColor(TGConfig.COLOR_LINE);
		styles.setScoreNoteColor(TGConfig.COLOR_SCORE_NOTE);
		styles.setTabNoteColor(TGConfig.COLOR_TAB_NOTE);
		styles.setPlayNoteColor(TGConfig.COLOR_PLAY_NOTE);
	}

	public int getTrackSelection(){
		if( (getViewLayout().getStyle() & TGLayout.DISPLAY_MULTITRACK) == 0 ){
			return getCaret().getTrack().getNumber();
		}
		return -1;
	}
	
	public boolean isRunning(TGBeat beat) {
		return ( isRunning( beat.getMeasure() ) && TuxGuitar.instance().getEditorCache().isPlaying(beat.getMeasure(),beat) );
	}
	
	public boolean isRunning(TGMeasure measure) {
		return ( measure.getTrack().equals(getCaret().getTrack()) && TuxGuitar.instance().getEditorCache().isPlaying( measure ) );
	}

	public boolean isLoopSHeader(TGMeasureHeader measureHeader) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLoopEHeader(TGMeasureHeader measureHeader) {
		// TODO Auto-generated method stub
		return false;
	}
}