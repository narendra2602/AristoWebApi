package com.aristowebapi.dao;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.UserInfo; 
  
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> { 
    Optional<UserInfo> findByLoginName(String username); 
    Optional<UserInfo> findByLoginNameAndUserStatus(String username,String status);
    UserInfo findById(int  userId);
    
}