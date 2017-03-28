package net.floodlightcontroller.mactracker;

import java.util.Set;
import net.floodlightcontroller.core.module.IFloodlightService;

public interface IMACTrackerService extends IFloodlightService{
    /**
     * GET current registered IoT servers on Floodlight Controller.
     * @return Set<String>
     * @param mac The MAC address to delete.
     * @param port The logical port the host is attached to.
     */
	public Set<String> getServers();
    
	/**
     * Registers IoT Server on floodlight controller.
     * @param server The url that is listening for device query.
     * @return boolean
     */
	public boolean putServerURL(ServerURL server);
}
