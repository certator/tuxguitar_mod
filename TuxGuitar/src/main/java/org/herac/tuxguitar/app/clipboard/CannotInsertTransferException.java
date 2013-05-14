/*
 * Created on 05-dic-2005
 */
package org.herac.tuxguitar.app.clipboard;

/**
 * @author julian
 */
public class CannotInsertTransferException extends Exception{

	private static final long serialVersionUID = 1L;

	public CannotInsertTransferException() {
		super();
	}

	public CannotInsertTransferException(String message) {
		super(message);
	}

	public CannotInsertTransferException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotInsertTransferException(Throwable cause) {
		super(cause);
	}
}
