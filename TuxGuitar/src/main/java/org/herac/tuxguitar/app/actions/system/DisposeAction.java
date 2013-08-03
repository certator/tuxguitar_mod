/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.system;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TypedEvent;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionAdapter;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.actions.file.FileActionUtils;
import org.herac.tuxguitar.app.marker.MarkerList;
import org.herac.tuxguitar.app.system.config.TGConfigKeys;
import org.herac.tuxguitar.app.system.config.TGConfigManager;
import org.herac.tuxguitar.app.util.ConfirmDialog;
import org.herac.tuxguitar.util.TGSynchronizer;

/**
 * @author julian
 */
public class DisposeAction extends Action {

	public static final String NAME = "action.system.dispose";

	public DisposeAction() {
		super(NAME, AUTO_LOCK);
	}

	@Override
	protected int execute(ActionData actionData){
		TypedEvent e = (TypedEvent)actionData.get(ActionAdapter.PROPERTY_TYPED_EVENT);

		if(e instanceof ShellEvent){
			TuxGuitar.instance().getPlayer().reset();

			if(TuxGuitar.instance().getFileHistory().isUnsavedFile()){
				ConfirmDialog confirm = new ConfirmDialog(TuxGuitar.getProperty("file.save-changes-question"));
				confirm.setDefaultStatus( ConfirmDialog.STATUS_CANCEL );
				int status = confirm.confirm(ConfirmDialog.BUTTON_YES | ConfirmDialog.BUTTON_NO | ConfirmDialog.BUTTON_CANCEL, ConfirmDialog.BUTTON_YES);
				if(status == ConfirmDialog.STATUS_CANCEL){
					return AUTO_UNLOCK;
				}
				if(status == ConfirmDialog.STATUS_YES){
					final String fileName = FileActionUtils.getFileName();
					if(fileName == null){
						return AUTO_UNLOCK;
					}
					TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);
					new Thread(new Runnable() {
						@Override
						public void run() {
							if(!TuxGuitar.isDisposed()){
								FileActionUtils.save(fileName);
								TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);

								exit();
							}
						}
					}).start();
					return 0;
				}
			}
			exit();
		}
		return 0;
	}

	protected void exit(){
		try {
			TGSynchronizer.instance().runLater(new TGSynchronizer.TGRunnable() {
				@Override
				public void run() throws Throwable {
					TuxGuitar.instance().lock();
					closeModules();
					saveConfig();
					dispose();
					TuxGuitar.instance().unlock();
				}
			});
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	protected void saveConfig(){
		TGConfigManager config = TuxGuitar.instance().getConfig();

		config.setProperty(TGConfigKeys.LAYOUT_MODE,getEditor().getTablature().getViewLayout().getMode());
		config.setProperty(TGConfigKeys.LAYOUT_STYLE,getEditor().getTablature().getViewLayout().getStyle());
		config.setProperty(TGConfigKeys.SHOW_PIANO,!TuxGuitar.instance().getPianoEditor().isDisposed());
		config.setProperty(TGConfigKeys.SHOW_MATRIX,!TuxGuitar.instance().getMatrixEditor().isDisposed());
		config.setProperty(TGConfigKeys.SHOW_FRETBOARD,TuxGuitar.instance().getFretBoardEditor().isVisible());
		config.setProperty(TGConfigKeys.SHOW_INSTRUMENTS,!TuxGuitar.instance().getChannelManager().isDisposed());
		config.setProperty(TGConfigKeys.SHOW_TRANSPORT,!TuxGuitar.instance().getTransport().isDisposed());
		config.setProperty(TGConfigKeys.SHOW_MARKERS,!MarkerList.instance().isDisposed());
		config.setProperty(TGConfigKeys.MAXIMIZED,TuxGuitar.instance().getShell().getMaximized());
		config.setProperty(TGConfigKeys.WIDTH,TuxGuitar.instance().getShell().getClientArea().width);
		config.setProperty(TGConfigKeys.HEIGHT,TuxGuitar.instance().getShell().getClientArea().height);
		config.setProperty(TGConfigKeys.EDITOR_MOUSE_MODE,getEditor().getTablature().getEditorKit().getMouseMode());
		config.setProperty(TGConfigKeys.MATRIX_GRIDS,TuxGuitar.instance().getMatrixEditor().getGrids());

		TuxGuitar.instance().getConfig().save();
	}

	protected void closeModules(){
		TuxGuitar.instance().getPlayer().close();
		TuxGuitar.instance().getPluginManager().closePlugins();
	}

	protected void dispose(){
		TuxGuitar.instance().getTable().dispose();
		TuxGuitar.instance().getSongManager().clearSong();
		TuxGuitar.instance().getFretBoardEditor().dispose();
		TuxGuitar.instance().getTablatureEditor().getTablature().dispose();
		TuxGuitar.instance().getIconManager().disposeIcons();
		TuxGuitar.instance().getShell().dispose();
	}
}