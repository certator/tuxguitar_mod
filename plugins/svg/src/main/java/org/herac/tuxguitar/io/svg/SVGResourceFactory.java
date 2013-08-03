package org.herac.tuxguitar.io.svg;

import org.herac.tuxguitar.graphics.TGColor;
import org.herac.tuxguitar.graphics.TGColorModel;
import org.herac.tuxguitar.graphics.TGFont;
import org.herac.tuxguitar.graphics.TGFontModel;
import org.herac.tuxguitar.graphics.TGImage;
import org.herac.tuxguitar.graphics.TGResourceFactory;

public class SVGResourceFactory implements TGResourceFactory{

	@Override
	public TGImage createImage(int width, int height) {
		return new SVGImage(width, height);
	}

	@Override
	public TGColor createColor(TGColorModel colorModel) {
		return this.createColor(colorModel.getRed(), colorModel.getGreen(), colorModel.getBlue());
	}

	@Override
	public TGColor createColor(int red, int green, int blue) {
		return new SVGColor(red, green, blue);
	}

	@Override
	public TGFont createFont(TGFontModel fontModel) {
		return createFont(fontModel.getName(), fontModel.getHeight(), fontModel.isBold(), fontModel.isItalic() );
	}

	@Override
	public TGFont createFont(String name, int height, boolean bold, boolean italic) {
		return new SVGFont(name, height, bold, italic);
	}
}
