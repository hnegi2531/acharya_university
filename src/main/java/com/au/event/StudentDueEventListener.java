package com.au.event;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.au.dto.OtherFeeTemplateForStudentDTO;
import com.au.dto.StudentDueEventDTO;
import com.au.model.AcerpAmount;
import com.au.model.FeeTemplate;
import com.au.model.Readmission;
import com.au.model.ScholarshipApprovalStatus;
import com.au.model.StudentDues;
import com.au.repository.AcerpAmountRepository;
import com.au.repository.CmaFeeReceiptRepository;
import com.au.repository.FeeTemplateRepository;
import com.au.repository.OtherFeeDetailsRepository;
import com.au.repository.ReadmissionRepository;
import com.au.repository.ScholarshipApprovalStatusRepository;
import com.au.repository.StudentDueRepository;
import com.au.repository.StudentPaymentHistoryRepository;
import com.au.repository.UniformReceiptRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class StudentDueEventListener {

	@Autowired
	private StudentDueRepository studentDueRepository;

	@Autowired
	private FeeTemplateRepository feeTemplateRepository;

	@Autowired
	private StudentPaymentHistoryRepository studentPaymentHistoryRepository;

	@Autowired
	private ScholarshipApprovalStatusRepository scholarshipApprovalStatusRepository;

	@Autowired
	private AcerpAmountRepository acerpAmountRepository;

	@Autowired
	private OtherFeeDetailsRepository otherFeeDetailsRepository;

	@Autowired
	private ReadmissionRepository readmissionRepository;
	
	@Autowired
	private CmaFeeReceiptRepository cmaFeeReceiptRepository;
	
	@Autowired
	private UniformReceiptRepository uniformReceiptRepository;

	@Async
	@EventListener
	public void handleStudentDueEvent(StudentDueEvent studentDueEvent) {

		try {
			List<StudentDueEventDTO> studentDueEventDTOs = null;

			if (ObjectUtils.isNotEmpty(studentDueEvent.getSchoolId())) {
				studentDueEventDTOs = studentDueRepository
						.getStudentDueEventDTODetailsBySchoolId(studentDueEvent.getSchoolId());
			} else if (ObjectUtils.isNotEmpty(studentDueEvent.getProgramId())) {
				studentDueEventDTOs = studentDueRepository
						.getStudentDueEventDTODetailsByProgramId(studentDueEvent.getProgramId());
			} else if (ObjectUtils.isNotEmpty(studentDueEvent.getStudentId())) {
				studentDueEventDTOs = studentDueRepository
						.getStudentDueEventDTODetailsByStudentId(studentDueEvent.getStudentId());
			} else {
				studentDueEventDTOs = studentDueRepository.getStudentDueEventDTODetails();
			}

			if (ObjectUtils.isNotEmpty(studentDueEventDTOs)) {

				studentDueEventDTOs.stream().forEach(std -> {

					Float sem1Fixed = 0f;

					Float sem1Paid = 0f;

					Float sem1Due = 0f;

					Float sem1Scholarship = 0f;

					Float sem1TuitionFee = 0f;

					Float sem2Fixed = 0f;

					Float sem2Paid = 0f;

					Float sem2Due = 0f;

					Float sem2Scholarship = 0f;

					Float sem2TuitionFee = 0f;

					Float sem3Fixed = 0f;

					Float sem3Paid = 0f;

					Float sem3Due = 0f;

					Float sem3Scholarship = 0f;

					Float sem3TuitionFee = 0f;

					Float sem4Fixed = 0f;

					Float sem4Paid = 0f;

					Float sem4Due = 0f;

					Float sem4Scholarship = 0f;

					Float sem4TuitionFee = 0f;

					Float sem5Fixed = 0f;

					Float sem5Paid = 0f;

					Float sem5Due = 0f;

					Float sem5Scholarship = 0f;

					Float sem5TuitionFee = 0f;

					Float sem6Fixed = 0f;

					Float sem6Paid = 0f;

					Float sem6Due = 0f;

					Float sem6Scholarship = 0f;

					Float sem6TuitionFee = 0f;

					Float sem7Fixed = 0f;

					Float sem7Paid = 0f;

					Float sem7Due = 0f;

					Float sem7Scholarship = 0f;

					Float sem7TuitionFee = 0f;

					Float sem8Fixed = 0f;

					Float sem8Paid = 0f;

					Float sem8Due = 0f;

					Float sem8Scholarship = 0f;

					Float sem8TuitionFee = 0f;

					Float sem9Fixed = 0f;

					Float sem9Paid = 0f;

					Float sem9Due = 0f;

					Float sem9Scholarship = 0f;

					Float sem9TuitionFee = 0f;

					Float sem10Fixed = 0f;

					Float sem10Paid = 0f;

					Float sem10Due = 0f;

					Float sem10Scholarship = 0f;

					Float sem10TuitionFee = 0f;

					Float sem11Fixed = 0f;

					Float sem11Paid = 0f;

					Float sem11Due = 0f;

					Float sem11Scholarship = 0f;
					Float sem11TuitionFee = 0f;

					Float sem12Fixed = 0f;

					Float sem12Paid = 0f;

					Float sem12Due = 0f;

					Float sem12Scholarship = 0f;

					Float sem12TuitionFee = 0f;

					Float sem1AddOn = 0f;

					Float sem2AddOn = 0f;

					Float sem3AddOn = 0f;

					Float sem4AddOn = 0f;

					Float sem5AddOn = 0f;

					Float sem6AddOn = 0f;

					Float sem7AddOn = 0f;

					Float sem8AddOn = 0f;

					Float sem9AddOn = 0f;

					Float sem10AddOn = 0f;
					Float sem11AddOn = 0f;

					Float sem12AddOn = 0f;

					Float sem1Waiver = 0f;

					Float sem2Waiver = 0f;

					Float sem3Waiver = 0f;

					Float sem4Waiver = 0f;

					Float sem5Waiver = 0f;

					Float sem6Waiver = 0f;

					Float sem7Waiver = 0f;

					Float sem8Waiver = 0f;

					Float sem9Waiver = 0f;

					Float sem10Waiver = 0f;
					Float sem11Waiver = 0f;

					Float sem12Waiver = 0f;

					Float sem1FeePaid = 0f;

					Float sem2FeePaid = 0f;

					Float sem3FeePaid = 0f;

					Float sem4FeePaid = 0f;

					Float sem5FeePaid = 0f;

					Float sem6FeePaid = 0f;

					Float sem7FeePaid = 0f;

					Float sem8FeePaid = 0f;

					Float sem9FeePaid = 0f;

					Float sem10FeePaid = 0f;
					Float sem11FeePaid = 0f;

					Float sem12FeePaid = 0f;

					Float sem1SumAddOn = 0f;

					Float sem2SumAddOn = 0f;

					Float sem3SumAddOn = 0f;

					Float sem4SumAddOn = 0f;

					Float sem5SumAddOn = 0f;

					Float sem6SumAddOn = 0f;

					Float sem7SumAddOn = 0f;

					Float sem8SumAddOn = 0f;

					Float sem9SumAddOn = 0f;
					Float sem10SumAddOn = 0f;

					Float sem11SumAddOn = 0f;
					Float sem12SumAddOn = 0f;

					Float sem1SumUniform = 0f;

					Float sem2SumUniform = 0f;

					Float sem3SumUniform = 0f;

					Float sem4SumUniform = 0f;

					Float sem5SumUniform = 0f;

					Float sem6SumUniform = 0f;

					Float sem7SumUniform = 0f;

					Float sem8SumUniform = 0f;

					Float sem9SumUniform = 0f;
					Float sem10SumUniform = 0f;

					Float sem11SumUniform = 0f;
					Float sem12SumUniform = 0f;

					Float totalDue = 0f;

					Float totalFix = 0f;

					Float totalPaid = 0f;

					Float totalGrant = 0f;

					Float totalAddOn = 0f;

					
					Float sem1ReadmissionFixed = 0f;
					Float sem2ReadmissionFixed = 0f;
					Float sem3ReadmissionFixed = 0f;
					Float sem4ReadmissionFixed = 0f;
					Float sem5ReadmissionFixed = 0f;
					Float sem6ReadmissionFixed = 0f;
					Float sem7ReadmissionFixed = 0f;
					Float sem8ReadmissionFixed = 0f;
					Float sem9ReadmissionFixed = 0f;
					Float sem10ReadmissionFixed = 0f;
					Float sem11ReadmissionFixed = 0f;
					Float sem12ReadmissionFixed = 0f;
					
					Float sem1AddonPaid = 0f;
					Float sem2AddonPaid = 0f;
					Float sem3AddonPaid = 0f;
					Float sem4AddonPaid = 0f;
					Float sem5AddonPaid = 0f;
					Float sem6AddonPaid = 0f;
					Float sem7AddonPaid = 0f;
					Float sem8AddonPaid = 0f;
					Float sem9AddonPaid = 0f;
					Float sem10AddonPaid = 0f;
					Float sem11AddonPaid = 0f;
					Float sem12AddonPaid = 0f;
					
					Float sem1UniformPaid = 0f;
					Float sem2UniformPaid = 0f;
					Float sem3UniformPaid = 0f;
					Float sem4UniformPaid = 0f;
					Float sem5UniformPaid = 0f;
					Float sem6UniformPaid = 0f;
					Float sem7UniformPaid = 0f;
					Float sem8UniformPaid = 0f;
					Float sem9UniformPaid = 0f;
					Float sem10UniformPaid = 0f;
					Float sem11UniformPaid = 0f;
					Float sem12UniformPaid = 0f;
					Double swoSem1 = 0d;
					Double swoSemWise = 0d;
					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(std.getStudentId());
					
					Readmission readmission = readmissionRepository.getReadmittedStudent(std.getStudentId());

					OtherFeeTemplateForStudentDTO otherFeeDetailsAddOn = otherFeeDetailsRepository
							.getAddOnProgramFeeDetailsByFeeTemplateId(std.getSchoolId(), std.getAcYearId(),
									std.getProgramId(), std.getFeeTemplateId());
					OtherFeeTemplateForStudentDTO otherFeeDetailsUniform = otherFeeDetailsRepository
							.getUniformFeeDetails(std.getSchoolId(), std.getAcYearId(), std.getProgramId(),
									std.getProgramSpecializationId());

					Map<String,Object>scholarshipApprovalStatus = scholarshipApprovalStatusRepository
							.getApprovedScholarShipbyStudentId(std.getStudentId());
					if (std.getCurrentSem() >= 1 || std.getCurrentYear() >= 1) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSem1 = feeTemplateRepository.getSwoSem1WithOutBoard(feeTemplate.getFee_template_id());
						}else {
							swoSem1 = feeTemplateRepository.getSwoSem1(feeTemplate.getFee_template_id());
						}
						sem1Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSem1)
							        ? (swoSem1 > 0 ? swoSem1
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year1_amt()) && feeTemplate.getFee_year1_amt() > 0
							                ? feeTemplate.getFee_year1_amt()
							                : 0f))
							        : 0f
							);
						sem1Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(1, std.getStudentId());
						sem1ReadmissionFixed = getReadmissionAmount(readmission, sem1Fixed, 1);
						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear1()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem1Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem1FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear1())
//										? acerpAmountFeePaid.getPaidYear1()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						sem1Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year1"))
								? ((Number) scholarshipApprovalStatus.get("year1")).floatValue()
								: 0f;

						 Double addOnPaid=getAddOnPaid(1,std.getStudentId());
						 sem1AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(1,std.getStudentId());
						 sem1UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						sem1Paid = (sem1Paid != null) ? sem1Paid : 0f;
						sem1TuitionFee = (sem1FeePaid != null) || (sem1Waiver != null) ? sem1FeePaid + sem1Waiver : 0f;
						sem1Scholarship = (sem1Scholarship != null) ? sem1Scholarship : 0f;
						sem1SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem1())
										? otherFeeDetailsAddOn.getSem1()
										: 0f);
						sem1SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem1())
										? otherFeeDetailsUniform.getSem1()
										: 0f);
						sem1AddOn = (sem1SumAddOn + sem1SumUniform) - sem1AddonPaid -sem1UniformPaid;
						sem1Due = ObjectUtils.isEmpty(readmission) 
								? sem1Fixed - sem1Scholarship - sem1TuitionFee - sem1Paid
								: sem1ReadmissionFixed - sem1Scholarship - sem1TuitionFee - sem1Paid;
						
					}

					if (std.getCurrentSem() >= 2 || std.getCurrentYear() >= 2) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),2);
						}else {
							if(feeTemplate.getLat_year_sem() == 2 )
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 2);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 2);
							}
						}
						sem2Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year2_amt()) && feeTemplate.getFee_year2_amt() > 0
							                ? feeTemplate.getFee_year2_amt()
							                : 0f))
							        : 0f
							);
						sem2Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(2, std.getStudentId());
						sem2ReadmissionFixed = getReadmissionAmount(readmission, sem2Fixed, 2);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear2()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem2Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem2FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear2())
//										? acerpAmountFeePaid.getPaidYear2()
//										: 0f;
//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						
						 Double addOnPaid=getAddOnPaid(2,std.getStudentId());
						 sem2AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(2,std.getStudentId());
						 sem2UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						sem2Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year2"))
								? ((Number) scholarshipApprovalStatus.get("year2")).floatValue()
								: 0f;

						sem2Paid = (sem2Paid != null) ? sem2Paid : 0f;
						sem2TuitionFee = (sem2FeePaid != null) || (sem2Waiver != null) ? sem2FeePaid + sem2Waiver : 0f;
						sem2Scholarship = (sem2Scholarship != null) ? sem2Scholarship : 0f;
						sem2SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem2())
										? otherFeeDetailsAddOn.getSem2()
										: 0f);
						sem2SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem2())
										? otherFeeDetailsUniform.getSem2()
										: 0f);
						sem2AddOn = (sem2SumAddOn + sem2SumUniform) -  sem2AddonPaid -sem2UniformPaid;
						sem2Due = ObjectUtils.isEmpty(readmission)
								? sem2Fixed - sem2Scholarship - sem2TuitionFee - sem2Paid
								: sem2ReadmissionFixed - sem2Scholarship - sem2TuitionFee - sem2Paid;
						

					}

					if (std.getCurrentSem() >= 3 || std.getCurrentYear() >= 3) {if(!feeTemplate.getIs_paid_at_board())
					{
						swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),3);
					}else {
						if(feeTemplate.getLat_year_sem() == 3)
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 3);
						}
						else {
							swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 3);
						}
					}
					//	swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 3);
						sem3Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year3_amt()) && feeTemplate.getFee_year3_amt() > 0
							                ? feeTemplate.getFee_year3_amt()
							                : 0f))
							        : 0f
							);
						sem3Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(3, std.getStudentId());
						sem3ReadmissionFixed = getReadmissionAmount(readmission, sem3Fixed, 3);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear3()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem3Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem3FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear3())
//										? acerpAmountFeePaid.getPaidYear3()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						 Double addOnPaid=getAddOnPaid(3,std.getStudentId());
						 sem3AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(3,std.getStudentId());
						 sem3UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						
						sem3Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year3"))
								? ((Number) scholarshipApprovalStatus.get("year3")).floatValue()
								: 0f;

						sem3Paid = (sem3Paid != null) ? sem3Paid : 0f;
						sem3TuitionFee = (sem3FeePaid != null) || (sem3Waiver != null) ? sem3FeePaid + sem3Waiver : 0f;
						sem3Scholarship = (sem3Scholarship != null) ? sem3Scholarship : 0f;
						sem3SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem3())
										? otherFeeDetailsAddOn.getSem3()
										: 0f);
						sem3SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem3())
										? otherFeeDetailsUniform.getSem3()
										: 0f);
						sem3AddOn = (sem3SumAddOn + sem3SumUniform) -  sem3AddonPaid -sem3UniformPaid;
						sem3Due = ObjectUtils.isEmpty(readmission) 
								? sem3Fixed - sem3Scholarship - sem3TuitionFee - sem3Paid
								: sem3ReadmissionFixed - sem3Scholarship - sem3TuitionFee - sem3Paid;
						

					}

					if (std.getCurrentSem() >= 4 || std.getCurrentYear() >= 4) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),4);
						}else {
							if(feeTemplate.getLat_year_sem() == 4)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 4);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 4);
							}
						}
					//	swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 4);
						
						sem4Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year4_amt()) && feeTemplate.getFee_year4_amt() > 0
							                ? feeTemplate.getFee_year4_amt()
							                : 0f))
							        : 0f
							);
						sem4Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(4, std.getStudentId());
						sem4ReadmissionFixed = getReadmissionAmount(readmission, sem4Fixed, 4);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear4()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem4Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem4FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear4())
//										? acerpAmountFeePaid.getPaidYear4()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						 Double addOnPaid=getAddOnPaid(4,std.getStudentId());
						 sem4AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(4,std.getStudentId());
						 sem4UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						
						sem4Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year4"))
								? ((Number) scholarshipApprovalStatus.get("year4")).floatValue()
								: 0f;

						sem4Paid = (sem4Paid != null) ? sem4Paid : 0f;
						sem4TuitionFee = (sem4FeePaid != null) || (sem4Waiver != null) ? sem4FeePaid + sem4Waiver : 0f;
						sem4Scholarship = (sem4Scholarship != null) ? sem4Scholarship : 0f;
						sem4SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem4())
										? otherFeeDetailsAddOn.getSem4()
										: 0f);
						sem4SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem4())
										? otherFeeDetailsUniform.getSem4()
										: 0f);
						sem4AddOn = (sem4SumAddOn + sem4SumUniform) - sem4AddonPaid -sem4UniformPaid;
						sem4Due = ObjectUtils.isEmpty(readmission) 
								? sem4Fixed - sem4Scholarship - sem4TuitionFee - sem4Paid
								: sem4ReadmissionFixed - sem4Scholarship - sem4TuitionFee - sem4Paid;
						

					}

					if (std.getCurrentSem() >= 5 || std.getCurrentYear() >= 5) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),5);
						}else {
							if(feeTemplate.getLat_year_sem() == 5)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 5);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 5);
							}
						}
					//	swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 5);
						
						sem5Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year5_amt()) && feeTemplate.getFee_year5_amt() > 0
							                ? feeTemplate.getFee_year5_amt()
							                : 0f))
							        : 0f
							);
						sem5Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(5, std.getStudentId());
						sem5ReadmissionFixed = getReadmissionAmount(readmission, sem5Fixed, 5);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear5()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem5Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);


//						sem5FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear5())
//										? acerpAmountFeePaid.getPaidYear5()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						
						 Double addOnPaid=getAddOnPaid(5,std.getStudentId());
						 sem5AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(5,std.getStudentId());
						 sem5UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						sem5Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year5"))
								? ((Number) scholarshipApprovalStatus.get("year5")).floatValue()
								: 0f;

						sem5Paid = (sem5Paid != null) ? sem5Paid : 0f;
						sem5TuitionFee = (sem5FeePaid != null) || (sem5Waiver != null) ? sem5FeePaid + sem5Waiver : 0f;
						sem5Scholarship = (sem5Scholarship != null) ? sem5Scholarship : 0f;
						sem5SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem5())
										? otherFeeDetailsAddOn.getSem5()
										: 0f);
						sem5SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem5())
										? otherFeeDetailsUniform.getSem5()
										: 0f);
						sem5AddOn = (sem5SumAddOn + sem5SumUniform) - sem5AddonPaid -sem5UniformPaid;
						sem5Due = ObjectUtils.isEmpty(readmission) 
								? sem5Fixed - sem5Scholarship - sem5TuitionFee - sem5Paid
								: sem5ReadmissionFixed - sem5Scholarship - sem5TuitionFee - sem5Paid;
						

					}

					if (std.getCurrentSem() >= 6 || std.getCurrentYear() >= 6) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),6);
						}else {
							if(feeTemplate.getLat_year_sem() == 6)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 6);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 6);
							}
						}
						//swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 6);
						
						sem6Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year6_amt()) && feeTemplate.getFee_year6_amt() > 0
							                ? feeTemplate.getFee_year6_amt()
							                : 0f))
							        : 0f
							);
						sem6Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(6, std.getStudentId());
						sem6ReadmissionFixed = getReadmissionAmount(readmission, sem6Fixed, 6);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear6()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem6Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem6FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear6())
//										? acerpAmountFeePaid.getPaidYear6()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						 Double addOnPaid=getAddOnPaid(6,std.getStudentId());
						 sem6AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(6,std.getStudentId());
						 sem6UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						
						sem6Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year6"))
								? ((Number) scholarshipApprovalStatus.get("year6")).floatValue()
								: 0f;

						sem6Paid = (sem6Paid != null) ? sem6Paid : 0f;
						sem6TuitionFee = (sem6FeePaid != null) || (sem6Waiver != null) ? sem6FeePaid + sem6Waiver : 0f;
						sem6Scholarship = (sem6Scholarship != null) ? sem6Scholarship : 0f;
						sem6SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem6())
										? otherFeeDetailsAddOn.getSem6()
										: 0f);
						sem6SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem6())
										? otherFeeDetailsUniform.getSem6()
										: 0f);
						sem6AddOn = (sem6SumAddOn + sem6SumUniform) - sem6AddonPaid -sem6UniformPaid;

						sem6Due = ObjectUtils.isEmpty(readmission)
								? sem6Fixed - sem6Scholarship - sem6TuitionFee - sem6Paid
								: sem6ReadmissionFixed - sem6Scholarship - sem6TuitionFee - sem6Paid;
						

					}

					if (std.getCurrentSem() >= 7 || std.getCurrentYear() >= 7) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),7);
						}else {
							if(feeTemplate.getLat_year_sem() == 7)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 7);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 7);
							}
						}
						//swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 7);
						
						sem7Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year7_amt()) && feeTemplate.getFee_year7_amt() > 0
							                ? feeTemplate.getFee_year7_amt()
							                : 0f))
							        : 0f
							);
						sem7Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(7, std.getStudentId());
						sem7ReadmissionFixed = getReadmissionAmount(readmission, sem7Fixed, 7);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear7()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem7Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem7FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear7())
//										? acerpAmountFeePaid.getPaidYear7()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						 Double addOnPaid=getAddOnPaid(7,std.getStudentId());
						 sem7AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(7,std.getStudentId());
						 sem7UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						
						sem7Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year7"))
								? ((Number) scholarshipApprovalStatus.get("year7")).floatValue()
								: 0f;

						sem7Paid = (sem7Paid != null) ? sem7Paid : 0f;
						sem7TuitionFee = (sem7FeePaid != null) || (sem7Waiver != null) ? sem7FeePaid + sem7Waiver : 0f;
						sem7Scholarship = (sem7Scholarship != null) ? sem7Scholarship : 0f;
						sem7SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem7())
										? otherFeeDetailsAddOn.getSem7()
										: 0f);
						sem7SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem7())
										? otherFeeDetailsUniform.getSem7()
										: 0f);
						sem7AddOn = (sem7SumAddOn + sem7SumUniform) - sem7AddonPaid -sem7UniformPaid;
						sem7Due = ObjectUtils.isEmpty(readmission) 
								? sem7Fixed - sem7Scholarship - sem7TuitionFee - sem7Paid
								: sem7ReadmissionFixed - sem7Scholarship - sem7TuitionFee - sem7Paid;
						

					}

					if (std.getCurrentSem() >= 8 || std.getCurrentYear() >= 8) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),8);
						}else {
							if(feeTemplate.getLat_year_sem() == 8)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 8);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 8);
							}
						}
						//swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 8);
						
						sem8Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year8_amt()) && feeTemplate.getFee_year8_amt() > 0
							                ? feeTemplate.getFee_year8_amt()
							                : 0f))
							        : 0f
							);
						sem8Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(8, std.getStudentId());
						sem8ReadmissionFixed = getReadmissionAmount(readmission, sem8Fixed, 8);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear8()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem8Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem8FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear8())
//										? acerpAmountFeePaid.getPaidYear8()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						
						 Double addOnPaid=getAddOnPaid(8,std.getStudentId());
						 sem8AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(8,std.getStudentId());
						 sem8UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						sem8Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year8"))
								? ((Number) scholarshipApprovalStatus.get("year8")).floatValue()
								: 0f;

						sem8Paid = (sem8Paid != null) ? sem8Paid : 0f;
						sem8TuitionFee = (sem8FeePaid != null) || (sem8Waiver != null) ? sem8FeePaid + sem8Waiver : 0f;
						sem8Scholarship = (sem8Scholarship != null) ? sem8Scholarship : 0f;
						sem8SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem8())
										? otherFeeDetailsAddOn.getSem8()
										: 0f);
						sem8SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem8())
										? otherFeeDetailsUniform.getSem8()
										: 0f);
						sem8AddOn = (sem8SumAddOn + sem8SumUniform) - sem8AddonPaid -sem8UniformPaid;
						sem8Due = ObjectUtils.isEmpty(readmission) 
								? sem8Fixed - sem8Scholarship - sem8TuitionFee - sem8Paid
								: sem8ReadmissionFixed - sem8Scholarship - sem8TuitionFee - sem8Paid;
						

					}

					if (std.getCurrentSem() >= 9 || std.getCurrentYear() >= 9) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),9);
						}else {
							if(feeTemplate.getLat_year_sem() == 9)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 9);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 9);
							}
						}
					//	swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 9);
						
						sem9Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year9_amt()) && feeTemplate.getFee_year9_amt() > 0
							                ? feeTemplate.getFee_year9_amt()
							                : 0f))
							        : 0f
							);
						sem9Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(9, std.getStudentId());
						sem9ReadmissionFixed = getReadmissionAmount(readmission, sem9Fixed, 9);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear9()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem9Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem9FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear9())
//										? acerpAmountFeePaid.getPaidYear9()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						 Double addOnPaid=getAddOnPaid(9,std.getStudentId());
						 sem9AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(9,std.getStudentId());
						 sem9UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						
						sem9Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year9"))
								? ((Number) scholarshipApprovalStatus.get("year9")).floatValue()
								: 0f;

						sem9Paid = (sem9Paid != null) ? sem9Paid : 0f;
						sem9TuitionFee = (sem9FeePaid != null) || (sem9Waiver != null) ? sem9FeePaid + sem9Waiver : 0f;
						sem9Scholarship = (sem9Scholarship != null) ? sem9Scholarship : 0f;
						sem9SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem9())
										? otherFeeDetailsAddOn.getSem9()
										: 0f);
						sem9SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem9())
										? otherFeeDetailsUniform.getSem9()
										: 0f);
						sem9AddOn = (sem9SumAddOn + sem9SumUniform) - sem9AddonPaid -sem9UniformPaid;

						sem9Due = ObjectUtils.isEmpty(readmission) 
								? sem9Fixed - sem9Scholarship - sem9TuitionFee -   sem9AddonPaid -sem9UniformPaid
								: sem9ReadmissionFixed - sem9Scholarship - sem9TuitionFee - sem9Paid;
						

					}

					if (std.getCurrentSem() >= 10 || std.getCurrentYear() >= 10) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),10);
						}else {
							if(feeTemplate.getLat_year_sem() == 10)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 10);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 10);
							}
						}
						//swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 10);
						
						sem10Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year10_amt()) && feeTemplate.getFee_year10_amt() > 0
							                ? feeTemplate.getFee_year10_amt()
							                : 0f))
							        : 0f
							);
						sem10Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(10, std.getStudentId());
						sem10ReadmissionFixed = getReadmissionAmount(readmission, sem10Fixed, 10);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear10()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem10Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem10FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear10())
//										? acerpAmountFeePaid.getPaidYear10()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						 Double addOnPaid=getAddOnPaid(10,std.getStudentId());
						 sem10AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(10,std.getStudentId());
						 sem10UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						
						sem10Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year10"))
								? ((Number) scholarshipApprovalStatus.get("year10")).floatValue()
								: 0f;
						sem10Paid = (sem10Paid != null) ? sem10Paid : 0f;
						sem10TuitionFee = (sem10FeePaid != null) || (sem10Waiver != null) ? sem10FeePaid + sem10Waiver
								: 0f;
						sem10Scholarship = (sem10Scholarship != null) ? sem10Scholarship : 0f;
						sem10SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem10())
										? otherFeeDetailsAddOn.getSem10()
										: 0f);
						sem10SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem10())
										? otherFeeDetailsUniform.getSem10()
										: 0f);
						sem10AddOn = (sem10SumAddOn + sem10SumUniform) -  sem10AddonPaid -sem10UniformPaid;
						sem10Due = ObjectUtils.isEmpty(readmission) 
								? sem10Fixed - sem10Scholarship - sem10TuitionFee - sem10Paid
								: sem10ReadmissionFixed - sem10Scholarship - sem10TuitionFee - sem10Paid;
					

					}

					if (std.getCurrentSem() >= 11 || std.getCurrentYear() >= 11) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),11);
						}else {
							if(feeTemplate.getLat_year_sem() == 11)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 11);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 11);
							}
						}
					//	swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 11);
						
						sem11Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year11_amt()) && feeTemplate.getFee_year11_amt() > 0
							                ? feeTemplate.getFee_year11_amt()
							                : 0f))
							        : 0f
							);
						sem11Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(11, std.getStudentId());
						sem11ReadmissionFixed = getReadmissionAmount(readmission, sem11Fixed, 11);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear11()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem11Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem11FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear11())
//										? acerpAmountFeePaid.getPaidYear11()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						 Double addOnPaid=getAddOnPaid(11,std.getStudentId());
						 sem11AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(11,std.getStudentId());
						 sem11UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						
						sem11Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year11"))
								? ((Number) scholarshipApprovalStatus.get("year11")).floatValue()
								: 0f;
						sem11Paid = (sem11Paid != null) ? sem11Paid : 0f;
						sem11TuitionFee = (sem11FeePaid != null) || (sem11Waiver != null) ? sem11FeePaid + sem11Waiver
								: 0f;
						sem11Scholarship = (sem11Scholarship != null) ? sem11Scholarship : 0f;
						sem11SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem11())
										? otherFeeDetailsAddOn.getSem11()
										: 0f);
						sem11SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem11())
										? otherFeeDetailsUniform.getSem11()
										: 0f);
						sem11AddOn = (sem11SumAddOn + sem11SumUniform) -  sem11AddonPaid -sem11UniformPaid;
						sem11Due = ObjectUtils.isEmpty(readmission) 
								? sem11Fixed - sem11Scholarship - sem11TuitionFee - sem11Paid
								: sem11ReadmissionFixed - sem11Scholarship - sem11TuitionFee - sem11Paid;
						

					}

					if (std.getCurrentSem() >= 12 || std.getCurrentYear() >= 12) {
						if(!feeTemplate.getIs_paid_at_board())
						{
							swoSemWise = feeTemplateRepository.getSwoSemWiseWithOutBoard(feeTemplate.getFee_template_id(),12);
						}else {
							if(feeTemplate.getLat_year_sem() == 12)
							{
								swoSemWise = feeTemplateRepository.getSwoSemWiseForLateralYear(feeTemplate.getFee_template_id(), 12);
							}
							else {
								swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 12);
							}
						}
					//	swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 12);
						
						sem12Fixed = (float) (
							    ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
							        ? (swoSemWise > 0 ? swoSemWise
							            : (ObjectUtils.isNotEmpty(feeTemplate.getFee_year12_amt()) && feeTemplate.getFee_year12_amt() > 0
							                ? feeTemplate.getFee_year12_amt()
							                : 0f))
							        : 0f
							);
						sem12Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(12, std.getStudentId());
						sem12ReadmissionFixed = getReadmissionAmount(readmission, sem12Fixed, 12);

						List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
								.getDataByStudentIdForWaiver(std.getStudentId());
					Double waiverSum	=acerpAmountWaiver.stream().mapToDouble(t->t.getPaidYear12()).sum();
//						AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//								.getDataByStudentIdForFeePaid(std.getStudentId());

						sem12Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
								&& ObjectUtils.isNotEmpty(waiverSum)
										? waiverSum
										: 0f);

//						sem12FeePaid = ObjectUtils.isNotEmpty(acerpAmountFeePaid)
//								&& ObjectUtils.isNotEmpty(acerpAmountFeePaid.getPaidYear12())
//										? acerpAmountFeePaid.getPaidYear12()
//										: 0f;

//						ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//								.getApprovedScholarShipbyYearAndStudentId(std.getStudentId());
						 Double addOnPaid=getAddOnPaid(12,std.getStudentId());
						 sem12AddonPaid=ObjectUtils.isNotEmpty(addOnPaid)?addOnPaid.floatValue():0.0f;
						 Double uniformPaid=getUniformPaid(12,std.getStudentId());
						 sem12UniformPaid=ObjectUtils.isNotEmpty(uniformPaid)?uniformPaid.floatValue():0.0f;
						
						
						sem12Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
								&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year12"))
								? ((Number) scholarshipApprovalStatus.get("year12")).floatValue()
								: 0f;

						sem12Paid = (sem12Paid != null) ? sem12Paid : 0f;
						sem12TuitionFee = (sem12FeePaid != null) || (sem12Waiver != null) ? sem12FeePaid + sem12Waiver
								: 0f;
						sem12Scholarship = (sem12Scholarship != null) ? sem12Scholarship : 0f;
						sem12SumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem12())
										? otherFeeDetailsAddOn.getSem12()
										: 0f);
						sem12SumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
								&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem12())
										? otherFeeDetailsUniform.getSem12()
										: 0f);
						sem12AddOn = (sem12SumAddOn + sem12SumUniform) - sem12AddonPaid -sem12UniformPaid;
						sem12Due = ObjectUtils.isEmpty(readmission) 
								? sem12Fixed - sem12Scholarship - sem12TuitionFee - sem12Paid
								: sem12ReadmissionFixed - sem12Scholarship - sem12TuitionFee - sem12Paid;
						

					}

					totalDue = sem1Due + sem2Due + sem3Due + sem4Due + sem5Due + sem6Due + sem7Due + sem8Due + sem9Due
							+ sem10Due + sem11Due + sem12Due;
					totalFix = sem1Fixed + sem2Fixed + sem3Fixed + sem4Fixed + sem5Fixed + sem6Fixed + sem7Fixed
							+ sem8Fixed + sem9Fixed + sem10Fixed + sem11Fixed + sem12Fixed;
					totalGrant = sem1Scholarship + sem2Scholarship + sem3Scholarship + sem4Scholarship + sem5Scholarship
							+ sem6Scholarship + sem7Scholarship + sem8Scholarship + sem9Scholarship + sem10Scholarship
							+ sem11Scholarship + sem12Scholarship;
					totalPaid = sem1Paid + sem2Paid + sem3Paid + sem4Paid + sem5Paid + sem6Paid + sem7Paid + sem8Paid
							+ sem9Paid + sem10Paid + sem11Paid + sem12Paid;
					totalAddOn = sem1AddOn + sem2AddOn + sem3AddOn + sem4AddOn + sem5AddOn + sem6AddOn + sem7AddOn
							+ sem8AddOn + sem9AddOn + sem10AddOn + sem11AddOn + sem12AddOn;

					StudentDues studentDues = studentDueRepository.getBySId(std.getStudentId());

					if (ObjectUtils.isEmpty(studentDues)) {
						studentDues = new StudentDues();
					}

					studentDues.setStudentId(std.getStudentId());
					studentDues.setCategoryId(std.getFeeAdmissionCatgoryId());
					studentDues.setProgramId(std.getProgramId());
					studentDues.setProgramSpecializationId(std.getProgramSpecializationId());
					studentDues.setSchoolId(std.getSchoolId());
					studentDues.setTotalDue(totalDue);
					studentDues.setTotalFix(totalFix);
					studentDues.setTotalPaid(totalPaid);
					studentDues.setTotalGrant(totalGrant);
					studentDues.setTotalAddOn(totalAddOn);
					studentDues.setS1waivr(sem1TuitionFee);
					studentDues.setS2waivr(sem2TuitionFee);
					studentDues.setS3waivr(sem3TuitionFee);
					studentDues.setS4waivr(sem4TuitionFee);
					studentDues.setS5waivr(sem5TuitionFee);
					studentDues.setS6waivr(sem6TuitionFee);
					studentDues.setS7waivr(sem7TuitionFee);
					studentDues.setS8waivr(sem8TuitionFee);
					studentDues.setS9waivr(sem9TuitionFee);
					studentDues.setS10waivr(sem10TuitionFee);
					studentDues.setS11waivr(sem11TuitionFee);
					studentDues.setS12waivr(sem12TuitionFee);
					studentDues.setS1sch(sem1Scholarship);
					studentDues.setS2sch(sem2Scholarship);
					studentDues.setS3sch(sem3Scholarship);
					studentDues.setS4sch(sem4Scholarship);
					studentDues.setS5sch(sem5Scholarship);
					studentDues.setS6sch(sem6Scholarship);
					studentDues.setS7sch(sem7Scholarship);
					studentDues.setS8sch(sem8Scholarship);
					studentDues.setS9sch(sem9Scholarship);
					studentDues.setS10sch(sem10Scholarship);
					studentDues.setS11sch(sem11Scholarship);
					studentDues.setS12sch(sem12Scholarship);
					studentDues.setS1paid(sem1Paid);
					studentDues.setS2paid(sem2Paid);
					studentDues.setS3paid(sem3Paid);
					studentDues.setS4paid(sem4Paid);
					studentDues.setS5paid(sem5Paid);
					studentDues.setS6paid(sem6Paid);
					studentDues.setS7paid(sem7Paid);
					studentDues.setS8paid(sem8Paid);
					studentDues.setS9paid(sem9Paid);
					studentDues.setS10paid(sem10Paid);
					studentDues.setS11paid(sem11Paid);
					studentDues.setS12paid(sem12Paid);

					studentDues.setS1fxd(ObjectUtils.isEmpty(readmission)?sem1Fixed:sem1ReadmissionFixed);

					studentDues.setS2fxd(ObjectUtils.isEmpty(readmission)?sem2Fixed:sem2ReadmissionFixed);

					studentDues.setS3fxd(ObjectUtils.isEmpty(readmission)?sem3Fixed:sem3ReadmissionFixed);

					studentDues.setS4fxd(ObjectUtils.isEmpty(readmission)?sem4Fixed:sem4ReadmissionFixed);

					studentDues.setS5fxd(ObjectUtils.isEmpty(readmission)?sem5Fixed:sem5ReadmissionFixed);

					studentDues.setS6fxd(ObjectUtils.isEmpty(readmission)?sem6Fixed:sem6ReadmissionFixed);

					studentDues.setS7fxd(ObjectUtils.isEmpty(readmission)?sem7Fixed:sem7ReadmissionFixed);

					studentDues.setS8fxd(ObjectUtils.isEmpty(readmission)?sem8Fixed:sem8ReadmissionFixed);

					studentDues.setS9fxd(ObjectUtils.isEmpty(readmission)?sem9Fixed:sem9ReadmissionFixed);

					studentDues.setS10fxd(ObjectUtils.isEmpty(readmission)?sem10Fixed:sem10ReadmissionFixed);
					studentDues.setS11fxd(ObjectUtils.isEmpty(readmission)?sem11Fixed:sem11ReadmissionFixed);
					studentDues.setS12fxd(ObjectUtils.isEmpty(readmission)?sem11Fixed:sem11ReadmissionFixed);

					studentDues.setS1adondue(sem1AddOn);

					studentDues.setS2adondue(sem2AddOn);

					studentDues.setS3adondue(sem3AddOn);

					studentDues.setS4adondue(sem4AddOn);

					studentDues.setS5adondue(sem5AddOn);

					studentDues.setS6adondue(sem6AddOn);

					studentDues.setS7adondue(sem7AddOn);

					studentDues.setS8adondue(sem8AddOn);

					studentDues.setS9adondue(sem9AddOn);
					studentDues.setS10adondue(sem10AddOn);

					studentDues.setS11adondue(sem11AddOn);

					studentDues.setS12adondue(sem12AddOn);

					studentDues.setS1due(sem1Due);
					studentDues.setS2due(sem2Due);
					studentDues.setS3due(sem3Due);
					studentDues.setS4due(sem4Due);
					studentDues.setS5due(sem5Due);
					studentDues.setS6due(sem6Due);
					studentDues.setS7due(sem7Due);
					studentDues.setS8due(sem8Due);
					studentDues.setS9due(sem9Due);
					studentDues.setS10due(sem10Due);
					studentDues.setS11due(sem11Due);
					studentDues.setS12due(sem12Due);

					studentDueRepository.saveAndFlush(studentDues);
				});

			}
		} catch (Exception e) {
			System.out.println("Exception in Student dues: " + e.getMessage());
		}

	}

	private Double getUniformPaid(int sem, Integer studentId) {
		return uniformReceiptRepository.getSumOfPaidUniformFee(sem, studentId);
	}

	private Double getAddOnPaid(int sem, Integer studentId) {
			return cmaFeeReceiptRepository.getCmaFeeReceiptForAddOnFeePaidDetails(sem, studentId);		
	}

	private Float getReadmissionAmount(Readmission readmission, Float semFixed, int i) {
		Float due = 0f;
		if (ObjectUtils.isNotEmpty(readmission) && ObjectUtils.isNotEmpty(readmission.getSemOrYear())) {
			int semOrYear = readmission.getSemOrYear();
			if (semOrYear > i) {
				due = 0f;
			} else if (semOrYear == i) {
				due = readmission.getTotalAmount().floatValue();
			} else {
				due = semFixed;
			}
		}
		return due;
	}

}
