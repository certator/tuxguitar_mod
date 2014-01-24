/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.action.impl.system;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TypedEvent;
import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.action.TGActionBase;
import org.herac.tuxguitar.app.action.TGActionLock;
import org.herac.tuxguitar.app.action.TGActionProcessor;
import org.herac.tuxguitar.app.action.impl.file.FileActionUtils;
import org.herac.tuxguitar.app.marker.MarkerList;
import org.herac.tuxguitar.app.system.config.TGConfigKeys;
import org.herac.tuxguitar.app.util.ConfirmDialog;
import org.herac.tuxguitar.util.TGException;
import org.herac.tuxguitar.util.TGSynchronizer;
import org.herac.tuxguitar.util.configuration.TGConfigManager;

/**
 * @author julian
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class DisposeAction extends TGActionBase {
	
	public static final String NAME = "action.system.dispose";
	
	public DisposeAction() {
		super(NAME, AUTO_LOCK);
	}
	
	protected void processAction(TGActionContext context){
		TypedEvent e = (TypedEvent)context.getAttribute(TGActionProcessor.PROPERTY_TYPED_EVENT);
		
		if(e instanceof ShellEvent){
			TuxGuitar.instance().getPlayer().reset();
			
			if(TuxGuitar.instance().getFileHistory().isUnsavedFile()){
				ConfirmDialog confirm = new ConfirmDialog(TuxGuitar.getProperty("file.save-changes-question"));
				confirm.setDefaultStatus( ConfirmDialog.STATUS_CANCEL );
				int status = confirm.confirm(ConfirmDialog.BUTTON_YES | ConfirmDialog.BUTTON_NO | ConfirmDialog.BUTTON_CANCEL, ConfirmDialog.BUTTON_YES);
				if(status == ConfirmDialog.STATUS_CANCEL){
					TGActionLock.unlock();
					return;
				}
				if(status == ConfirmDialog.STATUS_YES){
					final String fileName = FileActionUtils.getFileName();
					if(fileName == null){
						TGActionLock.unlock();
						return;
					}
					TuxGuitar.instance().loadCursor(SWT.CURSOR_WAIT);
					new Thread(new Runnable() {
						public void run() throws TGException {
							if(!TuxGuitar.isDisposed()){
								FileActionUtils.save(fileName);
								TuxGuitar.instance().loadCursor(SWT.CURSOR_ARROW);
								
								exit();
							}
						}
					}).start();
					return;
				}
			}
			exit();
		}
	}
	
	protected void exit(){
		try {
			TGSynchronizer.instance().executeLater(new TGSynchronizer.TGRunnable() {
				public void run() throws TGException {
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
		
		config.setValue(TGConfigKeys.LAYOUT_MODE,getEditor().getTablature().getViewLayout().getMode());
		config.setValue(TGConfigKeys.LAYOUT_STYLE,getEditor().getTablature().getViewLayout().getStyle());
		config.setValue(TGConfigKeys.SHOW_PIANO,!TuxGuitar.instance().getPianoEditor().isDisposed());
		config.setValue(TGConfigKeys.SHOW_MATRIX,!TuxGuitar.instance().getMatrixEditor().isDisposed());
		config.setValue(TGConfigKeys.SHOW_FRETBOARD,TuxGuitar.instance().getFretBoardEditor().isVisible());
		config.setValue(TGConfigKeys.SHOW_INSTRUMENTS,!TuxGuitar.instance().getChannelManager().isDisposed());
		config.setValue(TGConfigKeys.SHOW_TRANSPORT,!TuxGuitar.instance().getTransport().isDisposed());
		config.setValue(TGConfigKeys.SHOW_MARKERS,!MarkerList.instance().isDisposed());
		config.setValue(TGConfigKeys.MAXIMIZED,TuxGuitar.instance().getShell().getMaximized());
		config.setValue(TGConfigKeys.WIDTH,TuxGuitar.instance().getShell().getClientArea().width);
		config.setValue(TGConfigKeys.HEIGHT,TuxGuitar.instance().getShell().getClientArea().height);
		config.setValue(TGConfigKeys.EDITOR_MOUSE_MODE,getEditor().getTablature().getEditorKit().getMouseMode());
		config.setValue(TGConfigKeys.MATRIX_GRIDS,TuxGuitar.instance().getMatrixEditor().getGrids());
		
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