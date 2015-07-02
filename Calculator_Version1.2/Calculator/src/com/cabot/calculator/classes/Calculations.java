package com.cabot.calculator.classes;



import android.util.Log;
import android.widget.Toast;

public class Calculations {

private double arg1,arg2,result;

public final char PLUS='+';
public final char MINUS='-';
public final char DIVISION='/';
public final char MULTIPLY='X';
public final char MODE='%';
public final char SQRT='S';
public char operation='N';

	//Parameterized Constructor
	public Calculations(double firstArg,double secondArg) 
	{
		this.arg1=firstArg;
		this.arg2=secondArg;
	}
	//Default constructor 
	public Calculations()
	{
		arg1=0;arg2=0;result=0;
	}
	//To add two values
	public double addition()
	{
		return arg1+arg2;
	}
	//To subtract two values
	public double subtraction()
	{
		return arg1-arg2;
	}
	//To divide two values
	public double division()
	{
		return arg1/arg2;
		
	}
	//To get first argument
	public double getagr1()
	{
		return arg1;
	}
	//To get second argument
	public double getarg2()
	{
		return arg2;
	}
	//To set Values
	public void setValues(double op1,double op2)
	{
		arg1=op1;
		arg2=op2;
	}
	//To multiply two arguments
	public double multiply()
	{
		return arg1*arg2;
	}
	//To find the reminder
	public int modulo()
	{
		int res=(int)arg1%(int)arg2;
		if(res<0)
			return res+(int)arg2;
		else
			return res;
	}
	/*To extract argument left to the operator at the i-th position
	for evaluating the expression*/
	public int extractLeft(String exp, int i)
	{
		int start;
		char c='n';
		for(start=i-1;start>0;start--)
		{
			c=exp.charAt(start);
			if(c=='+' || c=='-' || c=='X' || c=='/' || c=='%')
			{
				if(c=='-')
					if(exp.charAt(start-1)=='+' || exp.charAt(start-1)=='-' || exp.charAt(start-1)=='X' || exp.charAt(start-1)=='/' || exp.charAt(start-1)=='%')
					{
						arg1=Double.parseDouble(exp.substring(start,i));
						return start-1;
					}
				arg1=Double.parseDouble(exp.substring(start+1,i));
				return start;//index of next operator left to the operator at ith position
			}					
		}		
		arg1=Double.parseDouble(exp.substring(0,i));
		return 0;//if no operator right to the operator at ith position
	}
	/*To extract argument right to the operator at the i-th position
	for evaluating the expression*/
	public int extractRight(String exp, int i)
	{
		int end;
		char c=exp.charAt(i-1);
		for(end=i+1;end<exp.length();end++)
		{
			c=exp.charAt(end);
			if(c=='+' || c=='-' || c=='/' || c=='%' || c=='X')
			{
				if(c=='-')
				{
					if(exp.charAt(end-1)=='+' || exp.charAt(end-1)=='-' || exp.charAt(end-1)=='X' || exp.charAt(end-1)=='/' || exp.charAt(end-1)=='%')
						continue;					
				}
				arg2=Double.parseDouble(exp.substring(i+1,end));
				return end;//index of next operator right to the operator at ith position
			}
					
		}
		
		arg2=Double.parseDouble(exp.substring(i+1,exp.length()));
		return exp.length();//if no operator right to the operator at ith position
		
		
	}
	//To evaluate an expression using precedence rule
	public double evaluate_Expr(String exp)throws ArithmeticException
	{
		String begin="",ending="";
		boolean flag=false; 
		for(int i=0;i<exp.length();i++)
		{
			if(exp.charAt(i)=='X' || exp.charAt(i)=='/' || exp.charAt(i)=='%')
			{
				flag=true;
				subStrings sb=findArguments(exp,i,begin,ending);
				Log.i("In evaluate", "Begin:"+sb.begin);
				
				switch(exp.charAt(i))
				{
					case 'X': operation=MULTIPLY;
								result=multiply();
								exp=sb.begin+""+result+""+sb.ending;
								i=0;
								break;
					case '/':operation=DIVISION;
								result=division();
								exp=sb.begin+""+result+""+sb.ending;
								i=0;
								break;
					case '%':operation=MODE;
								result=modulo();
								exp=sb.begin+""+result+""+sb.ending;
								i=0;
								break;
					default:i=0;continue;
				}
			}						
		}
		begin="";ending="";
		for(int i=0;i<exp.length();i++)
		{
			if(exp.charAt(i)=='+' || exp.charAt(i)=='-')
			{
				flag=true;
				subStrings sb=findArguments(exp,i,begin,ending);
				switch(exp.charAt(i))
				{
					case '+':operation=PLUS;
							result=addition();
							exp=sb.begin+""+result+""+sb.ending;
							i=0;
							break;
					case '-':operation=MINUS;
							result=subtraction();
							exp=sb.begin+""+result+""+sb.ending;
							i=0;
							break;
					default:i=0;continue;
				}
			}						
		}
		if(!flag) result=Double.parseDouble(exp);
		return result;
	}
	//To find the arguments and substrings in the expression
	public subStrings findArguments(String exp,int i,String begin,String ending)
	{
		int index_start,index_end;
		index_start=extractLeft(exp,i);
		index_end=extractRight(exp,i);
		if(index_start!=0)
			begin=exp.substring(0,index_start+1);
		else
			begin="";
		if(index_end!=exp.length())
			ending=exp.substring(index_end,exp.length());
		else
			ending="";
		subStrings sb=new subStrings(begin,ending); 
		return sb;
	}
}
//For storing the beginning and ending substrings so that it can be used by different functions
final class subStrings{
	public String begin;
	public String ending;	
	public subStrings(String beg,String end)
	{
		begin=beg;
		ending=end;
	}
	
}
