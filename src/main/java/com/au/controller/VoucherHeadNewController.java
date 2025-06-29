package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.JwtDetails;
import com.au.model.VendorOpeningBalance;
import com.au.model.VoucherHeadNew;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.VendorService;
import com.au.service.VoucherHeadNewService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class VoucherHeadNewController {

	@Autowired
	private VoucherHeadNewService voucherHeadNewService;

	Logger log = LoggerFactory.getLogger(VoucherHeadNewController.class);

	@Autowired
	private JwtTokenService jwtTokenService;
	
	

	@PostMapping("/VoucherHeadNew")
	public ResponseEntity<Object> saveVoucherHeadNew(@RequestBody @Valid VoucherHeadNew voucherHeadNew,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException, Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			voucherHeadNew.setCreated_by(jwtDetails.getUserId());
			voucherHeadNew.setCreated_username(jwtDetails.getUserName());
			VoucherHeadNew vhn = voucherHeadNewService.saveVoucherHeadNew(voucherHeadNew);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, vhn);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/VoucherHeadNew")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<VoucherHeadNew> voucherHeadNewList = voucherHeadNewService.listAll();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, voucherHeadNewList);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/fetchAllVoucherHeadNewDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				return voucherHeadNewService.listAll1(pageable, keyword);
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				return voucherHeadNewService.listAll2(pageable1);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/VoucherHeadNew/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				VoucherHeadNew product = voucherHeadNewService.get(id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@PutMapping("/VoucherHeadNew/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid VoucherHeadNew voucherHeadNew, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				voucherHeadNewService.get(id);
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				voucherHeadNew.setModified_by(jwtDetails.getUserId());
				voucherHeadNew.setModified_username(jwtDetails.getUserName());
				voucherHeadNewService.saveVoucherHeadNew1(voucherHeadNew);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/VoucherHeadNew/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			voucherHeadNewService.delete(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/activateVoucherHeadNew/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			voucherHeadNewService.delete1(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/allUnassignedSchoolDetails/{voucher_head_new_id}")
	public ResponseEntity<Object> fetchUnassignedSchoolDetails(@RequestBody @PathVariable Integer voucher_head_new_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> uncheckedSchool = voucherHeadNewService
					.fetchUnassignedSchoolDetails(voucher_head_new_id);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, uncheckedSchool);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/VoucherHeadNewDetailsOnIsSalaries")
	public ResponseEntity<Object> VoucherHeadNewDetailsOnIsSalaries() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<VoucherHeadNew> listOfVoucherHeadNew = voucherHeadNewService.VoucherHeadNewDetailsOnIsSalaries();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listOfVoucherHeadNew);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/VoucherHeadNewDetailsOnJournal")
	public ResponseEntity<Object> VoucherHeadNewDetailsOnJournal() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<VoucherHeadNew> listOfVoucherHeadNew = voucherHeadNewService.VoucherHeadNewDetailsOnJournal();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listOfVoucherHeadNew);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/VoucherHeadNewDetailsWoJournal")
	public ResponseEntity<Object> VoucherHeadNewDetailsWoJournal() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<VoucherHeadNew> listOfVoucherHeadNew = voucherHeadNewService.VoucherHeadNewDetailsWoJournal();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listOfVoucherHeadNew);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/getVoucherHeadNewData")
	public ResponseEntity<Object> getVoucherHeadNewData() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> listOfVoucherHeadNew = voucherHeadNewService.getVoucherHeadNewData();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listOfVoucherHeadNew);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getVoucherHeadNewDataOutflow")
	public ResponseEntity<Object> getVoucherHeadNewDataOutflow() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> listOfVoucherHeadNew = voucherHeadNewService.getVoucherHeadNewDataOutflow();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listOfVoucherHeadNew);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/fetchVoucherHeadNewDetailsBasedOnCashOrBank")
	public ResponseEntity<Object> fetchVoucherHeadNewDetailsBasedOnCashOrBank() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<VoucherHeadNew> voucherHeadNewList = voucherHeadNewService.fetchVoucherHeadNewDetailsBasedOnCashOrBank();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, voucherHeadNewList);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/voucherHeadDetailsOnHostelStatus")
	public ResponseEntity<Object> voucherHeadDetailsOnHostelStatus() {
		if(RateLimitController.bucket.tryConsume(1)) {
					List<VoucherHeadNew> list_voucher_head_new = voucherHeadNewService.voucherHeadDetailsOnHostelStatus();
					ResponseEntity<Object> list_voucher_head_new_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_voucher_head_new);
					return list_voucher_head_new_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}
	
	
	@GetMapping("/fetchInFlowVoucherHeadIds/{school_id}")
	public ResponseEntity<Object> fetchVoucherHeadIds(@PathVariable Integer school_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> list_of_voucher_head = voucherHeadNewService.fetchVoucherHeadIds(school_id);
				ResponseEntity<Object> list_of_voucher_head_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_of_voucher_head );
				return list_of_voucher_head_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
}
