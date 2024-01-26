package com.aristowebapi.utility;

import java.math.BigDecimal;

public class AppCalculationUtils {

	public static  double calculateAch(long sale,long target)
	{
		double ach=0.00;  
		ach=target!=0?Math.round(((sale*1.0/target)*100)*100.0)/100.0:0;

		return  ach;
	}

	public static double calculateGth(long sale,long lys)
	{
		double gth=0.00;  
		gth=lys!=0?Math.round((((sale*1.0/lys)*100)-100)*100.0)/100.0:0;
		return gth;
	}


	public static double calculatePmr(long sale,int fs)
	{
		double pmr=0.00;  
		pmr=fs!=0?Math.round(((sale*1.0)/fs)*100.0)/100.0:0;
		return  pmr;
	}
	
	public static long valueDivideByLacs(long value)
	{
		return (value/100000);
	}


	public static  double calculateAch(double sale,double target)
	{
		double ach=0.00;  
		ach=target!=0?Math.round(((sale*1.0/target)*100)*100.0)/100.0:0;

		return  ach;
	}


	public static double calculateGth(double sale,double lys)
	{
		double gth=0.00;  
		gth=lys!=0?Math.round((((sale*1.0/lys)*100)-100)*100.0)/100.0:0;
		return gth;
	}

	public static double calculatePmr(double sale,int fs)
	{
		double pmr=0.00;  
		pmr=fs!=0?Math.round(((sale*1.0)/fs)*100.0)/100.0:0;
		return  pmr;
	}
	
	
	public static double addDouble(double doubleVal1,double doubleVal2)
	{
		return  (doubleVal1==0)
				?doubleVal2
				:(doubleVal2==0)
				?doubleVal1
				:((new BigDecimal(doubleVal1)).add(new BigDecimal(doubleVal2))).doubleValue();
	}
	
}
