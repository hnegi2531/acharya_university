package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.au.model.EnvBillDetails;
import com.au.repository.EnvBillDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class EnvBillDetailsService {

	@Autowired
	private EnvBillDetailsRepository envBillDetailsRepository;
	
	private Logger log = LoggerFactory.getLogger(EnvBillDetailsService.class);
	
	public static final String value = "EnvBillDetails";
	
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
	
	
	public EnvBillDetails saveEnvBillDetails(@Valid EnvBillDetails envBillDetails) {
		return	envBillDetailsRepository.save(envBillDetails);
	}
	
	public List<EnvBillDetails> getAllActiveEnvBillDetails() {
		return envBillDetailsRepository.getAllActiveEnvBillDetails();
	}


	public EnvBillDetails get(Integer id) {
	return envBillDetailsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("EnvBillDetails Not Found:" + id));
	}
	
	public EnvBillDetails updateEnvBillDetails(EnvBillDetails envBillDetails) {
		return envBillDetailsRepository.save(envBillDetails);
	}


	public void deactivate(Integer id) {
		EnvBillDetails ademail = envBillDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EnvBillDetails Not Found:" + id));
		envBillDetailsRepository.deactivate(id);
	}
	
	public void activate(Integer id) {
		EnvBillDetails ademail = envBillDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EnvBillDetails Not Found:" + id));
		envBillDetailsRepository.activate(id);
	}
	
//	
//	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable,Integer created_by, Object keyword) {
//		 Page<Object> oc_filtered_response = envBillDetailsRepository.getAllDataFilteredByKeyword(pageable,created_by, keyword);
//		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
//	}
//
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable,Integer created_by) {
//		 Page<Object> oc_sorted_response = envBillDetailsRepository.getAllSortedData(pageable,created_by);
//		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
//	}
	
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable, LocalDate start, LocalDate end,
			LocalDate minDate,Integer created_by) {
		Date startDate = (start != null) ? java.sql.Date.valueOf(start) : null;
		Date endDate = (end != null) ? java.sql.Date.valueOf(end) : null;
		Date minStartDate = (minDate != null) ? java.sql.Date.valueOf(minDate) : null;

		Page<Object> response = envBillDetailsRepository.getAllSortedData(pageable, startDate, endDate, minStartDate,created_by);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, LocalDate start,
			LocalDate end, LocalDate minDate,Integer created_by) {
		Date startDate = (start != null) ? java.sql.Date.valueOf(start) : null;
		Date endDate = (end != null) ? java.sql.Date.valueOf(end) : null;
		Date minStartDate = (minDate != null) ? java.sql.Date.valueOf(minDate) : null;

		Page<Object> response = envBillDetailsRepository.getAllDataFilteredByKeyword(pageable, keyword, startDate, endDate,
				minStartDate,created_by);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public LocalDate getMinimumPaymentVoucherDate() {
	    Optional<Date> minCreatedDate = envBillDetailsRepository.findMinCreatedDate();
	    return minCreatedDate.map((Date date) -> date.toInstant()
	                                                 .atZone(ZoneId.systemDefault())
	                                                 .toLocalDate())
	                         .orElse(LocalDate.of(2024, 1, 1)); // fallback
	}
	
	public void uploadFile(MultipartFile multipartFile,Integer env_bill_details_id) {


		EnvBillDetails entity = envBillDetailsRepository.findById(env_bill_details_id)
		.orElseThrow(()-> new ResourceNotFoundException("env_bill_details_id not found"));
		try {
			log.debug("Message For envBillDetails Attachment --------------");
			
			
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
		
			log.debug("Cancel admission Attachment", multipartFile);
			
			entity.setAttachment_name(fileName);
			entity.setAttachment_path(LocalDate.now() + "/" + env_bill_details_id + "/" + fileName);
			uploadFileTos3bucket(fileName, file, env_bill_details_id);
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
		envBillDetailsRepository.save(entity);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer env_bill_details_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + env_bill_details_id + "/" + fileName; // file.getName()
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


	public List<Map<String, Object>> getEnvBillDetailsdata() {
		return envBillDetailsRepository.getEnvBillDetailsdata();
	}


	public List<Map<String, Object>> getEnvBillDetailsId(Integer journal_voucher_id, Integer payment_voucher_id) {
		return envBillDetailsRepository.getEnvBillDetailsId(journal_voucher_id,payment_voucher_id);
	}
}
