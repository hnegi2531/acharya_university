package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.au.dto.ResignationAttachmentDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Resignation;
import com.au.model.ResignationAttachment;
import com.au.repository.ResignationAttachmentRepository;

@Service
public class ResignationAttachmentService {
	
	private Logger logger = LoggerFactory.getLogger(ResignationAttachmentService.class);
	public static final String value = "ResignationAttachment";

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
	private ResignationAttachmentRepository ra_repo;
	
	public List<ResignationAttachment> saveResignation(@Valid List<ResignationAttachment> res) throws Exception {
		return ra_repo.saveAll(res);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer resignation_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + resignation_id + "/" + fileName; // file.getName()
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
	
	public void uploadFileResignationAttachment(ResignationAttachmentDto rad) throws IOException{
		rad.getRad().stream().forEach(request -> {
			ResignationAttachment resignation_attachment = new ResignationAttachment();
		try
		{
			logger.debug("Message For resignationAttachment--------------");
			File file = convertMultiPartToFile(request.getFile());
			String fileName = generateFileName(request.getFile());
			resignation_attachment.setResignation_id(rad.getResignation_id());
			resignation_attachment.setEmp_id(rad.getEmp_id());
			resignation_attachment.setAttachment_path(request.getAttachment_path());
			resignation_attachment.setActive(rad.getActive());
			logger.debug("Message For Candidate Attachment", file);
			resignation_attachment.setAttachment_name(request.getAttachment_name());
			resignation_attachment.setAttachment_path(LocalDate.now() + "/" + rad.getResignation_id()+ "/" + fileName);
			resignation_attachment.setAttachment_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + rad.getResignation_id() + "/" + fileName);
			uploadFileTos3bucket(fileName, file,rad.getResignation_id());
			logger.debug("Message For Candidate Attachment", file);
			file.delete();
		}catch (AmazonServiceException ase) {

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
		ra_repo.save(resignation_attachment);
		});
	}	

	public void deactivated(Integer resignation_id) {
		ra_repo.deactivated(resignation_id);
	}
	
	public void activated(Integer id) {
		ra_repo.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Employee Resignation Data Not Found:" + id));
		ra_repo.activated(id);
	}

	public ResignationAttachment get(Integer id) {
		return ra_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Resignation Data Not Found:" + id));
	}

	public Map<String, Object> getResignationAttachmentBasedOnEmployeeId(Integer emp_id) {
		return ra_repo.getResignationAttachmentBasedOnEmployeeId(emp_id);
	}

}
