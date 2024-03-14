package com.aristowebapi.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.aristowebapi.filter.JwtAuthFilter;
import com.aristowebapi.serviceimpl.UserInfoService; 
  
@Configuration
@EnableWebSecurity
 
public class SecurityConfig { 
  
    @Autowired
    private JwtAuthFilter authFilter; 
    
  
    // User Creation 
    @Bean
    public UserDetailsService userDetailsService() { 
        return new UserInfoService(); 
    } 
  
    // Configuring HttpSecurity 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
        /*return http.csrf().disable() 
                .authorizeHttpRequests() 
                .antMatchers("/auth/welcome").permitAll()
                .antMatchers("/auth/addNewUser").permitAll()
                .antMatchers("/auth/generateToken").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement() 
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                .and() 
                .authenticationProvider(authenticationProvider()) 
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) 
                .build();*/ 
        
        
                http.csrf().disable() 
                .authorizeHttpRequests() 
                .antMatchers("/auth/welcome", "/auth/addNewUser", "/auth/generateToken", "/auth/updateUser").permitAll() 
                .and() 
                .authorizeHttpRequests().antMatchers("/auth/user/**").authenticated() 
                .and() 
                .authorizeHttpRequests().antMatchers("/api/mis/**").authenticated() 
                .and() 
                .authorizeHttpRequests().antMatchers("/auth/admin/**").authenticated() 
                .and() 
                .sessionManagement() 
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); 
//                .and() 
//                .authenticationProvider(authenticationProvider()) 
//                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); 
    	
    	
     	 

     	//Exception handling configuration
    	 
     	/*http
     	    .exceptionHandling()
     	    .authenticationEntryPoint((request, response, e) -> 
     	    {
     	    		
     	    	response.setContentType("application/json;charset=UTF-8");
     	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
     	        response.getWriter().write(
     	        		 "{\"timestamp\":\"" + LocalDateTime.now()+ "\","+
     	        				 "\"url\":\"" +request.getRequestURL()+"\","+
     	        				 "\"method\":\"" +request.getMethod()+"\","+
     	        				 "\"status\":\"" +response.getStatus()+"\","+
     	        				 "\"message\":\"Access Denied or Token Expired\"}"
     	        		);
     	        		
     	    });    	*/

    	 
    	    http.authenticationProvider(authenticationProvider());
            
    	    http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    	    
    	  //  http.addFilterBefore(new JwtRequestFilter(authenticationManager(), userDetailsService, new JWTHelper(), handlerExceptionResolver), UsernamePasswordAuthenticationFilter.class);
    	    
    	    return http.build();
    } 
  
    // Password Encoding 
    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    } 
  
    @Bean
    public AuthenticationProvider authenticationProvider() { 
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
        authenticationProvider.setUserDetailsService(userDetailsService()); 
        authenticationProvider.setPasswordEncoder(passwordEncoder()); 
        return authenticationProvider; 
    } 
  
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
        return config.getAuthenticationManager(); 
    } 
  
  
}