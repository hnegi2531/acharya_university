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
import com.au.model.Publications;
import com.au.model.StudentNoDue;
import com.au.repository.PublicationsRepository;
import com.au.repository.StudentNoDueRepository;
import com.au.response.ResponseHandler;

@Service
public class StudentNoDueService {


	@Autowired
	private StudentNoDueRepository studentNoDueRepository;
	
	private Logger log = LoggerFactory.getLogger(PublicationsService.class);
	
	
	public static final String value = "StudentNoDue";
	
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
	
	
	public List<StudentNoDue> saveStudentNoDue(@Valid List<StudentNoDue> studentNoDue) {
		return	studentNoDueRepository.saveAll(studentNoDue);
	}
	
	
	public List<StudentNoDue> getAllActiveStudentNoDue() {
		return studentNoDueRepository.getAllActiveStudentNoDue();
	}


	public StudentNoDue get(Integer id) {
	return studentNoDueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publication Not Found:" + id));
	}
	
	
	public StudentNoDue updateStudentNoDue(StudentNoDue studentNoDue) {
		return studentNoDueRepository.save(studentNoDue);
	}


	public void deactivate(Integer id) {
		StudentNoDue studentNoDue = studentNoDueRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("studentNoDue Not Found:" + id));
		studentNoDueRepository.deactivate(id);
	}
	
	public void activate(Integer id) {
		StudentNoDue studentNoDue = studentNoDueRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("studentNoDue Not Found:" + id));
		studentNoDueRepository.activate(id);
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable,Integer school_id, Integer program_id, Integer program_specialization_id, Integer current_sem,Object keyword) {
		Page<Object> oc_filtered_response = studentNoDueRepository.getAllDataFilteredByKeyword(pageable,school_id,program_id,program_specialization_id,current_sem, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer school_id, Integer program_id, Integer program_specialization_id, Integer current_sem) {
		Page<Object> oc_sorted_response = studentNoDueRepository.getAllSortedData(pageable,school_id,program_id,program_specialization_id,current_sem);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}
	
	
	public void uploadFile(MultipartFile multipartFile, Integer student_id) {
	    List<Integer> studentNoDueIds = studentNoDueRepository.getStudentNoDueId(student_id);

	    for (Integer studentNoDueId : studentNoDueIds) {
	        StudentNoDue entity = studentNoDueRepository.findById(studentNoDueId)
	            .orElseThrow(() -> new ResourceNotFoundException("StudentNoDue not found for ID: " + studentNoDueId));

	        try {
	            log.debug("Message For Cancel admission Attachment --------------");
	            
	            File file = convertMultiPartToFile(multipartFile);
	            String fileName = generateFileName(multipartFile);
	        
	            log.debug("StudentNoDue Attachment", multipartFile);
	            
	            entity.setAttachment_name(fileName);
	            entity.setAttachment_path(LocalDate.now() + "/" + student_id + "/" + fileName);
	            uploadFileTos3bucket(fileName, file, student_id);
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
	        studentNoDueRepository.save(entity);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer student_id) {
	    final String uniqueFileName = value + "/" + LocalDate.now() + "/" + student_id + "/" + fileName; 
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



	
	
}
