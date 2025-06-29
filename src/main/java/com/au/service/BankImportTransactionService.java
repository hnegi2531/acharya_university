package com.au.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.BankImportTransactionDto;
import com.au.dto.BankImportUpdateDto;
import com.au.dto.CSVTransactionHelperDto;
import com.au.dto.EmployeeMedicalHistoryDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.BankImportTransaction;
import com.au.model.EmployeeDetails;
import com.au.repository.BankImportTransactionRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class BankImportTransactionService {

	@Autowired
	private BankImportTransactionRepository bit_repo;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private CSVTransactionHelperDto cvshelper;




	public BankImportTransaction saveBankImportTransaction(BankImportTransaction bit) throws Exception {
		return	bit_repo.save(bit);
		}


	public List<BankImportTransaction> listAll1() {
		return bit_repo.findAll11();
	}


	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer schoolId, LocalDate startDate, LocalDate endDate, LocalDate minDate, Integer bankId) {
		Date start = java.sql.Date.valueOf(startDate);
		Date end = java.sql.Date.valueOf(endDate);
		Date min = java.sql.Date.valueOf(minDate);
		Page<Object> bit_filtered_response = bit_repo.getAllDataFilteredByKeyword(pageable, keyword, schoolId, start, end, min, bankId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, bit_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable,Integer schoolId, LocalDate startDate, LocalDate endDate, LocalDate minDate,Integer bankId) {
		Date start = java.sql.Date.valueOf(startDate);
		Date end = java.sql.Date.valueOf(endDate);
		Date min = java.sql.Date.valueOf(minDate);
		Page<Object> bit_sorted_response = bit_repo.getAllSortedData(pageable,schoolId, start, end, min,bankId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, bit_sorted_response);
	}


	public BankImportTransaction get(Integer id) {
		return bit_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Import Transaction Not Found:" + id));
	}


//	public List<BankImportTransaction> saveBankImportTransaction1(List<BankImportTransaction> bit) {
//		return bit_repo.saveAll(bit);
//	}

	public BankImportTransaction saveBankImportTransaction1(BankImportTransaction bit) {
		return bit_repo.save(bit);
	}


	public void delete(Integer id) {
		BankImportTransaction bit = bit_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Import Transaction Not Found:" + id));
		bit_repo.delete1(id);
	}

	public void delete1(Integer id) {
		BankImportTransaction bit = bit_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Import Transaction Not Found:" + id));
		bit_repo.delete2(id);
	}

	public List<BankImportTransactionDto> getDataFromFile(MultipartFile file, BankImportTransactionDto bit, String jwtToken)
			throws Exception {

		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<BankImportTransactionDto> banktransaction= new ArrayList<BankImportTransactionDto>();
		List<BankImportTransactionDto> banktransaction1= new ArrayList<BankImportTransactionDto>();


	 if (CSVTransactionHelperDto.hasCSVFormat(file)) {
		  banktransaction = readFile(file,jwtDetails.getUserId(),jwtDetails.getUserName());

		  for(int i=bit.getStart_row()-2;i<=bit.getEnd_row()-2;i++) {
//			  BankImportTransactionDto bt = new BankImportTransactionDto();
//			  bt.setTransaction_date(banktransaction.get(i).getTransaction_date());
			  BankImportTransactionDto bt = banktransaction.get(i);
			  bt.setTransaction_type(bit.getTransaction_type());
			  bt.setDeposited_bank_id(bit.getDeposited_bank_id());
			  bt.setSchool_id(bit.getSchool_id());
			  bt.setActive(true);
			  banktransaction1.add(bt);

		  }
		}

	  return banktransaction1;

	}


	public List<BankImportTransactionDto> readFile(MultipartFile file, Integer user_id, String username) throws Exception {

		List<BankImportTransactionDto> bits = cvshelper.csvToTutorials(file.getInputStream(),user_id);
		bits.stream().forEach(b->{
			b.setCreated_by(user_id);
			b.setCreated_username(username);
		});
		return bits;

	}

	public List<BankImportTransactionDto> saveFile(MultipartFile file,String transaction_no, Integer user_id, String username) throws Exception {

		List<BankImportTransactionDto> bits = cvshelper.csvToTutorials(file.getInputStream(),user_id);
		bits.stream().forEach(b->{
			b.setTransaction_no(transaction_no);
			b.setCreated_by(user_id);
			b.setCreated_username(username);
		});
		return bits;

	}

	public List<BankImportTransaction> saveBankImportTransaction11(@Valid List<BankImportTransaction> bit) throws Exception {

		bit.stream().forEach(b -> {
			if(bit_repo.checkValidate(b.getDeposited_bank_id(), b.getCheque_dd_no(), b.getTransaction_date(), b.getTransaction_no(),
					b.getAmount()) >= 1) {
				throw new RuntimeException("Combination of above given input is already present. Please check!");
			}
		});
		return	 (List<BankImportTransaction>) bit_repo.saveAll(bit);
		}

	public List<Map<String, Object>> bankImportTransactionDetailsOnAmount(Double amount) {
		return bit_repo.bankImportTransactionDetailsOnAmount(amount);
	}

	public List<HashMap<String, Object>> bankImportTransaction(Integer deposited_bank_id) {
		return bit_repo.bankImportTransaction(deposited_bank_id);
	}

	public ResponseEntity<Object> getAllImportTransactionDetailsForClearedHistoryFilteredByKeyword(Pageable pageable, Object keyword, Integer schoolId,LocalDate startDate, LocalDate endDate, LocalDate minDate) {
		Date start = java.sql.Date.valueOf(startDate);
		Date end = java.sql.Date.valueOf(endDate);
		Date min = java.sql.Date.valueOf(minDate);
		Page<Object> bit_filtered_response = bit_repo.getAllImportTransactionDetailsForClearedHistoryFilteredByKeyword(pageable, keyword, schoolId, start, end, min);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, bit_filtered_response);
	}

	public ResponseEntity<Object> getAllImportTransactionDetailsForClearedHistorySortedData(Pageable pageable, Integer schoolId,LocalDate startDate, LocalDate endDate, LocalDate minDate) {
		Date start = java.sql.Date.valueOf(startDate);
		Date end = java.sql.Date.valueOf(endDate);
		Date min = java.sql.Date.valueOf(minDate);
		Page<Object> bit_sorted_response = bit_repo.getAllImportTransactionDetailsForClearedHistorySortedData(pageable,schoolId, start, end, min);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, bit_sorted_response);
	}

	public void deleteBankImportTransaction(Integer bank_import_transaction_id) {

	bit_repo.deleteBankImportTransaction(bank_import_transaction_id);
	}

	public ResponseEntity<Object> getAllInactiveDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> bit_filtered_response = bit_repo.getAllInactiveDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, bit_filtered_response);
	}

	public ResponseEntity<Object> getAllInactiveSortedData(Pageable pageable) {
		Page<Object> bit_sorted_response = bit_repo.getAllInactiveSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, bit_sorted_response);
	}


	public BankImportTransaction updateBankDetailsData(BankImportUpdateDto dto, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		BankImportTransaction bank = bit_repo.findById(dto.getBank_import_transaction_id())
		.orElseThrow(()-> new ResourceNotFoundException("BankImportTransaction not found"));
		bank.setTotal_usd(dto.getTotal_usd());
		bank.setExachange_rate(dto.getExachange_rate());
		bank.setModified_by(jwtDetails.getUserId());
		bank.setModified_username(jwtDetails.getUserName());
		return bit_repo.save(bank);
	}

	public List<HashMap<String, Object>> bankImportTransactionWithVoucherName(Integer bank_import_transaction_id) {
		return bit_repo.bankImportTransactionWithVoucherName(bank_import_transaction_id);
	}
}
