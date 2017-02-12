package com.gcmassari.tempmonitor.reader;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.gcmassari.tempmonitor.model.TemperatureSample;
import com.gcmassari.tempmonitor.model.TemperatureSamples;
import com.opencsv.CSVReader;

public class CsvReader {

	private final static String TIMESTAMP_FORMAT_US = "MM/dd/yyyy h:mm:ss a";
	private final static String TIMESTAMP_FORMAT_UK = "MM/dd/yyyy HH:mm:ss";
	
	/**
	 * 
	 * @param filePath
	 * @return Temperature samples read form the given file.
	 * File is expected to be in CSV format
	 *   <li>Expected format of timestamp is US format: "MM/dd/yyyy h:mm:ss a"</li>
	 *   <li>Expected file format is 
	 *  Point, Timestamp,Temperature (° C), Alarms,<br/>
	 *  1,12/14/2016 11:15:24 AM,5.1,,
	 *  (first line of the csv file - the header- gets ignored)</li>
	 
	 * @throws IOException
	 */
	public static TemperatureSamples readTemperaturesFromFile(String filePath) throws IOException {
		return readTemperaturesFromFile(filePath, TIMESTAMP_FORMAT_US);
	}

	/**
	 * 
	 * @param filePath
	 * 
	 * @param timestampFormat: format to use to parse timestamps with eg: "MM/dd/yyyy h:mm:ss a" (Use simpledateformat conventions)
	 * @return Temperature samples read form the file in CSV format, use given param. to parse dates
	 *   <li>Expected format of timestamp is US format: "MM/dd/yyyy h:mm:ss a"</li>
	 *   <li>Expected file format is 
	 *  Point, Timestamp,Temperature (° C), Alarms,<br/>
	 *  1,12/14/2016 11:15:24 AM,5.1,,
	 *  (first line of the csv file - the header- gets ignored)</li>
	 * @throws IOException
	 */
	public static TemperatureSamples readTemperaturesFromFile(String filePath, String timestampFormat) throws IOException {
		
		// TODO check time stamp format event. throw illegal argument exception
		List<TemperatureSample> tempSamples = new ArrayList<TemperatureSample>();

		// TODO consider make a parse.parameter out of all those vars to use by parsing the temp. files 
		final char SEPARATOR = ',';
		final char QUOTE_CHAR = '"';
		final char ESCAPE_CHAR = '\\';
		final int linesToSkipAtBeginning = 1;

		CSVReader reader = null;

		try {
			TemperatureSample tempEntry=null;
			reader = new CSVReader(new FileReader(filePath), SEPARATOR, QUOTE_CHAR, ESCAPE_CHAR, linesToSkipAtBeginning);
			String[] record;
			long counter = 0L;

			while ((record = reader.readNext()) != null) {
				// System.out.println("Entry " + (++counter) + " [Point= " + record[0] + ", Date/Time= " + record[1] + " , Temp (" + DEGREE_SYMBOL + "C)= " + record[2] + " , Alarms= " + record[3] + "]");
				try {
					
					// TODO GIM: manage point not parsable as long
					long point = Long.parseLong(record[0]);
					tempEntry = new TemperatureSample(point, record[1], record[2], timestampFormat);
				} catch (Exception e) {
					// TODO GIM manage parse exception. eg record[i] == null
					System.out.println("Exception parsing line" + counter);// TODO replace with logging
					e.printStackTrace();
					// TODO  ignore wrong temp? generate warning? try with another Temperature format?
				}

				if (tempEntry != null) {
					tempSamples.add(tempEntry);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}

		return new TemperatureSamples(tempSamples);
	}

}
