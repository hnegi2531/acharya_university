package com.au.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Component
@Data
@AllArgsConstructor
@ToString
@ComponentScan
public class AmazonProperties {
	
//	@Value("${endpointUrl}")
//	private String endpointUrl;
//	@Value("${bucketName}")
//	private String bucketName;
//	@Value("${accessKey}")
//	private String accessKey;
//	@Value("${secretKey}")
//	private String secretKey;

}
