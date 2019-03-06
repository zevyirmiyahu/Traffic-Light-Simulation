package com.zevyirmiyahu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.zevyirmiyahu.output.Output;

/*
 * PROJECT DESCRIPTION: This is a mini-project that allows a user to input 3 integer durations 
 * for light changing. The light durations are for traffic light #1 and specify how long a given 
 * light will remain on. Traffic light number 2 is then created and rendered to the screen and 
 * synchronizes in a way to ensure no two lights are ever green AND yellow at any one time. The 
 * purpose of this project was to build a mathematical and computer model that can easily simulate 
 * various intersections with differing time restrictions.
 * 
 * The main class builds the main menu that takes user input, then creates an instance of Sync which 
 * creates 2 instances of TrafficLight and syncs their light mechanisms based on the user input.
 * 
 * 
 * @author Zev Yirmiyahu
 * 
 * E-Mail: zy@zevyirmiyahu.com
 * 
 * GitHub: https://github.com/zevyirmiyahu 
 * 
 * Personal Website: www.zevyirmiyahu.com
 *  
 */

public class Main implements ActionListener, FocusListener {
	
	private String title = "Traffic Light Simulation | Input Menu";
	private String directions = "Directions: Input desired light durations";
	private String note = "     *NOTE: Green Time - 2 >= Red Time";
	private String invalid = "*Invalid input detected* Must be integer values only";
	private String invalid2 = "Must close current simulation before generating new";
	
	private boolean invalidInput = false;
	public static boolean simulationRunning = false;
	
	private int width = 400;
	private int height = 300;
	
	private int redTime;
	private int yellowTime;
	private int greenTime;
	
	private static ArrayList<Output> currentSimulation = new ArrayList<Output>(); // prevents simultaneous simulation creation
		
	private JFrame frame;
	private JButton button; // generate simulation
	private JLabel directionLabel;
	private JLabel noteLabel;
	private JLabel invalidLabel;
	private JLabel invalidLabel2;

	private JTextField jtext1, jtext2, jtext3;
	
	public Main() {
		initUI();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	private void initUI() {
		Color lightGray = new Color(0x9e9e9e);
		
		// labels
		directionLabel = new JLabel(directions);
		noteLabel = new JLabel(note);
		invalidLabel = new JLabel(invalid);
		invalidLabel.setVisible(false); // shows only when invalid input
		invalidLabel2 = new JLabel(invalid2);
		invalidLabel2.setVisible(false);
		
		// textfields
		jtext1 = new JTextField("Red Time", 7);
		jtext1.addFocusListener(this);
		
		jtext2 = new JTextField("Yellow Time", 7);
		jtext2.addFocusListener(this);
		
		jtext3 = new JTextField("Green Time", 7);
		jtext3.addFocusListener(this);

		// buttons
		button = new JButton("Generate");
		button.setBackground(lightGray);
		button.addActionListener(this);

		// frame
		Dimension size = new Dimension(width, height);
		frame = new JFrame(title);
		frame.setPreferredSize(size);
		frame.pack();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.setVisible(true);
		
		// add components
		frame.add(directionLabel);
		frame.add(jtext1);
		frame.add(jtext2);
		frame.add(jtext3);
		frame.add(button);
		frame.add(noteLabel);
		frame.add(invalidLabel);
		frame.add(invalidLabel2);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(e.getSource().equals(jtext1) && jtext1.getText().equals("Red Time")) jtext1.setText(null);
		if(e.getSource().equals(jtext2) && jtext2.getText().equals("Yellow Time")) jtext2.setText(null);
		if(e.getSource().equals(jtext3) && jtext3.getText().equals("Green Time")) jtext3.setText(null);
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource().equals(jtext1) && jtext1.getText().equals("")) jtext1.setText("Red Time");
		if(e.getSource().equals(jtext2) && jtext2.getText().equals("")) jtext2.setText("Yellow Time");
		if(e.getSource().equals(jtext3) && jtext3.getText().equals("")) jtext3.setText("Green Time");
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		invalidInput = false; // reset
		if(ae.getSource().equals(button)) {
			try {
				redTime = Integer.parseInt(jtext1.getText());
				yellowTime = Integer.parseInt(jtext2.getText());
				greenTime = Integer.parseInt(jtext3.getText());
			} catch(Exception e) {
				invalidInput = true;
				invalidLabel.setVisible(true);
			}
			if(greenTime - 2 < redTime) {
				invalidInput = true;
				invalidLabel.setVisible(true);
			}
		}

		if(!invalidInput) {
			if(simulationRunning) {
				invalidLabel2.setVisible(true);
			}
			else {
				invalidLabel.setVisible(false);
				invalidLabel2.setVisible(false);
				currentSimulation.clear();
				Output output = new Output(redTime, yellowTime, greenTime);
				currentSimulation.add(output);
				currentSimulation.get(0).start();
			}
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {						
						new Main();
					}
				});
	}
}
	