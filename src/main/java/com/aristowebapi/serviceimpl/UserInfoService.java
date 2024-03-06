package com.aristowebapi.serviceimpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aristowebapi.dao.UserInfoRepository;
import com.aristowebapi.dto.UserInfo;
import com.aristowebapi.request.ChangePasswordRequest; 
  
@Service
public class UserInfoService implements UserDetailsService { 
  
    @Autowired
    private UserInfoRepository repository; 
  
    @Autowired
    private PasswordEncoder encoder; 
  
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
  
        Optional<UserInfo> userDetail = repository.findByLoginName(username); 
  
        // Converting userDetail to UserDetails 
        return userDetail.map(UserInfoDetails::new) 
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username)); 
    } 
  
    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword())); 
        repository.save(userInfo);
        return "User Added Successfully"; 
    } 

    
    public String updateAllUser() {
    	List<UserInfo> userList = repository.findAll();
        //List<UserInfo> newList = userList.stream().map(user->user.setPassword(encoder.encode(user.getPassword())).collect(Collectors.toList()));
/*           List<UserInfo> newList =   userList.stream().map(e -> {
        	      e.setPassword(encoder.encode(e.getPassword()));
        	      return e;
        	    });*/
           
    	//   userList.stream().filter(u->u.getId()>150).map(user->{user.setPassword(encoder.encode(user.getPassword()));
    	 //                          return user;}).forEach(y->System.out.println(y.getPassword()));
								 
								

    	   
//           List<UserInfo> newList = userList.stream().filter(u->u.getId()>150)
                   List<UserInfo> newList = userList.stream()
        		    .map(e -> {
        		      e.setPassword(encoder.encode(e.getPassword().trim()));
        		      return e;
        		    })
        		    .collect(Collectors.toList());
           
//    	userInfo.setPassword(encoder.encode(userInfo.getPassword()));
           
//           UserInfo user = repository.findById(163).get();
//           user.setPassword(encoder.encode(user.getPassword().trim()));
           //repository.save(user);
        repository.saveAll(newList);
        return "User updated Successfully"; 
    } 

     public int changePassword(ChangePasswordRequest request )
     {
    		 UserInfo userDetail = repository.findById(request.getUserId());
    		 int update=0;
        	 if (userDetail!=null)
        	 {
        		 boolean check = encoder.matches(request.getOldPassword(), userDetail.getPassword());
        		 //boolean check=true;
        		 if(check)
        		 {
        			 userDetail.setPassword(encoder.encode(request.getNewPassword().trim()));
        			 repository.save(userDetail);
        			 update=1;
        		 }
        	 }

    		 return update;
     }
} 