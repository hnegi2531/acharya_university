package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;

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

import com.au.dto.CancelHostelBedAssignmentFileRequest;
import com.au.dto.HostelBedAssignmentDto;
import com.au.dto.HostelIdCardDto;
import com.au.response.ResponseHandler;
import com.au.service.HostelBedAssignmentService;

@RestController
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class HostelBedAssignmentController {
	
	Logger log=LoggerFactory.getLogger(HostelBedAssignmentController.class);
	
	@Autowired
	private HostelBedAssignmentService hostelBedAssignmentService;
	
	@PostMapping("/hostelBedAssignment")
	public ResponseEntity<Object> hostelBedAssignment(
			@RequestBody @Valid HostelBedAssignmentDto hostelBedAssignmentDto,
			@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.hostelBedAssignment(hostelBedAssignmentDto, jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/fetchAllHostelBedAssignment")
	public ResponseEntity<Object> fetchAllHostelBedAssignment(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword,
			@RequestParam(value = "active", required = false) Boolean active,
			@RequestParam(value="cancelledStatus",required = false) String cancelledStatus,
			@RequestParam(value="schoolId",required = false) Integer schoolId,
			@RequestParam(value="acYearId",required = false) Integer acYearId,
			@RequestParam(value="blockId",required = false) Integer blockId) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, pageSize,sorted);
					return hostelBedAssignmentService.filteredAndSortedResponses(pageable, keyword,active,cancelledStatus,schoolId,acYearId,blockId);
				}else {
					Pageable pageable1 = PageRequest.of(page, pageSize,sorted);
					return hostelBedAssignmentService.sortedResponses(pageable1,active,cancelledStatus,schoolId,acYearId,blockId);
				}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}	
	}
	
	@PutMapping("/updateHostelBedAssignment/{hostelBedAssignmentId}")
	public ResponseEntity<Object> updateHostelBedAssignment(
			@RequestBody @Valid HostelBedAssignmentDto hostelBedAssignmentDto,@PathVariable Integer hostelBedAssignmentId,
			@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.updateHostelBedAssignment(hostelBedAssignmentDto,hostelBedAssignmentId, jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@PutMapping("/updateHostelIdCard/{hostelBedAssignmentId}")
	public ResponseEntity<Object> updateHostelIdCard(
			@RequestBody @Valid List<HostelIdCardDto> hostelIdCardDto,@PathVariable List<Integer> hostelBedAssignmentId,
			@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.updateHostelIdCard(hostelIdCardDto,hostelBedAssignmentId, jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/hostelBedAssignment/{hostelBedAssignmentId}")
	public ResponseEntity<Object> hostelBedAssignment(@PathVariable Integer hostelBedAssignmentId) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.hostelBedAssignment(hostelBedAssignmentId);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@DeleteMapping("/deactiveHostelBedAssignment/{hostelBedAssignmentId}")
	public ResponseEntity<Object> deactiveHostelBedAssignment(@PathVariable Integer hostelBedAssignmentId) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.deactiveHostelBedAssignment(hostelBedAssignmentId);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@DeleteMapping("/activateHostelBedAssignment/{hostelBedAssignmentId}")
	public ResponseEntity<Object> activateHostelBedAssignment(@PathVariable Integer hostelBedAssignmentId) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.activateHostelBedAssignment(hostelBedAssignmentId);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@PostMapping(value="/cancelHostelBedAssignmentUploadFile")
	public ResponseEntity<Object> uploadFile(
			@ModelAttribute CancelHostelBedAssignmentFileRequest cancelHostelBedAssignmentFileRequest)
			throws IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			log.debug("Message For Cacel Hostel Bed Assigment Attachment");
			return hostelBedAssignmentService.uploadFile(cancelHostelBedAssignmentFileRequest.getFile(),
					cancelHostelBedAssignmentFileRequest.getHostelBedAssignmentId());
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping(path = "/fileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("pathName") final String pathName) {
		try {
			final byte[] data = hostelBedAssignmentService.downloadFile(pathName);
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
	
	@GetMapping("/hostelBedAssignmentForBedChangeHistory/{academicYearId}/{studentId}")
	public ResponseEntity<Object> hostelBedAssignmentForBedChangeHistory(@PathVariable Integer academicYearId, @PathVariable Integer studentId) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.hostelBedAssignmentForBedChangeHistory(academicYearId,studentId);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/cancelhostelBedAssignmentDetails")
	public ResponseEntity<Object> cancelhostelBedAssignmentDetails() {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.cancelhostelBedAssignmentDetails();

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/hostelBedAssignmentByAcYearIdAndStudentId/{academicYearId}/{studentId}")
	public ResponseEntity<Object> hostelBedAssignmentByAcYearIdAndStudentId(@PathVariable Integer academicYearId, @PathVariable Integer studentId) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.hostelBedAssignmentByAcYearIdAndStudentId(academicYearId,studentId);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/hostelBedAssignmentByAcYearAndhostelBlock/{hostelBlockId}")
	public ResponseEntity<Object> hostelBedAssignmentByAcYearAndhostelBlock( @PathVariable Integer hostelBlockId) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.hostelBedAssignmentByAcYearAndhostelBlock(hostelBlockId);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/isHostelBedAssignedByAcademicYearAndStudentId/{academicYearId}/{studentId}")
	public ResponseEntity<Object> isHostelBedAssignedByAcademicYearAndStudentId(@PathVariable Integer academicYearId, @PathVariable Integer studentId) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedAssignmentService.isHostelBedAssignedByAcademicYearAndStudentId(academicYearId,studentId);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	
	@GetMapping("/getStudentDetailDataBasedOnBlock/{acYearId}/{hostelBlockId}")
	public ResponseEntity<Object> getStudentDetailDataBasedOnBlock(@PathVariable Integer acYearId, @PathVariable Integer hostelBlockId){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> all_details_by_course_code = hostelBedAssignmentService.getStudentDetailDataBasedOnBlock(acYearId,hostelBlockId);
			ResponseEntity<Object> all_details_by_course_code_response = ResponseHandler.generateResponse(true, HttpStatus.OK, all_details_by_course_code);
			return all_details_by_course_code_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/getHostelDetailsForLedger/{studentId}")
	public ResponseEntity<Object> getHostelDetailsForLedger(@PathVariable Integer studentId){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> hostelDataForLedger = hostelBedAssignmentService.getHostelDetailsForLedger(studentId);
			ResponseEntity<Object> all_details_by_course_code_response = ResponseHandler.generateResponse(true, HttpStatus.OK, hostelDataForLedger);
			return all_details_by_course_code_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

    @GetMapping("/hostelbedDetailsByBlockAndAcademic")
    public ResponseEntity<Object> hostelbedDetailsByBlockAndAcademic(@RequestParam(value = "academicYearId",required = false) Integer academicYearId){
        if(RateLimitController.bucket.tryConsume(1)) {
            return hostelBedAssignmentService.hostelbedDetailsByBlockAndAcademic(academicYearId);

        }else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
        }
    }
	
	

}
