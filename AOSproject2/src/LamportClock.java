
public class LamportClock {
	volatile int currentClock;
	int incrementValue;
	
	public LamportClock() {
		this.currentClock = 0;
		this.incrementValue = 1;
	}
	
	/**
	 * Increment the logical clock with the given increment value
	 * @param incrementValue
	 */
	public LamportClock(int incrementValue) {
		this.currentClock = 0;
		this.incrementValue = incrementValue;
	}
	
	/**
	 * Increment the logical clock value with the given initial and increment value
	 * @param incrementValue
	 * @param intialClockValue
	 */
	public LamportClock(int incrementValue,int intialClockValue) {
		this.currentClock = intialClockValue;
		this.incrementValue = incrementValue;
	}
	
	/**
	 * Gets the next tick value
	 * @return
	 */
	public int getNextTickTime(){
		this.currentClock = this.currentClock + this.incrementValue;
		return this.currentClock;
	}
	
	public int getComparedTimeStamp(int messageTimeStamp) {
		if(messageTimeStamp > this.currentClock)
			this.currentClock = messageTimeStamp;
		this.currentClock = this.currentClock + this.incrementValue;
		return this.currentClock;
	}
	
	/**
	 * This method compares the current clock time and the time which was piggybacked on the message and increment it.
	 * @param messageTimeStamp
	 */
	public void setComparedTimeStamp(int messageTimeStamp){
		if(messageTimeStamp > this.currentClock)
			this.currentClock = messageTimeStamp + this.incrementValue;
		else
			this.currentClock = this.currentClock + this.incrementValue;
	}
}
	