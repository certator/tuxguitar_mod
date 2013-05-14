package org.herac.tuxguitar.app.editors;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.editors.fretboard.FretBoard;
import org.herac.tuxguitar.app.system.icons.IconLoader;
import org.herac.tuxguitar.app.system.language.LanguageLoader;
import org.herac.tuxguitar.app.tools.scale.ScaleListener;
import org.herac.tuxguitar.song.models.TGBeat;

public class FretBoardEditor implements TGRedrawListener, TGExternalBeatViewerListener, IconLoader, LanguageLoader, ScaleListener{

	private FretBoard fretBoard;
	private boolean visible;

	public FretBoardEditor(){
		TuxGuitar.instance().getIconManager().addLoader(this);
		TuxGuitar.instance().getLanguageManager().addLoader(this);
		TuxGuitar.instance().getScaleManager().addListener(this);
	}

	private FretBoard getFretBoard(){
		return this.fretBoard;
	}

	public void hideFretBoard(){
		this.visible = false;
		getFretBoard().setVisible(this.visible);
		TuxGuitar.instance().getEditorManager().removeRedrawListener(this);
		TuxGuitar.instance().getEditorManager().removeBeatViewerListener(this);
		TuxGuitar.instance().updateShellFooter(0,0,0);
	}

	public void showFretBoard(){
		this.visible = true;
		getFretBoard().setVisible(this.visible);
		TuxGuitar.instance().getEditorManager().addRedrawListener(this);
		TuxGuitar.instance().getEditorManager().addBeatViewerListener(this);
		TuxGuitar.instance().updateShellFooter(getFretBoard().getHeight(), 730,520);
	}

	public void showFretBoard(Composite parent) {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0,0);
		data.bottom = new FormAttachment(100,0);

		this.fretBoard = new FretBoard(parent);
		this.fretBoard.setLayoutData(data);
	}

	public void dispose(){
		if(getFretBoard() != null && !getFretBoard().isDisposed()){
			getFretBoard().dispose();
		}
	}

	public void redraw(){
		if(getFretBoard() != null && !getFretBoard().isDisposed() && !TuxGuitar.instance().isLocked()){
			getFretBoard().redraw();
		}
	}

	public void redrawPlayingMode(){
		if(getFretBoard() != null && !getFretBoard().isDisposed() && !TuxGuitar.instance().isLocked()){
			getFretBoard().redrawPlayingMode();
		}
	}

	public boolean isVisible(){
		return (getFretBoard() != null && !getFretBoard().isDisposed() && this.visible);
	}

	@Override
	public void loadProperties(){
		if(getFretBoard() != null && !getFretBoard().isDisposed()){
			getFretBoard().loadProperties();
		}
	}

	@Override
	public void loadIcons(){
		if(getFretBoard() != null && !getFretBoard().isDisposed()){
			getFretBoard().loadIcons();
		}
	}

	@Override
	public void loadScale(){
		if(getFretBoard() != null){
			getFretBoard().loadScale();
		}
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
	public void showExternalBeat(TGBeat beat) {
		if(getFretBoard() != null && !getFretBoard().isDisposed()){
			getFretBoard().setExternalBeat(beat);
		}
	}

	@Override
	public void hideExternalBeat() {
		if(getFretBoard() != null && !getFretBoard().isDisposed()){
			getFretBoard().setExternalBeat(null);
		}
	}
}
