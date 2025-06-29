package com.au.controller;

import java.io.IOException;
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

import com.au.dto.FeeReceiptDto;
import com.au.dto.JwtDetails;
import com.au.model.CancelledFeeReceipts;
import com.au.model.CancelledHostelFeeReceipts;
import com.au.model.CancelledTallyHostelReceipt;
import com.au.model.EnvBillDetails;
import com.au.model.FeeReceipt;
import com.au.model.StudentPaymentHistory;
import com.au.response.ResponseHandler;
import com.au.service.FeeReceiptService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;


@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class FeeReceiptController {
	
	
	Logger log = LoggerFactory.getLogger(ApplicantDetailsController.class);
	
	@Autowired
	private FeeReceiptService frc_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/feeReceipt")
	public ResponseEntity<Object> saveFeeReceipt(@RequestBody @Valid FeeReceiptDto frd, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
				return  frc_ser.saveFeeReceipt(frd,jwtToken);
				
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/activeFeeReceipt")
	public ResponseEntity<Object> getActiveFeeReceipt(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<FeeReceipt> fee_receipt_list = frc_ser.getActiveFeeReceipt();
			ResponseEntity<Object> fee_receipt_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_receipt_list);
			return fee_receipt_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/feeReceipt")
	public ResponseEntity<Object> getAllFeeReceipt(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> fee_receipt_sorted = frc_ser.getAllFeeReceipt1(pageable, keyword);//,column,value);
						return fee_receipt_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> fee_receipt_pageable = frc_ser.getAllFeeReceipt2(pageable1);
						return fee_receipt_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return frc_ser.getAllFeeReceipt();
	}
	
	
	@GetMapping("/fetchAllFeeReceipt")
	public ResponseEntity<Object> fetchAllFeeReceipt(
		    @RequestParam(value = "page") Integer page,
		    @RequestParam(value = "page_size") Integer page_size,
		    @RequestParam(value = "sort") String sort,
		    @RequestParam(value = "school_id", required = false) Integer school_id,
		    @RequestParam(value = "keyword", required = false) Object keyword,
		    @RequestParam(value = "date_range", required = false) String dateRange,
		    @RequestParam(value = "start_date", required = false) String startDate,
		    @RequestParam(value = "end_date", required = false) String endDate) {

		    Sort sorted = Sort.by(Direction.DESC, sort);
		    Pageable pageable = PageRequest.of(page, page_size, sorted);

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

		    // Pass the filter and pageable to service
		    if (keyword != null) {
		        return frc_ser.FeeReceiptListAll1(pageable, keyword,school_id, start, end, minDate);
		    } else {
		        return frc_ser.FeeReceiptListAll12(pageable,school_id, start, end, minDate);
		    }
		}
	
	
		@GetMapping("/getFeeReceiptWiseAndUserWiseData")
		public ResponseEntity<Object> getFeeReceipyWiseAndUserWiseData(
				@RequestParam(value = "start_date", required = false) String startDate,
				@RequestParam(value = "end_date", required = false) String endDate) {

			if (RateLimitController.bucket.tryConsume(1)) {
				LocalDate start = null;
				LocalDate end = null;
				try {
					start = LocalDate.parse(startDate);
					end = LocalDate.parse(endDate);
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + start);
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + end);
				} catch (DateTimeParseException e) {
					return ResponseEntity.badRequest()
							.body("Invalid date format. Ensure the date format is 'yyyy-MM-dd'.");
				}
				List<Map<String, Object>> details_of_candidate = frc_ser.getFeeReceipyWiseAndUserWiseData(start, end);
				ResponseEntity<Object> details_of_candidate_response = ResponseHandler.generateResponse(true,
						HttpStatus.OK, details_of_candidate);
				return details_of_candidate_response;
			} else {
				ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
						"Too Many Requests !!");
				return rs;
			}
		}

	@GetMapping("/feeReceipt/{fee_receipt_id}")
	public ResponseEntity<Object> getFeeReceiptById(@PathVariable Integer fee_receipt_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						FeeReceipt  product = frc_ser.getFeeReceiptById(fee_receipt_id);
						ResponseEntity<Object> fee_receipt_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return fee_receipt_response_by_id;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/feeReceipt/{fee_receipt_id}")
	public ResponseEntity<Object> updateFeeReceipt(@RequestBody @Valid FeeReceipt frc, @PathVariable Integer fee_receipt_id ,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						frc.setModified_by(jwtDetails.getUserId());
						frc.setModified_username(jwtDetails.getUserName());
						frc_ser.updateFeeReceipt(frc);
						ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
						return response;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/feeReceipt/{fee_receipt_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer fee_receipt_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				frc_ser.delete(fee_receipt_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateFeeReceipt/{fee_receipt_id}")
	public ResponseEntity<Object> activateFeeReceipt(@PathVariable Integer fee_receipt_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				frc_ser.activateFeeReceipt(fee_receipt_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	
	@GetMapping("/getAllDataOfFeeReceiptForFormating/{student_id}")
	public ResponseEntity<Object> getAllDataOfFeeReceiptForFormating(@PathVariable Integer student_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			log.debug("Request {}", student_id);
			HashMap<String, Object> fee_rec_by_student_id = frc_ser.getAllDataOfFeeReceiptForFormating(student_id);
			ResponseEntity<Object> fee_rec_by_student_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_rec_by_student_id);
			return fee_rec_by_student_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/cmaDueAmountCalculationOnYearWiseForFeeReceipt/{auid}")
	public ResponseEntity<Object> cmaDueAmountCalculationOnYearWiseForFeeReceipt(@PathVariable String auid){
		if(RateLimitController.bucket.tryConsume(1)) {
			log.debug("Request {}", auid);
			HashMap<String, Object> fee_rec_by_student_id = frc_ser.cmaDueAmountCalculationOnYearWiseForFeeReceipt(auid);
			ResponseEntity<Object> fee_rec_by_student_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_rec_by_student_id);
			return fee_rec_by_student_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/uniformAndStationaryDueAmountCalculationOnYearWiseForFeeReceipt/{auid}")
	public ResponseEntity<Object> uniformAndStationaryDueAmountCalculationOnYearWiseForFeeReceipt(@PathVariable String auid){
		if(RateLimitController.bucket.tryConsume(1)) {
			log.debug("Request {}", auid);
			HashMap<String, Object> fee_rec_by_student_id = frc_ser.uniformAndStationaryDueAmountCalculationOnYearWiseForFeeReceipt(auid);
			ResponseEntity<Object> fee_rec_by_student_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_rec_by_student_id);
			return fee_rec_by_student_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/dueAmountCalculationOnVocherHeadWiseAndYearWiseForFeeReceipt/{student_id}")
	public ResponseEntity<Object> dueAmountCalculationOnVocherHeadWiseAndYearWiseForFeeReceipt(@PathVariable Integer student_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			log.debug("Request {}", student_id);
			HashMap<String, Object> fee_rec_by_student_id = frc_ser.dueAmountCalculationOnVocherHeadWiseAndYearWiseForFeeReceipt(student_id);
			ResponseEntity<Object> fee_rec_by_student_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_rec_by_student_id);
			return fee_rec_by_student_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/getDataForDisplayingFeeReceipt/{student_id}/{financial_year_id}/{fee_receipt}/{transaction_type}/{hostel_status}")
	public ResponseEntity<Object> getDataForDisplayingFeeReceipt(@PathVariable Integer student_id,@PathVariable Integer financial_year_id,
			@PathVariable String fee_receipt,@PathVariable String transaction_type,@PathVariable Integer hostel_status) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> details_of_candidate= frc_ser.getDataForDisplayingFeeReceipt1(student_id,financial_year_id,
					fee_receipt,transaction_type,hostel_status);
			ResponseEntity<Object> details_of_candidate_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_of_candidate);
			return details_of_candidate_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			return rs;
		}
	}
	
	@GetMapping("/getDataForDisplayingAndCancelFeeReceipt")
	public ResponseEntity<Object> getDataForDisplayingAndCancelFeeReceipt(@RequestParam(value="financial_year_id") Integer financial_year_id,
			@RequestParam(value="school_id") Integer school_id, @RequestParam(value="fee_receipt") String fee_receipt) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> details_of_candidate= frc_ser.getDataForDisplayingAndCancelFeeReceipt(financial_year_id,school_id,fee_receipt);
			ResponseEntity<Object> details_of_candidate_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_of_candidate);
			return details_of_candidate_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			return rs;
		}
	}
	

	@PostMapping("/cancelFeeReceipt")
	public ResponseEntity<Object> cancelFeeReceipt1(@RequestBody @Valid CancelledFeeReceipts cfr, @RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			String  message =	frc_ser.cancelFeeReceipt1(cfr,jwtToken);
				ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, message);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@PutMapping("/UpdationOfBankTransactionHistoryId")
	public ResponseEntity<Object> updationOfBankTransactionHistoryId()throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						frc_ser.updationOfBankTransactionHistoryId();
						ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
						return response;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/feeReceiptTransfer/{oldStudentId}/{newStudentId}")
	public ResponseEntity<Object> feeReceiptTransfer(@RequestBody @Valid  List<StudentPaymentHistory> studentPaymentHistory,
			@RequestHeader("Authorization") String jwtToken,@PathVariable Integer oldStudentId,@PathVariable Integer newStudentId)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				studentPaymentHistory.stream().forEach(sph ->{
					sph.setCreated_by(jwtDetails.getUserId());
					sph.setCreated_username(jwtDetails.getUserName());
				});
				frc_ser.feeReceiptTransfer(studentPaymentHistory,oldStudentId,newStudentId);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/feeReceiptByStudentIdForReceiptTransfer/{oldStudentId}")
	public ResponseEntity<Object> feeReceiptByStudenId(@PathVariable Integer oldStudentId){
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

				List<HashMap<String,Object>>  feeReceipt = frc_ser.feeReceiptByStudenId(oldStudentId);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, feeReceipt);

			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}		
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	
	@GetMapping("/getFeeReceiptDetailsData/{fee_receipt_id}")
	public ResponseEntity<Object> getFeeReceiptDetailsData(@PathVariable Integer fee_receipt_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> details_of_candidate= frc_ser.getFeeReceiptDetailsData(fee_receipt_id);
			ResponseEntity<Object> details_of_candidate_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_of_candidate);
			return details_of_candidate_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			return rs;
		}

	}
	
	
	@GetMapping("/hostelDueCalculationVocherHeadWise/{acYearId}/{studentId}")
	public ResponseEntity<Object> hostelDueCalculationVocherHeadWise(@PathVariable Integer  acYearId,@PathVariable Integer  studentId){
		if(RateLimitController.bucket.tryConsume(1)) {
			return frc_ser.hostelDueCalculationVocherHeadWise(acYearId,studentId);
			
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
	
	@GetMapping("/getFeeReceiptDetails")
	public ResponseEntity<Object> getFeeReceiptDetails(@RequestParam("studentId") Integer studentId){
			 return  frc_ser.getFeeReceiptDetails(studentId);
			
	}
	
	@GetMapping("/changeOfCourseFeePaidStatusByStudentId/{studentId}")
	public ResponseEntity<Object> changeOfCourseFeePaidStatusByStudentId(@PathVariable Integer studentId){
		return  frc_ser.changeOfCourseFeePaidStatusByStudentId(studentId);

	}
	
	@GetMapping("/checkDuesOnSemForGeneratingNoDues/{studentId}")
	public ResponseEntity<Object> checkDuesOnSemForGeneratingNoDues(@PathVariable Integer studentId){
		return  frc_ser.checkDuesOnSemForGeneratingNoDues(studentId);

	}
	
	@GetMapping("/hostelFeeReceiptDetailsByFeeReceiptId/{feeReceiptId}")
	public ResponseEntity<Object> hostelFeeReceiptDetailsByFeeReceiptId(@PathVariable Integer feeReceiptId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return frc_ser.hostelFeeReceiptDetailsByFeeReceiptId(feeReceiptId);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			
		}
	}
	
	@GetMapping("/hostelBulkFeeReceiptDetailsByFeeReceiptId/{feeReceiptId}")
	public ResponseEntity<Object> hostelBulkFeeReceiptDetailsByFeeReceiptId(@PathVariable Integer feeReceiptId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return frc_ser.hostelBulkFeeReceiptDetailsByFeeReceiptId(feeReceiptId);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			
		}
	}

	@GetMapping("/paymentInformationByOrderId")
	public ResponseEntity<Object> paymentInformationByOrderId(@RequestParam("orderId") String orderId)
	{
		return frc_ser.paymentInformationByOrderId(orderId);
	}


//	@GetMapping("/feeReceiptsByDate")
//	public ResponseEntity<Object> getFeeReceiptsByDate(@RequestParam("date") String date)
//	{
//		return frc_ser.getFeeReceiptsByDate(date);
//	}
	
	
	
	@DeleteMapping("/inActivateFeeReceiptNumber")
	public ResponseEntity<Object> inActivateFeeReceiptNumber(@RequestParam("financial_year_id") Integer financial_year_id ,
			@RequestParam("fee_receipt") Integer fee_receipt,
			@RequestParam("receipt_type") String receipt_type) {
		if(RateLimitController.bucket.tryConsume(1)) {
				frc_ser.inActivateFeeReceiptNumber(financial_year_id,fee_receipt,receipt_type);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	
	@GetMapping("/getHostelFeeReceiptVoucherHeadWiseDetails")
	public ResponseEntity<Object> getHostelFeeReceiptVoucherHeadWiseDetails(@RequestParam("financial_year_id") Integer financial_year_id ,
			@RequestParam("fee_receipt") Integer fee_receipt,
			@RequestParam("receipt_type") String receipt_type) {
		if(RateLimitController.bucket.tryConsume(1)) {
			 ResponseEntity<Object> details_of_candidate= frc_ser.getHostelFeeReceiptVoucherHeadWiseDetails(financial_year_id,fee_receipt,receipt_type);
			ResponseEntity<Object> details_of_candidate_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_of_candidate);
			return details_of_candidate_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests !!");
			return rs;
		}

	}
	
	
	@PostMapping("/saveCancelledHostelFeeReceipts")
	public ResponseEntity<Object> saveCancelledHostelFeeReceipts(@RequestBody @Valid CancelledHostelFeeReceipts cancelledHostelFeeReceipts,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			
			CancelledHostelFeeReceipts cancelledFeeReceipts = frc_ser.saveCancelledHostelFeeReceipts(cancelledHostelFeeReceipts);
			ResponseEntity<Object> envBillDetail_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,cancelledFeeReceipts);
			return envBillDetail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
//	@PostMapping("/saveCancelledTallyHostelReceipt")
//	public ResponseEntity<Object> saveCancelledTallyHostelReceipt(@RequestBody @Valid CancelledTallyHostelReceipt cancelledTallyHostelReceipt,
//			@RequestHeader("Authorization") String jwtToken) throws Exception {
//		if (RateLimitController.bucket.tryConsume(1)) {
//			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//			
//			cancelledTallyHostelReceipt.setCreated_by(jwtDetails.getUserId());
//			cancelledTallyHostelReceipt.setCreated_username(jwtDetails.getUserName());
//			
//			CancelledTallyHostelReceipt cancelledTallyReceipt = frc_ser.saveCancelledTallyHostelReceipt(cancelledTallyHostelReceipt);
//			ResponseEntity<Object> cancelledTallyReceipt_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,cancelledTallyReceipt);
//			return cancelledTallyReceipt_response;
//		} else {
//			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
//					ResponseHandler.message1);
//			return rs;
//		}
//	}

	@GetMapping("/getCounterSummary")
	public ResponseEntity<Object> getCounterSummary(@RequestParam String fromDate,@RequestParam String toDate){
		return frc_ser.getCounterSummary(fromDate, toDate);
	}

	@GetMapping("/getCounterSummaryBySchools")
	public ResponseEntity<Object> getCounterSummaryBySchools(@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate){
		return frc_ser.getCounterSummaryBySchools(fromDate, toDate);
	}

}
