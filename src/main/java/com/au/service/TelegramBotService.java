package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.au.model.Candidate_Walkin;
import com.au.repository.CandidateWalkinRepository;
import com.au.response.ResponseHandler;

@Service
public class TelegramBotService {
	
	
Logger log = LoggerFactory.getLogger(TelegramBotService.class);
	
	@Autowired
	private CandidateWalkinRepository candidate_walkin_repo;
	
	@Autowired
	private ResponseHandler response_handler;
	
//	private long telegram_chat_id;
	
	
	@Value("${telgram.bot.token}")
	private String telegram_bot_token;
	
	@Value("${telegram.bot.vrification.link}")
	private String telegram_bot_vrification_link;
	
//	public HashMap<String,Object> verificationOfTelegramIdAndUsername(String application_no_npf){
//		
//		HashMap<String,Object> response=new HashMap<>();
//		Candidate_Walkin candidate_walkin=candidate_walkin_repo.candidateWalkinDetailOnApplicationNpf(application_no_npf);
//		if( candidate_walkin.getIs_telegram_verified() == null || candidate_walkin.getIs_telegram_verified() == false) {
//			
//			response.put("is_telegram_verified", false);
//			response.put("telegram_bot_vrification_link", telegram_bot_vrification_link+application_no_npf.replaceAll("/", "-"));
//			return response;
//		}else {
//			response.put("is_telegram_verified", true);
//			return response;
//		}
//	}
//	
//	@SuppressWarnings("unchecked")
//	public HashMap<String, Object> verificationProcessOfTelegramIdAndUsername(String application_no_npf, String url) {
//		Integer update_id = candidate_walkin_repo.getLastUpdateIdOfTelegram()==null?0:candidate_walkin_repo.getLastUpdateIdOfTelegram();
//
//		Candidate_Walkin candidate_walkin = candidate_walkin_repo.candidateWalkinDetailOnApplicationNpf(application_no_npf);
//		
//		HashMap<String,Object> response=new HashMap<>();
//		HashMap<String, Object> telegram_response = response_handler.getUpdatesDetailsFromTelegramBot(update_id);
//		
//		List<HashMap<String, Object>> result_response_of_telegram = (List<HashMap<String, Object>>) telegram_response
//				.get("result");
//		
//		
//		if(candidate_walkin.getIs_telegram_verified() == null || candidate_walkin.getIs_telegram_verified() == false) {
//			
//			result_response_of_telegram.stream()
//			.filter(e -> (((HashMap<String, Object>) ((HashMap<String, Object>) e.get("message")).get("from")))
//					.get("id").toString().equals(candidate_walkin.getTelegram_number())
//					|| (((HashMap<String, Object>) ((HashMap<String, Object>) e.get("message")).get("from")))
//					.get("username").toString().equals(candidate_walkin.getTelegram_number()))
//			.forEach(hm -> {
//				
//				if (((HashMap<String, Object>) hm.get("message")).get("text").toString().equals("/start"+" "+application_no_npf.replaceAll("/", "-"))) {
//					
//					response.put("verification_of_telegram", true);
//					
//					String telegram_chat_id=((HashMap<String, Object>) ((HashMap<String, Object>) hm.get("message"))
//							.get("from")).get("id").toString();
//					
//					candidate_walkin_repo.updateTelegramUpdateId(application_no_npf,(Integer)hm.get("update_id"),telegram_chat_id);
//					
//					response_handler.sendMessageToUser(telegram_chat_id,"Congratulations!! Your registration with Acharya University is successful against Reference number "+candidate_walkin.getApplication_no_npf() +" Please proceed for application submission with the link:- " + url);
//
//				}
//
//			});
//			
//			return response;
//		}else {
//			response.put("verification_of_telegram", true);
//			
//			return response;
//		}
//
//	}
//	
//	@SuppressWarnings("unchecked")
//	public  void verificationProcessOfTelegramIdAndUsernameByWebhooks(HashMap<String, Object> getUpdatesDetailsFromTelegramBot) {
//
//		HashMap<String, Object> telegram_message_details=(HashMap<String, Object>) getUpdatesDetailsFromTelegramBot.get("message");
//		if(telegram_message_details.get("text").toString().startsWith("/start AUZ-")) {
//
//			String application_no_npf=telegram_message_details.get("text").toString().substring(7).replaceAll("-","/");
//
//
//			HashMap<String, Object> telegram_from_details=(HashMap<String, Object>)telegram_message_details.get("from");
//
//			String telegram_chat_id=((HashMap<String, Object>)telegram_message_details.get("from")).get("id").toString();
//
//			Candidate_Walkin candidate_walkin = candidate_walkin_repo.candidateWalkinDetailOnApplicationNpf(application_no_npf);
//
//			if(telegram_from_details.containsKey("username")==true) {
//				if(((telegram_chat_id.equals(candidate_walkin.getTelegram_number())) || 
//						(telegram_from_details.get("username").toString().equals(candidate_walkin.getTelegram_number())) && 
//						(candidate_walkin.getIs_telegram_verified() == null || candidate_walkin.getIs_telegram_verified() == false))) {
//
//					candidate_walkin_repo.updateTelegramUpdateId(application_no_npf,(Integer)getUpdatesDetailsFromTelegramBot.get("update_id"),telegram_chat_id);
//
//					response_handler.sendMessageToUser(telegram_chat_id,"Congratulations!! Your registration with Acharya University is successful against Reference number "+candidate_walkin.getApplication_no_npf()  +" Please proceed for application submission with the link:- " + "https://www.acharyauniv-erp.uz/candidateapplication");
//				}else {
//
//					response_handler.sendMessageToUser(telegram_chat_id,"Congratulations!! Your registration with Acharya University is successful against Reference number "+candidate_walkin.getApplication_no_npf()  +" Please proceed for application submission with the link:- " + "https://www.acharyauniv-erp.uz/candidateapplication");
//				}
//			} else {
//				response_handler.sendMessageToUser(telegram_chat_id,"Your Telegram verification is not successful. Please Set Telegram Username And Try Again!!");
//			}	
//
//		}
//	}

}
