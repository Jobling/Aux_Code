package net.floodlightcontroller.mactracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class ServerInfo extends Info{    
    /**
     * Constructor
     * @return JSON ready object with null values
     */
	public ServerInfo() {
		super("hostname", "port");
	}
	
    /**
     * Constructor requires ipToken and portToken
     * @param ip: ip of IoT Server
     * @param port: port of IoT Server's mac address service
     * @return JSON ready object with set values
     */
	public ServerInfo(String ip, String port) {
		super("hostname", "port", ip, port);
	}
	
	
    public String getBaseURL(){
    	return "http://" + this.ip + ":" + this.port + "/devices/";
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

}
