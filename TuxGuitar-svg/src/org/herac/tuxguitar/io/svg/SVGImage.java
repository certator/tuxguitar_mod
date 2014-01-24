package org.herac.tuxguitar.io.svg;

import org.herac.tuxguitar.graphics.TGColor;
import org.herac.tuxguitar.graphics.TGImage;
import org.herac.tuxguitar.graphics.TGPainter;

public class SVGImage implements TGImage{
	
	private StringBuffer buffer;
	
	private int width;
	private int height;
	
	private boolean disposed;
	
	public SVGImage(int width, int height){
		this.width = width;
		this.height = height;
		this.disposed = false;
		this.buffer = new StringBuffer();
	}
	
	public void dispose() {
		this.disposed = true;
	}
	
	public boolean isDisposed() {
		return this.disposed;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public StringBuffer getBuffer(){
		return this.buffer;
	}
	
	public TGPainter createPainter() {
		return new SVGPainter( this.buffer );
	}
	
	public void applyTransparency(TGColor background) {
		// Not implemented
	}
}
