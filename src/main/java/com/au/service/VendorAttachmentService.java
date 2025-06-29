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
import org.apache.http.HttpStatus;
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
import com.au.model.VendorAttachment;
import com.au.repository.VendorAttachmentRepository;

@Service
public class VendorAttachmentService {
	
	private Logger logger = LoggerFactory.getLogger(VendorAttachmentService.class);
	
	public static final String value = "VendorBucket";
	
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
	private VendorAttachmentRepository ven_repo;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	public VendorAttachment saveAttachments(@Valid VendorAttachment vendor_attachments) throws IOException {
		return ven_repo.save(vendor_attachments);
	
	}
	
	public VendorAttachment uploadFile(MultipartFile multipartFile, Integer vendor_id) throws IOException{
		
		VendorAttachment vendor_attachment = new VendorAttachment();
		try
		{
			logger.debug("Message For VendorAttachment--------------");
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			vendor_attachment.setVendor_id(vendor_id);
			logger.debug("Message For VendorAttachment", file);
			System.out.println(vendor_id);
			vendor_attachment.setVendor_attachment_file_name(fileName);
			vendor_attachment.setVendor_attachment_path(LocalDate.now() + "/" + vendor_id + "/" + fileName);
			vendor_attachment.setVendor_attachement_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + vendor_id + "/" + fileName);
			uploadFileToS3Bucket(fileName, file, vendor_id);
			logger.debug("Message For Attachment", file);
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
		return ven_repo.save(vendor_attachment);
	}
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;
		
	}
	
	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}
	
	private void uploadFileToS3Bucket(String fileName, File file, Integer vendor_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + vendor_id + "/" + fileName; 
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
		
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
			if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}
	
	public VendorAttachment get(Integer id) {
		if(id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return ven_repo.findById1(id);
	}

	public List<Map<String, Object>> vendorAttachmentDetails(Integer vendor_id) {
		return ven_repo.vendorAttachmentDetails(vendor_id);
	}
	
	
	
	
	

}
