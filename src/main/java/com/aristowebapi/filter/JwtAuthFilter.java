package com.aristowebapi.filter;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.aristowebapi.serviceimpl.JwtService;
import com.aristowebapi.serviceimpl.UserInfoService;

import io.jsonwebtoken.ExpiredJwtException;

  
// This class helps us to validate the generated jwt token 
@Component
public class JwtAuthFilter extends OncePerRequestFilter { 
  
    @Autowired
    private JwtService jwtService; 
  
    @Autowired
    private UserInfoService userDetailsService;
    
    //@Qualifier("handlerExceptionResolver")
    //private  HandlerExceptionResolver exceptionResolver;
    
      
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException { 
    	String authHeader = request.getHeader("Authorization"); 
        String token = null; 
        String username = null; 

        System.out.println("in doFilterInernal method before validate token .......");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        

        token = authHeader.substring(7);
        username = jwtService.extractUsername(token);
        if (username == null) {
            // validation failed or token expired
            filterChain.doFilter(request, response);
            return;
        }
        
/*        if (authHeader != null && authHeader.startsWith("Bearer ")) { 
            token = authHeader.substring(7);
            System.out.println("in doFilterInernal method before calling exttractUsername .......");

            username = jwtService.extractUsername(token);
            
            System.out.println("in doFilterInernal method after calling extractUsername .......");

        }*/ 
        
  
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { 
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); 
            System.out.println("before validate token tak aaya hai.......");
            if (jwtService.validateToken(token, userDetails,request)) { 
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 
                SecurityContextHolder.getContext().setAuthentication(authToken);

                
            }
            
            System.out.println("after validate token tak aaya hai.......");

        } 
        
	   // final String expiredMsg = (String) request.getAttribute("expired");
	   // final String msg = (expiredMsg != null) ? expiredMsg : "Unauthorized";
	   // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);

        filterChain.doFilter(request, response);
    } 
} 