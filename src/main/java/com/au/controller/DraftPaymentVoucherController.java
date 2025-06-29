package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.au.dto.JwtDetails;
import com.au.dto.VendorFileRequest;
import com.au.model.DraftPaymentVoucher;
import com.au.response.ResponseHandler;
import com.au.service.DraftPaymentVoucherService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class DraftPaymentVoucherController {

	Logger log = LoggerFactory.getLogger(DraftPaymentVoucherController.class);
	@Autowired
	private DraftPaymentVoucherService pvd_Service;
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/draftPaymentVoucher")
	public ResponseEntity<Object> saveDraftPaymentVoucher(@RequestBody @Valid List<DraftPaymentVoucher> pv,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			pv.stream().forEach(p -> {

				p.setCreated_by(jwtDetails.getUserId());
				p.setCreated_username(jwtDetails.getUserName());
			});
			List<DraftPaymentVoucher> draftPaymentVoucher = pvd_Service.saveDraftPaymentVoucher(pv);
			ResponseEntity<Object> draftPaymentVoucher_response = ResponseHandler.generateResponse(true,
					HttpStatus.CREATED, draftPaymentVoucher);
			return draftPaymentVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/allActiveDraftPaymentVoucher")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<DraftPaymentVoucher> pv = pvd_Service.listAll1();
			ResponseEntity<Object> draftPaymentVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					pv);
			return draftPaymentVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/draftpaymentVoucher/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				DraftPaymentVoucher pv = pvd_Service.get(id);
				ResponseEntity<Object> draftPaymentVoucher_response = ResponseHandler.generateResponse(true,
						HttpStatus.OK, pv);
				return draftPaymentVoucher_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllDraftPaymentVoucher")
	public ResponseEntity<Object> getAllExamDetails(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> PaymentVoucher_filtered = pvd_Service.getAllDataFilteredByKeyword(pageable,
						keyword);
				return PaymentVoucher_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> PaymentVoucher_sorted = pvd_Service.getAllSortedData(pageable1);
				return PaymentVoucher_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllDraftPaymentVoucherStatus")
	public ResponseEntity<Object> fetchAllDraftPaymentVoucherStatus(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "verifier_id", required = false) Integer verifier_id,
			@RequestParam(value = "verified_status", required = false) Integer verified_status,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> PaymentVoucher_filtered = pvd_Service.getAllDataFilteredByKeywordStatus(pageable,verifier_id,verified_status,
						keyword);
				return PaymentVoucher_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> PaymentVoucher_sorted = pvd_Service.getAllSortedDataStatus(pageable1,verifier_id,verified_status);
				return PaymentVoucher_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateDraftPaymentVoucher/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<DraftPaymentVoucher> pv,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				pv.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				pvd_Service.updateDraftPaymentVoucher(pv);
				ResponseEntity<Object> draftPaymentVoucher_response = ResponseHandler
						.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return draftPaymentVoucher_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/DraftPaymentVoucher/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			pvd_Service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateDraftPaymentVoucher/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			pvd_Service.delete1(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getDraftPaymentVoucherData/{voucher_no}/{school_id}/{financial_year_id}")
	public ResponseEntity<Object> getDraftPaymentVoucherData(@PathVariable Integer voucher_no,
			@PathVariable Integer school_id, @PathVariable Integer financial_year_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> program = pvd_Service.getDraftPaymentVoucherData(voucher_no, school_id,
					financial_year_id);
			ResponseEntity<Object> program_response = ResponseHandler.generateResponse(true, HttpStatus.OK, program);
			return program_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping(value = "/draftPaymentVoucherUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute VendorFileRequest vendorFileRequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Message For Attachment");
		pvd_Service.uploadFile(vendorFileRequest.getFile(), vendorFileRequest.getVoucher_no());
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	@GetMapping(path = "/draftPaymentVoucherFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = pvd_Service.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}

	}

	@DeleteMapping("/deleteDraftPaymentVoucher/{voucher_no}")
	public ResponseEntity<Object> deleteDraftPaymentVoucher(@PathVariable Integer voucher_no) {
		if (RateLimitController.bucket.tryConsume(1)) {
			pvd_Service.deleteDraftPaymentVoucher(voucher_no);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/deactiveDraftPaymentVoucher/{voucher_no}/{financial_year_id}")
	public ResponseEntity<Object> deactiveDraftPaymentVoucher(@PathVariable Integer voucher_no,@PathVariable Integer financial_year_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			pvd_Service.deactiveDraftPaymentVoucher(voucher_no,financial_year_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
}
