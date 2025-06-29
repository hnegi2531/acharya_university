package com.au.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.ServiceTicketDto;
import com.au.dto.ServiceTypeDeptTag;
import com.au.dto.ServiceTypeDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Department;
import com.au.model.Designation;
import com.au.model.ServiceType;
import com.au.model.ServiceTypeDeptTags;
import com.au.model.ServiceTypeTicket;
import com.au.repository.ServiceTicketRepo;
import com.au.repository.ServiceTypeDeptTagRepo;
import com.au.repository.ServiceTypeRepo;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ServiceTypeService {
	
	@Autowired
	private ServiceTypeRepo serviceRepo;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private ServiceTypeDeptTagRepo serviceTypeDeptTagRepo;

	@Autowired
	private ServiceTicketRepo ticketRepo;

	public ServiceType saveServiceType(ServiceType request, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		ServiceType checkIfExist = serviceRepo.findByServiceTypeName(request.getServiceTypeName());
		if (checkIfExist != null) {
			throw new ResourceNotFoundException("Service type with this name already exists");
		}
		ServiceType checkIfShortNameExist = serviceRepo.findByServiceTypeShortName(request.getServiceTypeShortName());
		if (checkIfShortNameExist != null) {
			throw new ResourceNotFoundException("Service type short name already exists");
		}
		ServiceType entity = new ServiceType();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		entity.setCreated_username(jwtDetails.getUserName());
		entity.setCreated_by(jwtDetails.getUserId());
		entity.setActive(true);
		entity.setServiceTypeName(request.getServiceTypeName());
		entity.setServiceTypeShortName(request.getServiceTypeShortName());
		entity.setShowInEvent(request.getShowInEvent());
		entity.setHostelStatus(request.getHostelStatus());
		entity.setDept_id(request.getDept_id());
		entity.setIs_attachment(request.getIs_attachment());
		return serviceRepo.save(entity);

	}

	public ServiceType updateServiceType(Long id, @Valid ServiceTypeDto request, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		ServiceType entity = serviceRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found"));
		
	
			entity.setModified_username(jwtDetails.getUserName());
			entity.setModified_by(jwtDetails.getUserId());
			entity.setActive(true);
			entity.setDept_id(request.getDept_id());
			return serviceRepo.save(entity);

	}


	public void deleteServiceType(Long id, Boolean active) {
		serviceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service type " + id + " not found"));
		serviceRepo.update(active, id);
	}

	public List<ServiceTypeDeptTags> assignDeptTagToService(@Valid ServiceTypeDeptTag dto, JwtDetails jwtDetails2)
			throws JsonParseException, JsonMappingException, IOException {
		List<ServiceTypeDeptTags> 	tags = new ArrayList<ServiceTypeDeptTags>();
		if(serviceTypeDeptTagRepo.findByAcYearSchoolId(dto.getServiceTypeId(),dto.getDepttagId()) >=1){
			throw new IllegalArgumentException("Service Type already assigned to Department tag id ");
		}else {
			dto.getDepttagId().stream().forEach(st ->{
				ServiceTypeDeptTags stt= new ServiceTypeDeptTags();	
				stt.setCreated_username(jwtDetails2.getUserName());
				stt.setCreated_by(jwtDetails2.getUserId());
				stt.setActive(true);
				stt.setServiceTypeId(dto.getServiceTypeId());
				stt.setDeptTagId(st);
				serviceTypeDeptTagRepo.save(stt);
				tags.add(stt);
		});
	
	}	
		return tags;
	}
	

	public ResponseEntity<Object> getAllServiceDept(Pageable pageable, Object keyword) {
		if (keyword != null) {
			Page<Object> listWithsearch = serviceTypeDeptTagRepo.findAllServiceDept1(pageable, keyword);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, listWithsearch);
		} else {
			Page<Object> listWithOutsearch = serviceTypeDeptTagRepo.findAllServiceDept2(pageable);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, listWithOutsearch);
		}
	}

	public void deleteServiceTypeDeptTag(Long id, Boolean active) {
		serviceTypeDeptTagRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Service type department tag " + id + " not found"));
		serviceTypeDeptTagRepo.update(active, id);
	}

	public List<Map<String, Object>> getAllServiceByDeptTag(Integer dept_id) {
		return serviceRepo.getAllByDeptId(dept_id);
	
	}

	public ServiceTypeTicket saveServiceTicket(ServiceTicketDto dto, JwtDetails jwtDetails) {
//		ServiceTypeDeptTags nullCeck = serviceTypeDeptTagRepo.findByDeptTagIdAndServiceTypeId(dto.getTagId(),dto.getServiceTypeId());
//		if(nullCeck==null) {
//			
//		}
		ServiceTypeTicket entity = new ServiceTypeTicket();
		entity.setServiceTypeId(dto.getServiceTypeId());
		entity.setTagId(dto.getTagId());
		entity.setComplaintDetails(dto.getComplaintDetails());
		entity.setFloorAndExtension(dto.getFloorAndExtension());
		entity.setBlockId(dto.getBlockId());
		entity.setUserId(dto.getUserId());
		entity.setActive(dto.getActive());
		entity.setAttendedBy(dto.getAttendedBy());
		entity.setTicketStatus(dto.getTicketStatus());
		entity.setCreated_by(jwtDetails.getUserId());
		entity.setCreated_username(jwtDetails.getUserName());
		entity.setModified_by(dto.getModified_by());
		entity.setModified_username(dto.getModified_username());
		ticketRepo.save(entity);
		return entity;
	}

	public ResponseEntity<Object> getAllTickectsRaised(Pageable pageable, Object keyword, String fromDate,
			String toDate) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

			Date dateTimeFrom = dateTimeFormat.parse(fromDate);
			Date dateTimeTo = dateTimeFormat.parse(toDate);
			if (keyword != null) {
				Page<Object> listWithsearch = ticketRepo.findAll1(pageable, keyword, dateTimeFrom,
						dateTimeTo);
				return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, listWithsearch);
			} else {
				Page<Object> listWithOutsearch = ticketRepo.findAll2(pageable, dateTimeFrom,
						dateTimeTo);
				return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, listWithOutsearch);
			}
	
	}

	public ServiceType get(Long id) {
		return serviceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service Type Not Found:" + id));
	}

	public List<ServiceType> getAllActiveServiceType() {
		return serviceRepo.getAllActiveServiceType();
	}
	
	public List<ServiceType> getAllActiveServiceTypeOnlyevent() {
		return serviceRepo.getAllActiveServiceTypeOnlyevent();
	}
	

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> employee_filtered_response = serviceRepo.findAll1(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
	}

	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> employee_sorted_response = serviceRepo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_sorted_response);
	}
	
	public List<Map<String, Object>> getAllServiceByUserId(Integer user_id) {
		return serviceRepo.getAllServiceByUserId(user_id);
		 
	}

	public List<Map<String, Object>> getAllServiceTypeById(Long id) {
		return serviceRepo.getAllServiceTypeById(id);
	}
	
	
	
	public ServiceType updateServiceType(ServiceType st) {
		return serviceRepo.save(st);
	}

			
}
