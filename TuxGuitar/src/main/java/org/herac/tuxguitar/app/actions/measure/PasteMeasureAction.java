/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.actions.measure;

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
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.clipboard.CannotInsertTransferException;
import org.herac.tuxguitar.app.clipboard.MeasureTransfer;
import org.herac.tuxguitar.app.clipboard.MeasureTransferable;
import org.herac.tuxguitar.app.util.DialogUtils;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;

/**
 * @author julian
 */
public class PasteMeasureAction extends Action{

	public static final String NAME = "action.measure.paste";

	public PasteMeasureAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	@Override
	protected int execute(ActionData actionData){
		showDialog(getEditor().getTablature().getShell());
		return 0;
	}

	public void showDialog(Shell shell) {
		TGMeasureImpl measure = getEditor().getTablature().getCaret().getMeasure();
		if (measure != null) {
			final Shell dialog = DialogUtils.newDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialog.setLayout(new GridLayout());
			dialog.setText(TuxGuitar.getProperty("edit.paste"));

			//-----------------COUNT------------------------
			Group group = new Group(dialog,SWT.SHADOW_ETCHED_IN);
			group.setLayout(new GridLayout(2,false));
			group.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
			group.setText(TuxGuitar.getProperty("edit.paste"));

			Label countLabel = new Label(group, SWT.NULL);
			countLabel.setText(TuxGuitar.getProperty("edit.paste.count"));

			final Spinner countSpinner = new Spinner(group, SWT.BORDER);
			countSpinner.setLayoutData(getSpinnerData());
			countSpinner.setMinimum( 1 );
			countSpinner.setMaximum( 100 );
			countSpinner.setSelection( 1 );

			//----------------------------------------------------------------------
			Group options = new Group(dialog,SWT.SHADOW_ETCHED_IN);
			options.setLayout(new GridLayout());
			options.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
			options.setText(TuxGuitar.getProperty("options"));

			final Button replace = new Button(options,SWT.RADIO);
			replace.setText(TuxGuitar.getProperty("edit.paste.replace-mode"));
			replace.setSelection(true);

			final Button insert = new Button(options,SWT.RADIO);
			insert.setText(TuxGuitar.getProperty("edit.paste.insert-mode"));

			//------------------BUTTONS--------------------------
			Composite buttons = new Composite(dialog, SWT.NONE);
			buttons.setLayout(new GridLayout(2,false));
			buttons.setLayoutData(new GridData(SWT.END,SWT.FILL,true,true));

			final Button buttonOK = new Button(buttons, SWT.PUSH);
			buttonOK.setText(TuxGuitar.getProperty("ok"));
			buttonOK.setLayoutData(getButtonData());
			buttonOK.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					int pasteMode = 0;
					int pasteCount = countSpinner.getSelection();
					if( replace.getSelection() ){
						pasteMode = MeasureTransferable.TRANSFER_TYPE_REPLACE;
					}else if(insert.getSelection()){
						pasteMode = MeasureTransferable.TRANSFER_TYPE_INSERT;
					}
					pasteMeasures( pasteMode , pasteCount);

					dialog.dispose();
				}
			});

			Button buttonCancel = new Button(buttons, SWT.PUSH);
			buttonCancel.setText(TuxGuitar.getProperty("cancel"));
			buttonCancel.setLayoutData(getButtonData());
			buttonCancel.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					dialog.dispose();
				}
			});

			dialog.setDefaultButton( buttonOK );

			DialogUtils.openDialog(dialog,DialogUtils.OPEN_STYLE_CENTER | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);
		}
	}

	private GridData getSpinnerData(){
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
		data.minimumWidth = 150;
		return data;
	}

	private GridData getButtonData(){
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumWidth = 80;
		data.minimumHeight = 25;
		return data;
	}

	protected void pasteMeasures(int pasteMode, int pasteCount){
		try {
			if( pasteMode > 0 && pasteCount > 0 ){
				Object content = TuxGuitar.instance().getClipBoard().getContents(MeasureTransfer.getInstance());
				MeasureTransferable transferable = (MeasureTransferable) content;
				if(transferable != null){
					transferable.setTransferType( pasteMode );
					transferable.setPasteCount( pasteCount );

					transferable.insertTransfer(getEditor());

					updateTablature();
				} else {
					System.err.println("Unsupported clipboard content");
				}
			}
		} catch (CannotInsertTransferException ex) {
			ex.printStackTrace();
		}
	}
}
