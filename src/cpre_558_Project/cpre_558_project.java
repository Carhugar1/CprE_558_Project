package cpre_558_Project;

import java.util.ArrayList;
import java.util.List;

public class cpre_558_project {
	
	public static void main(String[] args) {
		List<Task> taskList = new ArrayList<>();
		
		//			 new Task(Id, c, d, r, Resource, Exclusive, Shared));
		
		taskList.add(new Task("T1", 6, 10, 0));
		taskList.add(new Task("T2", 7, 13, 2, "R", false, true));
		taskList.add(new Task("T3", 3, 16, 1, "R", true, false));
		taskList.add(new Task("T4", 7, 17, 2));
		taskList.add(new Task("T5", 9, 19, 3));
		
		/*
		taskList.add(new Task("T1", 1, 2, 0));
		taskList.add(new Task("T2", 1, 2, 0));
		taskList.add(new Task("T3", 1, 4, 2));
		taskList.add(new Task("T4", 1, 4, 2));
		taskList.add(new Task("T5", 1, 4, 1));
		taskList.add(new Task("T6", 1, 4, 1));
		taskList.add(new Task("T7", 1, 5, 3));
		taskList.add(new Task("T8", 1, 5, 3));
		taskList.add(new Task("T9", 1, 6, 5));
		taskList.add(new Task("T10", 1, 6, 5));
		*/
		
		/*
		taskList.add(new Task("T1", 3, 4, 0, "R", true, false));
		taskList.add(new Task("T2", 7, 14, 0, "R", false, true));
		taskList.add(new Task("T3", 10, 15, 3, "R", false, true));
		taskList.add(new Task("T4", 5, 7, 0));
		taskList.add(new Task("T5", 4, 7, 0));
		*/
		
		/*
		taskList.add(new Task("T1", 3, 6, 2));
		taskList.add(new Task("T2", 3, 6, 0));
		taskList.add(new Task("T3", 4, 7, 0));
		taskList.add(new Task("T4", 4, 8, 4));
		taskList.add(new Task("T5", 14, 31, 4));
		taskList.add(new Task("T6", 5, 11, 0));
		*/
		
		/*
		taskList.add(new Task("T1", 1, 2, 1, "R1", true, false));
		taskList.add(new Task("T2", 1, 2, 1, "R2", true, false));
		taskList.add(new Task("T3", 1, 4, 2, "R1", false, true));
		taskList.add(new Task("T4", 1, 4, 2, "R3", false, true));
		taskList.add(new Task("T5", 1, 3, 2));
		taskList.add(new Task("T6", 2, 7, 1, "R1", false, true));
		taskList.add(new Task("T7", 1, 8, 4, "R2", true, false));
		taskList.add(new Task("T8", 2, 8, 4, "R1", true, false));
		taskList.add(new Task("T9", 2, 7, 0, "R2", true, false));
		taskList.add(new Task("T10", 2, 8, 5, "R3", true, false));
		*/
		
		System.out.println("Task List In: ");
		System.out.println("\t" + taskList);
		System.out.println();
		System.out.println();
		
		MyopicSchedulingAlgorithm scheduler = new MyopicSchedulingAlgorithm();
		scheduler.setInputs(2, 3, 3, 1);
		scheduler.initialize();
		
		scheduler.calculateSchedule(taskList);
	}

}
