package net.floodlightcontroller.mactracker;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;

public abstract class Info {
    protected String ip;
    protected String port;
    
    /**
     * Constructor requires ip and port
     * @param ip: remote ip
     * @param port: remote port
     */
    public Info(String ip, String port) {
        this.ip = ip;
        this.port = port;
        return;        
    }
    
    /**
     * Constructor requires ip and port
     * @return object with null parameters
     */
    public Info(){
        this(null, null);        
    }
    
	/**
     * Sets ip and port from json string.
     * @param json: json as String
	 * @throws IOException 
     */
    public void setFromJSON(String json) throws IOException{
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
            	this.ip = jp.getText();
            else if (n.equals("port"))
            	this.port = jp.getText();
            else
            	throw new IOException("Unrecognized field.");
        }
        jp.close();
    }

    /**
     * Gets ip
     * @return ip: ip as String
     */
    public String getIP(){
    	return this.ip;
    }
    
    /**
     * Gets port
     * @return port: port as String
     */
    public String getPort(){
    	return this.port;
    }
    
    protected boolean isValid(){
    	return (this.ip != null && this.port != null); 
    }
}
