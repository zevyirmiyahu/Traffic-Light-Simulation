package com.zevyirmiyahu.output;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;

import com.zevyirmiyahu.Main;
import com.zevyirmiyahu.lights.Sync;

public class Output extends Canvas implements Runnable, WindowListener {

	private boolean running = false;

	private int width = 480;
	private int height = 600;
	
	private String title = "Traffic Light Simulation";
	
	private JFrame frame;
	private Thread thread;
	private Sync sync;
	
	public Output(int redTime, int yellowTime, int greenTime) {
		
		Dimension size = new Dimension(width, height);
		frame = new JFrame();

		int numberOfTrafficLights = 2; // may expand model to more lights in future,
		sync = new Sync(redTime, yellowTime, greenTime, numberOfTrafficLights);

		setPreferredSize(size);
		setFocusable(true);
		
		initUI();
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Dispaly");
		thread.start();
		sync.start();
	}
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1_000_000_000.0 / 60.0;
		double delta = 0;
		int updates = 0;
		
		// Application Loop
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + " | " + updates + "UPS");
				updates = 0;
			}
		}
		stop();
	}
	
	public void update() {
		sync.update();
	}
	
	public void render() {
		
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		// BACKGROUND
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		// RENDER ALL LIGHTS WITH SYNC METHOD ARRAYLIST
		sync.render(g);
		
		g.dispose();
		bs.show();
	}
	
	private void initUI() {
		frame.addWindowListener(this);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		Main.simulationRunning = true;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		sync.remove(); // clear for reset simulation
		Main.simulationRunning = false; // set flag
	}
	

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		running = false; // must stop thread
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

