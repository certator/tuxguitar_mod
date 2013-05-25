package org.herac.tuxguitar.midiinput;

import org.eclipse.swt.widgets.Shell;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.system.plugins.TGPluginException;
import org.herac.tuxguitar.app.system.plugins.TGPluginSetup;
import org.herac.tuxguitar.app.system.plugins.base.TGToolItemPlugin;
import org.herac.tuxguitar.app.util.MessageDialog;

public class MidiInputPlugin
	extends TGToolItemPlugin
	implements TGPluginSetup
{
	@Override
	public void setEnabled(boolean enabled)
		throws TGPluginException
	{
	if(enabled)
		{
		// try to setup the environment according to the user's preferences

		String	notesDeviceName = MiConfig.instance().getMidiInputPortName();

		if(notesDeviceName != null)
			{
			try {
				MiPort.setNotesPort(notesDeviceName);
				}
			catch(MiException mie)
				{
				MessageDialog.errorMessage(mie);
				}
			}

		MiProvider.instance().setBaseChannel	(MiConfig.instance().getMidiBaseChannel());
		MiProvider.instance().setMode			(MiConfig.instance().getMode());
		MiProvider.instance().setMinVelocity	((byte)MiConfig.instance().getMinVelocity());
		MiProvider.instance().setMinDuration	(MiConfig.instance().getMinDuration());
		MiProvider.instance().setEchoTimeOut	(MiConfig.instance().getEchoTimeOut());
		MiProvider.instance().setInputTimeOut	(MiConfig.instance().getInputTimeOut());
		}
	else
		{
		// try to cleanup the environment

		try {
			MiPort.setNotesPort(null);
			MiPort.setControlPort(null);
			}
		catch(MiException mie)
			{
			MessageDialog.errorMessage(mie);
			}
		}

	super.setEnabled(enabled);
	}

	/*
	 *	TGPlugin implementation
	 */

	@Override
	public String getAuthor()
	{
	return "Amedeo Farello <afarello@tiscalinet.it>";
	}


	@Override
	public String getDescription()
	{
	return "Supports MIDI equipped guitars";
	}


	@Override
	public String getName()
	{
	return "MIDI input plugin";
	}


	@Override
	public String getVersion()
	{
	return "1.0";
	}

	/*
	 *	TGPluginSetup implementation
	 */

	@Override
	public void setupDialog(Shell parent)
	{
	MiConfig.instance().showDialog(parent);
	}

	/*
	 *	TGToolItemPlugin implementation
	 */

	@Override
	protected void doAction()
	{
	MiPanel.instance().showDialog(TuxGuitar.instance().getShell());
	}


	@Override
	protected String getItemName()
	{
	return "MIDI input";
	}
}
