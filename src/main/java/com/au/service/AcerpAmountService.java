package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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
import com.au.dto.JwtDetails;
import com.au.event.StudentDueEvent;
import com.au.model.AcerpAmount;
import com.au.model.LeaveApply;
import com.au.model.Notifications;
import com.au.model.AcerpAmount;
import com.au.repository.AcerpAmountRepository;
import com.au.response.ResponseHandler;

@Service
public class AcerpAmountService {
	
	@Autowired
	private AcerpAmountRepository twa_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	private Logger logger = LoggerFactory.getLogger(AcerpAmountService.class);
	
	public static final String value = "AcerpAmountBucket";
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
	
	
	public AcerpAmount createAcerpAmount(AcerpAmount acerp, JwtDetails jwtDetails) throws Exception {	
		AcerpAmount acerpAmount=twa_repo.save(acerp);
		StudentDueEvent studentDueEvent=new StudentDueEvent(null,null,acerp.getStudentId(),null);
		applicationEventPublisher.publishEvent(studentDueEvent);
	   return acerpAmount;

	}

	public List<AcerpAmount> listAll() {
		return twa_repo.findAll1();
	}

	public AcerpAmount get(Integer acerpAmountId) {
		return twa_repo.findById(acerpAmountId)
				.orElseThrow(() -> new ResourceNotFoundException("AcerpAmount Not Found:" + acerpAmountId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = twa_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = twa_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public AcerpAmount updateAcerpAmount(AcerpAmount pr) {
		AcerpAmount acerpAmount=twa_repo.save(pr);
		StudentDueEvent studentDueEvent=new StudentDueEvent(null,null,pr.getStudentId(),null);
		applicationEventPublisher.publishEvent(studentDueEvent);
	   return acerpAmount;
	}

	public void delete(Integer acerpAmountId) {
		AcerpAmount sir = twa_repo.findById(acerpAmountId)
				.orElseThrow(() -> new ResourceNotFoundException("AcerpAmount Not Found:" + acerpAmountId));
		twa_repo.updateAcerpAmount(acerpAmountId);
	}

	public void delete1(Integer acerpAmountId) {
		AcerpAmount sir = twa_repo.findById(acerpAmountId)
				.orElseThrow(() -> new ResourceNotFoundException("AcerpAmount Not Found:" + acerpAmountId));
		twa_repo.updateAcerpAmount1(acerpAmountId);
	}

	public ResponseEntity<Object> getAcerpAmountByAuid(String auid, String type) {
		try {
		AcerpAmount acerpAmountByAuid = twa_repo.getAcerpAmountByAuid(auid ,type);
		if(acerpAmountByAuid == null) {
			return ResponseHandler.generateResponse(true, HttpStatus.OK, 
					"Auid not found!", null);
			
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.OK, acerpAmountByAuid);

		}
		} catch (Exception e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, 
						"FAILURE", null);
		}

	}

	public Object updateAcerpAmountByAcerpAmountId(AcerpAmount am,JwtDetails jwtDetails) {

		AcerpAmount dataByStudentId = twa_repo.getDataByStudentId(am.getStudentId(),am.getType());
		
		dataByStudentId.setPaidYear1(ObjectUtils.isNotEmpty(am.getPaidYear1()) ? am.getPaidYear1() : dataByStudentId.getPaidYear1());
		dataByStudentId.setPaidYear2(ObjectUtils.isNotEmpty(am.getPaidYear2()) ? am.getPaidYear2() : dataByStudentId.getPaidYear2());
		dataByStudentId.setPaidYear3(ObjectUtils.isNotEmpty(am.getPaidYear3()) ? am.getPaidYear3() : dataByStudentId.getPaidYear3());
		
		dataByStudentId.setPaidYear4(ObjectUtils.isNotEmpty(am.getPaidYear4()) ? am.getPaidYear4() : dataByStudentId.getPaidYear4());
		dataByStudentId.setPaidYear5(ObjectUtils.isNotEmpty(am.getPaidYear5()) ? am.getPaidYear5() : dataByStudentId.getPaidYear5());
		dataByStudentId.setPaidYear6(ObjectUtils.isNotEmpty(am.getPaidYear6()) ? am.getPaidYear6() : dataByStudentId.getPaidYear6());
		
		dataByStudentId.setPaidYear7(ObjectUtils.isNotEmpty(am.getPaidYear7()) ? am.getPaidYear7() : dataByStudentId.getPaidYear7());
		dataByStudentId.setPaidYear8(ObjectUtils.isNotEmpty(am.getPaidYear8()) ? am.getPaidYear8() : dataByStudentId.getPaidYear8());
		dataByStudentId.setPaidYear9(ObjectUtils.isNotEmpty(am.getPaidYear9()) ? am.getPaidYear9() : dataByStudentId.getPaidYear9());
		
		dataByStudentId.setPaidYear10(ObjectUtils.isNotEmpty(am.getPaidYear10()) ? am.getPaidYear10() : dataByStudentId.getPaidYear10());
		dataByStudentId.setPaidYear11(ObjectUtils.isNotEmpty(am.getPaidYear11()) ? am.getPaidYear11() : dataByStudentId.getPaidYear11());
		dataByStudentId.setPaidYear12(ObjectUtils.isNotEmpty(am.getPaidYear12()) ? am.getPaidYear12() : dataByStudentId.getPaidYear12());
		
		
		dataByStudentId.setRemarks(ObjectUtils.isNotEmpty(am.getRemarks()) ? am.getRemarks() : dataByStudentId.getRemarks());
		dataByStudentId.setModifiedBy(jwtDetails.getUserId());
		dataByStudentId.setModifiedUsername(jwtDetails.getUserName());
		dataByStudentId.setType(ObjectUtils.isNotEmpty(am.getType()) ? am.getType() : dataByStudentId.getType());
		StudentDueEvent studentDueEvent=new StudentDueEvent(null,null,dataByStudentId.getStudentId(),null);
		applicationEventPublisher.publishEvent(studentDueEvent);
	 		
		return twa_repo.save(dataByStudentId);
	}
	
	public void uploadFile(MultipartFile multipartFile, Integer acerpAmountId) {

		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = LocalDate.now() + "/" + acerpAmountId + "/" + fileName;
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, acerpAmountId);
			file.delete();
			twa_repo.updatePath(acerpAmountId, t2);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer acerpAmountId) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + acerpAmountId + "/" + fileName; // file.getName()
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
	
	public String checkAuidWithFeeTypeIsAlreadyPresentOrNot(String auid, String type) {
		if (twa_repo.checkAuidWithFeeTypeIsAlreadyPresentOrNot(auid,type) >= 1) {
			throw new RuntimeException("Auid with fee type already exist !!!");
		}

		return auid;
	}


	

}
