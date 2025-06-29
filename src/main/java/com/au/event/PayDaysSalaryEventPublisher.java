package com.au.event;

import com.au.model.*;
import com.au.repository.*;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class PayDaysSalaryEventPublisher {

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
	private LockDateRepository lockDateRepository;

	@Autowired
	private SlabStructureRepository slabStructureRepository;

	@Autowired
	private SlabDetailsRepository slabDetailsRepository;

	@Autowired
	private TdsDeductionRepository tdsDeductionRepository;

	@Async
	@EventListener
	public void handlePayDaysSalaryEvent(PayDaysSalaryEvent payDaysSalaryEvent) {
		try {
			System.out.println(payDaysSalaryEvent.getMonth() + " " + payDaysSalaryEvent.getYear());

			Integer empId = payDaysSalaryEvent.getEmpId();
			Integer month = payDaysSalaryEvent.getMonth();
			Integer year = payDaysSalaryEvent.getYear();

			List<EmployeeSheet> employeeSheets;

			if(payDaysSalaryEvent.getEmpId() != null)
				employeeSheets = Collections.singletonList(employeeSheetRepository.findByEmpIdAndMonthAndYear(empId, month, year));
			else
				employeeSheets = employeeSheetRepository.getByMonthAndYearAndPaydays(month, year);

			employeeSheets.forEach(es -> {
				Map<String, Object> employeeDetails = employeeDetailsRepository.getEmployeeDetailById(es.getEmpId())
						.stream().findFirst().get();

				if (ObjectUtils.isNotEmpty(employeeDetails) && es.getPaydays() > 0) {
					Float masterSalary = (Float) employeeDetails.get("grosspay_ctc");
					String empCode = (String) employeeDetails.get("empcode");
					if (ObjectUtils.isNotEmpty(masterSalary) && masterSalary > 0) {
						List<AdvanceMonthlyEmiDeduction> advanceMonthlyEmiDeductions = advanceMonthlyEmiDeductionRepository
								.getByActiveAndEmp_idAndMonthAndYear(true, es.getEmpId(),
										String.valueOf(month),
										String.valueOf(year));
						AdvanceMonthlyEmiDeduction emiDeduction = advanceMonthlyEmiDeductions.stream().findFirst()
								.orElse(null);
						TdsDeduction tdsDeduction = tdsDeductionRepository.findByEmpCodeAndMonthAndYear(empCode,
								month, year);
						Float annualSalary = (Float) employeeDetails.get("annual_salary") != null
								? (Float) employeeDetails.get("annual_salary")
								: 0.0f;
						Float basic = (float) ((annualSalary / es.getTotalDays()) * es.getPaydays());
						basic = (float) Math.round(basic);
						Integer salary_structure_id = (Integer) employeeDetails.get("salary_structure_id");
						List<InvPay> invPay = invPayRepository.getByEmpCodeAndMonthAndYear(es.getEmpCode(), es.getMonth(),
								es.getYear());
						List<SalaryStructureDetails> salaryStructureDetails = salaryStructureDetailsRepository
								.getSalaryStructureDetails(salary_structure_id);
						Map<String, String> salaryhead = new HashMap<String, String>();
						Map<String, Object> componentMap = new HashMap<String, Object>();
						Float totalEarning = 0.0f;
						EmployeePayHistory employeePayHistory = employeePayHistoryRepository
								.findByEmpIdAndMonthAndYear(es.getEmpId(), es.getMonth(), es.getYear());
						if (ObjectUtils.isEmpty(employeePayHistory)) {
							employeePayHistory = new EmployeePayHistory();
						}


						Double er=(invPay != null && !invPay.isEmpty()) ?
								invPay.stream().filter(Objects::nonNull)
										.filter(i -> !i.getType().equalsIgnoreCase("Exam Remuneration"))
										.mapToDouble(i -> i.getInvPay() != null ? i.getInvPay() : 0.0).sum() : 0.0;
						Double examRemuneration = (invPay != null && !invPay.isEmpty()) ?
								invPay.stream().filter(Objects::nonNull)
										.filter(i -> i.getType().equalsIgnoreCase("Exam Remuneration"))
										.mapToDouble(i -> i.getInvPay() != null ? i.getInvPay() : 0.0).sum() : 0.0;
						if(er == null) er = 0.0;
						if(examRemuneration == null) examRemuneration = 0.0;
						employeePayHistory.setEr( (ObjectUtils.isNotEmpty(er)) || (ObjectUtils.isNotEmpty(examRemuneration)) ?
										er + examRemuneration : 0);
						/*if (ObjectUtils.isNotEmpty(er)) {)? er + examRemuneration : 0);
						/*if (ObjectUtils.isNotEmpty(er)) {
							totalEarning = (float) (basic + er);
						} else {
							totalEarning = basic;
						}*/
						totalEarning = basic;
						employeePayHistory.setBasic(basic);
						for (SalaryStructureDetails salary : salaryStructureDetails) {
							SalaryStructureHead salaryStructureHead = salaryStructureHeadRepository
									.getSalaryStructureHead(salary.getSalary_structure_head_id());
							salaryhead.put(salaryStructureHead.getPrint_name(),
									salaryStructureHead.getCategory_name_type());
                            calculateComponent(salaryStructureHead, salary, totalEarning, componentMap, employeeDetails, es, masterSalary);
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
						employeePayHistory.setDa((float) Math.round(da));
						employeePayHistory.setHra((float) Math.round(hra));
						employeePayHistory.setPf((float) Math.round(pf1));
						employeePayHistory.setEsi((float) Math.round(esi));
						employeePayHistory.setEsi_contribution_employee((float) Math.round(esic));
						employeePayHistory.setContribution_epf((float) Math.round(pf2));
						employeePayHistory.setSpl_1((float) Math.round(spl1));
						employeePayHistory.setCca((float) Math.round(cca));
						employeePayHistory.setPt((float) Math.round(pt));
						employeePayHistory.setPf_earnings((float) Math.round(pfEarning));
						employeePayHistory.setPension_fund((float) Math.round(pensionFund));
						employeePayHistory.setTa((float) Math.round(ta));
						Float epfDifference = pf1 - pensionFund;

						employeePayHistory.setSchool_id((Integer) employeeDetails.get("school_id"));
						employeePayHistory.setDept_id((Integer) employeeDetails.get("dept_id"));
						employeePayHistory.setEpf_difference(epfDifference);
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
						float lic = 0.0f;
						
						Map<Integer, Double> advanceEmiAmount = advanceMonthlyEmiDeductions.stream()
								.filter(t -> t.getCategory_name().equalsIgnoreCase("Advance"))
								.collect(Collectors.groupingBy(AdvanceMonthlyEmiDeduction::getEmp_id,
										Collectors.summingDouble(AdvanceMonthlyEmiDeduction::getEmi_amount)));

						if (!advanceEmiAmount.isEmpty()) {
							employeePayHistory
									.setAdvance(advanceEmiAmount.entrySet().iterator().next().getValue().floatValue());
							advanceEmi = Math.round(employeePayHistory.getAdvance());
						} else {
							employeePayHistory.setAdvance(advanceEmi);

						}

						Map<Integer, Double> ptaxEmiAmount = advanceMonthlyEmiDeductions.stream()
								.filter(t -> t.getCategory_name().equalsIgnoreCase("Perquisite Tax"))
								.collect(Collectors.groupingBy(AdvanceMonthlyEmiDeduction::getEmp_id,
										Collectors.summingDouble(AdvanceMonthlyEmiDeduction::getEmi_amount)));

						if (!ptaxEmiAmount.isEmpty()) {
							employeePayHistory
									.setPTax(ptaxEmiAmount.entrySet().iterator().next().getValue().floatValue());
							pTaxEmi = Math.round(employeePayHistory.getPTax());
						} else {
							employeePayHistory.setPTax(pTaxEmi);
						}

						Map<Integer, Double> licAmount = advanceMonthlyEmiDeductions.stream()
								.filter(t -> t.getCategory_name().equalsIgnoreCase("LIC"))
								.collect(Collectors.groupingBy(AdvanceMonthlyEmiDeduction::getEmp_id,
										Collectors.summingDouble(AdvanceMonthlyEmiDeduction::getEmi_amount)));

						if (!licAmount.isEmpty()) {
							employeePayHistory
									.setLic(licAmount.entrySet().iterator().next().getValue().floatValue());
							lic = Math.round(employeePayHistory.getLic());
						} else {
							employeePayHistory.setLic(lic);

						}
						if (advanceEmi != 0.0 || pTaxEmi != 0.0 || lic != 0.0) {
							totalDeduction = totalDeduction + advanceEmi + pTaxEmi+lic;
						}

						Long grossLimit =  ObjectUtils.isNotEmpty( componentMap.get("esiGrossLimit"))? (Long) componentMap.get("esiGrossLimit"):0;
						Float esiPercentage = (Float) componentMap.get("esiPercentage");
						float te = Math.round(totalEarning);

						if (grossLimit > masterSalary) {
							esi = (esiPercentage / 100 * te);
							employeePayHistory.setEsi((float) Math.round(esi));
						} else {
							esi = 0.0f;
							employeePayHistory.setEsi((float) Math.round(esi));
						}

						if (componentMap.get("slab_details_id") != null) {
							Integer slabDetailsId = (Integer) componentMap.get("slab_details_id");
							Double totalAmount = 0.0;
							if(er != null) {
								totalAmount = totalEarning + er + examRemuneration;
							}
							SlabStructure slabStructure = slabStructureRepository
									.getSlabStructureDetailsBySlabDetailsId(slabDetailsId, (int) Math.round(totalAmount));

							pt = 0.0f;
							if(employeeDetails.get("pt_status") != null && (Boolean) employeeDetails.get("pt_status")) {
								if (slabStructure.getMin_value() <= totalAmount && slabStructure.getMax_value() >= totalAmount) {
									pt = slabStructure.getHead_value();
									totalDeduction = totalDeduction + pt;
								}
							}
							employeePayHistory.setPt(pt);
						}

						//te = te - esi;
						totalDeduction = totalDeduction + esi;

						if (ObjectUtils.isNotEmpty(tdsDeduction) && ObjectUtils.isNotEmpty(tdsDeduction.getAmount())) {
							totalDeduction = totalDeduction + tdsDeduction.getAmount();
							employeePayHistory.setTds(tdsDeduction.getAmount());
						}
						if (ObjectUtils.isNotEmpty(er)) {
							te = (float) (te + er);
						}
						employeePayHistory.setTotal_earning((float) Math.round(te + examRemuneration));
						float td = Math.round(totalDeduction);
						employeePayHistory.setTotal_deduction(td);
						employeePayHistory.setGross_pay((float) Math.round(te));
						float netpay = (float) ((te + examRemuneration) - (totalDeduction));
						if (netpay < 0.0) {
							netpay = 0.0f;
						}
						float np = Math.round(netpay);
						employeePayHistory.setExamRemuneration(examRemuneration);
						employeePayHistory.setNet_pay(np);
						employeePayHistory.setPay_days(es.getPaydays());
						employeePayHistory.setYear(es.getYear());
						employeePayHistory.setMonth(es.getMonth());
						employeePayHistory.setEmp_id(es.getEmpId());
						employeePayHistory.setRemarks(
								ObjectUtils.isNotEmpty(emiDeduction) && ObjectUtils.isNotEmpty(emiDeduction.getRemark())
										? emiDeduction.getRemark()
										: "");
						employeePayHistoryRepository.save(employeePayHistory);
						System.out.println(empId + " : Pay Sheet");
					}
				}
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	private void calculateComponent(SalaryStructureHead salaryStructureHead,
									SalaryStructureDetails salary, Float basic, Map<String, Object> componentMap,
									Map<String, Object> employeeDetails, EmployeeSheet es, Float masterSalary) {

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
			if (ObjectUtils.isNotEmpty(salary.getIsPayDay()) && salary.getIsPayDay()) {
				da = (float) (ObjectUtils.isNotEmpty(salary.getPercentage())
						? (((salary.getPercentage() / 100) * annualSalary) / es.getTotalDays()) * es.getPaydays()
						: 0.0f);

			} else {
				da = ObjectUtils.isNotEmpty(salary.getPercentage()) ? (salary.getPercentage() / 100) * annualSalary
						: 0.0f;
			}
			componentMap.put("da", ObjectUtils.isNotEmpty(da) ? da : 0);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "hra")) {
			if (ObjectUtils.isNotEmpty(salary.getIsPayDay()) && salary.getIsPayDay()) {
				hra = (float) (ObjectUtils.isNotEmpty(salary.getPercentage())
						? (((salary.getPercentage() / 100) * annualSalary) / es.getTotalDays()) * es.getPaydays()
						: 0.0f);
			} else {
				hra = ObjectUtils.isNotEmpty(salary.getPercentage()) ? (salary.getPercentage() / 100) * annualSalary
						: 0.0f;
			}
			componentMap.put("hra", ObjectUtils.isNotEmpty(hra) ? hra : 0);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "spl_1") && employeeDetails.get("spl_1") != null) {
			if (ObjectUtils.isNotEmpty(salary.getIsPayDay()) && salary.getIsPayDay()) {
				spl_1 = (float) (employeeDetails.get("spl_1") != null
						? ((float) employeeDetails.get("spl_1") / es.getTotalDays()) * es.getPaydays()
						: 0.0f);
			} else {
				spl_1 = employeeDetails.get("spl_1") != null ? (Float) employeeDetails.get("spl_1") : 0.0f;

			}
			componentMap.put("spl_1", spl_1);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "cca")) {

			SlabStructure slabStructure = slabStructureRepository
					.getSlabStructureDetailsBySlabDetailsId(salary.getSlab_details_id(), Math.round(annualSalary));

			Float cca = 0.0f;
			if (slabStructure.getMin_value() < annualSalary && slabStructure.getMax_value() >= annualSalary) {
				if (ObjectUtils.isNotEmpty(salary.getIsPayDay()) && salary.getIsPayDay()) {
					cca = (float) ((slabStructure.getHead_value() / es.getTotalDays()) * es.getPaydays());
				} else {
					cca = slabStructure.getHead_value();
				}
				componentMap.put("cca", cca);
			}

		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "esic")) {

			if(salary.getGross_limit() > masterSalary){
				esic = ObjectUtils.isNotEmpty(salary.getPercentage())
						? (salary.getPercentage() / 100) * (annualSalary + da + hra + spl_1 + ta)
						: 0.0f;
			}
			componentMap.put("esic", ObjectUtils.isNotEmpty(esic) ? esic : 0.0f);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "esi")) {
			componentMap.put("esi", 0.0f);
			componentMap.put("esiGrossLimit",  ObjectUtils.isNotEmpty(salary.getGross_limit()) ? salary.getGross_limit() : 0);
			componentMap.put("esiPercentage",ObjectUtils.isNotEmpty(salary.getPercentage()) ? salary.getPercentage() : 0);
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "pf")) {

			if((employeeDetails.get("pf_status") != null && (Boolean) employeeDetails.get("pf_status"))) {
				if (ObjectUtils.isNotEmpty(salary.getIsPayDay()) && salary.getIsPayDay()) {
					pf1 = (float) (ObjectUtils.isNotEmpty(salary.getPercentage())
							? (((salary.getPercentage() / 100) * (basic + da)) / es.getTotalDays()) * es.getPaydays()
							: 0.0f);
				} else {
					pf1 =  (ObjectUtils.isNotEmpty(salary.getPercentage()) ? (salary.getPercentage() / 100) * (basic + da) : 0.0f);
				}

				if (pf1 > 1800) {
					pf1 = 1800f;
					componentMap.put("pf", ObjectUtils.isNotEmpty(pf1) ? pf1 : 0);
				}else {
					componentMap.put("pf", ObjectUtils.isNotEmpty(pf1) ? pf1 : 0);
				}
			}else {
				componentMap.put("pf", ObjectUtils.isNotEmpty(pf1) ? pf1 : 0);
			}
		}
		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "management_pf")) {
			if((employeeDetails.get("pf_status") != null && (Boolean) employeeDetails.get("pf_status"))) {
				if (ObjectUtils.isNotEmpty(salary.getIsPayDay()) && salary.getIsPayDay()) {
					pf2 = (float) (ObjectUtils.isNotEmpty(salary.getPercentage())
							? (((salary.getPercentage() / 100) * (basic + da)) / es.getTotalDays()) * es.getPaydays()
							: 0.0f);
				} else {
					pf2 = (ObjectUtils.isNotEmpty(salary.getPercentage())
							? (salary.getPercentage() / 100) * (basic + da)
							: 0.0f);
				}
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
			componentMap.put("slab_details_id", salary.getSlab_details_id());
		}
		if (pf1 != 0.0 || pf2 != 0.0) {
			pf = pf1 + pf2;
			componentMap.put("totalpf", ObjectUtils.isNotEmpty(pf) ? pf : 0);
			if (pf <= 3600) {
				Float pensionFund = (float) (8.33f / 100) * 15000;
				Float pfEarning = 15000f;
				componentMap.put("pension_fund", pensionFund);
				componentMap.put("pf_earning", basic + da);
			}
		}

		if (StringUtils.equals(salaryStructureHead.getPrint_name(), "ta")) {
			if (ObjectUtils.isNotEmpty(salary.getIsPayDay()) && salary.getIsPayDay()) {
				ta = (float) (employeeDetails.get("ta") != null
						? ((float) employeeDetails.get("ta") / es.getTotalDays()) * es.getPaydays()
						: 0.0f);

			} else {
				ta = employeeDetails.get("ta") != null ? (Float) employeeDetails.get("ta") : 0.0f;

			}
			componentMap.put("ta", ta);
		}
	}

	private Date getFirstDateOfTheMonth(Integer month, Integer year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month); // Adjust month to be zero-based
		calendar.set(Calendar.DAY_OF_MONTH, 1); // Set day to the first day of the month
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

}
