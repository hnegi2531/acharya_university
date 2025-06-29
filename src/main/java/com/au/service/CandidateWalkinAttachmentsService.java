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
import com.au.dto.CandidateWalkinAttachmentsDto;
import com.au.model.CandidateWalkinAttachments;
import com.au.model.Candidate_Walkin;
import com.au.repository.CandidateWalkinAttachmentsRepository;
import com.au.repository.CandidateWalkinRepository;



@Service
public class CandidateWalkinAttachmentsService {
	
	private Logger logger = LoggerFactory.getLogger(CandidateWalkinAttachmentsService.class);
	
	public static final String value = "CandidateWalkinBucket";
	
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
	private CandidateWalkinAttachmentsRepository candidate_attachments_repo;
	
	@Autowired
	private CandidateWalkinRepository can_repo;
	
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
//	public CandidateWalkinAttachments uploadFile(MultipartFile multipartFile, Integer candidate_id,String attachment_purpose,Boolean active) throws IOException{
	public void uploadFile(CandidateWalkinAttachmentsDto cwad) throws IOException{
		cwad.getCwar().stream().forEach(request -> {
		CandidateWalkinAttachments candidate_attachment;
		if(candidate_attachments_repo.checkingOfCandidateAttachment(cwad.getCandidate_id(),request.getAttachment_purpose())==1) {
			candidate_attachment=candidate_attachments_repo.getCandidateAttachment(cwad.getCandidate_id(),request.getAttachment_purpose());
		}else {
			candidate_attachment = new CandidateWalkinAttachments();
		}
		try
		{
			logger.debug("Message For VendorAttachment--------------");
			File file = convertMultiPartToFile(request.getFile());
			String fileName = generateFileName(request.getFile());
			candidate_attachment.setCandidate_id(cwad.getCandidate_id());
			candidate_attachment.setAttachment_purpose(request.getAttachment_purpose());
			candidate_attachment.setActive(cwad.getActive());
			logger.debug("Message For Candidate Attachment", file);
			candidate_attachment.setAttachment_file_name(fileName);
			candidate_attachment.setAttachment_path(LocalDate.now() + "/" + cwad.getCandidate_id()+ "/" + fileName);
			candidate_attachment.setAttachment_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + cwad.getCandidate_id() + "/" + fileName);
			uploadFileToS3Bucket(fileName, file, cwad.getCandidate_id());
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
			candidate_attachments_repo.save(candidate_attachment);
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
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "");
	}
	
	private void uploadFileToS3Bucket(String fileName, File file, Integer candidate_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + candidate_id + "/" + fileName; 
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
	
	public List<CandidateWalkinAttachments> get(Integer candidate_id){
		return candidate_attachments_repo.get(candidate_id);
	}
	
	public Map<String,Object> candidatePhotoAttachmentDetails(String application_no_npf){
		Map<String,Object> final_response=new HashMap<String,Object>();
		Candidate_Walkin candidate_walkin = can_repo.candidateWalkinDetailOnApplicationNpf(application_no_npf);
		List<CandidateWalkinAttachments> candidate_walkin_attachment= candidate_attachments_repo.candidatePhotoAttachmentDetails(candidate_walkin.getCandidate_id());
		final_response.put("candiadte_walkin_details",candidate_walkin);
		final_response.put("candidate_walkin_attachment",candidate_walkin_attachment);
		return final_response;
	}
	
	public void uploadCandidateAttachmentFromProfile(CandidateWalkinAttachmentsDto cwad) throws IOException{
		cwad.getCwar().stream().forEach(request -> {
		CandidateWalkinAttachments candidate_attachment = new CandidateWalkinAttachments();
		try
		{
			logger.debug("Message For VendorAttachment--------------");
			File file = convertMultiPartToFile(request.getFile());
			String fileName = generateFileName(request.getFile());
			candidate_attachment.setCandidate_id(cwad.getCandidate_id());
			candidate_attachment.setAttachment_purpose(request.getAttachment_purpose());
			candidate_attachment.setActive(cwad.getActive());
			logger.debug("Message For Candidate Attachment", file);
			candidate_attachment.setAttachment_file_name(fileName);
			candidate_attachment.setAttachment_path(LocalDate.now() + "/" + cwad.getCandidate_id()+ "/" + fileName);
			candidate_attachment.setAttachment_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + cwad.getCandidate_id() + "/" + fileName);
			uploadFileToS3Bucket(fileName, file, cwad.getCandidate_id());
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
			candidate_attachments_repo.save(candidate_attachment);
		});
	}
 
}
