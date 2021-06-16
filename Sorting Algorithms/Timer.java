
public class Timer {

	// This class servers as a timer to measure how long it takes to run code
	// It takes the system time before and after a line of code is run and then outputs it in microseconds
	
	private long startTime = 0;
	private long endTime = 0;
	
	public void start()	{
		startTime = System.nanoTime();
	}
	
	public void stop()	{
		endTime = System.nanoTime();
	}
	
	public void getTimeEllapsed()	{
		System.out.println("Time ellapsed: " + (this.endTime - this.startTime) / 1000 + " microseconds.");
	}
	
}
