package com.au.event;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.au.model.ForeignRegionalRegistrationOfficesDetails;
import com.au.model.ProctorStudentAssignment;
import com.au.model.Student_Details;
import com.au.repository.ForeignRegionalRegistrationOfficesDetailsRepository;
import com.au.repository.ProctorStudentAssignmentRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;

@Component
public class FrroPassportAndVisaExpiryPublisher {
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private ResponseHandler response_handler;
	
	@Autowired
	private ProctorStudentAssignmentRepository psar_repo;
	
	
	@Autowired
	private ForeignRegionalRegistrationOfficesDetailsRepository frrod_repo;
	
	@EventListener
	@Async
	public void handleFrroPassportAndVisaExpiry20DaysPrior(FrroPassportAndVisaExpiry frro) {
		try {

			System.out.println(":::::::::::::::::::::::::::::::::::::::::::: "+frro.getExpectedExpiryDate20());
			
			 List<ForeignRegionalRegistrationOfficesDetails> frroStudentData =  frrod_repo.getFrroData(frro.getExpectedExpiryDate20());	  

	        List<ForeignRegionalRegistrationOfficesDetails> passpportToBeExpiredDataAfter20Days = frroStudentData.stream().filter(data -> data.getPassport_expiry_date().equals(frro.getExpectedExpiryDate20())).collect(Collectors.toList());
	        List<ForeignRegionalRegistrationOfficesDetails> visaToBeExpiredDataAfter20Days = frroStudentData.stream().filter(data -> data.getVisa_expiry_date().equals(frro.getExpectedExpiryDate20())).collect(Collectors.toList());  

	        if(ObjectUtils.isNotEmpty(passpportToBeExpiredDataAfter20Days)) {
	        	
	        	getDataAndTriggerEmail(passpportToBeExpiredDataAfter20Days, "PASSPORT");
	        		
	        }
	        
	        if(ObjectUtils.isNotEmpty(visaToBeExpiredDataAfter20Days)) {
	        	
	        	getDataAndTriggerEmail(visaToBeExpiredDataAfter20Days, "VISA");
	        	
	        }
	       
		 } catch (Exception e) {
			System.out.println("Exception occur:- " + e.getMessage());
		}
	}


//	@EventListener
//	@Async
//	public void handleFrroPassportAndVisaExpiry15DaysPrior(FrroPassportAndVisaExpiry frro) {
//		try {
//
//			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ "+frro.getExpectedExpiryDate15());
//			
//			List<ForeignRegionalRegistrationOfficesDetails> frroStudentData =  frrod_repo.getFrroData(frro.getExpectedExpiryDate15());	    
//			List<ForeignRegionalRegistrationOfficesDetails> passpportToBeExpiredDataAfter15Days = frroStudentData.stream().filter(data -> data.getPassport_expiry_date().equals(frro.getExpectedExpiryDate15())).collect(Collectors.toList());
//	        List<ForeignRegionalRegistrationOfficesDetails> visaToBeExpiredDataAfter15Days = frroStudentData.stream().filter(data -> data.getVisa_expiry_date().equals(frro.getExpectedExpiryDate15())).collect(Collectors.toList());  
//	        
//	        if(ObjectUtils.isNotEmpty(passpportToBeExpiredDataAfter15Days)) {
//	        	
//	        	getDataAndTriggerEmail(passpportToBeExpiredDataAfter15Days, "PASSPORT");
//	        		
//	        }
//	        
//	        if(ObjectUtils.isNotEmpty(visaToBeExpiredDataAfter15Days)) {
//	        	
//	        	getDataAndTriggerEmail(visaToBeExpiredDataAfter15Days, "VISA");
//	        	
//	        }
//	        
//		 } catch (Exception e) {
//			System.out.println("Exception occur:- " + e.getMessage());
//		}
//	}
//	
//	@EventListener
//	@Async
//	public void handleFrroPassportAndVisaExpiry07DaysPrior(FrroPassportAndVisaExpiry frro) {
//		try {
//				
//			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% "+frro.getExpectedExpiryDate07());
//			
//			List<ForeignRegionalRegistrationOfficesDetails> frroStudentData =  frrod_repo.getFrroData(frro.getExpectedExpiryDate07());
//			List<ForeignRegionalRegistrationOfficesDetails> passpportToBeExpiredDataAfter07Days = frroStudentData.stream().filter(data -> data.getPassport_expiry_date().equals(frro.getExpectedExpiryDate07())).collect(Collectors.toList());
//	        List<ForeignRegionalRegistrationOfficesDetails> visaToBeExpiredDataAfter07Days = frroStudentData.stream().filter(data -> data.getVisa_expiry_date().equals(frro.getExpectedExpiryDate07())).collect(Collectors.toList());  
//	        
//	        if(ObjectUtils.isNotEmpty(passpportToBeExpiredDataAfter07Days)) {
//	        	
//	        	getDataAndTriggerEmail(passpportToBeExpiredDataAfter07Days, "PASSPORT");
//	        		
//	        }
//	        
//	        if(ObjectUtils.isNotEmpty(visaToBeExpiredDataAfter07Days)) {
//	        	
//	        	getDataAndTriggerEmail(visaToBeExpiredDataAfter07Days, "VISA");
//	        	
//	        }
//	        
//		 } catch (Exception e) {
//			System.out.println("Exception occur:- " + e.getMessage());
//		}
//	}
//	
//	@EventListener
//	@Async
//	public void handleFrroPassportAndVisaExpired(FrroPassportAndVisaExpiry frro) {
//		try {
//
//			System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ "+frro.getExpired());
//			
//			List<ForeignRegionalRegistrationOfficesDetails> frroStudentData =  frrod_repo.getFrroData(frro.getExpired());    
//			List<ForeignRegionalRegistrationOfficesDetails> passpportExpired = frroStudentData.stream().filter(data -> data.getPassport_expiry_date().equals(frro.getExpired())).collect(Collectors.toList());
//	        List<ForeignRegionalRegistrationOfficesDetails> visaExpired = frroStudentData.stream().filter(data -> data.getVisa_expiry_date().equals(frro.getExpired())).collect(Collectors.toList());  
//	        
//	        if(ObjectUtils.isNotEmpty(passpportExpired)) {
//	        	
//	        	getDataAndTriggerEmail(passpportExpired, "PASSPORT");
//	        		
//	        }
//	        
//	        if(ObjectUtils.isNotEmpty(visaExpired)) {
//	        	
//	        	getDataAndTriggerEmail(visaExpired, "VISA");
//	        	
//	        }
//			
//		 } catch (Exception e) {
//			System.out.println("Exception occur:- " + e.getMessage());
//		}
//	}
	
	
	private void getDataAndTriggerEmail(List<ForeignRegionalRegistrationOfficesDetails> listOfFrroStudents, String document) throws IOException {

		Set<Integer> studentIds = new HashSet<Integer>();
    	
		listOfFrroStudents.stream().forEach(fr -> {
    		studentIds.add((Integer) fr.getStudent_id());
    	});

    	List<Student_Details> studentData = stu_repo.getStudentData(studentIds);
    	Map<Integer, List<Student_Details>> schoolWiseStudentData = studentData.stream().collect(Collectors.groupingBy(Student_Details::getSchool_id, LinkedHashMap::new, Collectors.toList()));
    	
    	schoolWiseStudentData.forEach((k, v) -> {
    	
    		System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJ   "+k);
    		Set<Integer> listOfStudentIdsOnSchool = new HashSet<Integer>();
    		v.stream().forEach(sd -> {
    			listOfStudentIdsOnSchool.add(sd.getStudent_id());
    		});
    		System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJ   "+k + "----------------------------------------------"+listOfStudentIdsOnSchool);
    		List<Map<String, Object>> frroStudentDataByStudentIds = frrod_repo.getFrroStudentDataByStudentIds(k, listOfStudentIdsOnSchool);

					try {
						triggerEmailsToPrincipalAndAdministrator(frroStudentDataByStudentIds, document);
					} catch (IOException e) {
						e.printStackTrace();
					}

    		
    	});
    	
    	List<ProctorStudentAssignment> proctorData = psar_repo.getProctorData(studentIds);
    	Map<Integer, List<ProctorStudentAssignment>> proctorWiseStudentData = proctorData.stream().collect(Collectors.groupingBy(ProctorStudentAssignment::getEmp_id, LinkedHashMap::new, Collectors.toList()));
    	
    	
    	proctorWiseStudentData.forEach((k, v) -> {
        	
    		System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJ   "+k);
    		Set<Integer> listOfStudentIdsOnProctor = new HashSet<Integer>();
    		v.stream().forEach(sd -> {
    			listOfStudentIdsOnProctor.add(sd.getStudent_id());
    		});
    		System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJ   "+k + "----------------------------------------------"+listOfStudentIdsOnProctor);
    		List<Map<String, Object>> frroStudentDataByStudentIds = frrod_repo.getFrroStudentDataByStudentIds(k, listOfStudentIdsOnProctor);

    		triggerEmailsToProctor(frroStudentDataByStudentIds, document);
    		
    	});
		
	}
	
	Integer count = 0;
	String contents = null;
	public Object triggerEmailsToPrincipalAndAdministrator(List<Map<String, Object>> frroStudentDataByStudentIds, String document) throws IOException {
		
		String principalEmail = frroStudentDataByStudentIds.get(0).get("principalEmail").toString();
		String administratorEmail = frroStudentDataByStudentIds.get(0).get("administratorEmail").toString();
		
		String contentForEmail = getContentForEmail(frroStudentDataByStudentIds, document);

		String subject = document + " EXPIRED!! URGENT ACTION";
			try {
				
				response_handler.sendMailWithTableFormat(principalEmail, contentForEmail, subject);
				response_handler.sendMailWithTableFormat(administratorEmail, contentForEmail, subject);

			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Mail Send...");
			count=0;
			contents=null;

		return null;
		
	}

	public Object triggerEmailsToProctor(List<Map<String, Object>> frroStudentDataByStudentIds, String document) {
		
		String proctorEmail = frroStudentDataByStudentIds.get(0).get("proctorEmail").toString();
		
		String contentForEmail = getContentForEmail(frroStudentDataByStudentIds, document);
		
		String subject = document + " EXPIRED!! URGENT ACTION";
		try {
			
			response_handler.sendMailWithTableFormat(proctorEmail, contentForEmail, subject);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Mail Send...");
		count=0;
		contents=null;

	return null;
		
	}

	

	private String getContentForEmail(List<Map<String, Object>> frroStudentDataByStudentIds, String document) {
		String changingContent = null;
		
		LocalDate currentDate = LocalDate.now();
		System.out.println("Todayyyyyyyysssss Dateeeeee : "+currentDate);
		
		String date = frroStudentDataByStudentIds.get(0).get("passport_expiry_date").toString();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String format = LocalDate.parse(date, formatter).format(formatter2);
		System.out.println("``````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````` "+format);
		LocalDate expiredLocalDate = LocalDate.parse(format);

		System.out.println("Exxxxxpiiiiiiirrryyyyyyyy Dateeeeee : "+expiredLocalDate);
		
		if(currentDate.isAfter(expiredLocalDate))
			changingContent="Please note that the below mentioned students " + document.toLowerCase() + " has expired";
		else
			changingContent="Please note that the below mentioned students " + document.toLowerCase() + " will expire on " +frroStudentDataByStudentIds.get(0).get("passport_expiry_date");
			
			
		contents = "Dear Sir/Madam," + "<br><br>"
				+ changingContent +"..." + "<br>"
				+ "Renew immediately or take suitable action to avoid legal and statutory issues.&nbsp;"
				+ "<html><head>" + "<style>" + "table {" + "  font-family: Calibri ;"
				+ "  border-collapse: collapse;" + "  width: 100%;" + "}" + "th {\r\n" + "  border: 1px solid #dddddd;"
				+ "  text-align: center;" + "  padding: 5px;" + "background-color : #4A57A9;color:white" + "}" + "td{"
				+ "border: 1px solid #dddddd;text-align:center;padding:5px" + "}" + "</style>"
				+ "</head><body><br><table>" + "  <tr>" + "    <th>SL No.</th>" + "    <th>Student Name</th> " + "	 <th>AUID</th>" + "		<th>USN</th>"
				+ "		<th>Ph No.</th>" + "		<th>Email-ID</th>"+ "		<th>Visa No</th>"+ "		<th>Visa Issue Date</th>"
				+ "		<th>	Visa Expiry Date</th>"+ "		<th>Passport No</th>" + "		<th>Passport Issue Date</th>" + "		<th>Passport Expiry Date</th>";    /*   width=20%    */
		
		frroStudentDataByStudentIds.stream().forEach(frro -> {
			count++;
			contents = contents + " <tr>\r\n" + "    <td>"
					+ count  + "</td>" + "    <td>" + frro.get("student_name") + "</td>" + "    <td>" + frro.get("auid") + "</td>" 
					+ "    <td>" + frro.get("usn") + "</td>" + "    <td>" + frro.get("mobile") + "</td>" 
					+ "    <td>" + frro.get("studentAcharyaEmail") + "</td> " + "    <td>" + frro.get("visa_no") + "</td> " 
					+ "    <td>" + frro.get("visa_issue_date") + "</td>" + "    <td>" + frro.get("visa_expiry_date")  + "</td>" + "    <td>" + frro.get("passport_no")
					+ "</td>" + "      <td>" + frro.get("passport_issue_date") + "</td>" + "    <td>" + frro.get("passport_expiry_date")+ "</td>" + "  </tr>";
			
		});
		
		contents = contents +  "</table>" + "<br> " + "<br> " + "Regards, " + "<br> "  + "<br> "  + "Team Acharya" + "<br> "  + "<br> "
				+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br> "
				+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> " 
				+ "</body></html>";
		
		count=0;
		return contents;
		
	}


	

}
