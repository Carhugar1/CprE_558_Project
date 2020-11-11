package cpre_558_Project;

public class Task {

	/* Basic task variables */
	public String Id; // Task Identifier
	public int c; // worst-case execution time
	//public int p; // task period
	public int d; // deadline
	//public int a; // arrival time
	public int r; // ready time
	
	/* Resource task variables */
	public String r_Id; // Resource Identifier
	public boolean r_E; // Needs Resource Exclusive Access
	public boolean r_S; // Needs Resource Shared Access
	
	public Task() {
		// basic constructor
	}
	
	public Task(String Id, int c, int d, int r) {
		
		// Basics
		this.Id = Id;
		this.c = c;
		this.d = d;
		this.r = r;
		
		// Resources
		this.r_Id = null;
		this.r_E = false;
		this.r_S = false;
	}
	
	public Task(String Id, int c, int d, int r, String r_Id, boolean r_E, boolean r_S) {
		
		// Basics
		this(Id, c, d, r);
		
		// Resources
		this.r_Id = r_Id;
		this.r_E = r_E;
		this.r_S = r_S;
	}
	
	@Override
	public String toString() {
		return this.Id;
	}
	
}
