//package com.au.event;
//
//import java.util.concurrent.Executor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//
//@Configuration
//public class TaskExecutor {
//
//	Logger log = LoggerFactory.getLogger(TaskExecutor.class);
//
//	@Bean
//	public Executor taskExecutor() {
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setCorePoolSize(5);
//		executor.setMaxPoolSize(10);
//		executor.setQueueCapacity(100);
//		executor.setThreadNamePrefix("Async-Thread-");
//		executor.initialize();
//		return executor;
//	}
//
//}
