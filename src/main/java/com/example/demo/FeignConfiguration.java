package com.example.demo;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

	@Bean
	public Logger.Level logLevel(){
		return Logger.Level.BASIC;
	}

}
