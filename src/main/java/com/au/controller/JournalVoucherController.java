package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

import com.au.dto.JournalVoucherDto;
import com.au.dto.JwtDetails;
import com.au.model.JournalVoucher;
import com.au.response.ResponseHandler;
import com.au.service.JournalVoucherService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class JournalVoucherController {

	Logger log = LoggerFactory.getLogger(JournalVoucherController.class);

	@Autowired
	private JournalVoucherService jv_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/journalVoucher")
	public ResponseEntity<Object> saveJournalVoucher(@RequestBody @Valid List<JournalVoucher> jv,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			jv.stream().forEach(p -> {

				p.setCreated_by(jwtDetails.getUserId());
				p.setCreated_username(jwtDetails.getUserName());
			});

			List<JournalVoucher> journalVoucher = jv_Service.saveJournalVoucher(jv);
			ResponseEntity<Object> journalVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					journalVoucher);
			return journalVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/allActiveJournalVoucher")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<JournalVoucher> jv = jv_Service.listAll1();
			ResponseEntity<Object> journalVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK, jv);
			return journalVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/journalVoucher/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				JournalVoucher jv = jv_Service.get(id);
				ResponseEntity<Object> journalVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						jv);
				return journalVoucher_response;
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

	@PutMapping("/updateJournalVoucher/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<JournalVoucher> jv, @PathVariable List<Integer> id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				jv.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				jv_Service.updateJournalVoucher(jv);
				ResponseEntity<Object> journalVoucher_response = ResponseHandler
						.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return journalVoucher_response;
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

	@DeleteMapping("/DeactiveJournalVoucher/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			jv_Service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateJournalVoucher/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			jv_Service.delete1(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllJournalVoucher")
	public ResponseEntity<Object> getAllJournalVoucher(
	        @RequestParam(value = "page") Integer page,
	        @RequestParam(value = "page_size") Integer page_size,
	        @RequestParam(value = "sort") String sort,
	        @RequestParam(value = "keyword", required = false) Object keyword,
	        @RequestParam(value = "date_range", required = false) String dateRange,
	        @RequestParam(value = "start_date", required = false) String startDate,
	        @RequestParam(value = "end_date", required = false) String endDate) {

	    Sort sorted = Sort.by(Direction.DESC, sort);
	    Pageable pageable = PageRequest.of(page, page_size, sorted);

	    LocalDate start = null;
	    LocalDate end = null;
	    LocalDate minDate = jv_Service.getMinimumJournalVoucherDate(); // dynamic minDate

	    if (dateRange != null) {
	        switch (dateRange) {
	            case "week":
	                start = LocalDate.now().minusWeeks(1);
	                if (start.isBefore(minDate)) start = minDate;
	                end = LocalDate.now();
	                break;
	            case "month":
	                start = LocalDate.now().minusMonths(1);
	                if (start.isBefore(minDate)) start = minDate;
	                end = LocalDate.now();
	                break;
	            case "custom":
	                try {
	                    if (startDate != null && endDate != null) {
	                        start = LocalDate.parse(startDate);
	                        if (start.isBefore(minDate)) start = minDate;
	                        end = LocalDate.parse(endDate);
	                    }
	                } catch (DateTimeParseException e) {
	                    return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd");
	                }
	                break;
	            case "today":
	                start = LocalDate.now();
	                end = LocalDate.now();
	                break;
	        }
	    }

	    return (keyword != null)
	            ? jv_Service.getAllDataFilteredByKeyword(pageable, keyword, start, end, minDate)
	            : jv_Service.getAllSortedData(pageable, start, end, minDate);
	}


//		if (RateLimitController.bucket.tryConsume(1)) {
//			Sort sorted = Sort.by(Direction.DESC, sort);
//			if (keyword != null) {
//				Pageable pageable = PageRequest.of(page, page_size, sorted);
//				System.out.println("page, page_size, sorted, keyword");
//				ResponseEntity<Object> PaymentVoucher_filtered = jv_Service.getAllDataFilteredByKeyword(pageable,
//						keyword);
//				return PaymentVoucher_filtered;
//			} else {
//				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
//				System.out.println("page, page_size, sorted");
//				ResponseEntity<Object> PaymentVoucher_sorted = jv_Service.getAllSortedData(pageable1);
//				return PaymentVoucher_sorted;
//			}
//		} else {
//			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
//					ResponseHandler.message1);
//			return rs;
//		}
	

	@GetMapping("/getJournalVoucherData/{journal_voucher_number}/{school_id}/{financial_year_id}")
	public ResponseEntity<Object> getJournalVoucherData(@PathVariable Integer journal_voucher_number,
			@PathVariable Integer school_id, @PathVariable Integer financial_year_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> draft_journal_voucher = jv_Service
					.getJournalVoucherData(journal_voucher_number, school_id, financial_year_id);
			ResponseEntity<Object> djv_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					draft_journal_voucher);
			return djv_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getJournalVoucherByVoucherNumber/{journal_voucher_number}/{school_id}/{financial_year_id}")
	public ResponseEntity<Object> getJournalVoucherByVoucherNumber(@PathVariable Integer journal_voucher_number,
			@PathVariable Integer school_id, @PathVariable Integer financial_year_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				List<Map<String, Object>> jv = jv_Service.getJournalVoucherByVoucherNumber(journal_voucher_number,
						school_id, financial_year_id);
				ResponseEntity<Object> journalVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						jv);
				return journalVoucher_response;
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
	
	@GetMapping("/getJournalVoucherDataById/{journal_voucher_id}")
	public ResponseEntity<Object> getJournalVoucherDataById(@PathVariable Integer journal_voucher_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

			Map<String, Object> jv = jv_Service.getJournalVoucherDataById(journal_voucher_id);
				ResponseEntity<Object> journalVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						jv);
				return journalVoucher_response;
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

	@PostMapping(value = "/journalVoucherUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute MultipartFile multipartFile,
			@RequestParam Integer journal_voucher_number) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Message For Attachment");
		jv_Service.uploadFile(multipartFile, journal_voucher_number);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	@GetMapping(path = "/journalVoucherFileDownload")
	public ResponseEntity<ByteArrayResource> journalVoucherFileDownload(
			@RequestParam("fileName") final String pathName) {
		try {

			final byte[] data = jv_Service.journalVoucherFileDownload(pathName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}

	@GetMapping("/getjournalVoucherAttachmentByVoucherNo/{journal_voucher_number}/{financial_year_id}")
	public ResponseEntity<?> getjournalVoucherAttachmentByVoucherNo(@PathVariable Integer journal_voucher_number,
			@PathVariable Integer financial_year_id) {

		List<HashMap<String, Object>> obj = jv_Service.getjournalVoucherAttachmentByVoucherNo(journal_voucher_number,
				financial_year_id);
		return new ResponseEntity<>(obj, HttpStatus.OK);

	}

	@PostMapping("/journalVoucherCreationByMonthAndYear/{month}/{year}")
	public ResponseEntity<Object> journalVoucherCreationByMonthAndYear(@PathVariable Integer month,
			@PathVariable Integer year, @RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			List<JournalVoucher> journalVouchers = jv_Service.journalVoucherCreationByMonthAndYear(month, year, jwtDetails);
            return ResponseHandler.generateResponse(true, HttpStatus.CREATED, journalVouchers);
		} else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

}
