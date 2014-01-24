package org.herac.tuxguitar.app.editors;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import org.herac.tuxguitar.graphics.TGColor;
import org.herac.tuxguitar.graphics.TGFont;
import org.herac.tuxguitar.graphics.TGImage;
import org.herac.tuxguitar.graphics.TGPainter;

public class TGPainterImpl extends TGResourceFactoryImpl implements TGPainter{
	
	public static final int PATH_DRAW = 0x01;
	
	public static final int PATH_FILL = 0x02;
	
	private boolean pathEmpty;
	
	private int style;
	
	private Graphics2D gc;
	
	private GeneralPath path;
	
	private BasicStroke stroke;
	
	private Color background;
	
	private Color foreground;
	
	public TGPainterImpl(){
		super();
	}
	
	public TGPainterImpl(Graphics2D gc){
		this.init(gc);
	}
	
	public TGPainterImpl(Image image){
		this.init((Graphics2D)image.getGraphics());
	}
	
	public void init(Graphics2D gc){
		if(this.gc != null){
			this.gc.dispose();
		}
		this.gc = gc;
		this.background = Color.WHITE;
		this.foreground = Color.BLACK;
		this.stroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	}
	
	public void initPath(int style){
		this.style = style;
		this.path = new GeneralPath();
		this.pathEmpty = true;
		this.setAntialias( true );
	}
	
	public void initPath(){
		this.initPath( PATH_DRAW );
	}
	
	public void closePath(){
		if(this.pathEmpty){
			System.out.println("Warning: Empty Path!");
		}
		if( (this.style & PATH_DRAW) != 0){
			//this.gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.gc.setColor(this.foreground);
			this.gc.draw(this.path);
		}
		if( (this.style & PATH_FILL) != 0){
			//this.gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.gc.setColor(this.background);
			this.gc.fill(this.path);
		}
		this.style = 0;
		this.path = null;
		this.pathEmpty = true;
		this.setAntialias( false );
	}
	
	public Graphics2D getGC(){
		return this.gc;
	}
	
	public void dispose(){
		this.gc.dispose();
		this.gc = null;
	}
	
	public void setFont(Font arg0) {
		this.gc.setFont(arg0);
	}
	
	public void setFont(TGFont font) {
		this.gc.setFont( ((TGFontImpl)font).getHandle() );
	}
	
	/** @Deprecated	**/ 
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public void setBackground(TGColor color) {
		this.background = ((TGColorImpl)color).getHandle();
	}
	
	/** @Deprecated	**/
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	
	public void setForeground(TGColor color) {
		this.foreground = ((TGColorImpl)color).getHandle();
	}
	
	public void setLineWidth(int lineWidth) {
		this.stroke = new BasicStroke(lineWidth, this.stroke.getEndCap(), this.stroke.getLineJoin(), this.stroke.getMiterLimit(), this.stroke.getDashArray(), this.stroke.getDashPhase());
		this.gc.setStroke(this.stroke);
	}
	
	public void setLineStyleSolid() {
		// TODO Auto-generated method stub
		
	}

	public void setLineStyleDot() {
		// TODO Auto-generated method stub
		
	}

	public void setLineStyleDash() {
		// TODO Auto-generated method stub
		
	}

	public void setLineStyleDashDot() {
		// TODO Auto-generated method stub
		
	}
	
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
	}
	
	public void setAntialias(boolean enabled){
		this.gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (enabled ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
	}
	
	public void setAdvanced(boolean advanced) {
		this.gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (advanced ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
		this.gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, (advanced ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));
	}
	
	public Point getStringExtent(String string) {
		return new Point(this.gc.getFontMetrics().stringWidth(string),this.gc.getFontMetrics().getHeight());
	}
	
	public void drawString(String string, int x, int y) {
		this.setAntialias(true);
		this.gc.setColor(this.foreground);
		this.gc.drawString(string, x, (y + this.gc.getFont().getSize()) );
	}
	
	public void drawString(String string, int x, int y, boolean isTransparent) {
		this.setAntialias(true);
		this.gc.setColor(this.foreground);
		this.gc.drawString(string, x, (y + this.gc.getFont().getSize()) /*, isTransparent*/);
	}
	
	/** @Deprecated	**/
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
		this.setAntialias(false);
		this.gc.drawImage(image, destX, destY, (destX + destWidth), (destY + destHeight), srcX, srcY, (srcX + srcWidth), (srcY + srcHeight), null);
	}
	
	/** @Deprecated	**/
	public void drawImage(Image image, int x, int y) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		this.setAntialias(false);
		this.gc.drawImage(image, x, y, (x + width), (y + height), 0, 0, width, height, null);
	}
	
	public void drawImage(TGImage image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
		this.setAntialias(false);
		this.gc.drawImage(((TGImageImpl)image).getHandle(), destX, destY, (destX + destWidth), (destY + destHeight), srcX, srcY, (srcX + srcWidth), (srcY + srcHeight), null);		
	}

	public void drawImage(TGImage image, int x, int y) {
		int width = image.getWidth();
		int height = image.getHeight();
		this.setAntialias(false);
		this.gc.drawImage(((TGImageImpl)image).getHandle(), x, y, (x + width), (y + height), 0, 0, width, height, null);		
	}
	
	public void cubicTo(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5) {
		this.path.curveTo(arg0, arg1, arg2, arg3, arg4, arg5);
		this.pathEmpty = false;
	}
	
	public void lineTo(float arg0, float arg1) {
		this.path.lineTo(arg0, arg1);
		this.pathEmpty = false;
	}
	
	public void moveTo(float arg0, float arg1) {
		this.path.moveTo(arg0, arg1);
		this.pathEmpty = false;
	}
	
	public void addArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
		this.path.append(new Arc2D.Float(x, y, width, height, startAngle,arcAngle, Arc2D.OPEN), true);
		this.pathEmpty = false;
	}
	
	public void addOval(float x, float y, float width, float height) {
		this.path.append(new Arc2D.Float(x, y, width, height, 0,360, Arc2D.OPEN), true);
		this.pathEmpty = false;
	}
	
	public void addRectangle(float x,float y,float width,float height) {
		this.path.append(new Rectangle2D.Float(x, y, width, height), true);
		this.pathEmpty = false;
	}
	
	public void addString(String arg0, float arg1, float arg2, TGFont arg3) {
		// TODO Auto-generated method stub
	}

	public void drawPolygon(int[] arg0) {
		// TODO Auto-generated method stub
	}

	public void fillPolygon(int[] arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public int getFontSize() {
		return this.gc.getFont().getSize();
	}

	public int getFMHeight() {
		return this.gc.getFontMetrics().getHeight();
	}

	public int getFMAscent() {
		return this.gc.getFontMetrics().getAscent();
	}

	public int getFMDescent() {
		return this.gc.getFontMetrics().getDescent();
	}

	public int getFMWidth(String text) {
		return this.gc.getFontMetrics().stringWidth(text);
	}

	public boolean isDisposed() {
		return (this.gc == null);
	}
}
