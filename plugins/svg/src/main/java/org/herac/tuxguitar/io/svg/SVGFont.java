package org.herac.tuxguitar.io.svg;

import org.herac.tuxguitar.graphics.TGFont;
import org.herac.tuxguitar.graphics.TGFontModel;

public class SVGFont implements TGFont {

	private TGFontModel handle;

	public SVGFont(String name, int height, boolean bold, boolean italic){
		this.handle = new TGFontModel(name, height, bold, italic);
	}

	@Override
	public void dispose() {
		this.handle = null;
	}

	@Override
	public boolean isDisposed() {
		return (this.handle == null);
	}

	@Override
	public String getName() {
		return this.handle.getName();
	}

	@Override
	public int getHeight() {
		return this.handle.getHeight();
	}

	@Override
	public boolean isBold() {
		return this.handle.isBold();
	}

	@Override
	public boolean isItalic() {
		return this.handle.isItalic();
	}
}
