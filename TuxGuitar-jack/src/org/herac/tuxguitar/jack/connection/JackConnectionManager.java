package org.herac.tuxguitar.jack.connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.jack.JackClient;
import org.herac.tuxguitar.jack.JackPortFlags;
import org.herac.tuxguitar.jack.JackPortTypes;
import org.herac.tuxguitar.jack.provider.JackClientProvider;
import org.herac.tuxguitar.util.TGException;

public class JackConnectionManager {
	
	private JackClient jackClient;
	private JackClientProvider jackClientProvider;
	private JackConnectionListener jackConnectionListener;
	private JackConnectionConfig jackConnectionConfig;
	
	private List jackConnections;
	private boolean autoConnectPorts;
	
	public JackConnectionManager(JackClientProvider jackClientProvider){
		this.jackClientProvider = jackClientProvider;
		this.jackConnectionConfig = new JackConnectionConfig(this);
		this.jackConnectionListener = new JackConnectionListener(this);
		this.jackConnections = new ArrayList();
	}
	
	public void initialize(){
		if( this.jackClient == null ){
			this.jackConnections.clear();
			this.jackClient = this.jackClientProvider.getJackClient();
			this.jackClient.addPortRegisterListener(this.jackConnectionListener);
			this.loadConfig();
			this.openJackClient(true);
			if( this.isAutoConnectPorts() && this.isJackClientOpen() ){
				this.connectAllPorts();
			}
		}
	}
	
	public void destroy(){
		if( this.jackClient != null ){
			this.jackConnections.clear();
			this.jackClient.removePortRegisterListener(this.jackConnectionListener);
			this.jackClient = null;
		}
	}
	
	public void openJackClient(boolean quiet) throws TGException {
		if(!this.isJackClientOpen() ){
			if( this.jackClient != null ){
				this.jackClient.open();
			}
		}
		if(!this.isJackClientOpen() && !quiet){
			throw new TGException("Jack server not running?");
		}
	}
	
	public void loadConfig(){
		this.jackConnectionConfig.load();
	}
	
	public void saveConfig(){
		this.jackConnectionConfig.save();
	}
	
	public void connectAllPorts(){
		this.openJackClient(false);
		
		Iterator it = this.jackConnections.iterator();
		while( it.hasNext() ){
			JackConnection jackConnection = (JackConnection) it.next();
			
			this.connectPorts(jackConnection);
		}
	}
	
	public void connectPorts(JackConnection jackConnection){
		this.openJackClient(false);
		
		List portNames = this.jackClient.getPortNames(JackPortTypes.JACK_ALL_TYPES, 0);
		String srcPortName = findPortNameById(jackConnection.getSrcPortId(), portNames);
		String dstPortName = findPortNameById(jackConnection.getDstPortId(), portNames);
		if( srcPortName != null && dstPortName != null ){
			this.jackClient.connectPorts(srcPortName, dstPortName);
		}
	}
	
	public void loadExistingConnections(){
		this.openJackClient(false);
		
		List existingConnections = new ArrayList();
		List existingPortNames = this.jackClient.getPortNames(JackPortTypes.JACK_ALL_TYPES, 0);
		
		this.loadExistingConnections(existingConnections);
		this.loadPreviousConnectionsToExistingList(existingConnections, existingPortNames);
		this.jackConnections.clear();
		this.jackConnections.addAll(existingConnections);
	}
	
	public void loadExistingConnections(List existingConnections){
		this.openJackClient(false);
		
		List srcPortNames = this.jackClient.getPortNames(JackPortTypes.JACK_ALL_TYPES, JackPortFlags.JACK_PORT_IS_OUTPUT);
		if( srcPortNames != null ){
			Iterator it = srcPortNames.iterator();
			while( it.hasNext() ){
				this.loadExistingConnections(existingConnections, (String) it.next());
			}
		}
	}
	
	public void loadExistingConnections(List existingConnections, String srcPortName){
		this.openJackClient(false);
		
		List dstPortNames = this.jackClient.getPortConnections(srcPortName);
		if( dstPortNames != null ){
			Iterator it = dstPortNames.iterator();
			while( it.hasNext() ){
				this.loadExistingConnection(existingConnections, srcPortName, (String) it.next());
			}
		}
	}
	
	public void loadExistingConnection(List existingConnections, String srcPortName, String dstPortName){
		this.openJackClient(false);
		
		JackConnection jackConnection = new JackConnection(createPortId(srcPortName), createPortId(dstPortName));
		if(!existingConnections.contains(jackConnection)){
			existingConnections.add(jackConnection);
		}
	}
	
	public void loadPreviousConnectionsToExistingList(List existingConnections, List existingPortNames){
		this.openJackClient(false);
		
		Iterator it = this.jackConnections.iterator();
		while( it.hasNext() ){
			JackConnection jackConnection = (JackConnection) it.next();
			
			String srcPortName = findPortNameById(jackConnection.getSrcPortId(), existingPortNames);
			String dstPortName = findPortNameById(jackConnection.getDstPortId(), existingPortNames);
			// If both ports are not null means "ports were disconnected".
			if( srcPortName == null || dstPortName == null ){
				existingConnections.add( jackConnection );
			}
		}
	}
	
	public String findPortNameById(long portId){
		this.openJackClient(false);
		
		return findPortNameById(portId, this.jackClient.getPortNames(JackPortTypes.JACK_ALL_TYPES, 0));
	}
	
	public String findPortNameById(long portId, List portNames){
		this.openJackClient(false);
		
		if( portNames != null ){
			Iterator it = portNames.iterator();
			while( it.hasNext() ){
				String portName = (String) it.next();
				if( portId == createPortId(portName) ){
					return portName;
				}
			}
		}
		
		return null;
	}
	
	public long createPortId(String portName){
		return portName.hashCode();
	}
	
	public boolean isJackClientOpen() {
		return ( this.jackClient != null && this.jackClient.isOpen());
	}
	
	public void clearJackConnections(){
		this.jackConnections.clear();
	}
	
	public void addJackConnection(JackConnection jackConnection){
		if(!this.jackConnections.contains(jackConnection)){
			this.jackConnections.add(jackConnection);
		}
	}
	
	public List getJackConnections() {
		return this.jackConnections;
	}
	
	public boolean isAutoConnectPorts() {
		return autoConnectPorts;
	}

	public void setAutoConnectPorts(boolean autoConnectPorts) {
		this.autoConnectPorts = autoConnectPorts;
	}
}
