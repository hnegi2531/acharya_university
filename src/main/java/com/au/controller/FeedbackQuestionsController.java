package com.au.controller;
import java.io.IOException;
import java.util.List;
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
import com.au.dto.AcademicSchoolRequest;
import com.au.dto.FeedbackQuestionsDto;
import com.au.dto.JwtDetails;
import com.au.model.FeedbackQuestions;
import com.au.model.academic_school;
import com.au.response.ResponseHandler;
import com.au.service.AcademicSchoolService;
import com.au.service.FeedbackQuestionsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class FeedbackQuestionsController {
	@Autowired
	private FeedbackQuestionsService fc_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	Logger log = LoggerFactory.getLogger(FeedbackQuestionsController.class);
	
	@PostMapping("/feedbackQuestions")
	public ResponseEntity<Object> saveFeedbackQuestions(@RequestBody @Valid FeedbackQuestionsDto fq,@RequestHeader("Authorization") String jwtToken)
			throws  Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				fq.setCreated_by(jwtDetails.getUserId());
				fq.setCreated_username(jwtDetails.getUserName());
				List<FeedbackQuestions> list_feedback_questions = fc_service.getFeedbackQuestions(fq);
				ResponseEntity<Object> list_feedback_questions_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, list_feedback_questions);
				return list_feedback_questions_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/feedbackQuestions")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<FeedbackQuestions> list_feedback_questions = fc_service.listAll();
				ResponseEntity<Object> list_feedback_questions_response1= ResponseHandler.generateResponse(true, HttpStatus.OK, list_feedback_questions);
				return list_feedback_questions_response1;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/fetchFeedbackQuestionsDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> feedback_questions_details = fc_service.listAll1(pageable, keyword);//,column,value);
						return feedback_questions_details;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> feedback_questions_details1 = fc_service.listAll2(pageable1);
						return feedback_questions_details1;
			}
			
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/feedbackQuestions/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					try {

						FeedbackQuestions product = fc_service.get(id);
							ResponseEntity<Object> feedback_questions_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
							return feedback_questions_response;

					} catch (NoSuchElementException e) {
							ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
							return response1;
					}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@PutMapping("/feedbackQuestions/{id}")
	public ResponseEntity<Object> update(@RequestBody FeedbackQuestions bq, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {

				try {
					FeedbackQuestions existProduct = fc_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						bq.setModified_by(jwtDetails.getUserId());
						bq.setModified_username(jwtDetails.getUserName());
						fc_service.save_academic_school(bq);
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

	

	@DeleteMapping("/feedbackQuestions/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return fc_service.delete(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/activateFeedbackQuestions/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return fc_service.delete1(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
}
