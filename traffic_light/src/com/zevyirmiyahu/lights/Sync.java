package com.zevyirmiyahu.lights;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

/*
 * Responsible for handling multiple traffic lights and synchronizing proper
 * light changes for a given intersection. ArrayList was use to possibly expand
 * simulation to great than 2 traffic lights for later versions, altougth a new mathematical model 
 * might need to be implemented.
 *  
 */

public class Sync {
	
	private int timeDelay = 0;; // handles the delay timing for multiple lights
	private int timeRed;
	private int timeYellow;
	private int timeGreen;
	private int numberOfIntersectRoads;
	
	protected static ArrayList<TrafficLight> trafficLights = new ArrayList<TrafficLight>();
	protected static HashMap<String, int[]> map = new HashMap<String, int[]>();
	
	public Sync(int timeRed, int timeYellow, int timeGreen, int numberOfIntersectRoads) {
		
		int[] timeInputs = new int[3]; //store original timeRed, timeYellow, timeGreen units
		timeInputs[0] = timeRed;
		timeInputs[1] = timeYellow;
		timeInputs[2] = timeGreen;
		
		map.put("Traffic1", timeInputs);
		
		this.timeRed = timeRed;
		this.timeYellow = timeYellow;
		this.timeGreen = timeGreen;
		this.numberOfIntersectRoads = numberOfIntersectRoads;
		add();
	}
	
	public void add() {
		int xOffset = 0;
		for(int i = 0; i < numberOfIntersectRoads; i++) {
			if(i > 0) syncController(i);
			trafficLights.add(new TrafficLight(xOffset, 0, timeDelay, timeRed, timeYellow, timeGreen, "Traffic" + (i + 1)));	
			xOffset += 50;
		}
	}
	
	public void remove() {
			trafficLights.clear();
			map.clear();
	}
	
	// ensures all lights change accordingly with no simultaneous green lights
	private void syncController(int currIndex) {
		// get values
		int[] timeInputs = new int[3];
		int timeRedPrev = map.get("Traffic" + currIndex)[0];
		int timeYellowPrev = map.get("Traffic" + currIndex)[1];
		int timeGreenPrev = map.get("Traffic" + currIndex)[2];
		
		this.timeRed = timeYellowPrev + timeGreenPrev;
		this.timeGreen = (timeGreenPrev - timeRedPrev);
		this.timeYellow = timeRedPrev - this.timeGreen;
		this.timeDelay = this.timeRed; // gets very first light
		
		timeInputs[0] = this.timeRed;
		timeInputs[1] = this.timeYellow;
		timeInputs[2] = this.timeGreen;
		// set new values into map
		map.put("Traffic" + (currIndex + 1), timeInputs);
	}
	
	public void start() {
		for(TrafficLight tl : trafficLights) {
			tl.start();
		}
	}
	
	public void update() {
		for(TrafficLight tl : trafficLights) {
			tl.update();
		}
	}
	
	public void render(Graphics g) {
		if(trafficLights.equals(null)) return;
		try {
		for(TrafficLight tl: trafficLights) {
			tl.render(g);
		}
		} catch(ConcurrentModificationException e) {
			System.out.println("Safe Concurrent Modification Made..");
		}
	}
}
