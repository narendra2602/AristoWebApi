package com.aristowebapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:endpoint-mapping.properties")
public class AristoWebAppConfig {

}
