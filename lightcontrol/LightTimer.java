package lightcontrol;

public class LightTimer extends Thread {
	
	ImagePanel panel;
	LightControl control;
	
	public LightTimer(ImagePanel panel, LightControl control){
		this.panel = panel;
		this.control = control;
	}
	
	public void run() {
		long start = System.currentTimeMillis();
		int count = 0;
		while(control.getTime() > 0 && !control.killTimer()){
			control.changeCount(-1);
			count++;
			while(System.currentTimeMillis() < start+count*100 && !control.killTimer());
		}
		if(control.getTime() == 0){
			new Thread() { public void run() { control.setMode(LightControl.ON);}; }.start();
		}
	}

}
