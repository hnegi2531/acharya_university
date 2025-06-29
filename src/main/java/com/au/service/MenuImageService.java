package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
import com.au.repository.MenuRepository;

@Service
public class MenuImageService {
	
	
	private Logger logger = LoggerFactory.getLogger(MenuImageService.class);

	public static final String value = "MenuBucket";
	
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
	
	@Autowired
	private MenuRepository menu_repo;
	
	
		public void uploadImage(MultipartFile multipartFile, List<Integer> menu_id) throws IOException{
		
		menu_id.stream().forEach(r -> {
			try
			{
				logger.debug("Message For MenuImage--------------");
				File file = convertMultiPartToFile(multipartFile);
				String fileName = generateFileName(multipartFile);
				//menu_image.setMenu_id(menu_id);
				logger.debug("Message For MenuImage", file);
				System.out.println(menu_id);
//				menu_repo.setMenu_image_file_name(fileName,r);
				String menu_image_path =LocalDate.now() + "/" + r + "/" + fileName;
//				menu_repo.setMenu_image_path(menu_image_path,r);
				String menu_image_type =endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + r + "/" + fileName;
//				menu_repo.setMenu_image_type(menu_image_type,r);
				uploadFileToS3Bucket(fileName, file, r);
				logger.debug("Message For MenuImage", file);
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
		});
		//return menuimage_repo.save(menu_image);
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
	
	
	private void uploadFileToS3Bucket(String fileName, File file, Integer menu_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + menu_id + "/" + fileName; 
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
	
	//	public void delete(Integer menu_image_id) {
	//		MenuImage mimage = menuimage_repo.findById(menu_image_id).orElseThrow(() -> new ResourceNotFoundException("Menu Image Id Not Found:" + menu_image_id));
	//		menuimage_repo.delete(menu_image_id);
	//	
	//	}

}
