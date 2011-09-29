package lightcontrol;

import lightcontrol.servicetracker.*;

import com.buglabs.util.ServiceFilterGenerator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	private LightControlServiceTracker stc;
	private ServiceTracker st;
	
	
	public void start(BundleContext context) throws Exception {
		//Create the service tracker and run it.
		stc = new lightcontrol.servicetracker.LightControlServiceTracker(context);
		Filter f = context.createFilter(ServiceFilterGenerator.generateServiceFilter(stc.getServices()));
		st = new ServiceTracker(context, f, stc);
		st.open();
		
	}

	public void stop(BundleContext context) throws Exception {
		stc.stop();
		st.close();
		
	}
}