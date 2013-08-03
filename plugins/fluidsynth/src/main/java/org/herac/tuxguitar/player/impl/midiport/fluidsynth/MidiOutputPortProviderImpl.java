package org.herac.tuxguitar.player.impl.midiport.fluidsynth;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiOutputPortProvider;
import org.herac.tuxguitar.player.base.MidiPlayerException;

public class MidiOutputPortProviderImpl implements MidiOutputPortProvider{

	private MidiSynth synth;
	private MidiOutputPortSettings settings;

	public MidiOutputPortProviderImpl(){
		super();
	}

	@Override
	public List<MidiOutputPort> listPorts() throws MidiPlayerException {
		try{
			List<MidiOutputPort> ports = new ArrayList<MidiOutputPort>();
			Iterator<String> it = getSettings().getSoundfonts().iterator();
			while(it.hasNext()){
				String path = it.next();
				File soundfont = new File( path );
				if( soundfont.exists() && !soundfont.isDirectory() ){
					ports.add( new MidiOutputPortImpl( getSynth(), soundfont ) );
				}
			}
			return ports;
		}catch(Throwable throwable){
			throw new MidiPlayerException(throwable.getMessage(), throwable);
		}
	}

	@Override
	public void closeAll() throws MidiPlayerException {
		try{
			if(this.synth != null && this.synth.isInitialized()){
				this.synth.finalize();
				this.synth = null;
			}
		}catch(Throwable throwable){
			throw new MidiPlayerException(throwable.getMessage(), throwable);
		}
	}

	public MidiSynth getSynth(){
		if(this.synth == null || !this.synth.isInitialized()){
			this.synth = new MidiSynth();
			this.getSettings().apply();
		}
		return this.synth;
	}

	public MidiOutputPortSettings getSettings(){
		if(this.settings == null){
			this.settings = new MidiOutputPortSettings( this );
		}
		return this.settings;
	}
}
