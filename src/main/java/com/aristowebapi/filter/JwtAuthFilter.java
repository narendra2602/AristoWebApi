package com.aristowebapi.filter;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aristowebapi.service.TokenBlacklist;
import com.aristowebapi.serviceimpl.JwtService;
import com.aristowebapi.serviceimpl.UserInfoService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

  
// This class helps us to validate the generated jwt token 
@Component
public class JwtAuthFilter extends OncePerRequestFilter { 
  
    @Autowired
    private JwtService jwtService; 
  
    @Autowired
    private UserInfoService userDetailsService;

    @Autowired
    private TokenBlacklist tokenBlacklist;
    
    //@Qualifier("handlerExceptionResolver")
    // private  HandlerExceptionResolver exceptionResolver;
    
      
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException { 
       try {	
    	String authHeader = request.getHeader("Authorization"); 
        String token = null; 
        String username = null; 

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        

        if (authHeader != null && authHeader.startsWith("Bearer ")) { 
            token = authHeader.substring(7);
            if(!tokenBlacklist.isBlacklisted(token))
            {
               username = jwtService.extractUsername(token);
            
            }
            else
            { 
            	// Token is blacklisted or expired, deny access
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                System.out.println("After logout comes in else part....."+tokenBlacklist.isBlacklisted(token));
                //username = jwtService.extractUsername(token);
                throw new UnsupportedJwtException("Invalid token");
            }

        } 
        
  
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { 
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); 
            if (jwtService.validateToken(token, userDetails,request)) { 
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
            }
            

        } 

        filterChain.doFilter(request, response);
       } catch (SignatureException ex) {
           // Invalid signature/claims
    	   System.out.println("SignatureException ");
       } catch (ExpiredJwtException ex) {
    	   response.setStatus(HttpStatus.UNAUTHORIZED.value());
    	   response.getOutputStream().print("{ \"message\":\"JWT Token has expired\"}");
    	   response.setContentType(MediaType.APPLICATION_JSON_VALUE);
       } catch (UnsupportedJwtException ex) {
    	   System.out.println("In UnsupportedJwtException ");
    	   response.setStatus(HttpStatus.UNAUTHORIZED.value());
    	   response.getOutputStream().print("{ \"message\":\"JWT Token is invalid\"}");
    	   response.setContentType(MediaType.APPLICATION_JSON_VALUE);
           // Unsupported JWT token
       } catch (MalformedJwtException ex) {
    	   System.out.println("In Malformed JWT token ");
           // Malformed JWT token
       } catch (IllegalArgumentException ex) {
    	   System.out.println("In JWT token is empty ");
           // JWT token is empty
       } 
       
    } 
} 