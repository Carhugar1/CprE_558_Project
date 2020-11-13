package cpre_558_Project;

public class ScheduledTask {

	public Task t;
	
	public int startTime;
	public int endTime;
	
	public ScheduledTask() {
		// basic constructor
	}
	
	public ScheduledTask(Task t, int startTime, int endTime) {
		this.t = t;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return t.toString();
	}
}
