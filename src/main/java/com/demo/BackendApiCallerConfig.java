package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BackendApiCallerConfig {

	@Autowired
	private Environment env;

	@Bean
	public RestTemplate restTemplate() {
		String baseUrl = env.getProperty("backendService.baseUrl");		
		return new RestTemplateBuilder().rootUri(baseUrl).build();
	}
}
