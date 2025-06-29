package com.au.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Ledger;
import com.au.repository.LedgerRepository;
import com.au.response.ResponseHandler;

@Service
public class LedgerService {

	@Autowired
	private LedgerRepository lr;

//	public List<HashMap<String, Object>> listAll() {
//		return lr.findAll1();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return lr.findAll1();
		Page<Object> response1 = lr.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return lr.findAll1();
		Page<Object> response = lr.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<Ledger> listAll1() {
		return lr.findAll11();
	}
	
	public Ledger save_Ledger(Ledger ledger) throws Exception {
		if(lr.getCountLedgerName(ledger.getLedger_name()) >= 1) {
			throw new Exception("Ledger Name Already Exist");
		} else if(lr.getCountLedgerShortName(ledger.getLedger_short_name()) >= 1) {
			throw new Exception("Short Name Already Exist");
		} else {
			return lr.save(ledger);
		}
	}

//	public Ledger save_Ledger(Ledger ledger) throws Exception {
//		try {
//			lr.save(ledger);
//		} catch (Exception e) {
//			if (e.getMessage().contains("ledger_name_UNIQUE")) {
//				throw new Exception("Ledger Name already present");
//			} else if (e.getMessage().contains("ledger_short_name_UNIQUE")) {
//				throw new Exception("Ledger short name already present");
//			} else {
//				e.printStackTrace();
//			}
//		}
//		return ledger;
//	}

	public Ledger get(Integer id) {
		return lr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ledger id Not Found:" + id));
	}

	public void delete(Integer id) {
		Ledger ay = lr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ledger id  Not Found:" + id));
		lr.update(id);
	}

	public void delete1(Integer id) {
		Ledger ay = lr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ledger id  Not Found:" + id));
		lr.update1(id);
	}

	public List<Ledger> getLedgerByGroupId(Integer grp_id) {
		// TODO Auto-generated method stub
		return lr.getLedgerByGroupId(grp_id);
	}

	public Integer CountLedgerByGroupId(Integer grp_id) {
		return lr.CountLedgerByGroupId(grp_id);
	}

	public Ledger save_Ledger1(Ledger l) {
		return lr.save(l);
	}

}
