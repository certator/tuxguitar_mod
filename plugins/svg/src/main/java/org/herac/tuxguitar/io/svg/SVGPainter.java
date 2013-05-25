package org.herac.tuxguitar.io.svg;

import org.herac.tuxguitar.graphics.TGColor;
import org.herac.tuxguitar.graphics.TGFont;
import org.herac.tuxguitar.graphics.TGImage;
import org.herac.tuxguitar.graphics.TGPainter;

public class SVGPainter extends SVGResourceFactory implements TGPainter {
	
	private int svgStrokeWidth;
	private int svgPathStyle;
	private SVGFont svgFont;
	private SVGColor svgBackground;
	private SVGColor svgForeground;
	private StringBuffer svgPath;
	private StringBuffer svgBuffer;
	
	private boolean disposed;
	
	public SVGPainter( StringBuffer svgBuffer ){
		this.svgBuffer = svgBuffer;
		this.svgPath = null;
		this.svgPathStyle = 0;
		this.svgStrokeWidth = 1;
		this.disposed = false;
		this.svgFont = new SVGFont("none", 10, false, false);
		this.svgBackground = new SVGColor(0xff, 0xff, 0xff);
		this.svgForeground = new SVGColor(0x00, 0x00, 0x00);
	}
	
	@Override
	public void dispose(){
		this.disposed = true;
	}
	
	@Override
	public boolean isDisposed(){
		return this.disposed;
	}
	
	@Override
	public void initPath(int style){
		this.svgPath = new StringBuffer();
		this.svgPathStyle = style;
		
		this.setAntialias(true);
	}
	
	@Override
	public void initPath(){
		this.initPath( PATH_DRAW );
	}
	
	@Override
	public void closePath(){
		if( this.svgPath != null && this.svgPath.length() > 0 ){
			this.svgBuffer.append("\r\n");
			this.svgBuffer.append("<path ");
			this.svgBuffer.append("d=\"" + this.svgPath.toString() + "Z\" ");
			this.svgBuffer.append("fill=\"" + ((this.svgPathStyle & PATH_FILL) != 0 ? this.svgBackground.toHexString() : "none") +"\" ");
			this.svgBuffer.append("stroke=\"" + ((this.svgPathStyle & PATH_DRAW) != 0 ? this.svgForeground.toHexString() : "none") +"\" ");
			this.svgBuffer.append("stroke-width=\"" + this.svgStrokeWidth + "\" ");
			this.svgBuffer.append("/>");
		}
		this.svgPath = null;
		this.svgPathStyle = 0;
		this.setAntialias(false);
	}
	
	@Override
	public void drawString(String string, int x, int y) {
		this.setAdvanced(false);
		this.svgBuffer.append("\r\n");
		this.svgBuffer.append("<text ");
		this.svgBuffer.append("x='" + x + "' ");
		this.svgBuffer.append("y='" + (y + Math.round(0.75f * getFontSize())) + "' ");
		this.svgBuffer.append("font-family=\""+ this.svgFont.getName() + "\" ");
		this.svgBuffer.append("font-size=\"" + this.svgFont.getHeight() + "\" ");
		this.svgBuffer.append("fill=\"" + this.svgForeground.toHexString() +"\" ");
		this.svgBuffer.append(">");
		this.svgBuffer.append( string );
		this.svgBuffer.append("</text>");
	}
	
	@Override
	public void drawString(String string, int x, int y, boolean isTransparent) {
		this.svgBuffer.append("\r\n");
		this.svgBuffer.append("<text ");
		this.svgBuffer.append("x='" + x + "' ");
		this.svgBuffer.append("y='" + (y + Math.round(0.75f * getFontSize())) + "' ");
		this.svgBuffer.append("font-family=\""+ this.svgFont.getName() + "\" ");
		this.svgBuffer.append("font-size=\"" + this.svgFont.getHeight() + "\" ");
		this.svgBuffer.append("fill=\"" + this.svgForeground.toHexString() +"\" ");
		this.svgBuffer.append(">");
		this.svgBuffer.append( string );
		this.svgBuffer.append("</text>");
	}
	
	@Override
	public void drawImage(TGImage image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
		if( image instanceof SVGImage ){
			this.svgBuffer.append("<g transform=\"translate(" + destX + "," + destY + ")\">");
			this.svgBuffer.append( ((SVGImage)image).getBuffer() );
			this.svgBuffer.append("</g>");
		}
	}
	
	@Override
	public void drawImage(TGImage image, int x, int y) {
		if( image instanceof SVGImage ){
			this.svgBuffer.append("<g transform=\"translate(" + x + "," + y + ")\">");
			this.svgBuffer.append( ((SVGImage)image).getBuffer() );
			this.svgBuffer.append("</g>");
		}
	}
	
	@Override
	public void cubicTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
		this.svgPath.append("C " + cx1 + " " + cy1 + " " + cx2 + " " + cy2 + " "+ x + " " + y + " ");
	}
	
	@Override
	public void lineTo(float x, float y) {
		this.svgPath.append("L " + x + " " + y + " ");
	}
	
	@Override
	public void moveTo(float x, float y) {
		this.svgPath.append("M " + x + " " + y + " ");
	}
	
	@Override
	public void addArc(float x, float y, float width, float height, float startAngle, float arcLength) {
		double angle1 = Math.toRadians(-startAngle);
		double x1 = (x + (Math.cos(angle1) * 0.5 + 0.5) * width);
		double y1 = (y + (Math.sin(angle1) * 0.5 + 0.5) * height);
		
		double angle2 = Math.toRadians(-startAngle - arcLength);
		double x2 = (x + (Math.cos(angle2) * 0.5 + 0.5) * width);
		double y2 = (y + (Math.sin(angle2) * 0.5 + 0.5) * height);
		
		this.svgPath.append("M " + x1 + " " + y1 + " ");
		this.svgPath.append("A " + (width / 2) + " " + (height / 2) + " 0 ");
		this.svgPath.append( ( arcLength > 180 || arcLength < -180 ? "1 " : "0 " ) );
		this.svgPath.append( ( arcLength > 0 ? "0 " : "1 " ) );
		this.svgPath.append(x2 + " " + y2 + " ");
		this.svgPath.append("M " + x2 + " " + y2 + " ");
	}
	
	@Override
	public void addOval(float x, float y, float w, float h) {
		this.svgPath.append("M " + x + " " + (y + (h / 2f)) + " a ");
		this.svgPath.append((w / 2f) + " " + (h / 2f) + " 0 1 0 " + w + " 0 ");
		this.svgPath.append((w / 2f) + " " + (h / 2f) + " 0 1 0 -" + w + " 0 ");
	}
	
	@Override
	public void addRectangle(float x,float y,float width,float height) {
		this.svgPath.append("M " + x + " " + y + " ");
		this.svgPath.append("L " + (x + width) + " " + y + " ");
		this.svgPath.append("L " + (x + width) + " " + (y + height) + " ");
		this.svgPath.append("L " + x + " " + (y + height) + " ");
		this.svgPath.append("L " + x + " " + y + " ");
	}
	
	@Override
	public void addString(String text, float x, float y, TGFont font) {
		TGFont currentFont = this.svgFont;
		this.setFont(font);
		this.drawString(text, Math.round(x), Math.round(y));
		this.setFont(currentFont);
	}
	
	@Override
	public void setFont(TGFont font) {
		if( font instanceof SVGFont ){
			this.svgFont = (SVGFont)font;
		}
	}
	
	@Override
	public void setBackground(TGColor color) {
		if( color instanceof SVGColor ){
			this.svgBackground = (SVGColor)color;
		}
	}
	
	@Override
	public void setForeground(TGColor color) {
		if( color instanceof SVGColor ){
			this.svgForeground = (SVGColor)color;
		}
	}
	
	@Override
	public void setLineWidth(int width) {
		this.svgStrokeWidth = width;
	}
	
	@Override
	public void setLineStyleSolid(){
		// Not Implemented
	}
	
	@Override
	public void setLineStyleDot(){
		// Not Implemented
	}
	
	@Override
	public void setLineStyleDash(){
		// Not Implemented
	}
	
	@Override
	public void setLineStyleDashDot(){
		// Not Implemented
	}
	
	@Override
	public void setAntialias(boolean enabled){
		// Not Implemented
	}
	
	@Override
	public void setAdvanced(boolean advanced){
		// Not Implemented
	}
	
	@Override
	public int getFontSize(){
		return this.svgFont.getHeight();
	}
	
	@Override
	public int getFMHeight(){
		return (getFMAscent() + getFMDescent());
	}
	
	@Override
	public int getFMAscent(){
		return getFontSize();
	}
	
	@Override
	public int getFMDescent(){
		return 0;
	}
	
	@Override
	public int getFMWidth( String text ){
		return ( text != null ? Math.round( text.length() * (0.75f * getFontSize() ) ) : 0 );
	}
	
	@Override
	public void setAlpha(int alpha) {
		// Not Implemented
	}
	
	@Override
	public void drawPolygon(int[] arg0) {
		// Not implemented
	}
	
	@Override
	public void fillPolygon(int[] arg0) {
		// Not implemented
	}
	
	@Override
	public void copyArea(TGImage image, int x, int y) {
		// Not Implemented
	}
}
