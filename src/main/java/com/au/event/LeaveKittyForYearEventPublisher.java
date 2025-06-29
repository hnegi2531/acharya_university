package com.au.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.au.constant.LeaveKittyEnum;
import com.au.dto.EmployeeDetailsForLeavePattern;
import com.au.model.LeaveKitty;
import com.au.model.LeavePattern;
import com.au.model.LeaveType;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.EmployeeTypeRepository;
import com.au.repository.JobTypeRepository;
import com.au.repository.LeaveApplyRepository;
import com.au.repository.LeaveKittyRepository;
import com.au.repository.LeavePatternRepository;
import com.au.repository.LeaveTypeRepository;

@Component
public class LeaveKittyForYearEventPublisher {

	@Autowired
	private LeavePatternRepository leavePatternRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private LeaveTypeRepository leaveTypeRepository;

	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;


	@Autowired
	private LeaveApplyRepository leaveApplyRepository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private LeaveKittyRepository leaveKittyRepository;

	@EventListener
	@Async
	public void handleLeaveKittyForYearEventPublisher(LeaveKittyForYearEvent leaveKittyForYearEvent) {
		try {
			int currentYear = Year.now().getValue();
		
			List<EmployeeDetailsForLeavePattern> employeeDetailsForLeavePattern  = new ArrayList<EmployeeDetailsForLeavePattern>();
            if(leaveKittyForYearEvent.getEmpId() != null)
            {
				/*employeeDetailsForLeavePattern = List.of(employeeDetailsRepository
						            			.getEmployeeDetailsForLeavePatternWithEmployeeId(leaveKittyForYearEvent.getEmpId()));*/
            	 employeeDetailsForLeavePattern = Collections.singletonList(employeeDetailsRepository
                         .getEmployeeDetailsForLeavePatternWithEmployeeId(leaveKittyForYearEvent.getEmpId()));
            }
            else {
				employeeDetailsForLeavePattern = employeeDetailsRepository
						.getEmployeeDetailsForLeavePattern();
			}

			if (ObjectUtils.isNotEmpty(employeeDetailsForLeavePattern)) {
				employeeDetailsForLeavePattern.forEach(elp -> {

					List<LeavePattern> leavePattern = leavePatternRepository
							.getLeavePatternByEmpTypeIdAndJobTypeIdAndSchoolId(elp.getEmpTypeId(), elp.getJobTypeId(),
									elp.getSchoolId(),currentYear);
					String empType = employeeTypeRepository.getEmployeeTypeName(elp.getEmpTypeId());
                    String jobType=jobTypeRepository.getJobTypeName(elp.getJobTypeId());
					leavePattern.forEach(lp -> {
						try {
							System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id());
							LeaveKitty leaveKitty = leaveKittyRepository
									.getByEmpIdAndLeaveId(elp.getEmpId(), lp.getLeave_pattern_id());
							LeaveType leaveType = leaveTypeRepository.fetchLeaveType(lp.getLeave_id());
							System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short());
							
							if (ObjectUtils.isEmpty(leaveKitty)) {
								leaveKitty = new LeaveKitty();
								leaveKitty.setActive(Boolean.TRUE);
							}

							// Wedding leave
							if(LeaveKittyEnum.WEDDING_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handleWL(leaveKitty, leaveType, lp, elp, empType);
							}

							//Maternity leave
							if(LeaveKittyEnum.MATERNITY_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handleML(leaveKitty, leaveType, lp, elp, empType);
							}

                           //Paternity leave
							if(LeaveKittyEnum.PATERNITY_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handlePL(leaveKitty, leaveType, lp, elp, empType);
							}

                         	//Research leave
							if(LeaveKittyEnum.RESEARCH_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handleRL(leaveKitty, leaveType, lp, elp, empType, jobType);
							}

							//Restricted Holiday leave
							if (LeaveKittyEnum.RESTRICTED_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handleRH(leaveKitty, leaveType, lp, elp, empType);
							}
							
							//Vacation leave
							if (LeaveKittyEnum.VACATION_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())) {
								handleVL(leaveKitty, leaveType, lp, elp, empType, jobType);
							}

						} catch (Exception e) {
							// Log the error and continue with the next leave pattern
							System.out.println("Exception occurred for EmpId: " + elp.getEmpId() + ", LeavePatternId: "
									+ lp.getLeave_pattern_id() + ". Error: " + e.getMessage());
						}
					});
				});
			}

		} catch (Exception e) {
			System.out.println("Exception occur:- " + e.getMessage());
		}

	}

	public void handleRH(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {
		if (LeaveKittyEnum.ORR.getCode().equalsIgnoreCase(empType)
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& LeaveKittyEnum.PERMANENT_STATUS == elp.getPermanentStatus()) {
			assignRHToOnRole(leaveKitty, leaveType, lp, elp);
		}
	}

	public void handleRL(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType, String jobType) {
		if (LeaveKittyEnum.ORR.getCode().equalsIgnoreCase(empType)
				&& ObjectUtils.isNotEmpty(jobType)
				&& (LeaveKittyEnum.TEACHING.getCode().equalsIgnoreCase(jobType) || LeaveKittyEnum.TEACHING_ADMIN.getCode().equalsIgnoreCase(jobType))
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& LeaveKittyEnum.PERMANENT_STATUS == elp.getPermanentStatus()) {
			assignRLToOnRole(leaveKitty, leaveType, lp, elp);
		}
	}

	public void handlePL(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {

		if (LeaveKittyEnum.ORR.getCode().equalsIgnoreCase(empType)
				&& ObjectUtils.isNotEmpty(elp.getGender())
				&& LeaveKittyEnum.MALE.getCode().equalsIgnoreCase(Character.toString(elp.getGender()))
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& LeaveKittyEnum.PERMANENT_STATUS == elp.getPermanentStatus()
				&& LeaveKittyEnum.MARRIED.getCode().equalsIgnoreCase(elp.getMartialStatus())
				&& elp.getPaternityStatus() != Boolean.TRUE) {
			assignPLToOnRole(leaveKitty, leaveType, lp, elp);
		}
	}

	public void handleML(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {

		if (LeaveKittyEnum.ORR.getCode().equalsIgnoreCase(empType)
				&& ObjectUtils.isNotEmpty(elp.getGender())
				&& LeaveKittyEnum.FEMALE.getCode().equalsIgnoreCase(Character.toString(elp.getGender()))
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& LeaveKittyEnum.PERMANENT_STATUS == elp.getPermanentStatus()
				&& elp.getMaternityStatus() != Boolean.TRUE) {
			assignMLToOnRole(leaveKitty, leaveType, lp, elp);
		}
	}

	public void handleWL(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {
		if (LeaveKittyEnum.ORR.getCode().equalsIgnoreCase(empType)
				&& ObjectUtils.isNotEmpty(elp.getMartialStatus())
				&& LeaveKittyEnum.UN_MARRIED.getCode().equalsIgnoreCase(elp.getMartialStatus())
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& LeaveKittyEnum.PERMANENT_STATUS == elp.getPermanentStatus()) {
			assignWLToOnRole(leaveKitty, leaveType, lp, elp);
		}
	}

	private void assignRHToOnRole(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {

				Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);

				Integer accumulate = (leaveApplyCount != 0)? 2 - leaveApplyCount  : 2;
				leaveKitty.setAccumulated_count((double)accumulate);

				leaveKitty.setAccumulated_date(new Date());
				leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				leaveKitty.setEmp_id(elp.getEmpId());

				leaveKittyRepository.save(leaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+leaveKitty.getAccumulated_count());

			}

		} catch (ParseException e) {
			e.printStackTrace();
		}


	}

	private void assignRLToOnRole(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {
				
				Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);
				
				Integer accumulate = (leaveApplyCount != 0)? 12 - leaveApplyCount  : 12;
				leaveKitty.setAccumulated_count((double)accumulate);
				
				leaveKitty.setAccumulated_date(new Date());
				leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				leaveKitty.setEmp_id(elp.getEmpId());

				leaveKittyRepository.save(leaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+leaveKitty.getAccumulated_count());
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	private void assignPLToOnRole(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {
				
				leaveKitty.setAccumulated_count((double)lp.getLeave_days_permit());
				leaveKitty.setAccumulated_date(new Date());
				leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				leaveKitty.setEmp_id(elp.getEmpId());

				leaveKittyRepository.save(leaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+leaveKitty.getAccumulated_count());
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	private void assignMLToOnRole(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {
				
					leaveKitty.setAccumulated_count((double)lp.getLeave_days_permit());
				leaveKitty.setAccumulated_date(new Date());
				leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				leaveKitty.setEmp_id(elp.getEmpId());

				leaveKittyRepository.save(leaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+leaveKitty.getAccumulated_count());
				
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	private void assignWLToOnRole(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp,
			EmployeeDetailsForLeavePattern elp) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 20);
			Date date = calendar.getTime();
			int currentYear = Year.now().getValue();

			String year = String.valueOf(currentYear);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
			if (dj.before(date)) {
				leaveKitty.setAccumulated_count((double)lp.getLeave_days_permit());
				leaveKitty.setAccumulated_date(new Date());
				leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
				leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
				leaveKitty.setEmp_id(elp.getEmpId());
				leaveKittyRepository.save(leaveKitty);
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+leaveKitty.getAccumulated_count());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


    public void handleVL(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType, String jobType) {
		if (LeaveKittyEnum.ORR.getCode().equalsIgnoreCase(empType)
				&& ObjectUtils.isNotEmpty(jobType)
				&& (LeaveKittyEnum.TEACHING.getCode().equalsIgnoreCase(jobType))
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& LeaveKittyEnum.PERMANENT_STATUS == elp.getPermanentStatus()) {
			assignVLToOnRole(leaveKitty, leaveType, lp, elp);
		}
    }

	private void assignVLToOnRole(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp) {
		{
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, 20);
				Date date = calendar.getTime();
				int currentYear = Year.now().getValue();

				String year = String.valueOf(currentYear);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

				Date dj = simpleDateFormat.parse(elp.getDateOfJoining());
				if (dj.before(date)) {

					Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);
					Integer accumulate = (leaveApplyCount != 0)? 14 - leaveApplyCount  : 14;
					leaveKitty.setAccumulated_count((double)accumulate);

					leaveKitty.setAccumulated_date(new Date());
					leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
					leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
					leaveKitty.setEmp_id(elp.getEmpId());

					leaveKittyRepository.save(leaveKitty);
					System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short()+" "+leaveKitty.getAccumulated_count());

				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}
}
