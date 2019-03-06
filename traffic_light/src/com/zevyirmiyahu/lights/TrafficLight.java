package com.zevyirmiyahu.lights;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/*
 * Simulates the logic/changing mechanism and renders a traffic light.
 */

public class TrafficLight implements Runnable {
	
	private int timer;
	private String ID; // name of traffic light
	private String currColor;
	private String nextColor;
	
	private int timeLeft; // time remaining until light change
	private int timeRed;
	private int timeYellow;
	private int timeGreen;
	
	// traffic light box start coordinates
	private int x;  
	private int y;
	
	// lights width/height
	private int lightWidth;
	private int lightHeight;
	
	// y-coordinates starts for each light
	private int redLightCoord_y;
	private int yellowLightCoord_y;
	private int greenLightCoord_y;
	
	private boolean isRed = false;
	private boolean isYellow = false;
	private boolean isGreen = false;
	private Thread thread;
	
	public TrafficLight(int x, int y, int timeDelay, int timeRed, int timeYellow, int timeGreen, String ID) {
		this.x = 64 + 4 * x;
		this.y = 64 + 4 * y;
		this.ID = ID;
		
		// since 60UPS, multiply by 60 converts 'X' to 'X' seconds
		this.timeRed = timeRed * 60; 
		this.timeGreen = (timeRed + timeGreen) * 60;
		this.timeYellow = (timeRed + timeGreen + timeYellow) * 60; // combines all times for logic mechanism of if-statement
		this.timer = timeDelay * 60; // need to offset time of each traffic light
		
		// position of y-coords of component pieces (circles)
		int ys = 64 + 4 * y;
		
		// dimensions of traffic light
		lightWidth = 96; 
		lightHeight = 96;
		
		// y-coordinates starts for each light
		redLightCoord_y = ys + 8;
		yellowLightCoord_y = ys + 16 + lightHeight;
		greenLightCoord_y = ys + 24 + 2 * lightHeight;
	}
	
	public int getTimeRed() {
		return timeRed;
	}
	
	public int getTimeYellow() {
		return timeYellow;
	}
	
	public int getTimeGreen() {
		return timeGreen;
	}
	
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	@Override
	public void run() {}

	public synchronized void start() {
		thread = new Thread(this, "Traffic Light");
		thread.start();		
	}
	
	private void logicController() {
		
		if(timer < timeRed) { 
			currColor = "Red";
			nextColor = "Green";
			timeLeft = (timeRed - timer) / 60;
			isRed = true;
			isYellow = false;
			isGreen = false;
		}
		else if(timer < timeGreen) {
			currColor = "Green";
			nextColor = "Yellow";
			timeLeft = (timeGreen - timer) / 60;
			isRed = false;
			isYellow = false;
			isGreen = true;
		}
		else if (timer < timeYellow) {
			currColor = "Yellow";
			nextColor = "Red";
			timeLeft = (timeYellow - timer) / 60;
			isRed = false;
			isYellow = true;
			isGreen = false;
		} 
		else {
			timer = -1; //reset timer
		}
		timer++;
	}
	
	public void update() {
		logicController();
	}
	
	public void render(Graphics g) {
		
		// ID 
		g.setColor(Color.WHITE);
		g.setFont(new Font("Ariel", 1, 25));
		g.drawString(ID, x + 2, y - 8);
		
		// Output and light change info
		g.setFont(new Font("Ariel", 0, 16));
		g.drawString("Current Color: " + currColor, x, 5 * lightHeight + 16);
		g.drawString("Next Color: " + nextColor, x, 5 * lightHeight + 48);
		g.drawString("Time Remaining: " + timeLeft + " sec.", x, 5 * lightHeight + 80);

		// TRAFFIC LIGHT FRAME
		g.setColor(Color.GRAY);
		g.fillRect(x, y, 112, 3 * lightHeight + 32);
		
		// LIGHTS
		g.setColor(new Color(0x7f0101)); //(0xad2626));
		g.fillOval(x + 8, redLightCoord_y, lightWidth, lightHeight);
		
		g.setColor(new Color(0x7f7d01)); //(0xabad26));
		g.fillOval(x + 8, yellowLightCoord_y, lightWidth, lightHeight);
		
		g.setColor(new Color(0x017f07)); //(0x26ad2e));
		g.fillOval(x + 8, greenLightCoord_y, lightWidth, lightHeight);
		
		// changing color rendering mechanism
		if(isRed) {
			g.setColor(Color.RED);
			g.fillOval(x + 8, redLightCoord_y, lightWidth, lightHeight);
		}
		if(isYellow) {
			g.setColor(Color.YELLOW);
			g.fillOval(x + 8, yellowLightCoord_y, lightWidth, lightHeight);		
		}
		if(isGreen) {
			g.setColor(Color.GREEN);
			g.fillOval(x + 8, greenLightCoord_y, lightWidth, lightHeight);
		}
	}
}
