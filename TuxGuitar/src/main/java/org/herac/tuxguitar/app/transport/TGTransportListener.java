package org.herac.tuxguitar.app.transport;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.player.base.MidiPlayerListener;
import org.herac.tuxguitar.util.TGSynchronizer;

public class TGTransportListener implements MidiPlayerListener{

	protected Object sync;
	protected TGSynchronizer.TGRunnable startedRunnable;
	protected TGSynchronizer.TGRunnable stoppedRunnable;

	public TGTransportListener(){
		this.sync = new Object();
		this.startedRunnable = getStartedRunnable();
		this.stoppedRunnable = getStoppedRunnable();
	}

	@Override
	public void notifyStarted() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TuxGuitar.instance().updateCache(true);
					while (TuxGuitar.instance().getPlayer().isRunning()) {
						synchronized( TGTransportListener.this.sync ){
							TGSynchronizer.instance().addRunnable( TGTransportListener.this.startedRunnable );
							TGTransportListener.this.sync.wait(25);
						}
					}
					TGTransportListener.this.notifyStopped();
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void notifyStopped() {
		try {
			if(!TuxGuitar.instance().getDisplay().isDisposed()){
				TGSynchronizer.instance().runLater( TGTransportListener.this.stoppedRunnable );
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	@Override
	public void notifyLoop(){
		// Not implemented
	}

	private TGSynchronizer.TGRunnable getStartedRunnable(){
		return new TGSynchronizer.TGRunnable() {
			@Override
			public void run() {
				if(TuxGuitar.instance().getPlayer().isRunning()){
					TuxGuitar.instance().redrawPlayingMode();
				}
			}
		};
	}

	private TGSynchronizer.TGRunnable getStoppedRunnable(){
		return new TGSynchronizer.TGRunnable() {
			@Override
			public void run() {
				TuxGuitar.instance().getTransport().gotoPlayerPosition();
			}
		};
	}

	@Override
	public void notifyCountDownStarted() {
		// Not implemented
	}

	@Override
	public void notifyCountDownStopped() {
		// Not implemented
	}
}
