package com.au.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.au.dto.IvrCreationDto;
import com.au.dto.JwtDetails;
import com.au.dto.UserAuthenticationDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.IvrCreation;
import com.au.model.UserAuthentication;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.IvrCreationRepository;
import com.au.repository.ProctorStudentAssignmentRepository;
import com.au.repository.StudentDetailsRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IvrCreationService {

	@Autowired
	private IvrCreationRepository ivrCreationRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private ProctorStudentAssignmentRepository proctorStudentAssignmentRepository;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	
	public ResponseEntity<Object> callOutboundApi(@Valid IvrCreation tm) {
		try {
			IvrCreation ivrCreation = new IvrCreation();

			ivrCreation.setCallid(tm.getCallid());
			ivrCreation.setStarttime(tm.getStarttime());
			ivrCreation.setEndtime(tm.getEndtime());
			ivrCreation.setFilename(tm.getFilename());
			ivrCreation.setAnsweredtime(tm.getAnsweredtime());
			ivrCreation.setEmp_phone(tm.getEmp_phone());
			ivrCreation.setClicktocalldid(tm.getClicktocalldid());
			ivrCreation.setCallto(tm.getCallto());
			ivrCreation.setDialstatus(tm.getDialstatus());
			ivrCreation.setDirection(tm.getDirection());
			ivrCreation.setDisconnectedby(tm.getDisconnectedby());
			ivrCreation.setGroupname(tm.getGroupname());
			ivrCreation.setAgentname(tm.getAgentname());
			ivrCreation.setExecutive(tm.getExecutive());
			ivrCreation.setCustomer(tm.getCustomer());
			ivrCreation.setStatus(tm.getStatus());
			ivrCreation.setDuration(tm.getDuration());
			ivrCreation.setCallType(tm.getCallType());
			ivrCreation.setText_file(tm.getText_file());
			ivrCreation.setUrl(tm.getUrl());
			ivrCreation.setApikey(tm.getApikey());
			ivrCreation.setActive(true);

			Integer studetId = studentDetailsRepository.getStudentDetailsDataForIvr(tm.getCustomer());
			Integer proctorId = proctorStudentAssignmentRepository.getProctorId(studetId);
			String proctorMobile = empDetail_repo.getProctorNumber(proctorId);

			ivrCreation.setStudent_id(studetId);
			ivrCreation.setProctor_id(proctorId);
			ivrCreation.setExenumber(proctorMobile);

			IvrCreation savedIvrCreation = ivrCreationRepository.save(ivrCreation);

			return ResponseEntity.status(HttpStatus.OK).body(ivrCreation);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exception occurred: " + e.getMessage());
		}
	}


//	public String getCallOutbound(String exenumber, String custnumber) {
//		String apikey = "94d8cdd68770d1e00d6c252149beeb5f";
//		String url = "1";
//		String apiUrl = String.format(
//				"https://mcube.vmc.in/api/outboundcall?apikey=%s&exenumber=%s&custnumber=%s&url=%s", apikey, exenumber,
//				custnumber, url);
//
//		RestTemplate restTemplate = new RestTemplate();
//		try {
//			// Make the GET request to the external API
//			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class);
//
//			// Check the response status and return "success"
//			if (response.getStatusCode().is2xxSuccessful()) {
//				return "success";
//			} else {
//				return "failure";
//			}
//		} catch (Exception ex) {
//			// Log error and return "failure"
//			System.err.println("Error during API call: " + ex.getMessage());
//			return "failure";
//		}
//	}

	
	public Map<String, Object> getCallOutbound( String custnumber, Integer userId) {
		String apikey = "94d8cdd68770d1e00d6c252149beeb5f";
		String url = "1";
		
		Integer studetId = studentDetailsRepository.getStudentDetailsDataForIvr(custnumber);
//		Integer proctorId = proctorStudentAssignmentRepository.getProctorId(studetId);
//		String exenumber = empDetail_repo.getProctorNumber(proctorId);
		String exenumber = empDetail_repo.getExecutiveNumber(userId);
		
		String apiUrl = String.format(
				"https://mcube.vmc.in/api/outboundcall?apikey=%s&exenumber=%s&custnumber=%s&url=%s", apikey, exenumber,
				custnumber, url);

		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			
			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> apiResponse = objectMapper.readValue(response.getBody(),
						new TypeReference<Map<String, Object>>() {
						});

				responseMap.put("status", "success");
				responseMap.put("callid", apiResponse.get("callid"));
			} else {
				responseMap.put("status", "failure");
			}
		} catch (Exception ex) {
			System.err.println("Error during API call: " + ex.getMessage());
			responseMap.put("status", "failure");
		}
		return responseMap;
	}


	public List<Map<String, Object>> getIvrCreationData(Integer student_id) {
	    // Fetch the IVR creation data (multiple customers)
	    List<Map<String, Object>> ivrCreationData = ivrCreationRepository.getIvrCreationData(student_id);

	    // Fetch the student's mobile details (student, father, and mother mobile numbers)
	    Map<String, Object> mobileNumber = studentDetailsRepository.getMobileNumber(student_id);
	    String studentMobile = (String) mobileNumber.get("mobile");
	    String fatherMobile = (String) mobileNumber.get("father_mobile");
	    String motherMobile = (String) mobileNumber.get("mother_mobile");

	    // Use Java 8 stream to create a new list with updated maps
	    return ivrCreationData.stream()
	        .map(ivrData -> {
	            // Create a new map to avoid modifying the original (immutable) map
	            Map<String, Object> modifiedIvrData = new HashMap<>(ivrData);

	            // Retrieve the customer's mobile number from IVR data
	            String customerMobile = (String) ivrData.get("customer");
	            if (customerMobile != null) {
	                // Add the relationship based on matching the mobile numbers
	                if (customerMobile.equals(studentMobile)) {
	                    modifiedIvrData.put("relationship", "Student");
	                } else if (customerMobile.equals(fatherMobile)) {
	                    modifiedIvrData.put("relationship", "Father");
	                } else if (customerMobile.equals(motherMobile)) {
	                    modifiedIvrData.put("relationship", "Mother");
	                }
	            }

	            // Return the new map with the added relationship field
	            return modifiedIvrData;
	        })
	        .collect(Collectors.toList());  // Collect the modified maps into a new list
	}


	public IvrCreation updateIvrCreation(IvrCreationDto dto, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		IvrCreation ivrCreation = ivrCreationRepository.findById(dto.getIvr_creation_id())
		.orElseThrow(()-> new ResourceNotFoundException("Ivr not found"));
		
		ivrCreation.setSummarize(dto.getSummarize());
		ivrCreation.setIvr_creation_id(dto.getIvr_creation_id());
		ivrCreation.setModified_by(jwtDetails.getUserId());
		ivrCreation.setModified_username(jwtDetails.getUserName());
		
		return ivrCreationRepository.save(ivrCreation);
	}


}
