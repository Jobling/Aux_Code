package net.floodlightcontroller.mactracker;

import java.util.Map;
import java.util.Set;

import org.projectfloodlight.openflow.types.MacAddress;
import net.floodlightcontroller.core.module.IFloodlightService;

public interface IMACTrackerService extends IFloodlightService{
    /**
     * GET current registered devices on Floodlight Controller.
     * @return Map<MacAddress, MACInfo>
     */
	public Map<MacAddress, MACInfo> getDevices();
	
	/**
     * GET current registered IoT servers on Floodlight Controller.
     * @return Set<String>
     */
	public Set<ServerInfo> getServers();
    
	/**
     * Registers IoT Server on floodlight controller.
     * @param server The url that is listening for device query.
     * @return boolean
     */
	public boolean putServerURL(ServerInfo server);
}
