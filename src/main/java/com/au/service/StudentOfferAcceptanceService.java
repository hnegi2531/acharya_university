package com.au.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.dto.StudentOfferAcceptanceRequest;
import com.au.model.StudentOfferAcceptance;
import com.au.repository.CandidateWalkinRepository;
import com.au.repository.StudentOfferAcceptanceRepository;



@Service
public class StudentOfferAcceptanceService {
	
	Logger log = LoggerFactory.getLogger(StudentOfferAcceptanceService.class);
	
	@Autowired
	private StudentOfferAcceptanceRepository studentOfferAcceptanceRepository;
	
	@Autowired
	private CandidateWalkinRepository candidateWalkinRepository;
	
	private ModelMapper modelMapper=new ModelMapper();
	
	@Autowired
	private CandidateWalkInService candidateWalkInService;
	
	public StudentOfferAcceptance saveStudentOfferAcceptance(StudentOfferAcceptanceRequest soar) {
		
		StudentOfferAcceptance soa=modelMapper.map(soar,StudentOfferAcceptance.class);
		soa = studentOfferAcceptanceRepository.save(soa);
		candidateWalkinRepository.updateNpfStatusOfAcceptingOffer(soar.getCandidate_id());
		candidateWalkInService.updateLsqStatus(soar.getCandidate_id(),4);
		
		return soa;
	}
	
	public List<StudentOfferAcceptance> activeStudentOfferAcceptanceDetails() {
		
		return studentOfferAcceptanceRepository.activeStudentOfferAcceptanceDetails();
	}

}
