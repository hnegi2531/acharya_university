package com.au.controller;

import java.time.Duration;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RateLimitController {
	
	public static Bucket bucket;
	//public static final long JWT_TOKEN_VALIDITY = 1 * 60 ;
	
	@GetMapping("/bucketInitialization")
	public static ResponseEntity<HashMap<String,String>> bucketInitialization(){
		
		Refill refill=Refill.intervally(10000, Duration.ofMinutes(1));
		bucket=Bucket4j.builder().addLimit(Bandwidth.classic(10000, refill)).build();
		
		HashMap<String,String> hm=new HashMap<>();
		hm.put("status","BucketInitialization done successfully");
		System.out.println("Bucket Initialized");
		return new ResponseEntity<HashMap<String,String>>(hm,HttpStatus.OK);
		
	}

	

}
