package com.gcmassari.tempmonitor.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gcmassari.tempmonitor.model.EvaluationParams;
import com.gcmassari.tempmonitor.model.TemperatureSample;
import com.gcmassari.tempmonitor.model.TemperatureSamples;

public class Utility {
	 private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	public static String getSampleAsJsonString(TemperatureSample sample, EvaluationParams params) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		sb.append(" \"temp\":\"");
		sb.append( sample.getTemperature().toString());
		
		sb.append("\","); 
		
		sb.append("\"eval\":\"" + params.eval(sample));
		
		sb.append("\","); 
		
		sb.append(" \"timestamp\":\"");
		sb.append( DATE_FORMAT.format(sample.getTimestamp()));
		sb.append("\"");
		
		sb.append(" }");
		return sb.toString();
	}
	
	
    public static EvaluationParams getDefaultEvaluationParams(TemperatureSamples temps) {
    	Date firstDate = getFirstDate(temps);
    	Date lastDate = getLastDate(temps);
    	return new EvaluationParams(BigDecimal.valueOf(5L), BigDecimal.valueOf(10L), firstDate, lastDate);
    }


	private static Date getFirstDate(TemperatureSamples temps) {
		// TODO REfine to get the earliest Date even if this is not the first
		if (temps.isEmpty()) {
			return null;
		}
		return temps.getFirstSample().getTimestamp();
	}

	private static Date getLastDate(TemperatureSamples temps) {
		// TODO REfine to get the earliest Date even if this is not the first
		if (temps.isEmpty()) {
			return null;
		}
		return temps.getLastSample().getTimestamp();
	}
}
