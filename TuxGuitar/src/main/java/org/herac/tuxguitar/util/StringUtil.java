package org.herac.tuxguitar.util;

public class StringUtil {

	/**
	 * Create a string with the first char upper case
	 * @param string A string
	 * @return A string with the first char upper case
	 */
	public static String toUpperFirstChar(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
}
