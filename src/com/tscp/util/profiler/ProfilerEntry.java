package com.tscp.util.profiler;

public class ProfilerEntry {
	public static final long fifteenMinutes = 900000l;
	protected long startTime;
	protected long endTime;
	protected int numRequests;
	protected long totalExecutionTime;
	protected String key;

	/* **************************************************
	 * Constructors
	 */

	public ProfilerEntry() {
		// do nothing
	}

	public ProfilerEntry(String key) {
		this.key = key;
	}

	/* **************************************************
	 * Profiler Methods
	 */

	public void start() {
		numRequests++;
		startTime = System.currentTimeMillis();
	}

	public void stop() {
		endTime = System.currentTimeMillis();
		totalExecutionTime += endTime - startTime;
		startTime = 0l;
	}

	public long getAverageExecutionTime() {
		return numRequests == 0l ? 0l : totalExecutionTime / (long) numRequests;
	}

	/* **************************************************
	 * Getters and Setters
	 */

	public String getKey() {
		return key;
	}

	public void setKey(
			String key) {
		this.key = key;
	}

	public int getNumRequests() {
		return numRequests;
	}

	public long getLastUpdated() {
		return endTime;
	}

	/* **************************************************
	 * Helper Methods
	 */

	public boolean isStale() {
		return System.currentTimeMillis() - endTime > fifteenMinutes;
	}

	public void reset() {
		numRequests = 0;
		totalExecutionTime = 0l;
	}

	/* **************************************************
	 * Debug Methods
	 */

	@Override
	public String toString() {
		return "[numRequests=" + numRequests + ", averageExecutionTime=" + getAverageExecutionTime() + "]";
	}

}
