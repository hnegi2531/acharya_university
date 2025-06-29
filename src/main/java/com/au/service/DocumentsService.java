package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

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
import com.au.model.Documents;
import com.au.repository.DocumentsRepository;
import com.au.response.ResponseHandler;


@Service
public class DocumentsService {

	
private Logger logger = LoggerFactory.getLogger(DocumentsService.class);
	
	public static final String value = "DocumentsBucket";
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
	private DocumentsRepository docs_repo;
	
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	public Documents saveDocuments(Documents doc) throws Exception {
		return docs_repo.save(doc);
				
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> documents_filtered_response = docs_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, documents_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> documents_sorted_response = docs_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, documents_sorted_response);
	}
	
	public List<Documents> listAll1() {
		return docs_repo.findAll11();
	}

	public Documents get(Integer id) {
		return docs_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Documents Not Found:" + id));
	}

	public void delete(Integer id) {
		Documents doc = docs_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Documents Not Found:" + id));
		docs_repo.updateDocuments(id);
	}

	public void delete1(Integer id) {
		Documents doc = docs_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Documents Not Found:" + id));
		docs_repo.updateDocuments1(id);
	}
	
	
	public void uploadFile(MultipartFile multipartFile, Integer documents_id) {
		Documents doc = new Documents();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = doc.setDocument_attachment_path(LocalDate.now() + "/" + documents_id + "/" + fileName);
			System.out.println("[[[[[[[[[[[[[[[[[[[775]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, documents_id);
			System.out.println("4577098765432");
			file.delete();
			String existing_file_path = docs_repo.getExisting_file_path(documents_id);
			System.out.println("cfhcfthhf");
			if(existing_file_path==null) {
				docs_repo.updatePath(documents_id, t2);
			} else {
				StringJoiner sj = new StringJoiner(",");
			String updated_path =	sj.add(existing_file_path).add(t2).toString();
			docs_repo.updatePath(documents_id, updated_path);
			}
				
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

	private void uploadFileTos3bucket(String fileName, File file, Integer documents_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + documents_id + "/" + fileName; // file.getName()
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
