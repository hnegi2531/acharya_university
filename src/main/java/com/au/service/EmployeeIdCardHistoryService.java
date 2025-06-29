package com.au.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.controller.EmployeeDetailsController;
import com.au.dto.EmployeeIdCardHistoryDetailsDto;
import com.au.dto.EmployeeIdCardHistoryDto;
import com.au.dto.JwtDetails;
import com.au.dto.StudentIdCardHistoryDto;
import com.au.model.EmployeeDetails;
import com.au.model.EmployeeIdCardHistory;
import com.au.model.StudentIdCardHistory;
import com.au.model.Student_Details;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.EmployeeIdCardHistoryRepository;
import com.au.repository.UserRoleRepository;
import com.au.response.ResponseHandler;

@Service
public class EmployeeIdCardHistoryService {
	
	
	Logger log = LoggerFactory.getLogger(EmployeeIdCardHistoryService.class);
	
	@Autowired
	private EmployeeIdCardHistoryRepository employeeIdCardHistoryRepository;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	private JwtTokenService jwtService;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	private static final Integer SUPER_ADMIN_ROLE_ID=1;
	
	public ResponseEntity<Object> employeeIdCardCreationWithHistory(List<EmployeeIdCardHistoryDto> employeeIdCardHistoryDto,String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);

			employeeIdCardHistoryDto.parallelStream().forEachOrdered(eidhdto -> {
				Optional<EmployeeDetails> employeeDetail = employeeDetailsRepository.findById(eidhdto.getEmpId());
				System.out.println(" ffffffffffffffffffff "+ (employeeIdCardHistoryRepository.countByEmpIdAndActiveTrue(employeeDetail.get()) >= 1));
				if (!(employeeIdCardHistoryRepository.countByEmpIdAndActiveTrue(employeeDetail.get()) >= 1)) {
					EmployeeIdCardHistory eich = modelMapper.map(eidhdto, EmployeeIdCardHistory.class);
					eich.setIssuedBy(jwtDetails.getUserId());
					eich.setIssuedByUsername(jwtDetails.getUserName());
					eich.setEmpId(employeeDetail.get());
					employeeIdCardHistoryRepository.save(eich);
				}
			});
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Created Successfully");
		} catch (Exception e) {
			log.info("Employee ID Card History Creation Error :"+  e.getMessage());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}
	
	public ResponseEntity<Object> employeeDuplicateIdCardCreationWithHistory(List<EmployeeIdCardHistoryDto> employeeIdCardHistoryDto,String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			Integer adminRoleId=userRoleRepository.getRoleIdByUserId(jwtDetails.getUserId());
			employeeIdCardHistoryDto.parallelStream().forEachOrdered(eidhdto -> {
				if (eidhdto.getReceiptNo() != null && eidhdto.getReceiptDate() != null) {
					EmployeeDetails employeeDetail = employeeDetailsRepository.findById(eidhdto.getEmpId()).orElseThrow(() ->  new RuntimeException("Employee Not Found:"+eidhdto.getEmpId()));
					EmployeeIdCardHistory eich=employeeIdCardHistoryRepository.findByEmpId(employeeDetail);
					eich.setModifiedBy(jwtDetails.getUserId());
					eich.setModifiedUsername(jwtDetails.getUserName());
					eich.setEmpId(employeeDetail);
					eich.setReceiptDate(eidhdto.getReceiptDate());
					eich.setReceiptNo(eidhdto.getReceiptNo());
					eich.setRemarks(eidhdto.getRemarks());
					employeeIdCardHistoryRepository.save(eich);
				} else if(adminRoleId==SUPER_ADMIN_ROLE_ID) {
					EmployeeDetails employeeDetail = employeeDetailsRepository.findById(eidhdto.getEmpId()).orElseThrow(() ->  new RuntimeException("Employee Not Found:"+eidhdto.getEmpId()));
					EmployeeIdCardHistory eich=employeeIdCardHistoryRepository.findByEmpId(employeeDetail);
					eich.setModifiedBy(jwtDetails.getUserId());
					eich.setModifiedUsername(jwtDetails.getUserName());
					eich.setEmpId(employeeDetail);
					eich.setReceiptDate(eidhdto.getReceiptDate());
					eich.setReceiptNo(eidhdto.getReceiptNo());
					eich.setRemarks(eidhdto.getRemarks());
					employeeIdCardHistoryRepository.save(eich);
				}
			});
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Created Successfully");
		} catch (Exception e) {
			log.info("Employee ID Card Duplicate History Creation Error :"+  e.getMessage());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}

	public List<EmployeeIdCardHistoryDetailsDto> employeeIdCardHistoryDetails() {
//		EmployeeDetails employeeDetail = employeeDetailsRepository.findById(empId).orElseThrow(() -> new RuntimeException("Employee Not Found :"+empId));
		return employeeIdCardHistoryRepository.employeeIdCardHistoryDetails();
	}

}
