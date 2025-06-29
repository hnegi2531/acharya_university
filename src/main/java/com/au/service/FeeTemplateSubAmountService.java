package com.au.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import com.au.dto.FeeTemplateAmount;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeTemplate;
import com.au.model.FeeTemplateHistory;
import com.au.model.FeeTemplateSubAmount;
import com.au.model.FeeTemplateSubAmountHistory;
import com.au.repository.FeeTemplateHistoryRepository;
import com.au.repository.FeeTemplateRepository;
import com.au.repository.FeeTemplateSubAmountHistoryRepository;
import com.au.repository.FeeTemplateSubAmountRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class FeeTemplateSubAmountService {

	@Autowired
	private FeeTemplateSubAmountRepository ftsa_repo;

	@Autowired
	private FeeTemplateRepository ftr_repo;

	@Autowired
	private FeeTemplateHistoryRepository fthr_repo;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private FeeTemplateSubAmountHistoryRepository ftsah_repo;
	
	@Autowired
	private ProgramSpecilizationRepository ps_repo;

//	public List<FeeTemplateSubAmount> listAll() {
//		return ftsa_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = ftsa_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = ftsa_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public FeeTemplateSubAmount saveFeeTemplateSubAmount1(@Valid FeeTemplateSubAmount feetemplatesubamount) {
		return ftsa_repo.save(feetemplatesubamount);

	}

	public List<FeeTemplateSubAmount> saveFeeTemplateSubAmount(List<FeeTemplateSubAmount> feetemplatesubamount) {
//		List<Integer> ids = new ArrayList<Integer>();
//		System.out.println("**************************** "+feetemplatesubamount.get(0).getFee_sub_amt_id());
//		List<Integer> fee_sub_amt_id = feetemplatesubamount.stream()
//		        .map(entity -> entity.getFee_sub_amt_id())
//		        .collect(Collectors.toList());
//		System.out.println("()()()()()()()()()()()())(((((((((((((( "+fee_sub_amt_id);
		ftsa_repo.deleteDataByFeeTemplateId(feetemplatesubamount.get(0).getFee_template_id());
		return ftsa_repo.saveAll(feetemplatesubamount);
	}

	public List<FeeTemplateSubAmountHistory> saveFeeTemplateSubAmountHistory(
			List<FeeTemplateSubAmountHistory> feetemplatesubamounthistory) {
		System.out.println("******************");
		try {
			ftsah_repo.saveAll(feetemplatesubamounthistory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feetemplatesubamounthistory;
	}

	public FeeTemplateSubAmount get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return ftsa_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplateSubAmount id Not Found:" + id));
	}

	public void delete(Integer id) {
		FeeTemplateSubAmount ay = ftsa_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplateSubAmount id Not Found:" + id));
		ftsa_repo.delete(ay);
	}

	public List<FeeTemplateSubAmount> saveFeeTemplateTotalAmount(FeeTemplateAmount feetemplate,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		FeeTemplate feetemplate1 = ftr_repo.findByfee_template_id(feetemplate.getFt().getFee_template_id());
		feetemplate1.setFee_year1_amt(feetemplate.getFt().getFee_year1_amt());
		feetemplate1.setFee_year2_amt(feetemplate.getFt().getFee_year2_amt());
		feetemplate1.setFee_year3_amt(feetemplate.getFt().getFee_year3_amt());
		feetemplate1.setFee_year4_amt(feetemplate.getFt().getFee_year4_amt());
		feetemplate1.setFee_year5_amt(feetemplate.getFt().getFee_year5_amt());
		feetemplate1.setFee_year6_amt(feetemplate.getFt().getFee_year6_amt());
		feetemplate1.setFee_year7_amt(feetemplate.getFt().getFee_year7_amt());
		feetemplate1.setFee_year8_amt(feetemplate.getFt().getFee_year8_amt());
		feetemplate1.setFee_year9_amt(feetemplate.getFt().getFee_year9_amt());
		feetemplate1.setFee_year10_amt(feetemplate.getFt().getFee_year10_amt());
		feetemplate1.setFee_year11_amt(feetemplate.getFt().getFee_year11_amt());
		feetemplate1.setFee_year12_amt(feetemplate.getFt().getFee_year12_amt());
		feetemplate1.setFee_year_total_amount(feetemplate.getFt().getFee_year_total_amount());
		feetemplate1.setIs_paid_at_board(feetemplate.getFt().getIs_paid_at_board());
		ftr_repo.save(feetemplate1);
		return saveFeeTemplateSubAmount(feetemplate.getFtsa());
		// FeeTemplateSubAmount fts = saveFeeTemplateSubAmount(feetemplate.getFtsa());
		// return fts;

	}

	public List<FeeTemplateSubAmount> saveFeeTemplateTotalAmounts(FeeTemplateAmount feetemplate,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		FeeTemplate feetemplate1 = ftr_repo.findByfee_template_id(feetemplate.getFt().getFee_template_id());
		feetemplate1.setFee_year1_amt(feetemplate.getFt().getFee_year1_amt());
		feetemplate1.setFee_year2_amt(feetemplate.getFt().getFee_year2_amt());
		feetemplate1.setFee_year3_amt(feetemplate.getFt().getFee_year3_amt());
		feetemplate1.setFee_year4_amt(feetemplate.getFt().getFee_year4_amt());
		feetemplate1.setFee_year5_amt(feetemplate.getFt().getFee_year5_amt());
		feetemplate1.setFee_year6_amt(feetemplate.getFt().getFee_year6_amt());
		feetemplate1.setFee_year7_amt(feetemplate.getFt().getFee_year7_amt());
		feetemplate1.setFee_year8_amt(feetemplate.getFt().getFee_year8_amt());
		feetemplate1.setFee_year9_amt(feetemplate.getFt().getFee_year9_amt());
		feetemplate1.setFee_year10_amt(feetemplate.getFt().getFee_year10_amt());
		feetemplate1.setFee_year11_amt(feetemplate.getFt().getFee_year11_amt());
		feetemplate1.setFee_year12_amt(feetemplate.getFt().getFee_year12_amt());
		feetemplate1.setFee_year_total_amount(feetemplate.getFt().getFee_year_total_amount());
		feetemplate1.setIs_paid_at_board(feetemplate.getFt().getIs_paid_at_board());
		ftr_repo.save(feetemplate1);
		fthr_repo.save(feetemplate.getFth());
		saveFeeTemplateSubAmountHistory(feetemplate.getFtsah());
		return saveFeeTemplateSubAmount(feetemplate.getFtsa());
		// FeeTemplateSubAmount fts = saveFeeTemplateSubAmount(feetemplate.getFtsa());
		// return fts;

	}

	public List<FeeTemplateSubAmount> approveFeeTemplateTotalAmount(@Valid FeeTemplateAmount feetemplateamount,
			String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		FeeTemplateHistory feeTemplateHistory = new FeeTemplateHistory();
		// FeeTemplate feeTemplate=new FeeTemplate();
		// ApproveHistory approveHistory=new ApproveHistory();
		FeeTemplate feetemplate = ftr_repo.findByfee_template_id(feetemplateamount.getFt().getFee_template_id());
		feetemplate.setFee_year1_amt(feetemplateamount.getFt().getFee_year1_amt());
		feetemplate.setFee_year2_amt(feetemplateamount.getFt().getFee_year2_amt());
		feetemplate.setFee_year3_amt(feetemplateamount.getFt().getFee_year3_amt());
		feetemplate.setFee_year4_amt(feetemplateamount.getFt().getFee_year4_amt());
		feetemplate.setFee_year5_amt(feetemplateamount.getFt().getFee_year5_amt());
		feetemplate.setFee_year6_amt(feetemplateamount.getFt().getFee_year6_amt());
		feetemplate.setFee_year7_amt(feetemplateamount.getFt().getFee_year7_amt());
		feetemplate.setFee_year8_amt(feetemplateamount.getFt().getFee_year8_amt());
		feetemplate.setFee_year9_amt(feetemplateamount.getFt().getFee_year9_amt());
		feetemplate.setFee_year10_amt(feetemplateamount.getFt().getFee_year10_amt());
		feetemplate.setFee_year11_amt(feetemplateamount.getFt().getFee_year11_amt());
		feetemplate.setFee_year12_amt(feetemplateamount.getFt().getFee_year12_amt());
		feetemplate.setFee_year_total_amount(feetemplateamount.getFt().getFee_year_total_amount());
		feetemplate.setApproved_status(feetemplateamount.getFt().getApproved_status());
		feetemplate.setApproved_date(LocalDate.now());
		feetemplate.setApproved_by(jwtDetails.getUserId());
		// approveHistory.setFee_template_id(feetemplateamount.getFt().getFee_template_id());
		// approveHistory.setApproved_status(feetemplateamount.getAh().getApproved_status());
		// approveHistory.setApproved_date(LocalDate.now());
		// approveHistory.setApproved_by(jwtDetails.getUserId());
		// ahr_repo.save(approveHistory);
		feeTemplateHistory.setAc_year_id(feetemplateamount.getFth().getAc_year_id());
		feeTemplateHistory.setFee_admission_category_id(feetemplateamount.getFth().getFee_admission_category_id());
		feeTemplateHistory.setFee_admission_sub_category_id(feetemplateamount.getFth().getFee_admission_sub_category_id());
		feeTemplateHistory.setNationality(feetemplateamount.getFth().getNationality());
		feeTemplateHistory.setIs_nri(feetemplateamount.getFth().getIs_nri());
		feeTemplateHistory.setProgram_type_id(feetemplateamount.getFth().getProgram_type_id());
		feeTemplateHistory.setCurrency_type_id(feetemplateamount.getFth().getCurrency_type_id());
		feeTemplateHistory.setFee_template_id(feetemplateamount.getFt().getFee_template_id());
		feeTemplateHistory.setFee_template_name(feetemplateamount.getFth().getFee_template_name());
		feeTemplateHistory.setRemarks(feetemplateamount.getFth().getRemarks());
		feeTemplateHistory.setSchool_id(feetemplateamount.getFth().getSchool_id());
		feeTemplateHistory.setProgram_id(feetemplateamount.getFth().getProgram_id());
		feeTemplateHistory.setProgram_specialization_id(feetemplateamount.getFth().getProgram_specialization_id());
		feeTemplateHistory.setProgram_specialization(ps_repo.getCommaSeperartedProgramSpecializationShortName(ResponseHandler.toConvertCommaSeperatedIdsAsList(feetemplateamount.getFth().getProgram_specialization_id())));
		feeTemplateHistory.setFee_year1_amt(feetemplateamount.getFth().getFee_year1_amt());
		feeTemplateHistory.setFee_year2_amt(feetemplateamount.getFth().getFee_year2_amt());
		feeTemplateHistory.setFee_year3_amt(feetemplateamount.getFth().getFee_year3_amt());
		feeTemplateHistory.setFee_year4_amt(feetemplateamount.getFth().getFee_year4_amt());
		feeTemplateHistory.setFee_year5_amt(feetemplateamount.getFth().getFee_year5_amt());
		feeTemplateHistory.setFee_year6_amt(feetemplateamount.getFth().getFee_year6_amt());
		feeTemplateHistory.setFee_year7_amt(feetemplateamount.getFth().getFee_year7_amt());
		feeTemplateHistory.setFee_year8_amt(feetemplateamount.getFth().getFee_year8_amt());
		feeTemplateHistory.setFee_year9_amt(feetemplateamount.getFth().getFee_year9_amt());
		feeTemplateHistory.setFee_year10_amt(feetemplateamount.getFth().getFee_year10_amt());
		feeTemplateHistory.setFee_year11_amt(feetemplateamount.getFth().getFee_year11_amt());
		feeTemplateHistory.setFee_year12_amt(feetemplateamount.getFth().getFee_year12_amt());
		feeTemplateHistory.setCreated_by(feetemplateamount.getFth().getCreated_by());
		feeTemplateHistory.setCreated_username(feetemplateamount.getFth().getCreated_username());
		feeTemplateHistory.setFee_year_total_amount(feetemplateamount.getFth().getFee_year_total_amount());
		feeTemplateHistory.setApproved_status(feetemplateamount.getFth().getApproved_status());
		feeTemplateHistory.setActive(feetemplateamount.getFth().getActive());
		feeTemplateHistory.setApproved_date(LocalDate.now());
		feeTemplateHistory.setApproved_by(jwtDetails.getUserId());
		feeTemplateHistory.setApproved_name(jwtDetails.getUserName());
		ftr_repo.save(feetemplate);
		fthr_repo.save(feeTemplateHistory);
		saveFeeTemplateSubAmountHistory(feetemplateamount.getFtsah());
		return saveFeeTemplateSubAmount(feetemplateamount.getFtsa());
	}

	public List<FeeTemplateSubAmount> saveFeeTemplateTotalAmount2(List<FeeTemplateSubAmount> feetemplatesubamount) {
		return ftsa_repo.saveAll(feetemplatesubamount);
	}

	public List<Map<String, Object>> findByFeeTemplate(Integer fee_template_id) {
		System.out.println("fee_template_id  -> "+fee_template_id);
		return ftsa_repo.fetchByFeeTemplateId(fee_template_id);
	}

	public List<FeeTemplateSubAmount> findByFeeTemplate1(Integer fee_template_id) {
		return ftsa_repo.fetchByFeeTemplateId1(fee_template_id);
	}
}
