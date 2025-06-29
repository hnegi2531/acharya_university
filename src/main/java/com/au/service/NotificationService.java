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
import com.au.model.LeaveApply;
import com.au.model.Notifications;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.LeaveApplyRepository;
import com.au.repository.NotificationRepository;
import com.au.response.ResponseHandler;

@Service
public class NotificationService {
	
private Logger logger = LoggerFactory.getLogger(FeeTemplateService.class);
	
	public static final String value = "NotificationBucket";
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
	private NotificationRepository n_repo;
	
	
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	public Notifications saveNotification(Notifications notf) {
		return n_repo.save(notf);
	}
	
	public List<Notifications> listAll1() {
		return n_repo.findAll1();
	}
	
	public List<Notifications> listAll() {
		return n_repo.findAll();
	}
	
	 public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
			Page<Map<String, Object>> Notifications_filtered_response = n_repo.getAllDataFilteredByKeyword(pageable, keyword);
			return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_filtered_response);
		}

		public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
			Page<Map<String, Object>> Notifications_sorted_response = n_repo.getAllSortedData(pageable);
			return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_sorted_response);
		}
		
		
		 public ResponseEntity<Object> fetchAllNotificationsForIndexBasedOnUserByKeyword(Pageable pageable, Object keyword, Integer userId) {
				Page<Map<String, Object>> Notifications_filtered_response = n_repo.fetchAllNotificationsForIndexBasedOnUserByKeyword(pageable, keyword,userId);
				return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_filtered_response);
			}

			public ResponseEntity<Object> fetchAllNotificationsForIndexBasedOnUserData(Pageable pageable, Integer userId) {
				Page<Map<String, Object>> Notifications_sorted_response = n_repo.fetchAllNotificationsForIndexBasedOnUserData(pageable,userId);
				return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_sorted_response);
			}
		
	
	public Notifications get(Integer notification_id) {
		return n_repo.findById(notification_id)
		.orElseThrow(() -> new ResourceNotFoundException("Notifications Not Found:" + notification_id));
	}
	
	public Notifications saveUpdateNotification(Notifications notf) {
		return n_repo.save(notf);
	}
	
	public void delete(Integer notification_id) {
		n_repo.findById(notification_id)
		.orElseThrow(() -> new ResourceNotFoundException("Notifications Id Not Found:" +notification_id));
		n_repo.delete(notification_id);
	}
	
	public void delete1(Integer notification_id)
	{
		n_repo.findById(notification_id)
		.orElseThrow(() -> new ResourceNotFoundException("Notifications Id Not Found:" +notification_id));
		n_repo.delete1(notification_id);
		
	}
	
	public void uploadFile(MultipartFile multipartFile, Integer notification_id) {
		Notifications noti = new Notifications();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = noti.setNotification_attach_path(LocalDate.now() + "/" + notification_id + "/" + fileName);
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, notification_id);
			file.delete();
			n_repo.updatePath(notification_id, t2);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer notification_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + notification_id + "/" + fileName; // file.getName()
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
	
	public List<Notifications> getLatestFiveNotifications() {
		return n_repo.getLatestFiveNotifications();
	}

	public List<Map<String, Object>> getDepartmentWithSchools(List<Integer> school_ids) {
		
		return n_repo.getDepartmentWithSchools(school_ids);
	}

	public Map<String, Object> getCountOfNotification(Integer dept_ids) {
		return n_repo.getCountOfNotification(dept_ids);
	}

	public List<Map<String, Object>> getNotificationDataBasedOnDept(Integer dept_ids) {
		return n_repo.getNotificationDataBasedOnDept(dept_ids);
	}

	public List<Map<String, Object>> getNotificationDataOfToday(Integer dept_ids) {
		return n_repo.getNotificationDataOfToday(dept_ids);
	}
	
}
