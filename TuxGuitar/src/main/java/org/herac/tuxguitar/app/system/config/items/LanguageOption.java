package org.herac.tuxguitar.app.system.config.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.helper.SyncThread;
import org.herac.tuxguitar.app.system.config.TGConfigEditor;
import org.herac.tuxguitar.app.system.config.TGConfigKeys;

public class LanguageOption extends Option{
	protected boolean initialized;
	protected Table table;
	protected TableColumn column;

	public LanguageOption(TGConfigEditor configEditor,ToolBar toolBar,final Composite parent){
		super(configEditor,toolBar,parent,TuxGuitar.getProperty("settings.config.language"), SWT.FILL, SWT.FILL);
		this.initialized = false;
	}

	@Override
	public void createOption(){
		getToolItem().setText(TuxGuitar.getProperty("settings.config.language"));
		getToolItem().setImage(TuxGuitar.instance().getIconManager().getOptionLanguage());
		getToolItem().addSelectionListener(this);

		showLabel(getComposite(),SWT.FILL,SWT.TOP, true, false,SWT.TOP | SWT.LEFT | SWT.WRAP,SWT.BOLD,0,TuxGuitar.getProperty("settings.config.language.choose"));

		Composite composite = new Composite(getComposite(),SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(getTabbedData(SWT.FILL, SWT.FILL));

		this.table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		this.table.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(false);

		this.column = new TableColumn(this.table, SWT.LEFT);
		this.column.setText(TuxGuitar.getProperty("settings.config.language.choose"));
		this.column.pack();

		this.loadConfig();
	}

	protected void loadTableItem(String text, String data, boolean selected){
		TableItem item = new TableItem(this.table, SWT.NONE);
		item.setText(text);
		item.setData(data);
		if( selected ){
			this.table.setSelection(item);
		}
	}

	protected List<LanguageItem> getLanguageItems(String[] languages){
		List<LanguageItem> list = new ArrayList<LanguageItem>();
		if( languages != null ){
			for(int i = 0;i < languages.length; i ++){
				list.add( new LanguageItem(languages[i],TuxGuitar.getProperty("locale." + languages[i] ) ) );
			}
			Collections.sort(list, new Comparator<LanguageItem>() {
				@Override
				public int compare(LanguageItem l1, LanguageItem l2) {
					return l1.getValue().compareTo( l2.getValue() );
				}
			} );
		}
		return list;
	}

	protected void loadConfig(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String language = getConfig().getStringConfigValue(TGConfigKeys.LANGUAGE);
				final List<LanguageItem> languages = getLanguageItems( TuxGuitar.instance().getLanguageManager().getLanguages() );
				new SyncThread(new Runnable() {
					@Override
					public void run() {
						if(!isDisposed()){
							// Load default item
							loadTableItem(TuxGuitar.getProperty("locale.default"), new String(), true);

							for(int i = 0;i < languages.size(); i ++){
								LanguageItem item = languages.get( i );
								loadTableItem(item.getValue(),item.getKey(),(language != null && item.getKey().equals( language )));
							}

							LanguageOption.this.initialized = true;
							LanguageOption.this.column.pack();
							LanguageOption.this.pack();
						}
					}
				}).start();
			}
		}).start();
	}

	@Override
	public void updateConfig(){
		if(this.initialized){
			String language = null;
			if(this.table != null && !this.table.isDisposed()){
				int index = this.table.getSelectionIndex();
				if(index >= 0 && index < this.table.getItemCount() ){
					language = (String)this.table.getItem(index).getData();
				}
			}
			getConfig().setProperty(TGConfigKeys.LANGUAGE, language );
		}
	}

	@Override
	public void updateDefaults(){
		if(this.initialized){
			getConfig().setProperty(TGConfigKeys.LANGUAGE,getDefaults().getProperty(TGConfigKeys.LANGUAGE));
		}
	}

	@Override
	public void applyConfig(boolean force){
		if(force || this.initialized){
			boolean changed = force;

			if(!changed){
				String languageLoaded = TuxGuitar.instance().getLanguageManager().getLanguage();
				String languageConfigured = getConfig().getStringConfigValue(TGConfigKeys.LANGUAGE);
				if(languageLoaded == null && languageConfigured == null){
					changed = false;
				}
				else if(languageLoaded != null && languageConfigured != null){
					changed = ( !languageLoaded.equals( languageConfigured ) );
				}
				else {
					changed = true;
				}
			}

			if(changed){
				addSyncThread(new Runnable() {
					@Override
					public void run() {
						TuxGuitar.instance().loadLanguage();
					}
				});
			}
		}
	}

	@Override
	public Point computeSize(){
		return this.computeSize(SWT.DEFAULT,SWT.NONE);
	}

	private class LanguageItem {
		private final String key;
		private final String value;

		public LanguageItem(String key, String value){
			this.key = key;
			this.value = value;
		}

		public String getKey(){
			return this.key;
		}

		public String getValue(){
			return this.value;
		}
	}
}