package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.StudentIdCardBucketDto;
import com.au.model.StudentIdCardBucket;
import com.au.model.Student_Details;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentIdCardBucketRepository;
import com.au.repository.StudentIdCardHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class StudentIdCardBucketService {
	
	@Autowired
	private StudentIdCardBucketRepository studentIdCardBucketRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private StudentIdCardHistoryRepository studentIdCardHistoryRepository;
	
	@Autowired
	private JwtTokenService jwtService;
	
	private final ModelMapper modelMapper = new ModelMapper();

	public ResponseEntity<Object> studentIdCardBucket(@Valid List<StudentIdCardBucketDto> studentIdCardBucketDto, String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);

			studentIdCardBucketDto.parallelStream().forEachOrdered(sidcbd -> {
					StudentIdCardBucket sich = modelMapper.map(sidcbd, StudentIdCardBucket.class);
					Optional<Student_Details> studentDetail = studentDetailsRepository.findById(sidcbd.getStudentId());
					sich.setValid_till(sidcbd.getValidTill());
					sich.setCurrent_year(sidcbd.getCurrentYear());
					sich.setCreated_by(jwtDetails.getUserId());
					sich.setCreated_username(jwtDetails.getUserName());
					sich.setStudent_id(studentDetail.get());
					sich.setStudentIdCardHistory(studentIdCardHistoryRepository.findById(sidcbd.getStudentIdCardHistoryId()).get());
					studentIdCardBucketRepository.save(sich);
					studentDetailsRepository.updateIdCardBucketStatus(sidcbd.getStudentId());
			});
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Created Successfully");
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
	}

	public List<HashMap<String, Object>> studentIdCardBucketDetails() {
		
		return studentIdCardBucketRepository.studentIdCardBucketDetails();
	}

	public void removeStudentDetailsFromBucket(List<Integer> studentIdCardBucketIds) {
		studentIdCardBucketRepository.deactivateStudentDetailsFromBucket(studentIdCardBucketIds);
		
	}

}
