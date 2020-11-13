package cpre_558_Project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

public class GUIComponent extends javax.swing.JFrame {
	
	private MyopicSchedulingAlgorithm scheduler;
	private List<Task> taskList;

	public GUIComponent() {
		initComponents();
		this.scheduler = new MyopicSchedulingAlgorithm();
		this.taskList = new ArrayList<>();
		this.pack();
	}
	
	/* Public Methods */
	
	public GUIComponent getComponent() {
		return this;
	}
	
	public List<Task> getTaskList() {
		return this.taskList;
	}
	
	public GUIComponent setTaskList(List<Task> list) {
		this.taskList = list;
		
		return this;
	}
	
	public Map<String, Integer> getInputs() {
		Map<String, Integer> inputs = new HashMap<>();
		
		inputs.put("processorCount", Integer.getInteger(processorCountInput.getText()));
		inputs.put("feasabilityCheckWindowSize", Integer.getInteger(feasabilityCheckWindowSizeInput.getText()));
		inputs.put("maxNumberBacktracks", Integer.getInteger(maxNumberBacktracksInput.getText()));
		inputs.put("weight", Integer.getInteger(weightInput.getText()));
		
		return inputs;
	}
	
	public GUIComponent setInputs(
			Integer processorCount, Integer feasabilityCheckWindow,
			Integer maxNumberBacktracks, Integer weight) {
		
		this.processorCountInput.setText(processorCount.toString());
		this.feasabilityCheckWindowSizeInput.setText(feasabilityCheckWindow.toString());
		this.maxNumberBacktracksInput.setText(maxNumberBacktracks.toString());
		this.weightInput.setText(weight.toString());
		
		return this;
	}
	
	/* Private Methods */
	
	// Algorithm Label
	private JLabel algorithmLabel;
	
	private void initComponents() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Project: char(9)");
		
		algorithmLabel = new JLabel("Myopic Scheduling Algorithm");
		
		this.initInputs();
		this.initButtons();
		
		this.setLayout();
		
		this.setLocationByPlatform(true);
		this.setLocationRelativeTo(null);
	}
	
	// Input variables
	private JLabel processorCountLabel;
	private JTextField processorCountInput;
	private JLabel feasabilityCheckWindowSizeLabel;
	private JTextField feasabilityCheckWindowSizeInput;
	private JLabel maxNumberBacktracksLabel;
	private JTextField maxNumberBacktracksInput;
	private JLabel weightLabel;
	private JTextField weightInput;
	
	private void initInputs() {
		processorCountLabel = new JLabel("Number of Processors: ");
		processorCountInput = new JTextField(3);
		
		feasabilityCheckWindowSizeLabel = new JLabel("Feasability Window Size: ");
		feasabilityCheckWindowSizeInput = new JTextField(3);
		
		maxNumberBacktracksLabel = new JLabel("Max Number of Backtracks: ");
		maxNumberBacktracksInput = new JTextField(3);
		
		weightLabel = new JLabel("Weight: ");
		weightInput = new JTextField(3);
	}
	
	// button variables
	private JButton runButton;
	private JButton tasksPanelButton;
	private JButton logPanelButton;
	
	private void initButtons() {
		
		// Start Run Button
		runButton = new JButton();
		runButton.setText("Run");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// get Inputs
				int processorCount = Integer.parseInt(processorCountInput.getText());
				int feasabilityCheckWindowSize = Integer.parseInt(feasabilityCheckWindowSizeInput.getText());
				int maxNumberBacktracks = Integer.parseInt(maxNumberBacktracksInput.getText());
				int weight = Integer.parseInt(weightInput.getText());
				
				new Thread(new Runnable() {
					public void run() {
						
						scheduler.setInputs(processorCount, feasabilityCheckWindowSize, maxNumberBacktracks, weight);
						scheduler.initialize();
						scheduler.calculateSchedule(getTaskList());
						
						// Runs inside of the Swing UI thread
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								// Show the Schedule in a new JFrame
								GUIComponent component = getComponent();
								JFrame frame = new ScheduleFrame(scheduler.getSchedule());
								frame.setLocation(component.getX() + component.getWidth()/2 - frame.getWidth()/2, component.getY() + component.getHeight());
								frame.setVisible(true);
							}
						});
					}
				}).start();
			}
		});
		// End Run Button
		
		
		tasksPanelButton = new JButton();
		tasksPanelButton.setText("Tasks");
		tasksPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Open the Tasks Panel
				GUIComponent component = getComponent();
				List<Task> taskList = getTaskList();
				JFrame frame = new TaskFrame(component, taskList);
				frame.setLocation(component.getX() - frame.getWidth(), component.getY());
				frame.setVisible(true);
			}
		});
		
		// TODO Move the Logs Button to the ScheduleFrame
		/*
		logPanelButton = new JButton();
		logPanelButton.setText("Logs");
		logPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Show the Logs Panel
			}
		});
		*/
	}
	
	private void setLayout() {
		JPanel panel = new JPanel();
		SpringLayout layout = new SpringLayout();
											// L, H
		panel.setPreferredSize(new Dimension(270, 200));
		panel.setLayout(layout);
		
		// Algorithm Label
		panel.add(algorithmLabel);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, algorithmLabel, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, algorithmLabel, 10, SpringLayout.NORTH, panel);
		
		JSeparator line = new JSeparator(JSeparator.HORIZONTAL);
		line.setPreferredSize(new Dimension(250, 3));
		panel.add(line);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, line, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, line, 25, SpringLayout.NORTH, algorithmLabel);
		
		// Inputs
		panel.add(processorCountLabel);
		panel.add(processorCountInput);
		layout.putConstraint(SpringLayout.EAST, processorCountLabel, -5, SpringLayout.WEST, processorCountInput);
		layout.putConstraint(SpringLayout.NORTH, processorCountLabel, 0, SpringLayout.NORTH, processorCountInput);
		layout.putConstraint(SpringLayout.WEST, processorCountInput, 180, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, processorCountInput, 10, SpringLayout.NORTH, line);
		
		panel.add(feasabilityCheckWindowSizeLabel);
		panel.add(feasabilityCheckWindowSizeInput);
		layout.putConstraint(SpringLayout.EAST, feasabilityCheckWindowSizeLabel, -5, SpringLayout.WEST, feasabilityCheckWindowSizeInput);
		layout.putConstraint(SpringLayout.NORTH, feasabilityCheckWindowSizeLabel, 0, SpringLayout.NORTH, feasabilityCheckWindowSizeInput);
		layout.putConstraint(SpringLayout.WEST, feasabilityCheckWindowSizeInput, 0, SpringLayout.WEST, processorCountInput);
		layout.putConstraint(SpringLayout.NORTH, feasabilityCheckWindowSizeInput, 25, SpringLayout.NORTH, processorCountInput);
	
		panel.add(maxNumberBacktracksLabel);
		panel.add(maxNumberBacktracksInput);
		layout.putConstraint(SpringLayout.EAST, maxNumberBacktracksLabel, -5, SpringLayout.WEST, maxNumberBacktracksInput);
		layout.putConstraint(SpringLayout.NORTH, maxNumberBacktracksLabel, 0, SpringLayout.NORTH, maxNumberBacktracksInput);
		layout.putConstraint(SpringLayout.WEST, maxNumberBacktracksInput, 0, SpringLayout.WEST, feasabilityCheckWindowSizeInput);
		layout.putConstraint(SpringLayout.NORTH, maxNumberBacktracksInput, 25, SpringLayout.NORTH, feasabilityCheckWindowSizeInput);
	
		panel.add(weightLabel);
		panel.add(weightInput);
		layout.putConstraint(SpringLayout.EAST, weightLabel, -5, SpringLayout.WEST, weightInput);
		layout.putConstraint(SpringLayout.NORTH, weightLabel, 0, SpringLayout.NORTH, weightInput);
		layout.putConstraint(SpringLayout.WEST, weightInput, 0, SpringLayout.WEST, maxNumberBacktracksInput);
		layout.putConstraint(SpringLayout.NORTH, weightInput, 25, SpringLayout.NORTH, maxNumberBacktracksInput);
		
		// Buttons now
		panel.add(tasksPanelButton);
		layout.putConstraint(SpringLayout.WEST, tasksPanelButton, 25, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.SOUTH, tasksPanelButton, -10, SpringLayout.SOUTH, panel);
		
		panel.add(runButton);
		layout.putConstraint(SpringLayout.EAST, runButton, -25, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.SOUTH, runButton, -10, SpringLayout.SOUTH, panel);
	
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

}
