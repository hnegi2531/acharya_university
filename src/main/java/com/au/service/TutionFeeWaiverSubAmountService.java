package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.TutionFeeWaiverDto;
import com.au.model.TutionFeeWaiver;
import com.au.model.TutionFeeWaiverSubAmount;
import com.au.repository.TutionFeeWaiverReportRepository;
import com.au.repository.TutionFeeWaiverRepository;
import com.au.repository.TutionFeeWaiverSubAmountRepository;

@Service
public class TutionFeeWaiverSubAmountService {
	
	@Autowired
	private TutionFeeWaiverSubAmountRepository tfwr_repo;
	
	@Autowired
	private TutionFeeWaiverRepository t_repo;
	
	@Autowired
	private TutionFeeWaiverReportRepository tut_repo;
	
	public TutionFeeWaiver updateTutionFeeWaiverSubAmount(@Valid TutionFeeWaiverDto cos,JwtDetails jwtDetails) {
		
		TutionFeeWaiver tfw = t_repo.getTutionFeeWaiver(cos.getTut_fee_wavier().getTution_fee_waiver_id());
		tfw.setModified_by(jwtDetails.getUserId());
		tfw.setModified_username(jwtDetails.getUserName());
		tfw.setApproved_status(cos.getTut_fee_wavier().getApproved_status());
		tfw.setApproved_by(cos.getTut_fee_wavier().getApproved_by());
		tfw.setApproved_date(cos.getTut_fee_wavier().getApproved_date());
		tfw.setActive(cos.getTut_fee_wavier().getActive());
		tfw.setReference_name(cos.getTut_fee_wavier().getReference_name());
		tfw.setRemarks(cos.getTut_fee_wavier().getRemarks());
		tfw.setStudent_id(cos.getTut_fee_wavier().getStudent_id());
		tfw.setTotal_amount(cos.getTut_fee_wavier().getTotal_amount());
		TutionFeeWaiver ttt = t_repo.save(tfw);
		
		cos.getYear_sem().entrySet().stream().forEach(f -> {
			tut_repo.updateYearlyWaiverAmount(cos.getTut_fee_wavier().getTution_fee_waiver_id(),f.getKey(),f.getValue());
		});
//		tut_repo.saveAll(cos.getTut_fee_waiver_report());
		
		cos.getTut_fee_waiver_sub_amount().stream().forEach(f1 -> {
			f1.setModified_by(jwtDetails.getUserId());
			f1.setModified_username(jwtDetails.getUserName());
		});
		tfwr_repo.saveAll(cos.getTut_fee_waiver_sub_amount());
	//	t_repo.updateApproveStatus(cos.get(0).getTution_fee_waiver_id());
		return ttt;
	}

}
