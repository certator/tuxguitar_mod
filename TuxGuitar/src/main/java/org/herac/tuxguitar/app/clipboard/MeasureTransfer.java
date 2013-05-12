package org.herac.tuxguitar.app.clipboard;

import org.herac.tuxguitar.util.TGVersion;


public class MeasureTransfer extends SerializableTransfer {

	private static final String NAME = "TuxGuitar " + TGVersion.CURRENT + " measure transfer";

	private static final int ID = registerType(NAME);

	private static MeasureTransfer instance = new MeasureTransfer();

	private MeasureTransfer() {}

	public static MeasureTransfer getInstance () {
		return instance;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[]{ID};
	}

	@Override
	protected String[] getTypeNames() {
		return new String[]{NAME};
	}
}
