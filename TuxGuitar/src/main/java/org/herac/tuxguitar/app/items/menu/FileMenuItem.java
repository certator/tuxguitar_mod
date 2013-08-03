/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.menu;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.actions.file.ExitAction;
import org.herac.tuxguitar.app.actions.file.ExportSongAction;
import org.herac.tuxguitar.app.actions.file.ImportSongAction;
import org.herac.tuxguitar.app.actions.file.NewFileAction;
import org.herac.tuxguitar.app.actions.file.OpenFileAction;
import org.herac.tuxguitar.app.actions.file.OpenURLAction;
import org.herac.tuxguitar.app.actions.file.PrintAction;
import org.herac.tuxguitar.app.actions.file.PrintPreviewAction;
import org.herac.tuxguitar.app.actions.file.SaveAsFileAction;
import org.herac.tuxguitar.app.actions.file.SaveFileAction;
import org.herac.tuxguitar.app.items.MenuItems;
import org.herac.tuxguitar.app.tools.template.TGTemplate;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.io.base.TGLocalFileExporter;
import org.herac.tuxguitar.io.base.TGLocalFileImporter;
import org.herac.tuxguitar.io.base.TGRawExporter;
import org.herac.tuxguitar.io.base.TGRawImporter;

/**
 * @author julian
 */
public class FileMenuItem extends MenuItems {

	private final MenuItem fileMenuItem;
	private final Menu menu;
	private Menu newSongMenu;
	private Menu importMenu;
	private Menu exportMenu;
	private Menu historyMenu;
	private MenuItem newSong;
	private MenuItem newSongDefault;
	private MenuItem open;
	private MenuItem openURL;
	private MenuItem save;
	private MenuItem saveAs;
	private MenuItem importItem;
	private MenuItem exportItem;
	private MenuItem printPreview;
	private MenuItem print;
	private MenuItem historyItem;
	private MenuItem[] historyFiles;
	private MenuItem exit;

	private final List<MenuItem> importItems;
	private final List<MenuItem> exportItems;

	public FileMenuItem(Shell shell,Menu parent, int style) {
		this.fileMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
		this.importItems = new ArrayList<MenuItem>();
		this.exportItems = new ArrayList<MenuItem>();
	}

	@Override
	public void showItems(){
		//---------------------------------------------------
		//--NEW--
		this.newSong = new MenuItem(this.menu, SWT.CASCADE);
		this.newSong.addSelectionListener(TuxGuitar.instance().getAction(NewFileAction.NAME));
		this.newSongMenu = new Menu(this.menu.getShell(), SWT.DROP_DOWN);
		this.newSongDefault = new MenuItem(this.newSongMenu, SWT.PUSH);
		this.newSongDefault.addSelectionListener(TuxGuitar.instance().getAction(NewFileAction.NAME));

		this.addNewSongTemplates();

		//--OPEN--
		this.open = new MenuItem(this.menu, SWT.PUSH);
		this.open.addSelectionListener(TuxGuitar.instance().getAction(OpenFileAction.NAME));
		//--OPEN--
		this.openURL = new MenuItem(this.menu, SWT.PUSH);
		this.openURL.addSelectionListener(TuxGuitar.instance().getAction(OpenURLAction.NAME));
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		//--SAVE--
		this.save = new MenuItem(this.menu, SWT.PUSH);
		this.save.addSelectionListener(TuxGuitar.instance().getAction(SaveFileAction.NAME));
		//--SAVE AS--
		this.saveAs = new MenuItem(this.menu, SWT.PUSH);
		this.saveAs.addSelectionListener(TuxGuitar.instance().getAction(SaveAsFileAction.NAME));

		//-- IMPORT | EXPORT --
		int countImporters = TGFileFormatManager.instance().countImporters();
		int countExporters = TGFileFormatManager.instance().countExporters();
		if( ( countImporters + countExporters ) > 0 ){
			//--SEPARATOR--
			new MenuItem(this.menu, SWT.SEPARATOR);

			//--IMPORT--
			this.importItems.clear();
			if( countImporters > 0 ){
				this.importItem = new MenuItem(this.menu,SWT.CASCADE);
				this.importMenu = new Menu(this.menu.getShell(), SWT.DROP_DOWN);
				this.addImporters();
			}

			//--EXPORT--
			this.exportItems.clear();
			if( countExporters > 0 ){
				this.exportItem = new MenuItem(this.menu,SWT.CASCADE);
				this.exportMenu = new Menu(this.menu.getShell(), SWT.DROP_DOWN);
				this.addExporters();
			}
		}

		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);

		//--PRINT PREVIEW--
		this.printPreview = new MenuItem(this.menu, SWT.PUSH);
		this.printPreview.addSelectionListener(TuxGuitar.instance().getAction(PrintPreviewAction.NAME));
		//--PRINT--
		this.print = new MenuItem(this.menu, SWT.PUSH);
		this.print.addSelectionListener(TuxGuitar.instance().getAction(PrintAction.NAME));
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);

		//--HISTORY--
		this.historyItem = new MenuItem(this.menu,SWT.CASCADE);
		this.historyMenu = new Menu(this.menu.getShell(), SWT.DROP_DOWN);
		this.updateHistoryFiles();
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		//--EXIT--
		this.exit = new MenuItem(this.menu, SWT.PUSH);
		this.exit.addSelectionListener(TuxGuitar.instance().getAction(ExitAction.NAME));

		//---------------------------------------------------
		if( this.importItem != null ){
			this.importItem.setMenu(this.importMenu);
		}
		if( this.exportItem != null ){
			this.exportItem.setMenu(this.exportMenu);
		}
		this.newSong.setMenu(this.newSongMenu);
		this.historyItem.setMenu(this.historyMenu);
		this.fileMenuItem.setMenu(this.menu);

		this.loadIcons();
		this.loadProperties();
	}

	private void addNewSongTemplates(){
		if( TuxGuitar.instance().getTemplateManager().countTemplates() > 0 ){
			//--SEPARATOR--
			new MenuItem(this.newSongMenu, SWT.SEPARATOR);

			Iterator<TGTemplate> it = TuxGuitar.instance().getTemplateManager().getTemplates();
			while( it.hasNext() ){
				TGTemplate tgTemplate = it.next();

				ActionData actionData = new ActionData();
				actionData.put(NewFileAction.PROPERTY_TEMPLATE, tgTemplate);

				MenuItem menuItem = new MenuItem(this.newSongMenu, SWT.PUSH);
				menuItem.setText(tgTemplate.getName());
				menuItem.setData(actionData);
				menuItem.addSelectionListener(TuxGuitar.instance().getAction(NewFileAction.NAME));
			}
		}
	}

	private void addImporters(){
		List<TGRawImporter> importersRaw = new ArrayList<TGRawImporter>();
		List<TGRawImporter> importersFile = new ArrayList<TGRawImporter>();

		Iterator<TGRawImporter> importers = TGFileFormatManager.instance().getImporters();
		while(importers.hasNext()){
			TGRawImporter importer = importers.next();
			if( importer instanceof TGLocalFileImporter ){
				importersFile.add( importer );
			}else{
				importersRaw.add( importer );
			}
		}

		Comparator<TGRawImporter> comparator = new Comparator<TGRawImporter>() {
			@Override
			public int compare(TGRawImporter a, TGRawImporter b) {
				return a.getImportName().compareToIgnoreCase(b.getImportName());
			}
		};
		Collections.sort(importersRaw, comparator);
		Collections.sort(importersFile, comparator);

		for( int i = 0 ; i < importersFile.size() ; i ++ ){
			ActionData actionData = new ActionData();
			actionData.put(ImportSongAction.PROPERTY_IMPORTER, importersFile.get( i ));

			MenuItem item = new MenuItem(this.importMenu, SWT.PUSH);
			item.setData(actionData);
			item.addSelectionListener(TuxGuitar.instance().getAction(ImportSongAction.NAME));
			this.importItems.add( item );
		}

		//--SEPARATOR--
		if( !importersFile.isEmpty() && !importersRaw.isEmpty() ){
			new MenuItem(this.importMenu, SWT.SEPARATOR);
		}

		for( int i = 0 ; i < importersRaw.size() ; i ++ ){
			ActionData actionData = new ActionData();
			actionData.put(ImportSongAction.PROPERTY_IMPORTER, importersRaw.get( i ));

			MenuItem item = new MenuItem(this.importMenu, SWT.PUSH);
			item.setData(actionData);
			item.addSelectionListener(TuxGuitar.instance().getAction(ImportSongAction.NAME));
			this.importItems.add( item );
		}
	}

	private void addExporters(){
		List<TGRawExporter> exportersRaw = new ArrayList<TGRawExporter>();
		List<TGRawExporter> exportersFile = new ArrayList<TGRawExporter>();

		Iterator<TGRawExporter> exporters = TGFileFormatManager.instance().getExporters();
		while(exporters.hasNext()){
			TGRawExporter exporter = exporters.next();
			if( exporter instanceof TGLocalFileExporter ){
				exportersFile.add( exporter );
			}else{
				exportersRaw.add( exporter );
			}
		}

		Comparator<TGRawExporter> comparator = new Comparator<TGRawExporter>() {
			@Override
			public int compare(TGRawExporter a, TGRawExporter b) {
				return a.getExportName().compareToIgnoreCase(b.getExportName());
			}
		};
		Collections.sort(exportersRaw, comparator);
		Collections.sort(exportersFile, comparator);

		for( int i = 0 ; i < exportersFile.size() ; i ++ ){
			ActionData actionData = new ActionData();
			actionData.put(ExportSongAction.PROPERTY_EXPORTER, exportersFile.get( i ));

			MenuItem item = new MenuItem(this.exportMenu, SWT.PUSH);
			item.setData(actionData);
			item.addSelectionListener(TuxGuitar.instance().getAction(ExportSongAction.NAME));
			this.exportItems.add( item );
		}

		//--SEPARATOR--
		if( !exportersFile.isEmpty() && !exportersRaw.isEmpty() ){
			new MenuItem(this.exportMenu, SWT.SEPARATOR);
		}

		for( int i = 0 ; i < exportersRaw.size() ; i ++ ){
			ActionData actionData = new ActionData();
			actionData.put(ExportSongAction.PROPERTY_EXPORTER, exportersRaw.get( i ));

			MenuItem item = new MenuItem(this.exportMenu, SWT.PUSH);
			item.setData(actionData);
			item.addSelectionListener(TuxGuitar.instance().getAction(ExportSongAction.NAME));
			this.exportItems.add( item );
		}
	}

	private void disposeHistoryFiles(){
		for(int i = 0;i < this.historyFiles.length; i++){
			this.historyFiles[i].dispose();
		}
	}

	private void updateHistoryFiles(){
		List<URL> urls = TuxGuitar.instance().getFileHistory().getURLs();
		this.historyFiles = new MenuItem[urls.size()];
		for(int i = 0;i < this.historyFiles.length; i++){
			URL url = urls.get(i);
			ActionData actionData = new ActionData();
			actionData.put(OpenFileAction.PROPERTY_URL, url);
			this.historyFiles[i] = new MenuItem(this.historyMenu, SWT.PUSH);
			this.historyFiles[i].setText(decode(url.toString()));
			this.historyFiles[i].setData(actionData);
			this.historyFiles[i].addSelectionListener(TuxGuitar.instance().getAction(OpenFileAction.NAME));
		}
		this.historyItem.setEnabled(this.historyFiles.length > 0);
	}

	private String decode(String url){
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	@Override
	public void update(){
		if(TuxGuitar.instance().getFileHistory().isChanged()){
			disposeHistoryFiles();
			updateHistoryFiles();
			TuxGuitar.instance().getFileHistory().setChanged(false);
		}
	}

	@Override
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.fileMenuItem, "file", null);
		setMenuItemTextAndAccelerator(this.newSong, "file.new", null);
		setMenuItemTextAndAccelerator(this.newSongDefault, "file.new-song.default-template", NewFileAction.NAME);
		setMenuItemTextAndAccelerator(this.open, "file.open", OpenFileAction.NAME);
		setMenuItemTextAndAccelerator(this.openURL, "file.open-url", OpenURLAction.NAME);
		setMenuItemTextAndAccelerator(this.save, "file.save", SaveFileAction.NAME);
		setMenuItemTextAndAccelerator(this.saveAs, "file.save-as", SaveAsFileAction.NAME);
		setMenuItemTextAndAccelerator(this.printPreview, "file.print-preview", PrintPreviewAction.NAME);
		setMenuItemTextAndAccelerator(this.print, "file.print", PrintAction.NAME);
		setMenuItemTextAndAccelerator(this.historyItem, "file.history", null);
		setMenuItemTextAndAccelerator(this.exit, "file.exit", ExitAction.NAME);

		if( this.importItem != null ){
			setMenuItemTextAndAccelerator(this.importItem, "file.import", ImportSongAction.NAME);

			Iterator<MenuItem> importItems = this.importItems.iterator();
			while(importItems.hasNext()){
				MenuItem item = importItems.next();

				Object itemImporter = ((ActionData)item.getData()).get(ImportSongAction.PROPERTY_IMPORTER);
				if( itemImporter instanceof TGLocalFileImporter ){
					item.setText(TuxGuitar.getProperty("file.import") + " " + ((TGRawImporter)itemImporter).getImportName());
				}else if( itemImporter instanceof TGRawImporter ){
					item.setText(((TGRawImporter)itemImporter).getImportName());
				}
			}
		}
		if( this.exportItem != null ){
			setMenuItemTextAndAccelerator(this.exportItem, "file.export", ExportSongAction.NAME);

			Iterator<MenuItem> exportItems = this.exportItems.iterator();
			while(exportItems.hasNext()){
				MenuItem item = exportItems.next();

				Object itemExporter = ((ActionData)item.getData()).get(ExportSongAction.PROPERTY_EXPORTER);
				if( itemExporter instanceof TGLocalFileExporter ){
					item.setText(TuxGuitar.getProperty("file.export") + " " + ((TGRawExporter)itemExporter).getExportName());
				}else if( itemExporter instanceof TGRawExporter ){
					item.setText(((TGRawExporter)itemExporter).getExportName());
				}
			}
		}
	}

	public void loadIcons(){
		this.newSong.setImage(TuxGuitar.instance().getIconManager().getFileNew());
		this.open.setImage(TuxGuitar.instance().getIconManager().getFileOpen());
		this.save.setImage(TuxGuitar.instance().getIconManager().getFileSave());
		this.saveAs.setImage(TuxGuitar.instance().getIconManager().getFileSaveAs());
		this.printPreview.setImage(TuxGuitar.instance().getIconManager().getFilePrintPreview());
		this.print.setImage(TuxGuitar.instance().getIconManager().getFilePrint());
	}
}