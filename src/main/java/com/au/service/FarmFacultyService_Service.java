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
import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.model.FarmFacultyService;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.FarmFacultyServiceRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class FarmFacultyService_Service {
	
	@Autowired
	private FarmFacultyServiceRepository ffs_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	private Logger logger = LoggerFactory.getLogger(FarmFacultyService_Service.class);
	
	public static final String value = "FarmFacultyServiceBucket";
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
	
	public FarmFacultyService createFarmFacultyService(FarmFacultyService fts) throws Exception {
		
		return ffs_repo.save(fts);
	}

	public List<FarmFacultyService> listAll() {
		return ffs_repo.findAll1();
	}

	public FarmFacultyService get(Integer facultyServiceId) {
		return ffs_repo.findById(facultyServiceId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmFacultyService Not Found:" + facultyServiceId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = ffs_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = ffs_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public FarmFacultyService updateFarmFacultyService(FarmFacultyService pr) {
		return ffs_repo.save(pr);
	}

	public void delete(Integer facultyServiceId) {
		FarmFacultyService sir = ffs_repo.findById(facultyServiceId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmFacultyService Not Found:" + facultyServiceId));
		ffs_repo.updateFarmFacultyService(facultyServiceId);
	}

	public void delete1(Integer facultyServiceId) {
		FarmFacultyService sir = ffs_repo.findById(facultyServiceId)
				.orElseThrow(() -> new ResourceNotFoundException("FarmFacultyService Not Found:" + facultyServiceId));
		ffs_repo.updateFarmFacultyService1(facultyServiceId);
	}

	public void uploadFile(MultipartFile multipartFile, Integer facultyServiceId) {

		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = LocalDate.now() + "/" + facultyServiceId + "/" + fileName;
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, facultyServiceId);
			file.delete();
			ffs_repo.updatePath(facultyServiceId, t2);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer facultyServiceId) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + facultyServiceId + "/" + fileName; // file.getName()
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
