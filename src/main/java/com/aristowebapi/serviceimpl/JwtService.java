package com.aristowebapi.serviceimpl;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.aristowebapi.constant.AuthenticationConfigConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; 

@Component
public class JwtService {  

	public String generateToken(String userName, int loginId,int userType,String fname) { 
		Map<String, Object> claims = new HashMap<>(); 
		claims.put("usertype", userType);
		claims.put("loginid", loginId);
		claims.put("fname", fname);
	
		
		return createToken(claims, userName); 
	} 

	private String createToken(Map<String, Object> claims, String userName) { 
		return Jwts.builder() 
				.setClaims(claims) 
				.setSubject(userName) 
				.setIssuedAt(new Date(System.currentTimeMillis())) 
				.setExpiration(new Date(System.currentTimeMillis() + AuthenticationConfigConstants.EXPIRATION_TIME)) // 30 mins token expiry
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); 
	} 

	private Key getSignKey() { 
		byte[] keyBytes= Decoders.BASE64.decode(AuthenticationConfigConstants.SECRET); 
		return Keys.hmacShaKeyFor(keyBytes); 
	} 

	public String extractUsername(String token) { 
		return extractClaim(token, Claims::getSubject);
	} 

	public Date extractExpiration(String token) { 
		return extractClaim(token, Claims::getExpiration); 
	} 

	public int extractLoginId(String token) { 
		final Claims claims = extractAllClaims(token);
		return (int) claims.get("loginid");
	} 

	public int extractUserType(String token) { 
		final Claims claims = extractAllClaims(token);
		return (int) claims.get("usertype");
	} 

	public String extractUserFirstname(String token) { 
		final Claims claims = extractAllClaims(token);
		return (String) claims.get("fname");
	} 

	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { 
		final Claims claims = extractAllClaims(token); 
		return claimsResolver.apply(claims); 
	} 

	private Claims extractAllClaims(String token) { 
		return Jwts 
				.parserBuilder() 
				.setSigningKey(getSignKey()) 
				.build() 
				.parseClaimsJws(token) 
				.getBody(); 
	} 

	private Boolean isTokenExpired(String token) { 
		return extractExpiration(token).before(new Date()); 
	} 

/*	public Boolean validateToken(String token, UserDetails userDetails) { 
		final String username = extractUsername(token); 
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
	}*/ 
	
	 public boolean validateToken(String token, UserDetails userDetails,HttpServletRequest request) {
	        try {
	    		final String username = extractUsername(token); 
	    		
	    		return (username.equals(userDetails.getUsername())  && !isTokenExpired(token)); 
//	    		return (username.equals(userDetails.getUsername()) && userDetails.getequals("Y") && !isTokenExpired(token)); 
	        } catch (SignatureException ex) {
	            // Invalid signature/claims
	        } catch (ExpiredJwtException ex) {
	        	 System.out.println("Expired JWT token par aaya kya check karo");
	             request.setAttribute("expired",ex.getMessage());
	        } catch (UnsupportedJwtException ex) {
	            // Unsupported JWT token
	        } catch (MalformedJwtException ex) {
	            // Malformed JWT token
	        } catch (IllegalArgumentException ex) {
	            // JWT token is empty
	        }
	        return false;
	    }


} 
