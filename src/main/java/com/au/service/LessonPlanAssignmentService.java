package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

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
import com.au.dto.CSVHelperDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.LessonPlan;
import com.au.model.LessonPlanAssignment;
import com.au.repository.LessonPlanAssignmentRepository;
import com.au.response.ResponseHandler;

@Service
public class LessonPlanAssignmentService {
	
	Logger log = LoggerFactory.getLogger(LessonPlanAssignmentService.class);
	
	public static final String value = "LessonPlan";
	
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
	private LessonPlanAssignmentRepository a_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private CSVHelperDto cvshelper;
	

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = a_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		Page<Object> response = a_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}
	
	public List<LessonPlanAssignment> savLessonPlanAssignment(@Valid List<LessonPlanAssignment> bs) {
		return a_repo.saveAll(bs);
	}
	
	public LessonPlanAssignment updateLessonPlanAssignment(LessonPlanAssignment s) {
		return a_repo.save(s);
	}
	
	public LessonPlanAssignment get(Integer id) {
        return a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("LessonPlanAssignment ID Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	LessonPlanAssignment ay = a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("LessonPlanAssignment ID Not Found:"+id));    	
    	a_repo.delete(ay);
    }

    public List<LessonPlanAssignment> getDataFromFile(MultipartFile file, String jwtToken) 
			throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<LessonPlanAssignment> data = new ArrayList<LessonPlanAssignment>();
		if (CSVHelperDto.hasCSVFormat(file)) {
			data = 	readFile(file,jwtDetails.getUserId(),jwtDetails.getUserName());
		}
		return data;
    }
    
    public List<LessonPlanAssignment> readFile(MultipartFile file, Integer user_id, String username) throws Exception {
		
		List<LessonPlanAssignment> lessonPlanAss = cvshelper.csvToTutorials(file.getInputStream(),user_id);
		lessonPlanAss.stream().forEach(lesson->{
			
			lesson.setCreated_by(user_id);
			lesson.setCreated_username(username);
			lesson.setActive(true);
		});
		return lessonPlanAss;

    } 
    

	public void uploadFile(MultipartFile multipartFile, Integer lesson_assignment_id,String jwtToken) throws IOException {

		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			LessonPlanAssignment lessonPlan = get(lesson_assignment_id);
			log.debug("Message For Lesson Plan Attachment--------------");
			File file = convertMultiPartToFile1(multipartFile);
			String fileName = generateFileName1(multipartFile);

			log.debug("Message For Lesson Plan Attachment", file);
			lessonPlan.setAttachment_path(LocalDate.now() + "/" + lesson_assignment_id + "/" + fileName);
			uploadFileToS3Bucket1(fileName, file, lesson_assignment_id);
			log.debug("Message For Lesson Plan Attachment", file);
			file.delete();
			a_repo.save(lessonPlan);
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
	}
	
	private File convertMultiPartToFile1(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;
		
	}
	
	private String generateFileName1(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "");
	}
	
	private void uploadFileToS3Bucket1(String fileName, File file, Integer lesson_assignment_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + lesson_assignment_id + "/" + fileName; 
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
		
	}

	public byte[] fileDownloadOfLessonPlanAttachment(final String keyName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
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

	public byte[] downloadFile(final String keyName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
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

}
