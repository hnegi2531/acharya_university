package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.ProctorStudentMeetingDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeDetails;
import com.au.model.ProctorStudentMeeting;
import com.au.model.Student_Details;
import com.au.repository.CandidateWalkinRepository;
import com.au.repository.DepartmentRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.ProctorStudentMeetingRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class ProctorStudentMeetingService {
	
	@Autowired
	private ProctorStudentMeetingRepository psm_repo;
	
	@Autowired
	private CandidateWalkinRepository can_repo;
	
	@Autowired
	private ResponseHandler response_handler;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private DepartmentRepository deptrepo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;

	public List<ProctorStudentMeeting> saveProctorStudentMeeting(ProctorStudentMeetingDto r) throws Exception {
	
		List<ProctorStudentMeeting> psm_list = new ArrayList<ProctorStudentMeeting>();
		
		r.getStudent_ids().stream().forEach(stu -> {
			
			ProctorStudentMeeting psm = new ProctorStudentMeeting();
			psm.setUser_id(r.getUser_id());
			psm.setActive(r.getActive());
			psm.setChief_proctor_id(r.getChief_proctor_id());
			psm.setCreated_by(r.getCreated_by());
			psm.setCreated_username(r.getCreated_username());
			psm.setDate_of_meeting(r.getDate_of_meeting());
			psm.setEmp_id(uar_repo.getEmployee_id(r.getUser_id()));
			psm.setFaq_id(r.getFaq_id());
			psm.setFeedback(r.getFeedback());
			psm.setFeedback_date(r.getFeedback_date());
			psm.setMeeting_agenda(r.getMeeting_agenda());
			psm.setMeeting_id(r.getMeeting_id());
			psm.setMeeting_type(r.getMeeting_type());
			psm.setMode_of_contact(r.getMode_of_contact());
			psm.setParent_name(r.getParent_name());
			psm.setProctor_id(r.getProctor_id());
			psm.setRemarks(r.getRemarks());
			psm.setSchool_id(r.getSchool_id());
			psm.setModeof_connect(r.getModeof_connect());
			psm.setStudent_parent(r.getStudent_parent());
			psm.setStudent_id(stu);
			
			psm_list.add(psm);
		});
		return psm_repo.saveAll(psm_list);
	}
	
	public List<ProctorStudentMeeting> listAll() {
		return psm_repo.findAll();
	}
	
	public List<ProctorStudentMeeting> updateProctorStudentMeeting(@Valid List<ProctorStudentMeeting> r) {
		return psm_repo.saveAll(r);	
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		List<Map<String, Object>> psm_filtered_response = psm_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, psm_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		List<Map<String, Object>> psm_sorted_response = psm_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, psm_sorted_response);
	}

	public ProctorStudentMeeting get(Integer id) {
		return psm_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProctorStudentMeeting Not Found:" + id));
	}

	public void delete(Integer id) {
		ProctorStudentMeeting cc = psm_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProctorStudentMeeting Not Found:" + id));
		psm_repo.updateProctorStudentMeeting(id);
	}

	public void delete1(Integer id) {
		ProctorStudentMeeting cc = psm_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProctorStudentMeeting Not Found:" + id));
		psm_repo.updateProctorStudentMeeting1(id);

	}

//	public String sendTelegramMessage(List<Integer> student_ids, JwtDetails jwtDetails) {
//		
//		List<String> telegram_chat_ids = can_repo.getTelegramChatId(student_ids);
//		System.out.println("################# "+telegram_chat_ids);
//		if(telegram_chat_ids.contains(null)) {
//			throw new ResourceNotFoundException("Telegram not verified");
//		}
//		System.out.println("#####@@@@@@@@@@@@@@@@@@@@@########### "+telegram_chat_ids);
//		telegram_chat_ids.stream().forEach(t -> {
//			System.out.println("#####@@@@@@@@@@1111111111111111@@@@@@@@@@@########### "+t);
//			Student_Details sd = stu_repo.getStudentDataByTelegramChatId(t);
//			response_handler.sendMessageToUser(t,"Hello Mr."+sd.getStudent_name()+"("+sd.getStudent_master_code()+")");
//		});
//		
//		return "Message sent successfully!";	
//	}
	
	String employeeName = null;
	public Object sendEmailMessageForMeeting(List<Integer> student_ids,JwtDetails jwtDetails, Integer user_id,String agenda_of_meeting, String description, 
			String date_of_meeting) throws Exception {

		Integer emp_id = uar_repo.getEmployee_id(user_id);
		System.out.println("222222222222222222222222222222 "+emp_id);
		EmployeeDetails emp_details = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);

		if (emp_details.getGender() == 'M')
			employeeName = "Mr. " + emp_details.getEmployee_name();
		else
			employeeName = "Ms. " + emp_details.getEmployee_name();
		
		String department = deptrepo.getDepartment(emp_details.getDept_id());
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "+student_ids);
		student_ids.stream().forEach(stu -> {
			System.out.println("::::::::::::::::::::::::::::::::::::::::: "+stu);
		Student_Details sd = stu_repo.getStudentByStudentId(stu);
		
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbb "+sd.getAcharya_email());
			
		String contents = "<p style='font-size:120%;font-family: Rockwell Extra Bold;text-align: justify;'>"
				+ "Dear  " + sd.getStudent_name() + " ,<br><br> " 
				+ "You are here with notified to attend the meeting with your Mentor " + employeeName + " on " + date_of_meeting + " @ " + "" +"<br><br>"
				+ "Agenda of Meeting : " + agenda_of_meeting + "<br>" + "Description : "+ description + "<br><br><br>"
				+ "Regards<br>" + "Team ERP"
				+ "<br>" + "Acharya University" + "<br>"
				+ "<br>"+"</p>";


		String subject = "Mentor-Student Meeting";
		try {
			response_handler.sendMailWithTableFormatNew(sd.getAcharya_email(), contents, subject , true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Mail Send...");
		});
		employeeName=null;
		return "Mail sent successfully";
	}

public ResponseEntity<Object> getAllDataFilteredByKeyword22(Pageable pageable, Object keyword, Integer user_id) {
		
		List<Map<String, Object>> psm_filtered_response = psm_repo.getAllDataFilteredByKeyword22(pageable, keyword,user_id);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, psm_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData22(Pageable pageable, Integer user_id) {

		List<Map<String, Object>> psm_sorted_response = psm_repo.getAllSortedData22(pageable,user_id);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, psm_sorted_response);
	}

	public List<Map<String, Object>> getAllMailHistoryBasedOnMentor(Integer emp_id) {
		return psm_repo.getAllMailHistoryBasedOnMentor(emp_id);
	}	
	

}
