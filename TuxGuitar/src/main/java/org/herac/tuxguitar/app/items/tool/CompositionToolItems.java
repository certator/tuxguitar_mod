/*
 * Created on 02-dic-2005
 */
package org.herac.tuxguitar.app.items.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.composition.ChangeTempoAction;
import org.herac.tuxguitar.app.actions.composition.ChangeTimeSignatureAction;
import org.herac.tuxguitar.app.actions.insert.DoubleBarAction;
import org.herac.tuxguitar.app.actions.insert.RepeatAlternativeAction;
import org.herac.tuxguitar.app.actions.insert.RepeatCloseAction;
import org.herac.tuxguitar.app.actions.insert.RepeatOpenAction;
import org.herac.tuxguitar.app.items.ToolItems;
import org.herac.tuxguitar.song.models.TGMeasure;

/**
 * @author julian
 */
public class CompositionToolItems extends ToolItems{
	public static final String NAME = "composition.items";

	private ToolItem tempo;
	private ToolItem timeSignature;
	private ToolItem repeatOpen;
	private ToolItem repeatClose;
	private ToolItem repeatAlternative;
	private ToolItem doubleBar;

	public CompositionToolItems(){
		super(NAME);
	}

	@Override
	public void showItems(ToolBar toolBar){
		this.tempo = new ToolItem(toolBar, SWT.PUSH);
		this.tempo.addSelectionListener(TuxGuitar.instance().getAction(ChangeTempoAction.NAME));

		this.timeSignature = new ToolItem(toolBar, SWT.PUSH);
		this.timeSignature.addSelectionListener(TuxGuitar.instance().getAction(ChangeTimeSignatureAction.NAME));

		new ToolItem(toolBar, SWT.SEPARATOR);

		this.repeatOpen = new ToolItem(toolBar, SWT.CHECK);
		this.repeatOpen.addSelectionListener(TuxGuitar.instance().getAction(RepeatOpenAction.NAME));

		this.repeatClose = new ToolItem(toolBar, SWT.CHECK);
		this.repeatClose.addSelectionListener(TuxGuitar.instance().getAction(RepeatCloseAction.NAME));

		this.doubleBar = new ToolItem(toolBar, SWT.CHECK);
		this.doubleBar.addSelectionListener(TuxGuitar.instance().getAction(DoubleBarAction.NAME));

		this.repeatAlternative = new ToolItem(toolBar, SWT.CHECK);
		this.repeatAlternative.addSelectionListener(TuxGuitar.instance().getAction(RepeatAlternativeAction.NAME));

		this.loadIcons();
		this.loadProperties();
	}

	@Override
	public void loadProperties(){
		this.tempo.setToolTipText(TuxGuitar.getProperty("composition.tempo"));
		this.timeSignature.setToolTipText(TuxGuitar.getProperty("composition.timesignature"));
		this.repeatOpen.setToolTipText(TuxGuitar.getProperty("repeat.open"));
		this.repeatClose.setToolTipText(TuxGuitar.getProperty("repeat.close"));
		this.repeatAlternative.setToolTipText(TuxGuitar.getProperty("repeat.alternative"));
		this.doubleBar.setToolTipText(TuxGuitar.getProperty("composition.doublebar"));
	}

	public void loadIcons(){
		this.tempo.setImage(TuxGuitar.instance().getIconManager().getCompositionTempo());
		this.timeSignature.setImage(TuxGuitar.instance().getIconManager().getCompositionTimeSignature());
		this.repeatOpen.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatOpen());
		this.repeatClose.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatClose());
		this.repeatAlternative.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatAlternative());
		this.doubleBar.setImage(TuxGuitar.instance().getIconManager().getCompositionDoubleBar());
	}

	@Override
	public void update(){
		TGMeasure measure = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getMeasure();
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.tempo.setEnabled( !running );
		this.timeSignature.setEnabled( !running );
		this.repeatOpen.setEnabled( !running );
		this.repeatOpen.setSelection(measure != null && measure.isRepeatOpen());
		this.repeatClose.setEnabled( !running );
		this.repeatClose.setSelection(measure != null && measure.getRepeatClose() > 0);
		this.repeatAlternative.setEnabled( !running );
		this.repeatAlternative.setSelection(measure != null && measure.getHeader().getRepeatAlternative() > 0);
		this.doubleBar.setEnabled( !running );
		this.doubleBar.setSelection(measure != null && measure.getHeader().hasDoubleBar());
	}
}
