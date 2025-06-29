package com.au.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.au.dto.EmailRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Offer;
import com.au.model.SalaryStructureDetails;
import com.au.model.SalaryStructureHead;
import com.au.repository.DepartmentRepository;
import com.au.repository.DesignationRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.EmployeeTypeRepository;
import com.au.repository.JobProfileRepository;
import com.au.repository.JobTypeRepository;
import com.au.repository.OfferRepository;
import com.au.repository.SalaryStructureDetailsRepository;
import com.au.repository.SalaryStructureHeadRepository;
import com.au.repository.SalaryStructureRepository;
import com.au.repository.School_Repository;
import com.au.repository.VoucherHeadNewRepository;
import com.au.response.ResponseHandler;
import com.au.util.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class OfferService {

	@Autowired
	private OfferRepository or_repo;

//	@Autowired
//	private JavaMailSender mailSender;
	
	  @Autowired
	    @Qualifier("mailSenderPrimary")  // ✅ Explicitly use the primary mail sender
	    private JavaMailSender mailSender;

	@Autowired
	private JobProfileRepository jpr_repo;

	@Autowired
	private ResponseHandler response_handler;

	@Autowired
	private Environment env;

	@Autowired
	private School_Repository sc_repo;

	@Autowired
	private DepartmentRepository deptrepo;

	@Autowired
	private SalaryStructureRepository ssr_repo;

	@Autowired
	private SalaryStructureDetailsRepository ssdr_repo;

	@Autowired
	private SalaryStructureHeadRepository sshr_repo;

	@Autowired
	private EmployeeTypeRepository s_repo;

	@Autowired
	private JobTypeRepository j_repo;

	@Autowired
	private DesignationRepository dr_repo;

	@Autowired
	private VoucherHeadNewRepository t_repo;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
//	//for local
//	private final static String LETTERHEADSPATH = "F:\\git\\au\\Acharya_University\\Letter-heads\\";
	
	//for PROD
	private final static String LETTERHEADSPATH = "D:\\git\\au\\Acharya_University\\Letter-heads\\";

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		// String dynamicQuery1 = CONCAT(cw.ac_year_id, ' ',cw.candidate_name) LIKE
		// %:keyword%;
		// String dynamicQuery1 = column +"="+keyword +" and "+column+"="+ value;
		Page<Object> offer_filtered_response = or_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, offer_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> offer_sorted_response = or_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, offer_sorted_response);
	}

	public List<HashMap<String, Object>> listAll1(Integer offer_id) {
		return or_repo.fetchAllDetails(offer_id);
	}

	public Offer saveOffer(Offer offer) {
		String val = or_repo.getofferCode();

		if (val == null) {
			int num1 = 0;
			int add1 = 1;
			num1 = num1 + add1;
			String formattedStrr = String.format("%04d", num1);
			String emp_Type = s_repo.getEmployeeTypeName(offer.getEmp_type_id());
			String job_Type = j_repo.getJobType(offer.getJob_type_id());
			String designation_type = dr_repo.getDesignation(offer.getDesignation_id());
			offer.setEmployee_type(emp_Type);
			offer.setDesignation(designation_type);
			offer.setJob_type(job_Type);
			offer.setOffercode("OL/" + emp_Type + "/" + job_Type + "/" + formattedStrr);
			offer.setIsPf( ObjectUtils.isNotEmpty(offer.getIsPf()) && offer.getIsPf() == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE);
			offer.setIsPt(ObjectUtils.isNotEmpty(offer.getIsPt()) && offer.getIsPt() == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE);
		} else {
			// String result;
			String[] arrOfStrr = val.split("/");
			System.out.println(arrOfStrr);
			String str2 = arrOfStrr[arrOfStrr.length - 1];
			int num1 = Integer.parseInt(str2);
			System.out.println("reference _no else condition " + num1);
			int add1 = 1;
			num1 = num1 + add1;
			String formattedStr1 = String.format("%04d", num1);
			String emp_Type = s_repo.getEmployeeTypeName(offer.getEmp_type_id());
			String job_Type = j_repo.getJobType(offer.getJob_type_id());
			String designation_type = dr_repo.getDesignation(offer.getDesignation_id());
			offer.setDesignation(designation_type);
			offer.setEmployee_type(emp_Type);
			offer.setJob_type(job_Type);
			offer.setOffercode("OL/" + emp_Type + "/" + job_Type + "/" + formattedStr1);
			offer.setIsPf( ObjectUtils.isNotEmpty(offer.getIsPf()) && offer.getIsPf() == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE);
			offer.setIsPt(ObjectUtils.isNotEmpty(offer.getIsPt()) && offer.getIsPt() == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE);
		}
		return or_repo.save(offer);

	}

	public Offer get(Integer id) {
		return or_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Offer Not Found:" + id));
	}

	public void delete(Integer id) {
		Offer ay = or_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Offer Not Found:" + id));
		or_repo.delete(ay);
	}

	public void sendMail(EmailRequest emails, String content, String subject) {
		String[] strarray = emails.getEmails().toArray(new String[0]);
		for (int i = 0; i < strarray.length; i++) {
			System.out.println(strarray[i]);
		}
		System.out.println(strarray);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"));
		message.setTo(strarray);
		message.setText(content);
		message.setSubject(subject);
		mailSender.send(message);
		System.out.println("Mail Send...");

	}

	public Object sendMailForOffer1(String url_domain, Integer job_id, Integer offer_id) throws Exception {

		Offer offer = or_repo.getOffer1(offer_id);
		String firstName = getname(job_id);
		String email = getEmail(job_id);
		// InternetAddress iaRecipient = new InternetAddress(email);
		String designation = getDesignation(offer_id);
		Character gender = jpr_repo.getGender(job_id);
		
		String genderSaluation = null;
		
		if (gender.equals('M')) 
			genderSaluation = "Mr.";
		else
			genderSaluation = "Ms.";
		
		String repotingdate = offer.getDate_of_joining();
//		LocalDate date1 = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		LocalDate repotDate = date1.plusDays(1);//while fetching date from DB, minus 1 day we are getting 
//		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		String reportingDatestring = date1.format(pattern);
		
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		String reportOn = formatter.format(date);
//		System.out.println("((((((((((((((((((((((((((())))))))))))))))))))))))))) " + reportingDatestring);

		ByteArrayOutputStream outputStream = null;

		// now write the PDF content to the output stream
		outputStream = new ByteArrayOutputStream();

		if (offer.getEmployee_type().equalsIgnoreCase("CON")) {
			
			writePdfForOfferLetter(outputStream, job_id, offer_id);

			byte[] bytes = outputStream.toByteArray();
			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName("Offer_" + job_id + ".pdf");

			String contents = "Dear " + genderSaluation + " " + firstName + "," + "<br/>" + "<br/>"
					+ "Congratulations on your offer from Acharya Institutes ! We are delighted to offer you a position with us.!!<br/>"
					+ "Please find attached your detailed offer letter. Kindly acknowledge and confirm your acceptance within 7 days from offer date.<br/>"
					+ "Looking forward for your reversion.<br/>" + "<br/>"
					+ "Click on the below link to confirm your acceptance for reporting on " + repotingdate + ".<br/>"
					+ "<br/>" + "<a href= " + url_domain + "/" + offer_id
					+ " style='background-color: #4A57A9;color:white;text-decoration:none;padding:6px'>Accept</a>"
					+ "<br/>" + "<br/>" + "<br/>" + "--<br/>" + "Divya<br/>" + "Manager - ERP<br/>"
					+ "Acharya Institutes<br/>" + "Bangalore" + "<br/> " + "<br/> " + "<br/> " 
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";

			String subject = "Offer letter -" + designation;

			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(contents);

			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);

			MimeMessage message = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
				helper.setTo(email);
				helper.setSubject(subject);
				helper.setText(contents, true);
				File file = new File("C:\\Offer and Salary Breakup\\Offer_" + job_id + ".pdf");
				helper.addAttachment("Offer_" + job_id + ".pdf", file);
			} catch (MessagingException e) {
				throw new MailParseException(e);
			}

			mailSender.send(message);
			System.out.println("PDF GENERATED...");
			System.out.println("Mail Send...");
			response_handler.delete(0, ".pdf");
			or_repo.updateOfferStatusToPending(offer_id);
			return null;

		} else {
			writePdfForSalaryBreakup(outputStream, job_id, offer_id);
			writePdfForOfferLetter(outputStream, job_id, offer_id);

			byte[] bytes = outputStream.toByteArray();
			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName("Offer_" + job_id + ".pdf");

			String contents = "Dear " + genderSaluation + " " + firstName + "," + "<br/>" + "<br/>"
					+ "Congratulations on your offer from Acharya Institutes ! We are delighted to offer you a position with us.!!<br/>"
					+ "Please find attached your detailed offer letter. Kindly acknowledge and confirm your acceptance within 7 days from offer date.<br/>"
					+ "Looking forward for your reversion.<br/>" + "<br/>"
					+ "Click on the below link to confirm your acceptance for reporting on " + repotingdate + ".<br/>"
					+ "<br/>" + "<a href= " + url_domain + "/" + offer_id
					+ " style='background-color: #4A57A9;color:white;text-decoration:none;padding:6px'>Accept</a>"
					+ "<br/>" + "<br/>" + "<br/>" + "--<br/>" + "Divya<br/>" + "Manager - ERP<br/>"
					+ "Acharya Institutes<br/>" + "Bangalore"+ "<br/> " + "<br/> " + "<br/> " 
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";

			String subject = "Offer letter -" + designation;

			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(contents);

			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);

			MimeMessage message = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
				helper.setTo(email);
				helper.setSubject(subject);
				helper.setText(contents, true);
				File file = new File("C:\\Offer and Salary Breakup\\Offer_" + job_id + ".pdf");
				File file1 = new File("C:\\Offer and Salary Breakup\\Salary_Breakup_" + job_id + ".pdf");
				helper.addAttachment("Offer_" + job_id + ".pdf", file);
				helper.addAttachment("Salary_Breakup_" + job_id + ".pdf", file1);
			} catch (MessagingException e) {
				throw new MailParseException(e);
			}

			mailSender.send(message);

			System.out.println("PDF GENERATED...");
			System.out.println("Mail Sent...");

			response_handler.delete(0, ".pdf");
			or_repo.updateOfferStatusToPending(offer_id);
			return null;

		}
	}

	Integer earn_monthly_total = 0;
	Integer earn_yearly_total = 0;

	Integer ded_monthly_total = 0;
	Integer ded_yearly_total = 0;

	Integer man_monthly_total = 0;
	Integer man_yearly_total = 0;

	
//	Font boldFont = new Font(FontFactory.getFont("Calibri", 10, Font.BOLD));
//	Font boldFont1 = new Font(FontFactory.getFont(BaseFont.TIMES_ROMAN, 10, Font.BOLD));
//	Font boldFont2 = new Font(FontFactory.getFont(BaseFont.TIMES_ROMAN, 11));
//	Font normalFont1 = new Font(FontFactory.getFont("Calibri", 10));
//	Font normalFont2 = new Font(FontFactory.getFont("Calibri", 10, BaseColor.DARK_GRAY));
	
	Font boldFont = new Font(FontFactory.getFont("Calibri", 10, Font.BOLD));
	Font boldFont1 = new Font(FontFactory.getFont("Roboto", 10, Font.BOLD));
	Font boldFont2 = new Font(FontFactory.getFont("Roboto", 10));
	Font normalFont1 = new Font(FontFactory.getFont("Calibri", 10));
	Font normalFont2 = new Font(FontFactory.getFont("Calibri", 10, BaseColor.DARK_GRAY));
	
	public void writePdfForSalaryBreakup(OutputStream outputStream, Integer job_id, Integer offer_id) throws Exception {
		List<HashMap<String, Object>> offer = or_repo.getOffer(offer_id);
		Offer offer1 = or_repo.getOffer1(offer_id);
		String firstName = getname(job_id);
		Character gender = jpr_repo.getGender(job_id);
		String schoolName = sc_repo.getSchoolName((Integer) offer.get(0).get("school_id"));
		String department = deptrepo.getDepartment((Integer) offer.get(0).get("dept_id"));
		String payScale = (String) offer.get(0).get("salary_structure");
		String designation = offer1.getDesignation();
		String img_path1 = null;
		File file = new File("C:\\Offer and Salary Breakup\\Salary_Breakup_" + job_id + ".pdf");

		Document document = new Document(PageSize.A4, 0, 0, 50, 0);

		PdfWriter.getInstance(document, new FileOutputStream(file));

		document.open();

		// Step-3 Creating a table
		PdfPTable table1 = new PdfPTable(1);
		PdfPTable table2 = new PdfPTable(2);
		PdfPTable table3 = new PdfPTable(3);
		table3.setWidths(new int[] { 3, 1, 1 });
		PdfPTable table4 = new PdfPTable(3);
		table4.setWidths(new int[] { 3, 1, 1 });
		PdfPTable table6 = new PdfPTable(3);
		table6.setWidths(new int[] { 3, 1, 1 });
		PdfPTable table7 = new PdfPTable(3);
		table7.setWidths(new int[] { 3, 1, 1 });
		PdfPTable table8 = new PdfPTable(3);
		table8.setWidths(new int[] { 3, 1, 1 });
		PdfPTable table10 = new PdfPTable(3);
		table10.setWidths(new int[] { 3, 1, 1 });
		PdfPTable table11 = new PdfPTable(3);
		table11.setWidths(new int[] { 3, 1, 1 });
		PdfPTable table12 = new PdfPTable(3);
		table12.setWidths(new int[] { 3, 1, 1 });
		PdfPTable table13 = new PdfPTable(1);
		PdfPTable table14 = new PdfPTable(1);
		PdfPTable table15 = new PdfPTable(1);
		PdfPTable table16 = new PdfPTable(1);
		PdfPTable table17 = new PdfPTable(1);
		PdfPTable table18 = new PdfPTable(1);
		
		PdfPTable table19 = new PdfPTable(1);

		// Creating fonts


		// Step-4 Adding cells to the table
		
		PdfPCell extraCell = new PdfPCell(new Phrase("ANNEXURE 1", boldFont1));
		extraCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		extraCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		extraCell.setExtraParagraphSpace(2);
		table19.addCell(extraCell);
		
		PdfPCell cell01 = new PdfPCell(new Phrase(schoolName, boldFont1));
		cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell01.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell01.setExtraParagraphSpace(2);
		table1.addCell(cell01);

		if (gender.equals('M')) {
			PdfPCell cell171 = new PdfPCell(new Phrase("Salary Breakup for Mr." + firstName, boldFont1));
			cell171.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell171.setExtraParagraphSpace(2);
			table17.addCell(cell171);
		} else {
			PdfPCell cell171 = new PdfPCell(new Phrase("Salary Breakup for Ms." + firstName, boldFont1));
			cell171.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell171.setExtraParagraphSpace(2);
			table17.addCell(cell171);
		}

		PdfPCell cell21 = new PdfPCell(new Phrase("Department : " + department, normalFont1));
		cell21.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell21.setExtraParagraphSpace(2);
		table2.addCell(cell21);
		PdfPCell cell22 = new PdfPCell(new Phrase("Designation : " + designation, normalFont1));
		cell22.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell22.setExtraParagraphSpace(2);
		table2.addCell(cell22);

		PdfPCell cell181 = new PdfPCell(new Phrase("Pay Scale : " + payScale, normalFont1));
		cell181.setExtraParagraphSpace(2);
		table18.addCell(cell181);

		PdfPCell cell1 = new PdfPCell(new Phrase("Earnings", boldFont1));
		cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell1.setExtraParagraphSpace(2);
		table3.addCell(cell1);
		PdfPCell cell2 = new PdfPCell(new Phrase("Monthly(Rs.)", boldFont1));
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setExtraParagraphSpace(2);
		table3.addCell(cell2);
		PdfPCell cell3 = new PdfPCell(new Phrase("Yearly(Rs.)", boldFont1));
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setExtraParagraphSpace(2);
		table3.addCell(cell3);
		table3.setHeaderRows(1);

		List<SalaryStructureDetails> ssd = ssdr_repo
				.getSalaryStructureDetails((Integer) offer.get(0).get("salary_structure_id"));
		ssd.stream().forEach(r -> {

			SalaryStructureHead ssh = sshr_repo.getSalaryStructureHead(r.getSalary_structure_head_id());

			if (ssh.getCategory_name_type().equals("Earning")) {
				if (offer.get(0).get(ssh.getPrint_name()) != null) {

					String voucher_name = t_repo.getVoucherName(ssh.getVoucher_head_new_id());
					System.out.println("************************************* " + voucher_name);
					PdfPCell v_name = new PdfPCell(new Phrase(voucher_name, normalFont1));
					v_name.setExtraParagraphSpace(2);
					table3.addCell(v_name);

					Integer monthly = Math.round((Float) offer.get(0).get(ssh.getPrint_name()));
					System.out.println("=============================== " + ssh.getPrint_name());
					System.out.println("=============================== " + monthly);
					PdfPCell cell_earn_monthly = new PdfPCell(new Phrase(monthly.toString(), normalFont1));
					cell_earn_monthly.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell_earn_monthly.setExtraParagraphSpace(2);
					table3.addCell(cell_earn_monthly);

					earn_monthly_total = earn_monthly_total + monthly;
					System.out.println("[[[[[[[[[[[[[[[[mothly]]]]]]]]]]]]]]]] " + earn_monthly_total);

					Integer yearly = Math.round((Float) offer.get(0).get(ssh.getPrint_name()) * 12);
					PdfPCell cell_earn_yearly = new PdfPCell(new Phrase(yearly.toString(), normalFont1));
					cell_earn_yearly.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell_earn_yearly.setExtraParagraphSpace(2);
					table3.addCell(cell_earn_yearly);

					earn_yearly_total = earn_yearly_total + yearly;
					System.out.println("[[[[[[[[[[[[[[yearly[[]]]]]]]]]]]]]]]] " + earn_monthly_total);
				}

			}
		});
		String earning_monthly_total = Integer.toString(earn_monthly_total);
		String earning_yearly_total = Integer.toString(earn_yearly_total);

		PdfPCell cell41 = new PdfPCell(new Phrase("Gross Salary ( A ) ", boldFont1));
		cell41.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell41.setExtraParagraphSpace(2);

		table4.addCell(cell41);
		PdfPCell cell42 = new PdfPCell(new Phrase(earning_monthly_total, boldFont1));
		cell42.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell42.setExtraParagraphSpace(2);
		table4.addCell(cell42);
		PdfPCell cell43 = new PdfPCell(new Phrase(earning_yearly_total, boldFont1));
		cell43.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell43.setExtraParagraphSpace(2);
		table4.addCell(cell43);
		table4.setHeaderRows(1);

		table4.addCell("");
		table4.addCell("");
		table4.addCell("");

		PdfPCell cell61 = new PdfPCell(new Phrase("Deductions - Employee Contribution", boldFont1));
		cell61.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell61.setExtraParagraphSpace(2);
		table6.addCell(cell61);
		PdfPCell cell62 = new PdfPCell(new Phrase("     "));
		cell62.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell62.setExtraParagraphSpace(2);
		table6.addCell(cell62);
		PdfPCell cell63 = new PdfPCell(new Phrase("     "));
		cell63.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell63.setExtraParagraphSpace(2);
		table6.addCell(cell63);

		table6.setHeaderRows(1);

		List<SalaryStructureDetails> ssd1 = ssdr_repo
				.getSalaryStructureDetails((Integer) offer.get(0).get("salary_structure_id"));
		ssd1.stream().forEach(r -> {

			SalaryStructureHead ssh = sshr_repo.getSalaryStructureHead(r.getSalary_structure_head_id());

			if (ssh.getCategory_name_type().equals("Deduction")) {
				if (offer.get(0).get(ssh.getPrint_name()) != null) {

					String voucherName = t_repo.getVoucherName(ssh.getVoucher_head_new_id());
					Integer monthly = Math.round((Float) offer.get(0).get(ssh.getPrint_name()));

					if (voucherName.equalsIgnoreCase("epf")) {
						Integer percentage = Math.round(ssdr_repo.getPercentage(ssh.getSalary_structure_head_id()));
						String value = " - " + percentage + "% of Basic or Rs " + monthly + "/- whichever is less";
						Phrase phrase = new Phrase();
						phrase.add(new Chunk(voucherName, normalFont1));
						phrase.add(new Chunk(value, normalFont2));
						// phrase.setExtraParagraphSpace(2);
						table6.addCell(phrase);

					} else if (voucherName.equalsIgnoreCase("esi")) {
						Integer percentage = Math.round(ssdr_repo.getPercentage(ssh.getSalary_structure_head_id()));
						String value = " - " + percentage + "% of Gross Salary";
						Phrase phrase = new Phrase();
						phrase.add(new Chunk(voucherName, normalFont1));
						phrase.add(new Chunk(value, normalFont2));
						table6.addCell(phrase);

					} else if (voucherName.equalsIgnoreCase("pt")) {
						String value = " - Present State slab";
						Phrase phrase = new Phrase();
						phrase.add(new Chunk(voucherName, normalFont1));
						phrase.add(new Chunk(value, normalFont2));
						table6.addCell(phrase);

					} else {
						// table6.addCell(t_repo.getVoucherName(ssh.getVoucher_head_new_id()));
						String voucher_name = t_repo.getVoucherName(ssh.getVoucher_head_new_id());
						PdfPCell v_name = new PdfPCell(new Phrase(voucher_name, normalFont1));
						v_name.setExtraParagraphSpace(2);
						table6.addCell(v_name);
					}
					// Integer monthly = Math.round((Float)offer.get(0).get(ssh.getPrint_name()));
					PdfPCell cell_ded_monthly = new PdfPCell(new Phrase(monthly.toString(), normalFont1));
					cell_ded_monthly.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell_ded_monthly.setExtraParagraphSpace(2);
					table6.addCell(cell_ded_monthly);

					ded_monthly_total = ded_monthly_total + monthly;

					Integer yearly = Math.round((Float) offer.get(0).get(ssh.getPrint_name()) * 12);
					PdfPCell cell_ded_yearly = new PdfPCell(new Phrase(yearly.toString(), normalFont1));
					cell_ded_yearly.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell_ded_yearly.setExtraParagraphSpace(2);
					table6.addCell(cell_ded_yearly);

					ded_yearly_total = ded_yearly_total + yearly;

				}
			}
		});

		String deduction_monthly_total = Integer.toString(ded_monthly_total);
		String deduction_yearly_total = Integer.toString(ded_yearly_total);

		PdfPCell cell71 = new PdfPCell(new Phrase("Total Deductions ( B )", boldFont1));
		cell71.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell71.setExtraParagraphSpace(2);
		table7.addCell(cell71);
		PdfPCell cell72 = new PdfPCell(new Phrase(deduction_monthly_total, boldFont1));
		cell72.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell72.setExtraParagraphSpace(2);
		table7.addCell(cell72);
		PdfPCell cell73 = new PdfPCell(new Phrase(deduction_yearly_total, boldFont1));
		cell73.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell73.setExtraParagraphSpace(2);
		table7.addCell(cell73);
		table7.setHeaderRows(1);

		table7.addCell("");
		table7.addCell("");
		table7.addCell("");

		Integer net_salary_monthly = earn_monthly_total - ded_monthly_total;
		String net_salary_monthly1 = Integer.toString(net_salary_monthly);

		Integer net_salary_yearly = earn_yearly_total - ded_yearly_total;
		String net_salary_yearly1 = Integer.toString(net_salary_yearly);

		PdfPCell cell81 = new PdfPCell(new Phrase("Net Salary ( C ) = ( A - B ) ", boldFont1));
		cell81.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell81.setExtraParagraphSpace(2);
		table8.addCell(cell81);
		PdfPCell cell82 = new PdfPCell(new Phrase(net_salary_monthly1, boldFont1));
		cell82.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell82.setExtraParagraphSpace(2);
		table8.addCell(cell82);
		PdfPCell cell83 = new PdfPCell(new Phrase(net_salary_yearly1, boldFont1));
		cell83.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell83.setExtraParagraphSpace(2);
		table8.addCell(cell83);
		table8.setHeaderRows(1);

		table8.addCell("");
		table8.addCell("");
		table8.addCell("");

		PdfPCell cell101 = new PdfPCell(new Phrase("Employer Contribution", boldFont1));
		cell101.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell101.setExtraParagraphSpace(2);
		table10.addCell(cell101);

		PdfPCell cell102 = new PdfPCell(new Phrase("     "));
		cell102.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell102.setExtraParagraphSpace(2);
		table10.addCell(cell102);
		PdfPCell cell103 = new PdfPCell(new Phrase("     "));
		cell103.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell103.setExtraParagraphSpace(2);
		table10.addCell(cell103);
		table10.setHeaderRows(1);

//		table10.addCell("");
//		table10.addCell("");
//		table10.addCell("");

		List<SalaryStructureDetails> ssd2 = ssdr_repo
				.getSalaryStructureDetails((Integer) offer.get(0).get("salary_structure_id"));
		ssd2.stream().forEach(r -> {

			SalaryStructureHead ssh = sshr_repo.getSalaryStructureHead(r.getSalary_structure_head_id());
			if (ssh.getCategory_name_type().equals("Management")) {
				if (offer.get(0).get(ssh.getPrint_name()) != null) {

					String voucherName = t_repo.getVoucherName(ssh.getVoucher_head_new_id());
					PdfPCell cell1011 = new PdfPCell(new Phrase(voucherName, normalFont1));
					cell1011.setExtraParagraphSpace(2);
					table10.addCell(cell1011);

					Integer monthly = Math.round((Float) offer.get(0).get(ssh.getPrint_name()));
					PdfPCell cell_ic_monthly = new PdfPCell(new Phrase(monthly.toString(), normalFont1));
					cell_ic_monthly.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell_ic_monthly.setExtraParagraphSpace(2);
					table10.addCell(cell_ic_monthly);

					man_monthly_total = man_monthly_total + monthly;

					Integer yearly = Math.round((Float) offer.get(0).get(ssh.getPrint_name()) * 12);
					PdfPCell cell_ic_yearly = new PdfPCell(new Phrase(yearly.toString(), normalFont1));
					cell_ic_yearly.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell_ic_yearly.setExtraParagraphSpace(2);
					table10.addCell(cell_ic_yearly);

					man_yearly_total = man_yearly_total + yearly;

				}
			}
		});

		String management_monthly_total = Integer.toString(man_monthly_total);
		String management_yearly_total = Integer.toString(man_yearly_total);

		PdfPCell cell111 = new PdfPCell(new Phrase("Institutional Contribution ( D )", boldFont1));
		cell111.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell111.setExtraParagraphSpace(2);
		table11.addCell(cell111);
		PdfPCell cell112 = new PdfPCell(new Phrase(management_monthly_total, boldFont1));
		cell112.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell112.setExtraParagraphSpace(2);
		table11.addCell(cell112);
		PdfPCell cell113 = new PdfPCell(new Phrase(management_yearly_total, boldFont1));
		cell113.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell113.setExtraParagraphSpace(2);
		table11.addCell(cell113);
		table11.setHeaderRows(1);

		table11.addCell("");
		table11.addCell("");
		table11.addCell("");

		Integer cost_to_institution_monthly = earn_monthly_total + man_monthly_total;
		String cost_to_institution_monthly1 = Integer.toString(cost_to_institution_monthly);

		Integer cost_to_institution_yearly = earn_yearly_total + man_yearly_total;
		String cost_to_institution_yearly1 = Integer.toString(cost_to_institution_yearly);

		PdfPCell cell121 = new PdfPCell(new Phrase("Cost to Institution ( E ) = ( A + D )", boldFont1));
		cell121.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell121.setExtraParagraphSpace(2);
		table12.addCell(cell121);
		PdfPCell cell122 = new PdfPCell(new Phrase(cost_to_institution_monthly1, boldFont1));
		cell122.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell122.setExtraParagraphSpace(2);
		table12.addCell(cell122);
		PdfPCell cell123 = new PdfPCell(new Phrase(cost_to_institution_yearly1, boldFont1));
		cell123.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell123.setExtraParagraphSpace(2);
		table12.addCell(cell123);
		table12.setHeaderRows(1);

		table12.addCell("");
		table12.addCell("");
		table12.addCell("");

		table13.addCell(new PdfPCell(new Phrase("Acceptance Acknowledgment from New Recruit \r\n" + "\r\n" + "\r\n"
				+ "Signature:   \r\n" + "\r\n" + "\r\n" + "Date:   \r\n" + "\r\n" + "\r\n", normalFont1)));
		table14.addCell(new PdfPCell(new Phrase("\r\n"
				+ "This document is only for Acharya Institutes HR Team reference. Any person or entity apart from Acharya Institutes HR Team is prohibited from having this document\r\n"
				+ "\r\n", normalFont2)));
		table15.addCell(new PdfPCell(new Phrase(
				"Remarks specific to New Recruit(If Any):   \r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n",
				normalFont1)));
		table16.addCell(new PdfPCell(new Phrase("Authorised Signatory:   \r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "\r\n" + "\r\n"
				+ "        New Recruit                      Executive-HR                        Head-HR                       Managing Director" + "\r\n" + "\r\n",
				normalFont1)));

//		        document.add(imgdata);
//		document.add(imgdata1);
//		document.add(imgdata2);\
		document.add(table19);
		document.add(table1);
		document.add(table17);
		document.add(table2);
		document.add(table18);
		document.add(table3);
		document.add(table4);
		// document.add(table5);
		document.add(table6);
		document.add(table7);
		document.add(table8);
		// document.add(table9);
		document.add(table10);
		document.add(table11);
		document.add(table12);
		document.add(table13);
		document.add(table14);
		document.add(table15);
		document.add(table16);

		document.close();

		earn_monthly_total = 0;
		earn_yearly_total = 0;

		ded_monthly_total = 0;
		ded_yearly_total = 0;

		man_monthly_total = 0;
		man_yearly_total = 0;
	}

	public void writePdfForOfferLetter(OutputStream outputStream, Integer job_id, Integer offer_id) throws Exception {
		List<HashMap<String, Object>> offer = or_repo.getOffer(offer_id);
		String email = getEmail(job_id);
		String mobile = jpr_repo.getMobile(job_id);
		Offer offer1 = or_repo.getOffer1(offer_id);
		String firstName = getname(job_id);
		String schoolName = sc_repo.getSchoolName((Integer) offer.get(0).get("school_id")).toUpperCase();
		String department = deptrepo.getDepartment((Integer) offer.get(0).get("dept_id")).toUpperCase();
		String offercode = offer1.getOffercode();
		String designation = offer1.getDesignation().toUpperCase();
		Character gender = jpr_repo.getGender(job_id);
		String address = jpr_repo.getAddress(job_id);
		String employeeType = offer1.getEmployee_type();
		String img_path1 = null;
		String genderSaluation = null;
		
		if (gender.equals('M')) 
			genderSaluation = "Mr.";
		else
			genderSaluation = "Ms.";
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String offer_date = formatter.format(date);

		File file = new File("C:\\Offer and Salary Breakup\\offer_" + job_id + ".pdf");

		Document document = new Document(PageSize.A4, 30, 30, 120, 0);

		if(schoolName.equalsIgnoreCase("ACHARYA INSTITUTE OF TECHNOLOGY")) {
			 img_path1 = LETTERHEADSPATH+"ait.jpg";
		}else if(schoolName.equalsIgnoreCase("Acharya Institute of Graduate Studies")){
			 img_path1 = LETTERHEADSPATH+"aigs.jpg";
		}else if(schoolName.equalsIgnoreCase("ACHARYAS NR INSTITUTE OF PHYSIOTHERAPY")) {
			 img_path1 = LETTERHEADSPATH+"physio.jpg";
		}else if(schoolName.equalsIgnoreCase("ACHARYA POLYTECHNIC")) {
			 img_path1 = LETTERHEADSPATH+"polytechnic.jpg";
		}else if(schoolName.equalsIgnoreCase("ACHARYA AND BM REDDY COLLEGE OF PHARMACY")) {
			 img_path1 = LETTERHEADSPATH+"pharmacy.jpg";
		}else if(schoolName.equalsIgnoreCase("ACHARYAS NRV SCHOOL OF ARCHITECTURE")) {
			 img_path1 = LETTERHEADSPATH+"architecture.jpg";
		}else if(schoolName.equalsIgnoreCase("SMT  NAGARATHNAMMA COLLEGE OF NURSING")) {
			 img_path1 = LETTERHEADSPATH+"nursing.jpg";
		}else if(schoolName.equalsIgnoreCase("ACHARYA SCHOOL OF DESIGN")) {
			 img_path1 = LETTERHEADSPATH+"sclofdesign.jpg";
		}else if(schoolName.equalsIgnoreCase("ACHARYA INSTITUTE OF ALLIED HEALTH SCIENCES")) {
			 img_path1 = LETTERHEADSPATH+"alliedhealth.jpg";
		}else if(schoolName.equalsIgnoreCase("SMT NAGARATHNAMMA SCHOOL OF NURSING")) {
			 img_path1 = LETTERHEADSPATH+"sclofnursing.jpg";
		}
		
//		String img_path1 = "C:\\Users\\admin\\git\\au\\Acharya_University\\Letter-heads\\ait.jpg"; // for aws server

		Image imgdata1 = Image.getInstance(img_path1);
		imgdata1.scaleToFit(900, 800);
		imgdata1.setAbsolutePosition(15, 45);

//		String img_path2 = "C:\\Users\\admin\\git\\au\\Acharya_University\\Letter-heads\\AIT_footer.png"; // for aws server  G:\\SpringBoot Project\\au\\Acharya_University\\AIT_footer.png
//		        String img_path2 = "C:\\Users\\admin\\git\\au\\Acharya_University\\AIT_footer.png"; //for local
//		Image imgdata2 = Image.getInstance(img_path2);
//		imgdata2.scaleToFit(600, 500);
//		imgdata2.setAbsolutePosition(0, 0);

		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

		Paragraph offerCodeAndDateParagraph = new Paragraph();
		Paragraph addressParagraph = new Paragraph();
		Paragraph paragraph = new Paragraph();
		Paragraph paragraph1 = new Paragraph();
		Paragraph paragraph2 = new Paragraph();
		Paragraph paragraph3 = new Paragraph();
		Paragraph paragraph4 = new Paragraph();
		Paragraph paragraph5 = new Paragraph();
		Paragraph paragraph6 = new Paragraph();
		Paragraph paragraph7 = new Paragraph();
		Paragraph paragraph8 = new Paragraph();
		Paragraph paragraph9 = new Paragraph();
		Paragraph paragraph10 = new Paragraph();
		Paragraph paragraph11 = new Paragraph();
		Paragraph paragraph12 = new Paragraph();
			
			if(!employeeType.equalsIgnoreCase("FTE")) {
				Phrase phrase = new Phrase();
				phrase.add("OFFER LETTER\r\n" + "\r\n");
				Phrase phrase1 = new Phrase();
				phrase1.add("Office of HR Department\r\n" + "\r\n");
				Phrase phrase2 = new Phrase(offercode
						+ "                                                                                                                               "
						+ offer_date + "\r\n" + "\r\n" + "To,\r\n" + genderSaluation + firstName + "\r\n" + offer.get(0).get("street") + ",\r\n" 
						+ offer.get(0).get("locality") + ",\r\n" + offer.get(0).get("cityName") + " - " + offer.get(0).get("pincode") + "\r\n" + mobile + "\r\n"
						+ "\r\n" + "Subject : Offer for the post of " + designation + "\r\n" + "\r\n" + "Dear Mr. "
						+ firstName + ",\r\n" + "\r\n"
						+ "With reference to your application and subsequent interview, we are pleased to offer you the post of "
						+ designation + " in the Department of " + department + " at " + schoolName
						+ ". You will be on Probation for a period of one year or six months from the date of reporting to duty and your performance will be reviewed at the end of every 6 months.\r\n"
						+ "\r\n"	
						+ "You are requested to contact HR Office first, on the day of reporting at the College. We look forward to a fruitful and long term association together.\r\n"
						+ "\r\n" + "\r\n", boldFont2);
//				Phrase phrase3 = new Phrase("Yours Sincerely,\r\n" + "\r\n", boldFont2);
				Phrase phrase4 = new Phrase("Managing Director\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n", boldFont2);
				Phrase phrase5 = new Phrase("*Encl: The details of the CTC/Salary break up in the Annexure 1 is appended here too.",boldFont2);
	
				paragraph.add(phrase);
				paragraph.setAlignment(Element.ALIGN_CENTER);
				
				paragraph1.add(phrase1);
				paragraph1.setAlignment(Element.ALIGN_CENTER);
				
				paragraph2.add(phrase2);
				paragraph2.setAlignment(Element.ALIGN_JUSTIFIED);
	
//				paragraph3.add(phrase3);
//				paragraph3.setAlignment(Element.ALIGN_LEFT);
				
				paragraph4.add(phrase4);
				paragraph4.setAlignment(Element.ALIGN_LEFT);
				
				paragraph5.add(phrase5);
				paragraph5.setAlignment(Element.ALIGN_LEFT);
				
				document.add(imgdata1);
//				document.add(imgdata2);
				document.add(paragraph);
				document.add(paragraph1);
				document.add(paragraph2);
//				document.add(paragraph3);
				document.add(paragraph4);
				document.add(paragraph5);
			
			}else {
				
				Phrase offerCodeAndDatePhrase = new Phrase();
				offerCodeAndDatePhrase.add(new Chunk("Ref : " , boldFont));
				offerCodeAndDatePhrase.add(new Chunk(offercode + "                                                                                                                           ", boldFont2));
				offerCodeAndDatePhrase.add(new Chunk("Date : ", boldFont));
				offerCodeAndDatePhrase.add(new Chunk(offer_date + "\r\n" + "\r\n", boldFont2));
				
				Phrase addressPhrase = new Phrase(genderSaluation + firstName + ",\r\n" + offer.get(0).get("street") + ",\r\n" 
						+ offer.get(0).get("locality") + ",\r\n" + offer.get(0).get("cityName") + " - " + offer.get(0).get("pincode") + "\r\n" + mobile + "\r\n" + "\r\n", boldFont2);
				
				Phrase phrase = new Phrase();
				phrase.add(new Chunk("FIXED TERM EMPLOYMENT OFFER\r\n" + "\r\n", boldFont)); //.setUnderline(0, -1)
				
				Phrase phrase1 = new Phrase();
				phrase1.add(new Chunk("Dear ",boldFont2));
				phrase1.add(new Chunk(genderSaluation + firstName + ",\r\n" + "\r\n", boldFont));
				phrase1.add(new Chunk("With reference to the application and subsequent discussion you had with us, we are pleased to offer you employment as Junior Executive at ",boldFont2));
				phrase1.add(new Chunk(schoolName ,boldFont));
				phrase1.add(new Chunk(" on a ",boldFont2));
				phrase1.add(new Chunk("FIXED TERM ",boldFont));
				phrase1.add(new Chunk("from ",boldFont2));
				phrase1.add(new Chunk(offer1.getFrom_date() ,boldFont));
				phrase1.add(new Chunk(" to ",boldFont2));
				phrase1.add(new Chunk(offer1.getTo_date() ,boldFont));
				phrase1.add(new Chunk(". FTE contract commences from Date of reporting. \r\n" + "\r\n",boldFont2));
				
				Phrase phrase2 = new Phrase();
				phrase2.add(new Chunk("Job Description : ",boldFont));
				phrase2.add(new Chunk(offer1.getRemarks() + "\r\n" + "\r\n",boldFont2));

				Phrase phrase3 = new Phrase();
				phrase3.add(new Chunk("Place of posting :  ", boldFont));
				phrase3.add(new Chunk("You will be posted at ", boldFont2));
				phrase3.add(new Chunk(schoolName, boldFont));
				phrase3.add(new Chunk(".However, during the employment with us you may be transferred or deputed to any of our Offices "
						+ "/ branches / Departments without any additional remuneration. \r\n" + "\r\n", boldFont2));
				
				Phrase phrase4 = new Phrase();
				phrase4.add(new Chunk("Remuneration : ",boldFont));
				phrase4.add(new Chunk("You will be paid an all-inclusive amount of ",boldFont2));
				phrase4.add(new Chunk(offer1.getCtc().toString().replace(".0", ""),boldFont));
                phrase4.add(new Chunk("/- pm during the term of employment with us. The said remuneration is subject to applicable statutory deductions.\r\n" + "\r\n",boldFont2));
				
				Phrase phrase5 = new Phrase("Your FTE offer is subject to fulfillment of following terms & conditions:\r\n" + "\r\n", boldFont);
				
				Phrase phrase6 = new Phrase();
				phrase6.add(new Chunk("1. ",boldFont));
				phrase6.add(new Chunk("You are required to enter into a Fixed Term Employment Contract/Agreement (FTE) at the time of "
						+ "joining/reporting to work. Your employment (FTE) is governed by the HR Policies in force and FTE Agreement. \r\n",boldFont2));
				
				Phrase phrase7 = new Phrase();
				phrase7.add(new Chunk("2. ",boldFont));
				phrase7.add(new Chunk("Your appointment is further subject to the information / references furnished by you being correct. In case "
						+ "particulars mentioned in your application are found false or unsatisfactory, your services are liable for "
						+ "termination without notice or payment of any remuneration in lieu thereof. \r\n",boldFont2));
				
				Phrase phrase8 = new Phrase();
				phrase8.add(new Chunk("3. ",boldFont));
				phrase8.add(new Chunk("You shall be required to sign and abide by the NDA (Non-Disclosure Agreement) and shall undertake to sign "
						+ "such declarations that the Policy may demand from time to time. \r\n",boldFont2));
				
				Phrase phrase9 = new Phrase();
				phrase9.add(new Chunk("4. ",boldFont));
				phrase9.add(new Chunk("You are required to submit attested copies of certificates in support of your educational / professions "
						+ "qualifications and experience date of birth and other testimonials and cause production of originals as and when "
						+ "demanded." + "\r\n" + "\r\n",boldFont2));
				
				Phrase phrase10 = new Phrase();
				phrase10.add(new Chunk("We look forward to your joining the Organization for a pleasant association with your remarkable contribution for ", boldFont2));
				phrase10.add(new Chunk(schoolName + ".\r\n" + "\r\n" + "\r\n", boldFont));
				
				Phrase phrase11 = new Phrase("Managing Director ", boldFont);
				
//				Phrase phrase3 = new Phrase("Yours Sincerely,\r\n" + "\r\n", boldFont2);
//				Phrase phrase4 = new Phrase("Managing Director\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n", boldFont2);
//				Phrase phrase5 = new Phrase("*Encl: The details of the CTC/Salary break up in the Annexure 1 is appended here too.\r\n" + "*Note: This offer is valid for 7 days from offer date.",boldFont2);

				offerCodeAndDateParagraph.add(offerCodeAndDatePhrase);
				offerCodeAndDateParagraph.setAlignment(Element.ALIGN_CENTER);
				
				addressParagraph.add(addressPhrase);
				addressParagraph.setAlignment(Element.ALIGN_LEFT);
				
				paragraph.add(phrase);
				paragraph.setAlignment(Element.ALIGN_CENTER);
				
				paragraph1.add(phrase1);
				paragraph1.setAlignment(Element.ALIGN_JUSTIFIED);
	
				paragraph2.add(phrase2);
				paragraph2.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph3.add(phrase3);
				paragraph3.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph4.add(phrase4); 
				paragraph4.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph5.add(phrase5);
				paragraph5.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph6.add(phrase6);
				paragraph6.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph7.add(phrase7);
				paragraph7.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph8.add(phrase8);
				paragraph8.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph9.add(phrase9);
				paragraph9.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph10.add(phrase10);
				paragraph10.setAlignment(Element.ALIGN_JUSTIFIED);
				
				paragraph11.add(phrase11);
				paragraph11.setAlignment(Element.ALIGN_JUSTIFIED);
				
				document.add(imgdata1);
//				document.add(imgdata2);
				document.add(offerCodeAndDateParagraph);
				document.add(addressParagraph);
				document.add(paragraph);
				document.add(paragraph1);
				document.add(paragraph2);
				document.add(paragraph3);
				document.add(paragraph4);
				document.add(paragraph5);
				document.add(paragraph6);
				document.add(paragraph7);
				document.add(paragraph8);
				document.add(paragraph9);
				document.add(paragraph10);
				document.add(paragraph11);

			
		}

//		       document.add(imgdata);

		document.close();
	}

	public Object sendMailForStaff(Integer job_id, Integer offer_id) throws Exception {
		String firstName = getname(job_id);
		String jobType = getjobType(job_id);
		String designation = getDesignation(offer_id);
		String offerName = getofferName(offer_id);
		String email = getemailOffer(offer_id);
		String reportingDateTime = getDateTime(offer_id);
		String contents = "Dear Sir/Madam," + "<br><br>"
				+ "This is to bring to your notice that the below candidate shall report to you on&nbsp;"
				+ reportingDateTime + "<html><head>" + "<style>" + "table {" + "  font-family: arial, sans-serif;"
				+ "  border-collapse: collapse;" + "  width: 60%;" + "}" + "th {\r\n" + "  border: 1px solid #dddddd;"
				+ "  text-align: center;" + "  padding: 5px;" + "background-color : #eee;color:black" + "}" + "td{"
				+ "border: 1px solid #dddddd;text-align:center;padding:5px" + "}" + "</style>"
				+ "</head><body><br><table>" + "  <tr>" + "    <th>Candidate Name</th>" + "    <th>Designation</th>"
				+ "    <th>Job type</th>" + "     <th>Offer Status</th>" + "  </tr>" + " <tr>\r\n" + "    <td>"
				+ firstName + "&nbsp;" + "</td>" + "    <td>" + designation + "</td>" + "    <td>" + jobType + "</td>"
				+ "      <td>" + offerName + "</td>" + "  </tr>" + "</table></body></html>" + "<br/> " + "<br/> " 
				+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
				+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
		
		String subject = "Candidate for the post of <" + designation + "> selected - reg";
		response_handler.sendMailWithTableFormat(email, contents, subject);
		System.out.println("Mail Send...");
		return null;
	}

	private String getDateTime(Integer offer_id) {
		return or_repo.getDateTime(offer_id);
	}

	private String getemailOffer(Integer offer_id) {
		return or_repo.getEmail(offer_id);
	}

	private String getDesignation(Integer offer_id) {
		return or_repo.getDesignation(offer_id);
	}

	private String getofferName(Integer offer_id) {
		return or_repo.getofferName(offer_id);
	}

	public String getname(Integer job_id) {
		return jpr_repo.getFirstname(job_id);
	}

	public String getjobType(Integer job_id) {
		return jpr_repo.getjobType(job_id);
	}

//	public String getLastname(Integer job_id) {
//		return jpr_repo.getLastname(job_id);
//	}

	public String getEmail(Integer job_id) {
		return jpr_repo.getEmail(job_id);
	}

	public Offer getDetails(Offer offer) {
		return or_repo.save(offer);
	}

	public Object updateOfferAfterAccepting(Integer offer_id, Boolean offerstatus, String ip_address) {
		return or_repo.updateOfferStatus(offer_id, offerstatus, ip_address);
	}

	public List<Offer>  offerDetailsByJobId(Integer job_id) {
		return or_repo.offerDetailsByJobId(job_id);
	}
	
	public List<Offer> getMultipleOfferDetails(List<Integer> offer_ids){
		return or_repo.findAllById(offer_ids);
	}
	
	public void updateMultipleOfferDetails(List<Offer> offer){
		or_repo.saveAll(offer);
	}

	public List<HashMap<String, Object>> fetchAllOfferDetailsByEmployeeId(Integer employeeId) {
		return  employeeDetailsRepository.fetchAllOfferDetailsByEmployeeId(employeeId);
	}

//	public List<Map<String, Object>> encryptedFetchAllOfferDetailsByEmployeeId(Integer employeeId) {
//		return  employeeDetailsRepository.encryptedFetchAllOfferDetailsByEmployeeId(employeeId);
//	}
	
	 public Map<String, Object> encryptedFetchAllOfferDetailsByEmployeeId(Integer employeeId) {
	        // Fetch employee details from the repository
	        Map<String, Object> data = employeeDetailsRepository.encryptedFetchAllOfferDetailsByEmployeeId(employeeId);

	        // Handle null values by replacing them with empty strings or other default values
	        for (Map.Entry<String, Object> entry : data.entrySet()) {
	            if (entry.getValue() == null) {
	                entry.setValue(""); // Replace null values with empty string or another default value
	            }
	        }

	        try {
	            // Convert the data to a JSON string before encryption
	            ObjectMapper objectMapper = new ObjectMapper();
	            String jsonData = objectMapper.writeValueAsString(data);

	            // Encrypt the data using the encryption utility
	            String encryptedData = EncryptionUtil.encrypt(jsonData);

	            // Create a new map to hold the encrypted data
	            Map<String, Object> encryptedResponse = new HashMap<>();
	            encryptedResponse.put("encryptedEmployeeDetails", encryptedData);

	            return encryptedResponse;
	        } catch (Exception e) {
	            // Handle encryption failure
	            e.printStackTrace();
	            throw new RuntimeException("Error during encryption");
	        }
	    }
	
}
