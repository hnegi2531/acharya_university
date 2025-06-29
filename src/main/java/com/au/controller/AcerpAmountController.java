package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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

import com.au.dto.JobFileRequest;
import com.au.dto.JwtDetails;
import com.au.model.AcerpAmount;
import com.au.model.AcerpAmount;
import com.au.response.ResponseHandler;
import com.au.service.AcerpAmountService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class AcerpAmountController {
	
	Logger log = LoggerFactory.getLogger(AcerpAmountController.class);

	@Autowired
	private AcerpAmountService twp_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/createAcerpAmount")
	public ResponseEntity<Object> createAcerpAmount(@RequestBody @Valid AcerpAmount acerp,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			acerp.setCreatedBy(jwtDetails.getUserId());
			acerp.setCreatedUsername(jwtDetails.getUserName());
			AcerpAmount AcerpAmount = twp_Service.createAcerpAmount(acerp, jwtDetails);
			ResponseEntity<Object> vacationresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					AcerpAmount);
			return vacationresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/allActiveAcerpAmount")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<AcerpAmount> vacation = twp_Service.listAll();
			ResponseEntity<Object> vacation_response = ResponseHandler.generateResponse(true, HttpStatus.OK, vacation);
			return vacation_response;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/getAcerpAmount/{acerpAmountId}")
	public ResponseEntity<Object> get(@PathVariable Integer acerpAmountId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				AcerpAmount response = twp_Service.get(acerpAmountId);
				ResponseEntity<Object> tech_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						response);
				return tech_response;
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

	@GetMapping("/fetchAllAcerpAmount")
	public ResponseEntity<Object> fetchAllAcerpAmount(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> vacation_filtered = twp_Service.getAllDataFilteredByKeyword(pageable, keyword);
				return vacation_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> vacation_sorted = twp_Service.getAllSortedData(pageable1);
				return vacation_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateAcerpAmountById/{acerpAmountId}")
	public ResponseEntity<Object> update(@RequestBody @Valid AcerpAmount pr,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				pr.setModifiedBy(jwtDetails.getUserId());
				pr.setModifiedUsername(jwtDetails.getUserName());
				AcerpAmount response = twp_Service.updateAcerpAmount(pr);

				ResponseEntity<Object> tech_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return tech_response;
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

	@DeleteMapping("/deactivateAcerpAmount/{acerpAmountId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer acerpAmountId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			twp_Service.delete(acerpAmountId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateAcerpAmount/{acerpAmountId}")
	public ResponseEntity<Object> activate(@PathVariable Integer acerpAmountId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			twp_Service.delete1(acerpAmountId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateAcerpAmountPartially")
	public ResponseEntity<Object> updateAcerpAmountByacerpAmountId(@RequestBody @Valid AcerpAmount am,
			@RequestHeader("Authorization") String jwtToken ) throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			twp_Service.updateAcerpAmountByAcerpAmountId(am, jwtDetails);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping(value = "/acerpAmountUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute JobFileRequest jobfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		twp_Service.uploadFile(jobfilerequest.getFile(), jobfilerequest.getAcerpAmountId());
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/acerpAmountFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = twp_Service.viewFiles(fileName);
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
	
	@GetMapping("/checkAuidWithFeeTypeIsAlreadyPresentOrNot")
	public ResponseEntity<Object> checkAuidWithFeeTypeIsAlreadyPresentOrNot(@RequestParam(value = "auid") String auid,@RequestParam(value = "type") String type) {
		if(RateLimitController.bucket.tryConsume(1)) {
			String amount = twp_Service.checkAuidWithFeeTypeIsAlreadyPresentOrNot(auid,type);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, amount);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	

	@GetMapping("/getAcerpAmountByAuid")
	public ResponseEntity<Object> getAcerpAmountByAuid(@RequestParam(value = "auid") String auid,@RequestParam(value = "type") String type) {
		if (RateLimitController.bucket.tryConsume(1)) {
			ResponseEntity<Object> acerpAmountData = twp_Service.getAcerpAmountByAuid(auid,type);
//			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
//					acerpAmountData);
			return acerpAmountData;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

}
