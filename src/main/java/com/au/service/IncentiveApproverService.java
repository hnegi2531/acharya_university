package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;

import com.au.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.IncentiveApprover;
import com.au.model.Publications;
import com.au.response.ResponseHandler;


@Service
public class IncentiveApproverService {

	@Autowired
	private IncentiveApproverRepository ApproverRepository;
	
	@Autowired
	private UserAuthenticationRepository userRepo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;

	@Autowired
	private PublicationsRepository publicationsRepository;

	@Autowired
	private ConferencesRepository conferencesRepository;

	@Autowired
	private BookChapterRepository bookChapterRepository;

	@Autowired
	private MembershipRepository membershipRepository;

	@Autowired
	private GrantsRepository grantsRepository;

	@Autowired
	private PatentRepository patentRepository;

	@Autowired
	private EntityManager entityManager;
	
	public List<IncentiveApprover> saveIncentiveApprover(@Valid List<IncentiveApprover> ademail) {
		return	ApproverRepository.saveAll(ademail);
	}

	public List<Map<String, Object>> getApproverDetailsData(Integer emp_id) {
	List<Integer> userIds =	userRepo.getUserIds();
	
	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	
	List<Map<String, Object>> data1 =  userRepo.getEmployeeData1(userIds);
	Map<String, Object> data2 = empDetail_repo.getEmployeeData2(emp_id);
	Map<String, Object> data3 = empDetail_repo.getEmployeeData3(emp_id);
	data.add(data2);
	data.add(data3);
	data.addAll(data1);
	return data;
	}
	
	
	public List<Map<String, Object>> getApproverDetailsDataForPatent(Integer emp_id) {
	List<Integer> userIds =	userRepo.getUserIdsPatent();
	
	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	
	List<Map<String, Object>> data1 =  userRepo.getEmployeeData1(userIds);
	Map<String, Object> data2 = empDetail_repo.getEmployeeData2(emp_id);
	Map<String, Object> data3 = empDetail_repo.getEmployeeData3(emp_id);
	data.add(data2);
	data.add(data3);
	data.addAll(data1);
	return data;
	}

	public List<IncentiveApprover> getAllActiveIncentiveApprover() {
		return ApproverRepository.getAllActiveIncentiveApprover();
	}


	public IncentiveApprover get(Integer id) {
	return ApproverRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("IncentiveApprover Not Found:" + id));
	}
	
	
	public IncentiveApprover updateIncentiveApprover(IncentiveApprover ademail) {
		return ApproverRepository.save(ademail);
	}


	public void deactivate(Integer id) {
		IncentiveApprover ademail = ApproverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("IncentiveApprover Not Found:" + id));
		ApproverRepository.deactivate(id);
	}
	
	public void activate(Integer id) {
		IncentiveApprover ademail = ApproverRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("IncentiveApprover Not Found:" + id));
		ApproverRepository.activate(id);
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable,Integer empId, Object keyword) {
		Page<Object> oc_filtered_response = ApproverRepository.getAllDataFilteredByKeyword(pageable,empId, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable,Integer empId) {
		Page<Object> oc_sorted_response = ApproverRepository.getAllSortedData(pageable,empId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}

	public List<Map<String, Object>> incentiveApproverBasedOnEmpId(Integer emp_id, Integer incentive_approver_id) {
		return ApproverRepository.incentiveApproverBasedOnEmpId(emp_id,incentive_approver_id);
	}

	public String checkIncentiveApprover(Integer id) { 

        IncentiveApprover approver = ApproverRepository.findByIncentiveApproverId(id)
                .orElseThrow(() -> new RuntimeException("Incentive Approver not found"));

        if (approver.getAsst_dir_remark() == null ||
            approver.getHod_remark() == null || approver.getHoi_remark() == null ||
            approver.getHr_remark() == null || approver.getQa_remark() == null) {
            throw new RuntimeException("One or more remarks are null, unable to proceed!!!");
        }
        return "All remarks are present, able to proceed!!!";
    }

	public List<Map<String, Object>> checkIncentiveApproverRemarks(Integer id) {
		IncentiveApprover incentive = ApproverRepository.findByIncentiveApproverId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incentive not found"));

        List<Map<String, Object>> response = new ArrayList<>();

        // Prepare the structured response
        response.add(createDetailMap("Emp_id", incentive.getHoi_id(), incentive.getHoi_remark(), incentive.getHoi_date(),incentive.getAmount(),incentive.getIp_address()));
        response.add(createDetailMap("Emp_id", incentive.getHod_id(), incentive.getHod_remark(), incentive.getHod_date(),incentive.getAmount(),incentive.getIp_address()));
        response.add(createDetailMap("Emp_id", incentive.getAsst_dir_id(), incentive.getAsst_dir_remark(), incentive.getAsst_dir_date(),incentive.getAmount(),incentive.getIp_address()));
        response.add(createDetailMap("Emp_id", incentive.getHr_id(), incentive.getHr_remark(), incentive.getHr_date(),incentive.getAmount(),incentive.getIp_address()));
        response.add(createDetailMap("Emp_id", incentive.getQa_id(), incentive.getQa_remark(), incentive.getQa_date(),incentive.getAmount(),incentive.getIp_address()));
        response.add(createDetailMap("Emp_id", incentive.getFinance_id(), incentive.getFinance_remark(), incentive.getFinance_date(),incentive.getAmount(),incentive.getIp_address()));
        response.add(createDetailMap("Emp_id", incentive.getIpr_id(), incentive.getIpr_remark(), incentive.getIpr_date(),incentive.getAmount(),incentive.getIp_address()));
        response.add(createDetailMap("Emp_id", incentive.getEmp_id(), incentive.getRemark(), incentive.getDate(),incentive.getAmount(),incentive.getIp_address()));

        return response;
    }

    private Map<String, Object> createDetailMap(String Emp_id, Integer idValue, String remark, String date , String amount , String ip_address) {
        Map<String, Object> detailMap = new HashMap<>();
        detailMap.put(Emp_id, idValue);
        detailMap.put(Emp_id.replace("_id", "_date"), date);
        detailMap.put(Emp_id.replace("_id", "_remark"), remark);
        detailMap.put(Emp_id.replace("_id", "_amount"), amount);
        detailMap.put(Emp_id.replace("_id", "_ip_address"), ip_address);
        return detailMap;
    }

	public String checkApproverStatus(Integer id, Integer emp_id) {
		IncentiveApprover incentive = ApproverRepository.findByIncentiveApproverData(id,emp_id);
		if (incentive != null) {
	        // Check each status and determine the approver_status
			 boolean approverStatus = (incentive.getHoi_status() != null && incentive.getHoi_status()) ||
                     (incentive.getHod_status() != null && incentive.getHod_status()) ||
                     (incentive.getAsst_dir_status() != null && incentive.getAsst_dir_status()) ||
                     (incentive.getHr_status() != null && incentive.getHr_status()) ||
                     (incentive.getFinance_status() != null && incentive.getFinance_status());
	        
	        // Set the approver_status
	        incentive.setApprover_status(approverStatus);
	        
	        // Optionally, you can save the updated status back to the database
	        ApproverRepository.save(incentive);
	        
	        return approverStatus ? "Approver Status is TRUE" : "Approver Status is FALSE";
	    }
	    
	    return "Incentive Approver not found.";
	}

	public ResponseEntity<Object> incentiveApproverReport(Integer creditedMonth, Integer creditedYear) {
		List<Map<String, Object>> response=new ArrayList<>();
		List<Map<String, Object>> publicationDetails=publicationsRepository.publicationsApprovedByAll(creditedMonth,creditedYear);
		List<Map<String, Object>> conferenceDetails=conferencesRepository.conferencesApprovedByAll(creditedMonth,creditedYear);
		List<Map<String, Object>> bookChapterDetails=bookChapterRepository.bookChaptersApprovedByAll(creditedMonth,creditedYear);
		List<Map<String, Object>> membershipDetails=membershipRepository.membershipsApprovedByAll(creditedMonth,creditedYear);
		List<Map<String, Object>> grantDetails=grantsRepository.grantsApprovedByAll(creditedMonth,creditedYear);
		List<Map<String, Object>> patentDetails=patentRepository.patentsApprovedByAll(creditedMonth,creditedYear);
		if(ObjectUtils.isNotEmpty(publicationDetails)){
			response.addAll(publicationDetails);
		}
		if(ObjectUtils.isNotEmpty(conferenceDetails)){
			response.addAll(conferenceDetails);
		}
		if(ObjectUtils.isNotEmpty(bookChapterDetails)){
			response.addAll(bookChapterDetails);
		}
		if(ObjectUtils.isNotEmpty(membershipDetails)){
			response.addAll(membershipDetails);
		}
		if(ObjectUtils.isNotEmpty(grantDetails)){
			response.addAll(grantDetails);
		}
		if(ObjectUtils.isNotEmpty(patentDetails)){
			response.addAll(patentDetails);
		}

		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);

	}

	public ResponseEntity<Object> incentiveApproverReportByMonthAndYear(Integer creditedMonth, Integer creditedYear) {
		String queryStr=null;
		if(creditedMonth != null && creditedYear != null) {
			queryStr = "SELECT ia.credited_year as creditedYear,ia.credited_month as creditedMonth, sum(ia.amount) as totalAmount FROM incentive_approver ia Where ia.credited_month = :creditedMonth And ia.credited_year=:creditedYear And ia.active=true GROUP BY ia.credited_month,ia.credited_year";
		}else if(creditedMonth == null && creditedYear != null){
			queryStr = "SELECT ia.credited_year as creditedYear,ia.credited_month as creditedMonth, sum(ia.amount) as totalAmount FROM incentive_approver ia Where ia.credited_year=:creditedYear And ia.active=true GROUP BY ia.credited_month,ia.credited_year";
		}else{
			queryStr = "SELECT ia.credited_year as creditedYear,ia.credited_month as creditedMonth, sum(ia.amount) as totalAmount FROM incentive_approver ia Where ia.credited_month is not null And ia.credited_year is not null And ia.active=true GROUP BY ia.credited_month,ia.credited_year";
		}

		Query query = entityManager.createNativeQuery(queryStr);

		if (creditedMonth != null) {
			query.setParameter("creditedMonth", creditedMonth);
		}
		if (creditedYear != null) {
			query.setParameter("creditedYear", creditedYear);
		}

		List<Object[]> resultList = query.getResultList();

		List<Map<String, Object>> resultMaps = new ArrayList<>();
		for (Object[] result : resultList) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("creditedYear", result[0]);
			resultMap.put("creditedMonth", result[1]);
			resultMap.put("totalAmount", result[2]);
			resultMaps.add(resultMap);
		}

		return ResponseHandler.generateResponse(true, HttpStatus.OK, resultMaps);
	}
}