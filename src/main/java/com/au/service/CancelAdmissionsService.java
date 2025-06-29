package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
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
import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.CancelAdmissionsDto;
import com.au.dto.JwtDetails;
import com.au.model.CancelAddmissions;
import com.au.model.HostelBedAssignment;
import com.au.model.Student_Details;
import com.au.repository.CancelAddmissionsRepository;
import com.au.repository.HostelBedAssignmentRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class CancelAdmissionsService {

private Logger log = LoggerFactory.getLogger(CancelAdmissionsService.class);

	
	public static final String value = "CancelPayment";
	
	private AmazonS3 s3client;
	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon1() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	@Autowired
	private CancelAddmissionsRepository repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private StudentDetailsRepository studentsRepo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private ReportingStudentsRepository r_repo;
	

	@Autowired
	private HostelBedAssignmentRepository hostelBedAssignmentRepository;
	

	
	public ResponseEntity<Object> cancelAdmissions(CancelAdmissionsDto dto, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		CancelAddmissions entity = new CancelAddmissions();
		Integer studentIdByAuid = studentsRepo.getStudentIdByAuid(dto.getAuid());
		HostelBedAssignment checkForHostelCancelledOrNot = hostelBedAssignmentRepository.checkForHostelCancelledOrNot(studentIdByAuid);		
		
		if(checkForHostelCancelledOrNot == null) {		
			createCancelAdmissions(dto, entity, jwtDetails);

		} else {
			if(checkForHostelCancelledOrNot.getCancelledRemarks() != null) {
				createCancelAdmissions(dto, entity, jwtDetails);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "Please cancel the hostel assignment to cancel the admission.", null);
			}		
		}
		
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", entity);
}


	private CancelAddmissions createCancelAdmissions(CancelAdmissionsDto dto, CancelAddmissions entity, JwtDetails jwtDetails) {

			entity.setCreated_by(jwtDetails.getUserId());
			entity.setCreated_username(jwtDetails.getUserName());
			entity.setAuid(dto.getAuid());
			entity.setDept_id(dto.getDept_id());
			entity.setHostel_remarks(dto.getHostel_remarks());
			entity.setRemarks(dto.getRemarks());
			entity.setStudent_name(dto.getStudent_name());
			entity.setActive(dto.getActive());
			entity.setSchool_id(dto.getSchool_id());
			entity.setAttachment_name(dto.getAttachment_name());
			entity.setAttachment_path(dto.getAttachment_path());
			entity.setCreated_date(dto.getCreated_date());
			entity.setModified_by(dto.getModified_by());
			entity.setModified_date(dto.getModified_date());
			entity.setModified_username(dto.getModified_username());
			
			Student_Details student = studentsRepo.findByAuid(dto.getAuid());
			student.setDeassign_status(1);
			
			studentsRepo.save(student);
			return repo.save(entity);
		
		
	}


	public void uploadFile(MultipartFile multipartFile,Integer cancel_id) {


		CancelAddmissions entity = repo.findById(cancel_id)
		.orElseThrow(()-> new ResourceNotFoundException("Cancel admissiond not found"));
		try {
			log.debug("Message For Cancel admission Attachment --------------");
			
			
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
		
			log.debug("Cancel admission Attachment", multipartFile);
			
			entity.setAttachment_name(fileName);
			entity.setAttachment_path(LocalDate.now() + "/" + cancel_id + "/" + fileName);
			uploadFileTos3bucket(fileName, file, cancel_id);
			log.debug("Message For Attachment", file);
			file.delete();
		} catch (AmazonServiceException ase) { 

			log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			log.info("Error Message:    " + ase.getMessage());
			log.info("HTTP Status Code: " + ase.getStatusCode());
			log.info("AWS Error Code:   " + ase.getErrorCode());
			log.info("Error Type:       " + ase.getErrorType());
			log.info("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			log.info("Caught an AmazonClientException: ");
			log.info("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			log.info("IOE Error Message: " + ioe.getMessage());

		}
		repo.save(entity);
	}
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_").replace("&", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file, Integer cancel_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + cancel_id + "/" + fileName; // file.getName()
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
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
	
	public CancelAddmissions confirmCancelAdmissions (CancelAddmissions ca) 
			throws JsonParseException, JsonMappingException, IOException {
		
		Student_Details student = studentsRepo.findByAuid(ca.getAuid());
		
		if(ca.getApproved_date() != null) {
			student.setDeassign_status(2);
			student.setActive(false);
			r_repo.deactivateReporitngStudentTable(student.getStudent_id());
			
		} else {
			student.setDeassign_status(null);
		}
		studentsRepo.save(student);
		repo.save(ca);
		
		return ca;
	}

	public CancelAddmissions updateApprovedByAndDate(CancelAddmissions ca) {
		return repo.save(ca);
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}


	public ResponseEntity<Object> listAll2(Pageable pageable1) {
		Page<Object> response = repo.findAll3(pageable1);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public CancelAddmissions get(Integer cancel_id) {
		return repo.findById(cancel_id).orElseThrow(() -> new ResourceNotFoundException("cancel_id Not Found:" + cancel_id));
	}


	public ResponseEntity<Object> listAllReport(Pageable pageable, Object keyword) {
		Page<Object> response1 = repo.listAllReport(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}


	public ResponseEntity<Object> listAllReport1(Pageable pageable1) {
		Page<Object> response = repo.listAllReport1(pageable1);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	
	public ResponseEntity<Object> getStudentIndex(Pageable pageable1, Integer ac_year_id ){
		Page<Object> response1 =   studentsRepo.getStudentInactiveIndex1(pageable1,ac_year_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getStudentIndex2(Pageable pageable, Object keyword, Integer ac_year_id) {
		Page<Object> response =  studentsRepo.getStudentInactiveIndex2(pageable,keyword,ac_year_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public List<HashMap<String, Object>> Student_DetailsWithCancelAdmissionDetailsData(Integer student_id) {
		return studentsRepo.Student_DetailsWithCancelAdmissionDetailsData(student_id);
	}		
}
