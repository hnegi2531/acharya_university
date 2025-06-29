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

import com.au.dto.HostelWaiverAttachmentDto;
import com.au.dto.JwtDetails;
import com.au.model.AcerpAmount;
import com.au.model.HostelBlocks;
import com.au.model.HostelWaiver;
import com.au.response.ResponseHandler;
import com.au.service.FeeReceiptService;
import com.au.service.HostelWaiverService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class HostelWaiverController {
	
	Logger log = LoggerFactory.getLogger(HostelWaiverController.class);
	
	@Autowired
	private FeeReceiptService frc_ser;
	
	@Autowired
	private HostelWaiverService hw_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/hostelWaiverAttachment")
	public ResponseEntity<Object> savehostelwaiverAttachments(@RequestBody @Valid HostelWaiver hw_attachments,@RequestHeader("Authorization") String jwtToken) throws IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			hw_attachments.setCreated_by(jwtDetails.getUserId());
			hw_attachments.setCreated_username(jwtDetails.getUserName());
			HostelWaiver hw = hw_ser.saveAttachments(hw_attachments);
		ResponseEntity<Object> hb_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, hw);
		return hb_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
}
	

	
	@PostMapping(value="/hostelWaiverUploadFile")
	public HostelWaiver uploadFile(@ModelAttribute HostelWaiverAttachmentDto hwfilerequest) throws IOException{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For HostelWaiverAttachment");
		return hw_ser.uploadFile(hwfilerequest.getFile() , hwfilerequest.getHostel_waiver_id() );
		
	}
	
	@GetMapping(path = "/hostelWaiverFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileName") final String pathName) {
		try {
			final byte[] data = hw_ser.downloadFile(pathName);
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
	
	@GetMapping(value="/hostelWaiverAttachment/{hostel_waiver_id}")
	public ResponseEntity<HostelWaiver> get1(@PathVariable Integer hostel_waiver_id ) {
		try {
			HostelWaiver details=hw_ser.get(hostel_waiver_id);
			log.debug("Request {}", hostel_waiver_id);
			return new ResponseEntity<HostelWaiver>(details,HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<HostelWaiver>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/hostelwaiver")
	public ResponseEntity<Object> createHostelWaiver(@RequestBody @Valid HostelWaiver hostelWaiver,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			hostelWaiver.setCreated_by(jwtDetails.getUserId());
			hostelWaiver.setCreated_username(jwtDetails.getUserName());
			HostelWaiver hw = hw_ser.saveHostelWaiver(hostelWaiver);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED,hw);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/gethostelwaiver")
	public List<HostelWaiver> listAll1() {
		return hw_ser.findAll1();
	}
	
	@GetMapping("/fetchAllHostelWaiver")
	public ResponseEntity<Object> getAllHostelBlocksDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> hb_filtered =  hw_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return hb_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> hb_sorted = hw_ser.getAllSortedData(pageable1);
			return hb_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/gethostelwaiver/{hostel_waiver_id}")
	public ResponseEntity<HostelWaiver> get(@PathVariable Integer hostel_waiver_id) {
		
		try {
		HostelWaiver hwaiver = hw_ser.get(hostel_waiver_id);
		return new ResponseEntity<HostelWaiver>(hwaiver, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			return new ResponseEntity<HostelWaiver>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/updatehostelwaiver/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid HostelWaiver r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HostelWaiver existProduct = hw_ser.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			r.setModified_by(jwtDetails.getUserId());
			r.setModified_username(jwtDetails.getUserName());

			hw_ser.saveUpdateHostelWaiver(r);
			ResponseEntity<Object> hb_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return hb_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
		
	@DeleteMapping("/deactivatehostelwaiver/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			hw_ser.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activatehostelwaiver/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			hw_ser.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		 return response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
}	
	
	
	@GetMapping("/getAllDataOfFeeReceiptForFormating1/{student_id}/{ac_year_id}")
	public HashMap<String, Object> getAllDataOfFeeReceiptForFormating1(@PathVariable Integer student_id, @PathVariable Integer ac_year_id){
		return hw_ser.getAllDataOfFeeReceiptForFormating1(student_id , ac_year_id);
	}
	
	@GetMapping("/checkAuidWithTypeIsAlreadyPresentOrNot")
	public ResponseEntity<Object> checkAuidWithTypeIsAlreadyPresentOrNot(
			@RequestParam(value = "student_id") Integer student_id,
			@RequestParam(value = "academic_year_id") Integer academic_year_id,
			@RequestParam(value = "type", required = false) String type) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> status = hw_ser.checkAuidWithTypeIsAlreadyPresentOrNot(student_id, academic_year_id, type);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, status);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}	
}
