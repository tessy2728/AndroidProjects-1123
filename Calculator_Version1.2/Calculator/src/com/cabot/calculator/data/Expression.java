package com.cabot.calculator.data;

public class Expression {
	private long Exp_ID;
	private String Exprn;
	private long timeStamp;
	private boolean checked;
	//constructor
	public Expression(String exp, long ts)
	{
		Exprn=exp;
		timeStamp=ts;
	}
	//constructor
	public Expression(long id, String exp,long ts)
	{
		Exprn=exp;
		timeStamp=ts;
		Exp_ID=id;
	}
	
	/*
	 * return expression ID
	 */
	public long getID()
	{
		return Exp_ID;
	}
	
	/*
	 * return expression string
	 */
	public String getExprn()
	{
		return Exprn;
	}
	
	/*
	 * return timestamp
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}
	
	/*
	 * set checked status
	 */
	public void setChecked(boolean checked)
	{
		this.checked=checked;
	}
	
	/*
	 * return checked status
	 */
	public boolean isChecked()
	{
		return checked;
	}
	
	/*
	 * set expression ID
	 */
	public void setID(long id)
	{
		Exp_ID=id;
	}
	
	/*
	 * set expression string
	 */
	public void setExprn(String exp)
	{
		Exprn=exp;
	}
	
	/*
	 * set timestamp
	 */
	public void setTimeStamp(long ts)
	{
		timeStamp=ts;
	}
}
