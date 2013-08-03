package org.herac.tuxguitar.app.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.herac.tuxguitar.graphics.TGFont;

public class TGFontImpl implements TGFont {

	private Font handle;

	public TGFontImpl( Font handle ){
		this.handle = handle;
	}

	public TGFontImpl( Device device , String name, int height, boolean bold, boolean italic){
		this( new Font( device, name, height, (SWT.NORMAL | (bold ? SWT.BOLD : 0) | (italic ? SWT.ITALIC : 0)) ) );
	}

	@Override
	public void dispose(){
		this.handle.dispose();
	}

	@Override
	public boolean isDisposed(){
		return this.handle.isDisposed();
	}

	public Font getHandle(){
		return this.handle;
	}

	@Override
	public String getName() {
		FontData[] fd = this.handle.getFontData();
		return ( fd != null && fd.length > 0 ? fd[0].getName() : new String() );
	}

	@Override
	public int getHeight() {
		FontData[] fd = this.handle.getFontData();
		return ( fd != null && fd.length > 0 ? fd[0].getHeight() : 0 );
	}

	@Override
	public boolean isBold() {
		FontData[] fd = this.handle.getFontData();
		return ( fd != null && fd.length > 0 ? ((fd[0].getStyle() & SWT.BOLD) != 0) : false );
	}

	@Override
	public boolean isItalic() {
		FontData[] fd = this.handle.getFontData();
		return ( fd != null && fd.length > 0 ? ((fd[0].getStyle() & SWT.ITALIC) != 0) : false );
	}
}
