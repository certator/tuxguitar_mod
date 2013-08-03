/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.layout.SetCompactViewAction;
import org.herac.tuxguitar.app.actions.layout.SetLinearLayoutAction;
import org.herac.tuxguitar.app.actions.layout.SetMultitrackViewAction;
import org.herac.tuxguitar.app.actions.layout.SetPageLayoutAction;
import org.herac.tuxguitar.app.actions.layout.SetScoreEnabledAction;
import org.herac.tuxguitar.app.items.ToolItems;
import org.herac.tuxguitar.graphics.control.TGLayout;
import org.herac.tuxguitar.graphics.control.TGLayoutHorizontal;
import org.herac.tuxguitar.graphics.control.TGLayoutVertical;

/**
 * @author julian
 */
public class LayoutToolItems extends ToolItems{
	public static final String NAME = "layout.items";

	private ToolItem pageLayout;
	private ToolItem linearLayout;
	private ToolItem multitrack;
	private ToolItem scoreEnabled;
	private ToolItem compact;

	public LayoutToolItems(){
		super(NAME);
	}

	@Override
	public void showItems(ToolBar toolBar){
		this.pageLayout = new ToolItem(toolBar, SWT.RADIO);
		this.pageLayout.addSelectionListener(TuxGuitar.instance().getAction(SetPageLayoutAction.NAME));

		this.linearLayout = new ToolItem(toolBar, SWT.RADIO);
		this.linearLayout.addSelectionListener(TuxGuitar.instance().getAction(SetLinearLayoutAction.NAME));

		this.multitrack = new ToolItem(toolBar, SWT.CHECK);
		this.multitrack.addSelectionListener(TuxGuitar.instance().getAction(SetMultitrackViewAction.NAME));

		this.scoreEnabled = new ToolItem(toolBar, SWT.CHECK);
		this.scoreEnabled.addSelectionListener(TuxGuitar.instance().getAction(SetScoreEnabledAction.NAME));

		this.compact = new ToolItem(toolBar, SWT.CHECK);
		this.compact.addSelectionListener(TuxGuitar.instance().getAction(SetCompactViewAction.NAME));

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void loadProperties(){
		this.pageLayout.setToolTipText(TuxGuitar.getProperty("view.layout.page"));
		this.linearLayout.setToolTipText(TuxGuitar.getProperty("view.layout.linear"));
		this.multitrack.setToolTipText(TuxGuitar.getProperty("view.layout.multitrack"));
		this.scoreEnabled.setToolTipText(TuxGuitar.getProperty("view.layout.score-enabled"));
		this.compact.setToolTipText(TuxGuitar.getProperty("view.layout.compact"));
	}

	public void loadIcons(){
		this.pageLayout.setImage(TuxGuitar.instance().getIconManager().getLayoutPage());
		this.linearLayout.setImage(TuxGuitar.instance().getIconManager().getLayoutLinear());
		this.multitrack.setImage(TuxGuitar.instance().getIconManager().getLayoutMultitrack());
		this.scoreEnabled.setImage(TuxGuitar.instance().getIconManager().getLayoutScore());
		this.compact.setImage(TuxGuitar.instance().getIconManager().getLayoutCompact());
	}

	@Override
	public void update(){
		TGLayout layout = getEditor().getTablature().getViewLayout();
		int style = layout.getStyle();
		this.pageLayout.setSelection(layout instanceof TGLayoutVertical);
		this.linearLayout.setSelection(layout instanceof TGLayoutHorizontal);
		this.multitrack.setSelection( (style & TGLayout.DISPLAY_MULTITRACK) != 0 );
		this.scoreEnabled.setSelection( (style & TGLayout.DISPLAY_SCORE) != 0 );
		this.compact.setSelection( (style & TGLayout.DISPLAY_COMPACT) != 0 );
		this.compact.setEnabled((style & TGLayout.DISPLAY_MULTITRACK) == 0 || getEditor().getTablature().getSongManager().getSong().countTracks() == 1);
	}
}
