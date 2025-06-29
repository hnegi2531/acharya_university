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

import com.au.dto.CopyFeeTemplateDto;
import com.au.dto.FeeTemplateHistoryRequest;
import com.au.dto.FeeTemplateRequest;
import com.au.dto.JobFileRequest;
import com.au.dto.JwtDetails;
import com.au.model.FeeTemplate;
import com.au.model.ProgramSpecilization;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;
import com.au.service.FeeTemplateService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class FeeTemplateController {

	Logger log = LoggerFactory.getLogger(FeeTemplateController.class);

	@Autowired
	private FeeTemplateService fts_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/FeeTemplate")
	public ResponseEntity<Object> saveFeeTemplate(@RequestBody @Valid FeeTemplateRequest feetemplate, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
//				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//				feetemplate.setCreated_by(jwtDetails.getUserId());
//				feetemplate.setCreated_username(jwtDetails.getUserName());
			List<FeeTemplate> fee_template_list = fts_service.getTemplateName(feetemplate, jwtToken);
			ResponseEntity<Object> fee_template_list_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, fee_template_list);
			return fee_template_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping(value = "/FeeTemplateUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute JobFileRequest jobfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		fts_service.uploadFile(jobfilerequest.getFile(), jobfilerequest.getFee_template_id());
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	@GetMapping(path = "/FeeTemplateFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = fts_service.viewFiles(fileName);
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

	@GetMapping("/FeeTemplate")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<FeeTemplate> fee_template_list = fts_service.listAll();
			ResponseEntity<Object> fee_template_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_template_list);
			return fee_template_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}

//	@GetMapping("/fetchFeeTemplateDetail")
//	public List<HashMap<String, Object>> listAll2() {
//		return fts_service.listAll2();
//		}

	@GetMapping("/fetchFeeTemplateDetail")
	public ResponseEntity<Object> listAll2(@RequestParam(value = "page") Integer page, @RequestParam(value = "page_size") Integer page_size,
										   @RequestParam(value = "sort") String sort, @RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> fee_template_sorted = fts_service.listAll2(pageable, keyword);//,column,value);
				return fee_template_sorted;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> fee_template_pageable = fts_service.listAll3(pageable1);
				return fee_template_pageable;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		//return fts_service.listAll2();
	}

	@GetMapping("/fetchFeeTemplateDetailByAcYearId")
	public ResponseEntity<Object> listAllAcYearId(@RequestParam(value = "page") Integer page, @RequestParam(value = "page_size") Integer page_size,
												  @RequestParam(value = "sort") String sort, @RequestParam(value = "keyword", required = false) Object keyword
			, @RequestParam(value = "ac_year_id", required = false) Integer ac_year_id, @RequestParam(value = "school_id", required = false) Integer school_id
			, @RequestParam(value = "fee_admission_category_id", required = false) Integer fee_admission_category_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> fee_template_sorted = fts_service.listAllAcYearId1(pageable, keyword, ac_year_id, school_id, fee_admission_category_id);//,column,value);
				return fee_template_sorted;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> fee_template_pageable = fts_service.listAllAcYearId2(pageable1, ac_year_id, school_id, fee_admission_category_id);
				return fee_template_pageable;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		//return fts_service.listAll2();
	}

	@GetMapping("/FeeTemplate/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				FeeTemplate product = fts_service.get(id);
				ResponseEntity<Object> fee_template_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
				return fee_template_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/FeeTemplate/{fee_template_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid FeeTemplate feetemplate, @PathVariable Integer fee_template_id,
										 @RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			//FeeTemplate existProduct = fts_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			feetemplate.setModified_by(jwtDetails.getUserId());
			feetemplate.setModified_username(jwtDetails.getUserName());
			fts_service.saveFeeTemplates(feetemplate, jwtToken);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateFeeTemplate/{fee_template_id}")
	public ResponseEntity<Object> updates(@RequestBody @Valid FeeTemplateHistoryRequest feetemplate, @PathVariable Integer fee_template_id,
										  @RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			//FeeTemplate existProduct = fts_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			feetemplate.getFtt().setModified_by(jwtDetails.getUserId());
			feetemplate.getFtt().setModified_username(jwtDetails.getUserName());
			fts_service.saveFeeTemplate(feetemplate, jwtToken);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/FeeTemplate/{fee_template_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer fee_template_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			fts_service.delete(fee_template_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFeeTemplate/{fee_template_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer fee_template_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			fts_service.delete1(fee_template_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateApproveFeeTemplate/{fee_template_id}")
	public ResponseEntity<Object> delete2(@PathVariable Integer fee_template_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			fts_service.delete2(fee_template_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/FeeTemplateCount/{id1}/{id2}/{id3}") // no of count behalf of schoolid
	public ResponseEntity<Object> countRecords(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2,
											   @PathVariable("id3") Integer id3) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Integer count_records = fts_service.countRecords(id1, id2, id3);
			ResponseEntity<Object> count_response = ResponseHandler.generateResponse(true, HttpStatus.OK, count_records);
			return count_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/checkApi")
	public ResponseEntity<Object> saveFeeTemplate(@RequestBody @Valid FeeTemplate board) {
		if (RateLimitController.bucket.tryConsume(1)) {
			FeeTemplate fee_template = fts_service.saveFeeTemplate1(board);
			ResponseEntity<Object> fee_template_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, fee_template);
			return fee_template_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
/*
	@PostMapping("/FeeTemplateDetails") // (Behalf of fee_template_id)
	public List<HashMap<String, Object>> fetch1(@RequestBody List<Integer> fee_template_id) {
		return fts_service.findById(fee_template_id);
	}
	*/

	@GetMapping("/FeeAdmissionSubCategoryDetail/{fee_admission_sub_category_id}") // (Behalf of fee_template_id)
	public ResponseEntity<Object> fetch(@RequestBody @PathVariable Integer fee_admission_sub_category_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> fee_admission_sub_category_detail = fts_service.findByFeeAdmissionSubCategory(fee_admission_sub_category_id);
			ResponseEntity<Object> fee_admission_sub_category_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_admission_sub_category_detail);
			return fee_admission_sub_category_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@GetMapping("/EditFeeTemplate")
	public ResponseEntity<Object> listAll1() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> list_of_fee_template = fts_service.listAll1();
			ResponseEntity<Object> list_of_fee_template_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_of_fee_template);
			return list_of_fee_template_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/FetchAllFeeTemplateDetail/{fee_template_id}") // (Behalf of fee_template_id)
	public ResponseEntity<Object> fetch2(@RequestBody @PathVariable Integer fee_template_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> fee_tem_by_id = fts_service.findByFeeTemplate1(fee_template_id);
			ResponseEntity<Object> list_of_fee_template_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_tem_by_id);
			return list_of_fee_template_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/FetchAllFeeTemplateDetails/{ac_year_id}/{school_id}/{program_id}/{program_specialization_id}/{fee_admission_category_id}/{fee_admission_sub_category_id}/{is_nri}")
	// (Behalf of fee_template_id)
	public ResponseEntity<Object> fetch3(@RequestBody @PathVariable Integer ac_year_id, @PathVariable Integer school_id,
										 @PathVariable Integer program_id, @PathVariable String program_specialization_id, @PathVariable Integer fee_admission_category_id,
										 @PathVariable Integer fee_admission_sub_category_id, @PathVariable Boolean is_nri) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> fee_temp_by_ids = fts_service.findByFeeTemplate3(ac_year_id, school_id, program_id, program_specialization_id,
					fee_admission_category_id, fee_admission_sub_category_id, is_nri);
			ResponseEntity<Object> list_of_fee_template_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_temp_by_ids);
			return list_of_fee_template_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/allFeeTemplateDetail/{ac_year_id}/{fee_admission_category_id}/{fee_admission_sub_category_id}/{nationality}/{program_type_id}/{currency_type_id}/{school_id}/{program_id}")
	public ResponseEntity<Object> fetch4(@RequestBody @PathVariable Integer ac_year_id,
										 @PathVariable Integer fee_admission_category_id, @PathVariable Integer fee_admission_sub_category_id, @PathVariable String nationality,
										 @PathVariable Integer program_type_id, @PathVariable Integer currency_type_id,
										 @PathVariable Integer school_id, @PathVariable Integer program_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> fee_template_list_by_ids = fts_service.findByFeeTemplate4(ac_year_id, fee_admission_category_id, fee_admission_sub_category_id, nationality,
					program_type_id, currency_type_id, school_id, program_id);
			ResponseEntity<Object> list_of_fee_template_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_template_list_by_ids);
			return list_of_fee_template_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}

	@GetMapping("/FetchStudentDetailsByFeeTemplateId/{fee_template_id}") // (Behalf of fee_template_id)
	public ResponseEntity<Object> FetchStudentDetailsByFeeTemplateId(@RequestBody @PathVariable Integer fee_template_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> fee_tem_by_id = fts_service.findStudentDetailsByFeeTemplateId(fee_template_id);
			ResponseEntity<Object> list_of_fee_template_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_tem_by_id);
			return list_of_fee_template_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/FetchCountStudentDetailsByFeeTemplateId")
	public ResponseEntity<Object> fetchCountOfStudentDetailsByFeeTemplateId() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> fee_tem_by_id = fts_service.fetchCountOfStudentDetailsByFeeTemplateId();
			ResponseEntity<Object> list_of_fee_template_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_tem_by_id);
			return list_of_fee_template_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/copyFeeTemplates")
	public ResponseEntity<Object> copyFeeTemplates(@RequestBody @Valid CopyFeeTemplateDto cftd, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {

			System.out.println("fffffffffffffffffffffffffffff " + cftd.getFee_template_id());
			List<FeeTemplate> copied_fee_template = fts_service.copyFeeTemplates(cftd, jwtToken);
			ResponseEntity<Object> copied_fee_template_response = ResponseHandler.generateResponse(true, HttpStatus.OK, copied_fee_template);
			return copied_fee_template_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getFeeTemplateDetailsData/{fee_template_id}")
	public ResponseEntity<Object> getFeeTemplateDetailsData(@PathVariable Integer fee_template_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> feeTemplate = fts_service.getFeeTemplateDetailsData(fee_template_id);
			ResponseEntity<Object> feeTemplate_response = ResponseHandler.generateResponse(true, HttpStatus.OK, feeTemplate);
			return feeTemplate_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/FetchFeeTemplateDetailsByFeeTemplateId/{fee_template_ids}")
	public ResponseEntity<Object> FetchFeeTemplateDetailsByFeeTemplateId(@PathVariable List<Integer> fee_template_ids) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Map<Integer, List<Map<String, Object>>> fee_tem_by_id = fts_service.FetchFeeTemplateDetailsByFeeTemplateId(fee_template_ids);
			ResponseEntity<Object> list_of_fee_template_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_tem_by_id);
			return list_of_fee_template_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getAllFeeTemplate")
	public ResponseEntity<Object> getAllFeeTemplate() {
		return fts_service.getAllFeeTemplate();
	}

	@GetMapping("/FetchStudentDetailsByFeeTemplateIds")
	public ResponseEntity<Object> FetchStudentDetailsByFeeTemplateId(@RequestBody List<Integer> fee_template_ids) {

			List<HashMap<String, Object>> fee_tem_by_id = fts_service.findStudentDetailsByFeeTemplateIds(fee_template_ids);
			ResponseEntity<Object> list_of_fee_template_detail_response = ResponseHandler.generateResponse(true, HttpStatus.OK, fee_tem_by_id);
			return list_of_fee_template_detail_response;


	}
}
	

