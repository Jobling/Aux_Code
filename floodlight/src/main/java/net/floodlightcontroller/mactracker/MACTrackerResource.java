package net.floodlightcontroller.mactracker;

import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MACTrackerResource extends ServerResource{
	protected static Logger log = LoggerFactory.getLogger(MACTrackerResource.class);
	
	@Put
	public boolean insert(){
		log.info("Received PUT request");
		return false;
	}
}
