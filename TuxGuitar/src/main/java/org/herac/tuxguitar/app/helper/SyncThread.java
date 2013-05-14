/*
 * Created on 09-dic-2005
 */
package org.herac.tuxguitar.app.helper;

import org.herac.tuxguitar.util.TGSynchronizer;

/**
 * @author julian
 */
public class SyncThread extends Thread {

	private TGSynchronizer.TGRunnable runnable;

	public SyncThread(TGSynchronizer.TGRunnable runnable) {
		this.runnable = runnable;
	}

	public SyncThread(final Runnable runnable) {
		this(new TGSynchronizer.TGRunnable() {
			@Override
			public void run() throws Throwable {
				runnable.run();
			}
		});
	}

	@Override
	public void run() {
		try {
			TGSynchronizer.instance().addRunnable(this.runnable);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}