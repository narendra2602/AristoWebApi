package com.aristowebapi.serviceimpl;
import java.util.ArrayList;
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
import com.aristowebapi.entity.UserInfo;
import com.aristowebapi.request.ChangePasswordRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.UserApiResponse;
import com.aristowebapi.response.UserResponse; 
  
@Service
public class UserInfoService implements UserDetailsService { 
  
    @Autowired 
    private UserInfoRepository repository; 
  
    @Autowired
    private PasswordEncoder encoder; 
  
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
  
//        Optional<UserInfo> userDetail = repository.findByLoginName(username); 
        Optional<UserInfo> userDetail = repository.findByLoginNameAndUserStatus(username,"Y"); 
        
        // Converting userDetail to UserDetails 
        return userDetail.map(UserInfoDetails::new) 
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username)); 
    } 
  
    public UserApiResponse addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword())); 
        userInfo.setUserStatus("Y");
        userInfo.setRoles(userInfo.getUserType()==1?"Branch":userInfo.getUserType()==2?"All India":userInfo.getUserType()==3?"PMT":userInfo.getUserType()==4?"HQ":"Multiple Branch");
        userInfo=repository.save(userInfo);
        UserApiResponse userResponse=new UserApiResponse();
        userResponse.setId(userInfo.getId());

        userResponse.setMessage("User Added Successfully");
        return userResponse; 
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
    	List<UserInfo> newList = userList.stream().filter(u->u.getUserType()==4)
                  // List<UserInfo> newList = userList.stream()
        		    .map(e -> {
//        		      e.setPassword(encoder.encode(e.getPassword().trim()));
        		      e.setPassword(encoder.encode("123"));
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

    
    public ApiResponse<UserResponse> getAllUser() {
    	List<UserInfo> userList = repository.findAll();
    	UserInfo userInfo=null;
    	UserResponse userResponse= null;
    	List<UserResponse> userResponseList=new ArrayList<UserResponse>();
    	
    	int size=userList.size();
    	for(int i=0;i<size;i++)
    	{
    	   userInfo = userList.get(i);
    	   userResponse= new UserResponse();
    	   userResponse.setLoginId(userInfo.getId());
    	   userResponse.setFname(userInfo.getFname());
    	   userResponse.setLoginName(userInfo.getLoginName());
    	   userResponse.setUtype(userInfo.getUserType());
    	   userResponse.setLastLoginDate(userInfo.getLastLoginDateTime().toString());
    	   userResponse.setUserStatus(userInfo.getUserStatus());
    	   userResponse.setUserType(userInfo.getUserType()==1?"Branch":userInfo.getUserType()==2?"All India":userInfo.getUserType()==3?"PMT":userInfo.getUserType()==4?"HQ":userInfo.getUserType()==5?"Multiple Branch":"Admin");
    	   userResponseList.add(userResponse);
    	}
    	ApiResponse<UserResponse> apiResponse = new ApiResponse<>("User List", size,userResponseList);
        return apiResponse; 
    } 

    
     public int changePassword(ChangePasswordRequest request )
     {
    		 UserInfo userDetail = repository.findById(request.getUserId());
    		 
    		 System.out.println(" id+kya hai "+userDetail.getId());
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
     
     public int resetPassword(int userId )
     {
    		 UserInfo userDetail = repository.findById(userId);
    		 
    		
    		 int update=0;
        	 if (userDetail!=null)
        	 {
        			 userDetail.setPassword(encoder.encode("123"));
        			 repository.save(userDetail);
        			 update=1;
        	 }

    		 return update;
     }

     public int updateStatus(int userId,String userStatus )
     {
    		 UserInfo userDetail = repository.findById(userId);
    		 
    		
    		 int update=0;
        	 if (userDetail!=null)
        	 {
        			 userDetail.setUserStatus(userStatus);
        			 repository.save(userDetail);
        			 update=1;
        	 }

    		 return update;
     }

     
} 