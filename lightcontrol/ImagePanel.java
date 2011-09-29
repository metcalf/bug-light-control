package lightcontrol;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AWT Canvas with the ability to draw countdown and buttons
 * 
 */
public class ImagePanel extends Panel implements ActionListener {
	LightControl control;
	
	Button onB;
	Button offB;
	Button dimB;
	Button upB;
	Button downB;
	
	Label timeL;
	Label modeL;
	
	private static final int TIME_INCREMENT = 60;
	
	public ImagePanel() {
		
	}
	
	public ImagePanel(LightControl control) {
		this.control = control;
		
		setSize(320, 240);
		setBackground(Color.blue);
		setLayout(new BorderLayout());
		
		onB 	= new Button ("On");
		offB 	= new Button ("Off");
		dimB 	= new Button ("Dim");
		upB 	= new Button ("+");
		downB 	= new Button ("-");
		
		onB.addActionListener(this);
		offB.addActionListener(this);
		dimB.addActionListener(this);
		upB.addActionListener(this);
		downB.addActionListener(this);
		
		Panel timeP = new Panel();
		timeP.setBackground(Color.blue);
		timeP.setLayout(new GridLayout(1,3));
		timeP.setSize(320, 100);
		
		timeL = new Label();
		changedCount();
		
		modeL = new Label();
		changedMode();
		
		Panel timeCtrlP = new Panel();
		timeCtrlP.setBackground(Color.blue);
		timeCtrlP.setLayout(new GridLayout(2,1));
		timeCtrlP.setSize(50, 50);
		timeCtrlP.add(upB);
		timeCtrlP.add(downB);
		
		Panel ctrlP = new Panel();
		ctrlP.setBackground(Color.cyan);
		ctrlP.setLayout(new GridLayout(1,3));
		ctrlP.setSize(320, 100);
		ctrlP.add(onB);
		ctrlP.add(dimB);
		ctrlP.add(offB);
		
		timeP.add(timeL);
		timeP.add(modeL);
		timeP.add(timeCtrlP);
		this.add(timeP, BorderLayout.CENTER);
		this.add(ctrlP, BorderLayout.SOUTH);
	}
	
	public void changedCount(){
		timeL.setText(getTimeString());
	}
	
	public void changedMode(){
		modeL.setText(LightControl.NAMES[control.getMode()]);
	}
	
	
	public String getTimeString(){
		int time = control.getTime();
		int hours = (int)(time / 3600);
		int minutes = (int)(time % 3600 / 60);
		int seconds = (int)(time % 3600 % 60);
		String line = hours + ":";
		
		if (minutes < 10)
			line +="0";
		line += minutes + ":";
		
		if (seconds < 10)
			line += "0";
		line += seconds;
		
		return  line;
	}
	
	public void paintTime(){
		this.repaint();
	}

	public void actionPerformed(ActionEvent event) {
		Button fired = (Button) event.getSource();
		String label = fired.getLabel();
		
		if(label.equals("+")){
			control.changeCount(TIME_INCREMENT);
		} else if(label.equals("-")){
			control.changeCount(-TIME_INCREMENT);
		} else if(label.equals("On")){
			this.control.setMode(LightControl.ON);
		} else if(label.equals("Off")){
			this.control.setMode(LightControl.OFF);
		} else if(label.equals("Dim")){
			this.control.setMode(LightControl.DIM);
		} 

		
	}
}
