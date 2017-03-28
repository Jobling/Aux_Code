package net.floodlightcontroller.mactracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class ServerURL {
    protected String hostname;
    protected String port;
    
    /**
     * Constructor sets parameters to null
     */
    public ServerURL() {
        this.hostname = null;
        this.hostname = null;
        return;        
    }
    
    /**
     * Constructor requires hostname and port
     * @param hostname: IoT Server's hostname
     * @param port: IoT Server's port
     */
    public ServerURL(String hostname, String port) {
        this.hostname = hostname;
        this.hostname = port;
        return;        
    }
    
    /**
     * Sets hostname
     * @param hostname: hostname as String
     */
    public void setName(String hostname){
        this.hostname = hostname;
        return;                
    }
    
    /**
     * Sets port
     * @param port: port as String
     */
    public void setPort(String port){
        this.port = port;
        return;                
    }
    
    public boolean isValid(){
    	return (this.hostname != null && this.port != null); 
    }
    
    public String getBaseURL(){
    	return "http://" + this.hostname + ":" + this.port + "/devices/";
    }
    
	private static String readAll(Reader rd) throws IOException{
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp = rd.read()) != -1){
			sb.append((char) cp);
		}
		return sb.toString();
	}
    
    /**
     * Try to get information on macAdress
     * @param macAddress macAddress being queried
     * @return json json containing information of macAdress's network
     * @throws IOException 
     * @throws  
     */    
    public String getInfo(String macAddress) throws IOException{
    	String url = getBaseURL() + macAddress;
		InputStream is = new URL(url).openStream();
		try{
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String json = readAll(rd);
			return json;
		}finally{
			is.close();
		}
    }


	@Override
	public String toString() {
		return "ServerURL [hostname=" + hostname + ", port=" + port + "]";
	}
}
