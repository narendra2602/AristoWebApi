package com.aristowebapi.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.dto.UserInfo;
import com.aristowebapi.request.LoginRequest;
import com.aristowebapi.response.TokenResponse;
import com.aristowebapi.serviceimpl.JwtService;
import com.aristowebapi.serviceimpl.UserInfoDetails;
import com.aristowebapi.serviceimpl.UserInfoService;



@RestController
@RequestMapping("/auth") 
public class UserController { 
  
    @Autowired
    private UserInfoService service; 
  
    @Autowired
    private JwtService jwtService; 
  
    @Autowired
    private AuthenticationManager authenticationManager; 
  
    @GetMapping("/welcome") 
    public String welcome() { 
        return "Welcome this endpoint is not secure"; 
    } 
  
    @PostMapping("/addNewUser") 
    public String addNewUser(@RequestBody UserInfo userInfo) { 
    	System.out.println(userInfo.getPassword());
    	System.out.println(userInfo.getLoginName());
    	System.out.println(userInfo.getUserType());
    	System.out.println(userInfo.getFname());
        return service.addUser(userInfo); 
    } 

    
    @PostMapping("/updateUser") 
    public String updateUser() { 
//    	System.out.println(userInfo.getPassword());
//    	System.out.println(userInfo.getLoginName());
//    	System.out.println(userInfo.getUserType());
//    	System.out.println(userInfo.getFname());
        return service.updateAllUser(); 
    } 

    
    @GetMapping("/user/userProfile") 
    @PreAuthorize("hasAuthority('ROLE_USER')") 
    public String userProfile() { 
        return "Welcome to User Profile"; 
    } 
  
    @GetMapping("/admin/adminProfile") 
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public String adminProfile() { 
        return "Welcome to Admin Profile"; 
    } 
  
    @PostMapping("/generateToken") 
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody LoginRequest authRequest) { 
    	System.out.println(authRequest.getUsername());
    	System.out.println(authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())); 
        
         
        UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
        
        
       // if (authentication.isAuthenticated()) { 
            String token =  jwtService.generateToken(authRequest.getUsername(),userDetails.getLoginId(),userDetails.getUserType(),userDetails.getFname());
            return new ResponseEntity<TokenResponse>(new TokenResponse(token),HttpStatus.OK);
            //return ResponseEntity.ok(new AuthenticationResponse(token));
        //} else { 
          //  throw new UsernameNotFoundException("invalid user request !"); 
        //} 
    } 
  
} 