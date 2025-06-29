package com.au.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.EmailRequest;
import com.au.dto.InterviewRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Interview;
import com.au.model.Interviewer;
import com.au.repository.AttachmentsRepository;
import com.au.repository.DesignationRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.InterviewRepository;
import com.au.repository.InterviewerHistoryRepository;
import com.au.repository.InterviewerRepository;
import com.au.repository.JobProfileRepository;
import com.au.response.ResponseHandler;

@Service
public class InterviewService {

	@Autowired
	private InterviewerHistoryService irhs_ser;
	
	@Autowired
	private InterviewerRepository hcr_repo;
	
	@Autowired
	private InterviewRepository ir_repo;

	  @Autowired
	    @Qualifier("mailSenderPrimary")  // ✅ Explicitly use the primary mail sender
	    private JavaMailSender mailSender;

	@Autowired
	private JobProfileRepository jpr_repo;

	@Autowired
	private InterviewerRepository irr_repo;

	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private AttachmentsRepository ar_repo;
	
	@Autowired
	private InterviewHistoryService ihs_ser;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ResponseHandler response_handler;
	
	@Autowired
	private DesignationRepository dr_repo;
	
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
	
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	public List<Interview> listAll() {
		return ir_repo.findAll();
	}

	public Interview saveInterview(InterviewRequest interviewRequest) {

		Interview inv = ir_repo.save(interviewRequest.getInterview());
		List<Interviewer> iw = new ArrayList<Interviewer>();
		interviewRequest.getEmails().stream().forEach(a -> {
			Interviewer interviewer = new Interviewer();
			interviewer.setEmail(a);
			interviewer.setEmp_id(getEmpId(a));
			interviewer.setInterview_id(inv.getInterview_id());
			interviewer.setInterviewer_name(getEmpName(a));
			interviewer.setActive(true);
			interviewer.setJob_id(interviewRequest.getJob_id());
			iw.add(interviewer);
			saveInterviewer(interviewer);
		});
		return inv;
	}

	private Interviewer saveInterviewer(Interviewer interviewer) {
		return irr_repo.save(interviewer);

	}

	public void getExistingDataForHistory(Integer job_id) {
		if(ir_repo.getCountInterview(job_id) >=1) {
		 List<Interview> list_interviews = ir_repo.findByJobId(job_id);
		 ihs_ser.saveToInterviewHistory(list_interviews);
		 ir_repo.deleteExistingData(job_id);
		} 
		 if (hcr_repo.getCountInterviewer(job_id) >=1) {
		 List<Interviewer> list_interviewers = hcr_repo.findByJobId(job_id);
		 irhs_ser.saveToInterviewerHistory(list_interviewers);
		 hcr_repo.deleteExistingData(job_id);
		}
	}
	
	public Interview get(Integer job_id) {
		return ir_repo.findById(job_id).orElseThrow(() -> new ResourceNotFoundException("Interview ID Not Found:" + job_id));
	}

	public void delete(Integer id) {
		Interview ms = ir_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Interview ID Not Found:" + id));
		ir_repo.delete(ms);
	}

	// For Testing(as of now no use)
	public void sendMail(EmailRequest emails, Integer job_id) throws IOException {
		String attachments = ar_repo.getAllRecipients(job_id);
		S3Object s3Object = s3client.getObject(bucketName, value + "/" + attachments);
		S3ObjectInputStream stream = s3Object.getObjectContent();
		String interviewDate = getInterviewDate(job_id);
		String firstName = getname(job_id);
		//String lastName = getLastname(job_id);
		String[] strarray = emails.getEmails().toArray(new String[0]);
		for (int i = 0; i < strarray.length; i++) {
			System.out.println("===============" + strarray[i]);
		}
		String contents = "Dear Sir/Madam\r\n" + "\r\n"
				+ "We would like to inform you that the interview is scheduled for the below candidate\r\n" + "\r\n"
				+ firstName + " "  + "\r\n" + interviewDate + "\r\n" + "\r\n" + "Regards\r\n" + "\r\n"
				+ "Team Acharya"+ "<br/> " + "<br/> " 
				+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
				+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
		
		String subject = "Fwd: Candidate for Interview Schedule.";
		response_handler.sendMailsWithAttachment(strarray, contents, subject, stream, "Candidate_Resume.pdf");
		System.out.println("Mail Send...");
	}
	

	//Send Mail To Candidate
	public Object sendMailToCandidate(@Valid EmailRequest emails, Integer job_id) {
		String firstName = getname(job_id).trim();
		String interviewDate = getInterviewDate(job_id);
		String designation = dr_repo.getDesignation(ir_repo.getDesignationId(job_id));
		String ref_number = jpr_repo.getReferenceNumber(job_id);
		String cc_email = ir_repo.getCreatedByEmailToAddcc(job_id);
		String comments = ir_repo.getComments(job_id);
		
		String[] strarray = emails.getEmails().toArray(new String[0]);
		for (int i = 0; i < strarray.length; i++) {
			System.out.println(strarray[i]);
		}
		System.out.println(strarray);

		String genderSaluation = null;
		Character gender = jpr_repo.getGender(job_id);
		if (gender.equals('M')) 
			genderSaluation = "Mr. ";
		else
			genderSaluation = "Ms. ";
	
		Boolean ictStatus = ir_repo.getIctStatus(job_id);
		
		String contents=null;
		if(ictStatus != null && ictStatus) {
			
			 contents = "Dear " + genderSaluation + firstName + ",<br/>" + "<br/>"
					+ "We are glad to inform you that you are shortlisted for the interview and in continuation to the discussion "
					+ "we had with you, we invite you to attend the Interview on <html><b>" + interviewDate + "</b></html>" + " for the Position of <html><b>" + designation + ".</b></html><br/>" + "<br/>"
					+ "<u><b>Mode of Interview (Online/offline) :</b></u><br/><br/>"
					+ "<html><head><style> "
					+ "ul.a {list-style-type: circle;} "
					+ "</style></head>"
					+ "<body>"
					+ "1. Introduction<br/>"
					+ "2. Session Preparedness (10-30 mins) on any topic of your choice. (Applicable for Teaching Staff only)<br/>"
					+ "<ul class='a'>"
					+ "		  <li>Mode – Online or Offline – Candidate shall use laptop for the demo session.<br/></li>"
					+ "		  <li>Methodology - Mandatorily blended, flipped, problem solving based classroom and any other.<br/></li>"
					+ "		  <li>Material - PPT, URL’s/Weblinks, e-resources, Animated content, AV session - 02-05 mins etc.<br/></li>"
					+ "		  <li>Delivery: Usage of Instructional Tools and Software.<br/></li>"
					+ "		  <li>Preparation: Mindfulness/possibilities on effective utilization of ICT in 1 hr class.</li>"
					+ "		</ul> "
					+ "3. Q and A session for 10-15 mins.<br/>"
					+ "4. HR Round<br/><br/>"
					+ "</body></html>"
					+ "Please login to www.acharya.ac.in to know more about the Institution. <br/>" + "<br/>"
					+ "<html><b>Kindly carry the Photocopies of below documents for the interview:<br/>" + "<br/>"
					+ "1. Updated resume<br/>"
					+ "2. UG Certificate<br/>"
					+ "3. PG Certificate<br/>"
					+ "4. Id Proof/Adress Proof<br/>"
					+ "5. Experience letter<br/></html></b>" + "<br/>" + "<br/>"
					+ "Additional Info: " + comments + "<br/>" + "<br/>"
					+ "Please reach to the below address:<br/>" + "<br/>"
					+ "<a href= https://g.page/Acharya_institutes?share style='background-color: #4A57A9;color:white;text-decoration:none;padding:6px'>Click For Location</a><br/>" + "<br/>"
					+ "Department of HR<br/>"
					+ "Admin Block - Gate No.3<br/>"
					+ "Acharya Institutes<br/>"
					+ "No.89 & 90, Dr. Sarvepalli Radhakrishna Road,<br/>"
					+ "Soladevanahalli, Off Hesaraghatta Main Road,<br/>"
					+ "Bangalore - 560107.<br/>" 
					+ "Direct Line: 080-22555555.<br/>" + "<br/>"
					+ "<br/>" + "Thanks & Regards<br/>" + "<br/>" + "Team ERP<br/>" + "HR Division<br/>" + "Acharya Institutes" + "<br/> " + "<br/> " 
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
		
		} else {
		
			 contents = "Dear " + genderSaluation + firstName + ",<br/>" + "<br/>"
					+ "We are glad to inform you that you are shortlisted for the interview and in continuation to the discussion "
					+ "we had with you, we invite you to attend the Interview on <html><b>" + interviewDate + "</b></html>" + " for the Position of <html><b>" + designation + ".</b></html><br/>" + "<br/>"
					+ "<u><b>Mode of Interview :</b></u><br/>"
					+ "<html><head><style> "
					+ "ul.a {list-style-type: circle;} "
					+ "</style></head>"
					+ "<body>"
					+ "<ul class='a'>"
					+ "		  <li>Demo round (chalk and talk for 15-20 mins on any topic of your choice) - applicable for teaching staff.<br/>In case of a virtual interview PPT Presentation for 15-20 minutes on any topic of your choice.<br/></li>"
					+ "		  <li>Personal Interview.</li>"
					+ "		</ul> "
					+ "</body></html>"
					+ "Please login to www.acharya.ac.in to know more about the Institution. <br/>" + "<br/>"
					+ "<html><b>Kindly carry the Photocopies of below documents for the interview:<br/>" + "<br/>"
					+ "1. Updated resume<br/>"
					+ "2. UG Certificate<br/>"
					+ "3. PG Certificate<br/>"
					+ "4. Id Proof/Adress Proof<br/>"
					+ "5. Experience letter<br/></html></b>" + "<br/>" + "<br/>"
					+ "Additional Info: " + comments + "<br/>" + "<br/>"
					+ "Please reach to the below address:<br/>" + "<br/>"
					+ "<a href= https://g.page/Acharya_institutes?share style='background-color: #4A57A9;color:white;text-decoration:none;padding:6px'>Click For Location</a><br/>" + "<br/>"
					+ "Department of HR<br/>"
					+ "Admin Block - Gate No.3<br/>"
					+ "Acharya Institutes<br/>"
					+ "No.89 & 90, Dr. Sarvepalli Radhakrishna Road,<br/>"
					+ "Soladevanahalli, Off Hesaraghatta Main Road,<br/>"
					+ "Bangalore - 560107.<br/>" 
					+ "Direct Line: 080-22555555.<br/>" + "<br/>"
					+ "<br/>" + "Thanks & Regards<br/>" + "<br/>" + "Team ERP<br/>" + "HR Division<br/>" + "Acharya Institutes" + "<br/> " + "<br/> " 
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
		}
		String subject = "Acharya Interview Invite - Job Application Ref No. "+ref_number;
		response_handler.sendMultipleSimpleEmailWithCc(strarray,contents,subject,cc_email);
		System.out.println("Mail Send...");
		return null;
	}

	//Send Mail To interviewers
	public void sendMails(InterviewRequest emails, Integer job_id) throws IOException {
		String attachments = ar_repo.getAllRecipients(job_id); 
		S3Object s3Object = s3client.getObject(bucketName, value + "/" + attachments);;
		S3ObjectInputStream stream = s3Object.getObjectContent();
		String interviewDate = getInterviewDate(job_id);
		String designation = dr_repo.getDesignation(ir_repo.getDesignationId(job_id));
//		String designation = dr_repo.getDesignation(emails.getInterview().getDesignation_id());
		String firstName = getname(job_id);
		String[] strarray = emails.getEmails().toArray(new String[0]);
		for (int i = 0; i < strarray.length; i++) {
			System.out.println(strarray[i]);
		}
		System.out.println(strarray);

		String genderSaluation = null;
		Character gender = jpr_repo.getGender(job_id);
		if (gender.equals('M')) 
			genderSaluation = "Mr. ";
		else
			genderSaluation = "Ms. ";
		
		String contents = "Dear Sir/Madam,<br/>" + "<br/>"
				+ "We would like to inform you that the interview is scheduled for the below candidate, resume is attached for your reference. Please make yourself available for the Interview.<br/>" + "<br/>"
				+ "<html><b>Mr/Ms." + firstName + " on " + interviewDate + "</b></html>" + " for the Position of <html><b>" + designation + ".</b></html><br/>" + "<br/>"
				+ "Kindly provide your valuable feedback in ERP PORTAL after interview for further action.<br/>" + "<br/>"
				+ "Regards<br/>" + "<br/>" + "Team ERP<br/>" + "HR Division<br/>" + "Acharya Institutes" + "<br/> " + "<br/> " 
				+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
				+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
		
		String subject = "Interview Scheduled - Candidate Name : "+ genderSaluation + firstName;

		response_handler.sendMailsWithAttachment(strarray, contents, subject, stream, "Candidate_Resume.pdf");
		System.out.println("Mail Send...");

	}

	public String getname(Integer job_id) {
		return jpr_repo.getFirstname(job_id);
	}

//	public String getLastname(Integer job_id) {
//		return jpr_repo.getLastname(job_id);
//	}

	public String getInterviewDate(Integer job_id) {
		return ir_repo.getInterviewDate(job_id);
	}

	public Interview saveInterviews(@Valid Interview i) {
		return ir_repo.save(i);
	}

	public Integer getEmpId(String email) {
		return empDetail_repo.getEmpId(email);
	}

	public String getEmpName(String email) {
		return empDetail_repo.getEmpName(email);
	}

	public List<Map<String, Object>> getAllDeatils(Integer job_id) {
		return ir_repo.getAllInterviewDetail(job_id);
	}

}
