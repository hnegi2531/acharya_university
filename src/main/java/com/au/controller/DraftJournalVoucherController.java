package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.au.model.DraftJournalVoucher;
import com.au.response.ResponseHandler;
import com.au.service.DraftJournalVoucherService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class DraftJournalVoucherController {

	Logger log = LoggerFactory.getLogger(DraftJournalVoucherController.class);

	@Autowired
	private DraftJournalVoucherService djv_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/draftJournalVoucher")
	public ResponseEntity<Object> saveJournalVoucher(@RequestBody @Valid List<DraftJournalVoucher> djv,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			djv.stream().forEach(p -> {

				p.setCreated_by(jwtDetails.getUserId());
				p.setCreated_username(jwtDetails.getUserName());
			});
			List<DraftJournalVoucher> draftJournalVoucher = djv_Service.saveDraftJournalVoucher(djv);
			ResponseEntity<Object> draftJournalVoucher_response = ResponseHandler.generateResponse(true,
					HttpStatus.CREATED, draftJournalVoucher);
			return draftJournalVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/allActiveDraftJournalVoucher")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<DraftJournalVoucher> djv = djv_Service.listAll1();
			ResponseEntity<Object> draftJournalVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					djv);
			return draftJournalVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/draftJournalVoucher/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				DraftJournalVoucher djv = djv_Service.get(id);
				ResponseEntity<Object> draftJournalVoucher_response = ResponseHandler.generateResponse(true,
						HttpStatus.OK, djv);
				return draftJournalVoucher_response;
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

	@PutMapping("/updateDraftJournalVoucher/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<DraftJournalVoucher> djv,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				djv.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				djv_Service.updateDraftJournalVoucher(djv);
				ResponseEntity<Object> draftJournalVoucher_response = ResponseHandler
						.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return draftJournalVoucher_response;
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

	@DeleteMapping("/DeactiveDraftJournalVoucher/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			djv_Service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateDraftJournalVoucher/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			djv_Service.delete1(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllDraftJournalVoucher")
	public ResponseEntity<Object> getAllJournalVoucher(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> DraftPaymentVoucher_filtered = djv_Service.getAllDataFilteredByKeyword(pageable,
						keyword);
				return DraftPaymentVoucher_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> DraftPaymentVoucher_sorted = djv_Service.getAllSortedData(pageable1);
				return DraftPaymentVoucher_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getDraftJournalVoucherData/{journal_voucher_number}/{school_id}/{financial_year_id}")
	public ResponseEntity<Object> getDraftJournalVoucherData(@PathVariable Integer journal_voucher_number,
			@PathVariable Integer school_id, @PathVariable Integer financial_year_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> draft_journal_voucher = djv_Service
					.getDraftJournalVoucherData(journal_voucher_number, school_id, financial_year_id);
			ResponseEntity<Object> djv_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					draft_journal_voucher);
			return djv_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping(value = "/draftJournalVoucherUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute VendorFileRequest vendorFileRequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Message For Attachment");
		djv_Service.uploadFile(vendorFileRequest.getFile(), vendorFileRequest.getJournal_voucher_number());
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	@GetMapping(path = "/draftJournalVoucherFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = djv_Service.viewFiles(fileName);
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

	@DeleteMapping("/deleteDraftJournalVoucher/{journal_voucher_number}")
	public ResponseEntity<Object> deleteDraftPaymentVoucher(@PathVariable Integer journal_voucher_number) {
		if (RateLimitController.bucket.tryConsume(1)) {
			djv_Service.deleteDraftJournalVoucher(journal_voucher_number);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getdraftJournalVoucherAttachmentByVoucherNo/{journal_voucher_number}/{financial_year_id}")
	public ResponseEntity<?> getdraftJournalVoucherAttachmentByVoucherNo(@PathVariable Integer journal_voucher_number,
			@PathVariable Integer financial_year_id) {

		List<HashMap<String, Object>> obj = djv_Service
				.getdraftJournalVoucherAttachmentByVoucherNo(journal_voucher_number, financial_year_id);
		return new ResponseEntity<>(obj, HttpStatus.OK);

	}

}
