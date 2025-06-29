package com.au.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.AdvancePayScaleDeductionDto;
import com.au.model.AdvanceMonthlyEmiDeduction;
import com.au.model.AdvancePayScaleDeduction;
import com.au.repository.AdvanceMonthlyEmiDeductionRepository;
import com.au.repository.AdvancePayScaleDeductionRepository;
import com.au.response.ResponseHandler;




@Service
public class AdvancePayScaleDeductionService {

	@Autowired
	private AdvancePayScaleDeductionRepository PSDeduction_repo;
	
	@Autowired
	private AdvanceMonthlyEmiDeductionRepository ameds_repo;
	
	
	Double remaining_balance = 0.0;

	public List<AdvancePayScaleDeduction> advancePayScaleDeduction(@Valid AdvancePayScaleDeductionDto apsd)
			throws Exception {

		List<AdvancePayScaleDeduction> list = new ArrayList<AdvancePayScaleDeduction>();

		if (apsd.getPrincipal_amount() != null && apsd.getEmi_amount() != null) {
			remaining_balance = apsd.getPrincipal_amount() - apsd.getEmi_amount();
		} else {
			// If either of them is null, do not calculate remaining_balance or set it to 0
			remaining_balance = 0.0;
		}

		if (apsd.getCategory_name().equalsIgnoreCase("advance")) {

			apsd.getEmp_id().stream().forEach(ps -> {

				AdvancePayScaleDeduction payScale = new AdvancePayScaleDeduction();

				payScale.setActive(apsd.getActive());
				payScale.setCategory_name(apsd.getCategory_name());
				payScale.setCompleted_tenture(apsd.getCompleted_tenture());
				payScale.setCreated_date(apsd.getCreated_date());
				payScale.setCreated_by(apsd.getCreated_by());
				payScale.setCreated_username(apsd.getCreated_username());
				payScale.setCurrent_tenture(apsd.getCurrent_tenture());
				payScale.setDeactivate_month(apsd.getDeactivate_month());
				payScale.setDeactivate_year(apsd.getDeactivate_year());
				payScale.setEmi_amount(apsd.getEmi_amount());
				payScale.setEmp_id(ps);
				payScale.setLoan_completed_date(apsd.getLoan_completed_date());
				payScale.setLoan_end_date(apsd.getLoan_end_date());
				payScale.setLoan_started_date(apsd.getLoan_started_date());
				payScale.setPrincipal_amount(apsd.getPrincipal_amount());
				payScale.setSchool_id(apsd.getSchool_id());
				payScale.setTenure(apsd.getTenure());
				payScale.setModified_by(apsd.getModified_by());
				payScale.setModified_username(apsd.getModified_username());
				payScale.setLic_number(apsd.getLic_number());

				AdvancePayScaleDeduction data = PSDeduction_repo.save(payScale);
				list.add(data);
//			System.out.println("RDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"+remaining_balance);
//			remaining_balance = apsd.getPrincipal_amount()-apsd.getEmi_amount();

				apsd.getAmed().stream().forEach(ap -> {

					AdvanceMonthlyEmiDeduction emiData = new AdvanceMonthlyEmiDeduction();
					System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP" + remaining_balance);
					emiData.setEmp_id(ps);
					emiData.setAdvance_id(data.getAdvance_id());
					emiData.setActive(apsd.getActive());
					emiData.setCategory_name(ap.getCategory_name());
					emiData.setCreated_by(apsd.getCreated_by());
					emiData.setCreated_date(apsd.getCreated_date());
					emiData.setEmi_amount(ap.getEmi_amount());
					emiData.setMonth(ap.getMonth());
					emiData.setYear(ap.getYear());
					emiData.setPrincipal_amount(apsd.getPrincipal_amount());
					emiData.setLoan_started_date(apsd.getLoan_started_date());
					emiData.setLoan_end_date(apsd.getLoan_end_date());

					if (apsd.getPrincipal_amount() != null && apsd.getEmi_amount() != null) {
						emiData.setRemaining_balance(remaining_balance);
					} else {
						emiData.setRemaining_balance(0.0); // Default value for remaining_balance if amounts are null
					}

					AdvanceMonthlyEmiDeduction savedData = ameds_repo.save(emiData);

					remaining_balance = savedData.getRemaining_balance() - ap.getEmi_amount();
//				 count++;
				});
				remaining_balance = 0.0;
			});
		} else {
			apsd.getEmp_id().stream().forEach(ps -> {

				AdvancePayScaleDeduction payScale = new AdvancePayScaleDeduction();

				payScale.setActive(apsd.getActive());
				payScale.setCategory_name(apsd.getCategory_name());
				payScale.setCompleted_tenture(apsd.getCompleted_tenture());
				payScale.setCreated_date(apsd.getCreated_date());
				payScale.setCreated_by(apsd.getCreated_by());
				payScale.setCreated_username(apsd.getCreated_username());
				payScale.setCurrent_tenture(apsd.getCurrent_tenture());
				payScale.setDeactivate_month(apsd.getDeactivate_month());
				payScale.setDeactivate_year(apsd.getDeactivate_year());
				payScale.setEmp_id(ps);
				payScale.setEmi_amount(apsd.getEmi_amount());
				payScale.setLoan_completed_date(apsd.getLoan_completed_date());
				payScale.setLoan_end_date(apsd.getLoan_end_date());
				payScale.setLoan_started_date(apsd.getLoan_started_date());
				payScale.setPrincipal_amount(apsd.getPrincipal_amount());
				payScale.setSchool_id(apsd.getSchool_id());
				payScale.setTenure(apsd.getTenure());
				payScale.setModified_by(apsd.getModified_by());
				payScale.setModified_username(apsd.getModified_username());
				payScale.setLic_number(apsd.getLic_number());
				payScale.setEmi_amount(apsd.getAmed().get(0).getEmi_amount());
				payScale.setPrincipal_amount(apsd.getTenure() * apsd.getAmed().get(0).getEmi_amount());
				AdvancePayScaleDeduction data = PSDeduction_repo.save(payScale);
				list.add(data);

//			remaining_balance = apsd.getPrincipal_amount()-apsd.getEmi_amount();

				apsd.getAmed().stream().forEach(ap -> {

					AdvanceMonthlyEmiDeduction emiData = new AdvanceMonthlyEmiDeduction();
					System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP" + remaining_balance);
					emiData.setEmp_id(ps);
					emiData.setAdvance_id(data.getAdvance_id());
					emiData.setActive(apsd.getActive());
					emiData.setCategory_name(ap.getCategory_name());
					emiData.setCreated_by(apsd.getCreated_by());
					emiData.setCreated_date(apsd.getCreated_date());
					emiData.setEmi_amount(ap.getEmi_amount());
					emiData.setMonth(ap.getMonth());
					emiData.setYear(ap.getYear());
					emiData.setPrincipal_amount(apsd.getPrincipal_amount());
					emiData.setLoan_started_date(apsd.getLoan_started_date());
					emiData.setLoan_end_date(apsd.getLoan_end_date());

					if (apsd.getPrincipal_amount() != null && apsd.getEmi_amount() != null) {
						emiData.setRemaining_balance(remaining_balance);
					} else {
						emiData.setRemaining_balance(0.0); // Default value for remaining_balance if amounts are
															// null
					}

					AdvanceMonthlyEmiDeduction savedData = ameds_repo.save(emiData);

					remaining_balance = savedData.getRemaining_balance() - ap.getEmi_amount();
//				 count++;
				});
				remaining_balance = 0.0;
			});

		}
		remaining_balance = 0.0;
		return list;

	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = PSDeduction_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = PSDeduction_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}

		public void deactivate(Integer id) {
		AdvancePayScaleDeduction co = PSDeduction_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		PSDeduction_repo.deactivate(id);
		ameds_repo.deactivateAllEmisByAdvanceId(id);
	}
	
	public void activate(Integer id) {
		AdvancePayScaleDeduction co = PSDeduction_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		PSDeduction_repo.activate(id);
		ameds_repo.activateAllEmisByAdvanceId(id);
	}

	public AdvancePayScaleDeduction updateAdvancePayScaleDeduction(AdvancePayScaleDeduction cos) {
		return PSDeduction_repo.save(cos);
	}

	public List<Map<String, Object>> fetchTimeTableDetailsByEmployeeId(Integer advance_id) {
		return PSDeduction_repo.fetchTimeTableDetailsByEmployeeId(advance_id);
	}

	Double remaining_balance1=0.0;
	public void updateEmi(Integer emi_id, Integer advance_id, Double emi_amount) {
		PSDeduction_repo.updateEmi(emi_id);
		List<Integer> li = PSDeduction_repo.getListOfEmiIds(emi_id,advance_id);
		System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL "+li);
		li.stream().forEach(l -> {
			remaining_balance1 = PSDeduction_repo.getRemainingBalance(l)+emi_amount;
			//remaining_balance1 = remaining_balance1+emi_amount;
			PSDeduction_repo.updateRemainingBalance(l, remaining_balance1);
		});
	}
}
