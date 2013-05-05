package org.herac.tuxguitar.app.tracktable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

class TrackTable {
	private ScrolledComposite sComposite;
	private Composite table;
	private SashForm columnControl;
	private Composite rowControl;
	private TrackTableColumn columnNumber;
	private TrackTableColumn columnSoloMute;
	private TrackTableColumn columnName;
	private TrackTableColumn columnInstrument;
	private TrackTableColumn columnCanvas;
	private final List<TrackTableRow> rows;
	private int rowHeight;
	private int scrollIncrement;
	
	public TrackTable(Composite parent){
		this.rows = new ArrayList<TrackTableRow>();
		this.newTable(parent);
	}
	
	public void newTable(Composite parent){
		this.sComposite = new ScrolledComposite(parent,SWT.BORDER | SWT.V_SCROLL);
		this.sComposite.setLayout(new GridLayout());
		this.sComposite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		this.sComposite.setAlwaysShowScrollBars(true);
		this.sComposite.setExpandHorizontal(true);
		this.sComposite.setExpandVertical(true);
		this.table = new Composite(this.sComposite,SWT.NONE);
		this.table.setLayout(newGridLayout(1,0,0,0,0));
		this.table.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		this.columnControl = new SashForm(this.table,SWT.HORIZONTAL);
		this.columnControl.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false));
		
		this.columnNumber = new TrackTableColumn(this,SWT.LEFT);
		this.columnSoloMute = new TrackTableColumn(this,SWT.LEFT);
		this.columnName = new TrackTableColumn(this,SWT.LEFT);
		this.columnInstrument = new TrackTableColumn(this,SWT.LEFT);
		this.columnCanvas = new TrackTableColumn(this,SWT.CENTER);
		this.columnControl.setWeights(new int[]{1,2,7,7,20});
		
		this.rowControl = new Composite(this.table,SWT.NONE);
		this.rowControl.setLayout(newGridLayout(1,0,1,0,1));
		this.rowControl.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		this.sComposite.setContent(this.table);
	}
	
	public Composite getControl(){
		return this.table;
	}
	
	public TrackTableRow newRow(){
		TrackTableRow row = new TrackTableRow(this);
		this.rows.add(row);
		return row;
	}
	
	private GridLayout newGridLayout(int cols,int marginWidth,int marginHeight,int horizontalSpacing,int verticalSpacing){
		GridLayout layout = new GridLayout(cols,false);
		layout.marginWidth = marginWidth;
		layout.marginHeight = marginHeight;
		layout.horizontalSpacing = horizontalSpacing;
		layout.verticalSpacing = verticalSpacing;
		return layout;
	}
	
	public void addRowItem(TrackTableColumn column,Control control,boolean computeSize){
		if(computeSize){
			this.rowHeight = Math.max(this.rowHeight,control.computeSize(SWT.DEFAULT,SWT.DEFAULT).y);
			this.scrollIncrement = this.rowHeight;
		}
		column.addControl(control);
	}
	
	public int getMinHeight(){
		return (this.sComposite.getMinHeight() + ( this.sComposite.getBorderWidth() * 2 ) );
	}
	
	public Composite getColumnControl(){
		return this.columnControl;
	}
	
	public Composite getRowControl(){
		return this.rowControl;
	}
	
	public int getRowHeight(){
		return this.rowHeight;
	}
	
	public int getScrollIncrement(){
		return this.scrollIncrement;
	}	
	
	public TrackTableColumn getColumnInstrument() {
		return this.columnInstrument;
	}
	
	public TrackTableColumn getColumnName() {
		return this.columnName;
	}
	
	public TrackTableColumn getColumnNumber() {
		return this.columnNumber;
	}
	
	public TrackTableColumn getColumnSoloMute() {
		return this.columnSoloMute;
	}
	
	public TrackTableColumn getColumnCanvas() {
		return this.columnCanvas;
	}
	
	public TrackTableRow getRow(int index){
		if(index >= 0 && index < this.rows.size()){
			return this.rows.get(index);
		}
		return null;
	}
	
	public void removeRowsAfter(int index){
		while(index < this.rows.size()){
			TrackTableRow row = this.rows.get(index);
			row.dispose();
			this.rows.remove(index);
		}
		this.notifyRemoved();
	}
	
	public int getRowCount(){
		return this.rows.size();
	}
	
	public void update(){
		this.layoutColumns();
		this.table.layout(true,true);
		this.sComposite.setMinHeight(this.table.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		this.sComposite.getVerticalBar().setIncrement( (getScrollIncrement() + this.sComposite.getBorderWidth() ) );
	}
	
	private void notifyRemoved(){
		this.columnNumber.notifyRemoved();
		this.columnSoloMute.notifyRemoved();
		this.columnName.notifyRemoved();
		this.columnInstrument.notifyRemoved();
		this.columnCanvas.notifyRemoved();
	}
	
	private void layoutColumns(){
		this.columnNumber.layout();
		this.columnSoloMute.layout();
		this.columnName.layout();
		this.columnInstrument.layout();
		this.columnCanvas.layout();
	}
	
}
