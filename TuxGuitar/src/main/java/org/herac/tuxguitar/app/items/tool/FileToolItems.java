/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.file.NewFileAction;
import org.herac.tuxguitar.app.actions.file.OpenFileAction;
import org.herac.tuxguitar.app.actions.file.PrintAction;
import org.herac.tuxguitar.app.actions.file.PrintPreviewAction;
import org.herac.tuxguitar.app.actions.file.SaveAsFileAction;
import org.herac.tuxguitar.app.actions.file.SaveFileAction;
import org.herac.tuxguitar.app.items.ToolItems;
/**
 * @author julian
 */
public class FileToolItems extends ToolItems {
	public static final String NAME = "file.items";
	private ToolItem newSong;
	private ToolItem openSong;
	private ToolItem saveSong;
	private ToolItem saveAsSong;
	private ToolItem printSong;
	private ToolItem printPreviewSong;

	public FileToolItems(){
		super(NAME);
	}

	@Override
	public void showItems(ToolBar toolBar){
		this.newSong = new ToolItem(toolBar, SWT.PUSH);
		this.newSong.addSelectionListener(TuxGuitar.instance().getAction(NewFileAction.NAME));

		this.openSong = new ToolItem(toolBar, SWT.PUSH);
		this.openSong.addSelectionListener(TuxGuitar.instance().getAction(OpenFileAction.NAME));

		this.saveSong = new ToolItem(toolBar, SWT.PUSH);
		this.saveSong.addSelectionListener(TuxGuitar.instance().getAction(SaveFileAction.NAME));

		this.saveAsSong = new ToolItem(toolBar, SWT.PUSH);
		this.saveAsSong.addSelectionListener(TuxGuitar.instance().getAction(SaveAsFileAction.NAME));

		this.printSong = new ToolItem(toolBar, SWT.PUSH);
		this.printSong.addSelectionListener(TuxGuitar.instance().getAction(PrintAction.NAME));

		this.printPreviewSong = new ToolItem(toolBar, SWT.PUSH);
		this.printPreviewSong.addSelectionListener(TuxGuitar.instance().getAction(PrintPreviewAction.NAME));

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void update(){
		//Nothing to do
	}

	@Override
	public void loadProperties(){
		this.newSong.setToolTipText(TuxGuitar.getProperty("file.new"));
		this.openSong.setToolTipText(TuxGuitar.getProperty("file.open"));
		this.saveSong.setToolTipText(TuxGuitar.getProperty("file.save"));
		this.saveAsSong.setToolTipText(TuxGuitar.getProperty("file.save-as"));
		this.printSong.setToolTipText(TuxGuitar.getProperty("file.print"));
		this.printPreviewSong.setToolTipText(TuxGuitar.getProperty("file.print-preview"));
	}

	public void loadIcons(){
		this.newSong.setImage(TuxGuitar.instance().getIconManager().getFileNew());
		this.openSong.setImage(TuxGuitar.instance().getIconManager().getFileOpen());
		this.saveSong.setImage(TuxGuitar.instance().getIconManager().getFileSave());
		this.saveAsSong.setImage(TuxGuitar.instance().getIconManager().getFileSaveAs());
		this.printSong.setImage(TuxGuitar.instance().getIconManager().getFilePrint());
		this.printPreviewSong.setImage(TuxGuitar.instance().getIconManager().getFilePrintPreview());
	}
}
