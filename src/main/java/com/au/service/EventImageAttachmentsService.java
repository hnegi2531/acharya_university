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
import org.springframework.http.HttpStatus;
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
import com.au.dto.JwtDetails;
import com.au.model.EmployeeDetails;
import com.au.model.EventImageAttachments;
import com.au.repository.EventImageAttachmentsRepository;

@Service
public class EventImageAttachmentsService {
	
	Logger log = LoggerFactory.getLogger(EventImageAttachmentsService.class);
	
	public static final String value = "EventImageBucket";
	
	
	@Autowired
	private EventImageAttachmentsRepository event_image_attch_repo;
	
	private AmazonS3 s3client;
	
	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	public void uploadFile(List<MultipartFile> file, Integer event_id,String image_upload_timing,JwtDetails jwtDetails,Boolean active) throws IOException{


		file.stream().forEach(file1 ->{
			EventImageAttachments event_image_attch = new EventImageAttachments();
			try
			{
				log.debug("Message For Employee Details Attachment --------------");
				File file2 = convertMultiPartToFile(file1);
				String fileName = generateFileName(file1);
				log.debug("Event Images Attachment", file2);
				System.out.println(event_id);
				event_image_attch.setEvent_id(event_id);
				event_image_attch.setActive(active);
				event_image_attch.setCreated_by(jwtDetails.getUserId());
				event_image_attch.setCreated_username(jwtDetails.getUserName());
				event_image_attch.setImage_upload_timing(image_upload_timing);
				event_image_attch.setEvent_image_path(LocalDate.now() + "/" + event_id + "/" + fileName);
				event_image_attch.setEvent_image_type(
						endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + event_id + "/" + fileName);
				uploadFileToS3Bucket(fileName, file2, event_id);
				log.debug("Message For Attachment", file2);
				file2.delete();
			}catch (AmazonServiceException ase) {

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
			event_image_attch_repo.save(event_image_attch);
		});
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

	private void uploadFileToS3Bucket(String fileName, File file, Integer event_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + event_id + "/" + fileName; 
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
			if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}
	
	public List<EventImageAttachments> eventImageAttachmentsDetails(Integer event_id) {
		return event_image_attch_repo.eventImageAttachmentsDetails(event_id);
	}
}
