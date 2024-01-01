package com.aristowebapi.utility;

public class AppCalculationUtils {

	public static  double calculateAch(long sale,long target)
	{
		double ach=0.00;  
//		ach1=Math.ceil((sale*1.0/target)*100);
		ach=Math.round(((sale*1.0/target)*100)*100.0)/100.0;
		return  ach;
	}

	public static double calculateGth(long sale,long lys)
	{
		double gth=0.00;  
		gth=Math.round((((sale*1.0/lys)*100)-100)*100.0)/100.0;
		return gth;
	}


	public static double calculatePmr(long sale,int fs)
	{
		double pmr=0.00;  
//		pmr=Math.ceil((sale*1.0/fs));
		pmr=Math.round(((sale*1.0)/fs)*100.0)/100.0;
		return  pmr;
	}
	
	public static long valueDivideByLacs(long value)
	{
		return (value/100000);
	}

	
}
