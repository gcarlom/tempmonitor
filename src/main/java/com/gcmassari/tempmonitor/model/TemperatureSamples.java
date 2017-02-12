package com.gcmassari.tempmonitor.model;

import java.util.ArrayList;
import java.util.List;

public class TemperatureSamples {
	
	private List<TemperatureSample> samples = new ArrayList<TemperatureSample>();

	public TemperatureSamples(List<TemperatureSample> samples) {
		if (samples == null) {
			throw new IllegalArgumentException("Illegal argument: cannot initialize Temperature sample with a null argument.");
		}
		this.samples = samples;
	}

	public List<TemperatureSample> getSamples() {
		return samples;
	}
	
	public boolean isEmpty() {
		return (samples==null || samples.isEmpty());
	}
	
	public int getNumberSamples() {
		return samples.size();
	}
	
	public TemperatureSample getSample(int index) {
		if (index >= 0 && index < samples.size()) {
			return samples.get(index);
		} 
		return null;
	}
	
	public TemperatureSample getFirstSample() {
		return !samples.isEmpty() ? samples.get(0) : null;
	}
	
	public TemperatureSample getLastSample() {
		return !samples.isEmpty() ? samples.get(samples.size()-1) : null;
	}
}
