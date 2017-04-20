package net.floodlightcontroller.mactracker;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;

public abstract class Info {
    protected String ip;
    protected String port;
    protected String ipToken;
    protected String portToken;
    
    /**
     * Constructor requires ip and port
     * @param ipToken: JSON field for ip
     * @param portToken: JSON field for port
     * @param ip: remote ip
     * @param port: remote port
     * @return object with set values
     */
    public Info(String ipToken, String portToken, String ip, String port) {
        this.ipToken = ipToken;
        this.portToken = portToken;
        this.ip = ip;
        this.port = port;
    }

    /**
     * Constructor requires ipToken and portToken
     * @param ipToken: JSON field for ip
     * @param portToken: JSON field for port
     * @return JSON ready object with null values
     */
    public Info(String ipToken, String portToken){
        this(ipToken, portToken, null, null);        
    }
    
    /**
     * Constructor
     * @return object with null values
     */
    public Info(){
        this(null, null, null, null);        
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
            
            if(n.equals(this.ipToken))
            	this.ip = jp.getText();
            else if (n.equals(this.portToken))
            	this.port = jp.getText();
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

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [ip=" + this.ip + ", port=" + this.port + "]";
	}
}
