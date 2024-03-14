package com.aristowebapi.constant;
public class AuthenticationConfigConstants { 
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
//    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final long EXPIRATION_TIME = 86400000; // 1 day
//   public static final long EXPIRATION_TIME = 1800000; // 30 mins (1000 * 60 * 30)
//     public static final long EXPIRATION_TIME = 60000; // 1mins (1000 * 60 * 1) 
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user";
    public static final String LOGIN_URL = "/login.html";
    public static final String HOME_URL = "/index.html";
    
    
    
}