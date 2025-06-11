package com.aristowebapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties  
public class AristoWebApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AristoWebApiApplication.class, args);
		
	}

}
   