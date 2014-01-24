/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.action.impl.insert;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.action.TGActionBase;
import org.herac.tuxguitar.app.undo.undoables.custom.UndoableChangeCloseRepeat;
import org.herac.tuxguitar.app.util.DialogUtils;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RepeatCloseAction extends TGActionBase{
	
	public static final String NAME = "action.insert.close-repeat";
	
	public RepeatCloseAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}
	
	protected void processAction(TGActionContext context){
		TGMeasureImpl measure = getEditor().getTablature().getCaret().getMeasure();
		showCloseRepeatDialog(getEditor().getTablature().getShell(), measure);
	}
	
	public void updateTablature() {
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}
	
	public void showCloseRepeatDialog(Shell shell, final TGMeasureImpl measure) {
		if (measure != null) {
			final Shell dialog = DialogUtils.newDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			
			dialog.setLayout(new GridLayout());
			dialog.setText(TuxGuitar.getProperty("repeat.close"));
			
			int currentRepeatClose = measure.getRepeatClose();
			if (currentRepeatClose < 1) {
				currentRepeatClose = 1;
			}
			
			Group group = new Group(dialog,SWT.SHADOW_ETCHED_IN);
			group.setLayout(new GridLayout(2,false));
			group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
			group.setText(TuxGuitar.getProperty("repeat.close"));
			
			Label repeatCloseLabel = new Label(group, SWT.NULL);
			repeatCloseLabel.setText(TuxGuitar.getProperty("repeat.number-of-repetitions"));
			
			final Spinner repeatClose = new Spinner(group, SWT.BORDER);
			repeatClose.setMinimum(0);
			repeatClose.setMaximum(100);
			repeatClose.setSelection(currentRepeatClose);
			repeatClose.setLayoutData(getSpinnerData());
			
			//----------------------BUTTONS--------------------------------
			Composite buttons = new Composite(dialog, SWT.NONE);
			buttons.setLayout(new GridLayout(3,false));
			buttons.setLayoutData(new GridData(SWT.END,SWT.FILL,true,true));
			
			final Button buttonOK = new Button(buttons, SWT.PUSH);
			buttonOK.setText(TuxGuitar.getProperty("ok"));
			buttonOK.setLayoutData(getButtonData());
			buttonOK.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent arg0) {
					closeRepeat(measure,repeatClose.getSelection());
					dialog.dispose();
				}
			});
			Button buttonClean = new Button(buttons, SWT.PUSH);
			buttonClean.setText(TuxGuitar.getProperty("clean"));
			buttonClean.setLayoutData(getButtonData());
			buttonClean.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent arg0) {
					closeRepeat(measure,0);
					dialog.dispose();
				}
			});
			Button buttonCancel = new Button(buttons, SWT.PUSH);
			buttonCancel.setText(TuxGuitar.getProperty("cancel"));
			buttonCancel.setLayoutData(getButtonData());
			buttonCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent arg0) {
					dialog.dispose();
				}
			});
			
			dialog.setDefaultButton( buttonOK );
			
			DialogUtils.openDialog(dialog,DialogUtils.OPEN_STYLE_CENTER | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);
		}
	}
	
	private GridData getButtonData(){
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumWidth = 80;
		data.minimumHeight = 25;
		return data;
	}
	
	protected GridData getSpinnerData(){
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
		data.minimumWidth = 100;
		return data;
	}
	
	protected void closeRepeat(TGMeasureImpl measure, int repeatClose) {
		if(repeatClose >= 0){
			
			//comienza el undoable
			UndoableChangeCloseRepeat undoable = UndoableChangeCloseRepeat.startUndo();
			TuxGuitar.instance().getFileHistory().setUnsavedFile();
			
			//numberOfRepetitions = Math.abs(numberOfRepetitions);
			getSongManager().changeCloseRepeat(measure.getStart(), repeatClose);
			updateTablature();
			
			//termia el undoable
			addUndoableEdit(undoable.endUndo(repeatClose));
		}
	}
}
