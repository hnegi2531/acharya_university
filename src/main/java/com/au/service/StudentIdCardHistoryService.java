package com.au.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.StudentIdCardHistoryDto;
import com.au.model.StudentIdCardHistory;
import com.au.model.Student_Details;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentIdCardHistoryRepository;
import com.au.repository.UserRoleRepository;
import com.au.response.ResponseHandler;

@Service
public class StudentIdCardHistoryService {
	
	@Autowired
	private StudentIdCardHistoryRepository studentIdCardHistoryRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private JwtTokenService jwtService;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	private static final Integer SUPER_ADMIN_ROLE_ID=1;
	
	public ResponseEntity<Object> studentIdCardCreationWithHistory(
			List<StudentIdCardHistoryDto> studentIdCardHistoryDto, String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);

			studentIdCardHistoryDto.parallelStream().forEachOrdered(sidhdto -> {
				if (studentDetailsRepository.getIdCardIssuedYear(sidhdto.getStudentId()) != sidhdto.getCurrentYear()) {
					StudentIdCardHistory sich = modelMapper.map(sidhdto, StudentIdCardHistory.class);
					Optional<Student_Details> studentDetail = studentDetailsRepository.findById(sidhdto.getStudentId());
					sich.setValid_till(sidhdto.getValidTill());
					sich.setCurrent_year(sidhdto.getCurrentYear());
					sich.setCreated_by(jwtDetails.getUserId());
					sich.setCreated_username(jwtDetails.getUserName());
					sich.setStudent_id(studentDetail.get());
					studentIdCardHistoryRepository.save(sich);
					studentDetailsRepository.updateIdCardIssuedYear(sidhdto.getStudentId(), sidhdto.getCurrentYear());
				}
			});
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Created Successfully");
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}
	
	public ResponseEntity<Object> studentDuplicateIdCardCreationWithHistory(
			List<StudentIdCardHistoryDto> studentIdCardHistoryDto, String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			Integer adminRoleId=userRoleRepository.getRoleIdByUserId(jwtDetails.getUserId());
			studentIdCardHistoryDto.parallelStream().forEachOrdered(sidhdto -> {
				if (sidhdto.getReceiptNo() != null && sidhdto.getReceiptDate() != null) {
					StudentIdCardHistory sich = modelMapper.map(sidhdto, StudentIdCardHistory.class);
					Optional<Student_Details> studentDetail = studentDetailsRepository.findById(sidhdto.getStudentId());
					sich.setValid_till(sidhdto.getValidTill());
					sich.setCurrent_year(sidhdto.getCurrentYear());
					sich.setCreated_by(jwtDetails.getUserId());
					sich.setCreated_username(jwtDetails.getUserName());
					sich.setStudent_id(studentDetail.get());
					sich.setReceipt_no(sidhdto.getReceiptNo());
					sich.setReceipt_date(sidhdto.getReceiptDate());
					studentIdCardHistoryRepository.save(sich);
				} else if(adminRoleId==SUPER_ADMIN_ROLE_ID) {
					StudentIdCardHistory sich = modelMapper.map(sidhdto, StudentIdCardHistory.class);
					Optional<Student_Details> studentDetail = studentDetailsRepository.findById(sidhdto.getStudentId());
					sich.setValid_till(sidhdto.getValidTill());
					sich.setCurrent_year(sidhdto.getCurrentYear());
					sich.setCreated_by(jwtDetails.getUserId());
					sich.setCreated_username(jwtDetails.getUserName());
					sich.setStudent_id(studentDetail.get());
					sich.setReceipt_no(sidhdto.getReceiptNo());
					sich.setReceipt_date(sidhdto.getReceiptDate());
					studentIdCardHistoryRepository.save(sich);
				}
			});
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Created Successfully");
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}
	
	public List<HashMap<String, Object>> studentIdCardHistoryDetails() {
		List<HashMap<String,Object>> emp= studentIdCardHistoryRepository.studentIdCardHistoryDetails();
		List<HashMap<String, Object>> response = emp.stream()
                .collect(Collectors.groupingBy(
                        e -> e.get("student_id"),Collectors.groupingBy(
                                e -> e.get("history_current_year"),
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(e -> (int) e.get("student_id_card_history_id"))),
                                Optional::get
                        )
                ))) 
                .values().stream() 
                .flatMap(map -> map.values().stream()) 
                .map(HashMap::new) 
                .collect(Collectors.toList());

		return response;
	}
}
