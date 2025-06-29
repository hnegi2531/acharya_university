package com.au;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.au.controller.RateLimitController;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//	RateLimitController.bucketInitialization();
		return application.sources(AcharyaUniversityApplication.class);
	}

}
