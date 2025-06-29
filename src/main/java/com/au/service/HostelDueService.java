package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.model.HostelDue;
import com.au.repository.HostelDueRepository;
import com.au.response.ResponseHandler;



@Service
public class HostelDueService {

	@Autowired
	private HostelDueRepository hostelDueRepository;
	
	@Autowired
	private HostelBedAssignmentService hostelBedAssignmentService;
	
	
	public ResponseEntity<Object> getAllHostelDue() {
		 List<HostelDue> hostelDues= hostelDueRepository.findAll11();
		 return ResponseHandler.generateResponse(true, HttpStatus.OK, hostelDues);
	}


	public ResponseEntity<Object> getHostelDueReportByAcademicYearGroupedByBlock(Integer acYearId) {
		System.out.println("acYearId acYearId acYearId acYearId 1"+acYearId);
		List<HashMap<String,Object>> hostelBedAssignments=hostelBedAssignmentService.getHostelDueReportByAcademicYearGroupedByBlock(acYearId);
		
		Map<String, Map<String, Object>> reportData = hostelBedAssignments.stream()
	            .collect(Collectors.groupingBy(
	                e -> (String) e.get("blockName"),
	                Collectors.collectingAndThen(
	                    Collectors.toList(),
	                    list -> {
	                    
	                        double totalDueAmount = list.stream()
	                            .mapToDouble(e -> (Double) e.get("due"))
	                            .sum();
	                       
	                        Integer totalAmount = list.stream()
	                            .mapToInt(e -> (Integer) e.get("totalAmount"))
	                            .sum();
	                        
	                        double totalPaidAmount = list.stream()
	                            .mapToDouble(e -> (Double) e.get("paid"))
	                            .sum();
	                        
//	                    	Integer waiverAmount = list.stream()
//									.mapToInt(e -> (Integer) e.get("waiverAmount"))
//									.sum();

	                        int waiverAmount = list.stream()
	                                .mapToInt(e -> ((Number) e.get("waiverAmount")).intValue()) // Summing up all waiver amounts
	                                .sum();
	                        
	                        Map<String, Object> blockData = new HashMap<>();
	                        blockData.put("totalDueAmount", totalDueAmount);
	                        blockData.put("totalAmount", totalAmount);
	                        blockData.put("totalPaidAmount", totalPaidAmount);
	                        blockData.put("hostelBedId", list.get(0).get("hostelBlockId"));
	                        blockData.put("waiverAmount", waiverAmount);
//							blockData.put("waiverAmount", list.get(0).get("waiverAmount"));
//	                        blockData.put("objects", list);

	                        return blockData;
	                    }
	                )
	            ));
		return ResponseHandler.generateResponse(true, HttpStatus.OK, reportData);
	}


	public ResponseEntity<Object> studentHostelDue(Integer studentId) {
		List<HashMap<String, Object>> dueDetails=hostelDueRepository.hostelDueByStudentId(studentId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, dueDetails);
	}
}
