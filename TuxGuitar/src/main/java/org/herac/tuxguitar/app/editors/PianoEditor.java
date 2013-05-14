package org.herac.tuxguitar.app.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.editors.piano.Piano;
import org.herac.tuxguitar.app.system.icons.IconLoader;
import org.herac.tuxguitar.app.system.language.LanguageLoader;
import org.herac.tuxguitar.app.tools.scale.ScaleListener;
import org.herac.tuxguitar.app.util.DialogUtils;
import org.herac.tuxguitar.song.models.TGBeat;

public class PianoEditor implements TGRedrawListener, TGExternalBeatViewerListener, IconLoader, LanguageLoader, ScaleListener{

	private Piano piano;

	public PianoEditor(){
		super();
	}

	public void show() {
		Shell dialog = DialogUtils.newDialog(TuxGuitar.instance().getShell(), SWT.DIALOG_TRIM);
		dialog.setLayout(new GridLayout());
		dialog.setText(TuxGuitar.getProperty("piano.editor"));

		this.piano = new Piano(dialog,SWT.NONE);

		this.addListeners();
		dialog.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				removeListeners();
				TuxGuitar.instance().updateCache(true);
			}
		});
		DialogUtils.openDialog(dialog, DialogUtils.OPEN_STYLE_CENTER | DialogUtils.OPEN_STYLE_PACK);
	}

	public void addListeners(){
		TuxGuitar.instance().getIconManager().addLoader(this);
		TuxGuitar.instance().getLanguageManager().addLoader(this);
		TuxGuitar.instance().getScaleManager().addListener(this);
		TuxGuitar.instance().getEditorManager().addRedrawListener(this);
		TuxGuitar.instance().getEditorManager().addBeatViewerListener(this);
	}

	public void removeListeners(){
		TuxGuitar.instance().getIconManager().removeLoader(this);
		TuxGuitar.instance().getLanguageManager().removeLoader(this);
		TuxGuitar.instance().getScaleManager().removeListener(this);
		TuxGuitar.instance().getEditorManager().removeRedrawListener(this);
		TuxGuitar.instance().getEditorManager().removeBeatViewerListener(this);
	}

	private Piano getPiano(){
		return this.piano;
	}

	public void dispose(){
		if(!isDisposed()){
			getPiano().getShell().dispose();
			getPiano().dispose();
		}
	}

	public void redraw(){
		if(!isDisposed() && !TuxGuitar.instance().isLocked()){
			getPiano().redraw();
		}
	}

	public void redrawPlayingMode(){
		if(!isDisposed() && !TuxGuitar.instance().isLocked()){
			getPiano().redrawPlayingMode();
		}
	}

	public boolean isDisposed() {
		return (this.getPiano() == null || getPiano().isDisposed());
	}

	@Override
	public void loadProperties(){
		if(!isDisposed()){
			getPiano().loadProperties();
			getPiano().getShell().setText(TuxGuitar.getProperty("piano.editor"));
		}
	}

	@Override
	public void loadIcons(){
		if(!isDisposed()){
			getPiano().loadIcons();
		}
	}

	@Override
	public void loadScale(){
		if(!isDisposed()){
			getPiano().loadScale();
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
		if(!isDisposed()){
			getPiano().setExternalBeat(beat);
		}
	}

	@Override
	public void hideExternalBeat() {
		if(!isDisposed()){
			getPiano().setExternalBeat(null);
		}
	}
}
