package org.herac.tuxguitar.player.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.util.TGLock;

public class MidiPlayer{

	private static final int MAX_CHANNELS = 16;

	public static final int MAX_VOLUME = 10;

	private static final int TIMER_DELAY = 10;

	private TGSongManager songManager;

	private MidiSequencer sequencer;

	private MidiTransmitter outputTransmitter;

	private MidiOutputPort outputPort;

	private MidiPlayerMode mode;

	private MidiPlayerCountDown countDown;

	private String sequencerKey;

	private String outputPortKey;

	private List<MidiOutputPortProvider> outputPortProviders;

	private List<MidiSequencerProvider> sequencerProviders;

	private List<MidiPlayerListener> listeners;

	private int volume;

	private boolean running;

	private boolean paused;

	private boolean changeTickPosition;

	private boolean metronomeEnabled;

	private int metronomeTrack;

	private int infoTrack;

	private int loopSHeader;

	private int loopEHeader;

	private long loopSPosition;

	private boolean anySolo;

	protected long tickLength;

	protected long tickPosition;

	protected TGLock lock = new TGLock();

	public MidiPlayer() {
		this.lock = new TGLock();
		this.volume = MAX_VOLUME;
	}

	public void init(TGSongManager songManager) {
		this.songManager = songManager;
		this.outputPortProviders = new ArrayList<MidiOutputPortProvider>();
		this.sequencerProviders = new ArrayList<MidiSequencerProvider>();
		this.listeners = new ArrayList<MidiPlayerListener>();
		this.getSequencer();
		this.getMode();
		this.reset();
	}

	public MidiInstrument[] getInstruments(){
		return MidiInstrument.INSTRUMENT_LIST;
	}

	public MidiPercussion[] getPercussions(){
		return MidiPercussion.PERCUSSION_LIST;
	}

	public void reset(){
		this.stop();
		this.lock.lock();
		this.tickPosition = TGDuration.QUARTER_TIME;
		this.setChangeTickPosition(false);
		this.lock.unlock();
	}

	public void close(){
		try {
			this.closeSequencer();
			this.closeOutputPort();
		} catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public void stopSequencer() {
		try{
			if( this.getSequencer().isRunning() ){
				this.getSequencer().stop();
			}
		}catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public void stop(boolean paused) {
		this.setPaused(paused);
		this.stopSequencer();
		this.setRunning(false);
	}

	public void stop() {
		this.stop(false);
	}

	public void pause(){
		this.stop(true);
	}

	public synchronized void play() throws MidiPlayerException{
		try {
			this.lock.lock();

			final boolean notifyStarted = (!this.isRunning());
			this.setRunning(true);
			this.stopSequencer();
			this.checkDevices();
			this.updateLoop(true);
			this.addSequence();
			this.updateTracks();
			this.updatePrograms();
			this.updateControllers();
			this.updateDefaultControllers();
			this.setMetronomeEnabled(isMetronomeEnabled());
			this.changeTickPosition();

			new Thread(new Runnable() {
				@Override
				public synchronized void run() {
					try {
						MidiPlayer.this.lock.lock();

						if( notifyStarted ){
							MidiPlayer.this.notifyStarted();
						}

						if( MidiPlayer.this.getCountDown().isEnabled() ){
							MidiPlayer.this.notifyCountDownStarted();
							MidiPlayer.this.getCountDown().start();
							MidiPlayer.this.notifyCountDownStopped();
						}

						if( MidiPlayer.this.isRunning() ){
							if( MidiPlayer.this.isChangeTickPosition() ){
								MidiPlayer.this.changeTickPosition();
							}
							MidiPlayer.this.getSequencer().start();
						}

						MidiPlayer.this.tickLength = getSequencer().getTickLength();
						MidiPlayer.this.tickPosition = getSequencer().getTickPosition();

						Object sequencerLock = new Object();
						while (getSequencer().isRunning() && isRunning()) {
							synchronized(sequencerLock) {
								if (isChangeTickPosition()) {
									changeTickPosition();
								}
								MidiPlayer.this.tickPosition = getSequencer().getTickPosition();

								sequencerLock.wait( TIMER_DELAY );
							}
						}

						//FINISH
						if(isRunning()){
							if(MidiPlayer.this.tickPosition >= (MidiPlayer.this.tickLength - (TGDuration.QUARTER_TIME / 2) )){
								finish();
							}else {
								stop(isPaused());
							}
						}

						if( !isRunning() ){
							MidiPlayer.this.notifyStopped();
						}
					}catch (Throwable throwable) {
						reset();
						throwable.printStackTrace();
					}finally{
						MidiPlayer.this.lock.unlock();
					}
				}
			}).start();
		}catch (Throwable throwable) {
			this.reset();
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}finally{
			this.lock.unlock();
		}
	}

	protected void finish(){
		try {
			if(this.getMode().isLoop()){
				this.stopSequencer();
				this.setTickPosition(TGDuration.QUARTER_TIME);
				this.getMode().notifyLoop();
				this.notifyLoop();
				this.play();
				return;
			}
			this.reset();
		} catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public void updateLoop( boolean force ){
		if( force || !this.isRunning() ){
			this.loopSHeader = -1;
			this.loopEHeader = -1;
			this.loopSPosition = TGDuration.QUARTER_TIME;
			if( getMode().isLoop() ){
				int hCount = this.songManager.getSong().countMeasureHeaders();
				this.loopSHeader = ( getMode().getLoopSHeader() <= hCount ? getMode().getLoopSHeader() : -1 ) ;
				this.loopEHeader = ( getMode().getLoopEHeader() <= hCount ? getMode().getLoopEHeader() : -1 ) ;
				if( this.loopSHeader > 0 && this.loopSHeader <= hCount ){
					TGMeasureHeader header = this.songManager.getMeasureHeader( this.loopSHeader );
					if( header != null ){
						this.loopSPosition = header.getStart();
					}
				}
			}
		}
	}

	public int getLoopSHeader() {
		return this.loopSHeader;
	}

	public int getLoopEHeader() {
		return this.loopEHeader;
	}

	public long getLoopSPosition() {
		return this.loopSPosition;
	}

	public void checkDevices() throws Throwable {
		this.getSequencer().check();
		if( this.getOutputPort() != null ){
			this.getOutputPort().check();
		}
	}

	public int getVolume() {
		return this.volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
		if (this.isRunning()) {
			this.updateControllers();
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isRunning() {
		try {
			return (this.running || this.getSequencer().isRunning());
		} catch (MidiPlayerException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	protected boolean isChangeTickPosition() {
		return this.changeTickPosition;
	}

	private void setChangeTickPosition(boolean changeTickPosition) {
		this.changeTickPosition = changeTickPosition;
	}

	public void setTickPosition(long position) {
		this.tickPosition = position;
		this.setChangeTickPosition(true);
	}

	public long getTickPosition() {
		return this.tickPosition;
	}

	protected void changeTickPosition(){
		try{
			if(isRunning()){
				if( this.tickPosition < this.getLoopSPosition() ){
					this.tickPosition = this.getLoopSPosition();
				}
				this.getSequencer().setTickPosition(this.tickPosition);
			}
			setChangeTickPosition(false);
		} catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public void addSequence() {
		try{
			MidiSequenceParser parser = new MidiSequenceParser(this.songManager,MidiSequenceParser.DEFAULT_PLAY_FLAGS,getMode().getCurrentPercent(),0);
			MidiSequenceHandler sequence = getSequencer().createSequence(this.songManager.getSong().countTracks() + 2);
			parser.setSHeader( getLoopSHeader() );
			parser.setEHeader( getLoopEHeader() );
			parser.parse(sequence);
			this.infoTrack = parser.getInfoTrack();
			this.metronomeTrack = parser.getMetronomeTrack();
		} catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	private void updateDefaultControllers(){
		try{
			for(int channel = 0; channel < MAX_CHANNELS;channel ++){
				getOutputTransmitter().sendControlChange(channel,MidiControllers.RPN_MSB,0);
				getOutputTransmitter().sendControlChange(channel,MidiControllers.RPN_LSB,0);
				getOutputTransmitter().sendControlChange(channel,MidiControllers.DATA_ENTRY_MSB,12);
				getOutputTransmitter().sendControlChange(channel,MidiControllers.DATA_ENTRY_LSB, 0);
			}
		}
		catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public void updatePrograms() {
		Iterator<TGChannel> it = this.songManager.getSong().getChannels();
		while(it.hasNext()){
			updateProgram(it.next());
		}
	}

	private void updateProgram(TGChannel channel) {
		this.updateProgram(channel.getChannel(), channel.getBank(), channel.getProgram());

		if( channel.getChannel() != channel.getEffectChannel()){
			this.updateProgram(channel.getEffectChannel(), channel.getBank(), channel.getProgram());
		}
	}

	private void updateProgram(int channel, int bank, int program) {
		try{
			if( bank >= 0 && bank <= 127 ){
				getOutputTransmitter().sendControlChange(channel, MidiControllers.BANK_SELECT, bank);
			}
			if( program >= 0 && program <= 127 ){
				getOutputTransmitter().sendProgramChange(channel, program);
			}
		}catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public void updateControllers() {
		boolean percussionUpdated = false;

		Iterator<TGChannel> channelsIt = this.songManager.getSong().getChannels();
		while( channelsIt.hasNext() ){
			TGChannel channel = channelsIt.next();
			this.updateController(channel);
			percussionUpdated = (percussionUpdated || channel.isPercussionChannel());
		}
		if(!percussionUpdated && isMetronomeEnabled()){
			int volume = (int)((this.getVolume() / 10.00) * TGChannel.DEFAULT_VOLUME);
			int balance = TGChannel.DEFAULT_BALANCE;
			int chorus = TGChannel.DEFAULT_CHORUS;
			int reverb = TGChannel.DEFAULT_REVERB;
			int phaser = TGChannel.DEFAULT_PHASER;
			int tremolo = TGChannel.DEFAULT_TREMOLO;
			updateController(9,volume,balance,chorus,reverb,phaser,tremolo,127);
		}
		this.afterUpdate();
	}

	private void updateController(TGChannel channel) {
		int volume = (int)((this.getVolume() / 10.00) * channel.getVolume());
		int balance = channel.getBalance();
		int chorus = channel.getChorus();
		int reverb = channel.getReverb();
		int phaser = channel.getPhaser();
		int tremolo = channel.getTremolo();

		updateController(channel.getChannel(),volume,balance,chorus,reverb,phaser,tremolo,127);
		if(channel.getChannel() != channel.getEffectChannel()){
			updateController(channel.getEffectChannel(),volume,balance,chorus,reverb,phaser,tremolo,127);
		}
	}

	private void updateController(int channel,int volume,int balance,int chorus, int reverb,int phaser, int tremolo, int expression) {
		try{
			getOutputTransmitter().sendControlChange(channel,MidiControllers.VOLUME,volume);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.BALANCE,balance);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.CHORUS,chorus);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.REVERB,reverb);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.PHASER,phaser);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.TREMOLO,tremolo);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.EXPRESSION,expression);
		}catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public void updateTracks() {
		this.anySolo = false;

		Iterator<TGTrack> tracksIt = this.songManager.getSong().getTracks();
		while( tracksIt.hasNext() ){
			TGTrack track = tracksIt.next();
			this.updateTrack(track);
			this.anySolo = ((!this.anySolo)?track.isSolo():this.anySolo);
		}

		this.afterUpdate();
	}

	private void updateTrack(TGTrack track) {
		try{
			getSequencer().setMute(track.getNumber(),track.isMute());
			getSequencer().setSolo(track.getNumber(),track.isSolo());
		}catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	private void afterUpdate(){
		try{
			getSequencer().setSolo(this.infoTrack,this.anySolo);
			getSequencer().setSolo(this.metronomeTrack,(isMetronomeEnabled() && this.anySolo));
		}catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public boolean isMetronomeEnabled() {
		return this.metronomeEnabled;
	}

	public void setMetronomeEnabled(boolean metronomeEnabled) {
		try{
			this.metronomeEnabled = metronomeEnabled;
			this.getSequencer().setMute(this.metronomeTrack,!isMetronomeEnabled());
			this.getSequencer().setSolo(this.metronomeTrack,(isMetronomeEnabled() && this.anySolo));
		}catch (MidiPlayerException e) {
			e.printStackTrace();
		}
	}

	public void playBeat(TGBeat beat) {
		List<TGNote> notes = new ArrayList<TGNote>();
		for( int v = 0; v < beat.countVoices(); v ++){
			notes.addAll( beat.getVoice(v).getNotes() );
		}
		playBeat(beat.getMeasure().getTrack(), notes);
	}

	public void playBeat(TGTrack track,final List<TGNote> notes) {
		TGChannel tgChannel = this.songManager.getChannel(track.getChannelId());
		if( tgChannel != null ){
			int channel = tgChannel.getChannel();
			int bank = tgChannel.getBank();
			int program = tgChannel.getProgram();
			int volume = (int)((this.getVolume() / 10.00) * tgChannel.getVolume());
			int balance = tgChannel.getBalance();
			int chorus = tgChannel.getChorus();
			int reverb = tgChannel.getReverb();
			int phaser = tgChannel.getPhaser();
			int tremolo = tgChannel.getTremolo();
			int size = notes.size();
			int[][] beat = new int[size][2];
			for(int i = 0; i < size; i ++){
				TGNote note = notes.get(i);
				beat[i][0] = track.getOffset() + (note.getValue() + track.getStrings().get(note.getString() - 1).getValue());
				beat[i][1] = note.getVelocity();
			}
			playBeat(channel,bank,program,volume,balance,chorus,reverb,phaser,tremolo,beat);
		}
	}

	public void playBeat(int channel,int bank,int program,int volume,int balance,int chorus, int reverb,int phaser,int tremolo,int[][] beat) {
		playBeat(channel, bank, program, volume, balance, chorus, reverb, phaser, tremolo, beat,500,0);
	}

	public void playBeat(int channel,int bank,int program,int volume,int balance,int chorus, int reverb,int phaser,int tremolo,int[][] beat,long duration,int interval) {
		try {
			getOutputTransmitter().sendControlChange(channel,MidiControllers.BANK_SELECT,bank);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.VOLUME,volume);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.BALANCE,balance);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.CHORUS,chorus);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.REVERB,reverb);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.PHASER,phaser);
			getOutputTransmitter().sendControlChange(channel,MidiControllers.TREMOLO,tremolo);
			getOutputTransmitter().sendProgramChange(channel,program);

			for(int i = 0; i < beat.length; i ++){
				getOutputTransmitter().sendNoteOn(channel,beat[i][0], beat[i][1]);
				if(interval > 0){
					Thread.sleep(interval);
				}
			}
			Thread.sleep(duration);
			for(int i = 0; i < beat.length; i ++){
				getOutputTransmitter().sendNoteOff(channel,beat[i][0], beat[i][1]);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	public TGSongManager getSongManager(){
		return this.songManager;
	}

	public MidiPlayerMode getMode(){
		if(this.mode == null){
			this.mode = new MidiPlayerMode();
		}
		return this.mode;
	}

	public MidiPlayerCountDown getCountDown(){
		if( this.countDown == null ){
			this.countDown = new MidiPlayerCountDown(this);
		}
		return this.countDown;
	}

	public MidiTransmitter getOutputTransmitter(){
		if (this.outputTransmitter == null) {
			this.outputTransmitter = new MidiTransmitter();
		}
		return this.outputTransmitter;
	}

	public MidiOutputPort getOutputPort(){
		return this.outputPort;
	}

	public MidiSequencer getSequencer(){
		if (this.sequencer == null) {
			this.sequencer = new MidiSequencerEmpty();
		}
		return this.sequencer;
	}

	public boolean loadSequencer(MidiSequencer sequencer){
		try{
			this.closeSequencer();
			this.sequencer = sequencer;
			this.sequencer.open();
			this.sequencer.setTransmitter( getOutputTransmitter() );
		}catch(Throwable throwable){
			this.sequencer = null;
			return false;
		}
		return true;
	}

	public boolean loadOutputPort(MidiOutputPort port){
		try{
			this.closeOutputPort();
			this.outputPort = port;
			this.outputPort.open();
			this.getOutputTransmitter().addReceiver(this.outputPort.getKey(), this.outputPort.getReceiver() );
		}catch(Throwable throwable){
			this.outputPort = null;
			return false;
		}
		return true;
	}

	public void openOutputPort(String key) {
		this.openOutputPort(key, false);
	}

	public void openOutputPort(String key, boolean tryFirst) {
		this.outputPortKey = key;
		this.openOutputPort(listOutputPorts(),tryFirst);
	}

	public void openOutputPort(List<MidiOutputPort> ports, boolean tryFirst) {
		try{
			if(this.outputPortKey != null && !this.isOutputPortOpen(this.outputPortKey)){
				this.closeOutputPort();
				for(int i = 0; i < ports.size(); i ++){
					MidiOutputPort port = ports.get(i);
					if(port.getKey().equals(this.outputPortKey)){
						if(this.loadOutputPort(port)){
							return;
						}
					}
				}
			}
			if(getOutputPort() == null && !ports.isEmpty() && tryFirst){
				this.loadOutputPort( ports.get(0) );
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
	}

	public void openSequencer(String key) {
		this.openSequencer(key, false);
	}

	public void openSequencer(String key, boolean tryFirst) {
		try{
			this.sequencerKey = key;
			this.openSequencer(listSequencers(),tryFirst);
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
	}

	public void openSequencer(List<MidiSequencer> sequencers ,boolean tryFirst) throws MidiPlayerException {
		try{
			if(this.sequencerKey != null && !this.isSequencerOpen(this.sequencerKey)){
				this.closeSequencer();
				for(int i = 0; i < sequencers.size(); i ++){
					MidiSequencer sequencer = sequencers.get(i);
					if(sequencer.getKey().equals(this.sequencerKey)){
						if(this.loadSequencer(sequencer)){
							return;
						}
					}
				}
			}

			if(getSequencer() instanceof MidiSequencerEmpty && !sequencers.isEmpty() && tryFirst){
				this.loadSequencer( sequencers.get(0));
			}

		}catch(Throwable throwable){
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}
	}

	public List<MidiOutputPort> listOutputPorts() {
		List<MidiOutputPort> ports = new ArrayList<MidiOutputPort>();
		Iterator<MidiOutputPortProvider> it = this.outputPortProviders.iterator();
		while(it.hasNext()){
			try{
				MidiOutputPortProvider provider = it.next();
				ports.addAll(provider.listPorts());
			}catch(Throwable throwable){
				throwable.printStackTrace();
			}
		}
		return ports;
	}

	public List<MidiSequencer> listSequencers(){
		List<MidiSequencer> sequencers = new ArrayList<MidiSequencer>();
		Iterator<MidiSequencerProvider> it = this.sequencerProviders.iterator();
		while(it.hasNext()){
			try{
				MidiSequencerProvider provider = it.next();
				sequencers.addAll(provider.listSequencers());
			}catch(Throwable throwable){
				throwable.printStackTrace();
			}
		}
		return sequencers;
	}

	public void closeSequencer() throws MidiPlayerException{
		try{
			if(this.isRunning()){
				this.stop();
			}
			this.lock.lock();
			if (this.sequencer != null) {
				this.sequencer.close();
				this.sequencer = null;
			}
			this.lock.unlock();
		}catch(Throwable throwable){
			throw new MidiPlayerException(throwable.getMessage(),throwable);
		}
	}

	public void closeOutputPort(){
		try{
			if(this.isRunning()){
				this.stop();
			}
			this.lock.lock();
			if (this.outputPort != null) {
				this.getOutputTransmitter().removeReceiver(this.outputPort.getKey());
				this.outputPort.close();
				this.outputPort = null;
			}
			this.lock.unlock();
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
	}

	public boolean isSequencerOpen(String key){
		if(key != null){
			String currentKey = getSequencer().getKey();
			if(currentKey == null){
				return false;
			}
			return currentKey.equals(key);
		}
		return false;
	}

	public boolean isOutputPortOpen(String key){
		if(key != null && getOutputPort() != null ){
			String currentKey = getOutputPort().getKey();
			if(currentKey == null){
				return false;
			}
			return currentKey.equals(key);
		}
		return false;
	}

	public void addOutputPortProvider(MidiOutputPortProvider provider) throws MidiPlayerException {
		this.addOutputPortProvider(provider, false);
	}

	public void addOutputPortProvider(MidiOutputPortProvider provider, boolean tryFirst) throws MidiPlayerException {
		this.outputPortProviders.add(provider);
		this.openOutputPort(provider.listPorts(),tryFirst);
	}

	public void addSequencerProvider(MidiSequencerProvider provider) throws MidiPlayerException {
		this.addSequencerProvider(provider, false);
	}

	public void addSequencerProvider(MidiSequencerProvider provider, boolean tryFirst) throws MidiPlayerException {
		this.sequencerProviders.add(provider);
		this.openSequencer(provider.listSequencers(), tryFirst);
	}

	public void removeOutputPortProvider(MidiOutputPortProvider provider) throws MidiPlayerException {
		this.outputPortProviders.remove(provider);

		MidiOutputPort current = getOutputPort();
		if( current != null ){
			Iterator<MidiOutputPort> it = provider.listPorts().iterator();
			while(it.hasNext()){
				MidiOutputPort port = it.next();
				if(port.getKey().equals(current.getKey())){
					closeOutputPort();
					break;
				}
			}
		}
	}

	public void removeSequencerProvider(MidiSequencerProvider provider) throws MidiPlayerException {
		this.sequencerProviders.remove(provider);

		MidiSequencer current = getSequencer();
		if(!(current instanceof MidiSequencerEmpty) && current != null){
			Iterator<MidiSequencer> it = provider.listSequencers().iterator();
			while(it.hasNext()){
				MidiSequencer sequencer = it.next();
				if(current.getKey().equals(sequencer.getKey())){
					closeSequencer();
					break;
				}
			}
		}
	}

	public void addListener( MidiPlayerListener listener ){
		if( !this.listeners.contains( listener ) ){
			this.listeners.add( listener );
		}
	}

	public void removeListener( MidiPlayerListener listener ){
		if( this.listeners.contains( listener ) ){
			this.listeners.remove( listener );
		}
	}

	public void notifyStarted(){
		Iterator<MidiPlayerListener> it = this.listeners.iterator();
		while( it.hasNext() ){
			MidiPlayerListener listener = it.next();
			listener.notifyStarted();
		}
	}

	public void notifyStopped(){
		Iterator<MidiPlayerListener> it = this.listeners.iterator();
		while( it.hasNext() ){
			MidiPlayerListener listener = it.next();
			listener.notifyStopped();
		}
	}

	public void notifyCountDownStarted(){
		Iterator<MidiPlayerListener> it = this.listeners.iterator();
		while( it.hasNext() ){
			MidiPlayerListener listener = it.next();
			listener.notifyCountDownStarted();
		}
	}

	public void notifyCountDownStopped(){
		Iterator<MidiPlayerListener> it = this.listeners.iterator();
		while( it.hasNext() ){
			MidiPlayerListener listener = it.next();
			listener.notifyCountDownStopped();
		}
	}

	public void notifyLoop(){
		Iterator<MidiPlayerListener> it = this.listeners.iterator();
		while( it.hasNext() ){
			MidiPlayerListener listener = it.next();
			listener.notifyLoop();
		}
	}
}
