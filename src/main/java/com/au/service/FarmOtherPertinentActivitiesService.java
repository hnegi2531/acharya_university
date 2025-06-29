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
import com.au.model.FarmOtherPertinentActivities;
import com.au.model.Notifications;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.FarmOtherPertinentActivitiesRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class FarmOtherPertinentActivitiesService {
	
	@Autowired
	private FarmOtherPertinentActivitiesRepository op_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	private Logger logger = LoggerFactory.getLogger(FarmOtherPertinentActivitiesService.class);
	
	public static final String value = "FarmOtherPertinentActivitiesBucket";
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
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	
	public List<FarmOtherPertinentActivities> createFarmOtherPertinentActivities(List<FarmOtherPertinentActivities> fts) throws Exception {

		return op_repo.saveAll(fts);
	}

	public List<FarmOtherPertinentActivities> listAll() {
		return op_repo.findAll1();
	}

	public FarmOtherPertinentActivities get(Integer otherPertinentId) {
		return op_repo.findById(otherPertinentId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmOtherPertinentActivities Not Found:" + otherPertinentId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = op_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = op_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public FarmOtherPertinentActivities updateFarmOtherPertinentActivities(FarmOtherPertinentActivities pr) {
		return op_repo.save(pr);
	}

	public void delete(Integer otherPertinentId) {
		FarmOtherPertinentActivities sir = op_repo.findById(otherPertinentId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmOtherPertinentActivities Not Found:" + otherPertinentId));
		op_repo.updateFarmOtherPertinentActivities(otherPertinentId);
	}

	public void delete1(Integer otherPertinentId) {
		FarmOtherPertinentActivities sir = op_repo.findById(otherPertinentId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmOtherPertinentActivities Not Found:" + otherPertinentId));
		op_repo.updateFarmOtherPertinentActivities1(otherPertinentId);
	}

	public void uploadFile(MultipartFile multipartFile, List<Integer> otherPertinentId) {
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = LocalDate.now() + "/" + otherPertinentId.get(0) + "/" + fileName;
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, otherPertinentId.get(0));
			file.delete();
			op_repo.updatePath(otherPertinentId, t2);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer otherPertinentId) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + otherPertinentId + "/" + fileName; // file.getName()
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
