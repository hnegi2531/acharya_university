package com.au.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.au.model.PaymeTransactions;
import com.au.payme.CancelTransactionResult;
import com.au.payme.CheckPerformTransactionResult;
import com.au.payme.CheckTransactionResult;
import com.au.payme.CreateTransactionResult;
import com.au.payme.OrderDetailsForOrderIdGeneration;
import com.au.payme.PerformTransactionResult;
import com.au.repository.CandidateWalkinRepository;
import com.au.repository.PaymePaymentGatewayRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class PaymePaymentGatewayService {
	
	Logger log = LoggerFactory.getLogger(PaymePaymentGatewayService.class);
	
	
	@Autowired
	private PaymePaymentGatewayRepository payme_payment_gateway_repo;
	
	@Autowired
	private CandidateWalkinRepository can_repo;
	
	@Value("${payme.merchantkey}")
	private String merchantkey;
	
	@Value("${payme.merchant.key.value}")
	private String merchantkey_value;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	
	public HashMap<String,Object> saveResponseOFPayme(HashMap<String, Object> payme) {
		
//		HashMap<String, Object> request_format = (HashMap<String, Object>) payme;
		HashMap<String, Object> transaction_response = new HashMap<>();
		
		if (payme.get("method").equals("CheckPerformTransaction")) {
			return checkPerformTransaction(payme);
			
		} else if(payme.get("method").equals("CreateTransaction")){
			
			return createTransaction(payme);
		
		} else if(payme.get("method").equals("PerformTransaction")){
			
			return performTransaction(payme);
			
		}else if(payme.get("method").equals("CancelTransaction")){
			
			return cancelTransaction(payme);
			
		}else if(payme.get("method").equals("CheckTransaction")){
			
			return checkTransaction(payme);
			
		}else if(payme.get("method").equals("GetStatement")){
			
			return getStatement(payme);
		}else{
			return transaction_response;
		}

	}
	
	public HashMap<String,Object> checkPerformTransaction(HashMap<String, Object> payme){
		
		HashMap<String, Object> transaction_response = new HashMap<>();
		
		HashMap<String, Object> payme_details=(HashMap<String, Object>) payme.get("params");
		
		HashMap<String, Object> account_details=(HashMap<String, Object>)payme_details.get("account");
		
		PaymeTransactions pt_details=payme_payment_gateway_repo.checkingPaymeTransactionsOnOrderId(account_details.get("order_id").toString());

		
		if (pt_details ==null) {
			
			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31050);
			result.put("message", "Transcation Details Not Found");
			transaction_response.put("error", result);
			return transaction_response;
			
		}else if (pt_details.getAmount() == ((Integer)payme_details.get("amount")).intValue()) {
			CheckPerformTransactionResult cpt=new CheckPerformTransactionResult();
			cpt.setAllow(true);
			transaction_response.put("result", cpt);
			return transaction_response;
			
		} else {
			
			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31001);
			result.put("message", "Invalid amount");
			transaction_response.put("error", result);
			return transaction_response;
		}
	}
	
public HashMap<String,Object> createTransaction(HashMap<String, Object> payme){
		
		HashMap<String, Object> transaction_response = new HashMap<>();

		HashMap<String, Object> payme_details=(HashMap<String, Object>) payme.get("params");

		HashMap<String, Object> account_details=(HashMap<String, Object>)payme_details.get("account");

		PaymeTransactions pt_details=payme_payment_gateway_repo.checkingPaymeTransactionsOnOrderId(account_details.get("order_id").toString());

		if(pt_details == null ) {
			
			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31050 );
			result.put("message", "Transcation Details Found");
			transaction_response.put("error", result);
			return transaction_response;

		} else if (pt_details.getPaycom_transaction_id() != null && pt_details.getAmount() != ((Integer)payme_details.get("amount")).intValue()) {
			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31001);
			result.put("message", "Invalid amount");
			transaction_response.put("error", result);
			return transaction_response;
			
		} else if(pt_details.getPaycom_transaction_id() != null && (!(pt_details.getPaycom_transaction_id()).equals(payme_details.get("id")))){
			
			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31050 );
			result.put("message", "Paycom Transcation ID Not Found");
			transaction_response.put("error", result);
			return transaction_response;
			
		}else if (pt_details != null && pt_details.getState() == null ) {
			pt_details.setPaycom_transaction_id(payme_details.get("id").toString());
			pt_details.setPaycom_time((Long)payme_details.get("time"));
			pt_details.setAmount((Integer)payme_details.get("amount"));
			pt_details.setState(1);
			pt_details.setCreate_time(System.currentTimeMillis());
			payme_payment_gateway_repo.save(pt_details);

			CreateTransactionResult ctr=new CreateTransactionResult();
			ctr.setCreate_time(pt_details.getCreate_time());
			ctr.setTransaction(pt_details.getOrder_id());
			ctr.setState(pt_details.getState());
			transaction_response.put("result", ctr);

			return transaction_response;

		} else if(pt_details.getState() == 1){

			CreateTransactionResult ctr=new CreateTransactionResult();
			ctr.setCreate_time(pt_details.getCreate_time());
			ctr.setTransaction(pt_details.getOrder_id());
			ctr.setState(pt_details.getState());
			transaction_response.put("result", ctr);
			return transaction_response;
		} else if(pt_details.getState() != 1 ){

			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31008);
			result.put("message", "Unable to perform operation.");
			transaction_response.put("error", result);
			return transaction_response;
		} else {

			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31001);
			result.put("message", "Invalid amount");
			transaction_response.put("error", result);
			return transaction_response;
		}

	}
	
	public HashMap<String,Object> performTransaction(HashMap<String, Object> payme){
		
		HashMap<String, Object> transaction_response = new HashMap<>();
		HashMap<String, Object> payme_details=(HashMap<String, Object>) payme.get("params");
		PaymeTransactions pt_details=payme_payment_gateway_repo.checkingPaymeTransactions(payme_details.get("id").toString());
		System.out.println("DDDDDDDDDDDDD " +pt_details);
		if (pt_details != null && pt_details.getState() != 2 && pt_details.getState() > 0) {
			
			pt_details.setPaycom_transaction_id(pt_details.getPaycom_transaction_id());
			pt_details.setPaycom_time(pt_details.getPaycom_time());
			pt_details.setAmount(pt_details.getAmount());
			
			pt_details.setState(2);
			pt_details.setPerform_time(System.currentTimeMillis());
			PaymeTransactions updated_pt_deatails=payme_payment_gateway_repo.save(pt_details);
			
			PerformTransactionResult ptr=new PerformTransactionResult();
			ptr.setPerform_time(updated_pt_deatails.getPerform_time());
			ptr.setTransaction(updated_pt_deatails.getOrder_id());
			ptr.setState(updated_pt_deatails.getState());
			transaction_response.put("result", ptr);
			
			return transaction_response;
			
		} else if(pt_details != null && pt_details.getState() == 2){
			
			PerformTransactionResult ptr=new PerformTransactionResult();
			ptr.setPerform_time(pt_details.getPerform_time());
			ptr.setTransaction(pt_details.getOrder_id());
			ptr.setState(pt_details.getState());
			transaction_response.put("result", ptr);
			return transaction_response;
			
		} else if(pt_details != null && pt_details.getState() < 1 ){
			
			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31008);
			result.put("message","This operation cannot be performed.");
			transaction_response.put("error", result);
			return transaction_response;
		}else {
			
			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31003);
			result.put("message", "Transaction not found");
			transaction_response.put("error", result);
			return transaction_response;
		}
	}
	
	public HashMap<String,Object> cancelTransaction(HashMap<String, Object> payme){

		HashMap<String, Object> transaction_response = new HashMap<>();
		HashMap<String, Object> payme_details=(HashMap<String, Object>) payme.get("params");
		PaymeTransactions pt_details=payme_payment_gateway_repo.checkingPaymeTransactions(payme_details.get("id").toString());

		if (pt_details != null && pt_details.getState() == 1 ) {

			pt_details.setPaycom_transaction_id(pt_details.getPaycom_transaction_id());
			pt_details.setPaycom_time(pt_details.getPaycom_time());
			pt_details.setAmount(pt_details.getAmount());

			pt_details.setState(-1);
			pt_details.setCancel_time(System.currentTimeMillis());
			pt_details.setReason((Integer)payme_details.get("reason"));
			PaymeTransactions updated_pt_deatails=payme_payment_gateway_repo.save(pt_details);

			CancelTransactionResult ctr=new CancelTransactionResult();
			ctr.setCancel_time(updated_pt_deatails.getCancel_time());
			ctr.setTransaction(updated_pt_deatails.getOrder_id());
			ctr.setState(updated_pt_deatails.getState());
			transaction_response.put("result", ctr);

			return transaction_response;

		} else if (pt_details != null && pt_details.getState() != -2 ) {
			
			if(pt_details.getState() == -1) {
				CancelTransactionResult ctr=new CancelTransactionResult();
				ctr.setCancel_time(pt_details.getCancel_time());
				ctr.setTransaction(pt_details.getOrder_id());
				ctr.setState(pt_details.getState());
				transaction_response.put("result", ctr);
				return transaction_response;
			} 
//			else if(pt_details.getState() > 1){
//				HashMap<String, Object> result = new HashMap<>();
//				result.put("code", -31007);
//				result.put("message","The order has been completed. It is not possible to cancel the transaction.");
//				transaction_response.put("error", result);
//				return transaction_response;
//			}
			else {
				pt_details.setPaycom_transaction_id(pt_details.getPaycom_transaction_id());
				pt_details.setPaycom_time(pt_details.getPaycom_time());
				pt_details.setAmount(pt_details.getAmount());

				pt_details.setState(-2);
				pt_details.setCancel_time(System.currentTimeMillis());
				pt_details.setReason((Integer)payme_details.get("reason"));
				PaymeTransactions updated_pt_deatails=payme_payment_gateway_repo.save(pt_details);

				CancelTransactionResult ctr=new CancelTransactionResult();
				ctr.setCancel_time(updated_pt_deatails.getCancel_time());
				ctr.setTransaction(updated_pt_deatails.getOrder_id());
				ctr.setState(updated_pt_deatails.getState());
				transaction_response.put("result", ctr);

				return transaction_response;
			}

		} else if(pt_details != null && pt_details.getState() == -2){

			CancelTransactionResult ctr=new CancelTransactionResult();
			ctr.setCancel_time(pt_details.getCancel_time());
			ctr.setTransaction(pt_details.getOrder_id());
			ctr.setState(pt_details.getState());
			transaction_response.put("result", ctr);
			return transaction_response;

		} else {

			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31003);
			result.put("message", "Transaction not found");
			transaction_response.put("error", result);
			return transaction_response;
		}
	}
	
	public HashMap<String,Object> checkTransaction(HashMap<String, Object> payme){
		
		HashMap<String, Object> transaction_response = new HashMap<>();
		HashMap<String, Object> payme_details=(HashMap<String, Object>) payme.get("params");
		PaymeTransactions pt_details=payme_payment_gateway_repo.checkingPaymeTransactions(payme_details.get("id").toString());
		
		if (pt_details != null) {
			CheckTransactionResult ctr=new CheckTransactionResult();
			ctr.setCancel_time(pt_details.getCancel_time()==null?0:pt_details.getCancel_time());
			ctr.setTransaction(pt_details.getOrder_id());
			ctr.setState(pt_details.getState());
			ctr.setCreate_time(pt_details.getCreate_time()==null?0:pt_details.getCreate_time());
			ctr.setPerform_time(pt_details.getPerform_time()==null?0:pt_details.getPerform_time());
			ctr.setReason(pt_details.getReason());
			transaction_response.put("result", ctr);
			
			return transaction_response;
			
		} else {
			
			HashMap<String, Object> result = new HashMap<>();
			result.put("code", -31003);
			result.put("message", "Transaction not found");
			transaction_response.put("error", result);
			return transaction_response;
		}
	}
	
	public HashMap<String,Object> getStatement(HashMap<String, Object> payme){

		HashMap<String, Object> transaction_response = new HashMap<>();
		List<HashMap<String, Object>> for_transactions_list_format = new ArrayList<>();
		HashMap<String, Object> multiple_transactions_format = new HashMap<>();
		HashMap<String, Object> payme_details=(HashMap<String, Object>) payme.get("params");
		List<Map<String, Object>> pt_details=payme_payment_gateway_repo.getPaymeTransactions((Long)payme_details.get("from"),(Long)payme_details.get("to"));

		if (pt_details != null) {
			pt_details.stream().forEach(gst ->{
				HashMap<String, Object> for_modification_response = new HashMap<>();
				HashMap<String, Object> for_account_response = new HashMap<>();
				List<HashMap<String, Object>> for_receivers_response = new ArrayList<>();
				pt_details.stream().forEach(ptd -> {
					if(gst.get("order_id")== ptd.get("order_id"))	
						for_account_response.put("order_id", gst.get("order_id"));
					for_modification_response.put("account", for_account_response);

					for_modification_response.put("receivers", for_receivers_response);

					for_modification_response.put("id", gst.get("paycom_transaction_id"));
					for_modification_response.put("time", gst.get("paycom_time"));
					for_modification_response.put("amount", gst.get("amount"));
					for_modification_response.put("create_time", gst.get("create_time")==null?0:gst.get("create_time"));
					for_modification_response.put("perform_time", gst.get("perform_time")==null?0:gst.get("perform_time"));
					for_modification_response.put("cancel_time", gst.get("cancel_time")==null?0:gst.get("cancel_time"));
					for_modification_response.put("transaction", gst.get("order_id"));
					for_modification_response.put("state", gst.get("state"));
					for_modification_response.put("reason" , gst.get("reason"));

					for_transactions_list_format.add(for_modification_response);
				});
			});
			
			multiple_transactions_format.put("transactions",for_transactions_list_format);
			transaction_response.put("result", multiple_transactions_format);
			return transaction_response;

		} else {
			multiple_transactions_format.put("transactions",for_transactions_list_format);
			transaction_response.put("result", multiple_transactions_format);
			return transaction_response;
		}
	}
	
public HashMap<String,Object> startingOfPayment(HashMap<String,Object> candidate_details) throws JsonProcessingException{
		
		HashMap<String, Object> result = new HashMap<>();
		HashMap<String, Object> for_order_details = new HashMap<>();
		HashMap<String, Object> for_base64 = new HashMap<>();
		PaymeTransactions pt=new PaymeTransactions();
		pt.setAmount((Integer)candidate_details.get("amount"));
		pt.setCandidate_id((Integer)candidate_details.get("candidate_id"));
		
		if (candidate_details.containsKey("payment_for") && candidate_details.get("payment_for") != null
				&& (!(candidate_details.containsKey("fee_head_amount_restriction_id")))) {
			pt.setPayment_for(candidate_details.get("payment_for").toString());
			String auid=studentDetailsRepository.getAuidByCandidateId((Integer)candidate_details.get("candidate_id"));
			pt.setAuid_or_other_info(ObjectUtils.isNotEmpty(auid)?auid:null);
			
		} else if (candidate_details.containsKey("payment_for") && candidate_details.get("payment_for") != null
				&& candidate_details.containsKey("fee_head_amount_restriction_id")
				&& candidate_details.get("fee_head_amount_restriction_id") != null) {
			
			pt.setPayment_for(candidate_details.get("payment_for").toString());
			pt.setRestriction_amount_payer((String)candidate_details.get("candidate_name"));
			pt.setPayer_email(candidate_details.get("payer_email").toString());
			pt.setMobile(candidate_details.get("mobile").toString());
			pt.setAuid_or_other_info(candidate_details.get("auid_or_other_info").toString());
			pt.setFee_head_amount_restriction_id((Integer)candidate_details.get("fee_head_amount_restriction_id"));
		}
	    payme_payment_gateway_repo.save(pt);
		
		for_order_details.put("candidate_id",(Integer)candidate_details.get("candidate_id"));
		for_order_details.put("candidate_name",(String)candidate_details.get("candidate_name"));
//		for_order_details.put("current_datetime",new Date());
		
		OrderDetailsForOrderIdGeneration order=new OrderDetailsForOrderIdGeneration();
		order.setOrder_details(for_order_details);
		order.setPrefix_suffix_word("AUZ");
		order.setLength_without_prefix(16);
		
		 String generated_order_id =ResponseHandler.orderIdGenerationForPayment(order);
		System.out.println( "generated_order_id " +generated_order_id);
		payme_payment_gateway_repo.updateOrderId(pt.getTransaction_id(),generated_order_id);
		
		for_base64.put("candidate_id",(Integer)candidate_details.get("candidate_id"));
		for_base64.put("candidate_name",(String)candidate_details.get("candidate_name"));
		for_base64.put("amount",(Integer)candidate_details.get("amount"));
		
	     String details_json=ResponseHandler.convertMapToJsonFormat(for_base64);
	     String encodedString = (String) Base64.getEncoder().encodeToString(details_json.getBytes());

	     
		result.put("Url-Post","https://checkout.paycom.uz");
		result.put("merchant", merchantkey_value);
		result.put("amount", (Integer)candidate_details.get("amount"));
		result.put("account[order_id]", generated_order_id);
		result.put("lang", "en");
		if (candidate_details.containsKey("payment_for") && candidate_details.get("payment_for") != null) {
			result.put("description", candidate_details.get("payment_for").toString());
		} else {
			result.put("description", "Application Fee");
		}
		result.put("detail", encodedString);
		result.put("callback", "https://api-prod-acharyainstitutes.in/AcharyaInstituteUZB/api/student/paymePayment");
		return result;
	}

	public HashMap<String,Object> getPaymentStatus(String order_id) {
		HashMap<String,Object> payment_state_details=new HashMap<>();
		PaymeTransactions payme_transcation_details=payme_payment_gateway_repo.getPaymeTransactionDetails(order_id);
		if(payme_transcation_details != null && payme_transcation_details.getState()==2) {
			payment_state_details.put("status", true);
			return payment_state_details;
		}else {
			payment_state_details.put("status", false);
			return payment_state_details;
		}
		
	}
	
}
