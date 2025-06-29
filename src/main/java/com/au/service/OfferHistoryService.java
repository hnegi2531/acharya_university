package com.au.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.dto.OfferHistoryResponse;
import com.au.model.OfferHistory;
import com.au.model.OfferHistoryRequest;
import com.au.repository.OfferHistoryRepository;

@Service
public class OfferHistoryService {
	
	@Autowired
	private OfferHistoryRepository offerHistoryRepository;
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	
	public OfferHistory saveOfferHistory(OfferHistory offerHistoryRequest) {
//		OfferHistory offerHistory=modelMapper.map(offerHistoryRequest,OfferHistory.class);
//		OfferHistoryResponse offerHistoryResponse=modelMapper.map(offerHistory,OfferHistoryResponse.class);
		return offerHistoryRepository.save(offerHistoryRequest);
		 
		
	}
	
	public List<OfferHistory>  offerHistoryByJobId(Integer job_id){
		List<OfferHistory> offerHistoryDetails=offerHistoryRepository.offerHistoryByJobId(job_id);
//		List<OfferHistoryResponse> offerHistoryResponses=modelMapper.map(offerHistoryDetails,List.class);
		return offerHistoryDetails;
	}
	
	
	public List<OfferHistory>  offerHistoryByEmployeeId(Integer employeeId){
		List<OfferHistory> offerHistoryDetails=offerHistoryRepository.offerHistoryByEmployeeId(employeeId);
//		List<OfferHistoryResponse> offerHistoryResponses=modelMapper.map(offerHistoryDetails,List.class);
		return offerHistoryDetails;
	}


}
