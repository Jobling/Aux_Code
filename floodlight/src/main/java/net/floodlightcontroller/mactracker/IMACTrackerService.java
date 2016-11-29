package net.floodlightcontroller.mactracker;

import net.floodlightcontroller.core.module.IFloodlightService;

public interface IMACTrackerService extends IFloodlightService{
	public boolean putServerURL(String newURL);
}
