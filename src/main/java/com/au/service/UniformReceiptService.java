package com.au.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.xspec.L;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.JwtDetails;
import com.au.model.CmaFeeReceipt;
import com.au.model.FinancialYear;
import com.au.model.UniformReceipt;
import com.au.repository.FinancialYearRepository;
import com.au.repository.UniformReceiptRepository;
import com.au.response.ResponseHandler;

@Service
public class UniformReceiptService {
	
	@Autowired
	private UniformReceiptRepository uniformReceiptRepository;
	
	@Autowired
	private FinancialYearRepository financial_year_repo;
	
	@Autowired
	private JwtTokenService jwt_service;

	private Logger logger = LoggerFactory.getLogger(UniformReceiptService.class);

	public UniformReceipt createUniformReceipt(UniformReceipt pr, JwtDetails jwtDetails) throws Exception {	
		return	uniformReceiptRepository.save(pr);

	}
	
	public List<UniformReceipt> createMultipleUniformReceipt(List<UniformReceipt> uniform, JwtDetails jwtDetails) throws Exception {	
		
		List<UniformReceipt> newCma = new ArrayList<UniformReceipt>();
		
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
		System.out.println("111111111111111111111 "+f_year.getFinancial_year_id());
		try{
			f_year.equals(null);
		   }catch(NullPointerException e) {
				throw new NullPointerException("Finanacial Year Is Not Created Yet !!!");
			}
			Integer uniform_receipt_no = uniformReceiptRepository.getLatestData(uniform.get(0).getSchoolId(),f_year.getFinancial_year_id(),uniform.get(0).getType());		
			UniformReceipt iid = uniformReceiptRepository.getLatestServiceTicketId(uniform.get(0).getSchoolId(),f_year.getFinancial_year_id(),uniform.get(0).getType()); // id will come
			
			uniform.stream().forEach(uni -> {
				if(uniform_receipt_no==null) {
					uni.setUniformReceiptNo(1);
					uni.setFcYearId(Long.valueOf(f_year.getFinancial_year_id().longValue()));
				}else if(Long.valueOf(f_year.getFinancial_year_id().longValue()) == iid.getFcYearId() ) {   
					uni.setUniformReceiptNo((uniform_receipt_no+1));
					uni.setFcYearId(Long.valueOf(f_year.getFinancial_year_id().longValue()));
					
				}else {
					uni.setUniformReceiptNo(1);
					uni.setFcYearId(Long.valueOf(f_year.getFinancial_year_id().longValue()));
					}
				
				newCma.add(uni);
			});
			
		return	uniformReceiptRepository.saveAll(newCma);

	}

	public List<UniformReceipt> listAll() {
		return uniformReceiptRepository.findAll1();
	}

	public UniformReceipt get(Integer uniformReceiptId) {
		return uniformReceiptRepository.findById(uniformReceiptId)
				.orElseThrow(() -> new ResourceNotFoundException("UniformReceipt Not Found:" + uniformReceiptId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> uniformReceipt_filtered_response = uniformReceiptRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, uniformReceipt_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> uniformReceipt_response = uniformReceiptRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, uniformReceipt_response);
	}

	public UniformReceipt updateUniformReceipt(UniformReceipt pr) {
		return	uniformReceiptRepository.save(pr);
	    
	}

	public void delete(Integer uniformReceiptId) {
		UniformReceipt sir = uniformReceiptRepository.findById(uniformReceiptId)
				.orElseThrow(() -> new ResourceNotFoundException("UniformReceipt Not Found:" + uniformReceiptId));
		uniformReceiptRepository.updateUniformReceipt(uniformReceiptId);
	}

	public void delete1(Integer uniformReceiptId) {
		UniformReceipt sir = uniformReceiptRepository.findById(uniformReceiptId)
				.orElseThrow(() -> new ResourceNotFoundException("UniformReceipt Not Found:" + uniformReceiptId));
		uniformReceiptRepository.updateUniformReceipt1(uniformReceiptId);
	}

	public ResponseEntity<Object> getUniformTransactions(Integer fcYearId, Integer month) {
		List<Map<String, Object>> uniformTransactions;
		List<Map<String, Object>> mutableMaps = Collections.emptyList();
		try {
			uniformTransactions = uniformReceiptRepository.fetchUniformReceipts(fcYearId, month);
			mutableMaps = uniformTransactions.stream().map(LinkedHashMap::new).collect(Collectors.toList());

			for(int i = 0; i < mutableMaps.size(); i++){

				Double previousBalance = 0.0;
				if(i != 0){
					previousBalance = (Double) mutableMaps.get(i - 1).get("balance");
				}
				Double amount = (Double) mutableMaps.get(i).get("amount");
				Number bankNum = (Number) mutableMaps.get(i).get("bankAmount");
				Float bankAmount = bankNum.floatValue();
				Number adjustmentNum = (Number) mutableMaps.get(i).get("adjustment");
				Float adjustment = adjustmentNum.floatValue();
				Double balance = amount - (bankAmount + adjustment);
				mutableMaps.get(i).put("balance", balance + previousBalance);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", mutableMaps);
	}

	public ResponseEntity<Object> getDateWiseUniformTransactions(String date) {
		List<Map<String, Object>> uniformTransactions = uniformReceiptRepository.getDateWiseUniformTransactions(date);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", uniformTransactions);
	}
}
