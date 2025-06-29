package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.TallyHead;
import com.au.repository.TallyHeadRepository;
import com.au.response.ResponseHandler;

@Service
public class TallyHeadService {

	@Autowired
	private TallyHeadRepository tally_repo;

	public List<TallyHead> listAll() {
		return tally_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return tally_repo.findAll2();
		Page<Object> response1 = tally_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return tally_repo.findAll3();
		Page<Object> response = tally_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public TallyHead save_TallyHead(TallyHead tallyHead) throws Exception {
		if(tally_repo.getTallyFeehead(tallyHead.getTally_fee_head()) >= 1) {
			throw new Exception("Tally Fee Head Already Exists");
		} else {
			return tally_repo.save(tallyHead);
		}
	}

//	public TallyHead save_TallyHead(TallyHead tallyHead) throws Exception {
//		try {
//			tally_repo.save(tallyHead);
//		} catch (Exception e) {
//			if (e.getMessage().contains("tally_fee_head_UNIQUE")) {
//				throw new Exception("Tally Head Name already present");
//			}
//		}
//		return tallyHead;
//	}

	public TallyHead get(Integer id) {
		return tally_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("TallyHead Not Found:" + id));
	}

	public void delete(Integer id) {
		TallyHead ay = tally_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TallyHead Not Found:" + id));
		tally_repo.update(id);
	}

	public void delete1(Integer id) {
		TallyHead ay = tally_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TallyHead Not Found:" + id));
		tally_repo.update1(id);
	}

	public TallyHead save_TallyHead1(TallyHead tally) {
		return tally_repo.save(tally);
		
	}
}
