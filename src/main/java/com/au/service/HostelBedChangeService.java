package com.au.service;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.HostelBedChangeDto;
import com.au.dto.JwtDetails;
import com.au.model.HostelBedAssignment;
import com.au.model.HostelBedChange;
import com.au.repository.HostelBedAssignmentRepository;
import com.au.repository.HostelBedChangeRepository;
import com.au.repository.HostelBedsRepository;
import com.au.repository.HostelBlocksRepository;
import com.au.repository.HostelFeeTemplateRepository;
import com.au.repository.HostelRoomsRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelBedChangeService {
	
	Logger log=LoggerFactory.getLogger(HostelBedChangeService.class);
	
	@Autowired
	private HostelBedChangeRepository hostelBedChangeRepository;
	
	@Autowired
	private HostelBlocksRepository hostelBlocksRepository;
	
	@Autowired
	private HostelRoomsRepository hostelRoomsRepository;
	
	@Autowired
	private HostelBedsRepository hostelBedsRepository;
	
	@Autowired
	private HostelFeeTemplateRepository hostelFeeTemplateRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private HostelBedAssignmentRepository hostelBedAssignmentRepository;
	
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private JwtTokenService jwtService;
	
	private final ModelMapper modelMapper = new ModelMapper();

	public ResponseEntity<Object> hostelBedChange(@Valid HostelBedChangeDto hostelBedChangeDto, String jwtToken) {

		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);

			HostelBedChange hostelBedChange = HostelBedChange.builder().active(hostelBedChangeDto.getActive())
					.approveStatus(hostelBedChangeDto.getApproveStatus()).createdBy(jwtDetails.getUserId())
					.createdUsername(jwtDetails.getUserName())
					.hoselBedAssignment(
							hostelBedAssignmentRepository.findById(hostelBedChangeDto.getHoselBedAssignmentId()).get())
					.hostelBed(hostelBedsRepository.findById(hostelBedChangeDto.getHostelBedId()).get())
					.hostelBlock(hostelBlocksRepository.findById(hostelBedChangeDto.getHostelBlockId()).get())
					.hostelFeeTemplate(
							hostelFeeTemplateRepository.findById(hostelBedChangeDto.getHostelFeeTemplateId()).get())
					.hostelRoom(hostelRoomsRepository.findById(hostelBedChangeDto.getHostelRoomId()).get())
					.student(studentDetailsRepository.findById(hostelBedChangeDto.getStudentId()).get())
					.acYear(hostelBedAssignmentRepository.findById(hostelBedChangeDto.getHoselBedAssignmentId()).get().getAcYear()).build();
			hostelBedChangeRepository.save(hostelBedChange);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Created Successfully");
		} catch (IOException e) {

			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public ResponseEntity<Object> approvalOfBedChange(@Valid HostelBedChangeDto hostelBedChangeDto,
			Integer hostelBedChangeId, String jwtToken) {
		try {
			Optional<HostelBedChange> hosetBedChange = hostelBedChangeRepository.findById(hostelBedChangeId);

			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);

			if (hosetBedChange.isPresent()) {
				hosetBedChange.get().setApproveStatus(hostelBedChangeDto.getApproveStatus());
				hosetBedChange.get()
						.setApprover(userAuthenticationRepository.findById(hostelBedChangeDto.getApproverId()).get());
				hosetBedChange.get().setPreviousHostelFeeTemplate(
						hosetBedChange.get().getHoselBedAssignment().getHostelFeeTemplate());
				hosetBedChange.get().setPreviousHostelBed(hosetBedChange.get().getHoselBedAssignment().getHostelBed());
				hosetBedChange.get()
						.setPreviousHostelRoom(hosetBedChange.get().getHoselBedAssignment().getHostelRoom());
				hosetBedChange.get().setModifiedBy(jwtDetails.getUserId());
				hosetBedChange.get().setModifiedUsername(jwtDetails.getUserName());
				hostelBedChangeRepository.save(hosetBedChange.get());
				HostelBedAssignment hostelBedAssignmentUpdation = modelMapper
						.map(hosetBedChange.get().getHoselBedAssignment(), HostelBedAssignment.class);
				hostelBedAssignmentUpdation.setHostelBlock(hosetBedChange.get().getHostelBlock());
				hostelBedAssignmentUpdation.setHostelBed(hosetBedChange.get().getHostelBed());
				hostelBedAssignmentUpdation.setHostelRoom(hosetBedChange.get().getHostelRoom());
				hostelBedAssignmentUpdation.setHostelFeeTemplate(hosetBedChange.get().getHostelFeeTemplate());
				hostelBedAssignmentUpdation.setModifiedBy(jwtDetails.getUserId());
				hostelBedAssignmentUpdation.setModifiedUsername(jwtDetails.getUserName());
				hostelBedAssignmentRepository.save(hostelBedAssignmentUpdation);
				return ResponseHandler.generateResponse(true, HttpStatus.OK,
						"Hostel Bed Change Request Approved Successfully!!");
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,
						"Hostel Bed Change Request Not Found!!");
			}
		} catch (IOException e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}
	
	public ResponseEntity<Object> filteredAndSortedResponses(Pageable pageable, Object keyword) {
		Page<Object> filterAndSortedResponse = hostelBedChangeRepository.filteredAndSortedResponses(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, filterAndSortedResponse);
	}

	public ResponseEntity<Object> sortedResponses(Pageable pageable1) {
		Page<Object> sortedResponse = hostelBedChangeRepository.sortedResponses(pageable1);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sortedResponse);
	}

}
