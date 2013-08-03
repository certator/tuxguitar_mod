/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.actions.insert.InsertChordAction;
import org.herac.tuxguitar.app.actions.note.ChangeTiedNoteAction;
import org.herac.tuxguitar.app.items.ToolItems;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGNote;

/**
 * @author julian
 */
public class BeatToolItems  extends ToolItems{
	public static final String NAME = "beat.items";

	protected ToolBar toolBar;
	private ToolItem tiedNote;
	private ChordMenuItem chordItems;

	public BeatToolItems(){
		super(NAME);
	}

	@Override
	public void showItems(ToolBar toolBar){
		this.toolBar = toolBar;

		this.tiedNote = new ToolItem(toolBar, SWT.CHECK);
		this.tiedNote.addSelectionListener(TuxGuitar.instance().getAction(ChangeTiedNoteAction.NAME));

		this.chordItems = new ChordMenuItem();
		this.chordItems.addItems();

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void update(){
		TGNote note = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getSelectedNote();
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.tiedNote.setEnabled(!running);
		this.tiedNote.setSelection(note != null && note.isTiedNote());
		this.chordItems.setEnabled(!running);
		this.chordItems.update();
	}

	@Override
	public void loadProperties(){
		this.tiedNote.setToolTipText(TuxGuitar.getProperty("note.tiednote"));
		this.chordItems.setToolTipText(TuxGuitar.getProperty("insert.chord"));
	}

	public void loadIcons(){
		this.tiedNote.setImage(TuxGuitar.instance().getIconManager().getNoteTied());
		this.chordItems.setImage(TuxGuitar.instance().getIconManager().getChord());
	}

	private class ChordMenuItem extends SelectionAdapter {
		private long lastEdit;
		private ToolItem item;
		private Menu subMenu;
		private MenuItem[] subMenuItems;

		public ChordMenuItem() {
			this.item = new ToolItem(BeatToolItems.this.toolBar, SWT.DROP_DOWN);
			this.item.addSelectionListener(this);
			this.subMenu = new Menu(this.item.getParent().getShell());
		}

		public void setToolTipText(String text){
			this.item.setToolTipText(text);
		}

		public void setEnabled(boolean enabled){
			this.item.setEnabled(enabled);
		}

		public void setImage(Image image){
			this.item.setImage(image);
		}

		public void addItems() {
			this.disposeItems();
			this.subMenuItems = new MenuItem[TuxGuitar.instance().getCustomChordManager().countChords()];
			for(int i = 0;i < this.subMenuItems.length; i++){
				TGChord chord = TuxGuitar.instance().getCustomChordManager().getChord(i);
				ActionData actionData = new ActionData();
				actionData.put(InsertChordAction.PROPERTY_CHORD, chord);

				this.subMenuItems[i] = new MenuItem(this.subMenu, SWT.PUSH);
				this.subMenuItems[i].setData(actionData);
				this.subMenuItems[i].setText(chord.getName());
				this.subMenuItems[i].addSelectionListener(TuxGuitar.instance().getAction(InsertChordAction.NAME));
			}
		}

		public void disposeItems() {
			if(this.subMenuItems != null){
				for(int i = 0;i < this.subMenuItems.length; i++){
					this.subMenuItems[i].dispose();
				}
			}
		}

		@Override
		public void widgetSelected(SelectionEvent event) {
			if (event.detail == SWT.ARROW && this.subMenuItems != null && this.subMenuItems.length > 0) {
				ToolItem item = (ToolItem) event.widget;
				Rectangle rect = item.getBounds();
				Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
				this.subMenu.setLocation(pt.x, pt.y + rect.height);
				this.subMenu.setVisible(true);
			}else{
				TuxGuitar.instance().getAction(InsertChordAction.NAME).process(new ActionData());
			}
		}

		public void update(){
			if(this.lastEdit != TuxGuitar.instance().getCustomChordManager().getLastEdit()){
				this.addItems();
				this.lastEdit = TuxGuitar.instance().getCustomChordManager().getLastEdit();
			}
		}
	}
}
