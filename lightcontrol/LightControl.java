package lightcontrol;

import java.awt.Frame;
import java.awt.GridLayout;
import java.io.IOException;

import lightcontrol.ImagePanel;

import com.buglabs.bug.module.lcd.pub.IModuleDisplay;
import com.buglabs.bug.module.vonhippel.pub.IVonHippelModuleControl;


public class LightControl {
	
	private IModuleDisplay display;
	private IVonHippelModuleControl control;
	
	public static final int OFF = 0;
	public static final int ON = 1;
	public static final int DIM = 2;
	public static final int RESET = 3;
	public static final String[] NAMES = {"Off","On","Dim","Reset"};
	
	private static final int[] PINS = {0,1,2,3};
	
	private static final int RESET_TIME = 2000;
	private static final int PIN_TIME = 1000;
	
	private int offSeconds;
	private int time;
	
	private Frame frame;
	private ImagePanel panel; 
	private LightTimer timer;
	
	private int mode;
	private boolean killTimer;
	
	public LightControl(IModuleDisplay display, IVonHippelModuleControl control) throws IOException{
		this.display = display;
		this.control = control;
		offSeconds = (int)(0.25*3600);
		time = offSeconds;
		
		control.makeGPIOOut(PINS[ON]);
		control.makeGPIOOut(PINS[OFF]);
		control.makeGPIOOut(PINS[RESET]);
		control.makeGPIOOut(PINS[DIM]);
		
		control.setGPIO(PINS[ON]);
		control.setGPIO(PINS[OFF]);
		control.setGPIO(PINS[RESET]);
		control.setGPIO(PINS[DIM]);
		
		mode = -1;
		killTimer = true;
	}
	
	public void start() throws IOException{
		// Reset the microcontroller
		pulsePin(PINS[RESET]);
		pause(RESET_TIME);
		
		pulsePin(PINS[ON]);
		
		mode = ON;
		
		// Setup the display
		panel = new ImagePanel(this);
		
		frame = display.getFrame();
		frame.setLayout(new GridLayout(1,1));
		frame.add(panel);

		frame.show();
	}
	
	public void pulsePin(int num) throws IOException{
		control.clearGPIO(num);
		pause(PIN_TIME);
		control.setGPIO(num);
	}
	
	public void pause(long millis){
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() < start+millis);
	}
	
	public void stop(){
		frame.dispose();
	}

	public void setOffSeconds(int offSeconds) {
		this.offSeconds = offSeconds;
	}

	public int getOffSeconds() {
		return offSeconds;
	}

	public void setMode(int new_mode) {
		if(mode != new_mode){
			try {
				killTimer = true;
				if(timer != null && timer.isAlive()){
					while(timer.isAlive());
				}
				
				pulsePin(new_mode);
				mode = new_mode;
				setTime(getOffSeconds());

				if(new_mode != ON) {
					killTimer = false;
					timer = new LightTimer(panel, this);
					timer.start();
				}
				panel.changedMode();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setTime(int time) {
		this.time = time;
		this.panel.changedCount();
	}
	
	public void changeTime(int time) {
		setTime(time + getTime());
	}
	
	public void changeOffSeconds(int time) {
		setOffSeconds(time + getOffSeconds());
	}
	
	public void changeCount(int time) {
		changeTime(time);
		if(mode == ON)
			setOffSeconds(getTime());
	}

	public int getTime() {
		return time;
	}
	
	public int getMode(){
		return mode;
	}
	
	public boolean killTimer(){
		return killTimer;
	}

}