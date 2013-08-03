/*
 * Created on 23-nov-2005
 */
package org.herac.tuxguitar.song.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGColor;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMarker;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;


/**
 * @author julian
 */
public class TGSongManager {
	public static final short MAX_CHANNELS = 16;

	private TGFactory factory;
	private TGSong song;
	private TGTrackManager trackManager;
	private TGMeasureManager measureManager;

	public TGSongManager(){
		this(new TGFactory());
	}

	public TGSongManager(TGFactory factory){
		this.factory = factory;
	}

	public TGFactory getFactory(){
		return this.factory;
	}

	public void setFactory(TGFactory factory){
		this.factory = factory;
	}

	public TGTrackManager getTrackManager(){
		if(this.trackManager == null){
			this.trackManager = new TGTrackManager(this);
		}
		return this.trackManager;
	}

	public TGMeasureManager getMeasureManager(){
		if(this.measureManager == null){
			this.measureManager = new TGMeasureManager(this);
		}
		return this.measureManager;
	}

	public void setSongName(String name){
		getSong().setName(name);
	}

	public TGSong getSong(){
		return this.song;
	}

	public void clearSong(){
		if(this.getSong() != null){
			this.getSong().clear();
		}
	}

	public void setSong(TGSong song){
		if(song != null){
			this.clearSong();
			this.song = song;
		}
	}

	public void setProperties(String name,String artist,String album,String author,String date,String copyright,String writer,String transcriber,String comments){
		getSong().setName(name);
		getSong().setArtist(artist);
		getSong().setAlbum(album);
		getSong().setAuthor(author);
		getSong().setDate(date);
		getSong().setCopyright(copyright);
		getSong().setWriter(writer);
		getSong().setTranscriber(transcriber);
		getSong().setComments(comments);
	}

	public TGSong newSong(){
		TGChannel channel = getFactory().newChannel();
		channel.setChannelId(1);
		channel.setChannel((short)0);
		channel.setEffectChannel((short)1);
		channel.setName(getDefaultChannelName(channel));

		TGMeasureHeader header = getFactory().newHeader();
		header.setNumber(1);
		header.setStart(TGDuration.QUARTER_TIME);
		header.getTimeSignature().setNumerator(4);
		header.getTimeSignature().getDenominator().setValue(TGDuration.QUARTER);

		TGTrack track = getFactory().newTrack();
		track.setNumber(1);
		track.setName(getDefaultTrackName(track));
		track.setChannelId(channel.getChannelId());
		track.setStrings(createDefaultInstrumentStrings());
		track.addMeasure(getFactory().newMeasure(header));
		TGColor.RED.copy(track.getColor());

		TGSong song = getFactory().newSong();
		song.addChannel(channel);
		song.addMeasureHeader(header);
		song.addTrack(track);

		return song;
	}

	public TGChannel createChannel(){
		TGChannel channel = getFactory().newChannel();

		List<Integer> freeChannels = getFreeChannels( channel );

		channel.setChannel((short)( freeChannels.size() > 0 ? freeChannels.get(0).intValue() : -1 ) );
		channel.setEffectChannel((short)( freeChannels.size() > 1 ? freeChannels.get(1).intValue() : channel.getChannel() ) );

		return channel;
	}

	public TGChannel addChannel(){
		TGChannel tgChannel = addChannel(createChannel());
		tgChannel.setName(getDefaultChannelName(tgChannel));
		return tgChannel;
	}

	public TGChannel addChannel(TGChannel tgChannel){
		if( tgChannel != null ){
			if( tgChannel.getChannelId() <= 0 ){
				tgChannel.setChannelId( getNextChannelId() );
			}
			getSong().addChannel(tgChannel);
		}
		return tgChannel;
	}

	public void removeChannel(TGChannel channel){
		if( channel != null ){
			getSong().removeChannel(channel);
		}
	}

	public void removeChannel(int channelId){
		TGChannel channel = getChannel(channelId);
		if( channel != null ){
			removeChannel(channel);
		}
	}

	public void removeAllChannels(){
		while( getSong().countChannels() > 0 ){
			removeChannel( getSong().getChannel(0) );
		}
	}

	public TGChannel getChannel(int channelId){
		Iterator<TGChannel> it = getSong().getChannels();
		while( it.hasNext() ){
			TGChannel channel = it.next();
			if( channel.getChannelId() == channelId ){
				return channel;
			}
		}
		return null;
	}

	public List<TGChannel> getChannels(){
		List<TGChannel> channels = new ArrayList<TGChannel>();

		Iterator<TGChannel> it = getSong().getChannels();
		while( it.hasNext() ){
			channels.add(it.next());
		}

		return channels;
	}

	public int getNextChannelId(){
		int maximumId = 0;

		Iterator<TGChannel> it = getSong().getChannels();
		while( it.hasNext() ){
			TGChannel channel = it.next();
			if( maximumId < channel.getChannelId() ){
				maximumId = channel.getChannelId();
			}
		}

		return (maximumId + 1);
	}

	public String getDefaultChannelName(TGChannel tgChannel){
		if( tgChannel != null && tgChannel.getChannelId() > 0 ){
			return new String("#" + tgChannel.getChannelId());
		}
		return new String();
	}

	public TGChannel updateChannel(int id,short c1,short c2,short bnk,short prg,short vol,short bal,short cho,short rev,short pha,short tre,String name){
		TGChannel channel = getChannel(id);
		if( channel != null ){
			channel.setChannel(c1);
			channel.setEffectChannel(c2);
			channel.setBank(bnk);
			channel.setProgram(prg);
			channel.setVolume(vol);
			channel.setBalance(bal);
			channel.setChorus(cho);
			channel.setReverb(rev);
			channel.setPhaser(pha);
			channel.setTremolo(tre);
			channel.setName(name);
		}
		return channel;
	}

	public List<Integer> getFreeChannels( TGChannel forChannel ){
		List<Integer> freeChannels = new ArrayList<Integer>();

		for( int ch = 0 ; ch < TGSongManager.MAX_CHANNELS ; ch ++ ){
			if( !TGChannel.isPercussionChannel(ch) ){
				boolean isFreeChannel = true;

				Iterator<TGChannel> channelIt = getSong().getChannels();
				while( channelIt.hasNext() ){
					TGChannel channel = channelIt.next();
					if( forChannel == null || !forChannel.equals( channel ) ){
						if( channel.getChannel() == ch || channel.getEffectChannel() == ch){
							isFreeChannel = false;
						}
					}
				}

				if( isFreeChannel ){
					freeChannels.add(new Integer(ch));
				}
			}
		}
		return freeChannels;
	}

	public boolean isPercussionChannel( int channelId ){
		TGChannel channel = getChannel(channelId);
		if( channel != null ){
			return channel.isPercussionChannel();
		}
		return false;
	}

	public boolean isAnyPercussionChannel(){
		Iterator<TGChannel> it = getSong().getChannels();
		while( it.hasNext() ){
			TGChannel channel = it.next();
			if( channel.isPercussionChannel() ){
				return true;
			}
		}
		return false;
	}

	public boolean isAnyTrackConnectedToChannel( int channelId ){
		Iterator<TGTrack> it = getSong().getTracks();
		while( it.hasNext() ){
			TGTrack track = it.next();
			if( track.getChannelId() == channelId ){
				return true;
			}
		}
		return false;
	}
	// -------------------------------------------------------------- //

	private TGTrack createTrack(){
		TGTrack tgTrack = getFactory().newTrack();
		tgTrack.setNumber(getNextTrackNumber());
		tgTrack.setName(getDefaultTrackName(tgTrack));

		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			TGMeasure measure = getFactory().newMeasure(header);
			tgTrack.addMeasure(measure);
		}
		tgTrack.setStrings(createDefaultInstrumentStrings());

		TGColor.RED.copy(tgTrack.getColor());

		return tgTrack;
	}

	public TGTrack addTrack(){
		if(getSong().isEmpty()){
			setSong(newSong());
			return getLastTrack();
		}
		TGTrack tgTrack = createTrack();
		tgTrack.setChannelId(addChannel().getChannelId());
		addTrack(tgTrack);
		return tgTrack;
	}


	public void addTrack(TGTrack trackToAdd){
		this.orderTracks();
		int addIndex = -1;
		for(int i = 0;i < getSong().countTracks();i++){
			TGTrack track = getSong().getTrack(i);
			if(addIndex == -1 && track.getNumber() == trackToAdd.getNumber()){
				addIndex = i;
			}
			if(addIndex >= 0){
				track.setNumber(track.getNumber() + 1);
			}
		}
		if(addIndex < 0){
			addIndex = getSong().countTracks();
		}
		getSong().addTrack(addIndex,trackToAdd);
	}

	public void removeTrack(TGTrack track){
		removeTrack(track.getNumber());
	}

	public void removeTrack(int number){
		int nextNumber = number;
		TGTrack trackToRemove = null;
		orderTracks();
		Iterator<TGTrack> it = getSong().getTracks();
		while(it.hasNext()){
			TGTrack currTrack = it.next();
			if(trackToRemove == null && currTrack.getNumber() == nextNumber){
				trackToRemove = currTrack;
			}else if(currTrack.getNumber() == (nextNumber + 1)){
				currTrack.setNumber(nextNumber);
				nextNumber ++;
			}

		}
		getSong().removeTrack(trackToRemove);
	}

	private void orderTracks(){
		for(int i = 0;i < getSong().countTracks();i++){
			TGTrack minTrack = null;
			for(int trackIdx = i;trackIdx < getSong().countTracks();trackIdx++){
				TGTrack track = getSong().getTrack(trackIdx);
				if(minTrack == null || track.getNumber() < minTrack.getNumber()){
					minTrack = track;
				}
			}
			getSong().moveTrack(i,minTrack);
		}
	}

	public TGTrack getTrack(int number){
		TGTrack track = null;
		for (int i = 0; i < getSong().countTracks(); i++) {
			TGTrack currTrack = getSong().getTrack(i);
			if(currTrack.getNumber() == number){
				track = currTrack;
				break;
			}
		}
		return track;
	}

	public TGTrack getFirstTrack(){
		TGTrack track = null;
		if(!getSong().isEmpty()){
			track = getSong().getTrack(0);
		}
		return track;
	}

	public TGTrack getLastTrack(){
		TGTrack track = null;
		if(!getSong().isEmpty()){
			track = getSong().getTrack(getSong().countTracks() - 1);
		}
		return track;
	}

	public int getNextTrackNumber(){
		return (getSong().countTracks() + 1);
	}

	public String getDefaultTrackName(TGTrack tgTrack){
		if( tgTrack != null && tgTrack.getNumber() > 0 ){
			return new String("Track " + tgTrack.getNumber());
		}
		return new String();
	}

	public TGTrack cloneTrack(TGTrack track){
		TGTrack clone = track.clone(getFactory(),getSong());
		clone.setNumber(getNextTrackNumber());
		addTrack(clone);
		return clone;
	}

	public boolean moveTrackUp(TGTrack track){
		if(track.getNumber() > 1){
			TGTrack prevTrack = getTrack(track.getNumber() - 1);
			prevTrack.setNumber(prevTrack.getNumber() + 1);
			track.setNumber(track.getNumber() - 1);
			orderTracks();
			return true;
		}
		return false;
	}

	public boolean moveTrackDown(TGTrack track){
		if(track.getNumber() < getSong().countTracks()){
			TGTrack nextTrack = getTrack(track.getNumber() + 1);
			nextTrack.setNumber(nextTrack.getNumber() - 1);
			track.setNumber(track.getNumber() + 1);
			orderTracks();
			return true;
		}
		return false;
	}

	public void changeTimeSignature(long start,TGTimeSignature timeSignature,boolean toEnd){
		changeTimeSignature(getMeasureHeaderAt(start),timeSignature,toEnd);
	}

	public void changeTimeSignature(TGMeasureHeader header,TGTimeSignature timeSignature,boolean toEnd){
		//asigno el nuevo ritmo
		timeSignature.copy(header.getTimeSignature());

		long nextStart = header.getStart() + header.getLength();
		List<TGMeasureHeader> measures = getMeasureHeadersBeforeEnd(header.getStart() + 1);
		Iterator<TGMeasureHeader> it = measures.iterator();
		while(it.hasNext()){
			TGMeasureHeader nextHeader = it.next();

			long theMove = nextStart - nextHeader.getStart();

			//moveMeasureComponents(nextHeader,theMove);
			moveMeasureHeader(nextHeader,theMove,0);

			if(toEnd){
				timeSignature.copy(nextHeader.getTimeSignature());
			}
			nextStart = nextHeader.getStart() + nextHeader.getLength();
		}
		moveOutOfBoundsBeatsToNewMeasure(header.getStart());
	}

	public void moveOutOfBoundsBeatsToNewMeasure(long start){
		Iterator<TGTrack> it = getSong().getTracks();
		while( it.hasNext() ){
			TGTrack track = it.next();
			getTrackManager().moveOutOfBoundsBeatsToNewMeasure(track, start);
		}
	}

	public void changeTripletFeel(long start,int tripletFeel,boolean toEnd){
		changeTripletFeel(getMeasureHeaderAt(start),tripletFeel,toEnd);
	}

	public void changeTripletFeel(TGMeasureHeader header,int tripletFeel,boolean toEnd){
		//asigno el nuevo tripletFeel
		header.setTripletFeel(tripletFeel);

		if(toEnd){
			List<TGMeasureHeader> measures = getMeasureHeadersBeforeEnd(header.getStart() + 1);
			Iterator<TGMeasureHeader> it = measures.iterator();
			while(it.hasNext()){
				TGMeasureHeader nextHeader = it.next();
				nextHeader.setTripletFeel(tripletFeel);
			}
		}
	}

	public void changeTempos(long start,TGTempo tempo,boolean toEnd){
		changeTempos(getMeasureHeaderAt(start),tempo,toEnd);
	}

	public void changeTempos(TGMeasureHeader header,TGTempo tempo,boolean toEnd){
		int oldValue = header.getTempo().getValue();
		Iterator<TGMeasureHeader> it = getMeasureHeadersAfter(header.getNumber() - 1).iterator();
		while(it.hasNext()){
			TGMeasureHeader nextHeader = it.next();
			if(toEnd || nextHeader.getTempo().getValue() == oldValue){
				changeTempo(nextHeader,tempo);
			}else{
				break;
			}
		}
	}

	public void changeTempo(TGMeasureHeader header,TGTempo tempo){
		tempo.copy(header.getTempo());
	}

	public void changeOpenRepeat(long start){
		TGMeasureHeader header = getMeasureHeaderAt(start);
		header.setRepeatOpen(!header.isRepeatOpen());
	}

	public void changeCloseRepeat(long start,int repeatClose){
		TGMeasureHeader header = getMeasureHeaderAt(start);
		header.setRepeatClose(repeatClose);
	}

	public void changeAlternativeRepeat(long start,int repeatAlternative){
		TGMeasureHeader header = getMeasureHeaderAt(start);
		header.setRepeatAlternative(repeatAlternative);
	}

	public TGMeasureHeader addNewMeasureBeforeEnd(){
		TGMeasureHeader lastHeader = getLastMeasureHeader();
		TGMeasureHeader header = getFactory().newHeader();
		header.setNumber((lastHeader.getNumber() + 1));
		header.setStart((lastHeader.getStart() + lastHeader.getLength()));
		header.setRepeatOpen(false);
		header.setRepeatClose(0);
		header.setTripletFeel(lastHeader.getTripletFeel());
		lastHeader.getTimeSignature().copy(header.getTimeSignature());
		lastHeader.getTempo().copy(header.getTempo());
		getSong().addMeasureHeader(header);

		Iterator<TGTrack> it = getSong().getTracks();
		while(it.hasNext()){
			TGTrack track = it.next();
			getTrackManager().addNewMeasureBeforeEnd(track,header);
		}
		return header;
	}

	public void addNewMeasure(int number){
		//Obtengo un clon para el nuevo Header.
		TGMeasureHeader header = null;
		if(number == 1){
			header = getMeasureHeader(number).clone(getFactory());
		}else{
			header = getMeasureHeader((number - 1)).clone(getFactory());
			header.setStart(header.getStart() + header.getLength());
			header.setNumber(header.getNumber() + 1);
		}
		header.setMarker(null);
		header.setRepeatOpen(false);
		header.setRepeatAlternative(0);
		header.setRepeatClose(0);

		//Si hay Headers siguientes los muevo
		TGMeasureHeader nextHeader = getMeasureHeader(number);
		if(nextHeader != null){
			moveMeasureHeaders(getMeasureHeadersBeforeEnd(nextHeader.getStart()),header.getLength(),1,true);
		}

		//Agrego el header a la lista
		addMeasureHeader( (header.getNumber() - 1) ,header);

		//Agrego los compases en todas las pistas
		Iterator<TGTrack> it = getSong().getTracks();
		while(it.hasNext()){
			TGTrack track = it.next();
			getTrackManager().addNewMeasure(track,header);
		}
	}

	public List<TGMeasure> getMeasures(long start){
		List<TGMeasure> measures = new ArrayList<TGMeasure>();
		Iterator<TGTrack> it = getSong().getTracks();
		while(it.hasNext()){
			TGTrack track = it.next();
			TGMeasure measure = getTrackManager().getMeasureAt(track,start);
			if(measure != null){
				measures.add(measure);
			}
		}
		return measures;
	}

	public TGTrack replaceTrack(TGTrack track){
		TGTrack current = getTrack(track.getNumber());
		if(current != null){
			track.copy(getFactory(), getSong(), current);
		}
		return current;
	}

	public TGMeasureHeader getFirstMeasureHeader(){
		TGMeasureHeader firstHeader = null;
		for(int i = 0;i < getSong().countMeasureHeaders();i++){
			TGMeasureHeader currHeader = getSong().getMeasureHeader(i);
			if(firstHeader == null || (currHeader.getStart() < firstHeader.getStart())){
				firstHeader = currHeader;
			}
		}
		return firstHeader;
	}

	public TGMeasureHeader getLastMeasureHeader(){
		int lastIndex = getSong().countMeasureHeaders() - 1;
		return getSong().getMeasureHeader(lastIndex);
	}

	public TGMeasureHeader getPrevMeasureHeader(TGMeasureHeader header){
		int prevIndex = header.getNumber() - 1;
		if(prevIndex > 0){
			return getSong().getMeasureHeader(prevIndex - 1);
		}
		return null;
	}

	public TGMeasureHeader getNextMeasureHeader(TGMeasureHeader header){
		int nextIndex = header.getNumber();
		if(nextIndex < getSong().countMeasureHeaders()){
			return getSong().getMeasureHeader(nextIndex);
		}
		return null;
	}

	public TGMeasureHeader getMeasureHeaderAt(long start){
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			long measureStart = header.getStart();
			long measureLength = header.getLength();
			if(start >= measureStart && start < measureStart + measureLength){
				return header;
			}
		}
		return null;
	}

	public TGMeasureHeader getMeasureHeader(int number){
		for (int i = 0; i < getSong().countMeasureHeaders(); i++) {
			TGMeasureHeader header = getSong().getMeasureHeader(i);
			if(header.getNumber() == number){
				return header;
			}
		}
		return null;
	}

	public int getMeasureHeaderIndex(TGMeasureHeader mh){
		for (int i = 0; i < getSong().countMeasureHeaders(); i++) {
			TGMeasureHeader header = getSong().getMeasureHeader(i);
			if(header.getNumber() == mh.getNumber()){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Retorna Todos los desde Start hasta el final del compas
	 */
	public List<TGMeasureHeader> getMeasureHeadersBeforeEnd(long fromStart) {
		List<TGMeasureHeader> headers = new ArrayList<TGMeasureHeader>();
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if (header.getStart() >= fromStart) {
				headers.add(header);
			}
		}
		return headers;
	}

	/**
	 * Retorna Todos los desde Start hasta el final del compas
	 */
	public List<TGMeasureHeader> getMeasureHeadersAfter(int number) {
		List<TGMeasureHeader> headers = new ArrayList<TGMeasureHeader>();
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if (header.getNumber() > number) {
				headers.add(header);
			}
		}
		return headers;
	}

	/**
	 * Retorna Todos los desde Start hasta el final del compas
	 */
	public List<TGMeasureHeader> getMeasureHeadersBetween(long p1,long p2) {
		List<TGMeasureHeader> headers = new ArrayList<TGMeasureHeader>();
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if ((header.getStart() + header.getLength()) > p1  &&  header.getStart() < p2) {
				headers.add(header);
			}
		}
		return headers;
	}

	public void removeLastMeasure(){
		removeLastMeasureHeader();
	}

	public void removeMeasure(long start){
		removeMeasureHeader(start);
	}

	public void removeMeasure(int number){
		removeMeasureHeader(number);
	}

	/**
	 * Agrega un Compas
	 */
	public void addMeasureHeader(TGMeasureHeader measure){
		getSong().addMeasureHeader(measure);
	}

	/**
	 * Agrega un Compas
	 */
	public void addMeasureHeader(int index,TGMeasureHeader measure){
		getSong().addMeasureHeader(index,measure);
	}

	public void removeMeasureHeaders(int n1,int n2){
		for(int i = n1; i <= n2; i ++){
			TGMeasureHeader measure = getMeasureHeader(n1);
			removeMeasureHeader(measure);
		}
	}

	public void removeLastMeasureHeader(){
		removeMeasureHeader(getLastMeasureHeader());
	}

	public void removeMeasureHeader(long start){
		removeMeasureHeader(getMeasureHeaderAt(start));
	}

	public void removeMeasureHeader(int number){
		removeMeasureHeader(getMeasureHeader(number));
	}

	public void removeMeasureHeader(TGMeasureHeader header){
		long start = header.getStart();
		long length = header.getLength();

		Iterator<TGTrack> it = getSong().getTracks();
		while(it.hasNext()){
			TGTrack track = it.next();
			getTrackManager().removeMeasure(track,start);
		}
		moveMeasureHeaders(getMeasureHeadersBeforeEnd(start + 1),-length,-1,true);
		getSong().removeMeasureHeader(header.getNumber() - 1);
	}

	public TGMeasureHeader replaceMeasureHeader(TGMeasureHeader newMeasure){
		TGMeasureHeader header = getMeasureHeaderAt(newMeasure.getStart());
		header.makeEqual(newMeasure.clone(getFactory()));
		return header;
	}

	public void moveMeasureHeaders(List<TGMeasureHeader> headers,long theMove,int numberMove,boolean moveComponents) {
		if(moveComponents){
			Iterator<TGMeasureHeader> it = headers.iterator();
			while(it.hasNext()){
				TGMeasureHeader header = it.next();
				moveMeasureComponents(header,theMove);
			}
		}
		Iterator<TGMeasureHeader> it = headers.iterator();
		while (it.hasNext()) {
			TGMeasureHeader header = it.next();
			moveMeasureHeader(header,theMove,numberMove);
		}
	}

	/**
	 * Mueve el compas
	 */
	public void moveMeasureHeader(TGMeasureHeader header,long theMove,int numberMove){
		header.setNumber(header.getNumber() + numberMove);
		header.setStart(header.getStart() + theMove);
	}

	/**
	 * Mueve el compas
	 */
	public void moveMeasureComponents(TGMeasureHeader header,long theMove){
		Iterator<TGTrack> it = getSong().getTracks();
		while(it.hasNext()){
			TGTrack track = it.next();
			getTrackManager().moveMeasure(getTrackManager().getMeasure(track,header.getNumber()),theMove);
		}
	}

	/**
	 * Retorna true si el start esta en el rango del compas
	 */
	public boolean isAtPosition(TGMeasureHeader header,long start){
		return (start >= header.getStart() && start < header.getStart() + header.getLength());
	}

	public TGMarker updateMarker(int measure,String title,TGColor color){
		TGMeasureHeader header = getMeasureHeader(measure);
		if(header != null){
			if(!header.hasMarker()){
				header.setMarker(getFactory().newMarker());
			}
			header.getMarker().setMeasure(measure);
			header.getMarker().setTitle(title);
			header.getMarker().getColor().setR(color.getR());
			header.getMarker().getColor().setG(color.getG());
			header.getMarker().getColor().setB(color.getB());
			return header.getMarker();
		}
		return null;
	}

	public TGMarker updateMarker(TGMarker marker){
		return updateMarker(marker.getMeasure(),marker.getTitle(),marker.getColor());
	}

	public void removeMarker(TGMarker marker){
		if(marker != null){
			removeMarker(marker.getMeasure());
		}
	}

	public void removeMarker(int number){
		TGMeasureHeader header = getMeasureHeader(number);
		if(header != null && header.hasMarker()){
			header.setMarker(null);
		}
	}

	public void removeAllMarkers(){
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if(header.hasMarker()){
				header.setMarker(null);
			}
		}
	}

	public List<TGMarker> getMarkers(){
		List<TGMarker> markers = new ArrayList<TGMarker>();
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if(header.hasMarker()){
				markers.add(header.getMarker());
			}
		}
		return markers;
	}

	public TGMarker getMarker(int number){
		TGMeasureHeader header = getMeasureHeader(number);
		if(header != null && header.hasMarker()){
			return header.getMarker();
		}
		return null;
	}

	public TGMarker getPreviousMarker(int from){
		TGMeasureHeader previous = null;
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if(header.hasMarker() && header.getNumber() < from){
				if(previous == null || previous.getNumber() < header.getNumber()){
					previous = header;
				}
			}
		}
		return (previous != null)?previous.getMarker():null;
	}

	public TGMarker getNextMarker(int from){
		TGMeasureHeader next = null;
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if(header.hasMarker() && header.getNumber() > from){
				if(next == null || next.getNumber() > header.getNumber()){
					next = header;
				}
			}
		}
		return (next != null)?next.getMarker():null;
	}

	public TGMarker getFirstMarker(){
		TGMeasureHeader first = null;
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if(header.hasMarker()){
				if(first == null || header.getNumber() < first.getNumber()){
					first = header;
				}
			}
		}
		return (first != null)?first.getMarker():null;
	}

	public TGMarker getLastMarker(){
		TGMeasureHeader next = null;
		Iterator<TGMeasureHeader> it = getSong().getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = it.next();
			if(header.hasMarker()){
				if(next == null || header.getNumber() > next.getNumber()){
					next = header;
				}
			}
		}
		return (next != null)?next.getMarker():null;
	}

	public void autoCompleteSilences(){
		Iterator<TGTrack> it = getSong().getTracks();
		while(it.hasNext()){
			TGTrack track = it.next();
			getTrackManager().autoCompleteSilences(track);
		}
	}

	public void orderBeats(){
		Iterator<TGTrack> it = getSong().getTracks();
		while(it.hasNext()){
			TGTrack track = it.next();
			getTrackManager().orderBeats(track);
		}
	}

	public List<TGString> createDefaultInstrumentStrings(){
		List<TGString> strings = new ArrayList<TGString>();
		strings.add(newString(getFactory(),1, 64));
		strings.add(newString(getFactory(),2, 59));
		strings.add(newString(getFactory(),3, 55));
		strings.add(newString(getFactory(),4, 50));
		strings.add(newString(getFactory(),5, 45));
		strings.add(newString(getFactory(),6, 40));
		return strings;
	}

	public static List<TGString> createPercussionStrings(TGFactory factory,int stringCount){
		List<TGString> strings = new ArrayList<TGString>();
		for(int i = 1;i <= stringCount; i++){
			strings.add(newString(factory,i, 0));
		}
		return strings;
	}

	public static TGString newString(TGFactory factory,int number,int value){
		TGString string = factory.newString();
		string.setNumber(number);
		string.setValue(value);
		return string;
	}

	public static long getDivisionLength(TGMeasureHeader header){
		long defaultLenght = TGDuration.QUARTER_TIME;
		int denominator = header.getTimeSignature().getDenominator().getValue();
		switch(denominator){
			case TGDuration.EIGHTH:
				if(header.getTimeSignature().getNumerator() % 3 == 0){
					defaultLenght += TGDuration.QUARTER_TIME / 2;
				}
				break;
		}
		return defaultLenght;
	}
}
