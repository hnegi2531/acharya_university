package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Date;
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
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeTemplate;
import com.au.model.LeaveType;
import com.au.repository.LeaveTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class LeaveTypeService {
	
	private Logger logger = LoggerFactory.getLogger(FeeTemplateService.class);
	
	public static final String value = "LeaveBucket";
	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@Autowired
	private LeaveTypeRepository s_repo;
	
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	public List<LeaveType> listAll() {
		return s_repo.findAll1();
	}
	
//	public List<LeaveType> listAll1() {
//		return s_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = s_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = s_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public LeaveType saveLeaveType(LeaveType s) throws Exception {
		if(s_repo.countLeaveName(s.getLeave_type()) >=1) {
			throw new Exception("Leave Name Already Exist");
		}else if(s_repo.countLeaveShortName(s.getLeave_type_short()) >=1) {
			throw new Exception("Leave ShortName Already Exist");
		}else if(s.getLeave_type_short().length()==2) {
			return s_repo.save(s);
				
		} else {
			throw new Exception("Short Name should be of two characters");	
		}
	}
	
	public void uploadFile(MultipartFile multipartFile, Integer leave_id) {
		LeaveType leavetype = new LeaveType();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t1 = leavetype.setLeave_type_path(LocalDate.now() + "/" + leave_id + "/" + fileName);
			uploadFileTos3bucket(fileName, file, leave_id);
			file.delete();
			s_repo.updatePath(leave_id, t1);
		} catch (AmazonServiceException ase) {

			logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			logger.info("IOE Error Message: " + ioe.getMessage());

		}

	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file, Integer leave_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + leave_id + "/" + fileName; // file.getName()
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
		// .withCannedAcl(CannedAccessControlList.PublicRead);
	}

	public byte[] viewFiles(String fileName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + fileName);
			final S3ObjectInputStream stream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(stream);
			System.out.println(content);
			s3Object.close();
			return content;

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == 404) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}

	}
	
	public LeaveType saveUpdateLeaveType(LeaveType s) throws Exception {
		return s_repo.save(s);
	}

	public LeaveType get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("LeaveType Not Found:" + id));
	}

	public void delete(Integer id) {
		LeaveType cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeaveType Not Found:" + id));
		s_repo.updateLeaveType(id);
	}

	public void delete1(Integer id) {
		LeaveType cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeaveType Not Found:" + id));
		s_repo.updateLeaveType1(id);

	}

	public List<LeaveType> getHolidayTypeLeaves() {
		return s_repo.fetchHolidayTypeLeaves();
	}

	public List<LeaveType> getLeaveTypeForAttendence() {
		return s_repo.getLeaveTypeForAttendence();
	}
	
	public List<LeaveType> getLeaveTypeForLeaveAndAttendence() {
		return s_repo.getLeaveTypeForLeaveAndAttendence();
	}
	
	public Integer getLeaveIdOfVacationLeave(String leaveTypeShort) {
		return s_repo.getLeaveIdOfVacationLeave(leaveTypeShort);
	}
}
