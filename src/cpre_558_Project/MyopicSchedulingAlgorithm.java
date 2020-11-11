package cpre_558_Project;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MyopicSchedulingAlgorithm {
	
	// Algorithm inputs
	private int numProcessors;
	private int feasabilityCheckWindowSize;
	private int maxNumberBacktracks; // inclusive
	private int weight;
	
	// Algorithm variables
	private int numberBacktracks;
	private boolean backtrackFlag;
	private int priorBestHeuristicValue;
	private Task priorBestTask;
	private List<Task> feasibilityWindow;
	
	// Processor variables
	private Map<Integer, Integer> processorAvailableTimeMap;
	private Map<Integer, List<ScheduledTask>> processorScheduleMap;
	
	// Resource variables
	private Map<String, Integer> resourceAvailableTimeMap_Exclusive;
	private Map<String, Integer> resourceAvailableTimeMap_Shared;
	
	// Memory Cell
	private class MemoryCell {
		public Map<Integer, Integer> processorAvailableTimeMap;
		public Map<Integer, List<ScheduledTask>> processorScheduleMap;
		public Map<String, Integer> resourceAvailableTimeMap_Exclusive;
		public Map<String, Integer> resourceAvailableTimeMap_Shared;
		
		public boolean backtrackFlag;
		public int priorBestHeuristicValue;
		public Task priorBestTask;
		public List<Task> feasibilityWindow;
	}
	
	// MemoryCell Stack
	private Stack<MemoryCell> memoryStack;
	
	
	public MyopicSchedulingAlgorithm() {
		// basic constructor
		processorAvailableTimeMap = new HashMap<>();
		processorScheduleMap = new HashMap<>();
		resourceAvailableTimeMap_Exclusive = new HashMap<>();
		resourceAvailableTimeMap_Shared = new HashMap<>();
		memoryStack = new Stack<>();
		feasibilityWindow = new ArrayList<>();
	}
	
	public MyopicSchedulingAlgorithm(
			int processorCount, int feasabilityCheckWindow,
			int maxNumberBacktracks, int weight) {
		this();
		
		this.numProcessors = processorCount;
		this.feasabilityCheckWindowSize = feasabilityCheckWindow;
		this.maxNumberBacktracks = maxNumberBacktracks;
		this.weight = weight;
	}
	
	public void setInputs(int processorCount, int feasabilityCheckWindow,
			int maxNumberBacktracks, int weight) {
		this.numProcessors = processorCount;
		this.feasabilityCheckWindowSize = feasabilityCheckWindow;
		this.maxNumberBacktracks = maxNumberBacktracks;
		this.weight = weight;
	}
	
	public void initialize() {
		this.numberBacktracks = 0;
		this.backtrackFlag = false;
		
		this.processorAvailableTimeMap.clear();
		this.processorScheduleMap.clear();
		this.resourceAvailableTimeMap_Exclusive.clear();
		this.resourceAvailableTimeMap_Shared.clear();
		this.memoryStack.clear();
		this.feasibilityWindow.clear();
		
		for(int i = 1; i <= numProcessors; i++) {
			this.processorAvailableTimeMap.put(i, 0);
			this.processorScheduleMap.put(i, new ArrayList<ScheduledTask>());
		}
		
		this.priorBestHeuristicValue = -1;
		this.priorBestTask = null;
		
	}
	
	public Map<Integer, List<ScheduledTask>> getSchedule() {
		return this.processorScheduleMap;
	}
	
	public boolean calculateSchedule(List<Task> taskList) {
		
		// logging variables
		String logOutput;
		boolean first;
		
		// Log Algorithm inputs 
		log("Number Of Processors: " + this.numProcessors);
		log("Feasability Check Window Size: " + this.feasabilityCheckWindowSize);
		log("Max Number of Backtracks: " + this.maxNumberBacktracks);
		log("Weight: " + this.weight);
		
		int scheduleSize = 0;
		
		/* 1. Sort the Tasks by deadline */
		this.sortByDeadline(taskList);
		
		// Log Sorted List
		log("\nTasks Sorted by Deadline: ");
		logOutput = "\t";
		first = true;
		for(Task t : taskList) {
			if (!first) {
				logOutput += ", ";
			} else {
				first = false;
			}
			
			logOutput += t.Id + "(" + t.d + ")";
		}
		log(logOutput);
		
		this.feasibilityWindow = deepClone(taskList.subList(0, feasabilityCheckWindowSize), new Task());
		
		do {
			
			log("\n----------------------");
			// log feasibilityWindow
			logOutput = "Feasibility Check Window: ";
			first = true;
			for(Task t : feasibilityWindow) {
				if (!first) {
					logOutput += ", ";
				} else {
					first = false;
				}
				
				logOutput += t.Id;
			}
			log(logOutput);
			
			/* 2. Check for strong feasibility for K tasks which are in the feasability check window */
			boolean isStronglyFeasible = checkStrongFeasibility(feasibilityWindow);
			
			/* 
			 * 3. if the current vertex is strongly feasible
			 * - Compute Heuristic value (Hi)
			 * - Choose best (smallest) heuristic value (Hx)
			 * - Extend the schedule with task (Tx)
			 */
			if(isStronglyFeasible) {
				// Compute Heuristic value (Hi)
				int Hx = Integer.MAX_VALUE;
				Task Tx = null;
				
				log("\nHeuristic values: ");
				
				for (int i = 0; i < feasibilityWindow.size(); i++) {
					Task Ti = feasibilityWindow.get(i);
					int Hi = Ti.d + weight * earliestStartTime(Ti);
					
					log("\t" + Ti.Id + ": " + Hi);
					
					if (this.backtrackFlag) {
						// (BackTracks) If it is less than the prior value we throw it out of the options, 
						//  if it is equal then we throw it out if it was the last option we used.
						if (Hi < priorBestHeuristicValue || Ti.Id == priorBestTask.Id) {
							continue;
						}
					}
					
					
					// Choose the smallest value
					if (Hi < Hx) {
						Hx = Hi;
						Tx = Ti;
					}
				}
				
				if (Tx == null) {
					// we need to backtrack more without increasing the backtrack count
					this.backtrackFlag = true;
					this.numberBacktracks -= 1;
				} else {
					// Extend the schedule with task (Tx)
					this.extendSchedule(Tx, Hx, taskList);
				}
				
			} else {
				// !isStronglyFeasible
				this.backtrackFlag = true;
			}
			
			/* 4. else backtrack to previous vertex and extend the schedule with the next best task */
			if (this.backtrackFlag) {
				
				this.numberBacktracks++;
				this.backtrackFlag = true;
				
				log("Backtrack (count = " + numberBacktracks + ")");
				
				// Maximum backtracks encountered
				if (numberBacktracks > maxNumberBacktracks) {
					log("\n----------------------");
					log("Backtrack count: " + this.numberBacktracks);
					log("Maximum number of backtracks encountered");
					
					return false;
				}
				
				// No more backtracking is possible
				if (this.memoryStack.size() == 0) {
					log("\n----------------------");
					log("Backtrack count: " + this.numberBacktracks);
					log("No more backtracking is possible");
					
					return false;
				}
				
				// Previous vertex
				this.RevertSchedule();
				
			}
			
			/* 
			 * Repeat steps 2 - 4 until either:
			 * - Feasible Schedule is obtained
			 * - Maximum backtracks encountered
			 * - No more backtracking is possible
			 */
			scheduleSize = 0;
			for(Integer p : processorScheduleMap.keySet()) {
				scheduleSize += processorScheduleMap.get(p).size();
			}
			
		} while (scheduleSize < taskList.size());
		
		// log results
		log("\n----------------------");
		log("Backtrack count: " + this.numberBacktracks);
		log("Schedule(s) Obtained: ");
		logOutput = "";
		first = true;
		for(Integer p : processorScheduleMap.keySet()) {
			if (!first) {
				logOutput += "\n";
			} else {
				first = false;
			}
			
			logOutput += "\t" + p + ": " + processorScheduleMap.get(p);
		}
		log(logOutput);
		
		return true;
	}
	
	/* Sorts the Tasks by deadline */
	private void sortByDeadline(List<Task> taskList) {
		// simple bubble sort
		int n = taskList.size();
		for(int i = 0; i < n-1; i++) {
			for (int j = 0; j < n-i-1; j++) {
				if (taskList.get(j).d > taskList.get(j+1).d) {
					// swap j and j+1
					Collections.swap(taskList, j, j+1);
				}
				
			}
		}
	}
	
	/* Checks for strong feasibility for K tasks which are in the feasability check window */
	private boolean checkStrongFeasibility(List<Task> feasabilityCheckWindow) {
		boolean result = true;
		
		log("\nStrong Feasibility Tests: ");
		
		for (int i = 0; i < feasabilityCheckWindow.size(); i++) {
			Task t = feasabilityCheckWindow.get(i);
			int EST = earliestStartTime(t);
			
			log("\t" + t.Id + ": " + EST + " + " + t.c + " <= " + t.d);
			
			if (EST + t.c > t.d) {
				result = false;
			}
		}
		
		log("Strong Feasibility: " + result);
		
		return result;
	}
	
	private int earliestStartTime(Task t) {
		int resourceAvailTime = earliestAvailableTime_Resource(t);
		int processorAvailTime = earliestAvailableTime_Processor().getValue();
		
		int earliestStartTime = Integer.max(t.r, processorAvailTime);
		earliestStartTime = Integer.max(earliestStartTime, resourceAvailTime);
		
		return earliestStartTime;
	}
	
	private SimpleEntry<Integer, Integer> earliestAvailableTime_Processor() {
		int result = Integer.MAX_VALUE;
		int processorNum = 0;
		
		for(Integer processor : processorAvailableTimeMap.keySet()) {
			int p_AvailTime = processorAvailableTimeMap.getOrDefault(processor, 0);
			if (p_AvailTime < result) {
				result = p_AvailTime;
				processorNum = processor.intValue();
			}
			
		}
		
		return new SimpleEntry<>(processorNum, result);
	}
	
	private int earliestAvailableTime_Resource(Task t) {
		// check for Exclusive Access
		if (t.r_E == true) {
			int r_AvailTime = resourceAvailableTimeMap_Exclusive.getOrDefault(t.r_Id, 0);
			return r_AvailTime;
		}
		
		// check for Shared Access
		if (t.r_S == true) {
			int r_AvailTime = resourceAvailableTimeMap_Shared.getOrDefault(t.r_Id, 0);
			return r_AvailTime;
		}
		
		// No Access
		return 0;
	}
	
	private void extendSchedule(Task t, int h, List<Task> taskList) {
		
		// variables we need
		int processor = earliestAvailableTime_Processor().getKey();
		int startTime = earliestStartTime(t);
		int finishTime = startTime + t.c;
		
		// create MemoryCell
		MemoryCell preStep = new MemoryCell();
		preStep.processorAvailableTimeMap = deepClone(processorAvailableTimeMap, 0);
		preStep.processorScheduleMap = deepClone(processorScheduleMap, new ArrayList<>());
		preStep.resourceAvailableTimeMap_Exclusive = deepClone(resourceAvailableTimeMap_Exclusive, "");
		preStep.resourceAvailableTimeMap_Shared = deepClone(resourceAvailableTimeMap_Shared, "");
		preStep.priorBestHeuristicValue = h;
		preStep.priorBestTask = deepClone(t);
		preStep.feasibilityWindow = deepClone(feasibilityWindow, new Task());
		
		// Push on the Stack
		this.memoryStack.push(preStep);
		
		// Update processor variables
		this.processorAvailableTimeMap.put(processor, finishTime);
		this.processorScheduleMap.get(processor).add(new ScheduledTask(t, startTime, finishTime));
		
		// Update resource variables
		if (t.r_E == true) {
			this.resourceAvailableTimeMap_Exclusive.put(t.r_Id, finishTime);
			this.resourceAvailableTimeMap_Shared.put(t.r_Id, finishTime);
		} else if (t.r_S == true) {
			this.resourceAvailableTimeMap_Exclusive.put(t.r_Id, finishTime);
			// no need to update shared
		}
		
		// update backtrack flag
		this.backtrackFlag = false;
		
		// update feasibility check Window
		this.feasibilityWindow.remove(t);
		int num_scheduled = 0;
		for(Integer p : processorScheduleMap.keySet()) {
			num_scheduled += processorScheduleMap.get(p).size();
		}
		if (num_scheduled + this.feasibilityWindow.size() < taskList.size()) {
			Task n = taskList.get(num_scheduled + this.feasabilityCheckWindowSize - 1);
			this.feasibilityWindow.add(n);
		}
		
		// log the new schedule
		log("\nSchedule(s): ");
		String logOutput = "";
		boolean first = true;
		for(Integer p : processorScheduleMap.keySet()) {
			if (!first) {
				logOutput += "\n";
			} else {
				first = false;
			}
			
			logOutput += "\t" + p + ": " + processorScheduleMap.get(p);
		}
		log(logOutput);
	}
	
	private void RevertSchedule() {
		MemoryCell preStep = this.memoryStack.pop();
		
		// reset variables
		this.processorAvailableTimeMap = deepClone(preStep.processorAvailableTimeMap, 0);
		this.processorScheduleMap = deepClone(preStep.processorScheduleMap, new ArrayList<>());
		this.resourceAvailableTimeMap_Exclusive = deepClone(preStep.resourceAvailableTimeMap_Exclusive, "");
		this.resourceAvailableTimeMap_Shared = deepClone(preStep.resourceAvailableTimeMap_Shared, "");
		this.priorBestHeuristicValue = preStep.priorBestHeuristicValue;
		this.priorBestTask = deepClone(preStep.priorBestTask);
		this.feasibilityWindow = deepClone(preStep.feasibilityWindow, new Task());
	}
	
	
	private void log(String s) {
		System.out.println(s);
	}
	
	/* 
	 * Deep Clones
	 */
	private Map<Integer, Integer> deepClone(Map<Integer, Integer> map, Integer garbage) {
		Map<Integer, Integer> cloned = new HashMap<>();
		
		for(Integer key : map.keySet()) {
			Integer i = map.get(key).intValue();
			cloned.put(key, i);
		}
		
		return cloned;
	}
	
	private Map<Integer, List<ScheduledTask>> deepClone(Map<Integer, List<ScheduledTask>> map, List<ScheduledTask> garbage) {
		Map<Integer, List<ScheduledTask>> cloned = new HashMap<>();
		
		for(Integer key : map.keySet()) {
			List<ScheduledTask> list = deepClone(map.get(key), new ScheduledTask());
			cloned.put(key, list);
		}
		
		return cloned;
	}
	
	private Map<String, Integer> deepClone(Map<String, Integer> map, String garbage) {
		Map<String, Integer> cloned = new HashMap<>();
		
		for(String key : map.keySet()) {
			Integer i = map.get(key).intValue();
			cloned.put(key, i);
		}
		
		return cloned;
	}
	
	private List<ScheduledTask> deepClone(List<ScheduledTask> list, ScheduledTask garbage) {
		List<ScheduledTask> cloned = new ArrayList<>();
		
		for(ScheduledTask value : list) {
			ScheduledTask st = deepClone(value);
			cloned.add(st);
		}
		
		return cloned;
	}
	
	private List<Task> deepClone(List<Task> list, Task garbage) {
		List<Task> cloned = new ArrayList<>();
		
		for(Task value : list) {
			Task t = deepClone(value);
			cloned.add(t);
		}
		
		return cloned;
	}
	
	private ScheduledTask deepClone(ScheduledTask st) {
		Task t = deepClone(st.t);
		return new ScheduledTask(t, st.startTime, st.endTime);
	}
	
	private Task deepClone(Task t) {
		return new Task(t.Id, t.c, t.d, t.r, t.r_Id, t.r_E, t.r_S);
	}
	
	
}
