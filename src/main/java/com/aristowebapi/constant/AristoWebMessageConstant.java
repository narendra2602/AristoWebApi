package com.aristowebapi.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:aristoweb-msg.properties")
public class AristoWebMessageConstant {
	
	@Value("${upload_message}")
	public String message;

	@Value("${chart_target_color}")
	public String target;
	
	@Value("${chart_sale_color}")
	public String sale;
	
	@Value("${chart_lys_color}")
	public String lys;

	@Value("${dashboard_pending}")
	public String pendingPi;

}
