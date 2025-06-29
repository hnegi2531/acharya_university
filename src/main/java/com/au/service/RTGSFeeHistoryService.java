package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.RTGSFeeHistory;
import com.au.repository.RTGSFeeHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class RTGSFeeHistoryService {

private Logger logger = LoggerFactory.getLogger(RTGSFeeHistoryService.class);
	
	@Autowired
	private RTGSFeeHistoryRepository rtgs_fee_history_repository;
	
	
		public RTGSFeeHistory saveRTGSFeeHistory(RTGSFeeHistory rtfh) {
			 return rtgs_fee_history_repository.save(rtfh);
			
		}
		
		public List<RTGSFeeHistory> getActiveRTGSFeeHistory(){
			return rtgs_fee_history_repository.getActiveRTGSFeeHistory();
		}
		
		
		public ResponseEntity<Object> getAllRTGSFeeHistory1(Pageable pageable, Object keyword){
			
			Page<Object> response1 =rtgs_fee_history_repository.getAllRTGSFeeHistory1(pageable, keyword );
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}
		
		public ResponseEntity<Object> getAllRTGSFeeHistory2(Pageable pageable){
			
			Page<Object> response = rtgs_fee_history_repository.getAllRTGSFeeHistory2(pageable);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		}
		
		public RTGSFeeHistory getRTGSFeeHistoryById(Integer rtgs_fee_history_id) {
			return rtgs_fee_history_repository.findById(rtgs_fee_history_id)
					.orElseThrow(() -> new ResourceNotFoundException("RTGS Fee Receipt History Not Found:" + rtgs_fee_history_id));
		}
		
		public RTGSFeeHistory updateRTGSFeeHistory(RTGSFeeHistory rtfh) {
			return rtgs_fee_history_repository.save(rtfh);
		}
		
		public void delete(Integer rtgs_fee_history_id) {
			rtgs_fee_history_repository.delete1(rtgs_fee_history_id);
		}
		
		public void activateRTGSFeeHistory(Integer rtgs_fee_history_id) {
			rtgs_fee_history_repository.activateRTGSFeeHistory(rtgs_fee_history_id);
		}
		
		public List<HashMap<String,Object>> allRTGSFeeHistoryDetails(Integer bank_transaction_history_id){
			return rtgs_fee_history_repository.allRTGSFeeHistoryDetails(bank_transaction_history_id);
		}

    public ResponseEntity<Object> rtgsAmountForPaidAtBoardTag(Integer fcYearId, String receiptNo) {
		List<Map<String,Object>> rtgsdetails= rtgs_fee_history_repository.rtgsAmountForPaidAtBoardTag(fcYearId,receiptNo);
		return ResponseHandler.generateResponse(true,HttpStatus.OK,rtgsdetails);
    }
}
