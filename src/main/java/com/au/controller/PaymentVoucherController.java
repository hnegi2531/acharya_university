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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.JwtDetails;
import com.au.dto.PaymentVoucherDto;
import com.au.model.CourseObjective;
import com.au.model.DraftPaymentVoucher;
import com.au.model.PaymentVoucher;
import com.au.model.PaymentVoucherAttachment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.PaymentVoucherService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class PaymentVoucherController {

	Logger log = LoggerFactory.getLogger(PaymentVoucherController.class);

	@Autowired
	private PaymentVoucherService pv_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/PaymentVoucher")
	public ResponseEntity<Object> saveDraftPaymentVoucher(@RequestBody @Valid List<PaymentVoucher> pv,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			pv.stream().forEach(p -> {

				p.setCreated_by(jwtDetails.getUserId());
				p.setCreated_username(jwtDetails.getUserName());
			});
			List<PaymentVoucher> PaymentVoucher = pv_Service.savePaymentVoucher(pv);
			ResponseEntity<Object> PaymentVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					PaymentVoucher);
			return PaymentVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/allActivePaymentVoucher")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<PaymentVoucher> pv = pv_Service.listAll1();
			ResponseEntity<Object> PaymentVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK, pv);
			return PaymentVoucher_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/paymentVoucher/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				PaymentVoucher pv = pv_Service.get(id);
				ResponseEntity<Object> PaymentVoucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						pv);
				return PaymentVoucher_response;
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

	@GetMapping("/fetchAllPaymentVoucher")
	public ResponseEntity<Object> getAllExamDetails(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword,
			 @RequestParam(value = "date_range", required = false) String dateRange,
		        @RequestParam(value = "start_date", required = false) String startDate,
		        @RequestParam(value = "end_date", required = false) String endDate) {

//		if (RateLimitController.bucket.tryConsume(1)) {
//			Sort sorted = Sort.by(Direction.DESC, sort);
//			if (keyword != null) {
//				Pageable pageable = PageRequest.of(page, page_size, sorted);
//				System.out.println("page, page_size, sorted, keyword");
//				ResponseEntity<Object> PaymentVoucher_filtered = pv_Service.getAllDataFilteredByKeyword(pageable,
//						keyword);
//				return PaymentVoucher_filtered;
//			} else {
//				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
//				System.out.println("page, page_size, sorted");
//				ResponseEntity<Object> PaymentVoucher_sorted = pv_Service.getAllSortedData(pageable1);
//				return PaymentVoucher_sorted;
//			}
//		} else {
//			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
//					ResponseHandler.message1);
//			return rs;
//		}
//	}
		
	 Sort sorted = Sort.by(Direction.DESC, sort);
	    Pageable pageable = PageRequest.of(page, page_size, sorted);

	    LocalDate start = null;
	    LocalDate end = null;
	    LocalDate minDate = pv_Service.getMinimumPaymentVoucherDate(); // dynamic minDate

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
	            ? pv_Service.getAllDataFilteredByKeyword(pageable, keyword, start, end, minDate)
	            : pv_Service.getAllSortedData(pageable, start, end, minDate);
	}

	@PutMapping("/updatePaymentVoucher/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<PaymentVoucher> paymentVoucher,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				paymentVoucher.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				pv_Service.updatePaymentVoucher1(paymentVoucher);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/PaymentVoucher/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			pv_Service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activatePaymentVoucher/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			pv_Service.delete1(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getPaymentVoucherData/{voucher_no}/{school_id}/{financial_year_id}")
	public ResponseEntity<Object> getPaymentVoucherData(@PathVariable Integer voucher_no,
			@PathVariable Integer school_id, @PathVariable Integer financial_year_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> program = pv_Service.getPaymentVoucherData(voucher_no, school_id,
					financial_year_id);
			ResponseEntity<Object> program_response = ResponseHandler.generateResponse(true, HttpStatus.OK, program);
			return program_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getPaymentVoucherDataById/{payment_voucher_id}")
	public ResponseEntity<Object> getPaymentVoucherDataById(@PathVariable Integer payment_voucher_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> program = pv_Service.getPaymentVoucherDataById(payment_voucher_id);
			ResponseEntity<Object> program_response = ResponseHandler.generateResponse(true, HttpStatus.OK, program);
			return program_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping(value = "/paymentVoucherUploadFile")
	public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile multipartFile,
			@RequestParam Integer payment_voucher_no) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Message For Attachment");
		pv_Service.uploadFile(multipartFile, payment_voucher_no);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	@GetMapping("/getVoucherAttachmentByVoucherNo/{voucherNo}")
	public ResponseEntity<?> getPaymentVoucherByVoucherNo(@PathVariable("voucherNo") Integer voucherNo) {

		List<PaymentVoucherAttachment> obj = pv_Service.getPaymentVoucherByVoucherNo(voucherNo);
		return new ResponseEntity<>(obj, HttpStatus.OK);

	}

	@GetMapping(path = "/paymentVoucherFileDownload")
	public ResponseEntity<ByteArrayResource> paymentVoucherFileDownload(
			@RequestParam("fileName") final String pathName) {
		try {

			final byte[] data = pv_Service.paymentVoucherFileDownload(pathName);
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

	@GetMapping("/getDraftVoucherFileByVoucherId/{voucherId}")
	public ResponseEntity<?> getDraftVoucherFileByVoucherId(@PathVariable("voucherId") Integer voucherId) {

		DraftPaymentVoucher obj = pv_Service.getVoucherFileByVoucherId(voucherId);
		return new ResponseEntity<>(obj, HttpStatus.OK);

	}
	
	@GetMapping("/getVoucherHeadBankDetails/{bankId}")
	public ResponseEntity<?> getVoucherHeadBankDetails(@PathVariable("bankId") Integer bankId) {

		List<Map<String, Object>> obj = pv_Service.getVoucherHeadBankDetails(bankId);
		return new ResponseEntity<>(obj, HttpStatus.OK);

	}
}
