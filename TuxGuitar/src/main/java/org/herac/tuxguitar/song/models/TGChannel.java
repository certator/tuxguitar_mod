package org.herac.tuxguitar.song.models;

import java.io.Serializable;

import org.herac.tuxguitar.song.factory.TGFactory;

public class TGChannel implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final short DEFAULT_PERCUSSION_CHANNEL = 9;
	public static final short DEFAULT_PERCUSSION_PROGRAM = 0;
	public static final short DEFAULT_PERCUSSION_BANK = 128;

	public static final short DEFAULT_BANK = 0;
	public static final short DEFAULT_PROGRAM = 25;
	public static final short DEFAULT_VOLUME = 127;
	public static final short DEFAULT_BALANCE = 64;
	public static final short DEFAULT_CHORUS = 0;
	public static final short DEFAULT_REVERB = 0;
	public static final short DEFAULT_PHASER = 0;
	public static final short DEFAULT_TREMOLO = 0;

	private int channelId;
	private short channel;
	private short effectChannel;
	private short bank;
	private short program;
	private short volume;
	private short balance;
	private short chorus;
	private short reverb;
	private short phaser;
	private short tremolo;
	private String name;

	public TGChannel() {
		this.channelId = 0;
		this.channel = 0;
		this.effectChannel = 0;
		this.bank = DEFAULT_BANK;
		this.program = DEFAULT_PROGRAM;
		this.volume = DEFAULT_VOLUME;
		this.balance = DEFAULT_BALANCE;
		this.chorus = DEFAULT_CHORUS;
		this.reverb = DEFAULT_REVERB;
		this.phaser = DEFAULT_PHASER;
		this.tremolo = DEFAULT_TREMOLO;
		this.name = new String();
	}

	public int getChannelId() {
		return this.channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public short getBalance() {
		return this.balance;
	}

	public void setBalance(short balance) {
		this.balance = balance;
	}

	public short getChannel() {
		return this.channel;
	}

	public void setChannel(short channel) {
		this.channel = channel;
	}

	public short getEffectChannel() {
		return this.effectChannel;
	}

	public void setEffectChannel(short effectChannel) {
		this.effectChannel = effectChannel;
	}

	public short getChorus() {
		return this.chorus;
	}

	public void setChorus(short chorus) {
		this.chorus = chorus;
	}

	public short getBank() {
		return (this.isPercussionChannel() ? DEFAULT_PERCUSSION_BANK : this.bank);
	}

	public void setBank(short bank) {
		this.bank = bank;
	}

	public short getProgram() {
		return this.program;
	}

	public void setProgram(short program) {
		this.program = program;
	}

	public short getPhaser() {
		return this.phaser;
	}

	public void setPhaser(short phaser) {
		this.phaser = phaser;
	}

	public short getReverb() {
		return this.reverb;
	}

	public void setReverb(short reverb) {
		this.reverb = reverb;
	}

	public short getTremolo() {
		return this.tremolo;
	}

	public void setTremolo(short tremolo) {
		this.tremolo = tremolo;
	}

	public short getVolume() {
		return this.volume;
	}

	public void setVolume(short volume) {
		this.volume = volume;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPercussionChannel(){
		return TGChannel.isPercussionChannel(this.getChannel());
	}

	public static boolean isPercussionChannel(int channel){
		return (channel == DEFAULT_PERCUSSION_CHANNEL);
	}

	public static void setPercussionChannel(TGChannel channel){
		channel.setChannel(DEFAULT_PERCUSSION_CHANNEL);
		channel.setEffectChannel(DEFAULT_PERCUSSION_CHANNEL);
		channel.setProgram(DEFAULT_PERCUSSION_PROGRAM);
		channel.setBank(DEFAULT_PERCUSSION_BANK);
	}

	public static TGChannel newPercussionChannel(TGFactory factory){
		TGChannel channel = factory.newChannel();
		TGChannel.setPercussionChannel(channel);
		return channel;
	}

	public TGChannel clone(TGFactory factory){
		TGChannel channel = factory.newChannel();
		copy(channel);
		return channel;
	}

	public void copy(TGChannel channel){
		channel.setChannelId(getChannelId());
		channel.setChannel(getChannel());
		channel.setEffectChannel(getEffectChannel());
		channel.setBank(getBank());
		channel.setProgram(getProgram());
		channel.setVolume(getVolume());
		channel.setBalance(getBalance());
		channel.setChorus(getChorus());
		channel.setReverb(getReverb());
		channel.setPhaser(getPhaser());
		channel.setTremolo(getTremolo());
		channel.setName(getName());
	}
}
