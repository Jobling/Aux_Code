package net.floodlightcontroller.mactracker;

import java.util.Set;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MACTrackerResource extends ServerResource{
	protected static Logger log = LoggerFactory.getLogger(MACTrackerResource.class);
	
	@Get
	public String getServers(){
		IMACTrackerService mactrack = (IMACTrackerService) getContext().getAttributes().get(IMACTrackerService.class.getCanonicalName());
		Set<String> servers = mactrack.getServers();
		return servers.toString();
	}
	
	@Put("json")
	public String putServer(String clientURL){
		log.info("Received PUT request from {}:{}", getClientInfo().getAddress(), getClientInfo().getPort());
		return "Hello";
	}
}
