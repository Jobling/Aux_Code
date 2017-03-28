package net.floodlightcontroller.mactracker;

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
}
