package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import com.au.model.Academic_year;
import com.au.model.EnvBillDetails;
import com.au.model.StudentAttachments;
import com.au.repository.CandidateWalkinRepository;
import com.au.repository.StudentAttachmentsRepository;

@Service
public class StudentAttachmentsService {
	
	

	@Autowired
	private StudentAttachmentsRepository s_repo;
	
	@Autowired
	private CandidateWalkinRepository candidateWalkinRepository;
	
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
	
	
	public List<StudentAttachments> listAll(){
		return s_repo.findAll();
	}
	
	/*public String saveFileToS3Bucket(MultipartFile file) {
		 s_repo.save(file);
		return "";
	}*/
	
	public StudentAttachments saveStudentAttachments(StudentAttachments s) 
	{				
		return s_repo.save(s);
	}
	
	public StudentAttachments get(Integer id) {
        return s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("StudentAttachments Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	StudentAttachments ay = s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("StudentAttachments Not Found:"+id));    	
    	s_repo.delete(ay);
    }
    
   
    public byte[] viewFiles(String fileName) throws NoSuchFileException {
        try {
            byte[] content;
            final S3Object s3Object = s3client.getObject(bucketName, value + "/" + fileName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
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


	public void uploadFile(String subType, MultipartFile multipartFile, String lead_id, 
			String opportunity_id) {
		
		Integer student_id = candidateWalkinRepository.getStudentId(lead_id,opportunity_id);
		
		StudentAttachments studentAttachments = s_repo.findById(student_id)
		.orElse(new StudentAttachments()); 
				try {
					log.debug("Message For envBillDetails Attachment --------------");
					
					
					File file = convertMultiPartToFile(multipartFile);
					String fileName = generateFileName(multipartFile);
				
					log.debug("Cancel admission Attachment", multipartFile);
					
					studentAttachments.setAttachments_file_name(fileName);
					studentAttachments.setAttachments_file_path(LocalDate.now() + "/" + student_id + "/" + fileName);
					uploadFileTos3bucket(fileName, file, student_id);
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
				s_repo.save(studentAttachments);
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

			private void uploadFileTos3bucket(String fileName, File file, Integer student_id) {
				final String uniqueFileName = value + "/" + LocalDate.now() + "/" + student_id + "/" + fileName; // file.getName()
				s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
			}
    
	
}
