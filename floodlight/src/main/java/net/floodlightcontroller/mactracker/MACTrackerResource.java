package net.floodlightcontroller.mactracker;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MACTrackerResource extends ServerResource{
	protected static Logger log = LoggerFactory.getLogger(MACTrackerResource.class);
	
	@Get
	public String getSomething(){
		return "Hello World.";
	}
	
	@Put
	public String insert(){
		log.info("Received PUT request from {}:{}", getClientInfo().getAddress(), getClientInfo().getPort());
		return "Hello";
	}
}
