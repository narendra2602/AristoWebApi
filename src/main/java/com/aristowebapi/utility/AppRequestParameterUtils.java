package com.aristowebapi.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aristowebapi.serviceimpl.JwtService;

@Component
public class AppRequestParameterUtils {
	 
	@Autowired
	private  JwtService jwtService;
	
	public  int[] getRequestBodyParameters(String authHeader)
	{

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
