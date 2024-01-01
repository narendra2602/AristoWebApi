package com.aristowebapi.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:aristoweb-msg.properties")
public class AristoWebMessageConstant {
	
	@Value("${upload_message}")
	public String message;

}
