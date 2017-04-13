package net.floodlightcontroller.mactracker;

import java.util.Map;
import java.util.Map.Entry;

import org.projectfloodlight.openflow.types.MacAddress;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MACTrackerDeviceResource extends ServerResource{
	protected static Logger log = LoggerFactory.getLogger(MACTrackerDeviceResource.class);

	@Get
	public String getDevices(){
		log.info("Received GET request.");
		IMACTrackerService mactrack = (IMACTrackerService) getContext().getAttributes().get(IMACTrackerService.class.getCanonicalName());
		Map<MacAddress, MACInfo> devices = mactrack.getDevices();
		
		String output = "";
		for(Entry<MacAddress, MACInfo> entry : devices.entrySet()){
			output += entry.getKey().toString() + " - " + entry.getValue().toString() + "\n";
		}
		
		return output;
	}
}
