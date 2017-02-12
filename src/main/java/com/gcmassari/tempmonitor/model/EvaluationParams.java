package com.gcmassari.tempmonitor.model;

import java.math.BigDecimal;
import java.util.Date;

public class EvaluationParams {
	
	private BigDecimal minTemp;
	private BigDecimal maxTemp;
	private Date ignoreDataBefore;
	private Date ignoreDataAfter;

	public EvaluationParams() {
		super();
	}
	
	public EvaluationParams(BigDecimal minTemp, BigDecimal maxTemp,
			Date ignoreDataBefore, Date ignoreDataAfter) {
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.ignoreDataBefore = ignoreDataBefore;
		this.ignoreDataAfter = ignoreDataAfter;
	}
	
	public BigDecimal getMinTemp() {
		return minTemp;
	}
	public BigDecimal getMaxTemp() {
		return maxTemp;
	}
	public Date getIgnoreDataBefore() {
		return ignoreDataBefore;
	}
	public Date getIgnoreDataAfter() {
		return ignoreDataAfter;
	}

	public void setMinTemp(BigDecimal minTemp) {
		this.minTemp = minTemp;
	}

	public void setMaxTemp(BigDecimal maxTemp) {
		this.maxTemp = maxTemp;
	}

	public void setIgnoreDataBefore(Date ignoreDataBefore) {
		this.ignoreDataBefore = ignoreDataBefore;
	}


	public void setIgnoreDataAfter(Date ignoreDataAfter) {
		this.ignoreDataAfter = ignoreDataAfter;
	}

	public Evaluation eval(TemperatureSample sample) {
		if (sample.getTemperature() == null) {
			return Evaluation.UNDEFINED;
		}
		if (sample.getTimestamp() == null || sample.getTimestamp().before(ignoreDataBefore) || sample.getTimestamp().after(ignoreDataAfter)) {
			return Evaluation.DONT_CARE;
		}
//		int c1 = sample.getTemperature().compareTo(minTemp); // 4.9 - 5
//		int c2 = sample.getTemperature().compareTo(maxTemp); // 4.9-10
//		System.out.println(c1);
//		System.out.println(c2);
		if (sample.getTemperature().compareTo(minTemp) < 0 || sample.getTemperature().compareTo(maxTemp) > 0) {
			return Evaluation.NOK;
		}
		return Evaluation.OK;
	}
}
