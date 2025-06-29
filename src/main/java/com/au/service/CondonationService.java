package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.model.Condonation.Status;
import com.au.dto.CondonationDTO;
import com.au.dto.CondonationListDTO;
import com.au.model.Condonation;
import com.au.repository.CondonationRepository;
import com.au.response.ResponseHandler;


@Service
public class CondonationService {

	@Autowired
	private CondonationRepository condotionRepository;
	
	
	public ResponseEntity<Object> saveCondonation(CondonationDTO condonationDTO) {
	try {	
		Condonation condonation=new Condonation();
		condonation.setAdditionClass(condonationDTO.getAdditionClass());
		condonation.setCondonationType(condonationDTO.getCondonationType());
		condonation.setCourseId(condonationDTO.getCourseId());
		condonation.setPercentage(condonationDTO.getPercentage());
		condonation.setRemarks(condonationDTO.getRemarks());
		condonation.setStudentId(condonationDTO.getStudentId());
		condonation.setTotalClass(condonationDTO.getTotalClass());
		condonation.setTotalClassTaken(condonationDTO.getTotalClassTaken());
		condonation.setStatus(Status.PENDING);
		condotionRepository.save(condonation);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		
	}catch (Exception e) {
		return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
				e.getMessage());
	}
	}


	public ResponseEntity<Object> getCondonationList() {
		try {	
			
		List<CondonationListDTO> condonationListDTOs	 =condotionRepository.getAllCondonation();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", condonationListDTOs);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
					e.getMessage());
		}
	}


	public ResponseEntity<Object> selectCondonationForPrincipalScreen(CondonationDTO condonationDTO) {
		try {	
			
			Condonation condonation	=condotionRepository.findByCondonationId(condonationDTO.getCondonationId());
			condonation.setPrincpalStatus(Boolean.TRUE);
			condotionRepository.save(condonation);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
				
			}catch (Exception e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
						e.getMessage());
			}
	}


	public ResponseEntity<Object> getCondonationListForPrincipalScreen(Long condonationId) {
		try {	
			
			List<CondonationListDTO> condonationListDTOs	 =condotionRepository.getCondonationListForPrincipalScreen(condonationId);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", condonationListDTOs);
				
			}catch (Exception e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
						e.getMessage());
			}
	}


	public ResponseEntity<Object> approveCondonationListForPrincipalScreen(CondonationDTO condonationDTO) {
try {	
			
			Condonation condonation	=condotionRepository.findByCondonationId(condonationDTO.getCondonationId());
			condonation.setStatus(Status.APPROVED);
			condotionRepository.save(condonation);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
				
			}catch (Exception e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
						e.getMessage());
			}
	}

}

