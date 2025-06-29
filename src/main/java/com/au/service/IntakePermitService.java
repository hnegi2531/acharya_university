package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.model.IntakePermit;
import com.au.repository.IntakePermitRepository;
import com.au.response.ResponseHandler;

@Service
public class IntakePermitService {
	
	@Autowired
	private IntakePermitRepository intake_permit_repo;
	
	
	
	public List<IntakePermit>copiedIntakePermitDetails(List<IntakePermit> ipl) {
		return intake_permit_repo.saveAll(ipl);
		
	}
	
	public List<IntakePermit> listAll() {
		return intake_permit_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = intake_permit_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = intake_permit_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public List<HashMap<String, Object>> getIntakePermitByIntakeId(Integer intake_id) {
		List<HashMap<String, Object>> intake_permit_list = intake_permit_repo.getIntakePermitByIntakeId(intake_id);
		return intake_permit_list;
	}
	

	public IntakePermit saveIntake(@Valid IntakePermit ip) {
		return intake_permit_repo.save(ip);
	}

	public List<IntakePermit> updateIntakePermit(List<IntakePermit> ip) {
		return intake_permit_repo.saveAll(ip);
		

	}
	
	public Map<Object, List<HashMap<String, Object>>> getIntakePermitDetailsForGridView(List<Integer> intakeIds) {
		List<HashMap<String, Object>> intakePermitList = intake_permit_repo.getIntakePermitDetailsForGridView(intakeIds);
//		 Map<Object, List<HashMap<String, Object>>> intakePermitDetails = intakePermitList.stream()
//		            .collect(Collectors.groupingBy(e -> e.get("intake_id"))) // Group by intake_id
//		            .entrySet().stream()
//		            .collect(Collectors.toMap(
//		                Map.Entry::getKey,
//		                entry -> {
//		                    List<HashMap<String, Object>> dataList = entry.getValue();
//		                    return IntStream.range(0, 
//		                        Math.max(dataList.size(), intakePermitList.stream().collect(Collectors.groupingBy(e -> e.get("intake_id"))).values().stream().mapToInt(List::size).max().orElse(0)))
//		                        .mapToObj(i -> i < dataList.size() ? dataList.get(i) : new HashMap<>()) // Fill with empty HashMap
//		                        .collect(Collectors.toList());
//		                }
//		            ));

        
        Map<Object, List<HashMap<String, Object>>> intakePermitDetails = intakePermitList.stream()
            .collect(Collectors.groupingBy(e -> e.get("intake_id")));

        int maxSize = intakePermitDetails.values().stream()
            .mapToInt(List::size)
            .max()
            .orElse(0);
        Map<Object, List<HashMap<String, Object>>> resultMap = new HashMap<>();
        intakePermitDetails.forEach((intakeId, dataList) -> {
            List<HashMap<String, Object>> permitData = new ArrayList<>(dataList);
            while (permitData.size() < maxSize) {
            	permitData.add(new HashMap<>());
            }
            resultMap.put(intakeId, permitData);
        });
		return resultMap;
	}
}
