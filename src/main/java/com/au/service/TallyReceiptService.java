package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.au.exception.ResourceNotFoundException;
import com.au.model.TallyReceipt;
import com.au.repository.TallyReceiptRepository;
import com.au.response.ResponseHandler;

@Service
public class TallyReceiptService {
	
	@Autowired
	private TallyReceiptRepository tally_rec_repo;
	
	public TallyReceipt saveTallyReceipt(TallyReceipt tal_rec) {
		return tally_rec_repo.save(tal_rec);
	}
	
	public List<TallyReceipt> getActiveTallyReceipt(){
		return tally_rec_repo.getActiveTallyReceipt();
	}
	
	public ResponseEntity<Object> getAllTallyReceipt1(Pageable pageable, Object keyword){
		Page<Object> response1 = tally_rec_repo.getAllTallyReceipt2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getAllTallyReceipt2(Pageable pageable){
		Page<Object> response = tally_rec_repo.getAllTallyReceipt3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public TallyReceipt getTallyReceiptById(Integer tally_receipt_id) {
		return tally_rec_repo.findById(tally_receipt_id).orElseThrow(() -> new ResourceNotFoundException("Tally Receipt not found:" + tally_receipt_id ));
	}
	
	public TallyReceipt updateTallyReceipt(TallyReceipt tal_rec) {
		return tally_rec_repo.save(tal_rec);
	}
	
	public void delete1(Integer tally_receipt_id) {
		tally_rec_repo.delete1(tally_receipt_id);
		}
	
	public void delete2(Integer tally_receipt_id) {
		tally_rec_repo.delete2(tally_receipt_id);
		}
}
