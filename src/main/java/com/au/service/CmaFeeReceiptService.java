package com.au.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CmaFeeReceipt;
import com.au.model.FinancialYear;
import com.au.model.PettyCash;
import com.au.model.ServiceTicketMaintenance;
import com.au.repository.CmaFeeReceiptRepository;
import com.au.repository.FinancialYearRepository;
import com.au.repository.PettyCashRepository;
import com.au.response.ResponseHandler;

@Service
public class CmaFeeReceiptService {

	@Autowired
	private CmaFeeReceiptRepository cmaFeeReceiptRepository;
	
	@Autowired
	private FinancialYearRepository financial_year_repo;
	
	public CmaFeeReceipt createCmaFeeReceipt(@Valid CmaFeeReceipt cmaFeeReceipt) throws Exception {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		   LocalDateTime now = LocalDateTime.now();
		   String date  = dtf.format(now);
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=null;
			try {
				date1 = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		FinancialYear f_year = financial_year_repo.getFinancialYearData(date1);
		try{
			f_year.equals(null);
		   }catch(NullPointerException e) {
				throw new NullPointerException("Finanacial Year Is Not Created Yet !!!");
			}
			Integer cma_receipt_id = cmaFeeReceiptRepository.getLatestData(cmaFeeReceipt.getSchool_id(),f_year.getFinancial_year_id(),cmaFeeReceipt.getReceipt_type());		
			CmaFeeReceipt iid = cmaFeeReceiptRepository.getLatestServiceTicketId(cmaFeeReceipt.getSchool_id(),f_year.getFinancial_year_id(),cmaFeeReceipt.getReceipt_type()); // id will come
			
			
			if(cma_receipt_id==null) {
				cmaFeeReceipt.setCma_receipt_id(1);
				cmaFeeReceipt.setFinancial_year_id(f_year.getFinancial_year_id());
			}else if(f_year.getFinancial_year_id() == iid.getFinancial_year_id() ) {   
				cmaFeeReceipt.setCma_receipt_id((cma_receipt_id+1));
				cmaFeeReceipt.setFinancial_year_id(f_year.getFinancial_year_id());
				
			}else {
				cmaFeeReceipt.setCma_receipt_id(1);
				cmaFeeReceipt.setFinancial_year_id(f_year.getFinancial_year_id());
				}
		return	cmaFeeReceiptRepository.save(cmaFeeReceipt);
		
	}
	
	
	public List<CmaFeeReceipt> createMultipleCmaFeeReceipt(@Valid List<CmaFeeReceipt> cmaFeeReceipt) throws Exception {
		
		List<CmaFeeReceipt> newCma = new ArrayList<CmaFeeReceipt>();
		
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		   LocalDateTime now = LocalDateTime.now();
		   String date  = dtf.format(now);
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=null;
			try {
				date1 = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		FinancialYear f_year = financial_year_repo.getFinancialYearData(date1);
		try{
			f_year.equals(null);
		   }catch(NullPointerException e) {
				throw new NullPointerException("Finanacial Year Is Not Created Yet !!!");
			}
			Integer cma_receipt_id = cmaFeeReceiptRepository.getLatestData(cmaFeeReceipt.get(0).getSchool_id(),f_year.getFinancial_year_id(),cmaFeeReceipt.get(0).getReceipt_type());		
			CmaFeeReceipt iid = cmaFeeReceiptRepository.getLatestServiceTicketId(cmaFeeReceipt.get(0).getSchool_id(),f_year.getFinancial_year_id(),cmaFeeReceipt.get(0).getReceipt_type()); // id will come
			
			cmaFeeReceipt.stream().forEach(cma -> {
				if(cma_receipt_id==null) {
					cma.setCma_receipt_id(1);
					cma.setFinancial_year_id(f_year.getFinancial_year_id());
				}else if(f_year.getFinancial_year_id() == iid.getFinancial_year_id() ) {   
					cma.setCma_receipt_id((cma_receipt_id+1));
					cma.setFinancial_year_id(f_year.getFinancial_year_id());
					
				}else {
					cma.setCma_receipt_id(1);
					cma.setFinancial_year_id(f_year.getFinancial_year_id());
					}
				
				newCma.add(cma);
			});
			
		return	cmaFeeReceiptRepository.saveAll(newCma);
		
	}
	public List<CmaFeeReceipt> getAllCmaFeeReceipt() {
		return cmaFeeReceiptRepository.getAllCmaFeeReceipt();
	}
	
	public CmaFeeReceipt get(Integer id) {
		return cmaFeeReceiptRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PettyCash Not Found:" + id));
	}
	
	public CmaFeeReceipt updateCmaFeeReceipt(CmaFeeReceipt cmaFeeReceipt) {
		return cmaFeeReceiptRepository.save(cmaFeeReceipt);
	}


	public void deactivateCmaFeeReceipt(Integer id) {
		CmaFeeReceipt dept = cmaFeeReceiptRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PettyCash Not Found:" + id));
		cmaFeeReceiptRepository.deactivateCmaFeeReceipt(id);
	}

	public void activateCmaFeeReceipt(Integer id) {
		CmaFeeReceipt dept = cmaFeeReceiptRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PettyCash Not Found:" + id));
		cmaFeeReceiptRepository.activateCmaFeeReceipt(id);
	}	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = cmaFeeReceiptRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = cmaFeeReceiptRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}


	public List<HashMap<String, Object>> getCmaFeeReceiptByReceiptId(Integer cma_receipt_id, Integer financial_year_id, Integer student_id) {
		return cmaFeeReceiptRepository.getCmaFeeReceiptByReceiptId(cma_receipt_id,  financial_year_id,  student_id);
	}	
	
	public List<HashMap<String, Object>> getUniformFeeReceiptByReceiptId(Integer uniform_receipt_no, Integer financial_year_id, Integer student_id) {
		Long fc_year_id = Long.valueOf(financial_year_id.longValue());
		Long studentId = Long.valueOf(student_id.longValue());
		return cmaFeeReceiptRepository.getUniformFeeReceiptByReceiptId(uniform_receipt_no,  fc_year_id,  studentId);
	}	

}
