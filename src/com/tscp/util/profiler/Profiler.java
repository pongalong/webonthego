package com.tscp.util.profiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Profiler extends Stack<ProfilerEntry> {
	private static final long serialVersionUID = 3304303864376745950L;
	protected Map<String, ProfilerEntry> resultMap = new HashMap<String, ProfilerEntry>();
	protected int maxSize = 20;

	public void start() {
		ProfilerEntry entry = new ProfilerEntry();
		entry.start();
		super.push(entry);
	}

	public void start(
			String key) {

		ProfilerEntry entry = getResult(key);

		if (entry == null) {
			entry = new ProfilerEntry(key);
			resultMap.put(key, entry);
		}

		entry.start();
		super.push(entry);
	}

	public void stop() {
		ProfilerEntry entry = super.pop();

		if (entry == null)
			return;

		entry.stop();
		putResult(entry);
	}

	public void stop(
			String key) {
		ProfilerEntry entry = super.pop();

		if (entry == null)
			return;

		entry.stop();
		entry.setKey(key);
		putResult(entry);
	}

	protected ProfilerEntry getResult(
			String key) {
		return resultMap.get(key);
	}

	protected void putResult(
			ProfilerEntry entry) {
		if (resultMap.size() >= maxSize)
			clean();
		resultMap.put(entry.getKey(), entry);
	};

	public Map<String, ProfilerEntry> getResultMap() {
		return resultMap;
	}

	public void reset() {
		super.clear();
		resultMap.clear();
	}

	public void clean() {

		ProfilerEntry oldestEntry = null;

		for (ProfilerEntry entry : resultMap.values()) {
			if (entry.isStale()) {
				resultMap.remove(entry.getKey());
				continue;
			}
			if (oldestEntry == null)
				oldestEntry = entry;
			if (oldestEntry.getLastUpdated() > entry.getLastUpdated())
				oldestEntry = entry;
		}

		if (oldestEntry != null)
			resultMap.remove(oldestEntry.getKey());
	}
}
