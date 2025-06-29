package com.au.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import com.au.dto.FileRequestDto;
import com.au.dto.JwtDetails;
import com.au.model.ScholarshipAttachment;
import com.au.model.VendorAttachment;
import com.au.repository.ScholarshipAttachmentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AmazonClientService { 

	//private static final Logger LOGGER = LoggerFactory.getLogger(AmazonClientService.class);
	
	private AmazonS3 s3client;
	
	public static final String value = "scholarship_attachment";
	
	public static final String value1 = "AcharyaYoutubeVideo";
	
	@Autowired
	private ScholarshipAttachmentRepository s_repo;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	private Logger logger = LoggerFactory.getLogger(AmazonClientService.class);
	
	public boolean FileExists(String file_path)
    {
		File tempFile = new File(bucketName,file_path);
		boolean exists = tempFile.exists();
		if(exists) {
			return true;
		}
		else
		{
			return false;
		}	
    }

	public Collection<String> loadFileFromS3(String file_path) throws IOException{
		
		System.out.println(file_path);
		try (
	    		final S3Object s3Object = s3client.getObject(bucketName,file_path
	    	);
				
	        final InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8);
	    		final BufferedReader reader = new BufferedReader(streamReader)) {
	    	
	        return reader.lines().collect(Collectors.toSet());
	    } catch (final IOException e) {
	        logger.error(e.getMessage(), e);
	        return Collections.emptySet();
	    }
	}
	
	
	
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

//	public FileRequestDto uploadFile(MultipartFile multipartFile) throws IOException {
//		
//		String PATH = "scholarship_attachment";
//
//	    File folder = new File(PATH);
//	    folder.mkdir();
//	    if (!folder.exists()){
//	    	folder.mkdir();
//	        // If you require it to make the entire directory path including parents,
//	        // use directory.mkdirs(); here instead.
//	    }
//	    	    
//		FileRequestDto filerequestdto = new FileRequestDto();
//		try {
//		File file = convertMultiPartToFile(multipartFile);
//		System.out.println("==="+file);
//		String fileName = generateFileName(multipartFile);
//		System.out.println("================="+fileName);
//		filerequestdto.setBucketName(bucketName);
//		filerequestdto.setFileName(fileName);
//		//filerequestdto.setPath(bucketName + "/" + fileName);
//		filerequestdto.setPath(folder+"/"+LocalDate.now()+"/"+ fileName);
//	//	filerequestdto.setEndpointUrl(endpointUrl + "/" + bucketName + "/" +folder+"/"+"123"+"/"+ LocalDate.now()+"/"+ fileName);
//		filerequestdto.setEndpointUrl(endpointUrl + "/" + bucketName + "/" +folder+"/"+ LocalDate.now()+"/"+ fileName);
//
//		uploadFileTos3bucket(folder,fileName, file);
//		file.delete();
//		} catch (AmazonServiceException ase) {
//		logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
//		logger.info("Error Message:    " + ase.getMessage());
//		logger.info("HTTP Status Code: " + ase.getStatusCode());
//		logger.info("AWS Error Code:   " + ase.getErrorCode());
//		logger.info("Error Type:       " + ase.getErrorType());
//		logger.info("Request ID:       " + ase.getRequestId());
//
//		} catch (AmazonClientException ace) {
//		logger.info("Caught an AmazonClientException: ");
//		logger.info("Error Message: " + ace.getMessage());
//		} catch (IOException ioe) {
//		logger.info("IOE Error Message: " + ioe.getMessage());
//		}
//		return filerequestdto;
//	}
	
	public ScholarshipAttachment uploadFile(MultipartFile multipartFile, Integer scholarship_id, Integer candidate_id, JwtDetails jwtDetails) throws IOException{
		
		ScholarshipAttachment checkAttachmentAlreadyPresentOrNot = s_repo.checkAttachmentAlreadyPresentOrNot(candidate_id);
		
		if(ObjectUtils.isEmpty(checkAttachmentAlreadyPresentOrNot)) {
		
		
		ScholarshipAttachment scholarship_attachment = new ScholarshipAttachment();
			try
			{
				logger.debug("Message For Scholarship Attachment --------------");
				File file = convertMultiPartToFile(multipartFile);
				String fileName = generateFileName(multipartFile);
				scholarship_attachment.setCandidate_id(candidate_id);
				logger.debug("Message For Scholarship Attachment", file);
				System.out.println(candidate_id);
				scholarship_attachment.setActive(true);
				scholarship_attachment.setCreated_by(jwtDetails.getUserId());
				scholarship_attachment.setCreated_username(jwtDetails.getUserName());
				scholarship_attachment.setScholarship_attachment_file_name(fileName);
				scholarship_attachment.setCandidate_id(candidate_id);
				scholarship_attachment.setScholarship_attachment_path(LocalDate.now() + "/" + candidate_id + "/" + fileName);
				scholarship_attachment.setScholarship_attachement_type(
						endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + candidate_id + "/" + fileName);
				uploadFileTos3bucket(fileName, file, candidate_id);
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
			
			return s_repo.save(scholarship_attachment);
			
		} else {
		
			try
			{
				logger.debug("Message For Scholarship Attachment --------------");
				File file = convertMultiPartToFile(multipartFile);
				String fileName = generateFileName(multipartFile);
				checkAttachmentAlreadyPresentOrNot.setCandidate_id(candidate_id);
				logger.debug("Message For Scholarship Attachment", file);
				System.out.println(candidate_id);
				checkAttachmentAlreadyPresentOrNot.setActive(true);
				checkAttachmentAlreadyPresentOrNot.setModified_by(jwtDetails.getUserId());
				checkAttachmentAlreadyPresentOrNot.setModified_username(jwtDetails.getUserName());
				checkAttachmentAlreadyPresentOrNot.setScholarship_attachment_file_name(fileName);
				checkAttachmentAlreadyPresentOrNot.setCandidate_id(candidate_id);
				checkAttachmentAlreadyPresentOrNot.setScholarship_attachment_path(LocalDate.now() + "/" + candidate_id + "/" + fileName);
				checkAttachmentAlreadyPresentOrNot.setScholarship_attachement_type(
						endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + candidate_id + "/" + fileName);
				uploadFileTos3bucket(fileName, file, candidate_id);
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
			
			return s_repo.save(checkAttachmentAlreadyPresentOrNot);
		}
		
	}

		public String deleteFileFromS3Bucket(String fileUrl) {
			System.out.println(fileUrl);
			String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			System.out.println(fileName);
			s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
			return "Successfully deleted";
		}

		public void uploadFileTos3bucket(String fileName,File file, Integer candidate_id) {
			final String uniqueFileName = value + "/" + LocalDate.now() + "/" + candidate_id + "/" + fileName;  
			s3client.putObject(
					new PutObjectRequest(bucketName, uniqueFileName, file));
					//.withCannedAcl(CannedAccessControlList.PublicRead));
		}	

		public File convertMultiPartToFile(MultipartFile file) throws IOException {
			File convFile = new File(file.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
			return convFile;
		}

		public String generateFileName(MultipartFile multiPart) {
//			return new Date().getTime() +"_"+ LocalDate.now().getDayOfMonth() +"-"+ LocalDate.now().getMonthValue() +"-"+ LocalDate.now().getYear() +"/"+ multiPart.getOriginalFilename().replace(" ", "_");
//			return new Date().getTime() +"_"+ multiPart.getOriginalFilename().replace(" ", "_");
			return new Date().getTime() +"_"+ multiPart.getOriginalFilename().replace(" ", "_");
		}	 
	@Async
    public byte[] downloadFile(final String keyName) {
        byte[] content = null;
        logger.info("Downloading an object with key= " + keyName);
        final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
        final S3ObjectInputStream stream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(stream);
            logger.info("File downloaded successfully.");
            s3Object.close();
        } catch(final IOException ex) {
        	logger.info("IO Error Message= " + ex.getMessage());
        }
        return content;
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
	
//	public ScholarshipAttachment uploadVideoFile(MultipartFile multipartFile, Integer scholarship_id) throws IOException, AmazonClientException, InterruptedException{
//		
//		ScholarshipAttachment scholarship_attachment = s_repo.getOne(scholarship_id);
//		
//		try {
//		   File file = convertMultiPartToFile(multipartFile);
//		   String fileName = generateFileName(multipartFile);
//		   
//		   //uploading video file to s3 bucket
//		   // String videoPath = "C:\\Users\\Admin\\Downloads\\";
//		   //Upload upload = tm.upload(bucketName, uniqueFileName, new File(videoPath + "SampleVideo_1280x720_1mb.mp4"));
//		   uploadFileTos3bucket1(fileName, file, scholarship_id);
//		 
//		    scholarship_attachment.setScholarship_attachment_path(LocalDate.now() + "/" + fileName);
//		}catch (AmazonServiceException ase) {
//
//			logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
//			logger.info("Error Message:    " + ase.getMessage());
//			logger.info("HTTP Status Code: " + ase.getStatusCode());
//			logger.info("AWS Error Code:   " + ase.getErrorCode());
//			logger.info("Error Type:       " + ase.getErrorType());
//			logger.info("Request ID:       " + ase.getRequestId());
//
//		} catch (AmazonClientException ace) {
//			logger.info("Caught an AmazonClientException: ");
//			logger.info("Error Message: " + ace.getMessage());
//		} catch (IOException ioe) {
//			logger.info("IOE Error Message: " + ioe.getMessage());
//
//		}
//		return s_repo.save(scholarship_attachment);
//			
//		}
	
//	@Async
//    public S3Object downloadVideoFile(final String keyName) {
//        byte[] content = null;
//        logger.info("Downloading an object with key= " + keyName);
//        final S3Object s3Object = s3client.getObject(bucketName, value1 + "/" + keyName);
//        System.out.println("Content-Type 2: "  + s3Object.getObjectMetadata().getContentType());
////        final S3ObjectInputStream stream = s3Object.getObjectContent();
////        try {
////            content = IOUtils.toByteArray(stream);
////            logger.info("File downloaded successfully.");
////            s3Object.close();
////        } catch(final IOException ex) {
////        	logger.info("IO Error Message= " + ex.getMessage());
////        }
//        return s3Object;
//    }
}

//GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, keyName);
//
//S3Object s3Object = s3client.getObject(getObjectRequest);
//
//S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
//
//byte[] bytes = IOUtils.toByteArray(objectInputStream);
//
//String fileName = URLEncoder.encode(keyName, "UTF-8").replaceAll("\\+", "%20");
//
//HttpHeaders httpHeaders = new HttpHeaders();
//httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//httpHeaders.setContentLength(bytes.length);
//httpHeaders.setContentDispositionFormData("attachment", fileName);
//
//return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);



//Upload video to s3 bucket 
//try {
//    TransferManager tm = TransferManagerBuilder.standard()
//            .withS3Client(s3Client)
//            .build();
//
//    // TransferManager processes all transfers asynchronously,
//    // so this call returns immediately.
//    Upload upload = tm.upload(bucketName, "movie.mov", 
//        new File(picturePath + "/" + "movie.mov"));
//    System.out.println("Object upload started");
//
//    // Optionally, wait for the upload to finish before continuing.
//    upload.waitForCompletion();
//    System.out.println("Object upload complete");
//} catch (AmazonServiceException e) {
//    // The call was transmitted successfully, but Amazon S3 couldn't process 
//    // it, so it returned an error response.
//    e.printStackTrace();
//} catch (SdkClientException e) {
//    // Amazon S3 couldn't be contacted for a response, or the client
//    // couldn't parse the response from Amazon S3.
//    e.printStackTrace();
//}