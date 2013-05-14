/*
 * Created on 05-dic-2005
 */
package org.herac.tuxguitar.app.undo;

/**
 * @author julian
 */
public class CannotRedoException extends Exception{

	private static final long serialVersionUID = 1L;

	public CannotRedoException() {
		super();
	}

	public CannotRedoException(String message) {
		super(message);
	}

	public CannotRedoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotRedoException(Throwable cause) {
		super(cause);
	}
}
