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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ForeignRegionalRegistrationOfficesDetails;
import com.au.repository.ForeignRegionalRegistrationOfficesDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class ForeignRegionalRegistrationOfficesDetailsService {
	
	private Logger logger = LoggerFactory.getLogger(ForeignRegionalRegistrationOfficesDetailsService.class);
	
	@Autowired
	private ForeignRegionalRegistrationOfficesDetailsRepository frrod_repo;
	
	public static final String value = "FrroDocumentsBucket";
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
	
	
	public ForeignRegionalRegistrationOfficesDetails saveForeignRegionalRegistrationOfficesDetails(ForeignRegionalRegistrationOfficesDetails frrod){
		return frrod_repo.save(frrod);
	}
	
	public List<ForeignRegionalRegistrationOfficesDetails> listAll(){
		return frrod_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> frrod_details_filtered_response = frrod_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, frrod_details_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> frrod_details_sorted_response = frrod_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, frrod_details_sorted_response);
	}
	
	public ForeignRegionalRegistrationOfficesDetails get(Integer id) {
		return frrod_repo.findById(id)
		    	.orElseThrow(()-> new ResourceNotFoundException("Foreign Regional Registration Offices Details Not Found: "+id)); 

    }
	
	
	public ForeignRegionalRegistrationOfficesDetails updateForeignRegionalRegistrationOfficesDetails(ForeignRegionalRegistrationOfficesDetails frrod) {
		return frrod_repo.save(frrod);
		
	}
	
	public void delete(Integer id) {
		frrod_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("Foreign Regional Registration Offices Details Not Found:"+id));    	
		frrod_repo.deactivate(id);
    }
    
    public void delete1(Integer id) {
    	frrod_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("Foreign Regional Registration Offices Details Not Found:"+id));    	
    	frrod_repo.activate(id);
    }
    
public ForeignRegionalRegistrationOfficesDetails uploadFile(MultipartFile passport_copy_document,MultipartFile visa_copy_document,
		MultipartFile residential_permit_copy_document,MultipartFile aiu_equivalence_document,
		Integer frrod_id,Integer student_id) throws IOException{
		
		ForeignRegionalRegistrationOfficesDetails frro_details = get(frrod_id);
		try
		{
			logger.debug("Message For  Foreign Regional Registration Offices Attachment--------------");
			File passport_copy_document_file = convertMultiPartToFile(passport_copy_document);
			File visa_copy_document_file = convertMultiPartToFile(visa_copy_document);
			File residential_permit_copy_document_file = convertMultiPartToFile(residential_permit_copy_document);
			File aiu_equivalence_document_file = convertMultiPartToFile(aiu_equivalence_document);
			
			String passport_copy_document_fileName = generateFileName(passport_copy_document);
			String visa_copy_document_fileName = generateFileName(visa_copy_document);
			String residential_permit_copy_document_fileName = generateFileName(residential_permit_copy_document);
			String aiu_equivalence_document_fileName = generateFileName(aiu_equivalence_document);
			
			logger.debug("Message For  Foreign Regional Registration Offices Attachment ", passport_copy_document_file);
			logger.debug("Message For  Foreign Regional Registration Offices Attachment ", visa_copy_document_file);
			logger.debug("Message For  Foreign Regional Registration Offices Attachment ", residential_permit_copy_document_file);
			logger.debug("Message For  Foreign Regional Registration Offices Attachment ", aiu_equivalence_document_file);
			
			frro_details.setPassport_copy_document_path(LocalDate.now() + "/" + student_id + "/" + passport_copy_document_fileName);
			frro_details.setVisa_copy_document_path(LocalDate.now() + "/" + student_id + "/" + visa_copy_document_fileName);
			frro_details.setResidential_permit_copy_document_path(LocalDate.now() + "/" + student_id + "/" + residential_permit_copy_document_fileName);
			frro_details.setAiu_equivalence_document_path(LocalDate.now() + "/" + student_id + "/" + aiu_equivalence_document_fileName);
			
			frro_details.setPassport_copy_document_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + student_id + "/" + passport_copy_document_fileName);
			frro_details.setPassport_copy_document_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + student_id + "/" + visa_copy_document_fileName);
			frro_details.setPassport_copy_document_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + student_id + "/" + residential_permit_copy_document_fileName);
			frro_details.setPassport_copy_document_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + student_id + "/" + aiu_equivalence_document_fileName);
			
			uploadFileToS3Bucket(passport_copy_document_fileName, passport_copy_document_file, student_id);
			uploadFileToS3Bucket(visa_copy_document_fileName, visa_copy_document_file, student_id);
			uploadFileToS3Bucket(residential_permit_copy_document_fileName,residential_permit_copy_document_file, student_id);
			uploadFileToS3Bucket(aiu_equivalence_document_fileName,aiu_equivalence_document_file, student_id);
			
			passport_copy_document_file.delete();
			visa_copy_document_file.delete();
			residential_permit_copy_document_file.delete();
			aiu_equivalence_document_file.delete();
			
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
		return frrod_repo.save(frro_details);
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
	
	private void uploadFileToS3Bucket(String fileName, File file, Integer student_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + student_id + "/" + fileName; 
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
}
