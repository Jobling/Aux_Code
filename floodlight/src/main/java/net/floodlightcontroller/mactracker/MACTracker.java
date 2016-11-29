package net.floodlightcontroller.mactracker;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;

import net.floodlightcontroller.core.IFloodlightProviderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.Set;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.restserver.IRestApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jsonreader.JsonReader;

public class MACTracker implements IFloodlightModule, IMACTrackerService, IOFMessageListener{
	protected IFloodlightProviderService floodlightProvider;
	protected Set<Long> macAddresses;
	protected Set<String> serversURL;
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
		// TODO Auto-generated method stub
		return false;
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
		macAddresses = new ConcurrentSkipListSet<Long>();
	    logger = LoggerFactory.getLogger(MACTracker.class);
		serversURL = new HashSet<String>();
		serversURL.add("http://127.0.0.1:8080/devices/");
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
	    Long sourceMACHash = eth.getSourceMACAddress().getLong();
	    if(!macAddresses.contains(sourceMACHash)){
	    	logger.info("MAC is {}", sourceMACHash);
	    	for(String serverURL : serversURL){
	    		try{
		    		// GET corresponding ip address;
	    			String url = serverURL.concat(eth.getSourceMACAddress().toString());
		    		JSONObject json = JsonReader.readJsonFromUrl(url);
		    		logger.info("json is {}", json.toString());
		    		
		    		// Add macAddress to list
		    		macAddresses.add(sourceMACHash);
		    		logger.info("MAC Address: {} seen on switch: {}",
		                    eth.getSourceMACAddress().toString(),
		                    sw.getId().toString());
		    		break;
		    	}catch (IOException e){
		    		logger.info("IOException on {}", serverURL);
		    	}catch (JSONException e){
		    		logger.info("JSONException on {}", serverURL);
		    	}
	    	}
	    }
	    return Command.CONTINUE;
	}

	@Override
	public boolean putServerURL(String newURL){
		serversURL.add(newURL);
		logger.info("Added url {}", newURL);
		return true;
	}

}
