package com.au.sms;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class SmsIntegrationForUZB {
	
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<HashMap<String,Object>> sendSmsGateway(List<HashMap<String,Object>> sending_data) {

		//		RestTemplate restTemplate1 = new RestTemplate();

		MultiValueMap<String,Object> sms_final_data=new LinkedMultiValueMap<>();
		sms_final_data.add("login", "acharya");
		sms_final_data.add("password", "W1AkcDA38Aia3ir5f1A3");
		sms_final_data.add("data", sending_data);


		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED));

		System.out.println("APPLICATION_FORM_URLENCODED :::::::::::::::::::::::: ");

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>((LinkedMultiValueMap<String, Object>) sms_final_data,headers);

		System.out.println("LinkedMultiValueMap :::::::::::::::::::::::: "); 

		//	     System.out.println("{{{{{{{{{{{{{{{{{{{{{{{ :::::::::::::::::::::::: "+restTemplate1.exchange(
		//		         "http://185.8.212.184/smsgateway/", HttpMethod.POST, entity, String.class).getBody().toString());
		//	     
		//	     System.out.println("response_of_sms from exchange method response_of_sms 1");



		//	      String response_of_sms= restTemplate1.exchange(
		//	         "http://185.8.212.184/smsgateway/", HttpMethod.POST, entity, String.class).getBody().toString();
		//	      
		//	      System.out.println("response_of_sms from exchange method restTemplate2 "+response_of_sms);


		RestTemplate restTemplate2 = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
		restTemplate2.getMessageConverters().add(converter);


		//	      Object response_of_sms4= restTemplate2.exchange(
		//	 	         "http://185.8.212.184/smsgateway/", HttpMethod.POST, entity, Object.class).getBody();
		//	 	      
		//	 	      System.out.println("response_of_sms from exchange method response_of_sms4 "+(List<HashMap<String,Object>>)response_of_sms4);
		//	 	     List<HashMap<String,Object>> object_conversion=(List<HashMap<String,Object>>)response_of_sms4;
		//	 	     
		//	 	     System.out.println("ffffffffffffff "+object_conversion);

		List<HashMap<String,Object>> response_of_sms5= (List<HashMap<String, Object>>) restTemplate2.exchange(
				"http://185.8.212.184/smsgateway/", HttpMethod.POST, entity, Object.class).getBody();

		System.out.println("response_of_sms from exchange method response_of_sms5 "+response_of_sms5);

		//	 	   ResponseEntity<Object> response_restTemplate2 = restTemplate2.postForEntity(
		//		    		  "http://185.8.212.184/smsgateway/", entity , Object.class);
		//		      
		//		      System.out.println("response_of_sms from postForEntity method  response_restTemplate2 "+(List<HashMap<String,Object>>)response_restTemplate2.getBody());

//	 	   ResponseEntity<Object> response_restTemplate2 = restTemplate2.postForEntity(
		//		    		  "http://185.8.212.184/smsgateway/", entity , Object.class);
		//		      
		//		      System.out.println("response_of_sms from postForEntity method  response_restTemplate2 "+(List<HashMap<String,Object>>)response_restTemplate2.getBody());

		return (List<HashMap<String, Object>>) response_of_sms5;

	}






	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<HashMap<String, Object>> checkSmsStatus(List<HashMap<String,Object>> sending_data_for_status) {

		//		RestTemplate restTemplate1 = new RestTemplate();

		MultiValueMap<String,Object> sms_final_data=new LinkedMultiValueMap<>();
		sms_final_data.add("login", "acharya");
		sms_final_data.add("password", "W1AkcDA38Aia3ir5f1A3");
		sms_final_data.add("data", sending_data_for_status);


		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED));
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>((LinkedMultiValueMap<String, Object>) sms_final_data,headers);

		//	     System.out.println("{{{{{{{{{{{{{{{{{{{{{{{ :::::::::::::::::::::::: status "+restTemplate1.exchange(
		//		         "http://185.8.212.184/smsgateway/status/", HttpMethod.POST, entity, String.class).getBody().toString());
		//	     
		//	     System.out.println("response_of_sms from exchange method response_of_sms 1 status");



		//	      String response_of_sms= restTemplate1.exchange(
		//	         "http://185.8.212.184/smsgateway/status/", HttpMethod.POST, entity, String.class).getBody().toString();
		//	      
		//	      System.out.println("response_of_sms from exchange method restTemplate2 status"+response_of_sms);

		RestTemplate restTemplate2 = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
		restTemplate2.getMessageConverters().add(converter);


		//	      Object response_of_sms4= restTemplate2.exchange(
		//	 	         "http://185.8.212.184/smsgateway/status/", HttpMethod.POST, entity, Object.class).getBody();
		//	 	      
		//	 	      System.out.println("response_of_sms from exchange method response_of_sms4 status"+(List<HashMap<String,Object>>)response_of_sms4);
		//	 	     List<HashMap<String,Object>> object_conversion=(List<HashMap<String,Object>>)response_of_sms4;
		//	 	     
		//	 	     System.out.println("ffffffffffffff "+object_conversion);

		List<HashMap<String,Object>> response_of_sms5= (List<HashMap<String, Object>>) restTemplate2.exchange(
				"http://185.8.212.184/smsgateway/status/", HttpMethod.POST, entity, Object.class).getBody();

		System.out.println("response_of_sms from exchange method response_of_sms5 status"+response_of_sms5);

		//	 	   ResponseEntity<Object> response_restTemplate2 = restTemplate2.postForEntity(
		//		    		  "http://185.8.212.184/smsgateway/status/", entity , Object.class);
		//		      
		//		 System.out.println("response_of_sms from postForEntity method status response_restTemplate2 "+(List<HashMap<String,Object>>)response_restTemplate2.getBody());

		return response_of_sms5;

	}

}
