package com.gcmassari.tempmonitor.main;

import java.io.IOException;
import java.math.BigDecimal;
//import java.time.Instant;

import java.util.Date;
import java.util.List;

import org.joda.time.Instant;

import com.gcmassari.tempmonitor.model.TemperatureSample;
import com.gcmassari.tempmonitor.model.TemperatureSamples;
import com.gcmassari.tempmonitor.reader.CsvReader;

public class Main {
	final static String DEGREE  = "\u00B0"; // == "°" not correctly rendered on DOS console (local=DE?) - for DOS console in this environement  ° is correctly rendered by (char)('z' +126)
	// TODO DEGREE_DOS_CONSOLE or implement run time logic to detect the OS
	final static String DEGREE_DOS_CONSOLE  =  "" + (char)('z' +126); // in case determine OS runtime using  org.apache.commons.lang3.SystemUtils.SystemUtils.IS_OS_WINDOWS_7
	final static String DEGREE_SYMBOL = DEGREE_DOS_CONSOLE;

    public static void main(String[] args) throws IOException {
    	   
    	// TODO manage/ read/ pass following parameters: 
        String csvFile = "C:/Users/gc/workspace/tempmonitor/tempmonitor/temp/input-data-samples/0_DF73109DR0_0.csv";
        BigDecimal minTemp = new BigDecimal("4.7");
        BigDecimal maxTemp = new BigDecimal("5.7");
        
        Instant startEvaluationTime = Instant.parse("2016-12-14T10:59:00.00Z"); // T1
        Instant endEvaluationTime =   Instant.parse("2016-12-15T08:00:00.00Z"); // T2 
//        TemporalAmount skipFirstMinutes = Duration.of(60, ChronoUnit.MINUTES);// 
//        TemporalAmount skipLastMinutes = Duration.of(120, ChronoUnit.MINUTES);
        
        TemperatureSamples temperatureSamples = CsvReader.readTemperaturesFromFile(csvFile);
        // TODO validate temperatures 
        //  - pointNo unique & ordered asc, 
        //  - timestamps ordered asc,regular intervals
        //  - temperatures not null and predefined"common sense"  range
        
        // TODO to implement: take only valid temperatures (t0 + Delta_t1 , tf-Delta_t2) 
        Date initialTime = temperatureSamples.getFirstSample().getTimestamp();
        Date finalTime = temperatureSamples.getLastSample().getTimestamp();
        System.out.println("Initial time:" + initialTime + " = " + initialTime);
        System.out.println("Final time:"+ finalTime  + " = " + finalTime);
        System.out.println("Considering ALL temperature samples (no time filtering)\n");
        
//        LocalDateTime dt = LocalDateTime.now();
//        dt.plus(skipFirstMinutes);
        Instant t1 = startEvaluationTime;   //initialTime.toInstant().plus(skipFirstMinutes);
        Instant t2 = endEvaluationTime; // finalTime.toInstant().minus(skipLastMinutes);
        
        System.out.println("** T1 = Initial time + 1h:" + t1);
        System.out.println("** T2 = Final time   - 1h :"+ t2);
        
        for (TemperatureSample sample : temperatureSamples.getSamples()) {
        	Instant timestamp = new Instant(sample.getTimestamp().getTime());
			if (!timestamp.isBefore(t1) && !timestamp.isAfter(t2)) {
				if (sample.isTemperatureLowerThan(minTemp)) {
					System.out.printf("!!! Temp  lower than min (%.1f): [ %d ] at %tc : (%.1f) %sC %n", minTemp.doubleValue(), sample.getPointNumber(), sample.getTimestamp(), sample.getTemperature(), DEGREE_SYMBOL);
				} else {
					if (sample.isTemperatureHigherThan(maxTemp)) {
						System.out.printf("!!! Temp HIGHER than min (%.1f): [ %d ] at %tc : (%.1f) %sC %n", maxTemp.doubleValue(), sample.getPointNumber(), sample.getTimestamp(), sample.getTemperature(), DEGREE_SYMBOL);
					}
					else {
						System.out.printf("!!! Temp ok                    : [ %d ] at %tc : (%.1f) %sC %n", sample.getPointNumber(), sample.getTimestamp(), sample.getTemperature(), DEGREE_SYMBOL);
					}
				}
			} else {
				System.out.printf("-- Ignored [ %d ] at %s : (%.1f) %sC %n", sample.getPointNumber(), timestamp.toString(), sample.getTemperature(), DEGREE_SYMBOL);
			}

		}
        
    }

}
