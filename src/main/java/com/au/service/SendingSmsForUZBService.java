package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.au.sms.SmsIntegrationForUZB;


@Service
public class SendingSmsForUZBService {
	
	Logger log = LoggerFactory.getLogger(SendingSmsForUZBService.class);
	
	private  final String candidate_registration_text="Hii ,Registration Success";
	
	
	public List<HashMap<String,Object>> sendSms(String mobile_number,String message_text){
		
		 List<HashMap<String,Object>> final_data_for_send=new ArrayList<>();
		 List<HashMap<String,Object>> final_data_for_status=new ArrayList<>();
		 
		HashMap<String,Object> data_for_send=new HashMap<String,Object>();
		data_for_send.put("phone", mobile_number);
		data_for_send.put("text", message_text);
		
		final_data_for_send.add(data_for_send);
		
		List<HashMap<String,Object>> send_response=SmsIntegrationForUZB.sendSmsGateway(final_data_for_send);
		
		HashMap<String,Object> data_for_status=new HashMap<String,Object>();
		System.out.println("sendSmsGateway :::::::::::::::::::::::: "); 
		send_response.stream().forEach(sr -> {
			data_for_status.put("request_id", sr.get("request_id"));
			System.out.println("request_id request_id request_id" +sr.get("request_id"));
		});
		final_data_for_status.add(data_for_status);
		List<HashMap<String,Object>> status_response=SmsIntegrationForUZB.checkSmsStatus(final_data_for_status);
		
		return status_response;
	}

}
