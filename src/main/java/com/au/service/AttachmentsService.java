package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import com.au.exception.ResourceNotFoundException;
import com.au.model.Attachments;
import com.au.model.JobProfile;
import com.au.repository.AttachmentsRepository;
import com.au.repository.JobProfileRepository;
import com.au.response.ResponseHandler;



@Service
public class AttachmentsService {
	
	private static final List<String> contentTypes = Arrays.asList("application/pdf");

	private Logger logger = LoggerFactory.getLogger(AttachmentsService.class);

	public static final String value = "JobProfileAttachmentBucket";

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
	private AttachmentsRepository ar_repo;
	
	@Autowired
	private ResponseHandler response_handler;   
//
//	@Autowired
//	private JavaMailSender mailSender;
//	
//	@Autowired
//	private Environment env;

	@Autowired
	private JobProfileRepository jpr_repo;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	public List<Attachments> listAll() {
		return ar_repo.findAll();
	}

	public Attachments saveAttachments(@Valid Attachments attachments) throws IOException {
		return ar_repo.save(attachments);
	}

	public Attachments get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return ar_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attachments id Not Found:" + id));
	}

	public void delete(Integer id) {
		Attachments ay = ar_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Attachments id Not Found:" + id));
		ar_repo.delete(ay);
	}

	public String getEmailId(Integer job_id) {
		return ar_repo.getEmail(job_id);
	}

	public String getName(Integer job_id) {
		return ar_repo.getName(job_id);
	}

	public String uploadFile(MultipartFile multipartFile, Integer job) throws Exception {
		System.out.println("11111111111111111111 " + job);
		if(getJobId(job)>=1)
			throw new Exception("Request Can't be Processed as Job Id is Already Present");
		/*
		 * FileRequest filerequest = new FileRequest(); Attachments attachments=new
		 * Attachments(); try { logger.debug("Message For Attachment--------------");
		 * File file = convertMultiPartToFile(multipartFile); String fileName =
		 * generateFileName(multipartFile); filerequest.setJob_id(job);
		 * logger.debug("Message For Attachment",file); System.out.println(job);
		 * filerequest.setBucketName(bucketName); filerequest.setFileName(fileName);
		 * filerequest.setPath(bucketName + "/" + fileName);
		 * filerequest.setEndpointUrl(endpointUrl + "/" + bucketName + "/" + fileName);
		 * uploadFileTos3bucket(fileName, file, job);
		 * logger.debug("Message For Attachment",file); file.delete();
		 */
		else {
		Attachments attachments = new Attachments();
		try {
			String fileContentType = multipartFile.getContentType();
		    if(contentTypes.contains(fileContentType)) {
		        // You have the correct extension
		        // rest of your code here
			logger.debug("Message For Attachment--------------");
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			attachments.setJob_id(job);
			logger.debug("Message For Attachment", file);
			System.out.println(job);
			// attachments.setBucketName(bucketName);
			attachments.setAttachment_file_name(fileName);
			attachments.setAttachment_path(LocalDate.now() + "/" + job + "/" + fileName);
			attachments.setAttachment_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + job + "/" + fileName);
			uploadFileTos3bucket(fileName, file, job);
			logger.debug("Message For Attachment", file);
			file.delete();
		    }
		    else {
		    	throw new Exception("Request Can't be Processed as the type of file you are trying to upload is not PDF");
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
		String emailId = getEmailId(job);
		System.out.println("|||||||||||||||" + emailId);
		String name = getName(job);
		System.out.println("==================" + name);
		
		/*Random r = new Random();
		int low = 1000;
		int high = 10000;
		int check = r.nextInt(high - low) + low;
		String result = check + "-" + LocalDate.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.BASIC_ISO_DATE);
		System.out.println("(((((((((((((((((((())))))))))))))))))))"+result);
		// int splitAfter = 10;
		// String finalResult = result.substring(0, splitAfter);
		// String b2 = result.substring(splitAfter);
		JobProfile jobProfile = jpr_repo.findReferenceNo(job);
		jobProfile.setReference_no(result);
		System.out.println("(((((((((((((((((((())))))))))))))))))))"+result);
		jpr_repo.save(jobProfile);
		String content = "Dear\r\n" + "\r\n" + "Mr " + name + "\r\n" + "\r\n" + "\r\n"
				+ "This letter is to let you know that we have received your application. We appreciate your interest in applying to Acharya Institutes. We are reviewing applications currently and expect to schedule interviews soon. If you are selected for an interview, you can expect a phone call from our Human Resources department.\r\n"
				+ "\r\n"
				+ "Thank you, once again, for your interest in our Institutes. We do appreciate the time that you invested in this application.\r\n"
				+ "\r\n" + "Please quote the reference number created @" + result + " "
				+ "of application for further queries.\r\n" + "\r\n"
				+ "Contact us on hr@acharya.ac.in or reach us on 080-22555555.\r\n" + "\r\n" + "Regards\r\n" + "\r\n"
				+ "\r\n" + "Team Acharya\r\n" + "\r\n" + "This is an automated response. please do not reply.";
		sendSimpleEmail(emailId, content, "Fwd: Your request for job application @ " + result);

		System.out.println("Till here I am");
		// return filerequest;
		return ar_repo.save(attachments);
		*/
		
		String val = getReference_no();
		
		if (val == null) {
			String result;
			int num1 = 0;
			int add1 = 1;
			num1 = num1 + add1;
			String formattedStrr = String.format("%04d", num1);
			//String ld=LocalDate.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.BASIC_ISO_DATE);
			Date date = new Date();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int month = localDate.getMonthValue();
			int year = localDate.getYear();
			String year1 = String.valueOf(year).substring(2);
			if(month<10) {
				result = formattedStrr  + "/0" + month + year1 ;
			}else {
				result = formattedStrr  + "/" + month + year1 ;
			}
			JobProfile jobProfile = jpr_repo.findReferenceNo(job);
			jobProfile.setReference_no(result);
			jpr_repo.save(jobProfile);
			
				String content = "Dear  Mr/Ms. "+ name + ","+ "<br/> " + "<br/> "
						+ "This letter is to let you know that we have received your application. We appreciate your interest in applying to Acharya Institutes. "
						+ "We are reviewing applications currently and expect to schedule interview soon. If you are selected for an interview,  You can expect a call or mail from Human Resources department."
						+ "<br/> "
						+ "Please quote the job application reference number for further queries. You can reach us on  hr@acharya.ac.in ."+ "<br/> "  + "<br/> " + "Regards" + "<br/> " + "Team ERP" + "<br/> " + "HR Division" + "<br/> " + "Acharya Institutes" + "<br/> " + "<br/> " 
						+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
						+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
				
				response_handler.sendSimpleEmailWithHtmlContent(emailId, content, "Job Application  Ref No. " + result);

			System.out.println("Till here I am");
		    ar_repo.save(attachments);
		    return result;
			
		} else {
			String result;
			String[] arrOfStrr = val.split("/");
			System.out.println(arrOfStrr);
			String str2 = arrOfStrr[0];
			int num1 = Integer.parseInt(str2);
			System.out.println("reference _no else condition " + num1);
			int add1 = 1;
			num1 = num1 + add1;
			String formattedStr1 = String.format("%04d", num1);
			Date date = new Date();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int month = localDate.getMonthValue();
			int year = localDate.getYear();
			String year1 = String.valueOf(year).substring(2);
			if(month<10) {
				result = formattedStr1 + "/"+ "0" + month + year1 ;
			}else {
				result = formattedStr1  + "/" + month + year1 ;
			}
			JobProfile jobProfile = jpr_repo.findReferenceNo(job);
			jobProfile.setReference_no(result);
			jpr_repo.save(jobProfile);
			
				String content = "Dear  Mr/Ms. "+ name + "," +  "<br/> " + "<br/> "
						+ "This letter is to let you know that we have received your application. We appreciate your interest in applying to Acharya Institutes."
						+ "We are reviewing applications currently and expect to schedule interview soon. If you are selected for an interview,  You can expect a call from Human Resources department."
						+ "<br/> "
						+ "Please quote the job application reference number for further queries. You can reach us on  hr@acharya.ac.in ." + "<br/> " + "<br/> " + "Regards" + "<br/> " + "Team ERP"  + "<br/> " + "HR Division" + "<br/> " + "Acharya Institutes"
						+ "<br/> " + "<br/> " 
						+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
						+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
				response_handler.sendSimpleEmailWithHtmlContent(emailId, content, "Job Application  Ref No. " + result);
			
			System.out.println("Till here I am");
			ar_repo.save(attachments);
			return result;
		}
	}
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

	public byte[] viewFiles(final String keyName) throws NoSuchFileException {
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

	private void uploadFileTos3bucket(String fileName, File file, Integer job) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + job + "/" + fileName; // file.getName()
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
		// .withCannedAcl(CannedAccessControlList.PublicRead);
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

//	public void sendSimpleEmail(String toEmail, String body, String subject) {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"));
//		message.setTo(toEmail);
//		message.setText(body);
//		message.setSubject(subject);
//		// message.setCc("vikashkumar@acharya.ac.in");
//		mailSender.send(message);
//		System.out.println("Mail Send...");
//
//	}
	
//	public void sendSimpleEmail(String toEmail, String body, String subject) {
//
//		MimeMessagePreparator mailMessage = mimeMessage -> {
//
//		    MimeMessageHelper message = new MimeMessageHelper(
//		            mimeMessage, true);
//		    try {
//		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"),"ERP-Info");
//		message.setTo(toEmail);
//		message.setText(body);
//		message.setSubject(subject);
//		// message.setCc("vikashkumar@acharya.ac.in");
//		    } catch (Exception e) {
//		        throw new Exception(toEmail, e);
//		    }
//		};
//		mailSender.send(mailMessage);
//		System.out.println("Mail Send...");
//	}
	
	public Attachments getAllDeatils(Integer job_id) {
		return ar_repo.getAttachmentPath(job_id);
	}
	
	private Integer getJobId(Integer job_id) {
		return ar_repo.getcountjobid(job_id);
	}
	
	private String getReference_no() {
		System.out.println("<<<<<<<<<<<>>>>>>>>>>>>>> " +jpr_repo.fetchgetReference_no());
		return jpr_repo.fetchgetReference_no();
	}
	
	
	public String uploadFileForPhp(MultipartFile multipartFile, Integer job) throws Exception {
		System.out.println("11111111111111111111 " + job);
		if(getJobId(job)>=1)
			throw new Exception("Request Can't be Processed as Job Id is Already Present");
		/*
		 * FileRequest filerequest = new FileRequest(); Attachments attachments=new
		 * Attachments(); try { logger.debug("Message For Attachment--------------");
		 * File file = convertMultiPartToFile(multipartFile); String fileName =
		 * generateFileName(multipartFile); filerequest.setJob_id(job);
		 * logger.debug("Message For Attachment",file); System.out.println(job);
		 * filerequest.setBucketName(bucketName); filerequest.setFileName(fileName);
		 * filerequest.setPath(bucketName + "/" + fileName);
		 * filerequest.setEndpointUrl(endpointUrl + "/" + bucketName + "/" + fileName);
		 * uploadFileTos3bucket(fileName, file, job);
		 * logger.debug("Message For Attachment",file); file.delete();
		 */
		else {
		Attachments attachments = new Attachments();
		try {
			String fileContentType = multipartFile.getContentType();
		    if(contentTypes.contains(fileContentType)) {
		        // You have the correct extension
		        // rest of your code here
			logger.debug("Message For Attachment--------------");
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			attachments.setJob_id(job);
			logger.debug("Message For Attachment", file);
			System.out.println(job);
			// attachments.setBucketName(bucketName);
			attachments.setAttachment_file_name(fileName);
			attachments.setAttachment_path(LocalDate.now() + "/" + job + "/" + fileName);
			attachments.setAttachment_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + job + "/" + fileName);
			uploadFileTos3bucket(fileName, file, job);
			logger.debug("Message For Attachment", file);
			file.delete();
		    }
		    else {
		    	throw new Exception("Request Can't be Processed as the type of file you are trying to upload is not PDF");
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
		String emailId = getEmailId(job);
		System.out.println("|||||||||||||||" + emailId);
		String name = getName(job);
		System.out.println("==================" + name);
		
		/*Random r = new Random();
		int low = 1000;
		int high = 10000;
		int check = r.nextInt(high - low) + low;
		String result = check + "-" + LocalDate.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.BASIC_ISO_DATE);
		System.out.println("(((((((((((((((((((())))))))))))))))))))"+result);
		// int splitAfter = 10;
		// String finalResult = result.substring(0, splitAfter);
		// String b2 = result.substring(splitAfter);
		JobProfile jobProfile = jpr_repo.findReferenceNo(job);
		jobProfile.setReference_no(result);
		System.out.println("(((((((((((((((((((())))))))))))))))))))"+result);
		jpr_repo.save(jobProfile);
		String content = "Dear\r\n" + "\r\n" + "Mr " + name + "\r\n" + "\r\n" + "\r\n"
				+ "This letter is to let you know that we have received your application. We appreciate your interest in applying to Acharya Institutes. We are reviewing applications currently and expect to schedule interviews soon. If you are selected for an interview, you can expect a phone call from our Human Resources department.\r\n"
				+ "\r\n"
				+ "Thank you, once again, for your interest in our Institutes. We do appreciate the time that you invested in this application.\r\n"
				+ "\r\n" + "Please quote the reference number created @" + result + " "
				+ "of application for further queries.\r\n" + "\r\n"
				+ "Contact us on hr@acharya.ac.in or reach us on 080-22555555.\r\n" + "\r\n" + "Regards\r\n" + "\r\n"
				+ "\r\n" + "Team Acharya\r\n" + "\r\n" + "This is an automated response. please do not reply.";
		sendSimpleEmail(emailId, content, "Fwd: Your request for job application @ " + result);

		System.out.println("Till here I am");
		// return filerequest;
		return ar_repo.save(attachments);
		*/
		
		String val = getReference_no();
		
		if (val == null) {
			String result;
			int num1 = 0;
			int add1 = 1;
			num1 = num1 + add1;
			String formattedStrr = String.format("%04d", num1);
			//String ld=LocalDate.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.BASIC_ISO_DATE);
			Date date = new Date();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int month = localDate.getMonthValue();
			int year = localDate.getYear();
			String year1 = String.valueOf(year).substring(2);
			if(month<10) {
				result = formattedStrr  + "/0" + month + year1 ;
			}else {
				result = formattedStrr  + "/" + month + year1 ;
			}
			JobProfile jobProfile = jpr_repo.findReferenceNo(job);
			jobProfile.setReference_no(result);
			jpr_repo.save(jobProfile);
			
				String content = "Dear  Mr/Ms. "+ name + ","+ "<br/> " + "<br/> "
						+ "This letter is to let you know that we have received your application. We appreciate your interest in applying to Acharya Institutes. "
						+ "We are reviewing applications currently and expect to schedule interview soon. If you are selected for an interview,  You can expect a call or mail from Human Resources department."
						+ "<br/> "
						+ "Please quote the job application reference number for further queries. You can reach us on  hr@acharya.ac.in ."+ "<br/> "  + "<br/> " + "Regards" + "<br/> " + "Team ERP" + "<br/> " + "HR Division" + "<br/> " + "Acharya Institutes" + "<br/> " + "<br/> " 
						+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
						+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
				
//				response_handler.sendSimpleEmailWithHtmlContent(emailId, content, "Job Application  Ref No. " + result);

			System.out.println("Till here I am");
		    ar_repo.save(attachments);
		    return result;
			
		} else {
			String result;
			String[] arrOfStrr = val.split("/");
			System.out.println(arrOfStrr);
			String str2 = arrOfStrr[0];
			int num1 = Integer.parseInt(str2);
			System.out.println("reference _no else condition " + num1);
			int add1 = 1;
			num1 = num1 + add1;
			String formattedStr1 = String.format("%04d", num1);
			Date date = new Date();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int month = localDate.getMonthValue();
			int year = localDate.getYear();
			String year1 = String.valueOf(year).substring(2);
			if(month<10) {
				result = formattedStr1 + "/"+ "0" + month + year1 ;
			}else {
				result = formattedStr1  + "/" + month + year1 ;
			}
			JobProfile jobProfile = jpr_repo.findReferenceNo(job);
			jobProfile.setReference_no(result);
			jpr_repo.save(jobProfile);
			
				String content = "Dear  Mr/Ms. "+ name + "," +  "<br/> " + "<br/> "
						+ "This letter is to let you know that we have received your application. We appreciate your interest in applying to Acharya Institutes."
						+ "We are reviewing applications currently and expect to schedule interview soon. If you are selected for an interview,  You can expect a call from Human Resources department."
						+ "<br/> "
						+ "Please quote the job application reference number for further queries. You can reach us on  hr@acharya.ac.in ." + "<br/> " + "<br/> " + "Regards" + "<br/> " + "Team ERP"  + "<br/> " + "HR Division" + "<br/> " + "Acharya Institutes"
						+ "<br/> " + "<br/> " 
						+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
						+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
//				response_handler.sendSimpleEmailWithHtmlContent(emailId, content, "Job Application  Ref No. " + result);
			
			System.out.println("Till here I am");
			ar_repo.save(attachments);
			return result;
		}
	}
}	
	
	
	
	
}

//LocalDate.now( ZoneId.of( "Asia/Kolkata" ) ).with( TemporalAdjusters.next( DayOfWeek.FRIDAY ) ).format( DateTimeFormatter.BASIC_ISO_DATE )	
