package org.herac.tuxguitar.app.tracktable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.actions.ActionLock;
import org.herac.tuxguitar.app.actions.composition.ChangeInfoAction;
import org.herac.tuxguitar.app.actions.track.GoToTrackAction;
import org.herac.tuxguitar.app.actions.track.TrackPropertiesAction;
import org.herac.tuxguitar.app.editors.TGRedrawListener;
import org.herac.tuxguitar.app.editors.TGUpdateListener;
import org.herac.tuxguitar.app.editors.TablatureEditor;
import org.herac.tuxguitar.app.items.menu.TrackMenu;
import org.herac.tuxguitar.app.system.config.TGConfigKeys;
import org.herac.tuxguitar.app.system.icons.IconLoader;
import org.herac.tuxguitar.app.system.language.LanguageLoader;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.graphics.control.TGTrackImpl;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

public class TrackTableViewer implements TGRedrawListener, TGUpdateListener, LanguageLoader {

	private Composite composite;
	private ScrollBar hSroll;
	private Color[] backgrounds;
	private Color[] foregrounds;
	private TrackTable table;
	private int selectedTrack;
	private int selectedMeasure;
	private int trackCount = 0;
	private boolean autoSizeEnabled;
	private boolean update;
	private boolean followScroll;
	private boolean resetTexts;
	private final MouseWheelListener trackCanvasZoomMouseWheel;

	public TrackTableViewer() {
		TuxGuitar.instance().getLanguageManager().addLoader(this);
		TuxGuitar.instance().getEditorManager().addRedrawListener(this);
		TuxGuitar.instance().getEditorManager().addUpdateListener(this);

		trackCanvasZoomMouseWheel = new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent e) {
				// skip if CTRL key is not pressed
				if ((e.stateMask & SWT.CTRL) == 0) {
					return;
				}
				TrackCanvas canvas = (TrackCanvas) e.getSource();
				float coef = canvas.getWidthMeasureCoef();
				final float constante = 4;
				if (e.count > 0) {
					coef *= 1.0 + 1.0 / constante;
				} else if (e.count < 0) {
					coef *= (constante - 1) / constante;
				} else {
					return;
				}
				for (int i = 0; i < getTable().getRowCount(); i++) {
					TrackTableRow row = getTable().getRow(i);
					row.getCanvas().setWidthMeasureCoef(coef);
				}
				hSroll.setThumb(canvas.getSize().x);
				hSroll.setMaximum(canvas.getRealWidth());
				updateCanvasScroll(hSroll.getSelection());
			}
		};
	}

	public void init(Composite parent){
		this.composite = new Composite(parent,SWT.H_SCROLL);
		this.addColors();
		this.addLayout();
		this.addTable();
		this.addHScroll();
		this.loadConfig();
	}

	private void addColors(){
		Display display = this.getComposite().getDisplay();

		this.backgrounds = new Color[]{
			new Color(display,255,255,255),
			new Color(display,238,238,238),
			new Color(display,192,192,192),
		};
		this.foregrounds = new Color[]{
			new Color( display, 0, 0, 0 ),
			new Color( display, 0, 0, 0 ),
			new Color( display, 0, 0, 0 ),
		};
	}

	private void addLayout(){
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		getComposite().setLayout(layout);
	}

	private void addHScroll(){
		this.hSroll = getComposite().getHorizontalBar();
		this.hSroll.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				updateCanvasScroll(hSroll.getSelection());
			}
		});
	}

	private void addTable(){
		MouseListener listener = mouseFocusListener();
		this.table = new TrackTable(getComposite());
		this.table.getColumnNumber().getControl().addMouseListener(listener);
		this.table.getColumnSoloMute().getControl().addMouseListener(listener);
		this.table.getColumnName().getControl().addMouseListener(listener);
		this.table.getColumnInstrument().getControl().addMouseListener(listener);
		this.table.getColumnCanvas().getControl().addMouseListener(listener);
		this.table.getColumnCanvas().getControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TuxGuitar.instance().getAction(ChangeInfoAction.NAME).process(new ActionData());
			}
		});
		this.fireUpdate(true);
		this.loadProperties();
	}

	@Override
	public void loadProperties() {
		this.table.getColumnNumber().setTitle(TuxGuitar.getProperty("track.number"));
		this.table.getColumnSoloMute().setTitle(TuxGuitar.getProperty("track.short-solo-mute"));
		this.table.getColumnName().setTitle(TuxGuitar.getProperty("track.name"));
		this.table.getColumnInstrument().setTitle(TuxGuitar.getProperty("track.instrument"));
	}

	public void fireUpdate(boolean newSong){
		this.update = true;
		if(newSong){
			this.trackCount = 0;
		}
	}

	public void updateItems(){
		this.resetTexts = true;
		this.followScroll = true;
	}

	public void updateHScroll(){
		int width = 0;
		int visibleSize = 0;
		if (getTable().getRowCount() > 0) {
			TrackCanvas canvas = getTable().getRow(0).getCanvas();
			width = canvas.getRealWidth();
			visibleSize = Math.min(width , canvas.getSize().x);
		}
		this.hSroll.setIncrement(this.table.getScrollIncrement());
		this.hSroll.setMaximum(width);
		this.hSroll.setThumb(visibleSize);
	}

	public TrackTable getTable(){
		return this.table;
	}

	public int getHScrollSelection(){
		return this.hSroll.getSelection();
	}

	public TablatureEditor getEditor(){
		return TuxGuitar.instance().getTablatureEditor();
	}

	private void updateTable(){
		if(this.update){
			int count = TuxGuitar.instance().getSongManager().getSong().countTracks();
			this.table.removeRowsAfter(count);
			for(int i = this.table.getRowCount(); i < count; i ++){
				final TrackTableRow row = this.table.newRow();
				if (i == 0) {
					row.getCanvas().addControlListener(new ControlListener() {
						@Override
						public void controlResized(ControlEvent arg0) {
							updateHScroll();
						}
						@Override
						public void controlMoved(ControlEvent arg0) {
						}
					});
				}

				row.getCanvas().addMouseWheelListener(trackCanvasZoomMouseWheel);
				row.addMouseListenerLabel(new MouseAdapter() {

					@Override
					public void mouseUp(MouseEvent e) {
						row.getCanvas().setFocus();
					}

					@Override
					public void mouseDown(MouseEvent e) {
						if(row.getTrack().getNumber() != getEditor().getTablature().getCaret().getTrack().getNumber()){
							ActionData actionData = new ActionData();
							actionData.put(GoToTrackAction.PROPERTY_TRACK, row.getTrack());

							TuxGuitar.instance().getAction(GoToTrackAction.NAME).process(actionData);
						}
					}

					@Override
					public void mouseDoubleClick(final MouseEvent e) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								ActionLock.waitFor();
								TuxGuitar.instance().getAction(TrackPropertiesAction.NAME).process(new ActionData());
							}
						}).start();
					}
				});
				row.addMouseListenerCanvas(new MouseAdapter() {

					@Override
					public void mouseUp(MouseEvent e) {
						row.getCanvas().setFocus();
					}

					@Override
					public void mouseDown(MouseEvent e) {
						TGTrack track = row.getTrack();
						TGMeasureImpl measure = (TGMeasureImpl) row.getCanvas().getMeasureAtPosition(e.x);
						if(measure != null){
							TGBeat beat = TuxGuitar.instance().getSongManager().getMeasureManager().getFirstBeat(measure.getBeats());
							if(beat != null){
								getEditor().getTablature().getCaret().moveTo((TGTrackImpl)track,measure,beat,1);
								TuxGuitar.instance().updateCache(true);
							}
						}
					}
				});
			}
			for(int i = 0; i < count; i ++){
				TGTrack track = TuxGuitar.instance().getSongManager().getSong().getTrack(i);
				final TrackTableRow row = this.table.getRow(i);
				if(row != null){
					row.setTrack(track);
					row.setMenu(createTrackMenu());
				}
			}
			this.table.update();
			this.selectedTrack = 0;
			this.selectedMeasure = 0;

			if(this.autoSizeEnabled && this.trackCount != count){
				TuxGuitar.instance().setTableHeight( getHeight() );
				this.trackCount = count;
			}

		}
		this.update = false;
	}

	private int getHeight(){
		Rectangle r1 = this.composite.getBounds();
		Rectangle r2 = this.composite.getClientArea();
		return ( this.table.getMinHeight() + (r1.height - r2.height) );
	}

	private void resetTextsValues(){
		int rows = this.table.getRowCount();
		for(int i = 0; i < rows; i ++){
			TrackTableRow row = this.table.getRow(i);
			row.resetTextsValues();
		}
	}

	private void redrawRows(int selectedTrack){
		int rows = this.table.getRowCount();
		for(int i = 0; i < rows; i ++){
			TrackTableRow row = this.table.getRow(i);
			row.getCanvas().redraw();
			if(this.selectedTrack != selectedTrack){
				row.setBackground( this.backgrounds[ ((selectedTrack - 1) == i)? 2: ( i % 2 ) ] );
				row.setForeground( this.foregrounds[ ((selectedTrack - 1) == i)? 2: ( i % 2 ) ] );
			}
		}
	}

	private void updateCanvasScroll(int scroll) {
		for (int i = 0; i < getTable().getRowCount(); i++) {
			TrackTableRow row = getTable().getRow(i);
			row.getCanvas().setHScroll(scroll);
		}
	}

	public void redraw(){
		if(!isDisposed() && !TuxGuitar.instance().isLocked()){
			this.updateTable();
			this.table.getColumnCanvas().setTitle(TuxGuitar.instance().getSongManager().getSong().getName());
			int selectedTrack = getEditor().getTablature().getCaret().getTrack().getNumber();
			this.redrawRows(selectedTrack);
			this.selectedTrack = selectedTrack;
			this.selectedMeasure = 0;
			this.updateHScroll();

			if( this.resetTexts ){
				this.resetTextsValues();
				this.resetTexts = false;
			}
			if( this.followScroll ){
				this.followHorizontalScroll(getEditor().getTablature().getCaret().getMeasure().getNumber());
				this.followScroll = false;
			}
			getComposite().redraw();
		}
	}

	public void redrawPlayingMode(){
		if(!isDisposed() && !TuxGuitar.instance().isLocked()){
			TGMeasure measure =  TuxGuitar.instance().getEditorCache().getPlayMeasure();
			if(measure != null && measure.getTrack() != null){
				this.updateTable();
				int selectedTrack = measure.getTrack().getNumber();
				int selectedMeasure = measure.getNumber();
				if(this.selectedTrack != selectedTrack || this.selectedMeasure != selectedMeasure){
					this.redrawRows(selectedTrack);
					this.followHorizontalScroll(selectedMeasure);
				}
				this.selectedTrack = selectedTrack;
				this.selectedMeasure = selectedMeasure;
			}
		}
	}

	private void followHorizontalScroll(int selectedMeasure){
		int hScrollSelection = this.hSroll.getSelection();
		int hScrollThumb = this.hSroll.getThumb();

		int measureSize = this.table.getRowHeight();
		int measurePosition = ( (selectedMeasure * measureSize) - measureSize );

		if( (measurePosition - hScrollSelection) < 0 || (measurePosition + measureSize - hScrollSelection ) > hScrollThumb){
			this.hSroll.setSelection(measurePosition);
			// hScroll.setSelection can clamp measurePosition
			updateCanvasScroll(this.hSroll.getSelection());
		}
	}

	public void loadConfig(){
		this.autoSizeEnabled = TuxGuitar.instance().getConfig().getBooleanConfigValue(TGConfigKeys.TABLE_AUTO_SIZE);
		this.trackCount = 0;
	}

	public Composite getComposite(){
		return this.composite;
	}

	public Menu createTrackMenu(){
		final TrackMenu trackMenu = new TrackMenu(getComposite().getShell(),SWT.POP_UP);
		trackMenu.showItems();
		trackMenu.update();

		final TGUpdateListener trackMenuUpdateListener = new TGUpdateListener() {
			@Override
			public void doUpdate(int type) {
				if(!trackMenu.isDisposed() && type == TGUpdateListener.SELECTION ){
					trackMenu.update();
				}
			}
		};
		final LanguageLoader trackMenuLanguageLoader = new LanguageLoader() {
			@Override
			public void loadProperties() {
				if(!trackMenu.isDisposed()){
					trackMenu.loadProperties();
				}
			}
		};
		final IconLoader trackMenuIconLoader = new IconLoader() {
			@Override
			public void loadIcons() {
				if(!trackMenu.isDisposed()){
					trackMenu.loadIcons();
				}
			}
		};

		TuxGuitar.instance().getEditorManager().addUpdateListener(trackMenuUpdateListener);
		TuxGuitar.instance().getLanguageManager().addLoader(trackMenuLanguageLoader);
		TuxGuitar.instance().getIconManager().addLoader(trackMenuIconLoader);

		trackMenu.getMenu().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				TuxGuitar.instance().getEditorManager().removeUpdateListener(trackMenuUpdateListener);
				TuxGuitar.instance().getLanguageManager().removeLoader(trackMenuLanguageLoader);
				TuxGuitar.instance().getIconManager().removeLoader(trackMenuIconLoader);
			}
		});

		return trackMenu.getMenu();
	}

	public void disposeColors(){
		for(int i = 0; i < this.backgrounds.length; i++){
			this.backgrounds[i].dispose();
		}
		for(int i = 0; i < this.foregrounds.length; i++){
			this.foregrounds[i].dispose();
		}
	}

	public void dispose(){
		if(!isDisposed()){
			getComposite().dispose();
			disposeColors();
		}
	}

	public boolean isDisposed(){
		return (getComposite() == null || getComposite().isDisposed());
	}

	protected int getSelectedTrack(){
		return this.selectedTrack;
	}

	private MouseListener mouseFocusListener(){
		return new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TrackTable table = getTable();
				if(table != null){
					TrackTableRow row = table.getRow( ( getSelectedTrack() - 1 ) );
					if(row != null){
						row.getCanvas().setFocus();
					}
				}
			}
		};
	}

	@Override
	public void doRedraw(int type) {
		if( type == TGRedrawListener.NORMAL ){
			this.redraw();
		}else if( type == TGRedrawListener.PLAYING_NEW_BEAT ){
			this.redrawPlayingMode();
		}
	}

	@Override
	public void doUpdate(int type) {
		if( type == TGUpdateListener.SELECTION ){
			this.updateItems();
		}else if( type == TGUpdateListener.SONG_UPDATED ){
			this.fireUpdate( false );
		}else if( type == TGUpdateListener.SONG_LOADED ){
			this.fireUpdate( true );
		}
	}
}
