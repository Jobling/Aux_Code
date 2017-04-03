package net.floodlightcontroller.mactracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.restserver.IRestApiService;

public class MACTracker implements IFloodlightModule, IMACTrackerService, IOFMessageListener{
	protected IFloodlightProviderService floodlightProvider;
	protected Map<MacAddress, MACInfo> macToNetwork;
	protected Set<ServerURL> serversURL;
	protected IRestApiService restApi;
	protected static Logger logger;
	
	@Override
	public String getName(){
		return MACTracker.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		return (type.equals(OFType.PACKET_IN) && name.equals("forwarding"));
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices(){
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IMACTrackerService.class);
        return l;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		Map<Class<? extends IFloodlightService>, IFloodlightService> m = new HashMap<Class<? extends IFloodlightService>, IFloodlightService>();
		m.put(IMACTrackerService.class, this);
		return m;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
	    Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
	    l.add(IFloodlightProviderService.class);
	    l.add(IRestApiService.class);
	    return l;
	}

	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
	    restApi = context.getServiceImpl(IRestApiService.class);
		macToNetwork = new HashMap<MacAddress, MACInfo>();
	    logger = LoggerFactory.getLogger(MACTracker.class);
		serversURL = new HashSet<ServerURL>();
		// serversURL.add("http://127.0.0.1:8080/devices/");
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		restApi.addRestletRoutable(new MACTrackerWebRoutable());
	}
	
	@Override
	public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		switch (msg.getType()) {
		case PACKET_IN:
			processPacketIn(sw, (OFPacketIn)msg, cntx);
			break;
		default:
			logger.warn("Received unexpected message {}", msg);
			break;
		}
		return Command.CONTINUE;
	}
	
	protected void processPacketIn(IOFSwitch sw, OFPacketIn msg, FloodlightContext cntx) {
		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
	    MacAddress sourceMAC = eth.getSourceMACAddress();
	    if(macToNetwork.containsKey(sourceMAC)){
	    	logger.info(sourceMAC.toString() + " already registered.");
	    }else{
	    	logger.info("MAC is {}", sourceMAC.toString());
	    	// Try to register macAddress
	    	for(ServerURL server : serversURL){
	    		try{
		    		// GET mac address information;
	    			String json = server.getInfo(sourceMAC.toString());
		    		logger.info("json is {}", json.toString());
		    		
		    		// Add macAddress to list
		    		macToNetwork.put(sourceMAC, null);
		    		logger.info("MAC Address: {} seen on switch: {}",
		                    sourceMAC.toString(),
		                    sw.getId().toString());
		    		break;
		    	}catch (IOException e){
		    		logger.info("IOException on {}", server);
		    	}
	    	}
	    }
	}

	@Override
	public boolean putServerURL(ServerURL server){
		if(server.isValid()){
			serversURL.add(server);
			logger.info("Added server with url: {}", server.getBaseURL());
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Set<ServerURL> getServers(){
		return serversURL;
	}
}
