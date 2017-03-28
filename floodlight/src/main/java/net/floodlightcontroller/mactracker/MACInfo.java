package net.floodlightcontroller.mactracker;

public class MACInfo {
    protected String ip;
    protected String port;
    
    /**
     * Constructor requires macAddress and server information
     */
    public MACInfo(String ip, String port) {
        this.ip = ip;
        this.port = port;
        return;        
    }

    /**
     * Constructor requires macAddress and server information
     */
    public MACInfo(String ip) {
        this.ip = ip;
        return;        
    }
    
    /**
     * Sets ip
     * @param ip: IoT Network ip as String
     */
    public void setIP(String ip){
        this.ip = ip;
        return;                
    }
    
    /**
     * Sets port
     * @param port: IoT network port as String
     */
    public void setPort(String port){
        this.port = port;
        return;                
    }
    
    public String getNetworkIP(){
    	return this.ip;
    }
    
    public String getNetworkPort(){
    	return this.port;
    }
}
