package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Graduation;
import com.au.model.LeaveType;
import com.au.model.StudentTranscriptSubmission;
import com.au.repository.StudentTranscriptSubmissionRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class StudentTranscriptSubmissionService {

	@Autowired
	private StudentTranscriptSubmissionRepository s_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> sts_filtered_response = s_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sts_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> sts_sorted_response = s_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sts_sorted_response);
	}
	
	public List<StudentTranscriptSubmission> saveStudentTranscriptSubmission(List<StudentTranscriptSubmission> sts) throws JsonParseException, JsonMappingException, IOException {
	
		 LocalDate currentDate = LocalDate.now();
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		 String dateString = currentDate.format(formatter);
		sts.parallelStream().forEach(sts1 -> {
			if(sts1.getIs_collected() != null && sts1.getIs_collected().equalsIgnoreCase("YES")) {
				sts1.setSubmitted_date(dateString);
				sts1.setCollected_by(sts1.getCreated_by());
			}
		});
		return s_repo.saveAll(sts);
	}
	
	public List<StudentTranscriptSubmission> updateStudentTranscriptSubmission(List<StudentTranscriptSubmission> g) {
		return s_repo.saveAll(g);
	}
	
	public StudentTranscriptSubmission get(Integer id) {
        return s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("StudentTranscriptSubmission Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	StudentTranscriptSubmission ay =s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("StudentTranscriptSubmission  Not Found:"+id));    	
    	s_repo.delete(ay);
    }

	public List<HashMap<String, Object>> getTranscriptDetails(Integer student_id) {
		// TODO Auto-generated method stub
		return s_repo.getTranscriptDetails(student_id);
	}
	
	
}
