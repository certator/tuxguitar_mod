/*
 * Created on 17-dic-2005
 */
package org.herac.tuxguitar.app.editors.chord;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.util.DialogUtils;
import org.herac.tuxguitar.graphics.control.TGChordImpl;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 * @author julian
 */
public class ChordDialog {
	public static final String NAME = "action.insert.chord";

	public static final int RESULT_SAVE = 1;
	public static final int RESULT_CLEAN = 2;
	public static final int RESULT_CANCEL = 3;

	private static final int DEFAULT_STYLE = SWT.BORDER;

	private Shell dialog;
	private TGChord chord;
	private ChordEditor editor;
	private ChordSelector selector;
	private ChordList list;
	private ChordRecognizer recognizer;
	//private boolean accepted;
	private int result;

	public ChordDialog() {
		super();
	}

	public int open(Shell shell,final TGMeasureImpl measure,TGBeat beat, long start) {
		this.setResult(RESULT_CANCEL);

		this.dialog = DialogUtils.newDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.dialog.setLayout(new GridLayout());
		this.dialog.setText(TuxGuitar.getProperty("chord.editor"));
		this.dialog.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				TuxGuitar.instance().getCustomChordManager().write();
			}
		});

		Composite topComposite = new Composite(this.dialog, SWT.NONE);
		topComposite.setLayout(new GridLayout(4,false));
		topComposite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));

		Composite bottomComposite = new Composite(this.dialog, SWT.NONE);
		bottomComposite.setLayout(new GridLayout());
		bottomComposite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));

		int[] tuning = findCurrentTuning(measure.getTrack());

		//---------------SELECTOR--------------------------------
		this.selector = new ChordSelector(this,topComposite,DEFAULT_STYLE, tuning);
		this.selector.pack();

		//---------------EDITOR--------------------------------
		this.editor = new ChordEditor(this, topComposite, DEFAULT_STYLE,(short)tuning.length);
		this.editor.pack();

		this.editor.setCurrentTrack(measure.getTrack());

		//---------------RECOGNIZER------------------------------------
		this.recognizer = new ChordRecognizer(this, topComposite, DEFAULT_STYLE);

		//---------------CUSTOM CHORDS---------------------------------
		new ChordCustomList(this, topComposite, DEFAULT_STYLE,Math.max(this.selector.getBounds().height,this.editor.getBounds().height));

		//---------------LIST--------------------------------
		Composite listComposite = new Composite(bottomComposite, SWT.NONE);
		listComposite.setLayout(gridLayout(1,false,0,0));
		listComposite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		this.list = new ChordList(this,listComposite,beat);

		//------------------BUTTONS--------------------------
		Composite buttons = new Composite(this.dialog, SWT.NONE);
		buttons.setLayout(gridLayout(3,false,0,0));
		buttons.setLayoutData(new GridData(SWT.RIGHT,SWT.FILL,true,true));

		final Button buttonOK = new Button(buttons, SWT.PUSH);
		buttonOK.setText(TuxGuitar.getProperty("ok"));
		buttonOK.setLayoutData(getButtonData());
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setChord(getEditor().getChord());
				setResult(RESULT_SAVE);
				getDialog().dispose();
			}
		});

		Button buttonClean = new Button(buttons, SWT.PUSH);
		buttonClean.setText(TuxGuitar.getProperty("clean"));
		buttonClean.setLayoutData(getButtonData());
		buttonClean.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setResult(RESULT_CLEAN);
				getDialog().dispose();
			}
		});

		Button buttonCancel = new Button(buttons, SWT.PUSH);
		buttonCancel.setText(TuxGuitar.getProperty("cancel"));
		buttonCancel.setLayoutData(getButtonData());
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getDialog().dispose();
			}
		});

		// load the current chord
		this.editor.setChord(findCurrentChord(measure, start));

		this.dialog.setDefaultButton( buttonOK );

		DialogUtils.openDialog(this.dialog,DialogUtils.OPEN_STYLE_CENTER | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);

		return getResult();
	}

	public ChordEditor getEditor() {
		return this.editor;
	}

	public ChordSelector getSelector() {
		return this.selector;
	}

	public ChordList getList() {
		return this.list;
	}

	public ChordRecognizer getRecognizer() {
		return this.recognizer;
	}

	public TGChord getChord() {
		return this.chord;
	}

	public void setChord(TGChord chord) {
		this.chord = chord;
	}

	public int getResult() {
		return this.result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public boolean isDisposed(){
		return this.dialog.isDisposed();
	}

	public Shell getDialog(){
		return this.dialog;
	}

	public GridLayout gridLayout(int numColumns,boolean makeColumnsEqualWidth,int marginWidth,int marginHeight){
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;
		layout.marginWidth = (marginWidth >= 0)?marginWidth:layout.marginWidth;
		layout.marginHeight = (marginHeight >= 0)?marginHeight:layout.marginHeight;
		return layout;
	}

	private GridData getButtonData(){
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumWidth = 80;
		data.minimumHeight = 25;
		return data;
	}

	private int[] findCurrentTuning(TGTrack track){
		int[] tuning = new int[track.stringCount()];
		Iterator<TGString> it = track.getStrings().iterator();
		while(it.hasNext()){
			TGString string = it.next();
			tuning[(tuning.length - string.getNumber())] = string.getValue();
		}
		return tuning;
	}

	protected TGChord findCurrentChord(TGMeasure measure, long start){
		TGSongManager manager = TuxGuitar.instance().getSongManager();
		TGChord chord = manager.getMeasureManager().getChord(measure, start);
		if(chord == null){
			chord = manager.getFactory().newChord(measure.getTrack().stringCount());
			chord.setFirstFret(1);
			List<TGNote> notes = manager.getMeasureManager().getNotes(measure, start);
			if(!notes.isEmpty()){
				int maxValue = -1;
				int minValue = -1;

				//verifico el first fret
				Iterator<TGNote> it = notes.iterator();
				while(it.hasNext()){
					TGNote note = it.next();
					if(maxValue < 0 || maxValue < note.getValue()){
						maxValue = note.getValue();
					}
					if(note.getValue() > 0 && (minValue < 0 || minValue > note.getValue())){
						minValue = note.getValue();
					}
				}
				if(maxValue > TGChordImpl.MAX_FRETS  && minValue > 0){
					chord.setFirstFret((short)(minValue));
				}

				//agrego los valores
				it = notes.iterator();
				while(it.hasNext()){
					TGNote note = it.next();
					chord.addFretValue( ( note.getString() - 1) , note.getValue());
				}
			}
		}
		return chord;
	}
}