package net.floodlightcontroller.mactracker;

import java.io.IOException;
import java.util.Set;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MACTrackerServerResource extends ServerResource{
	protected static Logger log = LoggerFactory.getLogger(MACTrackerServerResource.class);
	
	@Get
	public String getServers(){
		log.info("Received GET request.");
		IMACTrackerService mactrack = (IMACTrackerService) getContext().getAttributes().get(IMACTrackerService.class.getCanonicalName());
		Set<ServerInfo> servers = mactrack.getServers();
		
		String output = "<!DOCTYPE html>\n<html>\n<head>Servers</head>\n<body>\n";
		for(ServerInfo server : servers){
			output += "<p>" + server.getBaseURL() + "</p>\n";
		}
		output += "</body>\n</html>";
		
		return output;
	}
	
	@Put("json")
	public String putServer(String clientJSON){
		log.info("Received PUT request.");
		ServerInfo server = new ServerInfo();
		try{
			server.setFromJSON(clientJSON);
		}catch(IOException e){
			log.error("Could not parse JSON {}", e.getMessage());
		}
		
		IMACTrackerService mactrack = (IMACTrackerService) getContext().getAttributes().get(IMACTrackerService.class.getCanonicalName());
		if(mactrack.putServerURL(server)){
	        setStatus(Status.SUCCESS_OK);
	        return "{\"status\":\"ok\"}";
		} else return "Something went wrong.";
	}
}
