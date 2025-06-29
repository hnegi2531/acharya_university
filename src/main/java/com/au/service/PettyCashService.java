package com.au.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.PettyCash;
import com.au.repository.PettyCashRepository;
import com.au.response.ResponseHandler;

@Service
public class PettyCashService {

    @Autowired
    private PettyCashRepository pettyCashRepository;


    public PettyCash createPettyCash(@Valid PettyCash pc) throws Exception {

        Date now = new Date();
        pc.setCreated_date(now);
        pc.setActive(true);
        if (pettyCashRepository.getCount(pc.getSchool_id(), pc.getCreated_date()) >= 1)
            throw new Exception("Today already paid for this school !!");
        else {
            return pettyCashRepository.save(pc);
        }
    }

    public List<PettyCash> getAllPettyCash() {
        return pettyCashRepository.getAllPettyCash();
    }

    public PettyCash get(Integer id) {
        return pettyCashRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PettyCash Not Found:" + id));
    }


    public PettyCash updatePettyCash(PettyCash pettyCash, Integer id) {
		Optional<PettyCash> optionalPettyCash = pettyCashRepository.findById(id);
		if(!optionalPettyCash.isPresent())
			throw new RuntimeException("No Petty Cash available for id: " + id);
        optionalPettyCash.get().setAmount(pettyCash.getAmount());
        optionalPettyCash.get().setRemark(pettyCash.getRemark());
        optionalPettyCash.get().setModified_by(pettyCash.getModified_by());
        optionalPettyCash.get().setModified_username(pettyCash.getModified_username());
        return pettyCashRepository.save(optionalPettyCash.get());
    }


    public void deactivate(Integer id) {
        PettyCash dept = pettyCashRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PettyCash Not Found:" + id));
        pettyCashRepository.deactivate(id);
    }

    public void activate(Integer id) {
        PettyCash dept = pettyCashRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PettyCash Not Found:" + id));
        pettyCashRepository.activate(id);
    }


    public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
        Page<Object> oc_filtered_response = pettyCashRepository.getAllDataFilteredByKeyword(pageable, keyword);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
    }

    public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
        Page<Object> oc_sorted_response = pettyCashRepository.getAllSortedData(pageable);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
    }

    public List<Map<String, Object>> getPettyCashDetailsBasedOnSchoolId(Integer school_id) {
        return pettyCashRepository.getPettyCashDetailsBasedOnSchoolId(school_id);
    }
}
