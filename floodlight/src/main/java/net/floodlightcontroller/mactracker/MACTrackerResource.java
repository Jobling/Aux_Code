package net.floodlightcontroller.mactracker;

import java.io.IOException;
import java.util.Set;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;

public class MACTrackerResource extends ServerResource{
	protected static Logger log = LoggerFactory.getLogger(MACTrackerResource.class);
	
    protected void jsonToServerURL(String json, ServerURL url) throws IOException {
        MappingJsonFactory f = new MappingJsonFactory();
        JsonParser jp;
        
        try {
            jp = f.createParser(json);
        } catch (JsonParseException e) {
            throw new IOException(e);
        }
        
        jp.nextToken();
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IOException("Expected START_OBJECT");
        }
        
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() != JsonToken.FIELD_NAME) {
                throw new IOException("Expected FIELD_NAME");
            }
            
            String n = jp.getCurrentName();
            jp.nextToken();
            
            if(n.equals("hostname"))
            	url.hostname = jp.getText();
            else if (n.equals("port"))
            	url.port = jp.getText();
            else{ 
            	log.warn("Unrecognized field {} in " +
            		"parsing network definition", 
            		jp.getText());
            }
        }
        jp.close();
    }
    
	@Get
	public String getServers(){
		log.info("Received GET request.");
		IMACTrackerService mactrack = (IMACTrackerService) getContext().getAttributes().get(IMACTrackerService.class.getCanonicalName());
		Set<ServerURL> servers = mactrack.getServers();
		return servers.toString();
	}
	
	@Put("json")
	public String putServer(String clientJSON){
		log.info("Received PUT request.");
		ServerURL server = new ServerURL();
		try{
			jsonToServerURL(clientJSON, server);
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
