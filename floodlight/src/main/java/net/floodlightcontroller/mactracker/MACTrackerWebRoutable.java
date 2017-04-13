package net.floodlightcontroller.mactracker;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import net.floodlightcontroller.restserver.RestletRoutable;
import net.floodlightcontroller.virtualnetwork.NoOp;

public class MACTrackerWebRoutable implements RestletRoutable{
	@Override
	public Restlet getRestlet(Context context) {
		Router router = new Router(context);
		router.attach("/servers", MACTrackerServerResource.class);
		router.attach("/devices", MACTrackerDeviceResource.class);
		router.attachDefault(NoOp.class);
		return router;
	}

	@Override
	public String basePath(){
		return "/wm/mactracker";
	}

}
