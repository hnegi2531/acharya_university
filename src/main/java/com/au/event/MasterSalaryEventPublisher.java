package com.au.event;

import com.au.model.*;
import com.au.repository.*;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class MasterSalaryEventPublisher {

	@Autowired
	private EmployeeSheetRepository employeeSheetRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private SalaryStructureDetailsRepository salaryStructureDetailsRepository;

	@Autowired
	private SalaryStructureHeadRepository salaryStructureHeadRepository;

	@Autowired
	private EmployeePayHistoryRepository employeePayHistoryRepository;

	@Autowired
	private InvPayRepository invPayRepository;

	@Autowired
	private AdvanceMonthlyEmiDeductionRepository advanceMonthlyEmiDeductionRepository;

	@Autowired
	private PaySlipLockDateRepository paySlipLockDateRepository;

	@Autowired
	private SlabStructureRepository slabStructureRepository;

	@Autowired
	private SlabDetailsRepository slabDetailsRepository;

	@Autowired
	private MasterSalaryRepository masterSalaryRepository;

	@Autowired
	private IncrementCreationRepository incrementCreationRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Async
	@EventListener
	public void handlePayDaysSalaryEvent(MasterSalaryEvent masterSalaryEvent) {
		PaySlipLockDate paySlipLockDate = paySlipLockDateRepository.getFirstByMonthAndYear(masterSalaryEvent.getMonth(), masterSalaryEvent.getYear());
		Date dateFromString = null;
		SimpleDateFormat formatter;
		try {
			if (ObjectUtils.isEmpty(paySlipLockDate)) {
				paySlipLockDate = new PaySlipLockDate();
				Date leaveLockDate = getDefaultLockDate(masterSalaryEvent.getMonth(), masterSalaryEvent.getYear());
				paySlipLockDate.setDisplay_date(leaveLockDate.toString());
				formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
				dateFromString = formatter.parse(paySlipLockDate.getDisplay_date());
				System.out.println("Converted Date: " + dateFromString);
			}else {
				String lockDate = paySlipLockDate.getDisplay_date();
				formatter = new SimpleDateFormat("dd-MM-yyyy");
				dateFromString = formatter.parse(lockDate);
				System.out.println("Converted Date: " + dateFromString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date date = new Date();

        if(dateFromString == null) throw new RuntimeException("Payslip lock date is null");
        LocalDate lockLocalDate = dateFromString.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate currentLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (currentLocalDate.isEqual(lockLocalDate) || currentLocalDate.isAfter(lockLocalDate)) {
			try {
				Integer empId = masterSalaryEvent.getEmpId();
				Integer month = masterSalaryEvent.getMonth();
				Integer year = masterSalaryEvent.getYear();
				System.out.println(empId + " : " + month + " : " + year);

				updateIncrementsOfMonthYear(month, year, empId);

				List<Integer> employeeIds;

				if(empId != null)
					employeeIds = Collections.singletonList(empId);
				else
					employeeIds = employeeDetailsRepository.getAllActiveEmployeeId();

				employeeIds.forEach(employeeId -> {
					Map<String, Object> employeeDetails = employeeDetailsRepository.getEmployeeDetailById(employeeId).stream().findFirst().get();
					Boolean active = (Boolean) employeeDetails.get("active");
					if(!active) return;
					if (ObjectUtils.isNotEmpty(employeeDetails)) {
						Float grossPay = (Float) employeeDetails.get("grosspay_ctc");
						if (ObjectUtils.isNotEmpty(grossPay) && grossPay > 0) {
							List<AdvanceMonthlyEmiDeduction> advanceMonthlyEmiDeductions = advanceMonthlyEmiDeductionRepository
									.getByActiveAndEmp_idAndMonthAndYear(true, employeeId,
											String.valueOf(month),
											String.valueOf(year));
							AdvanceMonthlyEmiDeduction emiDeduction = advanceMonthlyEmiDeductions.stream().findFirst()
									.orElse(null);
							String empCode = employeeDetails.get("empcode") != null
									? (String) employeeDetails.get("empcode")
									: null;
                            Float basic = employeeDetails.get("annual_salary") != null
                                    ? (Float) employeeDetails.get("annual_salary")
                                    : 0.0f;
							basic = (float) Math.round(basic);
							Integer salary_structure_id = (Integer) employeeDetails.get("salary_structure_id");
							InvPay invPay = invPayRepository.findByEmpCodeAndMonthAndYear(empCode,
									month, year);
							List<SalaryStructureDetails> salaryStructureDetails = salaryStructureDetailsRepository
									.getSalaryStructureDetails(salary_structure_id);
							Map<String, String> salaryhead = new HashMap<>();
							Map<String, Object> componentMap = new HashMap<>();
							Float totalEarning = 0.0f;
							MasterSalary masterSalary = masterSalaryRepository.findByEmpIdAndMonthAndYear(employeeId,
									month, year);
							if (ObjectUtils.isEmpty(masterSalary)) {
								masterSalary = new MasterSalary();
							}
							/*masterSalary.setEr(ObjectUtils.isNotEmpty(invPay) && ObjectUtils.isNotEmpty(invPay.getInvPay()) ?
									invPay.getInvPay() : 0);*/
							// ER is only for emp_pay_history not for master
							masterSalary.setEr(0.0);
							if (ObjectUtils.isNotEmpty(invPay) && ObjectUtils.isNotEmpty(invPay.getInvPay())) {
								totalEarning = (float) (basic);
							} else {
								totalEarning = basic;
							}
							masterSalary.setBasic(basic);

							for (SalaryStructureDetails salary : salaryStructureDetails) {
								SalaryStructureHead salaryStructureHead = salaryStructureHeadRepository
										.getSalaryStructureHead(salary.getSalary_structure_head_id());
								salaryhead.put(salaryStructureHead.getPrint_name(),
										salaryStructureHead.getCategory_name_type());
                                calculateComponent(salaryStructureHead, salary, totalEarning, componentMap, employeeDetails, grossPay);
                            }

							float da = componentMap.get("da") != null ? (float) componentMap.get("da") : 0.0f;
							float hra = componentMap.get("hra") != null ? (float) componentMap.get("hra") : 0.0f;
							float esi = componentMap.get("esi") != null ? (float) componentMap.get("esi") : 0.0f;
							;
							float pf1 = componentMap.get("pf") != null ? (float) componentMap.get("pf") : 0.0f;
							;
							float pf2 = componentMap.get("management_pf") != null
									? (float) componentMap.get("management_pf")
									: 0.0f;
							;
							float esic = componentMap.get("esic") != null ? (float) componentMap.get("esic") : 0.0f;
							;
							float pt = componentMap.get("pt") != null ? (float) componentMap.get("pt") : 0.0f;
							;
							float cca = componentMap.get("cca") != null ? (float) componentMap.get("cca") : 0.0f;
							;
							float totalpf = componentMap.get("totalpf") != null ? (float) componentMap.get("totalpf")
									: 0.0f;
							;
							float pfEarning = componentMap.get("pf_earning") != null
									? (float) componentMap.get("pf_earning")
									: 0.0f;
							;
							float pensionFund = componentMap.get("pension_fund") != null
									? (float) componentMap.get("pension_fund")
									: 0.0f;
							;
							float ta = componentMap.get("ta") != null ? (float) componentMap.get("ta") : 0.0f;
							float spl1 = componentMap.get("spl_1") != null ? (float) componentMap.get("spl_1") : 0.0f;
							;

							masterSalary.setDa((float) Math.round(da));
							masterSalary.setHra((float) Math.round(hra));
							masterSalary.setPf((float) Math.round(pf1));
							masterSalary.setEsi((float) Math.round(esi));
							masterSalary.setEsi_contribution_employee((float) Math.round(esic));
							masterSalary.setContribution_epf((float) Math.round(pf2));
							masterSalary.setSpl_1(
									employeeDetails.get("spl_1") != null ? (float) employeeDetails.get("spl_1") : 0.0f);
							masterSalary.setCca((float) Math.round(cca));
							masterSalary.setPt((float) Math.round(pt));
							masterSalary.setPf_earnings((float) Math.round(pfEarning));
							masterSalary.setPension_fund((float) Math.round(pensionFund));
							masterSalary.setTa((float) Math.round(ta));

							Float epfDifference = pf1 - pensionFund;

							masterSalary.setSchool_id((Integer) employeeDetails.get("school_id"));
							masterSalary.setDept_id((Integer) employeeDetails.get("dept_id"));

							masterSalary.setEpf_difference(epfDifference);
							Float totalDeduction = 0.0f;
							for (Entry<String, String> k : salaryhead.entrySet()) {
								if (StringUtils.equals(k.getValue(), "Earning")
										&& ObjectUtils.isNotEmpty(componentMap.get(k.getKey()))) {
									totalEarning = totalEarning + (Float) componentMap.get(k.getKey());
								}
								if (StringUtils.equals(k.getValue(), "Deduction")
										&& ObjectUtils.isNotEmpty(componentMap.get(k.getKey()))) {
									totalDeduction = totalDeduction + (Float) componentMap.get(k.getKey());
								}
							}

							float advanceEmi = 0.0f;
							float pTaxEmi = 0.0f;

							Map<Integer, Double> advanceEmiAmount = advanceMonthlyEmiDeductions.stream()
									.filter(t -> t.getCategory_name().equalsIgnoreCase("Advance"))
									.collect(Collectors.groupingBy(AdvanceMonthlyEmiDeduction::getEmp_id,
											Collectors.summingDouble(AdvanceMonthlyEmiDeduction::getEmi_amount)));

							if (!advanceEmiAmount.isEmpty()) {
								masterSalary
										.setAdvance(advanceEmiAmount.entrySet().iterator().next().getValue().floatValue());
								advanceEmi = Math.round(masterSalary.getAdvance());
							} else {
								masterSalary.setAdvance(advanceEmi);

							}

							Map<Integer, Double> ptaxEmiAmount = advanceMonthlyEmiDeductions.stream()
									.filter(t -> t.getCategory_name().equalsIgnoreCase("Perquisite Tax"))
									.collect(Collectors.groupingBy(AdvanceMonthlyEmiDeduction::getEmp_id,
											Collectors.summingDouble(AdvanceMonthlyEmiDeduction::getEmi_amount)));

							if (!ptaxEmiAmount.isEmpty()) {
								masterSalary
										.setPTax(ptaxEmiAmount.entrySet().iterator().next().getValue().floatValue());
								pTaxEmi = Math.round(masterSalary.getPTax());
							} else {
								masterSalary.setPTax(pTaxEmi);
							}

							if (advanceEmi != 0.0 || pTaxEmi != 0.0) {
								totalDeduction = totalDeduction + advanceEmi + pTaxEmi;
							}

							float te = Math.round(totalEarning);
							masterSalary.setTotal_earning(te);
							float td = Math.round(totalDeduction);
							masterSalary.setTotal_deduction(td);
							masterSalary.setGross_pay(te);
							float netpay = totalEarning - totalDeduction;
							if (netpay < 0.0) {
								netpay = 0.0f;
							}
							float np = Math.round(netpay);
							masterSalary.setNet_pay(np);
							masterSalary.setYear(masterSalaryEvent.getYear());
							masterSalary.setMonth(masterSalaryEvent.getMonth());
							masterSalary.setEmp_id(employeeId);
							masterSalary.setRemarks(
									ObjectUtils.isNotEmpty(emiDeduction) && ObjectUtils.isNotEmpty(emiDeduction.getRemark())
											? emiDeduction.getRemark()
											: "");
							masterSalaryRepository.save(masterSalary);
							System.out.println(empId + " : Master Salary");
							employeeDetailsRepository.updateDetailsForMasterSalary(ta, hra, da, employeeId);
						}
					}

				});
				PayDaysSalaryEvent payDaysSalaryEvent = new PayDaysSalaryEvent(month, year, empId);
				applicationEventPublisher.publishEvent(payDaysSalaryEvent);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
		}else {
			System.out.println("Lock date reached");
			throw new RuntimeException("Payslip lock  reached !!");
		}
	}

	private void updateIncrementsOfMonthYear(Integer month, Integer year, Integer empId) {
        try {
            List<IncrementCreation> incrementCreations = null;
			List<Integer> employeeIds = null;

			if(empId != null) {
				incrementCreations = Optional.ofNullable(incrementCreationRepository.findByEmpIdMonthAndYear(empId, month, year))
						.map(Collections::singletonList)
						.orElse(Collections.emptyList());
				if (!incrementCreations.isEmpty())
					employeeIds = Collections.singletonList(empId);
			}
			else {
				incrementCreations = incrementCreationRepository.getAllIncrementsOfMonthYear(month, year);
				employeeIds = incrementCreationRepository.getAllEmployeeIds(month, year);
			}

			if (incrementCreations.isEmpty()) return;
			if (employeeIds.isEmpty()) return;

			Map<Integer, EmployeeDetails> employeeDetailsMap = employeeDetailsRepository
					.getEmployeeDetailsByEmployeeIds(employeeIds)
					.stream()
					.collect(Collectors.toMap(EmployeeDetails::getEmp_id, e -> e));

            incrementCreations.forEach(incrementCreation -> {
				Integer employeeId = incrementCreation.getEmpId();
				EmployeeDetails employeeDetails = employeeDetailsMap.get(employeeId);

				if (employeeDetails == null) return;

				if(incrementCreation.getProposedCtc() != null){
					employeeDetails.setCtc(incrementCreation.getProposedCtc());
				}
				if(incrementCreation.getProposedGrosspay() != null){
					employeeDetails.setGrosspay_ctc(incrementCreation.getProposedGrosspay());
				}
				if(incrementCreation.getProposedBasic() != null){
					employeeDetails.setAnnual_salary(incrementCreation.getProposedBasic());
				}
				if(incrementCreation.getProposedDepartment() != null){
					employeeDetails.setDept_name_short(incrementCreation.getProposedDepartment());
				}
				if (incrementCreation.getProposedDepartmentId() != null){
					employeeDetails.setDept_id(incrementCreation.getProposedDepartmentId());
				}
				if(incrementCreation.getProposedDesignationId() != null){
					employeeDetails.setDesignation_id(incrementCreation.getProposedDesignationId());
				}
				if(incrementCreation.getProposedSalaryStructureId() != null){
					employeeDetails.setSalary_structure_id(incrementCreation.getProposedSalaryStructureId());
				}
				if(incrementCreation.getProposedTa() != null){
					employeeDetails.setTa(incrementCreation.getProposedTa());
				}
				if(incrementCreation.getProposedSplPay() != null){
					employeeDetails.setSpl_1(incrementCreation.getProposedSplPay());
				}
				employeeDetailsRepository.save(employeeDetails);
			});
        } catch (Exception e) {
            e.printStackTrace();
			System.out.println("Exception occurred in Increment: " + e.getMessage());
        }
    }

	private void calculateComponent(SalaryStructureHead salaryStructureHead,
									SalaryStructureDetails salary, Float basic, Map<String, Object> componentMap,
									Map<String, Object> employeeDetails, Float grossPay) {

		Float pf = 0.0f;
		Float pf1 = componentMap.get("pf") != null ? (Float) componentMap.get("pf") : 0.0f;
		Float pf2 = componentMap.get("management_pf") != null ? (Float) componentMap.get("management_pf") : 0.0f;
		Float esi = componentMap.get("esi") != null ? (Float) componentMap.get("esi") : 0.0f;
		Float esic = componentMap.get("esic") != null ? (Float) componentMap.get("esic") : 0.0f;
		Float da = componentMap.get("da") != null ? (Float) componentMap.get("da") : 0.0f;
		Float spl_1 = componentMap.get("spl_1") != null ? (Float) componentMap.get("spl_1") : 0.0f;
		Float hra = componentMap.get("hra") != null ? (Float) componentMap.get("hra") : 0.0f;
		Float totalpf = componentMap.get("totalpf") != null ? (Float) componentMap.get("totalpf") : 0.0f;
		Float ta = componentMap.get("ta") != null ? (Float) componentMap.get("ta") : 0.0f;

		Float annualSalary = (Float) employeeDetails.get("annual_salary") != null
				? (Float) employeeDetails.get("annual_salary")
				: 0.0f;

		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "da")) {

			da = ObjectUtils.isNotEmpty(salary.getPercentage()) ? (salary.getPercentage() / 100) * annualSalary : 0.0f;

			componentMap.put("da", ObjectUtils.isNotEmpty(da) ? da : 0);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "hra")) {

			hra = ObjectUtils.isNotEmpty(salary.getPercentage()) ? (salary.getPercentage() / 100) * annualSalary : 0.0f;

			componentMap.put("hra", ObjectUtils.isNotEmpty(hra) ? hra : 0);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "spl_1") && employeeDetails.get("spl_1") != null) {

			spl_1 = employeeDetails.get("spl_1") != null ? (Float) employeeDetails.get("spl_1") : 0.0f;

			componentMap.put("spl_1", spl_1);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "cca")) {

			SlabStructure slabStructure = slabStructureRepository
					.getSlabStructureDetailsBySlabDetailsId(salary.getSlab_details_id(), Math.round(annualSalary));

			Float cca = 0.0f;
			if (slabStructure.getMin_value() < annualSalary && slabStructure.getMax_value() >= annualSalary) {

				cca = slabStructure.getHead_value();

				componentMap.put("cca", cca);
			}

		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "esic")) {

			if (salary.getGross_limit() > grossPay) {
				esic = ObjectUtils.isNotEmpty(salary.getPercentage()) ?
						(salary.getPercentage() / 100) * (annualSalary + da + hra + spl_1 + ta) : 0.0f;

				componentMap.put("esic", ObjectUtils.isNotEmpty(esic) ? esic : 0.0f);
			}
			else componentMap.put("esic",0.0f);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "esi")) {
			if (salary.getGross_limit() > grossPay) {
				esi =  (ObjectUtils.isNotEmpty(salary.getPercentage()) ?
						(((salary.getPercentage() / 100) * (annualSalary + da + hra + spl_1 + ta))) : 0.0f);
			} else {
				esi = 0.0f;
			}
			componentMap.put("esi", esi);
			componentMap.put("esiGrossLimit", salary.getGross_limit());
			componentMap.put("esiPercentage", salary.getPercentage());
			componentMap.put("esiPaydays", ObjectUtils.isNotEmpty(salary.getIsPayDay()) ? salary.getIsPayDay() : false);

		}
		
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "pf")) {
			if (employeeDetails.get("pf_status") != null && (Boolean) employeeDetails.get("pf_status")) {
				pf1 = (ObjectUtils.isNotEmpty(salary.getPercentage()) ? (salary.getPercentage() / 100) * (basic + da) : 0.0f);
				if (pf1 > 1800) {
					pf1 = 1800f;
					componentMap.put("pf", ObjectUtils.isNotEmpty(pf1) ? pf1 : 0);
				} else {
					componentMap.put("pf", ObjectUtils.isNotEmpty(pf1) ? pf1 : 0);
				}
			}else {
				componentMap.put("pf", ObjectUtils.isNotEmpty(pf1) ? pf1 : 0);
			}
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "management_pf")) {
			if (employeeDetails.get("pf_status") != null && (Boolean) employeeDetails.get("pf_status")) {
				pf2 = (ObjectUtils.isNotEmpty(salary.getPercentage()) ? (salary.getPercentage() / 100) * (basic + da) : 0.0f);
				if (pf2 > 1800) {
					pf2 = 1800f;
					componentMap.put("management_pf", ObjectUtils.isNotEmpty(pf2) ? pf2 : 0);
				} else {
					componentMap.put("management_pf", ObjectUtils.isNotEmpty(pf2) ? pf2 : 0);
				}
			}else {
				componentMap.put("management_pf", ObjectUtils.isNotEmpty(pf2) ? pf2 : 0);
			}
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "pt")) {
			Float pt = 0.0f;
			if(employeeDetails.get("pt_status") != null && (Boolean) employeeDetails.get("pt_status")) {
				SlabStructure slabStructure = slabStructureRepository
						.getSlabStructureDetailsBySlabDetailsId(salary.getSlab_details_id(), Math.round(annualSalary));
				if (slabStructure.getMin_value() < annualSalary && slabStructure.getMax_value() >= annualSalary) {
					pt = slabStructure.getHead_value();
				}
			}
			componentMap.put("pt", pt);
		}
		if (pf1 != 0.0 || pf2 != 0.0) {
			pf = pf1 + pf2;
			componentMap.put("totalpf", ObjectUtils.isNotEmpty(pf) ? pf : 0);
			if (pf >= 3600) {
				Float pensionFund = (float) (8.33f / 100) * 15000;
				Float pfEarning = 15000f;
				componentMap.put("pension_fund", pensionFund);
				componentMap.put("pf_earning", pfEarning);
			}
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "ta")) {

			ta = employeeDetails.get("ta") != null ? (Float) employeeDetails.get("ta") : 0.0f;

			componentMap.put("ta", ta);
		}
	}

	private Date getDefaultLockDate(Integer month, Integer year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month); // Adjust month to be zero-based
		calendar.set(Calendar.DAY_OF_MONTH, 6); // Set day to the first day of the month
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

}
