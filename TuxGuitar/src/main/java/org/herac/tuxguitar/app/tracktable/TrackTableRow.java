package org.herac.tuxguitar.app.tracktable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGTrack;

class TrackTableRow {
	private final TrackTable table;
	private TGTrack track;
	private Composite row;
	private CLabel number;
	private CLabel soloMute;
	private CLabel name;
	private CLabel instrument;
	private TrackCanvas canvas;

	public TrackTableRow(TrackTable table){
		this.table = table;
		this.track = null;
		this.init();
	}

	private void init(){
		this.row = new Composite(this.table.getRowControl(),SWT.NONE );
		this.row.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false));

		this.number = new CLabel(this.row,SWT.LEFT);
		this.table.addRowItem(this.table.getColumnNumber(),this.number,true);

		this.soloMute = new CLabel(this.row,SWT.LEFT);
		this.table.addRowItem(this.table.getColumnSoloMute(),this.soloMute,true);

		this.name = new CLabel(this.row,SWT.LEFT);
		this.table.addRowItem(this.table.getColumnName(),this.name,true);

		this.instrument = new CLabel(this.row,SWT.LEFT);
		this.table.addRowItem(this.table.getColumnInstrument(),this.instrument,true);

		this.canvas = new TrackCanvas(this.row, this.track);
		this.table.addRowItem(this.table.getColumnCanvas(),this.canvas,false);

		this.row.pack();
	}

	public void setBackground(Color background){
		this.number.setBackground(background);
		this.soloMute.setBackground(background);
		this.name.setBackground(background);
		this.instrument.setBackground(background);
	}

	public void setForeground(Color foreground){
		this.number.setForeground(foreground);
		this.soloMute.setForeground(foreground);
		this.name.setForeground(foreground);
		this.instrument.setForeground(foreground);
	}

	public void dispose(){
		this.row.dispose();
	}

	public TrackCanvas getCanvas() {
		return this.canvas;
	}

	public CLabel getInstrument() {
		return this.instrument;
	}

	public CLabel getName() {
		return this.name;
	}

	public CLabel getNumber() {
		return this.number;
	}

	public CLabel getSoloMute() {
		return this.soloMute;
	}

	public void addMouseListenerLabel(MouseListener mouseListenerLabel) {
		this.number.addMouseListener(mouseListenerLabel);
		this.soloMute.addMouseListener(mouseListenerLabel);
		this.name.addMouseListener(mouseListenerLabel);
		this.instrument.addMouseListener(mouseListenerLabel);
	}

	public void addMouseListenerCanvas(MouseListener mouseListenerCanvas) {
		this.canvas.addMouseListener(mouseListenerCanvas);
	}

	public void setMenu(Menu menu) {
		number.setMenu(menu);
		soloMute.setMenu(menu);
		name.setMenu(menu);
		instrument.setMenu(menu);
	}

	public void setTrack(TGTrack track) {
		this.track = track;

		//Number
		number.setText(Integer.toString(track.getNumber()));
		number.setData(track);

		//Solo-Mute
		soloMute.setText(getSoloMute(track));
		soloMute.setData(track);
		
		//Name
		name.setText(track.getName());
		name.setData(track);
		
		//Instrument
		instrument.setText(getInstrument(track));
		instrument.setData(track);
		
		canvas.setTrack(track);
	}

	private static String getInstrument(TGTrack track){
		TGChannel channel = TuxGuitar.instance().getSongManager().getChannel(track.getChannelId());
		if( channel != null ){
			return ( channel.getName() != null ? channel.getName() : new String() );
		}
		return new String();
	}
	
	private static String getSoloMute(TGTrack track){
		if( track.isSolo() ){
			return TuxGuitar.getProperty("track.short-solo-mute.s");
		}
		if( track.isMute() ){
			return TuxGuitar.getProperty("track.short-solo-mute.m");
		}
		return TuxGuitar.getProperty("track.short-solo-mute.none");
	}

	public void resetTextsValues() {
		number.setText(Integer.toString(track.getNumber()));
		soloMute.setText(getSoloMute(track));
		name.setText(track.getName());
		instrument.setText(getInstrument(track));
	}

	public TGTrack getTrack() {
		return track;
	}

}
