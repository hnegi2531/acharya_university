package com.au.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import com.au.scheduler.BankImportScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.BankImportTransactionDto;
import com.au.dto.BankImportUpdateDto;
import com.au.dto.JwtDetails;
import com.au.dto.TransactionSettlementDTO;
import com.au.model.BankImportTransaction;
import com.au.response.ResponseHandler;
import com.au.service.BankImportTransactionService;
import com.au.service.JwtTokenService;
import com.au.service.TriggerService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class BankImportTransactionController {

	Logger log = LoggerFactory.getLogger(BankImportTransactionController.class);
	
	@Autowired
	private BankImportTransactionService bit_Service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private TriggerService triggerService;


	
	@PostMapping("/bankImportTransaction")
	public ResponseEntity<Object> saveBankImportTransaction(@RequestBody @Valid BankImportTransaction bit,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		bit.setCreated_by(jwtDetails.getUserId());
		bit.setCreated_username(jwtDetails.getUserName());
		BankImportTransaction bits = bit_Service.saveBankImportTransaction(bit);
		ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, bits);
		return bit_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/bankImportTransaction")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<BankImportTransaction> bit = bit_Service.listAll1();
		ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);
		return bit_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/fetchAllbankImportTransactionDetail")
	public ResponseEntity<Object> getAllBankImportTransaction(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,
            @RequestParam(value = "school_id", required = false) Integer school_id,
            @RequestParam(value = "date_range", required = false) String dateRange,
            @RequestParam(value = "start_date", required = false) String startDate,
		    @RequestParam(value = "end_date", required = false) String endDate,
	        @RequestParam(value = "bank_id", required = false) Integer bankId) {
		
		if(RateLimitController.bucket.tryConsume(1)) {

			Sort sorted = Sort.by(Direction.DESC, sort);

			// Create a filter for date range
			LocalDate start = null;
			LocalDate end = null;
			LocalDate minDate = LocalDate.of(2025, 3, 17);

			if (dateRange != null) {
				switch (dateRange) {
					case "week":
						start = LocalDate.now().minusWeeks(1);
						if (start.isBefore(minDate)) {
							start = minDate;
						}

						end = LocalDate.now();
						break;
					case "month":
						start = LocalDate.now().minusMonths(1);
						if (start.isBefore(minDate)) {
							start = minDate;
						}
						end = LocalDate.now();
						break;
					case "custom":
						if (startDate != null && endDate != null) {
							// Convert the input dates into LocalDate
							try {
								start = LocalDate.parse(startDate);
								if (start.isBefore(minDate)) {
									start = minDate;
								}
								end = LocalDate.parse(endDate);
								System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +start);
								System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " +end);
							} catch (DateTimeParseException e) {
								return ResponseEntity.badRequest().body("Invalid date format. Ensure the date format is 'yyyy-MM-dd'.");
							}
						}
						break;

					case "today": // Handle the "today" range
						start = LocalDate.now();
						end = LocalDate.now();
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + start);
						System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + end);
						break;
				}

			}

		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> bit_filtered =  bit_Service.getAllDataFilteredByKeyword(pageable, keyword,school_id, start, end, minDate,bankId);//,column,value);
			return bit_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> bit_sorted = bit_Service.getAllSortedData(pageable1,school_id, start, end, minDate,bankId);
			return bit_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/bankImportTransaction/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	BankImportTransaction bit = bit_Service.get(id);
	    	ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);
			return bit_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/bankImportTransaction/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid BankImportTransaction bit, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	bit.setModified_by(jwtDetails.getUserId());
	    	bit.setModified_username(jwtDetails.getUserName());
	    	bit_Service.saveBankImportTransaction1(bit);
	    	ResponseEntity<Object> bit_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return bit_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/bankImportTransaction/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			bit_Service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateBankImportTransaction/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			bit_Service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PostMapping("/bankImportTransactionCSV")
	public ResponseEntity<Object> postDataFromFile(@ModelAttribute BankImportTransactionDto bit,
			@RequestHeader("Authorization") String jwtToken)throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			bit.setCreated_by(jwtDetails.getUserId());
			bit.setCreated_username(jwtDetails.getUserName());
		
			List<BankImportTransactionDto> list = bit_Service.getDataFromFile(bit.getFile(), bit,jwtToken);
			ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.CREATED, list);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PostMapping("/saveBankImportTransaction")
	public ResponseEntity<Object> saveBankImportTransaction1(@RequestBody @Valid List<BankImportTransaction> bit,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			bit.stream().forEach(b -> {

				b.setCreated_by(jwtDetails.getUserId());
				b.setCreated_username(jwtDetails.getUserName());
			});

			List<BankImportTransaction> bits = bit_Service.saveBankImportTransaction11(bit);
			ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, bits);
			return bit_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/bankImportTransactionDetailsOnAmount/{amount}")
	public ResponseEntity<Object> bankImportTransactionDetailsOnAmount(@PathVariable Double amount) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>>  bit = bit_Service.bankImportTransactionDetailsOnAmount(amount);
			ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);

			return bit_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/bankImportTransactionForBankName/{deposited_bank_id}")
	public ResponseEntity<Object> bankImportTransaction(@PathVariable Integer deposited_bank_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> bit = bit_Service.bankImportTransaction(deposited_bank_id);
			ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);

			return bit_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/fetchAllbankImportTransactionDetailsForClearedHistory")
	public ResponseEntity<Object> fetchAllbankImportTransactionDetailsForClearedHistory(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,
			@RequestParam(value = "school_id", required = false) Integer school_id,
			@RequestParam(value = "date_range", required = false) String dateRange,
			@RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate){

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			// Create a filter for date range
			LocalDate start = null;
			LocalDate end = null;
			LocalDate minDate = LocalDate.of(2025, 3, 17);

			if (dateRange != null) {
				switch (dateRange) {
					case "week":
						start = LocalDate.now().minusWeeks(1);
						if (start.isBefore(minDate)) {
							start = minDate;
						}

						end = LocalDate.now();
						break;
					case "month":
						start = LocalDate.now().minusMonths(1);
						if (start.isBefore(minDate)) {
							start = minDate;
						}
						end = LocalDate.now();
						break;
					case "custom":
						if (startDate != null && endDate != null) {
							// Convert the input dates into LocalDate
							try {
								start = LocalDate.parse(startDate);
								if (start.isBefore(minDate)) {
									start = minDate;
								}
								end = LocalDate.parse(endDate);
								System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + start);
								System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + end);
							} catch (DateTimeParseException e) {
								return ResponseEntity.badRequest().body("Invalid date format. Ensure the date format is 'yyyy-MM-dd'.");
							}
						}
						break;

					case "today": // Handle the "today" range
						start = LocalDate.now();
						end = LocalDate.now();
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + start);
						System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + end);
						break;
				}
			}

			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> bit_filtered = bit_Service.getAllImportTransactionDetailsForClearedHistoryFilteredByKeyword(pageable, keyword, school_id, start, end, minDate);//,column,value);
				return bit_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> bit_sorted = bit_Service.getAllImportTransactionDetailsForClearedHistorySortedData(pageable1, school_id, start, end, minDate);
				return bit_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@DeleteMapping("/deleteBankImportTransaction/{bank_import_transaction_id}")
	public ResponseEntity<Object> deleteBankImportTransaction(@PathVariable Integer bank_import_transaction_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			bit_Service.deleteBankImportTransaction(bank_import_transaction_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/fetchAllInactivebankImportTransactionDetail")
	public ResponseEntity<Object> fetchAllInactivebankImportTransactionDetail(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> bit_filtered =  bit_Service.getAllInactiveDataFilteredByKeyword(pageable, keyword);//,column,value);
			return bit_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> bit_sorted = bit_Service.getAllInactiveSortedData(pageable1);
			return bit_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	

	@PutMapping("/updateBankDetailsData/{bank_import_transaction_id}")
	public ResponseEntity<Object> updateBankDetailsData(@RequestBody BankImportUpdateDto dto,@PathVariable Integer bank_import_transaction_id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			bit_Service.updateBankDetailsData(dto, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	
	@GetMapping("/bankImportTransactionWithVoucherName/{bank_import_transaction_id}")
	public ResponseEntity<Object> bankImportTransactionWithVoucherName(@PathVariable Integer bank_import_transaction_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> bit = bit_Service.bankImportTransactionWithVoucherName(bank_import_transaction_id);
			ResponseEntity<Object> bit_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bit);

			return bit_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	

	
	@PostMapping("/bankImportSettlement")
	public ResponseEntity<Object> bankImportSettlement(@RequestBody TransactionSettlementDTO transactionSettlementDTO) {
	    triggerService.bankImportSettlement(transactionSettlementDTO);
		return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Settlement Processing started");
	
	}
	
	@PostMapping("/bankImportSettlementTrigger")
	public ResponseEntity<Object> bankImportSettlementTrigger(@RequestParam(value="year", required = false) Integer year,@RequestParam(value="month", required = false) Integer month,@RequestParam(value="day", required = false) Integer day,@RequestParam(value="schoolId", required = false) Integer schoolId) {
	    triggerService.bankImportSettlementTrigger(year,month,day,schoolId);
		return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Settlement Processing started");
	
	}

	@PostMapping("/bankImportSettlementTriggerForAll")
	public ResponseEntity<Object> bankImportSettlementTrigger(@RequestParam(value="year", required = false) Integer year,@RequestParam(value="month", required = false) Integer month,@RequestParam(value="day", required = false) Integer day) {
		triggerService.bankImportSettlementTriggerForAll(year,month,day);
		return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Settlement Processing started");

	}
}
