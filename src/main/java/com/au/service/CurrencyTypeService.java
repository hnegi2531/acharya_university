package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Currency_Type;
import com.au.repository.CurrencyTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class CurrencyTypeService {

	@Autowired
	private CurrencyTypeRepository ct_repo;

	public List<Currency_Type> listAll() {
		return ct_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return ct_repo.findAll();
		Page<Object> response1 = ct_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return ct_repo.findAll();
		Page<Object> response = ct_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public Currency_Type saveCurrency_Type(Currency_Type currency) throws Exception {
		if(ct_repo.countOfCurrencyTypeName(currency.getCurrency_type_name())>=1) {
			throw new Exception(" Currency Name Already exist");
		}else if(ct_repo.countOfCurrencyTypeShortName(currency.getCurrency_type_short_name())>=1) {
			throw new Exception("Short Name Already exist");
		}else {
			return ct_repo.save(currency);
		}
		
	}

	public Currency_Type get(Integer id) {
		return ct_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency_Type Found:" + id));
	}

	public void delete(Integer id) {
		Currency_Type ay = ct_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Currency_Type Found:" + id));
		ct_repo.update(id);
	}

	public void delete1(Integer id) {
		Currency_Type ay = ct_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Currency_Type Found:" + id));
		ct_repo.update1(id);
	}

	public Currency_Type saveCurrency_Type1(Currency_Type c) {
		return ct_repo.save(c);

	}
}
