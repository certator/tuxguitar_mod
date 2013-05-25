package org.herac.tuxguitar.jack.sequencer;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.player.base.MidiPlayerListener;

public class JackMidiPlayerStarter implements MidiPlayerListener{
	
	private JackSequencer jackSequencer;
	private boolean countDownByPass;
	
	public JackMidiPlayerStarter(JackSequencer jackSequencer){
		this.jackSequencer = jackSequencer;
	}
	
	private MidiPlayer getPlayer(){
		return TuxGuitar.instance().getPlayer();
	}
	
	public void open(){
		getPlayer().addListener(this);
	}
	
	public void close(){
		getPlayer().removeListener(this);
	}
	
	public void start(){
		TuxGuitar.instance().getTransport().play();
	}
	
	@Override
	public void notifyCountDownStarted() {
		this.countDownByPass = (this.jackSequencer.getJackClient().isTransportRunning() && getPlayer().getCountDown().isEnabled());
		if( this.countDownByPass ){
			this.getPlayer().getCountDown().setEnabled( false );
		}
	}
	
	@Override
	public void notifyCountDownStopped() {
		if( this.countDownByPass ){
			this.countDownByPass = false;
			this.getPlayer().getCountDown().setEnabled( true );
		}
	}
	
	@Override
	public void notifyStarted() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void notifyStopped() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void notifyLoop() {
		// TODO Auto-generated method stub
	}
}
