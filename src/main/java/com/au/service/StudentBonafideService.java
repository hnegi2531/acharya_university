package com.au.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.au.dto.StudentOfferDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.StudentBonafide;
import com.au.model.Student_Details;
import com.au.repository.Academic_year_repository;
import com.au.repository.CandidateWalkinRepository;
import com.au.repository.ProgramRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.School_Repository;
import com.au.repository.StudentBonafideRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class StudentBonafideService {

	@Autowired
	private StudentBonafideRepository std_bonafide_repo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;

	@Autowired
	private Academic_year_repository ac_repo;
	
	@Autowired
	private ResponseHandler response_handler;
	
	@Autowired
	private ReportingStudentsRepository reportingStudent_repo;
	
	@Autowired
	private ProgramRepository pro_repo;
	
	@Autowired
	private ProgramSpecilizationRepository ps_repo;
	
	@Autowired
	private CandidateWalkinRepository can_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private School_Repository sc_repo;
	
//	@Autowired
//	private JavaMailSender mailSender;
	
	  @Autowired
	    @Qualifier("mailSenderPrimary")  // ✅ Explicitly use the primary mail sender
	    private JavaMailSender mailSender;
	
	@Autowired
	private Environment env;
	

	@Value("${letterheads.path}")
	private String LETTERHEADSPATH;
	
	
	//for local(change in application.properties file)
	//private String LETTERHEADSPATH = "C:\\Users\\Admin\\kaushal\\au\\au\\Acharya_University\\Letter-heads\\";
	
//	//for PROD
//	private final static String LETTERHEADSPATH = "D:\\git\\au\\Acharya_University\\Letter-heads\\";
	
	public StudentBonafide save_studentbonafide(StudentBonafide stdbonafide) throws Exception {
		
		Integer studentId = stu_repo.getStudentIds(stdbonafide.getAuid());
		Integer currentReportingSem = reportingStudent_repo.getSem(studentId);
		Integer currentReportingYear = reportingStudent_repo.getYear(studentId);
		
		stdbonafide.setCurrent_sem(currentReportingSem);
		stdbonafide.setCurrent_year(currentReportingYear);
		
		Integer bonafideCount=	std_bonafide_repo.getCount(stdbonafide.getAuid(),stdbonafide.getBonafide_type(),stdbonafide.getCurrent_sem(),stdbonafide.getCurrent_year());
		System.out.println("--------------currentReportingSem---------- " + stdbonafide.getCurrent_sem());
		System.out.println("--------------currentReportingYear---------- " + stdbonafide.getCurrent_year());
		System.out.println("--------------count---------- " + bonafideCount);
		
		if (bonafideCount >= 1) {
			throw new Exception("Auid is already created for this type !!");
		}else {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		LocalDate date1 = null;
		date1 = LocalDate.parse(date);
		Integer currentYear = ac_repo.getCurrentYear();
		Integer year = Integer.parseInt(Integer.toString(currentYear).substring(2)); 
		StudentBonafide latestBonafideNumber = std_bonafide_repo.getLatestBonafideNumber();
		
		String bonadide_number = null;
		String count_for_id = std_bonafide_repo.getMaxLatestBonafideNumberCount();
		
		if (count_for_id == null) {
			bonadide_number = String.format("%04d", 1);
		}else {
			String abc = count_for_id.substring(10);
			Integer count = Integer.valueOf(abc) + 1;
			bonadide_number = String.format("%04d", count);
		}
		stdbonafide.setBonafide_number("OW/"+"STD/" +year+"/"+bonadide_number );
		
		return	std_bonafide_repo.save(stdbonafide);
		}
	}
	
	public List<StudentBonafide> listAll1() {
		return std_bonafide_repo.findAll11();
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> stdbonafide_filtered_response = std_bonafide_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, stdbonafide_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> stdbonafide_sorted_response = std_bonafide_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, stdbonafide_sorted_response);
	}
	
	public StudentBonafide get(Integer id) {
		return std_bonafide_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student Bonafide Not Found:" + id));
	}
	
	
	public StudentBonafide savestudentBonafide1(StudentBonafide stdbonafide) {
		return std_bonafide_repo.save(stdbonafide);
	}
	
	public void delete(Integer id) {
		StudentBonafide stdbonafide = std_bonafide_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Import Transaction Not Found:" + id));
		std_bonafide_repo.delete1(id);
	}

	public void delete1(Integer id) {
		StudentBonafide stdbonafide = std_bonafide_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Import Transaction Not Found:" + id));
		std_bonafide_repo.delete2(id);
	}
	

	public List<String> list1() {
		return std_bonafide_repo.list1();
	}
	
	public List<Map<String,Object>> getStudentBonafideDetails(String auid, String bonafide_type) {
		return std_bonafide_repo.getStudentBonafideDetails(auid,bonafide_type);
	}
	
	public Map<String,Object> studentBonafideAddOnDetails(String auid, String bonafide_type) {
		
		Map<String, Object> data  = new HashMap<String, Object>();
		
		 List<Map<String, Object>> studentBonafideUniformAndStationaryDetails = std_bonafide_repo.studentBonafideUniformAndStationaryDetails(auid,bonafide_type);
		 List<Map<String, Object>> studentBonafideAddOnDetails = std_bonafide_repo.studentBonafideAddOnDetails(auid,bonafide_type);
		 
		 data.put("uniformAndStationaryData", studentBonafideUniformAndStationaryDetails);
		 data.put("AddOn", studentBonafideAddOnDetails);
		 
		 return data;
	}

	public List<Map<String, Object>> studentBonafideDetailsDropDown(String auid) {
		return std_bonafide_repo.studentBonafideDetailsDropDown(auid);
	}
	
	public void sendEmailForStudentOnboardingWithProvisionalAndBonafide(StudentOfferDto sd) throws MessagingException, IOException {
		Student_Details studentDetails = stu_repo.getOne(sd.getStudent_id());
		String counsellorEmail = can_repo.getCounsellorEmail(studentDetails.getCandidate_id());
		List<String> emails = Arrays.asList("studentspoc@acharya.ac.in" ,counsellorEmail);
		String[] strarray = emails.toArray(new String[0]);
	    String subject = "Welcome Aboard !";
	    String content = getMailBody(studentDetails);

		response_handler.sendEmailWithMultipleAttachment(sd.getFiles(), strarray,
				studentDetails.getStudent_email(), content, subject);

		System.out.println("PDF GENERATED...");
		System.out.println("Mail Sent...");

		response_handler.delete(0, ".pdf");
		
	}
	
	private String getMailBody(Student_Details studentDetails) {
    	String schoolName = sc_repo.getSchoolName(studentDetails.getSchool_id());
    	String content = "Dear " + studentDetails.getStudent_name() + "," + "<br><br>"
	    		+ "Congratulations & welcome to the Acharya Family !!!" + "<br><br>"
				+ "As a part of your onboarding process, you are required to register yourselves with Acharya ERP Portal. "
				+ "This portal shall enable access to your student portal, through which you shall have access to online classes, "
				+ "know your time table, update on your grades and attendance as well. The student portal also acts a contactless option to know your fee, "
				+ "the due dates and also pay the fee online. " + "<br><br>"
				+ "To access the student portal you may please login using the link below:" + "<br><br>"
				+ "https://www.acharyaerptech.in" + "<br><br>"
				+ "User ID : " + studentDetails.getAuid() + "<br>"
				+ "Password : acharya1234" + "<br><br>"
				+ "You may also access the student portal through your mobile phones using the following links: " + "<br><br>"
				+ "Android: https://play.google.com/store/apps/details?id=com.ait.acerp&hl=en_US" + "<br>"
				+ "iOS: https://apps.apple.com/us/app/acharya-erp/id1486096886" + "<br><br>"
				+ "NOTE: Users already registered are also requested to re-register to access our updated version." + "<br><br>"
				+ "Best Regards," + "<br><br>"
				+ "Team Admissions," + "<br>"
				+ "Acharya Institutes," + "<br>"
				+ "Bangalore - 560107." + "<br>"
				+ "Email - admissions@acharya.ac.in" +  "<br><br><br><br><br><br>"
				+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
				+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
    	return content;
    }

	public ResponseEntity<Object> updateHostelFeeTemplateId(Integer studentBonaFideId, Integer hostelFeeTemplateId) {
		try {
			Optional<StudentBonafide> optionalStudentBonafide = std_bonafide_repo.findById(studentBonaFideId);
			if(!optionalStudentBonafide.isPresent())
				return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "Student Bonafide not exist with id: " + studentBonaFideId);
			optionalStudentBonafide.get().setHostel_fee_template_id(hostelFeeTemplateId);
			std_bonafide_repo.save(optionalStudentBonafide.get());
		}catch (Exception e){
			e.printStackTrace();
		}
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Updated Successfully!!!");
	}

// ------------------------------------------------------------------------------------------------------------------------------------	
// ------------------------------------------------------------------------------------------------------------------------------------	
	
	
//	public void sendEmailForStudentOnboardingWithProvisionalAndBonafide(String auid) throws Exception {
//
//
////		List<Student_Details> students = stu_repo.studentAdmissionDataByCurrentDate();
////		for (Student_Details stu : students) {
//
//		
//			Student_Details studentDetails = stu_repo.findByAuid(auid);
//			String counsellorEmail = can_repo.getCounsellorEmail(studentDetails.getCandidate_id());
//		    String outputPath = "C:\\Offer and Salary Breakup\\ProvisionalAdmission_" + studentDetails.getAuid() + ".pdf";
//		    String outputPath1 = "C:\\Offer and Salary Breakup\\StudentBonafide_" + studentDetails.getAuid() + ".pdf";
//		
////		    if(studentDetails.getNationality().equalsIgnoreCase("103")) {
////			    generateProvisionalAdmissionPdf(outputPath, studentDetails);
////			    generateStudentBonafidePdf(outputPath1, studentDetails);
////			    ByteArrayOutputStream outputStream = null;
////
////				// now write the PDF content to the output stream
////				outputStream = new ByteArrayOutputStream();
////			    byte[] bytes = outputStream.toByteArray();
////				// construct the pdf body part
////				DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
////
////				MimeBodyPart pdfBodyPart = new MimeBodyPart();
////				pdfBodyPart.setDataHandler(new DataHandler(dataSource));
////				pdfBodyPart.setFileName("Provisional_" + studentDetails.getStudent_name() + ".pdf");
////				
////			    String[] recipients = new String[] { studentDetails.getAcharya_email() };
////			//    String[] recipients = new String[] { candidateWalkin.getCandidate_email() }; 
////			    String subject = "Provisional Admission Certificate";
////			    String content = getContent(studentDetails);
////			    		
////			
////			    String[] ccEmails = null; // Add CC emails if needed
////			    MimeBodyPart textBodyPart = new MimeBodyPart();
////				textBodyPart.setText(content);
////
////				MimeMultipart mimeMultipart = new MimeMultipart();
////				mimeMultipart.addBodyPart(textBodyPart);
////				mimeMultipart.addBodyPart(pdfBodyPart);
////
////				MimeMessage message = mailSender.createMimeMessage();
////				try {
////					MimeMessageHelper helper = new MimeMessageHelper(message, true);
////					helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
////					helper.setTo(studentDetails.getAcharya_email());
////					helper.setSubject(subject);
////					helper.setText(content, true);
////					File file = new File("C:\\Offer and Salary Breakup\\ProvisionalAdmission_" + studentDetails.getAuid() + ".pdf");
////					File file1 = new File("C:\\Offer and Salary Breakup\\StudentBonafide_" + studentDetails.getAuid() + ".pdf");
////					helper.addAttachment("ProvisionalAdmission_" + studentDetails.getAuid() + ".pdf", file);
////					helper.addAttachment("StudentBonafide_" + studentDetails.getAuid() + ".pdf", file1);
////				} catch (MessagingException e) {
////					throw new MailParseException(e);
////				}
////
////				mailSender.send(message);
////
////				System.out.println("PDF GENERATED...");
////				System.out.println("Mail Sent...");
////
////				response_handler.delete(0, ".pdf");
////				
////		    } else {
//		    	
//		    	generateProvisionalAdmissionPdf(outputPath, studentDetails);
//		    	System.out.println("LETTERHEADSPATH :"+LETTERHEADSPATH);
//			    ByteArrayOutputStream outputStream = null;
//
//				// now write the PDF content to the output stream
//				outputStream = new ByteArrayOutputStream();
//			    byte[] bytes = outputStream.toByteArray();
//				// construct the pdf body part
//				DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
//
//				MimeBodyPart pdfBodyPart = new MimeBodyPart();
//				pdfBodyPart.setDataHandler(new DataHandler(dataSource));
//				pdfBodyPart.setFileName("Provisional_" + studentDetails.getStudent_name() + ".pdf");
//				
//			    String[] recipients = new String[] { studentDetails.getAcharya_email() };
//			//    String[] recipients = new String[] { candidateWalkin.getCandidate_email() }; 
//			    String subject = "Welcome Aboard !";
//			    String content = getContent(studentDetails);
//			
//			    String[] ccEmails = null; // Add CC emails if needed
//			    MimeBodyPart textBodyPart = new MimeBodyPart();
//				textBodyPart.setText(content);
//
//				MimeMultipart mimeMultipart = new MimeMultipart();
//				mimeMultipart.addBodyPart(textBodyPart);
//				mimeMultipart.addBodyPart(pdfBodyPart);
//
//				MimeMessage message = mailSender.createMimeMessage();
//				try {
//					MimeMessageHelper helper = new MimeMessageHelper(message, true);
//					helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
//					helper.setTo(studentDetails.getStudent_email());
//					helper.setCc(counsellorEmail);
//					helper.setSubject(subject);
//					helper.setText(content, true);
//					File file = new File("C:\\Offer and Salary Breakup\\ProvisionalAdmission_" + studentDetails.getAuid() + ".pdf");
//					helper.addAttachment("ProvisionalAdmission_" + studentDetails.getAuid() + ".pdf", file);
//				} catch (MessagingException e) {
//					throw new MailParseException(e);
//				}
//
//				mailSender.send(message);
//
//				System.out.println("PDF GENERATED...");
//				System.out.println("Mail Sent...");
//
//				response_handler.delete(0, ".pdf");
////		    }
//		    
//
////		}
//}
//
//
//	
//    private String getContent(Student_Details studentDetails) {
//    	String schoolName = sc_repo.getSchoolName(studentDetails.getSchool_id());
//    	String content = "Dear " + studentDetails.getStudent_name() + "," + "<br><br>"
//	    		+ "Congratulations & welcome to the Acharya Family !!!" + "<br><br>"
//				+ "As a part of your onboarding process, you are required to register yourselves with Acharya ERP Portal. "
//				+ "This portal shall enable access to your student portal, through which you shall have access to online classes, "
//				+ "know your time table, update on your grades and attendance as well. The student portal also acts a contactless option to know your fee, "
//				+ "the due dates and also pay the fee online. " + "<br><br>"
//				+ "To access the student portal you may please login using the link below:" + "<br><br>"
//				+ "https://www.acharyaerptech.in" + "<br><br>"
//				+ "User ID : " + studentDetails.getAuid() + "<br>"
//				+ "Password : acharya1234" + "<br><br>"
//				+ "You may also access the student portal through your mobile phones using the following links: " + "<br><br>"
//				+ "Android: https://play.google.com/store/apps/details?id=com.ait.acerp&hl=en_US" + "<br>"
//				+ "iOS: https://apps.apple.com/us/app/acharya-erp/id1486096886" + "<br><br>"
//				+ "NOTE: Users already registered are also requested to re-register to access our updated version." + "<br><br>"
//				+ "Best Regards," + "<br><br>"
//				+ "Team Admissions," + "<br>"
//				+ "Acharya Institutes," + "<br>"
//				+ "Bangalore - 560107." + "<br>"
//				+ "Email - admissions@acharya.ac.in" +  "<br><br><br><br><br><br>"
//				+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
//				+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
//    	return content;
//    }
//
//	public void generateProvisionalAdmissionPdf(String outputPath, Student_Details studentDetails) throws Exception {
//    	Font boldFont1 = new Font(FontFactory.getFont("Times-Roman", 10, Font.BOLD));
//    	Font boldFont2 = new Font(FontFactory.getFont("Times-Roman", 11, Font.BOLD));
//    	Font normalFont1 = new Font(FontFactory.getFont("Times-Roman", 11));
//    	Font normalFont2 = new Font(FontFactory.getFont("Times-Roman", 10));
//    	
//    	String acYear = ac_repo.getAcademicYear(studentDetails.getAc_year_id());
//    	String programName = pro_repo.getOne(studentDetails.getProgram_id()).getProgram_name();
//    	String programSpecializationName = ps_repo.getOne(studentDetails.getProgram_specialization_id()).getProgram_specialization_name();
//    	String counsellorName = can_repo.getCounsellorName(studentDetails.getCandidate_id());
//    	Document document = new Document(PageSize.A4, 25, 25, 0, 25);
//        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
//        document.open();
//        
//        // Add header image
////        String headerImagePath = "C:\\Users\\Admin\\git\\au\\Acharya_University\\Letter-heads\\provisionalHeader.jpg"; // for testing in local
//        String headerImagePath = LETTERHEADSPATH + "provisionalHeader.jpg";
//        Image headerImage = Image.getInstance(headerImagePath);
//        headerImage.scaleToFit(600, 200);  // Adjust the image size to fit the document
//        headerImage.setAlignment(Element.ALIGN_CENTER);  // Center the image
//        document.add(headerImage);
//
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        String formattedDate = today.format(formatter);
//        
//        PdfPTable table = new PdfPTable(2); // Create a table with 2 columns
//        table.setWidthPercentage(100); // Ensure the table takes up 100% of the page width
//        table.setWidths(new float[]{3, 1}); // Adjust the column widths
//
//     // Create the second cell for the greeting (aligned left)
//        PdfPCell greetingCell = new PdfPCell();
//        greetingCell.setBorder(Rectangle.NO_BORDER);
//        greetingCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        greetingCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Center vertically
//        
//        Paragraph greetingsParagraph = new Paragraph();
////        greetingCell.add(new Chunk("Congratulations ", normalFont1));
//        greetingsParagraph.add(new Chunk("Congratulations ", normalFont1));
//        greetingsParagraph.add(new Chunk(studentDetails.getStudent_name() + " !!", boldFont2));
//        greetingCell.addElement(greetingsParagraph);
//        table.addCell(greetingCell);
//        
//        // Create the first cell for the date (aligned right)
//        PdfPCell dateCell = new PdfPCell(new Phrase(formattedDate, normalFont1));
//        dateCell.setBorder(Rectangle.NO_BORDER);
//        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Center vertically
//        table.addCell(dateCell);
//
//        
//
//        // Add the table to the document
//        document.add(table);
//
//
//
//        // Add body content
//        Paragraph content = new Paragraph();
//        content.add(new Chunk("We are pleased to inform you that your", normalFont1));
//        content.add(new Chunk(" Provisional Admission ", boldFont2));
//        content.add(new Chunk("has been completed. Below are the relevant details:\n \n", normalFont1));
//        content.setAlignment(Element.ALIGN_JUSTIFIED);
//        content.setSpacingBefore(15);
//        document.add(content);
//
//        PdfPTable table1 = new PdfPTable(2);
//        table1.setWidths(new int[] { 1, 1 });
//        table1.setWidthPercentage(80);
//        table1.setHorizontalAlignment(Element.ALIGN_LEFT);       
//        
//        PdfPCell CellA = new PdfPCell(new Phrase("Program", boldFont1));
//        CellA.setHorizontalAlignment(Element.ALIGN_LEFT);
//        CellA.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        CellA.setExtraParagraphSpace(2);
//		table1.addCell(CellA);
//		
//		PdfPCell CellB = new PdfPCell(new Phrase(programName, normalFont2));
//		CellB.setHorizontalAlignment(Element.ALIGN_LEFT);
//		CellB.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		CellB.setExtraParagraphSpace(2);
//		table1.addCell(CellB);
//		
//        PdfPCell CellC = new PdfPCell(new Phrase("Specialization", boldFont1));
//        CellC.setHorizontalAlignment(Element.ALIGN_LEFT);
//        CellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        CellC.setExtraParagraphSpace(2);
//		table1.addCell(CellC);
//		
//		PdfPCell CellD = new PdfPCell(new Phrase(programSpecializationName, normalFont2));
//		CellD.setHorizontalAlignment(Element.ALIGN_LEFT);
//		CellD.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		CellD.setExtraParagraphSpace(2);
//		table1.addCell(CellD);
//		
//        PdfPCell CellE = new PdfPCell(new Phrase("Academic Year", boldFont1));
//        CellE.setHorizontalAlignment(Element.ALIGN_LEFT);
//        CellE.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        CellE.setExtraParagraphSpace(2);
//		table1.addCell(CellE);
//		
//		PdfPCell CellF = new PdfPCell(new Phrase(acYear, normalFont2));
//		CellF.setHorizontalAlignment(Element.ALIGN_LEFT);
//		CellF.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		CellF.setExtraParagraphSpace(2);
//		table1.addCell(CellF);
//
//        document.add(table1);
//        
//        
//        // Add email details
//        Paragraph emailDetails1 = new Paragraph();
//        emailDetails1.add(new Chunk("Acharya Unique Identification (AUID) : ", boldFont2));
//        emailDetails1.add(new Chunk("Your AUID is ", normalFont1));
//        emailDetails1.add(new Chunk(studentDetails.getAuid(), boldFont2));
//        emailDetails1.add(new Chunk(". Please ensure that you reference this AUID in all future communication and transactions with the institution.", normalFont1)); 
//        
//        emailDetails1.setAlignment(Element.ALIGN_JUSTIFIED);
//        emailDetails1.setSpacingBefore(5);
//        document.add(emailDetails1);
//        
//        Paragraph emailDetails2 = new Paragraph();
//        emailDetails2.add(new Chunk("Acharya Email Address : ", boldFont2));
//        emailDetails2.add(new Chunk("All Acharya students are assigned a dedicated college email address. You are encouraged to use "
//        		+ "this account as your primary means of receiving important information and communicating with the college. Your Acharya "
//        		+ "email will become active within ", normalFont1));
//        emailDetails2.add(new Chunk("2 working days ", boldFont2));
//        emailDetails2.add(new Chunk("from the date of admission.\n", normalFont1)); 
//                
//        emailDetails2.setAlignment(Element.ALIGN_JUSTIFIED);
//        emailDetails2.setSpacingBefore(10);
//        emailDetails2.setSpacingAfter(10);
//        document.add(emailDetails2);
//
//        PdfPTable table2 = new PdfPTable(2);
//        table2.setWidths(new int[] { 1, 1 });
//        table2.setWidthPercentage(80); 
//        table2.setHorizontalAlignment(Element.ALIGN_LEFT);
//
//        PdfPCell Cell1 = new PdfPCell(new Phrase("Email ID ", boldFont1));
//        Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
//        Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        Cell1.setExtraParagraphSpace(2);
//		table2.addCell(Cell1);
//		
//		PdfPCell Cell2 = new PdfPCell(new Phrase(studentDetails.getAcharya_email(), normalFont2));
//		Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
//		Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		Cell2.setExtraParagraphSpace(2);
//		table2.addCell(Cell2);
//		
//        PdfPCell Cell3 = new PdfPCell(new Phrase("ERP User ID ", boldFont1));
//        Cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
//        Cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        Cell3.setExtraParagraphSpace(2);
//		table2.addCell(Cell3);
//		
//		PdfPCell Cell4 = new PdfPCell(new Phrase(studentDetails.getAuid(), normalFont2));
//		Cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
//		Cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		Cell4.setExtraParagraphSpace(2);
//		table2.addCell(Cell4);
//		
//		 PdfPCell Cell5 = new PdfPCell(new Phrase("Passsword for Email and ERP Login ", boldFont1));
//	        Cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
//	        Cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//	        Cell5.setExtraParagraphSpace(2);
//			table2.addCell(Cell5);
//			
//			PdfPCell Cell6 = new PdfPCell(new Phrase("acharya1234", normalFont2));
//			Cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
//			Cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			Cell6.setExtraParagraphSpace(2);
//			table2.addCell(Cell6);
//			
//			 PdfPCell Cell7 = new PdfPCell(new Phrase("ERP Web Access Link ", boldFont1));
//		        Cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
//		        Cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		        Cell7.setExtraParagraphSpace(2);
//				table2.addCell(Cell7);
//				
//				PdfPCell Cell8 = new PdfPCell(new Phrase("https://www.acharyaerptech.in", normalFont2));
//				Cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
//				Cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
//				Cell8.setExtraParagraphSpace(2);
//				table2.addCell(Cell8);
//        
//        document.add(table2);
//
//        
//        // Add ERP details
//        Paragraph erpDetails = new Paragraph("The ERP provides you access to your programme details, information on your teachers, timetable, attendance, pay fee and "
//        		+ "most importantly connect with your Mentor. The information may be accessed on a web browser or by using the ERP App "
//        		+ "on your mobile phone. You may download the app using the QR codes below. The portal allows multiple logins, allowing "
//        		+ "parents to use the same to be updated on their ward's academic progress.\n", normalFont1);
//        
//        erpDetails.setAlignment(Element.ALIGN_JUSTIFIED);
//        erpDetails.setSpacingBefore(5);
//        document.add(erpDetails);
//        
////        Paragraph erpDetails1 = new Paragraph("The ERP provides you access to your programme details, information on your teachers, timetable, attendance, pay fee and most "
////        + "importantly connect with your Proctor. The information may be accessed on a web browser or by using the ERP App on your mobile phone. "
////        + "You may download the app using the QR codes below. The portal allows multiple logins, allowing parents to use "
////        + "the same to be updated on their ward’s academic progress.\n", normalFont1);
////        
////        erpDetails1.setAlignment(Element.ALIGN_JUSTIFIED);
////        erpDetails1.setSpacingBefore(10);        
////        document.add(erpDetails1);
//        
//        Paragraph erpDetails2 = new Paragraph();
//        erpDetails2.add(new Chunk("Counsellor Assistance: Should you require any further details or assistance, please feel free to contact ", normalFont1));
//        erpDetails2.add(new Chunk(counsellorName, boldFont2));
//        erpDetails2.add(new Chunk(", your designated counsellor and they would be glad to help you navigate any queries or concerns.", normalFont1)); 
//        
//        erpDetails2.setAlignment(Element.ALIGN_JUSTIFIED);
//        erpDetails2.setSpacingBefore(10);        
//        document.add(erpDetails2);
//
//        
//        // Add additional information
//        Paragraph additionalInfo = new Paragraph(
//        "Once again, congratulations on taking this important step in your academic journey. We look forward to welcoming you to Acharya and wish you every success in your studies.\n\n", normalFont1);
//        additionalInfo.setAlignment(Element.ALIGN_JUSTIFIED);
//        additionalInfo.setSpacingBefore(10);
//        document.add(additionalInfo);
//        
// //       String footerImagePath = "C:\\Users\\Admin\\git\\au\\Acharya_University\\Letter-heads\\provisionalFooter.jpg"; // for testing in local
//        String footerImagePath = LETTERHEADSPATH + "provisionalFooter.jpg";
//        
////        Image footerImage = Image.getInstance(footerImagePath);
////        footerImage.scaleToFit(600, 200);  // Adjust the image size to fit the document
////        footerImage.setAlignment(Element.ALIGN_CENTER);  // Center the image
////        document.add(footerImage);
//        
//        FooterImageEvent footerEvent = new FooterImageEvent(footerImagePath);
//        writer.setPageEvent(footerEvent);
//        
//        document.close();
//    }
//    
//    public class FooterImageEvent extends PdfPageEventHelper {
//        private Image footerImage;
//
//        public FooterImageEvent(String imagePath) throws BadElementException, MalformedURLException, IOException {
//            // Load the footer image from the provided path
//            this.footerImage = Image.getInstance(imagePath);
//            this.footerImage.setAlignment(Element.ALIGN_CENTER);
//            this.footerImage.scaleToFit(600, 200); // Adjust size as needed
//        }
//
//        @Override
//        public void onEndPage(PdfWriter writer, Document document) {
//            // Calculate the X and Y positions to place the footer at the bottom
//            float x = (document.right() - document.left() - footerImage.getScaledWidth()) / 2 + document.leftMargin();
//            float y = document.bottomMargin() - 35; // Adjust Y position as needed
//            
//            // Set the position and add the footer image
//            footerImage.setAbsolutePosition(x, y);
//            try {
//                PdfContentByte canvas = writer.getDirectContentUnder();
//                canvas.addImage(footerImage);
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Method to send an email with attachment
//    public void sendEmailWithAttachment(String[] recipients, String subject, String content, String attachmentPath) throws Exception {
//        // Call the sendMultipleSimpleEmail method of your email handler
//        response_handler.sendMultipleEmail1(recipients, content, subject, attachmentPath); // No CC emails
//    }
//    
//    int yearValue;
//    public void generateStudentBonafidePdf(String outputPath1, Student_Details studentDetails) throws Exception {
//    	Font boldFont1 = new Font(FontFactory.getFont("TimesNewRomanPSMT", 10, Font.BOLD));
//    	Font boldUnderlineFont = FontFactory.getFont("TimesNewRomanPSMT", 10, Font.BOLD | Font.UNDERLINE);
//    	Font normalFont1 = new Font(FontFactory.getFont("TimesNewRomanPSMT", 10));
//        Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 9);
//
//    	try {
//    	Student_Details studentDetails1 = stu_repo.getOne(studentDetails.getStudent_id());
//    	
//    	String genderSaluation = null;
//		
//		if (studentDetails1.getCandidate_sex().equals("Male")) 
//			genderSaluation = "Mr.";
//		else
//			genderSaluation = "Ms.";
//
//
//		    String bonafide_type = "Provisional Bonafide";
//		    List<Map<String,Object>> studentBonafideDetails =  std_bonafide_repo.getStudentBonafideDetails( studentDetails1.getAuid(), bonafide_type);
//		    List<Map<String,Object>> studentBonafideAddOnDetails = std_bonafide_repo.studentBonafideAddOnDetails( studentDetails1.getAuid(),  bonafide_type);
//		    
//		    if (studentBonafideDetails == null || studentBonafideDetails.isEmpty()) {
//		        throw new IllegalArgumentException("No bonafide details available for the student.");
//		    }
//		    
//		    // Check if directory exists, if not, create it
//		    File outputFile = new File(outputPath1);
//		    File parentDirectory = outputFile.getParentFile();
//		    if (parentDirectory != null && !parentDirectory.exists()) {
//		        if (!parentDirectory.mkdirs()) {
//		            throw new IOException("Failed to create directory: " + parentDirectory.getAbsolutePath());
//		        }
//		    }
//		    System.out.println("////////////////////////////////////////////////");
//		    String todaysDate = ResponseHandler.getStringTypeTodaysDateDDMMYYYY();
//		    
//		    Document document = new Document(PageSize.A4, 36, 36, 20, 36);
////	        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
//	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath1));
//	        document.open();
//	     
//	        
//	        Paragraph header = new Paragraph("RefNo : " + studentBonafideDetails.get(0).get("bonafide_number")
//					+ "                                                                                                                  "
//					+ "Date : " + todaysDate + "\r\n", boldFont1);
//	        header.setAlignment(Element.ALIGN_CENTER);
//	        document.add(header);
//	        System.out.println("////////////////////////////////////////////////");
//	     // Title
//	        Paragraph title = new Paragraph("TO WHOM SO EVER IT MAY CONCERN", boldUnderlineFont);
//	        title.setAlignment(Element.ALIGN_CENTER);
//	        title.setSpacingBefore(20);
//	        title.setSpacingAfter(20);
//	        document.add(title);
//	        System.out.println("2222222222222222222222222222222");
//	     // Body
//	        Paragraph body = new Paragraph();
//	        body.add(new Chunk("This is to certify that ", normalFont1));
//	        body.add(new Chunk(genderSaluation + studentDetails.getStudent_name(), boldFont1));
//	        body.add(new Chunk(", S/o. " + studentDetails.getFather_name(), boldFont1));
//	        body.add(new Chunk(", AUID No. ", normalFont1));
//	        body.add(new Chunk(studentDetails.getAuid(), boldFont1));
//	        body.add(new Chunk(" is provisionally admitted to ", normalFont1));
//	        body.add(new Chunk(studentBonafideDetails.get(0).get("school_name").toString(), boldFont1));
//	        body.add(new Chunk(" in ", normalFont1));
//	        body.add(new Chunk(studentBonafideDetails.get(0).get("program_short_name").toString(), boldFont1));
//	        body.add(new Chunk(" - ", normalFont1));
//	        body.add(new Chunk(studentBonafideDetails.get(0).get("program_specialization_name").toString(), boldFont1));
//	        body.add(new Chunk(" on merit basis after undergoing the selection procedure laid down by ", normalFont1));
//	        body.add(new Chunk(studentBonafideDetails.get(0).get("school_name").toString(), boldFont1));
//	        body.add(new Chunk(" for the Academic year ", normalFont1));
//	        body.add(new Chunk(studentBonafideDetails.get(0).get("ac_year").toString(), boldFont1));
//	        body.add(new Chunk(", subject to fulfilling the eligibility conditions prescribed by the affiliating University. The fee payable during the Academic Batch  ", normalFont1));
//	        body.add(new Chunk(studentBonafideDetails.get(0).get("academic_batch").toString(), boldFont1));
//	        body.add(new Chunk(" is given below.", normalFont1));
//	        
//	        body.setSpacingAfter(20);
//	        body.setAlignment(Element.ALIGN_JUSTIFIED);
//	        document.add(body);
//	        System.out.println("333333333333333333333333333");
//            Integer numberOfSemesters =(Integer) studentBonafideDetails.get(0).get("number_of_semester"); // Example value, replace with your dynamic value
//            float[] columnWidths = new float[numberOfSemesters + 1]; // +1 for "Voucher Head" column
//
//            // Set the first column width for "Voucher Head"
//            columnWidths[0] = 200f;
//
//            // Set the rest of the columns for semesters
//            for (int i = 1; i <= numberOfSemesters; i++) {
//                columnWidths[i] = 60f;
//            }
//            System.out.println("////////////////////////////////////////////////");
//            System.out.println("////////////////////////////////////////////////");
//            PdfPTable table = new PdfPTable(columnWidths);
//            table.setWidthPercentage(100);
//            // Add table headers
//            PdfPCell particularsCell = new PdfPCell(new Paragraph("Particulars", boldFont1));
//            particularsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(particularsCell);
//            for (int i = 1; i <= numberOfSemesters; i++) {
//            	 PdfPCell semesterCell = new PdfPCell(new Paragraph("Sem " + i, boldFont1));
//            	 semesterCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                 table.addCell(semesterCell);
//            }
//
//            // Use Streams to loop over the data and populate the table
//            studentBonafideDetails.forEach(record -> {
//            	 String voucherHead = record.get("voucher_head").toString();
//            	 PdfPCell voucherHeadCell = new PdfPCell(new Paragraph(voucherHead, normalFont1));
//            	 voucherHeadCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//	             table.addCell(voucherHeadCell);
//            	    // Dynamically add semester columns based on number_of_semester
//            	    for (int i = 1; i <= numberOfSemesters; i++) {
//            	        String columnKey = "year" + i + "_amt"; // Dynamically construct the column key, e.g., "year1_amt", "year2_amt"
//            	        String columnValue = record.getOrDefault(columnKey, "0").toString(); // Fallback to "0" if the value is null
//            	        PdfPCell voucherHeadAmountCell = new PdfPCell(new Paragraph(columnValue, normalFont1));
//            	        voucherHeadAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//       	             	table.addCell(voucherHeadAmountCell);
//            	    }
//            });
//            System.out.println("////////////////////////////////////////////////");
//            // Total Row
//            PdfPCell totalCell = new PdfPCell(new Paragraph("Total", boldFont1));
//            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(totalCell);
//            for (yearValue = 1; yearValue <= numberOfSemesters; yearValue++) {
//                int total = studentBonafideDetails.stream()
//                    .mapToInt(record -> Integer.parseInt(record.getOrDefault("year" + yearValue + "_amt", "0").toString()))
//                    .sum();
//                PdfPCell totalAmountCell = new PdfPCell(new Paragraph(String.valueOf(total), boldFont1));
//                totalAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                table.addCell(totalAmountCell);
//            }
//            
//            System.out.println("////////////////////////////////////////////////"+studentBonafideAddOnDetails.size());
//            Paragraph amountInRupees = new Paragraph("(Amount in Rupees)", smallFont);
//            amountInRupees.setAlignment(Element.ALIGN_RIGHT);
//            amountInRupees.setSpacingAfter(2);
//    	    document.add(amountInRupees);
//    	    
//            document.add(table);
//            
//            document.add(Chunk.NEWLINE); // Add spacing between tables
//            
//            //generate addOn table            
//            for (Map<String, Object> record : studentBonafideAddOnDetails) {
//                // Filter valid semesters
//                List<String> validSemesters = new ArrayList<>();
//                List<Float> validValues = new ArrayList<>();
//
//                IntStream.rangeClosed(1, 12).forEach(i -> {
//                	
//                    String key = "sem" + i;
//                    System.out.println("////////////////////////////////////////////////"+key);
//                    Object value = record.get(key);
//                    System.out.println("////////////////////////////////////////////////"+value);
//                    System.out.println("////////////////////////////////////////////////"+(value != null && (Float) value > 0.0));
//                    if (value != null && (Float) value > 0.0) {
//                        validSemesters.add("sem" + i);
//                        validValues.add((Float) value);
//                    }
//                });
//
//                float[] columnWidths1 = new float[validSemesters.size() + 1]; // +1 for "Voucher Head" column
//
//                // Set the first column width for "Voucher Head"
//                columnWidths1[0] = 200f;
//
//                // Set the rest of the columns for semesters
//                for (int i = 1; i <= validSemesters.size(); i++) {
//                    columnWidths1[i] = 60f;
//                }
//                // Create table for valid semesters
//                PdfPTable table1 = new PdfPTable(columnWidths1); // +1 for the first column
//                table1.setWidthPercentage(100);
//
//                	System.out.println("////////////////////////////////////////////////////////////////////////////////////////// "+validSemesters);
//                
//                // Header Row
//                PdfPCell particularsCell1 = new PdfPCell(new Paragraph("Particulars", boldFont1));
//                particularsCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table1.addCell(particularsCell1);
//                for (String semester : validSemesters) {
//                    PdfPCell semesterCell1 = new PdfPCell(new Paragraph(semester, boldFont1));
//                    semesterCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    table1.addCell(semesterCell1);
//                }
//
//                // Data Row
//                PdfPCell particularsCellAddOn = new PdfPCell(new Paragraph(record.get("voucher_head").toString(), normalFont1));
//                particularsCellAddOn.setHorizontalAlignment(Element.ALIGN_LEFT);
//                table1.addCell(particularsCellAddOn);
//
//                for (Float value : validValues) {
//                    PdfPCell valueCell = new PdfPCell(new Paragraph(String.valueOf(value), normalFont1));
//                    valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table1.addCell(valueCell);
//                }
//                
//                PdfPCell totalCell1 = new PdfPCell(new Paragraph("Total", boldFont1));
//                totalCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table1.addCell(totalCell1);
//                for (yearValue = 1; yearValue <= validSemesters.size(); yearValue++) {
//                	int total = studentBonafideAddOnDetails.stream()
//                	        .mapToInt(record1 -> (int) Double.parseDouble(record1.getOrDefault("sem" + yearValue, "0").toString()))
//                	        .sum();
//                    PdfPCell totalAmountCell = new PdfPCell(new Paragraph(String.valueOf(total), boldFont1));
//                    totalAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                    table1.addCell(totalAmountCell);
//                }
//                
//                Paragraph amountInRupees1 = new Paragraph("(Amount in Rupees)", smallFont);
//                amountInRupees1.setAlignment(Element.ALIGN_RIGHT);
//                amountInRupees1.setSpacingAfter(2);
//        	    document.add(amountInRupees1);
//
//                // Add table to document
//                document.add(table1);
//                
//            }
//
//            Paragraph footer = new Paragraph(
//                    "*please note that the given fee is applicable only for the prescribed Academic Batch.This Bonafide is issued only for the purpose of Bank loan", normalFont1);
//                footer.setAlignment(Element.ALIGN_LEFT);
//                footer.setAlignment(Element.ALIGN_JUSTIFIED);
//                footer.setSpacingBefore(20);
//                footer.setSpacingAfter(20);
//                document.add(footer);
//                
//                Paragraph paymentDetails = new Paragraph("Payment Instructions:", boldFont1);
//                paymentDetails.setSpacingAfter(10);
//                document.add(paymentDetails);
//                com.itextpdf.text.List pdfList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
//                pdfList.setIndentationLeft(20);
//
//                // Add list items
//                pdfList.add(new ListItem("Student can pay all fees through Acharya ERP APP.",normalFont1));
//                pdfList.add(new ListItem("If student opts for Bank loan, DD can be drawn in favor of " + studentBonafideDetails.get(0).get("school_name").toString() + " payable at Bangalore for college fee OR",normalFont1));
//                pdfList.add(new ListItem("If bank prefers to make RTGS Transfer, bank can contact Institution via e-mail - " + studentBonafideDetails.get(0).get("principalEmail").toString() + " for bank details.",normalFont1));
//                pdfList.add(new ListItem("DD can be drawn in favour of 'Nini Skillup Pvt Ltd' for Add-on Programme Fee.",normalFont1));
//                pdfList.add(new ListItem("Uniform & Stationery to be paid through ERP APP only.",normalFont1));
//
//                // Add the list to the document
//                document.add(pdfList);
//           
//                Paragraph para6 = new Paragraph("PRINCIPAL", boldFont1);
//                para6.setAlignment(Element.ALIGN_JUSTIFIED);
//                para6.setSpacingBefore(80);
//                
//                Paragraph para7 = new Paragraph("AUTHORIZED SIGNATORY", boldFont1);
//                para7.setAlignment(Element.ALIGN_JUSTIFIED);
//            
//                document.add(para6);
//                document.add(para7);
//
//            // Close the document
//            document.close();
//
//            System.out.println("PDF generated successfully: ");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

	
    
	
}
