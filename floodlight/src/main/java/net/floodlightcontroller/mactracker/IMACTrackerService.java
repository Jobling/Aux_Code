package net.floodlightcontroller.mactracker;

import java.util.Set;
import net.floodlightcontroller.core.module.IFloodlightService;

public interface IMACTrackerService extends IFloodlightService{
	public boolean putServerURL(String newJsonURL);
	public Set<String> getServers();
}
