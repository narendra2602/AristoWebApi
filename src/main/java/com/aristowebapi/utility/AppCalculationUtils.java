package com.aristowebapi.utility;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.aristowebapi.constant.AristoWebMessageConstant;

public class AppCalculationUtils {

	
	@Autowired
	private static AristoWebMessageConstant aristoWebMessageConstant;
	
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


/*	public static double calculatePmr(long sale,int fs)
	{
		double pmr=0.00;  
		pmr=fs!=0?Math.round(((sale*1.0)/fs)*100.0)/100.0:0;
		return  pmr;
	}
*/
	public static int calculatePmr(long sale,int fs)
	{
		int pmr=0;  
		pmr=fs!=0?Math.round(((sale)/fs)):0;
		return  pmr;
	}

	
	public static long valueDivideByLacs(long value)
	{
		return (value/100000);
	}

	public static double valueDivideByLacs(double value)
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
	
	public static double calculatePmr(long sale,long fs)
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

	public static double roundToDecimal2Places(double number)
	{
		  
		number=Math.round(number*100.0)/100.0;
		return number;
	}


/*	public static String getDivName(int div_code) {

		
		Map<Integer,String> divMap = new HashMap<>();  
		divMap.put(1, "MF - ");
		divMap.put(2, "TF - ");
		divMap.put(3, "GENETICA - ");
		divMap.put(10, "MF2 - ");
		divMap.put(20, "MF3 - ");
		divMap.put(30, "MF4 - ");
		return divMap.get(div_code);
	}

*/


}
