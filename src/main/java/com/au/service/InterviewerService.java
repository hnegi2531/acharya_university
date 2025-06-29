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
import com.au.exception.ResourceNotFoundException;
import com.au.model.Interviewer;
import com.au.model.JobProfile;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.InterviewerRepository;
import com.au.repository.JobProfileRepository;
import com.au.repository.UserAuthenticationRepository;

@Service
public class InterviewerService {
	
private Logger logger = LoggerFactory.getLogger(FeeTemplateService.class);
	
	public static final String value = "HrFeedbackBucket";
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

	@Autowired
	private InterviewerRepository hcr_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private JobProfileRepository jpr_repo;

	public List<Interviewer> listAll() {
		return hcr_repo.findAll();
	}
	
	public Interviewer saveInterviewer(Interviewer i) {
		return hcr_repo.save(i);
	}

	public List<Interviewer> saveInterviewer(List<Interviewer> i) {
//		i.stream().forEach(i1 ->{
//			i1.setMail_sent(i1.getMail_sent());
//		});
		hcr_repo.saveAll(i);
		return i;
	}

	public List<Interviewer> getlist(List<Integer> id_list) {
		List<Interviewer> list = hcr_repo.findByIds(id_list);
		return list;
	}
	
	public Interviewer get(Integer id) {
		return hcr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interviewer ID Not Found:" + id));
	}

	public void delete(Integer id) {
		Interviewer ms = hcr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Interviewer ID Not Found:" + id));
		hcr_repo.delete(ms);
	}

	public List<HashMap<String, Object>> getAllDeatils(Integer job_id) {
		return hcr_repo.getAllDeatils(job_id);
	}
	
	public List<HashMap<String, Object>> listAll1(Integer emp_id) {
		return hcr_repo.findAll1(emp_id);
	}

	public List<HashMap<String, Object>> getAllEmailDeatils(Integer interview_id) {
		return hcr_repo.getinterviewerEmail(interview_id);
	}
	
	public List<Map<String, Object>> getJobProfileDetails( Integer user_id) {
		String email_of_user = uar_repo.getEmail(user_id);
	return jpr_repo.getAllDetailFromJobProfile(hcr_repo.getAllJobIdFromInterviewer(email_of_user));
	}
	
	public void setInterviewerComments(Integer user_id,Integer job_id,String interviewer_comments) {
		String email_of_user = uar_repo.getEmail(user_id);
		hcr_repo.setInterviewerComments(email_of_user,job_id,interviewer_comments);
	}
	
	public void uploadFile(MultipartFile multipartFile, Integer job_id) {
//		JobProfile job = jpr_repo.getOne(job_id);
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t1 = LocalDate.now() + "/" + job_id + "/" + fileName;
			uploadFileTos3bucket(fileName, file, job_id);
			file.delete();
			System.out.println("gggggggggggg "+t1);
			System.out.println("gggggggggggg "+job_id);
			jpr_repo.updatePath(job_id, t1);
			
//			job.setHr_feedback_attachment(t1);
//			jpr_repo.save(job);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer job_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + job_id + "/" + fileName; // file.getName()
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
		// .withCannedAcl(CannedAccessControlList.PublicRead);
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
