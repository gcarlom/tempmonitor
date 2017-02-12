package com.gcmassari.tempmonitor.model;

import java.math.BigDecimal;
import java.text.ParseException;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EvaluationParamsTest {
	private EvaluationParams param;
	private static final DateTime IGNORE_BEFORE = new DateTime(2017, 3, 01, 12, 0, 0, 0);
	private static final DateTime IGNORE_AFTER = new DateTime(2017, 3, 31, 12, 0, 0, 0);
	private static final BigDecimal MIN_TEMP = BigDecimal.valueOf(5L);
	private static final BigDecimal MAX_TEMP = BigDecimal.valueOf(10L);
	
	@Before
	public void setUp() throws Exception {
		param = new EvaluationParams(MIN_TEMP, MAX_TEMP, IGNORE_BEFORE.toDate(), IGNORE_AFTER.toDate());
	}

	@Test
	public void testEvalTempInRangeShouldBeOk() throws IllegalArgumentException, ParseException {
		TemperatureSample sample = new TemperatureSample(
				1L, 
				"2017-03-15 12:00:00",
				"7.5",
				"yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(Evaluation.OK, param.eval(sample));
	}
	
	@Test
	public void testEvalTempAtLowLimitShouldBeOk() throws IllegalArgumentException, ParseException {
		TemperatureSample sample = new TemperatureSample(
				1L, 
				"2017-03-15 12:00:00",
				"5.0",
				"yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(Evaluation.OK, param.eval(sample));
	}
	
	@Test
	public void testEvalTempAtHighLimitShouldBeOk() throws IllegalArgumentException, ParseException {
		TemperatureSample sample = new TemperatureSample(
				1L, 
				"2017-03-15 12:00:00",
				"5.0",
				"yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(Evaluation.OK, param.eval(sample));
	}
	@Test
	public void testEvalTempTooLowShouldBeNok() throws IllegalArgumentException, ParseException {
		TemperatureSample sample = new TemperatureSample(
				1L, 
				"2017-03-15 12:00:00",
				"4.9",
				"yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(Evaluation.NOK, param.eval(sample));
	}
	
	@Test
	public void testEvalTempTooHighShouldBeNok() throws IllegalArgumentException, ParseException {
		TemperatureSample sample = new TemperatureSample(
				1L, 
				"2017-03-15 12:00:00",
				"10.01",
				"yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(Evaluation.NOK, param.eval(sample));
	}

}
