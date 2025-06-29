package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
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
import org.springframework.web.client.RestTemplate;

import com.au.dto.CandidateWalkinLsqDto;
import com.au.dto.Candidate_walkinRequest;
import com.au.dto.EmployeeMedicalHistoryDto;
import com.au.dto.JwtDetails;
import com.au.model.ApplicantDetails;
import com.au.model.Candidate_Walkin;
import com.au.response.ResponseHandler;
import com.au.service.CandidateWalkInService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.au.controller.RateLimitController;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class CandidateWalkinController {

//	@Filter(name = "specification")
//	private Specification<Candidate_Walkin> column1;

	@Autowired
	private CandidateWalkInService can_service;

	@Autowired
	private JwtTokenService jwt_service;

	Logger log = LoggerFactory.getLogger(CandidateWalkinController.class);
	

	@PostMapping("/Candidate_Walkin")
	public ResponseEntity<Object> saveCandidateWalkin(@RequestBody @Valid Candidate_walkinRequest c,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			Candidate_Walkin c1 = c.getCd();

			c1.setCreated_by(jwtDetails.getUserId());
			c1.setCreated_username(jwtDetails.getUserName());
			c.setCd(c1);

			Candidate_Walkin cw = can_service.getcandidates(c);
			ResponseEntity<Object> candidate_walkin_response = ResponseHandler.generateResponse(true,
					HttpStatus.CREATED, cw);
			return candidate_walkin_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/Candidate_Walkin1")
	public ResponseEntity<Object> saveCandidateWalkin(@RequestBody @Valid Candidate_Walkin c)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			System.out.println("---");
			Candidate_Walkin cw1 = can_service.save_Candidate_Walkin(c);
			ResponseEntity<Object> candidate_walkin_response1 = ResponseHandler.generateResponse(true,
					HttpStatus.CREATED, cw1);
			return candidate_walkin_response1;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/Candidate_WalkinForLsq")
	public ResponseEntity<Object> Candidate_WalkinForLsq(@RequestBody @Valid Candidate_Walkin c) throws Exception {
		 log.debug("Received request for Candidate Walkin: {}", c);
		if (RateLimitController.bucket.tryConsume(1)) {
			   log.debug("Rate limit passed, processing the request.");
			   ResponseEntity<Object> cw =	 can_service.Candidate_WalkinForLsq(c);
			   return cw;

		} else {
			  log.warn("Too many requests, returning 429 status.");
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateCandidate_WalkinForLsq")
	public ResponseEntity<Object> updateCandidate_WalkinForLsq(@RequestBody CandidateWalkinLsqDto dto)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			return can_service.updateCandidate_WalkinForLsq(dto);
			
//			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getCandidateWalkinDataLsq")
	public ResponseEntity<Object> getCandidateWalkinDataLsq(
			@RequestParam(value = "lead_id", required = false) String lead_id,
			@RequestParam(value = "opportunity_id", required = false) String opportunity_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if (lead_id != null || ObjectUtils.isNotEmpty(lead_id)) {
				List<Candidate_Walkin> candidate = can_service.getCandidateWalkinDataLsqWithLeadId(lead_id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, candidate);
			} else {
				List<Candidate_Walkin> candidate = can_service
						.getCandidateWalkinDataLsqWithOpportunityId(opportunity_id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, candidate);
			}

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Candidate_Walkin")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Candidate_Walkin> list_candidate_walkin = can_service.listAll();
			ResponseEntity<Object> list_candidate_walkin_response = ResponseHandler.generateResponse(true,
					HttpStatus.OK, list_candidate_walkin);
			return list_candidate_walkin_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Candidate_Walkinssss")
	public ResponseEntity<Object> listAll6() {
		// RateLimitController rlc=new RateLimitController();
		if (RateLimitController.bucket.tryConsume(1)) {
			return can_service.listAll6();
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					"Too Many Requests !!");
			return rs;
		}
	}

	@GetMapping("/fetchAllFilteredCandidateWalkinDetails")
	public ResponseEntity<Object> getAllCandidateWalkinData(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> abc = can_service.getAllDataFilteredByKeyword(pageable, keyword);// ,column,value);
				return abc;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> xyz = can_service.getAllSortedData(pageable1);
				return xyz;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllFilteredCandidateWalkinDetailsOnPreApprovalStatus")
	public ResponseEntity<Object> getAllCandidateWalkinData1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> abc = can_service.getAllDataFilteredByKeyword1(pageable, keyword);// ,column,value);
				return abc;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> xyz = can_service.getAllSortedData1(pageable1);
				return xyz;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Candidate_Walkin/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				Candidate_Walkin product = can_service.get(id);
				ResponseEntity<Object> candidate_walkin_response_by_id = ResponseHandler.generateResponse(true,
						HttpStatus.OK, product);
				return candidate_walkin_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping(value = { "/Candidate_Walkin/{id}", "/Candidate_Walkin/{id}/{counselor_update}" })
	public ResponseEntity<Object> update(@RequestBody Candidate_Walkin c, @PathVariable Integer id,
			@PathVariable(required = false) String counselor_update)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				can_service.get(id);

				can_service.updateCounselorInLeadAssignment(c.getCounselor_id(), c.getCandidate_id());

				can_service.updateCandidateWalkin(c);

				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/Candidate_Walkin/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			can_service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateCandidate_Walkin/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			can_service.delete1(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/EditCandidateDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword,
			@RequestParam(value = "user_id", required = false) Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				if (user_id == null) {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> xyz = can_service.listAll1(pageable, keyword);// ,column,value);
					return xyz;
				} else {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> xyz = can_service.listAll2(pageable, keyword, user_id);// ,column,value);
					return xyz;
				}

			} else {
				if (user_id == null) {
					Pageable pageable1 = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> xyz = can_service.listAll3(pageable1);
					return xyz;
				} else {
					Pageable pageable1 = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> xyz = can_service.listAll4(pageable1, user_id);
					return xyz;
				}

			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/scholarship1/{cid}")
	public ResponseEntity<Object> listAll2(@PathVariable Integer cid) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> candidate_walkin_by_id = can_service.listAll2(cid);
			ResponseEntity<Object> list_candidate_walkin_response_by_id = ResponseHandler.generateResponse(true,
					HttpStatus.OK, candidate_walkin_by_id);
			return list_candidate_walkin_response_by_id;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/emailToCandidateForOffer")
	// public Object emailForOffer(@RequestParam("pdf_content") String
	// pdf_content,@RequestParam("candidate_id") Integer candidate_id) throws
	// Exception {
	public Object emailForOffer(@RequestBody @Valid ApplicantDetails ad) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				can_service.sendMailForStudentOffer(ad.getPdf_content(), ad.getCandidate_id());
				ResponseEntity<Object> offer_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return offer_response;
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

	@PutMapping("/updateLeadStatus")
	public ResponseEntity<Object> updateLeadStatus(@RequestParam(value = "candidate_id") Integer candidate_id,
			@RequestParam(value = "lead_status") String lead_status)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				Candidate_Walkin existProduct = can_service.get(candidate_id);

				can_service.updateLeadStatus(candidate_id, lead_status);
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchNonIndianDataFromCandidateWalkin")
	public ResponseEntity<Object> fetchNonIndianDataFromCandidateWalkin(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword,
			@RequestParam(value = "user_id", required = false) Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				if (user_id == null) {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> xyz = can_service.fetchNonIndianDataFromCandidateWalkin1(pageable, keyword);// ,column,value);
					return xyz;
				} else {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> xyz = can_service.fetchNonIndianDataFromCandidateWalkin2(pageable, keyword, user_id);// ,column,value);
					return xyz;
				}

			} else {
				if (user_id == null) {
					Pageable pageable1 = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> xyz = can_service.fetchNonIndianDataFromCandidateWalkin3(pageable1);
					return xyz;
				} else {
					Pageable pageable1 = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> xyz = can_service.fetchNonIndianDataFromCandidateWalkin4(pageable1, user_id);
					return xyz;
				}

			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/candidateWalkinForLsqApplicationStatus")
	public ResponseEntity<Object> candidateWalkinForLsqApplicationStatus(@RequestBody @Valid Candidate_Walkin c) throws Exception {
		return can_service.candidateWalkinForLsqApplicationStatus( c);
	}
	
	@GetMapping("/callLeadSquaredApiForUpdateAuid")
	public void callLeadSquaredApiForUpdateAuid(@RequestParam  Integer  candidateId,@RequestParam String auid) throws Exception {
		 can_service.callLeadSquaredApiForUpdateAuid(candidateId,auid);
	}
	

	}

