package com.cabot.calculator.unittestcases;

import com.cabot.calculator.classes.Calculations;

import junit.framework.TestCase;

public class TestCalculator extends TestCase {

	Calculations cp=new Calculations();
	public TestCalculator(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testAddition() {
		cp.setValues(12.25,2.5);
		assertTrue("12.25+2.5",cp.addition()==14.75);
	}

	public void testSubtraction() {
		cp.setValues(12.25,2.5);
		assertTrue("12.25-2.5",cp.subtraction()==9.75);
	}

	public void testDivision()throws Exception
	{
		cp.setValues(12.25,2.5);
		assertTrue("12.25/2.5",cp.division()==4.9);
	}

	public void testMultiply() {
		cp.setValues(12.25,2.5);
		assertTrue("12.25X2.5",cp.multiply()==30.625);
	}

	public void testModulo() {
		cp.setValues(13.25,2);
		assertTrue("13.25+2",cp.modulo()==1);
	}

	public void testExtractLeft() {
		//cp.setValues(12.25,2.5);
		assertTrue("12.25+2.5X3-16/2X3",cp.extractLeft("12.25+2.5X3-16/2X3",14)==11);
	}

	public void testExtractRight() {
		assertTrue("12.25+2.5X3-16/2X3",cp.extractRight("12.25+2.5X3-16/2X3",14)==16);
	}

	public void testEvaluate_Expr()throws Exception
	{
		
		assertTrue(cp.evaluate_Expr("12.5+2.5+3X5-4+5.25/2X3")==33.875);
	}

}
