package com.gcmassari.tempmonitor.model;

public enum Evaluation {
	UNDEFINED("undefined"),
	DONT_CARE("dont-care"),
	OK("ok"),
	NOK("nok");
	
	private final String value;
	
	private Evaluation(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
