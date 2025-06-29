package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.MealRefreshmentDTO;
import com.au.dto.MessMealRequestDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.MealRefreshmentRequest;
import com.au.repository.MealRefreshmentRequestRepository;
import com.au.repository.MealTypeRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;


@Service
public class MealRefreshmentRequestService {
	
	Logger log = LoggerFactory.getLogger(MealRefreshmentRequestService.class);
	
	@Autowired
	private MealRefreshmentRequestRepository mealref_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private MealTypeRepository mts_repo;
	
	@Autowired
	private ResponseHandler response_handler;
	
	public final static long HOURS_PER_DAY = 24;
	
	public MealRefreshmentRequest saveMealRefreshmentRequest(MealRefreshmentRequest meal) throws Exception {
		
		if(meal.getDept_id()!= null && meal.getSchool_id() != null) {
			return	mealref_repo.save(meal);
		}else {
			Map<String, Object> deptSchool =	mealref_repo.getDeptAndSchool_ids(meal.getCreated_by());
			meal.setDept_id((Integer) deptSchool.get("dept_id"));
			meal.setSchool_id((Integer) deptSchool.get("school_id"));
			
			return	mealref_repo.save(meal);
		}
		
		}
	
		
//		MealType mt = mts_repo.getOne(meal.getMeal_id());

//		if(mt.getFor_end_user() == Boolean.TRUE) {
//			String mealdateTime = meal.getDate()+" "+meal.getTime();
//			
//			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//			  LocalDate localDate = LocalDate.now();
//
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
//			  LocalTime localTime = LocalTime.now();
//
//			String currentStringDateTime = dtf1.format(localDate).toString() + " " + dtf.format(localTime.plusHours(5).plusMinutes(30)).toString();	
//			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//			Date d1 = sdf.parse(currentStringDateTime);
//			Date d2 = sdf.parse(mealdateTime);
//			
//			 long difference_In_Time
//             = d2.getTime() - d1.getTime();
//			 
//			 long difference_In_Days
//             = (difference_In_Time
//                / (1000 * 60 * 60 * 24))
//               % 365;
//			 
//			 System.out.print("Difference between two dates is: "+difference_In_Days);
//			
//			
//			if(difference_In_Days < 1) {
//				throw new RuntimeException("Meal request time should be more than 24 hours from the time of submitting request!");
//			} 	
//		}
	
	
	Date date1=null;
	public List<MealRefreshmentRequest> MealRefreshmentRequestForMultipleDates(MessMealRequestDto mealdto) throws Exception {
		
		List<MealRefreshmentRequest> list = new ArrayList<MealRefreshmentRequest>();
		
		mealdto.getDate().stream().forEach(date -> {
			if(mealref_repo.getCountOfMealTypeAndDate(mealdto.getMeal_id(),date,mealdto.getUser_id(),mealdto.getCount()) >= 1) {
				throw new RuntimeException("Meal type is already present in this selected date !!!");
			}
			
		if(mealdto.getDept_id()!= null && mealdto.getSchool_id() != null) {
				
				MealRefreshmentRequest mealrefresh = new MealRefreshmentRequest();
				mealrefresh.setActive(mealdto.getActive());
				mealrefresh.setCreated_by(mealdto.getCreated_by());
				mealrefresh.setCreated_username(mealdto.getCreated_username());
				mealrefresh.setDate(date);
				mealrefresh.setCount(mealdto.getCount());
				mealrefresh.setTime(mealdto.getTime());
				mealrefresh.setRemarks(mealdto.getRemarks());
				mealrefresh.setMeal_id(mealdto.getMeal_id());
				mealrefresh.setApproved_status(mealdto.getApproved_status());
				mealrefresh.setEnd_user_feedback_remarks(mealdto.getEnd_user_feedback_remarks());
				mealrefresh.setCancel_date(mealdto.getCancel_date());
				mealrefresh.setReceive_date(mealdto.getReceive_date());
				mealrefresh.setReceive_status(mealdto.getReceive_status());
				mealrefresh.setDept_id(mealdto.getDept_id());
				mealrefresh.setSchool_id(mealdto.getSchool_id());
				mealrefresh.setUser_id(mealdto.getUser_id());
				mealrefresh.setTime_for_frontend(mealdto.getTime_for_frontend());
				list.add(mealrefresh);
		}else {
			Map<String, Object> deptSchool =	mealref_repo.getDeptAndSchool_ids(mealdto.getCreated_by());
		
				MealRefreshmentRequest mealrefresh = new MealRefreshmentRequest();
				mealrefresh.setActive(mealdto.getActive());
				mealrefresh.setCreated_by(mealdto.getCreated_by());
				mealrefresh.setCreated_username(mealdto.getCreated_username());
				mealrefresh.setDate(date);
				mealrefresh.setCount(mealdto.getCount());
				mealrefresh.setTime(mealdto.getTime());
				mealrefresh.setRemarks(mealdto.getRemarks());
				mealrefresh.setMeal_id(mealdto.getMeal_id());
				mealrefresh.setApproved_status(mealdto.getApproved_status());
				mealrefresh.setEnd_user_feedback_remarks(mealdto.getEnd_user_feedback_remarks());
				mealrefresh.setCancel_date(mealdto.getCancel_date());
				mealrefresh.setReceive_date(mealdto.getReceive_date());
				mealrefresh.setReceive_status(mealdto.getReceive_status());
				mealrefresh.setUser_id(mealdto.getUser_id());
				mealrefresh.setTime_for_frontend(mealdto.getTime_for_frontend());
				mealrefresh.setDept_id((Integer) deptSchool.get("dept_id"));
				mealrefresh.setSchool_id((Integer) deptSchool.get("school_id"));
				list.add(mealrefresh);
	
		}
		});
		
		return	mealref_repo.saveAll(list);
		
	}
	
	

	public List<MealRefreshmentRequest> listAll1() {
		return mealref_repo.findAll1();
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer user_id) {
	
		Page<Object> mealRefreshment_filtered_response = mealref_repo.getAllDataFilteredByKeyword(pageable, keyword ,user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_filtered_response);
		}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer user_id) {
	
		Page<Object> mealRefreshment_sorted_response = mealref_repo.getAllSortedData(pageable,user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_sorted_response);
	}
	
	public MealRefreshmentRequest get(Integer refreshment_id) {
		return mealref_repo.findById(refreshment_id)
				.orElseThrow(() -> new ResourceNotFoundException("Meal Request Not Found:" + refreshment_id));
	}
	
	public MealRefreshmentRequest saveUpdateMealRefreshmentRequest(MealRefreshmentRequest meal) {
		
		return mealref_repo.save(meal);
	}
	
	public void delete(Integer refreshment_id) {
		mealref_repo.findById(refreshment_id)
				.orElseThrow(() -> new ResourceNotFoundException("Meal Request Not Found:" + refreshment_id));
		mealref_repo.update(refreshment_id);
	}
	
	public void delete1(Integer refreshment_id) {
		
		mealref_repo.findById(refreshment_id)
		.orElseThrow(() -> new ResourceNotFoundException("Meal Request Not Found:" + refreshment_id));
		mealref_repo.update1(refreshment_id);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword11(Pageable pageable,Object keyword) {
		Page<Object> mealRefreshment_filtered_response = mealref_repo.getAllDataFilteredByKeyword11(pageable,keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_filtered_response);
		}
	
	public ResponseEntity<Object> getAllSortedData11(Pageable pageable) {
		Page<Object> mealRefreshment_sorted_response = mealref_repo.getAllSortedData11(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_sorted_response);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword1(Pageable pageable ,Object keyword, String month, String year,Integer approved_status) {
		Page<Object> mealRefreshment_filtered_response = mealref_repo.getAllDataFilteredByKeyword1(pageable ,keyword,month,year,approved_status);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_filtered_response);
		}
	
	public ResponseEntity<Object> getAllSortedData1(Pageable pageable, String month, String year, Integer approved_status) {
		Page<Object> mealRefreshment_sorted_response = mealref_repo.getAllSortedData1(pageable,month,year,approved_status);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_sorted_response);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword1Pending(Pageable pageable,Object keyword, String month,String year,  Integer approved_status) {
		Page<Object> mealRefreshment_filtered_response = mealref_repo.getAllDataFilteredByKeyword1pending(pageable,keyword,month,year,approved_status );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_filtered_response);
		}
	
	public ResponseEntity<Object> getAllSortedData1Pending(Pageable pageable,String month, String year, Integer approved_status) {
		Page<Object> mealRefreshment_sorted_response = mealref_repo.getAllSortedData1Pending(pageable,month,year,approved_status);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_sorted_response);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword2(Pageable pageable, Object keyword) {
	
		Page<Object> mealRefreshment_filtered_response = mealref_repo.getAllDataFilteredByKeyword2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_filtered_response);
		}
	
	public ResponseEntity<Object> getAllSortedData2(Pageable pageable) {
	
		Page<Object> mealRefreshment_sorted_response = mealref_repo.getAllSortedData2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_sorted_response);
	}

	
	public List<HashMap<String, Object>> fetchAllMealRefreshmentRequestDetailsForEmailIndex() {
	List<HashMap<String, Object>> email_index_data =  mealref_repo.fetchAllMealRefreshmentRequestDetailsForEmailIndex();
		return email_index_data;
	}
	
	
	public List<HashMap<String, Object>> getFilteredEndUserData(String date) {
		
		List<HashMap<String, Object>> email_index_data =  mealref_repo.getFilteredEndUserData(date);
		if(!email_index_data.isEmpty()) {
		email_index_data.stream().forEach(data -> {
			data.entrySet().stream().forEach(d -> {
				if(d.getKey().equalsIgnoreCase("approved_status")) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+d.getValue());
						if(d.getValue().equals(0)) {
							d.setValue("Pending");
						}else if(d.getValue().equals(1)) {
							d.setValue("Approved");
						}else if(d.getValue().equals(2)) {
							d.setValue("Food Received");
						}else {
							d.setValue("Rejected");
						}
					
				}
			});
		});
		} else {
			throw new RuntimeException("No request found!");
		}
		return email_index_data;
	}

	String contents = null;
	String email = null;
	String end_user_name = null;
	Integer count = 0;
	public Object emailToEndUSerForApprovalOfFoodRequest(String meal_date) throws IOException {
		
		List<Integer> unique_create_by_ids = mealref_repo.getApprovedDataByDateForSendingEmail1(meal_date);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +meal_date);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@! " +meal_date);
//		String[] emails = {"kaushal_2285@acharya.ac.in","hanumanthab@acharya.ac.in"};
		unique_create_by_ids.stream().forEach(d -> {
			
//		email = uar_repo.getEmail(d);
			
			List<Map<String, Object>> data = mealref_repo.getApprovedDataByDateForSendingEmail(d, meal_date);
			email = (String) data.get(0).get("email");
			end_user_name =  (String) data.get(0).get("employee_name");
			String approverEmail = data.get(0).get("approverEmail").toString();
			contents = "Dear " +  end_user_name + "," + "<br><br>"
					+ "Please note that your request for food supply is approved & shall be delivered as scheduled below...&nbsp;"
					+ "<html><head>" + "<style>" + "table {" + "  font-family: Calibri ;"
					+ "  border-collapse: collapse;" + "  width: 100%;" + "}" + "th {\r\n" + "  border: 1px solid #dddddd;"
					+ "  text-align: center;" + "  padding: 5px;" + "background-color : #4A57A9;color:white" + "}" + "td{"
					+ "border: 1px solid #dddddd;text-align:center;padding:5px" + "}" + "</style>"
					+ "</head><body><br><table>" + "  <tr>" + "    <th>SL No.</th>" + "    <th>END USER</th> " + "	 <th>INST</th>" + "		<th>DEPT</th>"
					+ "		<th>PURPOSE</th>" + "		<th>MENU NAME</th>"+ "		<th>QTY</th>"+ "		<th>DELIVERY PLACE</th>"
					+ "		<th>	MEAL DATE & TIME</th>"+ "		<th>APPROVER NAME</th>" + "		<th>APPROVED DATE</th>" + "		<th>APPROVER REMARKS</th>";    /*   width=20%    */
			
			data.stream().forEach(d1 -> {
				count++;
				System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLL "+	d1.get("meal_type"));
				contents = contents + " <tr>\r\n" + "    <td>"
						+ count  + "</td>" + "    <td>" + d1.get("employee_name") + "</td>" + "    <td>" + d1.get("school_name_short") + "</td>" 
						+ "    <td>" + d1.get("dept_name_short") + "</td>" + "    <td>" + d1.get("remarks") + "</td>" 
						+ "    <td>" + d1.get("meal_type") + "</td> " + "    <td>" + d1.get("approved_count") + "</td> " 
						+ "    <td>" + d1.get("delivery_address") + "</td>" + "    <td>" + d1.get("date") + " @ " + d1.get("time_for_frontend") + "</td>" + "    <td>" + d1.get("approver_name")
						+ "</td>" + "      <td>" + d1.get("approved_date") + "</td>" + "    <td>" + d1.get("approver_remarks")+ "</td>" + "  </tr>";
				
			});
			
			contents = contents +  "</table>" + "<br> "  + "For any queries regarding supply, contact "+ data.get(0).get("approver_name") +" on " + data.get(0).get("mobile") 
					+ " or reach on " + approverEmail + ".<br> "  + "<br> " + "<br> " + "Regards<br>" + "<br>Facility & Hospitality Dept."
					+ "<br>Acharya Institutes<br>" + "<br> " + "<br> " 
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> " 
					+ "</body></html>";
			
			String subject = "Refreshment supply request approved";
			try {
				response_handler.sendMailWithTableFormatWithCC(email, approverEmail, contents, subject);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Mail Send...");
			count=0;
			contents=null;
			email=null;
			end_user_name = null;
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+data.get(0).get("email"));
		});
		
		
		System.out.println(":::::::::::::::::::::::::::::::::::::::: "+unique_create_by_ids);
		return null;
		
	}

//	String vendor_name = null;
//	String vendor_email = null;
//	Integer count1 = 0;
//	String contents1 = null;
//	public Object emailToVendorForSupplyOfFoodRequest(String meal_date) {
//		
//		List<Integer> unique_voucher_ids = mealref_repo.getApprovedMealIdsByDateForSendingEmailToVendor(meal_date);
//		System.out.println("22222222222222222222222222222222222222222222222222222 "+unique_voucher_ids );
//		
////		List<Integer> unique_voucher_ids = new ArrayList<Integer>();
////		  for(Integer data: unique_voucher_id) {
////	    	  if(data != null ) { 
////	    		  unique_voucher_ids.add(data);
////		         }
////	      }
//
//		unique_voucher_ids.stream().forEach(vou_id -> {
//			System.out.println("11111111111111111111111111111111111111111111111 "+unique_voucher_ids);
//			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&77 "+vou_id);
//
//			
//			List<Map<String, Object>> data = mealref_repo.getApprovedDataByDateForSendingEmailToVendor(vou_id, meal_date);
//			vendor_email = (String) data.get(0).get("vendor_email");
//			vendor_name =  (String) data.get(0).get("vendor_name");
//			String approverEmail = data.get(0).get("approverEmail").toString();
//			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&77111 "+(String) data.get(0).get("vendor_email"));
//			contents1 = "Dear " +  vendor_name + "," + "<br><br>"
//					+ "With reference to the above subject, please note the order for food supply." + "<br>"
//					+ "<html><head>" + "<style>" + "table {" + "  font-family: Roboto;"
//					+ "  border-collapse: collapse;" + "  width: 100%;" + "}" + "th {\r\n" + "  border: 1px solid #dddddd;"
//					+ "  text-align: center;" + "  padding: 5px;" + "background-color : #4A57A9;color:white" + "}" + "td{"
//					+ "border: 1px solid #dddddd;text-align:center;padding:5px" + "}" + "</style>"
//					+ "</head><body><br><table>" + "  <tr>" + "    <th>SL No.</th>" + "    <th>END USER</th> " + "	 <th>INST</th>" + "		<th>DEPT</th>"
//					+ "		<th>PURPOSE</th>" + "		<th>MENU NAME</th>"+ "		<th>QTY</th>"
//					+ "		<th>	MEAL DATE & TIME</th>"+ "		<th>APPROVER NAME</th>" + "		<th>APPROVED DATE</th>" + "		<th>APPROVER REMARKS</th>";
//			
//			data.stream().forEach(d1 -> {
//				count1++;
//				System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLL "+	d1.get("meal_type"));
//				contents1 = contents1 + " <tr>\r\n" + "    <td>"
//						+ count1  + "</td>" + "    <td>" + d1.get("employee_name") + "</td>" + "    <td>" + d1.get("school_name_short") + "</td>" 
//						+ "    <td>" + d1.get("dept_name_short") + "</td>" + "    <td>" + d1.get("remarks") + "</td>" + "    <td>" + d1.get("meal_type") + "</td>" 
//						+ "    <td>" + d1.get("approved_count") + "</td>" + "    <td>" + d1.get("date") + " @ " + d1.get("time_for_frontend") + "</td>" + "    <td>" + d1.get("approver_name")
//						+ "</td>" + "      <td>" + d1.get("approved_date") + "</td>"  + "    <td>" + d1.get("approver_remarks")+ "</td>" + "  </tr>";
//				
//			});
//			
//			contents1 = contents1 +  "</table>" + "<br> "  + "For any queries regarding supply, contact "+ data.get(0).get("approver_name") +" on " + data.get(0).get("mobile") 
//					+ " or reach on " + approverEmail + ".<br> "  + "<br> " + "<br> " + "Regards<br>" + "<br>Facility & Hospitality Dept."
//					+ "<br>Acharya Institutes<br>" + "<br> " + "<br> " 
//					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br> "
//					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> " 
//					+ "</body></html>";
//			
//			String subject = "Refreshment supply on "+(String) data.get(0).get("date");   
//			try {
//				response_handler.sendMailWithTableFormatWithCC(vendor_email, approverEmail,contents1, subject);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			System.out.println("Mail Send...");
//			count1=0;
//			contents1=null;
//			vendor_email=null;
//			approverEmail=null;
//			vendor_name = null;
//			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+data.get(0).get("email"));
//		});
//		
//		
//		System.out.println(":::::::::::::::::::::::::::::::::::::::: "+unique_voucher_ids);
//
//		return null;
//		
//	}
	
	String vendor_name = null;
	String vendor_email = null;
	Integer count1 = 0;
	String contents1 = null;
	public Object emailToVendorForSupplyOfFoodRequest(String meal_date) {

	    List<Integer> unique_voucher_ids = mealref_repo.getApprovedMealIdsByDateForSendingEmailToVendor(meal_date);
	    System.out.println("22222222222222222222222222222222222222222222222222222 " + unique_voucher_ids);

	    unique_voucher_ids.stream().forEach(vou_id -> {
	        System.out.println("11111111111111111111111111111111111111111111111 " + unique_voucher_ids);
	        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&77 " + vou_id);

	        List<Map<String, Object>> data = mealref_repo.getApprovedDataByDateForSendingEmailToVendor(vou_id, meal_date);
	        System.out.println("*************************************************** " + data.get(0).get("approverEmail").toString());
	        vendor_email = (String) data.get(0).get("vendor_email");
	        vendor_name = (String) data.get(0).get("vendor_name");
	        String approverEmail = data.get(0).get("approverEmail").toString();
	        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&77111 " + (String) data.get(0).get("vendor_email"));


	        contents1 = "Dear " + vendor_name + "," + "<br><br>"
	                + "With reference to the above subject, please note the order for food supply." + "<br>"
	                + "<html><head>" + "<style>" +
	                "/* General styles */" +
	                "table { font-family: Roboto; border-collapse: collapse; width: 100%; }" +
	                "th { border: 1px solid #dddddd; text-align: center; padding: 5px; background-color: #4A57A9; color: white; }" +
	                "td { border: 1px solid #dddddd; text-align: center; padding: 5px; }" +
	                "/* Mobile-specific styles */" +
	                "@media only screen and (max-width: 600px) {" +
	                "    table { width: 100%; font-size: 14px; }" +
	                "    th, td { padding: 8px; }" +
	                "    body { font-size: 14px; }" +
	                "}" +
	                "</style>" +
	                "</head><body><br><table>" +
	                "  <tr>" +
	                "    <th>SL No.</th>" +
	                "    <th>END USER</th>" +
	                "    <th>INST</th>" +
	                "    <th>DEPT</th>" +
	                "    <th>PURPOSE</th>" +
	                "    <th>MENU NAME</th>" +
	                "    <th>QTY</th>" +
	                "    <th>MEAL DATE & TIME</th>" +
	                "    <th>APPROVER NAME</th>" +
	                "    <th>APPROVED DATE</th>" +
	                "    <th>APPROVER REMARKS</th>" +
	                "  </tr>";

	        data.stream().forEach(d1 -> {
	            count1++;
	            contents1 = contents1 + " <tr>" +
	                    "    <td>" + count1 + "</td>" +
	                    "    <td>" + d1.get("employee_name") + "</td>" +
	                    "    <td>" + d1.get("school_name_short") + "</td>" +
	                    "    <td>" + d1.get("dept_name_short") + "</td>" +
	                    "    <td>" + d1.get("remarks") + "</td>" +
	                    "    <td>" + d1.get("meal_type") + "</td>" +
	                    "    <td>" + d1.get("approved_count") + "</td>" +
	                    "    <td>" + d1.get("date") + " @ " + d1.get("time_for_frontend") + "</td>" +
	                    "    <td>" + d1.get("approver_name") + "</td>" +
	                    "    <td>" + d1.get("approved_date") + "</td>" +
	                    "    <td>" + d1.get("approver_remarks") + "</td>" +
	                    "  </tr>";
	        });

	        // Add additional content and footer to the email
	        contents1 = contents1 + "</table>" +
	                "<br>" +
	                "For any queries regarding supply, contact " + data.get(0).get("approver_name") + " on " + data.get(0).get("mobile") +
	                " or reach on " + approverEmail + ".<br><br>" +
	                "Regards,<br><br>" +
	                "Facility & Hospitality Dept.<br>" +
	                "Acharya Institutes<br><br>" +
	                "<div style='background-color:#FAF9F6;padding:15px;width:100%;margin:5px'>" +
	                "<div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div><br>" +
	                "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div>" +
	                "</div>" +
	                "</body></html>";

	        // Set the email subject
	        String subject = "Refreshment supply on " + (String) data.get(0).get("date");

	        try {
	            response_handler.sendMailWithTableFormatWithCC(vendor_email, approverEmail, contents1, subject);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        System.out.println("Mail Sent...");
	        count1 = 0;
	        contents1 = null;
	        vendor_email = null;
	        approverEmail = null;
	        vendor_name = null;
	        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + data.get(0).get("email"));
	    });

	    System.out.println(":::::::::::::::::::::::::::::::::::::::: " + unique_voucher_ids);

	    return null;
	}


	public HashMap<String, Object> getMealRefreshmentRequestByRefreshmentId(Integer refreshment_id) {
		HashMap<String, Object> data = mealref_repo.getMealRefreshmentRequestByRefreshmentId(refreshment_id);
		return data;
	}

	public 	List<Map<String, Object>> messDataForCalendarView(String date) {
		System.out.println(date);
		List<Map<String, Object>> month_data = mealref_repo.messDataForCalendarView(date);
		return month_data;
	}
	
	public HashMap<String, Object> getMealRefreshmentRequestById(Integer refreshment_id) {
		HashMap<String, Object> data = mealref_repo.getMealRefreshmentRequestById(refreshment_id);
		return data;
	}	
	
	public Integer getMealRefreshmentRequestById(Integer meal_id, Integer voucher_head_new_id) {
		Integer rate = mealref_repo.getMealRefreshmentRequestById(meal_id, voucher_head_new_id);
		return rate;
	}	

	public ResponseEntity<Object> getAllDataFilteredByKeyword3(Pageable pageable, Object keyword) {
		
		Page<Object> mealRefreshment_filtered_response = mealref_repo.getAllDataFilteredByKeyword3(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_filtered_response);
		}
	
	public ResponseEntity<Object> getAllSortedData3(Pageable pageable) {
	
		Page<Object> mealRefreshment_sorted_response = mealref_repo.getAllSortedData3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, mealRefreshment_sorted_response);
	}
	
	public List<HashMap<String, Object>> fetchAllMealRefreshmentRequestDetailsForEmailIndexReport() {
	List<HashMap<String, Object>> email_index_data =  mealref_repo.fetchAllMealRefreshmentRequestDetailsForEmailIndexReport();
	return email_index_data;
	}



	public List<HashMap<String, Object>> getVendorData(Integer meal_id) {
		return mealref_repo.getVendorData(meal_id);
	}



	public void updateMailStatus(String approved_date) {
		mealref_repo.updateMailStatus(approved_date);
		
	}



	public ResponseEntity<Object> getMealRefreshmentRequests(Integer voucher_head_new_id, Integer month, Integer year) {
		try {
			List<MealRefreshmentDTO> mealRefreshmentDTOs=mealref_repo.getMealRefreshmentRequests(voucher_head_new_id,month,year);
			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", mealRefreshmentDTOs);
			} catch (Exception e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

			}
	}
	
}
