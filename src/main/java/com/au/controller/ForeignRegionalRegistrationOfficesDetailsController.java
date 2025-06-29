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
import com.au.dto.ForeignRegionalRegistrationOfficesDetailsFileRequest;
import com.au.dto.JwtDetails;
import com.au.model.ForeignRegionalRegistrationOfficesDetails;
import com.au.response.ResponseHandler;
import com.au.service.ForeignRegionalRegistrationOfficesDetailsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ForeignRegionalRegistrationOfficesDetailsController {
	
	Logger log = LoggerFactory.getLogger(ForeignRegionalRegistrationOfficesDetailsController.class);
	
	@Autowired
	private ForeignRegionalRegistrationOfficesDetailsService frrod_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/frroDetails")
	public ResponseEntity<Object> saveForeignRegionalRegistrationOfficesDetails(@RequestBody @Valid ForeignRegionalRegistrationOfficesDetails frrod,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			frrod.setCreated_by(jwtDetails.getUserId());
			frrod.setCreated_username(jwtDetails.getUserName());
			ForeignRegionalRegistrationOfficesDetails frro_detail = frrod_ser.saveForeignRegionalRegistrationOfficesDetails(frrod);
			ResponseEntity<Object> frro_detail_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, frro_detail);
			return frro_detail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/frroDetails")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ForeignRegionalRegistrationOfficesDetails> frro_details = frrod_ser.listAll();
			ResponseEntity<Object> frro_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, frro_details);
			return frro_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllFrroDetails")
	public ResponseEntity<Object> getAllRolesDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> frrod_details_filtered =  frrod_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return frrod_details_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> frrod_details_sorted = frrod_ser.getAllSortedData(pageable1);
			return frrod_details_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/frroDetails/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

				ForeignRegionalRegistrationOfficesDetails product =frrod_ser.get(id);
				ResponseEntity<Object> product_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
				return product_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/frroDetails/{id}")
	public ResponseEntity<Object> updateForeignRegionalRegistrationOfficesDetails(@RequestBody @Valid ForeignRegionalRegistrationOfficesDetails frrod, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				frrod_ser.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				frrod.setModified_by(jwtDetails.getUserId());
				frrod.setModified_username(jwtDetails.getUserName());
				frrod_ser.updateForeignRegionalRegistrationOfficesDetails(frrod);
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/frroDetails/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			frrod_ser.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFrroDetails/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			frrod_ser.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping(value="/FrroDetailsUploadFile")
	public ResponseEntity<Object> uploadFile(
			@ModelAttribute ForeignRegionalRegistrationOfficesDetailsFileRequest frrodfs) throws IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For VendorAttachment");
			ForeignRegionalRegistrationOfficesDetails frro_details = frrod_ser.uploadFile(
					frrodfs.getPassport_copy_document_file(), frrodfs.getVisa_copy_document_file(),
					frrodfs.getResidential_permit_copy_document_file(), frrodfs.getAiu_equivalence_document_file(),
					frrodfs.getFrrod_id(), frrodfs.getStudent_id());
			ResponseEntity<Object> va_response = ResponseHandler.generateResponse(true, HttpStatus.OK, frro_details);
			return va_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}

	}
	
	@GetMapping(path = "/FrroDetailsFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("PathName") final String pathName) {
		try {
			final byte[] data = frrod_ser.downloadFile(pathName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
}
