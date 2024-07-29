package com.aristowebapi.serviceimpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aristowebapi.entity.UserInfo; 
  
public class UserInfoDetails implements UserDetails { 
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name; 
    private String password; 
    private int loginId;
    private int userType;
    private String fname;
    private List<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();; 
  
    public UserInfoDetails(UserInfo userInfo) { 
        name = userInfo.getLoginName(); 
        password = userInfo.getPassword();
        loginId = userInfo.getId();
        userType = userInfo.getUserType();
        fname=userInfo.getFname();
       // String ROLE_PREFIX = "ROLE_";
       // authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + userInfo.getRoles()));
        authorities = Arrays.stream(userInfo.getRoles().split(",")) 
                .map(SimpleGrantedAuthority::new) 
                .collect(Collectors.toList()); 
    } 
  
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { 
        return authorities; 
    } 

    
    public int getLoginId() {
		return loginId;
	}

	public int getUserType() {
		return userType;
	}

	public String getFname() {
		return fname;
	}

	@Override
    public String getPassword() { 
        return password; 
    } 
  
    @Override
    public String getUsername() { 
        return name; 
    } 
  
    @Override
    public boolean isAccountNonExpired() { 
        return true; 
    } 
  
    @Override
    public boolean isAccountNonLocked() { 
        return true; 
    } 
  
    @Override
    public boolean isCredentialsNonExpired() { 
        return true; 
    } 
  
    @Override
    public boolean isEnabled() { 
        return true; 
    } 
} 