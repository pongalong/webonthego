package com.tscp.util.profiler;

import java.util.Stack;

public class TimerStack extends Stack<Long> {
	private static final long serialVersionUID = 692917492770375345L;
	protected int numRequests;
	protected long totalExecutionTime;

	public Long start() {
		long startTime = super.push(System.currentTimeMillis());
		numRequests++;
		return startTime;
	}

	public Long stop() {
		long executionTime = super.isEmpty() ? 0l : System.currentTimeMillis() - super.pop();
		totalExecutionTime += executionTime;
		return executionTime;
	}

	public void reset() {
		super.clear();
		numRequests = 0;
		totalExecutionTime = 0l;
	}

	public int getNumRequests() {
		return numRequests;
	}

	public long getTotalExecutionTime() {
		return totalExecutionTime;
	}

	public long getAverageExecutionTime() {
		return numRequests == 0l ? 0l : totalExecutionTime / (long) numRequests;
	}

}