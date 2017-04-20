package net.floodlightcontroller.mactracker;

public class MACInfo extends Info{
    /**
     * Constructor
     * @return JSON ready object with null values
     */
	public MACInfo() {
		super("server_ip", "server_port");
	}
	
    /**
     * Constructor requires ipToken and portToken
     * @param ip: ip of network associated to a MAC Address
     * @param port: port of network associated to a MAC Address
     * @return JSON ready object with set values
     */
	public MACInfo(String ip, String port) {
		super("server_ip", "server_port", ip, port);
	}

}
