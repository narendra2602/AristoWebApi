package com.aristowebapi.service;

import com.aristowebapi.request.LoginRequest;
import com.aristowebapi.response.LoginResponse;

public interface LoginService {
	
	LoginResponse authenticateUser(LoginRequest request);

}
