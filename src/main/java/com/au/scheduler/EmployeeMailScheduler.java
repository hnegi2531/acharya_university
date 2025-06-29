package com.au.scheduler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.au.model.Department;
import com.au.model.Designation;
import com.au.model.EmployeeDetails;
import com.au.model.JobProfile;
import com.au.model.Shift;
import com.au.model.UserAuthentication;
import com.au.repository.DepartmentRepository;
import com.au.repository.DesignationRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.JobProfileRepository;
import com.au.repository.ShiftRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.au.service.StudentBonafideService;

@Service
public class EmployeeMailScheduler {
	
	@Autowired
	private ResponseHandler response_handler;

	@Autowired
	private EmployeeDetailsRepository empRepo;

	@Autowired
	private UserAuthenticationRepository uar_repo;

	@Autowired
	private JobProfileRepository jobRepo;

	@Autowired
	private DesignationRepository designationRepo;

	@Autowired
	private DepartmentRepository deptRepo;

	@Autowired
	private ShiftRepository shiftRepo;
	
	@Autowired
	private StudentBonafideService std_bonafide_Service;
	
	@Autowired
	private Environment env;
	
    @Autowired
    @Qualifier("mailSenderPrimary")  // ✅ Specify which bean to use
    private JavaMailSender mailSender;
	

	public static String convertToTitleCase(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		StringBuilder titleCase = new StringBuilder(input.length());
		boolean nextTitleCase = true;

		for (char c : input.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				nextTitleCase = true;
			} else if (nextTitleCase) {
				c = Character.toTitleCase(c);
				nextTitleCase = false;
			} else {
				c = Character.toLowerCase(c);
			}
			titleCase.append(c);
		}

		return titleCase.toString();
	}
	
	public String mailTemplate1 = ("<!DOCTYPE html>\r\n" + "<html>\r\n" + "  <head>\r\n" + "    <title></title>\r\n"
			+ "    \r\n" + "  </head>\r\n" + "  <body style=\"padding: 25px;\">\r\n" + "     \r\n" + "      \r\n"
			+ "      \r\n"
			+ "    <span style=\"font-style:italic;font-family:Georgia;font-size:15px;color:#500050\">Dear All,</span>\r\n"
			+ "      \r\n" + "     \r\n"
			+ "      <p><span style=\"font-style:italic;font-family:Georgia;font-size:15px\"> We are very pleased to announce that our team is growing. Mr. CHENCHANABOINA JAGADEESH YADAV, Software Developer has joined the Department of departmentName on DOJ</span></p>\r\n"
			+ "      <p><span style=\"font-style:italic;font-family:Georgia;font-size:15px;color:#500050\"> Please join us in welcoming Mr. CHENCHANABOINA JAGADEESH YADAV   and make sure to drop by their workspace to introduce yourselves.</span></p>\r\n"
			+ "      <hr>\r\n" + "    \r\n"
			+ "   <span style=\"font-style:italic;font-family:Georgia;font-size:15px;color:#500050\">Yours Sincerely </span>\r\n"
			+ "    <p style=\"font-style:italic;font-family:Georgia;font-size:15px;color:#500050\">Regards<br>\r\n"
			+ "  Principal, Acharya University<br>\r\n" + "  HOD - ERP\r\n" + "    </p>\r\n" + "  \r\n" + "    \r\n"
			+ "  </body>\r\n" + "</html>");

	public String generalGuidelinesMail = ("<!DOCTYPE html>\r\n" + "<html>\r\n" + "  <head>\r\n"
			+ "    <title></title>\r\n" + "    \r\n" + "  </head>\r\n" + "  <body style=\"padding: 25px;\">\r\n"
			+ "     <span style=\"font-style:italic;font-family:Georgia;font-size:15px\">Dear Ms. Poonam Sharma  </span>\r\n"
			+ "      <p><span style=\"font-style:italic;font-family:Georgia;font-size:15px\"> Welcome aboard </span>!<span style=\"font-style:italic;font-family:Georgia;font-size:15px\"> We are pleased to have you working with us. You are selected for employment due to the attributes you displayed that appear to match the qualities Acharya looks for, in an employee. </span>  </p>\r\n"
			+ "      <p><span style=\"font-style:italic;font-family:Georgia;font-size:15px\"> We look forward to seeing you grow and develop into an outstanding colleague who exhibits a high level of care, concern and compassion for others. We hope that you will find your work rewarding, challenging and meaningful.</span></p>\r\n"
			+ "     <p><span style=\"font-style:italic;font-family:Georgia;font-size:15px\"> The keys to your success will be being dependable, reliable, showing openness, follow-through, attentiveness, supervision, documentation and following the policies and procedures. While doing these things, you will be successful and so will Acharya.  </span></p> \r\n"
			+ "   <p><span style=\"font-style:italic;font-family:Georgia;font-size:15px\"><b> Please make note of the below details </b>  </span></p>\r\n"
			+ "    <table border=\"2px solid black\" align=\"center\" width=\"40%\" height=\"100%\" style=\"border-collapse:collapse\"><tbody><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>Employee Id</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\">AI002599</td></tr><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>Email Id</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><a href=\"mailto:poonam2599@acharya.ac.in\" target=\"_blank\">poonam2599@acharya.ac.in</a></td></tr><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>ERP Login Id</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\">poonam2598</td></tr><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>Institute</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\">SMT. NAGARATHNAMMA COLLEGE OF NURSING</td></tr><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>Staff of</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\">PSYCHIATRIC NURSING</td></tr><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>Shift Time</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\">Shift : Gen12 ( 09:00:00 - 16:45:00 )</td></tr><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>Reporting Officer</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\">DEVI NANJAPPAN</td></tr><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>Leave Approver 1</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\">leaveApprover1</td></tr><tr><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\"><b>Leave Approver 2</b></td><td style=\"padding-left:0.5em;padding-top:0.5em;padding-bottom:0.5em\">leaveApprover2</td></tr></tbody></table>\r\n"
			+ "    <p><span style=\"font-style:italic;font-family:Georgia;font-size:15px\"> <b> General  Guidelines:</b> </span></p>\r\n"
			+ "    <ul> <li style=\"margin-top:10px\"><span style=\"font-style:italic;font-family:Georgia;font-size:15px\">Default Email  login password is acharya1234 ( Acharya E-mail shall be operational within 3 days from DOJ ), Please reset password once login.</span></li><li style=\"margin-top:10px\"><span style=\"font-style:italic;font-family:Georgia;font-size:15px\">Login on &lt;<a href=\"https://loginUrl\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=http://loginUrl&amp;source=gmail&amp;ust=1696501072836000&amp;usg=AOvVaw3tTKZ4DjWH6jX5X5EpNq8E\">loginUrl</a>&gt;&nbsp;for ERP login, default password is acharya1234.  Please reset password once login.</span></li><li style=\"margin-top:10px\"><span style=\"font-style:italic;font-family:Georgia;font-size:15px\">Upon reporting to duty, kindly ensure that you collect your Bio-metric Card from HR Department on the same day. Please note Attendance is recorded as per Punching.</span></li><li style=\"margin-top:10px\"><span style=\"font-style:italic;font-family:Georgia;font-size:15px\">Leave to be availed with prior intimation of minimum 24 hrs to the reporting officer. Leave to be availed only on approval in ERP. However, in extreme circumstances and unavoidable situations where in leave could not be applied in advance, your leave can be initiated by your reporting officer within two days of the leave date.</span></li><li style=\"margin-top:10px\"><span style=\"font-style:italic;font-family:Georgia;font-size:15px\">You can view your attendance report in your ERP login. Your attendance is your responsibility and ensure your leaves are approved well in advance and the attendance status is to your satisfaction at any given date. Payroll is automatically calculated as per the attendance status in ERP. </span></li><li style=\"margin-top:10px\"><span style=\"font-style:italic;font-family:Georgia;font-size:15px\">You can raise tickets against services like Maintenance, Housekeeping, System/IT support, HR attendance and Security related support in ERP. </span></li></ul>\r\n"
			+ "    \r\n"
			+ "    <hr>\r\n" + "    <br>\r\n" + "  \r\n"
			+ "    <span style=\"font-style:italic;font-family:Georgia;font-size:15px\"> Sincerely,</span><br><br><br><br>\r\n"
			+ "    <span style=\"font-family:Georgia;font-size:15px\">Regards<br>Principal,</span>\r\n"
			+ "    <span style=\"font-family:Georgia;font-size:15px\">Acharya University<br>HOD - &lt;dynamic&gt;<br>Human Resource Department</span>\r\n"
			+ "    \r\n" + "    \r\n" + "    \r\n" + "  </body>\r\n" + "  \r\n" + "</html>");

//	@Scheduled(fixedDelay = 30000)
//	@Scheduled(cron = "0 0 20 * * ?", zone = "Asia/Kolkata")
//	public void emailScheduler() throws InterruptedException, IOException {
//
////		List<String> emails = empRepo.findLast48HoursRecords();
//		List<EmployeeDetails> emails = empRepo.findAllEmails();
//		System.err.println("List " + emails);
//		for (EmployeeDetails emp : emails) {
//			System.err.println(emp.getEmail());
//			String gender = null;
//			String leaveApprover1name;
//			String leaveApprover2name;
//			String reportingOfficer;
//			String deptName;
//			String designationName;
//			String jobEmail;
//			String employeename;
//			String shiftDetail;
//			String reportingOfficerEmail;
//			String empCode;
//			String empSchool;
//			String username;
//
//			Optional<EmployeeDetails> leaveApprover1 = empRepo.findById(emp.getLeave_approver1_emp_id());
//			if (!leaveApprover1.isPresent()) {
//				leaveApprover1name = "";
//			} else {
//				leaveApprover1name = convertToTitleCase(leaveApprover1.get().getEmployee_name());
//			}
//			Optional<EmployeeDetails> leaveApprover2 = empRepo.findById(emp.getLeave_approver2_emp_id());
//			if (!leaveApprover2.isPresent()) {
//				leaveApprover2name = "";
//			} else {
//				leaveApprover2name = convertToTitleCase(leaveApprover2.get().getEmployee_name());
//			}
//			Optional<EmployeeDetails> reportingEmail = empRepo.findById(emp.getReport_id());
//			if (!reportingEmail.isPresent()) {
//				reportingOfficer = "";
//				reportingOfficerEmail = "divyakumari@acharya.ac.in";
//			} else {
//				reportingOfficer = convertToTitleCase(reportingEmail.get().getEmployee_name());
//				reportingOfficerEmail = reportingEmail.get().getEmail();
//			}
//			List<String> emailsToDept = empRepo.findByDeptId(emp.getDept_id(), emp.getSchool_id());
//			String[] emailtoDept = emailsToDept.toArray(new String[0]);
//			
////			String[] emailtoDept = new String[2];
////			emailtoDept[0] = "santanu2596@acharya.ac.in";
////			emailtoDept[1] = "divyakumari@acharya.ac.in";
//			
//			Optional<Department> department = deptRepo.findById(emp.getDept_id());
//			if (!department.isPresent()) {
//				deptName = "";
//			} else {
//				deptName = department.get().getDept_name();
//			}
//			Optional<Designation> designation = designationRepo.findById(emp.getDesignation_id());
//			if (!designation.isPresent()) {
//				designationName = "";
//			} else {
//				designationName = designation.get().getDesignation_name();
//			}
//			Optional<JobProfile> job = jobRepo.findById(emp.getJob_id());
//			if (!job.isPresent()) {
//				jobEmail = "divyakumari@acharya.ac.in";
//			} else {
//				jobEmail = job.get().getEmail();
//			}
//			Optional<UserAuthentication> userauthentication = uar_repo.findByEmailAndActiveTrue(emp.getEmail());
//			if (userauthentication == null) {
//				username = " ";
//			} else {
//				username = userauthentication.get().getUsername();
//			}
//
//			if (emp.getEmployee_name() == null) {
//				employeename = " ";
//			} else {
//				employeename = emp.getEmployee_name();
//			}
//
//			Optional<Shift> shiftDetails = shiftRepo.findById(emp.getShift_category_id());
//			if (!shiftDetails.isPresent()) {
//				shiftDetail = " ";
//			} else {
//				shiftDetail = shiftDetails.get().getShiftName() + " ( " + shiftDetails.get().getShiftStartTime() + " - "
//						+ shiftDetails.get().getShiftEndTime() + " )";
//			}
//
//			if (emp.getGender() == 'M') {
//				gender = "Mr";
//			} else if (emp.getGender() == 'F') {
//				gender = "Ms";
//			}
//
//			String[] emailArrayPersonalAndAcharya = new String[2];
//			emailArrayPersonalAndAcharya[0] = emp.getEmail();
//			emailArrayPersonalAndAcharya[1] = jobEmail;
//
//			String[] emailArrayReportingAndPrincipal = new String[2];
//			emailArrayReportingAndPrincipal[0] = reportingOfficerEmail;
//			emailArrayReportingAndPrincipal[1] = "divyakumari@acharya.ac.in";
//
//			String employeeName = convertToTitleCase(employeename);
//
//			String date_of_joining = emp.getDate_of_joining();
//
//			if (emp.getEmpcode() == null) {
//				empCode = "";
//			} else {
//				empCode = emp.getEmpcode();
//			}
//			if (emp.getSchool() == null) {
//				empSchool = "";
//			} else {
//				empSchool = emp.getSchool();
//			}
//
//			String replacedString = mailTemplate1.replace("Mr", gender)
//					.replace("CHENCHANABOINA JAGADEESH YADAV", employeeName).replace("departmentName", deptName)
//					.replace("DOJ", date_of_joining).replace("Software Developer", designationName);
//
//			String replacedStringForGeneralGuideline = generalGuidelinesMail.replace("Ms", gender)
//					.replace("Poonam Sharma", employeeName).replace("AI002599", empCode)
//					.replace("poonam2599@acharya.ac.in", emp.getEmail()).replace("poonam2598", username)
//					.replace("SMT. NAGARATHNAMMA COLLEGE OF NURSING", empSchool)
//					.replace("PSYCHIATRIC NURSING", deptName)
//					.replace("Shift : Gen12 ( 09:00:00 - 16:45:00 )", shiftDetail)
//					.replace("DEVI NANJAPPAN", reportingOfficer).replace("leaveApprover1", leaveApprover1name)
//					.replace("leaveApprover2", leaveApprover2name)
//					.replace("loginUrl", "www.acharyaerptech.in")
//					.replace("&lt;dynamic&gt;", deptName)
//					.replace("Acharya University", empSchool);
//			
//			
//			response_handler.sendMailToMultipleWithCC(emailtoDept, emp.getEmail(), replacedString,
//					"New Addition to Our Team");
//
//			response_handler.sendMailToMultipleWithMultipleCC(emailArrayPersonalAndAcharya,
//					emailArrayReportingAndPrincipal, replacedStringForGeneralGuideline, "General Guidelines");
//			System.err.println("MAIL SENT TO " + emp.getEmail());
//		}
//
//	}
	
	
	@Scheduled(cron = "0 0 20 * * ?", zone = "Asia/Kolkata")
	public void emailScheduler() throws InterruptedException, IOException {
	    // List of employees to email
	    List<EmployeeDetails> employees = empRepo.findAllEmails();
	    System.err.println("Employee List: " + employees);

	    // Set to avoid sending emails multiple times to the same email address
	    Set<String> allEmails = new HashSet<>();
	    
	    for (EmployeeDetails emp : employees) {
	        try {
	            // Debug log for each employee's email
	            System.err.println("Processing employee: " + emp.getEmail());
	            
	            String gender = (emp.getGender() == 'M') ? "Mr" : "Ms";
	            String leaveApprover1name = getLeaveApproverName(emp.getLeave_approver1_emp_id());
	            String leaveApprover2name = getLeaveApproverName(emp.getLeave_approver2_emp_id());
	            String reportingOfficer = getReportingOfficer(emp.getReport_id());
	            String reportingOfficerEmail = getReportingOfficerEmail(emp.getReport_id());
	            List<String> emailsToDept = empRepo.findByDeptId(emp.getDept_id(), emp.getSchool_id());
	            String[] emailtoDept = emailsToDept.toArray(new String[0]);
	            String deptName = getDepartmentName(emp.getDept_id());
	            String designationName = getDesignationName(emp.getDesignation_id());
	            String jobEmail = getJobEmail(emp.getJob_id());
	            String username = getUsername(emp.getEmail());
	            String employeename = (emp.getEmployee_name() != null) ? convertToTitleCase(emp.getEmployee_name()) : "";
	            String shiftDetail = getShiftDetails(emp.getShift_category_id());
	            String empCode = (emp.getEmpcode() != null) ? emp.getEmpcode() : "";
	            String empSchool = (emp.getSchool() != null) ? emp.getSchool() : "";

	            // Prepare email subject and body content
	            String replacedString = prepareEmailBody(gender, employeename, deptName, emp.getDate_of_joining(), designationName);
	            String replacedStringForGeneralGuideline = prepareGeneralGuidelineEmailBody(gender, employeename, 
	            		empCode, emp.getEmail(), username, deptName, empSchool, shiftDetail, reportingOfficer, leaveApprover1name, leaveApprover2name);

	            // Add emails to the set to ensure unique email addresses
	            addUniqueEmails(allEmails, emailtoDept);
	            addUniqueEmails(allEmails, new String[]{emp.getEmail(), jobEmail});

	            // Send emails
	            sendMailToMultipleWithCC1(emailtoDept, emp.getEmail(), replacedString, "New Addition to Our Team");
	            sendMailToMultipleWithMultipleCC1(new String[]{emp.getEmail(), jobEmail},
	            		new String[]{reportingOfficerEmail, "divyakumari@acharya.ac.in"}, replacedStringForGeneralGuideline, "General Guidelines");

	            System.err.println("Mail Sent to " + emp.getEmail());
	        } catch (Exception e) {
	            System.err.println("Error sending email to employee " + emp.getEmail() + ": " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}

	private String getLeaveApproverName(Integer leaveApproverEmpId) {
	    Optional<EmployeeDetails> leaveApprover = empRepo.findById(leaveApproverEmpId);
	    return leaveApprover.isPresent() ? convertToTitleCase(leaveApprover.get().getEmployee_name()) : "";
	}

	private String getReportingOfficer(Integer reportId) {
	    Optional<EmployeeDetails> reportingEmail = empRepo.findById(reportId);
	    return reportingEmail.isPresent() ? convertToTitleCase(reportingEmail.get().getEmployee_name()) : "";
	}

	private String getReportingOfficerEmail(Integer reportId) {
	    Optional<EmployeeDetails> reportingEmail = empRepo.findById(reportId);
	    return reportingEmail.isPresent() ? reportingEmail.get().getEmail() : "divyakumari@acharya.ac.in";
	}

	private String getDepartmentName(Integer deptId) {
	    Optional<Department> department = deptRepo.findById(deptId);
	    return department.isPresent() ? department.get().getDept_name() : "";
	}

	private String getDesignationName(Integer designationId) {
	    Optional<Designation> designation = designationRepo.findById(designationId);
	    return designation.isPresent() ? designation.get().getDesignation_name() : "";
	}

	private String getJobEmail(Integer jobId) {
	    Optional<JobProfile> job = jobRepo.findById(jobId);
	    return job.isPresent() ? job.get().getEmail() : "divyakumari@acharya.ac.in";
	}

	private String getUsername(String email) {
	    Optional<UserAuthentication> userauthentication = uar_repo.findByEmailAndActiveTrue(email);
	    return userauthentication.isPresent() ? userauthentication.get().getUsername() : " ";
	}

	private String getShiftDetails(Integer shiftCategoryId) {
	    Optional<Shift> shiftDetails = shiftRepo.findById(shiftCategoryId);
	    return shiftDetails.isPresent() ? shiftDetails.get().getShiftName() + " (" + shiftDetails.get().getShiftStartTime() + " - " + shiftDetails.get().getShiftEndTime() + ")" : " ";
	}

	private void addUniqueEmails(Set<String> emailSet, String[] emails) {
	    for (String email : emails) {
	        if (email != null && !email.trim().isEmpty()) {
	            emailSet.add(email);
	        }
	    }
	}

	private String prepareEmailBody(String gender, String employeeName, String deptName, String dateOfJoining, String designationName) {
	    return mailTemplate1.replace("Mr", gender)
	            .replace("CHENCHANABOINA JAGADEESH YADAV", employeeName)
	            .replace("departmentName", deptName)
	            .replace("DOJ", dateOfJoining)
	            .replace("Software Developer", designationName);
	}

	private String prepareGeneralGuidelineEmailBody(String gender, String employeeName, String empCode, String email, String username, String deptName, String empSchool, String shiftDetail, String reportingOfficer, String leaveApprover1name, String leaveApprover2name) {
	    return generalGuidelinesMail.replace("Ms", gender)
	            .replace("Poonam Sharma", employeeName)
	            .replace("AI002599", empCode)
	            .replace("poonam2599@acharya.ac.in", email)
	            .replace("poonam2598", username)
	            .replace("SMT. NAGARATHNAMMA COLLEGE OF NURSING", empSchool)
	            .replace("PSYCHIATRIC NURSING", deptName)
	            .replace("Shift : Gen12 ( 09:00:00 - 16:45:00 )", shiftDetail)
	            .replace("DEVI NANJAPPAN", reportingOfficer)
	            .replace("leaveApprover1", leaveApprover1name)
	            .replace("leaveApprover2", leaveApprover2name)
	            .replace("loginUrl", "www.acharyaerptech.in")
	            .replace("&lt;dynamic&gt;", deptName)
	            .replace("Acharya University", empSchool);
	}

	public void sendMailToMultipleWithCC1(String[] email, String cc_emails, String contents, String subject) throws IOException {
	    MimeMessage message = mailSender.createMimeMessage();
	    try {
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
	        helper.setTo(email);
	        helper.setCc(cc_emails);
	        helper.setSubject(subject);
	        helper.setText(contents, true);
	    } catch (MessagingException e) {
	        throw new MailParseException(e);
	    }
	    mailSender.send(message);
	}

	public void sendMailToMultipleWithMultipleCC1(String[] email, String[] cc_emails, String contents, String subject) throws IOException {
	    MimeMessage message = mailSender.createMimeMessage();
	    try {
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
	        helper.setTo(email);
	        helper.setCc(cc_emails);
	        helper.setSubject(subject);
	        helper.setText(contents, true);
	    } catch (MessagingException e) {
	        throw new MailParseException(e);
	    }
	    mailSender.send(message);
	}


	

// Converted to API -> (/sendEmailForStudentOnboardingWithProvisionalAndBonafide/)	
//	@Scheduled(cron = "0 0 20 * * ?", zone = "Asia/Kolkata")
//	@Scheduled(cron="0 0/1 * * * *", zone = "Asia/Kolkata") //For Every Minute
//	public void generateAndSendProvisionalAdmissionPdf() throws Exception {
//
//		std_bonafide_Service.sendEmailForStudentOnboardingWithProvisionalAndBonafide();
//		
//    }
    
}
