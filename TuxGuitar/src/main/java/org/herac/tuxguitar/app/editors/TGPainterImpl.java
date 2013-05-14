package org.herac.tuxguitar.app.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.herac.tuxguitar.graphics.TGColor;
import org.herac.tuxguitar.graphics.TGDimension;
import org.herac.tuxguitar.graphics.TGFont;
import org.herac.tuxguitar.graphics.TGImage;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGPoint;

public class TGPainterImpl extends TGResourceFactoryImpl implements TGPainter {
	
	private boolean pathEmpty;
	
	private int style;
	
	private GC gc;
	
	private Path path;
	
	public TGPainterImpl(){
		super();
	}
	
	public TGPainterImpl(GC gc){
		this.init(gc);
	}
	
	public TGPainterImpl(Image image){
		this.init(image);
	}
	
	public void init(Image image){
		this.init(new GC(image));
	}
	
	public void init(GC gc){
		this.setDevice(gc.getDevice());
		if(this.gc != null && !this.gc.isDisposed()){
			this.gc.dispose();
		}
		this.gc = gc;
	}
	
	@Override
	public void initPath(int style){
		this.style = style;
		this.path = new Path(this.gc.getDevice());
		this.pathEmpty = true;
		this.setAntialias(true);
	}
	
	@Override
	public void initPath(){
		this.initPath( PATH_DRAW );
	}
	
	@Override
	public void closePath(){
		if(! this.pathEmpty ){
			if( (this.style & PATH_DRAW) != 0){
				this.gc.drawPath(this.path);
			}
			if( (this.style & PATH_FILL) != 0){
				this.gc.fillPath(this.path);
			}
		}
		this.style = 0;
		this.path.dispose();
		this.pathEmpty = true;
		this.setAntialias(false);
	}
	
	public GC getGC(){
		return this.gc;
	}
	
	@Override
	public void dispose(){
		this.gc.dispose();
	}
	
	@Override
	public boolean isDisposed(){
		return this.gc.isDisposed();
	}
	
	@Override
	public void copyArea(TGImage image, int x, int y) {
		this.gc.copyArea(getImage(image), x, y);
	}
	
	@Override
	public void drawString(String string, int x, int y) {
		this.setAdvanced(false);
		this.gc.drawString(string, x, y);
	}
	
	@Override
	public void drawString(String string, int x, int y, boolean isTransparent) {
		this.setAdvanced(false);
		this.gc.drawString(string, x, y, isTransparent);
	}
	
	@Override
	public void drawImage(TGImage image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
		this.setAdvanced(false);
		this.gc.drawImage(getImage(image), srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight);
	}
	
	@Override
	public void drawImage(TGImage image, int x, int y) {
		this.setAdvanced(false);
		this.gc.drawImage(getImage(image), x, y);
	}
	
	@Override
	public void drawPolygon(int[] arg0) {
		this.gc.drawPolygon(arg0);
	}
	
	@Override
	public void fillPolygon(int[] arg0) {
		this.gc.fillPolygon(arg0);
	}
	
	@Override
	public void cubicTo(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5) {
		this.path.cubicTo(arg0, arg1, arg2, arg3, arg4, arg5);
		this.pathEmpty = false;
	}
	
	@Override
	public void lineTo(float arg0, float arg1) {
		this.path.lineTo(arg0, arg1);
		this.pathEmpty = false;
	}
	
	@Override
	public void moveTo(float arg0, float arg1) {
		this.path.moveTo(arg0, arg1);
		this.pathEmpty = false;
	}
	
	@Override
	public void addString(String arg0, float arg1, float arg2, TGFont font) {
		this.path.addString(arg0, arg1, arg2, getFont(font));
		this.pathEmpty = false;
	}
	
	@Override
	public void addArc(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5) {
		this.path.addArc(arg0, arg1, arg2, arg3, arg4, arg5);
		this.pathEmpty = false;
	}
	
	@Override
	public void addOval(float arg0, float arg1, float arg2, float arg3) {
		this.path.addArc(arg0, arg1, arg2, arg3, 0, 360);
		this.pathEmpty = false;
	}
	
	@Override
	public void addRectangle(float x,float y,float width,float height) {
		this.path.addRectangle(x, y, width, height);
		this.pathEmpty = false;
	}
	
	public void addRectangle(Rectangle rectangle) {
		this.path.addRectangle(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
		this.pathEmpty = false;
	}
	
	@Override
	public void setFont(TGFont font) {
		this.gc.setFont(getFont(font));
	}
	
	@Override
	public void setForeground(TGColor color) {
		this.gc.setForeground(getColor(color));
	}
	
	@Override
	public void setBackground(TGColor color) {
		this.gc.setBackground(getColor(color));
	}
	
	@Override
	public void setLineWidth(int width) {
		this.gc.setLineWidth(width);
	}
	
	@Override
	public void setLineStyleSolid(){
		this.gc.setLineStyle(SWT.LINE_SOLID);
	}
	
	@Override
	public void setLineStyleDot(){
		this.gc.setLineStyle(SWT.LINE_DOT);
	}
	
	@Override
	public void setLineStyleDash(){
		this.gc.setLineStyle(SWT.LINE_DASH);
	}
	
	@Override
	public void setLineStyleDashDot(){
		this.gc.setLineStyle(SWT.LINE_DASHDOT);
	}
	
	@Override
	public void setAlpha(int alpha) {
		this.gc.setAlpha(alpha);
	}
	
	@Override
	public void setAntialias(boolean enabled){
		if( !TGPainterUtils.FORCE_OS_DEFAULTS ){
			this.gc.setAntialias(enabled ? SWT.ON : SWT.OFF );
		}
	}
	
	@Override
	public void setAdvanced(boolean advanced){
		if( !TGPainterUtils.FORCE_OS_DEFAULTS ){
			this.gc.setAdvanced(advanced);
		}
	}
	
	@Override
	public int getFontSize(){
		FontData[] fd = this.gc.getFont().getFontData();
		if( fd != null && fd.length > 0 ){
			return fd[0].getHeight();
		}
		return 0;
	}
	
	@Override
	public int getFMHeight(){
		this.setAdvanced(false);
		return this.gc.getFontMetrics().getHeight();
	}
	
	@Override
	public int getFMAscent(){
		this.setAdvanced(false);
		return this.gc.getFontMetrics().getAscent();
	}
	
	@Override
	public int getFMDescent(){
		this.setAdvanced(false);
		return this.gc.getFontMetrics().getDescent();
	}
	
	@Override
	public int getFMWidth( String text ){
		this.setAdvanced(false);
		return this.gc.stringExtent( text ).x;
	}
	
	public TGPoint createPoint( Point point ){
		return new TGPoint( point.x , point.y );
	}
	
	public TGDimension createDimension( Point point ){
		return new TGDimension( point.x , point.y );
	}
	
	public Image getImage(TGImage image){
		if( image instanceof TGImageImpl ){
			return ((TGImageImpl)image).getHandle();
		}
		return null;
	}
	
	public Color getColor(TGColor color){
		if( color instanceof TGColorImpl ){
			return ((TGColorImpl)color).getHandle();
		}
		return null;
	}
	
	public Font getFont(TGFont font){
		if( font instanceof TGFontImpl ){
			return ((TGFontImpl)font).getHandle();
		}
		return null;
	}
}
