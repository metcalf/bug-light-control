package lightcontrol.servicetracker;

import java.io.IOException;

import lightcontrol.LightControl;

import org.osgi.framework.BundleContext;
import com.buglabs.application.AbstractServiceTracker;
import com.buglabs.bug.module.lcd.pub.IModuleDisplay;
import com.buglabs.bug.module.vonhippel.pub.IVonHippelModuleControl;

public class LightControlServiceTracker extends AbstractServiceTracker {
	
	private LightControl app;
	
	public LightControlServiceTracker(BundleContext context) {
		super(context);
	}
	
	public void doStart() {
		IModuleDisplay display = (IModuleDisplay) getService( IModuleDisplay.class );
		IVonHippelModuleControl control = (IVonHippelModuleControl) getService( IVonHippelModuleControl.class );
		try {
			app = new LightControl(display, control);
			app.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doStop() {
		app.stop();
	}
	
	public void initServices() {
		getServices().add("com.buglabs.bug.module.lcd.pub.IModuleDisplay");
		getServices().add("com.buglabs.bug.module.vonhippel.pub.IVonHippelModuleControl");
	}

}
