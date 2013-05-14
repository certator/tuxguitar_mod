/*
 * Created on 05-dic-2005
 */
package org.herac.tuxguitar.app.undo;

/**
 * @author julian
 */
public class CannotUndoException extends Exception{

	private static final long serialVersionUID = 1L;

	public CannotUndoException() {
		super();
	}

	public CannotUndoException(String message) {
		super(message);
	}

	public CannotUndoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotUndoException(Throwable cause) {
		super(cause);
	}
}
