package com.gcmassari.tempmonitor.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TemperatureSample {
	private long pointNumber;
	private Date timestamp;
	private BigDecimal temperature;
	
	public TemperatureSample(long point, String timestamp, String temperatureCelsius, String timestampFormat) throws ParseException, IllegalArgumentException  {
		SimpleDateFormat dateFormat = new SimpleDateFormat(timestampFormat); // migh throw IllegalArgumentException if format is not a valid one
		this.pointNumber = point;
		this.temperature = new BigDecimal(temperatureCelsius);
		this.timestamp = dateFormat.parse(timestamp);
	}
	
	public long getPointNumber() {
		return pointNumber;
	}
	
	public void setPointNumber(long pointNumber) {
		this.pointNumber = pointNumber;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public BigDecimal getTemperature() {
		return temperature;
	}
	
	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}
	
	public boolean isTemperatureLowerThan(BigDecimal minTemp) {
		return temperature != null && temperature.compareTo(minTemp) < 0;
	}

	public boolean isTemperatureHigherThan(BigDecimal maxTemp) {
		return temperature != null && temperature.compareTo(maxTemp) > 0;
			
	}
	//TODO GIM : write unit test !! delete if not used!!!
	public Evaluation evaluateSample(BigDecimal minTemp, BigDecimal maxTemp, Date ignoreBefore, Date ignoreAfter) {
		if (temperature == null) {
			return Evaluation.UNDEFINED;
		}
		if (timestamp.before(ignoreBefore) || timestamp.after(ignoreAfter)) {
			return Evaluation.DONT_CARE;
		}
		if (temperature.compareTo(minTemp) < -1 || temperature.compareTo(maxTemp) > 1) {
			return Evaluation.NOK;
		}
		return Evaluation.OK;
	}
	
	@Override
	public String toString() {
		return temperature.toString();
	}
}
