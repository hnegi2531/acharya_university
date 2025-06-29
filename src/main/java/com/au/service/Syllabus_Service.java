package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.au.model.Syllabus;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.Syllabus_Repository;
import com.au.response.ResponseHandler;

@Service
public class Syllabus_Service {


	private Logger logger = LoggerFactory.getLogger(Syllabus_Service.class);

	public static final String value = "Syllabus";
	
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
	private Syllabus_Repository syllabus_repository;

	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private CourseAssignmentRepository courseAssignmentRepository;

	public List<Syllabus> listAll() {
		return syllabus_repository.findAll1();
	}
	
	public List<Syllabus> getSyllabusByCourseAssignmentId(Integer course_assignment_id) {
		return syllabus_repository.getSyllabusByCourseAssignmentId(course_assignment_id);
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {

		List<Map<String, Object>> response1 = syllabus_repository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {

		List<Map<String, Object>> response = syllabus_repository.findAll3(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	

	public List<Syllabus> get(List<Integer> id) {
		return syllabus_repository.findAllById(id);
				
	}

	public void delete(Integer id) {
		Syllabus s = syllabus_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Syllabus Not Found:" + id));
		syllabus_repository.update(id);
	}

	public void delete1(Integer id) {
		Syllabus s = syllabus_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Syllabus Not Found:" + id));
		syllabus_repository.update1(id);
	}


	public Syllabus save_Syllabuss(@Valid Syllabus syllabus) {
		return syllabus_repository.save(syllabus);
	}

	
	public List<Syllabus> saveSyllabusObjective(@Valid List<Syllabus> s) throws Exception {
		return	syllabus_repository.saveAll(s);
	}
	
	

	public void uploadFile(MultipartFile multipartFile, Integer syllabus_id) {
		Syllabus s = new Syllabus();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = s.setSyllabus_path(LocalDate.now() + "/" + syllabus_id + "/" + fileName);
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, syllabus_id);
			file.delete();
			syllabus_repository.updatePath(syllabus_id, t2);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer syllabus_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + syllabus_id + "/" + fileName; // file.getName()
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

	public List<Syllabus> getSyllabusDetailsData(Integer course_id) {
		return syllabus_repository.getSyllabusDetailsData(course_id);
	}

	public List<Map<String, Object>> getSyllabusDetails(Integer course_assignment_id) {
		return syllabus_repository.getSyllabusDetails(course_assignment_id);
	}

	public List<Syllabus> syllabusByCourseAssignment(Integer courseAsgsignmentId) {
		
		return syllabus_repository.syllabusByCourseAssignment(courseAsgsignmentId);
	}

	public List<Map<String, Object>> syllabusByCourseAssignment(Integer courseId, Integer acYearId) {
		List<Integer> courseAssignmentIds=courseAssignmentRepository.courseAssignmentIdsByCourseAndAcadmicYear(courseId,acYearId);
		return syllabus_repository.getSyllabusDetailsByIds(courseAssignmentIds);
	}
	
	
}
