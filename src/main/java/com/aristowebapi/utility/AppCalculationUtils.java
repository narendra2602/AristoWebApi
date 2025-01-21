package com.aristowebapi.utility;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

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
		double pmr1=0.00;
		pmr1=fs!=0?Math.round((sale*1.0)/fs):0;
		pmr=(int)pmr1;

		return  pmr;
	}

	
	
	public static long valueDivideByLacs(long value)
	{
		return (value/100000);
	}
	
	public static double valueDivideByLacInDouble(double value)
	{
		double val=0.00;  
		val=Math.round((value/100000)*100.0)/100.0;
		return  val;
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

	public static double calculateSdf(double sale,double target)
	{
		double sdf=0.00;  
		sdf=Math.round((sale*1.0-target)*100)/100.0;
		return sdf;
	}

	
	public static double calculatePmr(double sale,int fs)
	{
		
		double pmr=0.00;  
		pmr=fs!=0?Math.round(((sale*1.0)/fs)*100.0)/100.0:0;
		return  pmr;
	}
	
	public static long calculatePmr(long sale,long fs)
	{
		long pmr=0;  
		pmr=fs!=0?Math.round(((sale)/fs)*100)/100:0;
		return  pmr;
	}
	
	public static double calculateExpiryRatio(long expiry,long sale)
	{
		double expiryratio=0.00;  
		expiryratio=sale!=0?Math.round(((expiry*1.0/sale)*100)*100)/100.0:0;
		return  expiryratio;
	}

	
	public static double addDouble(double doubleVal1,double doubleVal2)
	{
		return  (doubleVal1==0)
				?doubleVal2
				:(doubleVal2==0)
				?doubleVal1
				:Math.round(((new BigDecimal(doubleVal1)).add(new BigDecimal(doubleVal2))).doubleValue()*100.0)/100.00;
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
