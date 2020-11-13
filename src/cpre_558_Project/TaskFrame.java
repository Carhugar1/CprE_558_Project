package cpre_558_Project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TaskFrame extends javax.swing.JFrame {
	
	private JPanel panel;
	private GUIComponent callingComponent;
	
	public TaskFrame() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Task Set");
	}
	
	public TaskFrame(List<Task> taskList) {
		this();
		
		this.init();
		this.buildComponents(taskList);
		
		this.pack();
	}
	
	public TaskFrame(GUIComponent callingFrame, List<Task> taskList) {
		this();
		
		this.callingComponent = callingFrame;
		
		this.init();
		this.buildComponents(taskList);
		
		this.pack();
	}
	
	/* Public Methods */
	
	public void addNewRow() {
		addRow(new Task());
		this.pack();
		this.revalidate();
		this.repaint();
	}
	
	public GUIComponent getCallingComponent() {
		return this.callingComponent;
	}
	
	public List<Task> getTaskList() {
		List<Task> taskList = new ArrayList<Task>();
		List<String> inputList = new ArrayList<String>();
		
		// we need to skip the first 2 Header Panels
		for(int i = 2; i < panel.getComponentCount(); i++) {
			Component c1 = panel.getComponent(i);
			
			if (c1 instanceof JPanel) {
				JPanel subPanel = (JPanel) c1;
				
				// Build a list of Strings from the Inputs
				inputList.clear();
				
				for(int j = 0; j < subPanel.getComponentCount(); j++) {
					Component c2 = subPanel.getComponent(j);
					
					if (c2 instanceof JTextField) {
						JTextField input = (JTextField) c2;
						inputList.add(input.getText());
					}
				}
				
				// Using the List Build the Task
				Task t;
				if (inputList.get(4).isEmpty()) {
					// Basic Task
					t = new Task(
							inputList.get(0), 					// String Id
							Integer.parseInt(inputList.get(1)), // int c
							Integer.parseInt(inputList.get(2)), // int d
							Integer.parseInt(inputList.get(3))	// int r
						);
				} else {
					// Task w/ Resource Constraints
					t = new Task(
							inputList.get(0), 					// String Id
							Integer.parseInt(inputList.get(1)), // int c
							Integer.parseInt(inputList.get(2)), // int d
							Integer.parseInt(inputList.get(3)), // int r
							inputList.get(4),							// Resource Id
							inputList.get(5).isEmpty() ? false : true,	// Exclusive
							inputList.get(6).isEmpty() ? false : true	// Shared
						);
				}
				
				taskList.add(t);
			}
		}
		
		return taskList;
	}
	
	public void clearTasks() {
		// we need to skip the first 2 Header Panels
		for(int i = 2; i < panel.getComponentCount();) {
			panel.remove(i);
		}
		
		this.addNewRow();
	}
	
	/* Private Methods */
	
	private JButton addButton;
	private JButton saveButton;
	private JButton clearButton;
	
	private void init() {
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(0,1));
		this.getContentPane().add(this.panel);
		
		JPanel panel;
		SpringLayout layout;
		
		// Header Button Panel
		panel = new JPanel();
		layout = new SpringLayout();
		panel.setLayout(layout);
		
		// Set Up Buttons
		addButton = new JButton();
		addButton.setText("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Add a new Task Row
				addNewRow();
			}
		});
		
		saveButton = new JButton();
		saveButton.setText("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Save the TaskList
				List<Task> taskList = getTaskList();
				getCallingComponent().setTaskList(taskList);
			}
		});
		
		clearButton = new JButton();
		clearButton.setText("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Clear the Tasks
				clearTasks();
			}
		});
		
		// Add the Buttons
		panel.add(clearButton);
		layout.putConstraint(SpringLayout.EAST, clearButton, -15, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.NORTH, clearButton, 0, SpringLayout.NORTH, panel);
		
		panel.add(saveButton);
		layout.putConstraint(SpringLayout.EAST, saveButton, -5, SpringLayout.WEST, clearButton);
		layout.putConstraint(SpringLayout.NORTH, saveButton, 0, SpringLayout.NORTH, panel);
		
		panel.add(addButton);
		layout.putConstraint(SpringLayout.EAST, addButton, -5, SpringLayout.WEST, saveButton);
		layout.putConstraint(SpringLayout.NORTH, addButton, 0, SpringLayout.NORTH, panel);
		
		// Add the Panel to the Main Panel
										// L, H
		panel.setPreferredSize(new Dimension(475, 26));
		this.panel.add(panel);
		
		
		
		// Header Label Panel
		panel = new JPanel();
		layout = new SpringLayout();
		panel.setLayout(layout);
		
		// Set Up Labels
		JLabel temp;
		JLabel taskIdLabel = new JLabel("TaskId");
		JLabel taskCLabel = new JLabel("c");
		JLabel taskDLabel = new JLabel("d");
		JLabel taskRLabel = new JLabel("r");
		JLabel taskR_IdLabel = new JLabel("ResourceId");
		JLabel taskR_ELabel = new JLabel("Exclusive");
		JLabel taskR_SLabel = new JLabel("Shared");
		
		// Add the Labels
		panel.add(taskIdLabel);
		layout.putConstraint(SpringLayout.WEST, taskIdLabel, 15, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, taskIdLabel, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel("(");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskIdLabel);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskCLabel);
		layout.putConstraint(SpringLayout.WEST, taskCLabel, 20, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskCLabel, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(",");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 20, SpringLayout.EAST, taskCLabel);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskDLabel);
		layout.putConstraint(SpringLayout.WEST, taskDLabel, 20, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskDLabel, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(",");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 20, SpringLayout.EAST, taskDLabel);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskRLabel);
		layout.putConstraint(SpringLayout.WEST, taskRLabel, 20, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskRLabel, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(")");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 20, SpringLayout.EAST, taskRLabel);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskR_IdLabel);
		layout.putConstraint(SpringLayout.WEST, taskR_IdLabel, 35, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskR_IdLabel, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel("(");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskR_IdLabel);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskR_ELabel);
		layout.putConstraint(SpringLayout.WEST, taskR_ELabel, 1, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskR_ELabel, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(",");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 1, SpringLayout.EAST, taskR_ELabel);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskR_SLabel);
		layout.putConstraint(SpringLayout.WEST, taskR_SLabel, 1, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskR_SLabel, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(")");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 1, SpringLayout.EAST, taskR_SLabel);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		// Add a Line Break
		JSeparator line = new JSeparator(JSeparator.HORIZONTAL);
		line.setPreferredSize(new Dimension(450, 1));
		panel.add(line);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, line, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, line, 2, SpringLayout.SOUTH, taskIdLabel);
		
		// Add the Panel to the Main Panel
										// L, H
		panel.setPreferredSize(new Dimension(450, 10));
		this.panel.add(panel);
	}
	
	private void buildComponents(List<Task> taskList) {
		for(Task t : taskList) {
			addRow(t);
		}
	}
	
	private void addRow(Task t) {
		// Row Panel
		JPanel panel = new JPanel();
		SpringLayout layout = new SpringLayout();
		panel.setLayout(layout);
		
		// Set Up Inputs
		JLabel temp;
		JTextField taskIdInput = new JTextField(t.Id, 3);
		JTextField taskCInput = new JTextField("" + t.c, 3);
		JTextField taskDInput = new JTextField("" + t.d, 3);
		JTextField taskRInput = new JTextField("" + t.r, 3);
		JTextField taskR_IdInput = new JTextField(t.r_Id, 3);
		JTextField taskR_EInput = new JTextField(t.r_E ? "x" : "", 1);
		JTextField taskR_SInput = new JTextField(t.r_S ? "x" : "", 1);
		
		// Add the Inputs
		panel.add(taskIdInput);
		layout.putConstraint(SpringLayout.WEST, taskIdInput, 15, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, taskIdInput, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel("(");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskIdInput);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskCInput);
		layout.putConstraint(SpringLayout.WEST, taskCInput, 5, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskCInput, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(",");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskCInput);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskDInput);
		layout.putConstraint(SpringLayout.WEST, taskDInput, 5, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskDInput, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(",");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskDInput);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskRInput);
		layout.putConstraint(SpringLayout.WEST, taskRInput, 5, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskRInput, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(")");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskRInput);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskR_IdInput);
		layout.putConstraint(SpringLayout.WEST, taskR_IdInput, 62, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskR_IdInput, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel("(");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskR_IdInput);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskR_EInput);
		layout.putConstraint(SpringLayout.WEST, taskR_EInput, 5, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskR_EInput, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(",");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskR_EInput);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		panel.add(taskR_SInput);
		layout.putConstraint(SpringLayout.WEST, taskR_SInput, 5, SpringLayout.EAST, temp);
		layout.putConstraint(SpringLayout.NORTH, taskR_SInput, 0, SpringLayout.NORTH, panel);
		
		temp = new JLabel(")");
		panel.add(temp);
		layout.putConstraint(SpringLayout.WEST, temp, 5, SpringLayout.EAST, taskR_SInput);
		layout.putConstraint(SpringLayout.NORTH, temp, 0, SpringLayout.NORTH, panel);
		
		// Add the Panel to the Main Panel
										// L, H
		panel.setPreferredSize(new Dimension(450, 10));
		this.panel.add(panel);
	}
}