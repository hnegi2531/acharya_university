package com.au.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.au.clickpay.ClickError;
import com.au.model.ClickPayment;
import com.au.payme.OrderDetailsForOrderIdGeneration;
import com.au.repository.ClickPaymentRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.au.repository.CandidateWalkinRepository;

@Service
public class ClickPaymentService {
	
	
	Logger log = LoggerFactory.getLogger(ClickPaymentService.class);
	
	@Autowired
	private ClickPaymentRepository click_payment_repo;
	
	@Value("${click.merchant_id}")
	private String merchant_id;
	
	@Value("${click.merchant_user_id}")
	private String merchant_user_id;
	
	@Value("${click.service_id}")
	private String service_id;
	
//	@Autowired
//	private ClickError click_error;
	
	@Autowired
	private CandidateWalkinRepository can_repo;
	
public HashMap<String,Object> startingOfPayment(HashMap<String,Object> candidate_details) throws ParseException{
		
		HashMap<String, Object> result = new HashMap<>();
		HashMap<String, Object> for_order_details = new HashMap<>();
		
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
		String parse_date = date.format(format);
		System.out.println("ggggggggggggggyyyyyyyyyy "+parse_date);;

		for_order_details.put("candidate_id",(Integer)candidate_details.get("candidate_id"));
		for_order_details.put("candidate_name",(String)candidate_details.get("candidate_name"));
//		for_order_details.put("current_datetime",date);

		OrderDetailsForOrderIdGeneration order=new OrderDetailsForOrderIdGeneration();
		order.setOrder_details(for_order_details);
		order.setPrefix_suffix_word("AUZ");
		order.setLength_without_prefix(16);

		String generated_order_id =ResponseHandler.orderIdGenerationForPayment(order);
		System.out.println( "generated_order_id " +generated_order_id);
		
		ClickPayment cp = new ClickPayment();
		cp.setAmount((Float) ((Integer) candidate_details.get("amount")).floatValue());
		cp.setCandidate_id((Integer) candidate_details.get("candidate_id"));
		cp.setMerchant_trans_id(generated_order_id);
		
		if (candidate_details.containsKey("payment_for") && candidate_details.get("payment_for") != null
				&& (!(candidate_details.containsKey("fee_head_amount_restriction_id")))) {
			
			cp.setPayment_for(candidate_details.get("payment_for").toString());
			
		} else if (candidate_details.containsKey("payment_for") && candidate_details.get("payment_for") != null
				&& candidate_details.containsKey("fee_head_amount_restriction_id")
				&& candidate_details.get("fee_head_amount_restriction_id") != null) {

			cp.setPayment_for(candidate_details.get("payment_for").toString());
			cp.setRestriction_amount_payer((String) candidate_details.get("candidate_name"));
			cp.setPayer_email(candidate_details.get("payer_email").toString());
			cp.setMobile(candidate_details.get("mobile").toString());
			cp.setAuid_or_other_info(candidate_details.get("auid_or_other_info").toString());
			cp.setFee_head_amount_restriction_id((Integer) candidate_details.get("fee_head_amount_restriction_id"));
		}
//		cp.setSign_time(parse_date);
		click_payment_repo.save(cp);

		result.put("Url-Get","https://my.click.uz/services/pay");
		result.put("merchant_id", merchant_id);
		result.put("amount", (Integer)candidate_details.get("amount"));
		result.put("merchant_user_id", merchant_user_id);
		result.put("service_id", service_id);
		result.put("transaction_param", generated_order_id);
		result.put("return_url", "https://www.stageapi-acharyainstitutes.in/api/student/completeClickPayment");
		System.out.println("gfhhhhhhhhhhh " +ClickError.SUCESS+" jjjjj " +Integer.parseInt(ClickError.SUCESS.getString()));
		return result;
	}
	
	
	public HashMap<String,Object> saveResponseOfPrepareClickPayment(Map<String,Object> click_pay) throws JsonProcessingException {
		
		System.out.println("PRe gggggggggggg " +click_pay);
		ObjectMapper Obj = new ObjectMapper(); 
		 String jsonStr = Obj.writeValueAsString(click_pay); 
		 System.out.println(" ddddddddddddd " +jsonStr);
		 
		 System.out.println("gggggggggggg " +click_pay);
		 
		HashMap<String, Object> transaction_response = new HashMap<>();
		
		System.out.println("yyyyyyyyyyyyyyyyyyyyy "+click_pay.get("merchant_trans_id"));
		ClickPayment cp=click_payment_repo.getClickPaymentOnMerchantTransId(click_pay.get("merchant_trans_id").toString());
		
		if(cp ==null) {
			
			transaction_response.put("click_trans_id", click_pay.get("click_trans_id"));
			transaction_response.put("merchant_trans_id", null);
			transaction_response.put("merchant_confirm_id", null);
			transaction_response.put("error", -5);
			transaction_response.put("error_note", "User does not exist");
			return transaction_response;
			
		} else if(cp != null && cp.getAmount() != Float.parseFloat(click_pay.get("amount").toString())){
			
			System.out.println("jjjjjjjjjjjjjjj "+ cp.getAmount() +" llll "+click_pay.get("amount"));
			
			transaction_response.put("click_trans_id", click_pay.get("click_trans_id"));
			transaction_response.put("merchant_trans_id", cp.getMerchant_trans_id());
			transaction_response.put("merchant_confirm_id", cp.getClick_payment_id());
			transaction_response.put("error", -2);
			transaction_response.put("error_note", "Incorrect parameter amount");
			return transaction_response;
			
		}else if(cp != null && cp.getError() != null &&cp.getError() < 0){
			
			transaction_response.put("click_trans_id",click_pay.get("click_trans_id"));
			transaction_response.put("merchant_trans_id", cp.getMerchant_trans_id());
			transaction_response.put("merchant_confirm_id", cp.getClick_payment_id());
			transaction_response.put("error", -9);
			transaction_response.put("error_note", "Transaction cancelled");
			return transaction_response;
			
		} else {

			System.out.println( "////////////////////////////////");
			cp.setClick_trans_id(click_pay.get("click_trans_id").toString());
			cp.setService_id(Integer.parseInt(click_pay.get("service_id").toString()));
			cp.setClick_paydoc_id(click_pay.get("click_paydoc_id").toString());
			cp.setAction(Integer.parseInt(click_pay.get("action").toString()));
			cp.setError(Integer.parseInt(click_pay.get("error").toString()));
			cp.setError_note(click_pay.get("error_note").toString());
			cp.setSign_time(click_pay.get("sign_time").toString());
			cp.setSign_string(click_pay.get("sign_string").toString());
			click_payment_repo.save(cp);
			
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			transaction_response.put("click_trans_id", click_pay.get("click_trans_id"));
			transaction_response.put("merchant_trans_id", cp.getMerchant_trans_id());
			transaction_response.put("merchant_prepare_id", cp.getClick_payment_id());
			transaction_response.put("error", 0);
			transaction_response.put("error_note", "Success");

			System.out.println("gfhhhhhhhhhhh error" +ClickError.SUCESS.toString() +ClickError.SUCESS.getString());
			return transaction_response;
		}	
	}
	
	public HashMap<String,Object> saveResponseOfCompleteClickPayment(Map<String,Object> click_pay) throws JsonProcessingException {
		
		System.out.println("Com gggggggggggg " +click_pay);
		ObjectMapper Obj = new ObjectMapper(); 
		 String jsonStr = Obj.writeValueAsString(click_pay); 
		 System.out.println(" ddddddddddddddd " +jsonStr);
		 
		 System.out.println("gggggggggggg " +click_pay);
		 
		HashMap<String, Object> transaction_response = new HashMap<>();
		
		ClickPayment cp=click_payment_repo.getClickPaymentOnMerchantTransId(click_pay.get("merchant_trans_id").toString());
		
		if(cp ==null) {
			
			transaction_response.put("click_trans_id", click_pay.get("click_trans_id"));
			transaction_response.put("merchant_trans_id", null);
			transaction_response.put("merchant_confirm_id", null);
			transaction_response.put("error", -5);
			transaction_response.put("error_note", "User does not exist");
			return transaction_response;
			
		} else if(cp != null && cp.getAmount() != Float.parseFloat(click_pay.get("amount").toString())){
			
			transaction_response.put("click_trans_id", click_pay.get("click_trans_id"));
			transaction_response.put("merchant_trans_id", cp.getMerchant_trans_id());
//			transaction_response.put("merchant_confirm_id", cp.getClick_payment_id());
			transaction_response.put("error", -2);
			transaction_response.put("error_note", "Incorrect parameter amount");
			return transaction_response;
			
		}else if(cp != null && cp.getError() != null &&cp.getError() < 0){
			
			transaction_response.put("click_trans_id", click_pay.get("click_trans_id"));
			transaction_response.put("merchant_trans_id", cp.getMerchant_trans_id());
			transaction_response.put("merchant_confirm_id", cp.getClick_payment_id());
			transaction_response.put("error", -9);
			transaction_response.put("error_note", "Transaction cancelled");
			return transaction_response;
			
		} else {
			
			cp.setMerchant_prepare_id(Integer.parseInt(click_pay.get("merchant_prepare_id").toString()));
			cp.setAction(Integer.parseInt(click_pay.get("action").toString()));
			cp.setError(Integer.parseInt(click_pay.get("error").toString()));
			cp.setError_note(click_pay.get("error_note").toString());
			click_payment_repo.save(cp);

			transaction_response.put("click_trans_id", click_pay.get("click_trans_id"));
			transaction_response.put("merchant_trans_id", cp.getMerchant_trans_id());
			transaction_response.put("merchant_confirm_id", cp.getClick_payment_id());
			transaction_response.put("error", 0);
			transaction_response.put("error_note", "Success");
			can_repo.updateFormFilledPrecentageOfCandidate(cp.getCandidate_id());
			System.out.println("gfhhhhhhhhhhh error" +ClickError.SUCESS.toString() +ClickError.SUCESS.getString());
			return transaction_response;
		}
	}


	public HashMap<String,Object> getClickPaymentStatus(String merchant_trans_id) {
		HashMap<String,Object> payment_action_details=new HashMap<>();
		ClickPayment cp=click_payment_repo.getClickPaymentOnMerchantTransId(merchant_trans_id);
		if(cp != null && cp.getAction()==1) {
			payment_action_details.put("status", true);
			return payment_action_details;
		}else {
			payment_action_details.put("status", false);
			return payment_action_details;
		}
		
	}
	
}
